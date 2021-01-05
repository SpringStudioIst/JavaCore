package com.angcyo.selenium.auto

import com.angcyo.library.ex.sleep
import com.angcyo.log.L
import com.angcyo.selenium.auto.AutoControl.Companion.STATE_PAUSE
import com.angcyo.selenium.auto.AutoControl.Companion.STATE_RUNNING
import com.angcyo.selenium.bean.ActionBean
import com.angcyo.selenium.bean.TaskBean
import javafx.beans.property.IntegerPropertyBase

/**
 * 自动辅助操作控制类
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/29
 */
class AutoControl : BaseControl(), Runnable {

    /**控制器的状态*/
    var _controlState: IntegerPropertyBase = object : IntegerPropertyBase(STATE_NORMAL) {
        override fun getBean(): Any {
            return this@AutoControl
        }

        override fun getName(): String {
            return "_controlState"
        }
    }

    //线程
    var _currentThread: Thread? = null

    companion object {
        const val STATE_NORMAL = 0
        const val STATE_RUNNING = 1
        const val STATE_FINISH = 2
        const val STATE_PAUSE = 9
        const val STATE_STOP = 10
    }

    /**启动控制器*/
    fun start(task: TaskBean): Boolean {
        return if (_currentTaskBean != null && _controlState.get().isControlStart()) {
            tipAction?.invoke(ControlTip().apply {
                title = "已有任务在运行"
                des = _currentTaskBean?.title
            })
            false
        } /*else if (task.actionList.isNullOrEmpty()) {
            tipAction?.invoke(ControlTip().apply {
                title = "无操作需要执行"
            })
            false
        }*/ else {
            _start(task)
            true
        }
    }

    //正式开始
    fun _start(task: TaskBean, restart: Boolean = true) {
        _currentThread?.interrupt()
        _controlState.set(STATE_RUNNING)
        if (restart) {
            actionRunSchedule.reset()
        }
        startInner(task)
        tipAction?.invoke(ControlTip().apply {
            title = "请稍等..."
            des = "即将执行:${_currentTaskBean?.title}(${actionRunSchedule.actionSize()})"
        })
        _currentThread = Thread(this).apply {
            start()
        }
    }

    /**停止控制器, 释放资源*/
    fun stop() {
        _currentThread?.interrupt()
        _controlState.set(STATE_STOP)
        driver?.quit()
        driverProperty.set(null)
    }

    /**暂停*/
    fun pause() {
        if (_controlState.get() == STATE_RUNNING) {
            _controlState.set(STATE_PAUSE)
        }
    }

    /**恢复*/
    fun resume() {
        val state = _controlState.get()
        when {
            //如果只是暂停了
            state == STATE_PAUSE -> _controlState.set(STATE_RUNNING)
            //线程都退出了
            state.isControlEnd() -> {
                _currentTaskBean?.let {
                    _start(it, false)
                }
            }
        }
    }

    override fun finish() {
        super.finish()
        _controlState.set(STATE_FINISH)
    }

    /**线程运行*/
    override fun run() {
        _initDriver()
        while (!_controlState.get().isControlEnd()) {
            if (_controlState.get() == STATE_RUNNING) {
                //正在运行
                try {
                    actionRunSchedule.schedule()
                } catch (e: Exception) {
                    L.e("异常:$e")
                    e.printStackTrace()
                }
                if (actionRunSchedule.nextActionBean != null) {
                    //还有下一个需要执行, 则不延迟
                    sleep(actionRunSchedule._nextTime)
                }
            } else {
                //no op
                sleep()
            }
        }
    }
}

/**获取下一个有效的[ActionBean]*/
fun List<ActionBean>.nextAction(index: Int = 0): ActionBean? {
    var result: ActionBean? = null
    for (i in indices) {
        if (i >= index) {
            val bean = getOrNull(i)
            if (bean?.enable == true) {
                result = bean
                break
            }
        }
    }
    return result
}

/**控制器是否已经开始*/
fun Number.isControlStart() = this == STATE_RUNNING || this == STATE_PAUSE

fun Number.isControlPause() = this == STATE_PAUSE

/**控制器没有在运行*/
fun Number.isControlEnd() = this == AutoControl.STATE_STOP || this == AutoControl.STATE_FINISH