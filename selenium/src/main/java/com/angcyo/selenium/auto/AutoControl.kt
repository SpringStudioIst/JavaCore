package com.angcyo.selenium.auto

import com.angcyo.library.ex.sleep
import com.angcyo.selenium.auto.AutoControl.Companion.STATE_RUNNING
import com.angcyo.selenium.bean.TaskBean
import javafx.beans.property.IntegerPropertyBase
import org.openqa.selenium.PageLoadStrategy
import org.openqa.selenium.edge.EdgeDriver
import org.openqa.selenium.edge.EdgeOptions

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
        } else if (task.actionList.isNullOrEmpty()) {
            tipAction?.invoke(ControlTip().apply {
                title = "无操作需要执行"
            })
            false
        } else {
            _start(task)
            true
        }
    }

    //正式开始
    fun _start(task: TaskBean) {
        _currentThread?.interrupt()
        _controlState.set(STATE_RUNNING)
        startInner(task)
        tipAction?.invoke(ControlTip().apply {
            title = "请稍等..."
            des = "即将执行:${_currentTaskBean?.title}(${_currentTaskBean?.actionList?.size})"
        })
        _currentThread = Thread(this).apply {
            start()
        }
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
            driver = EdgeDriver(options)
        }
    }

    /**停止控制器*/
    fun stop() {
        _currentThread?.interrupt()
        _controlState.set(STATE_STOP)
        driver?.quit()
        driver = null
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
                actionRunManager.run()
                if (actionRunManager.nextActionBean != null) {
                    //还有下一个需要执行, 则不延迟
                    sleep(actionRunManager._nextTime)
                }
            } else {
                //no op
                sleep()
            }
        }
    }
}

/**控制器是否已经开始*/
fun Int.isControlStart() = this == STATE_RUNNING

/**控制器没有在运行*/
fun Int.isControlEnd() = this == AutoControl.STATE_STOP || this == AutoControl.STATE_FINISH