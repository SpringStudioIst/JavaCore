package com.angcyo.selenium.auto

import com.angcyo.library.ex.toElapsedTime
import com.angcyo.log.L
import com.angcyo.selenium.DriverWebElement
import com.angcyo.selenium.auto.action.*
import com.angcyo.selenium.bean.ActionBean
import com.angcyo.selenium.bean.TaskBean
import com.angcyo.selenium.js.exeJs
import com.angcyo.selenium.parse.AutoParse
import com.angcyo.selenium.parse.HandleResult
import com.angcyo.selenium.toPx
import javafx.beans.property.ObjectPropertyBase
import javafx.beans.property.SimpleObjectProperty
import org.openqa.selenium.PageLoadStrategy
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.edge.EdgeDriver
import org.openqa.selenium.edge.EdgeOptions
import org.openqa.selenium.remote.RemoteWebDriver

/** 控制[org.openqa.selenium.WebElement]
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/24
 */
open class BaseControl {

    /**驱动属性, 支持监听*/
    val driverProperty: ObjectPropertyBase<WebDriver?> = SimpleObjectProperty<WebDriver?>()

    val driver: WebDriver?
        get() = driverProperty.get()

    /**提示输出*/
    var tipAction: ((ControlTip) -> Unit)? = {
        L.i(it.title, it.des ?: "")
    }

    /**日志输出*/
    var logAction: ((String) -> Unit)? = {
        L.i(it)
    }

    /**当前运行的任务*/
    var _currentTaskBean: TaskBean? = null

    /**[ActionBean]执行器*/
    var actionRunSchedule: ActionRunSchedule = ActionRunSchedule(this)

    /**保存[ActionBean]执行的结果.
     * key = id
     * value = 成功or失败*/
    val actionResultMap = hashMapOf<Long, Boolean>()

    val _autoParse = AutoParse(this)

    //具体的执行操作
    val registerActionList = mutableListOf<BaseAction>()

    init {
        registerActionList.add(BackAction())
        registerActionList.add(ClickAction())
        registerActionList.add(CodeAction())
        registerActionList.add(ForwardAction())
        registerActionList.add(GetAttrAction())
        registerActionList.add(GetCssAction())
        registerActionList.add(GetTextAction())
        registerActionList.add(InputAction())
        registerActionList.add(RefreshAction())
        registerActionList.add(ToAction())
        registerActionList.add(RemoveAttrAction())
        registerActionList.add(SetAttrAction())
    }

    //初始化驱动
    fun _initDriver() {
        if (driver == null) {
            //驱动程序配置
            val options = EdgeOptions()
            _currentTaskBean?.config?.pageLoadStrategy?.let {
                PageLoadStrategy.fromString(it)?.let {
                    options.setPageLoadStrategy(it)
                }
            }
            driverProperty.set(EdgeDriver(options))
        }
    }

    open fun startInner(task: TaskBean) {
        _currentTaskBean = task
    }

    /**执行动作, [actionBean]无法处理时, 则交给[otherActionList]处理*/
    open fun runAction(actionBean: ActionBean, otherActionList: List<ActionBean>?) {
        var handleActionResult: HandleResult? = null
        if (actionBean.check == null) {
            //无效的check, 直接操作浏览器Driver
            handleActionResult = _autoParse.handle(this, actionBean.check?.handle)
        } else {
            val event = actionBean.check?.event
            val eventElementList =
                if (event == null) listOf(DriverWebElement()) else _autoParse.parseSelector(this, event)
            showElementTip(eventElementList)
            if (eventElementList.isEmpty()) {
                logAction?.invoke("[event]未匹配到元素:${actionBean.check?.event}")
                //未找到元素
                val handleResult = _autoParse.handle(this, actionBean.check?.other)
                if (!handleResult.success) {
                    //还是未处理
                    otherActionList?.forEach {
                        runAction(actionBean, null)
                    }
                }
            } else {
                //找到了目标元素
                logAction?.invoke(buildString {
                    appendLine("[event]匹配到元素(${eventElementList.size})↓")
                    eventElementList.forEach {
                        appendLine(it.toStr())
                    }
                })
                handleActionResult = _autoParse.handle(this, actionBean.check?.handle, eventElementList)
            }
        }

        //退出框架
        actionBean.check?.event?.forEach {
            if (it.frame?._frame != null) {
                logAction?.invoke("退出iframe")
                it.frame?._frame = null
                driver?.switchTo()?.defaultContent()
            }
        }

        //保存执行结果.
        actionResultMap[actionBean.actionId] = handleActionResult?.success ?: false

        //处理结果
        handleActionResult?.apply {
            if (success) {
                //处理成功
                //showElementTip(elementList)
                actionRunSchedule.next(actionBean)
            } else {
                //未处理成功
            }
        }
    }

    /**运行结束*/
    open fun finish() {
        tipAction?.invoke(ControlTip().apply {
            title = "${_currentTaskBean?.title}${actionRunSchedule.indexTip()} 执行完成!"
            des = "耗时:${actionRunSchedule.duration().toElapsedTime(pattern = intArrayOf(-1, 1, 1))}"
        })
    }

    /**调用js, 显示选择的元素提示*/
    fun showElementTip(list: List<WebElement>?) {
        (driver as? RemoteWebDriver)?.let { web ->
            //先移除所有之前的提示
            web.exeJs("remove_all_tip.js")
            //再添加新的提示
            list?.forEach {
                try {
                    if (it.isValidElement()) {
                        val rect = it.rect
                        web.exeJs("append_tip.js", rect.x.toPx(), rect.y.toPx(), rect.width.toPx(), rect.height.toPx())
                    }
                } catch (e: Exception) {
                    L.w(e)
                }
            }
        }
    }

    /**执行动作*/
    fun handleAction(element: WebElement, action: String?): HandleResult {
        val result = HandleResult()
        val elementList = mutableListOf<WebElement>()
        registerActionList.forEach {
            if (it.interceptAction(this, action)) {
                it.runAction(this, element, action ?: "").apply {
                    result.success = success || result.success
                    if (success) {
                        //把处理成功的元素收集起来
                        if (!elementList.containsAll(this.elementList ?: emptyList())) {
                            elementList.addAll(this.elementList!!)
                        }
                    }
                }
            }
        }
        if (result.success) {
            result.elementList = elementList
        }
        return result
    }
}