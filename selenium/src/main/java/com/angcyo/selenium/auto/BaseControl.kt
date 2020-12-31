package com.angcyo.selenium.auto

import com.angcyo.log.L
import com.angcyo.selenium.bean.ActionBean
import com.angcyo.selenium.bean.TaskBean
import com.angcyo.selenium.js.exeJs
import com.angcyo.selenium.parse.AutoParse
import com.angcyo.selenium.toPx
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.RemoteWebDriver

/** 控制[org.openqa.selenium.WebElement]
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/24
 */
open class BaseControl {
    /**驱动*/
    var driver: WebDriver? = null

    /**提示输出*/
    var tipAction: ((ControlTip) -> Unit)? = {
        L.i(it.title, it.des)
    }

    /**日志输出*/
    var logAction: ((String) -> Unit)? = {
        L.i(it)
    }

    /**当前运行的任务*/
    var _currentTaskBean: TaskBean? = null

    /**[ActionBean]执行器*/
    var actionRunManager: ActionRunManager = ActionRunManager(this)

    val _autoParse = AutoParse()

    open fun startInner(task: TaskBean) {
        _currentTaskBean = task
    }

    /**执行动作, [actionBean]无法处理时, 则交给[otherActionList]处理*/
    open fun runAction(actionBean: ActionBean, otherActionList: List<ActionBean>?) {
        if (actionBean.check == null) {
            //无效的check, no op
        } else {
            val elementList = _autoParse.parseSelector(this, actionBean.check?.event)
            showElementTip(elementList)
            if (elementList.isEmpty()) {
                logAction?.invoke("[event]未匹配到元素")

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
                    appendLine("[event]匹配到元素:")
                    elementList.forEach {
                        appendLine(it.toStr())
                    }
                })
                val handleResult = _autoParse.handle(this, actionBean.check?.handle, elementList)
                if (handleResult.success) {
                    //处理成功
                    actionRunManager.next()
                } else {
                    //未处理成功
                }
            }
        }
    }

    /**运行结束*/
    open fun finish() {
        tipAction?.invoke(ControlTip().apply {
            title = "${_currentTaskBean?.title}${actionRunManager.indexTip()} 执行完成!"
            des = "耗时:${actionRunManager.duration()}ms"
        })
    }

    /**调用js, 显示选择的元素提示*/
    fun showElementTip(list: List<WebElement>?) {
        list?.forEach {
            val rect = it.rect
            (driver as? RemoteWebDriver)?.exeJs(
                "append_tip.js",
                rect.x.toPx(), rect.y.toPx(), rect.width.toPx(), rect.height.toPx()
            )
        }
    }
}