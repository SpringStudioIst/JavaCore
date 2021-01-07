package com.angcyo.javafx.base.ex

import com.angcyo.http.rx.runRx
import com.angcyo.javafx.BaseApp
import com.angcyo.javafx.base.BaseController
import com.angcyo.javafx.base.CancelRunnable
import com.angcyo.library.ex.getResource
import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import javafx.stage.Window
import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.io.File
import java.net.URI


/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/28
 */

/**在主线程运行, 通常用来在子线程调用修改UI
 * Not on FX application thread; currentThread = Thread-3
 * */
fun onMain(action: () -> Unit): CancelRunnable {
    val cancelRunnable = CancelRunnable(action)
    if (isFxApplicationThread()) {
        cancelRunnable.run()
    } else {
        Platform.runLater(cancelRunnable)
    }
    return cancelRunnable
}

fun onBack(action: () -> Unit): CancelRunnable {
    val cancelRunnable = CancelRunnable(action)
    if (isFxApplicationThread()) {
        runRx({ cancelRunnable.run();true })
    } else {
        cancelRunnable.run()
    }
    return cancelRunnable
}

fun onLater(action: () -> Unit): CancelRunnable {
    val cancelRunnable = CancelRunnable(action)
    Platform.runLater(cancelRunnable)
    return cancelRunnable
}

/**[time]需要延迟的毫秒*/
fun onDelay(time: Long = 160, action: () -> Unit): CancelRunnable {
    val cancelRunnable = CancelRunnable(action)
    onBack {
        Thread.sleep(time)
        Platform.runLater(cancelRunnable)
    }
    return cancelRunnable
}

/**是否是主进程
 * JavaFX Application Thread
 * */
fun isFxApplicationThread() = Platform.isFxApplicationThread()

/**获取控制器*/
inline fun <reified Controller : BaseController> ctl(): Controller? =
    BaseController.controllerHolder[Controller::class.java] as? Controller

inline fun <reified Controller : BaseController> controller(): Controller? = ctl()

/**通过[Node]获取所在的舞台*/
fun Node.getStage() = scene?.window as? Stage

fun String.toSelector() = if (startsWith("#")) this else "#$this"

/**[Node], 只能查找到子child, 多级查不到.*/
fun <T> Node?.findByCss(selector: String): T? = this?.lookup(selector.toSelector()) as? T?
fun <T> Node?.findByCssAll(selector: String): Set<T>? = this?.lookupAll(selector.toSelector()) as? Set<T>?

fun <T> Node?.find(selector: String): T? = this?.findByCss(selector)
fun <T> Node?.findAll(selector: String): Set<T>? = this?.findByCssAll(selector)

/**通过css选择器, 选择场景中的[Node]*/
fun <T> Scene.findByCss(selector: String): T? = root?.findByCss(selector)
fun <T> Scene.findByCssAll(selector: String): Set<T>? = root?.findByCssAll(selector)

/**[Node], 支持多级查询.*/
fun <T> Window.findByCss(selector: String) = scene?.findByCss(selector) as? T?
fun <T> Window.find(selector: String): T? = findByCss(selector)
fun <T> Window.findAll(selector: String): Set<T>? = scene?.findByCssAll(selector)

/**[javafx.scene.image.Image]*/
fun Any.getImageFx(imageName: String): Image? {
    return try {
        Image(getResource(imageName).toString())
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**使用默认浏览器打开[url]*/
fun openUrl(url: String) {
    Desktop.getDesktop().browse(URI(url))
}

fun showDocument(uri: String) {
    BaseApp.app.showDocument(uri)
}

fun showDocument(file: File) {
    BaseApp.app.showDocument(file)
}

/**使用系统默认的文件打开方式打开文件*/
fun Application.showDocument(uri: String) {
    hostServices.showDocument(uri)
}

fun Application.showDocument(file: File) {
    showDocument(file.toURI().toString())
}

/**复制到系统剪切板*/
fun String.copy() {
    val stringSelection = StringSelection(this)
    val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
    clipboard.setContents(stringSelection, null)
}