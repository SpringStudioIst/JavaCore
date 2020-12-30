package com.angcyo.selenium.auto

import com.angcyo.log.L
import com.angcyo.selenium.bean.ActionBean
import com.angcyo.selenium.bean.TaskBean
import com.angcyo.selenium.parse.CheckParse
import org.openqa.selenium.WebDriver

/** 控制[org.openqa.selenium.WebElement]
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/24
 */
open class BaseControl {
    /**驱动*/
    var driver: WebDriver? = null

    /**日志输出*/
    var logAction: ((ControlTip) -> Unit)? = {
        L.i(it.title, it.des)
    }

    /**当前运行的任务*/
    var _currentTaskBean: TaskBean? = null

    /**[ActionBean]执行器*/
    var actionRunManager: ActionRunManager = ActionRunManager(this)

    val _checkParse = CheckParse()

    open fun startInner(task: TaskBean) {
        _currentTaskBean = task
    }

    /**执行动作, [actionBean]无法处理时, 则交给[otherActionList]处理*/
    open fun runAction(actionBean: ActionBean, otherActionList: List<ActionBean>?) {
        if (actionBean.check == null) {
            //无效的check, no op
        } else {
            val elementList = _checkParse.parse(this, actionBean.check?.event)
            if (elementList.isEmpty()) {
                //未找到元素
                val handleResult = _checkParse.handle(this, actionBean.check?.other)
                if (!handleResult.success) {
                    //还是未处理
                    otherActionList?.forEach {
                        runAction(actionBean, null)
                    }
                }
            } else {
                //找到了目标元素
                val handleResult = _checkParse.handle(this, actionBean.check?.handle)
                if (!handleResult.success) {
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
        logAction?.invoke(ControlTip().apply {
            title = "${_currentTaskBean?.title}${actionRunManager.indexTip()} 执行完成!"
            des = "耗时:${actionRunManager.duration()}ms"
        })
    }
}