package com.angcyo.selenium.auto

import com.angcyo.library.ex.nowTime
import com.angcyo.selenium.auto.action.StartAction
import com.angcyo.selenium.bean.ActionBean

/**
 * 用于执行[ActionBean]
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/30
 */
class ActionRunManager(val control: BaseControl) {

    /**当前执行到哪一步*/
    var actionIndex = -1

    /**临时需要执行的[ActionBean]*/
    val tempActionList = mutableListOf<ActionBean>()

    /**下一个[ActionBean]需要执行的时间间隔*/
    var _nextTime: Long = 0

    var _nextActionIndex = -1

    val nextActionBean: ActionBean?
        get() = tempActionList.firstOrNull() ?: control._currentTaskBean?.actionList?.getOrNull(_nextActionIndex)

    /**执行开始的时间, 毫秒*/
    var _startTime: Long = 0

    /**执行结束的时间, 毫秒*/
    var _endTime: Long = 0

    /**开始执行[ActionBean]*/
    fun run() {
        val driver = control.driver
        val task = control._currentTaskBean

        if (driver != null && task != null) {
            if (actionIndex < 0) {
                _nextActionIndex = 0
                _startTime = nowTime()
                StartAction().runAction(control)
            } else {
                //_nextActionIndex = _currentActionIndex + 1
                val nextAction = nextActionBean
                if (nextAction == null) {
                    //执行结束
                    actionIndex = control._currentTaskBean?.actionList?.size ?: actionIndex
                    _endTime = nowTime()
                    control.finish()
                } else {
                    if (nextAction.check == null) {
                        next()
                    } else {
                        control.runAction(nextAction, control._currentTaskBean?.backActionList)
                    }
                }
            }

            //...
            if (nextActionBean != null) {
                actionIndex = _nextActionIndex
                _nextTime = nextActionTime()
                showControlTip()
            }
        }
    }

    /**执行下一个*/
    fun next() {
        _nextActionIndex = actionIndex + 1
    }

    fun showControlTip() {
        val title = "${control._currentTaskBean?.title}${indexTip()}"
        val des = nextActionBean?.title
        control.tipAction?.invoke(ControlTip(title, des, _nextTime))
    }

    /**索引指示提示文本*/
    fun indexTip(): String {
        return if (actionIndex >= 0) {
            "(${actionIndex}/${control._currentTaskBean?.actionList?.size})"
        } else {
            ""
        }
    }

    /**下一个[ActionBean]需要执行的时间间隔*/
    fun nextActionTime(): Long {
        return if (tempActionList.isNullOrEmpty()) {
            control._autoParse.parseTime(nextActionBean?.start)
        } else {
            control._autoParse.parseTime(tempActionList.first().start)
        }
    }

    /**获取总共运行时长*/
    fun duration(): Long = _endTime - _startTime
}