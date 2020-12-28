package com.angcyo.javafx.base.ex

import com.angcyo.http.rx.runRx
import com.angcyo.javafx.base.BaseController
import javafx.application.Platform

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/28
 */

/**在主线程运行, 通常用来在子线程调用修改UI
 * Not on FX application thread; currentThread = Thread-3
 * */
fun onMain(action: () -> Unit) {
    if (isFxApplicationThread()) {
        action()
    } else {
        Platform.runLater { action() }
    }
}

fun onBack(action: () -> Unit) {
    if (isFxApplicationThread()) {
        runRx({ action();true })
    } else {
        action()
    }
}

fun onLater(action: () -> Unit) {
    Platform.runLater { action() }
}

/**[time]需要延迟的毫秒*/
fun onDelay(time: Long, action: () -> Unit) {
    onBack {
        Thread.sleep(time)
        onLater(action)
    }
}

/**是否是主进程
 * JavaFX Application Thread
 * */
fun isFxApplicationThread() = Platform.isFxApplicationThread()

/**获取控制器*/
inline fun <reified Controller : BaseController> ctl(): Controller? =
    BaseController.controllerHolder[Controller::class.java] as? Controller