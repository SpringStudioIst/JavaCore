package com.angcyo.selenium.auto

import com.angcyo.library.ex.nowTime
import com.angcyo.selenium.PlaceholderWebElement
import com.angcyo.selenium.auto.action.StartAction
import com.angcyo.selenium.bean.ActionBean

/**
 * 用于执行[ActionBean]
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/30
 */
class ActionRunSchedule(val control: BaseControl) {

    /**当前执行到哪一步*/
    var actionIndex = -1

    /**临时需要执行的[ActionBean]*/
    val tempActionList = mutableListOf<ActionBean>()

    /**下一个[ActionBean]需要执行的时间间隔*/
    var _nextTime: Long = 0

    var _nextActionIndex = -1

    val nextActionBean: ActionBean?
        get() = tempActionList.nextAction() ?: control._currentTaskBean?.actionList?.nextAction(_nextActionIndex)

    /**执行开始的时间, 毫秒*/
    var _startTime: Long = 0

    /**执行结束的时间, 毫秒*/
    var _endTime: Long = 0

    /**重置状态*/
    fun reset() {
        _startTime = 0
        _endTime = 0
        _nextTime = 0
        actionIndex = -1
        _nextActionIndex = -1
    }

    /**开始调度执行[ActionBean]*/
    fun schedule() {
        val driver = control.driver
        val task = control._currentTaskBean

        if (driver != null && task != null) {
            if (actionIndex < 0) {
                _nextActionIndex = 0
                _startTime = nowTime()
                StartAction().runAction(control, PlaceholderWebElement(), "start")
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
                        next(nextAction)
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
    fun next(currentRunActionBean: ActionBean?) {
        var nextIndex = actionIndex + 1
        currentRunActionBean?.let {
            //从临时action中移除已经执行完成的action,如果有
            if (tempActionList.firstOrNull() == it) {
                tempActionList.removeFirstOrNull()
                nextIndex = actionIndex
            }
        }
        _nextActionIndex = nextIndex
    }

    fun addNextAction(bean: ActionBean) {
        if (!tempActionList.contains(bean)) {
            tempActionList.add(bean)
        }
    }

    /**总共需要执行的[ActionBean]的数量*/
    fun actionSize() = control._currentTaskBean?.actionList?.sumBy { if (it.enable) 1 else 0 }
        ?: 0 + tempActionList.sumBy { if (it.enable) 1 else 0 }

    fun showControlTip() {
        val title = "${control._currentTaskBean?.title}${indexTip()}"
        val des = nextActionBean?.title
        control.tipAction?.invoke(ControlTip(title, des, _nextTime))
    }

    /**索引指示提示文本*/
    fun indexTip(): String {
        return if (actionIndex >= 0) {
            "(${actionIndex}/${actionSize()})"
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