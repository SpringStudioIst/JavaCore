package com.angcyo.javafx.ui

import com.angcyo.javafx.base.ex.getImageFx
import com.angcyo.javafx.base.ex.getStage
import javafx.scene.Node
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.control.TextInputDialog
import javafx.scene.image.Image
import java.util.*

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/30
 */

class DslDialog {

    //<editor-fold desc="alert">

    /**[Alert]窗口类型*/
    var alertType: Alert.AlertType = Alert.AlertType.INFORMATION

    /**配置回调*/
    var configAlertDialog: (Dialog<ButtonType>) -> Unit = {}

    //</editor-fold desc="alert">

    //<editor-fold desc="input">

    /**[TextInputDialog]默认值*/
    var textInputDefaultValue: String? = null

    var configInputDialog: (Dialog<String>) -> Unit = {}

    //</editor-fold desc="input">

    //<editor-fold desc="dialog">

    /**标题*/
    var title: String? = "提示"

    /**头部文本*/
    var headerText: String? = null

    /**内容文本*/
    var contentText: String? = null

    /**提示[Node]*/
    var tipIconNode: Node? = null

    /**窗口上的图标
     * getImageFx("logo.png")*/
    val icons = mutableListOf<Image>()

    /**扩展内容, 折叠展开的[Node]*/
    var expandableContent: Node? = null

    //</editor-fold desc="dialog">

    fun showAlert(): Optional<ButtonType>? = show(Alert(alertType))

    fun showInput(): Optional<String>? = show(TextInputDialog(textInputDefaultValue))

    fun <R> show(dialog: Dialog<R>): Optional<R>? {
        dialog.title = title
        dialog.headerText = headerText
        dialog.contentText = contentText
        //提示图标
        tipIconNode?.let {
            //ImageView(getResource("logo.png").toString())
            dialog.graphic = it
        }
        //对话框图标
        if (icons.isEmpty()) {
            getImageFx("logo.png")?.let {
                icons.add(it)
            }
        }
        if (icons.isNotEmpty()) {
            dialog.dialogPane.getStage()?.icons?.addAll(icons)
        }
        //扩展内容
        expandableContent?.let {
            dialog.dialogPane.expandableContent = it
        }
        if (dialog is Alert) {
            configAlertDialog(dialog)
        } else if (dialog is TextInputDialog) {
            configInputDialog(dialog)
        }
        return dialog.showAndWait()
    }
}

/**https://blog.csdn.net/qq_26954773/article/details/78215554
<pre>
dslAlert {
alertType = Alert.AlertType.ERROR
icons.add(getImageFx("logo.png")!!)
contentText = "无效的驱动程序!"
}?.let {
if (it.get() == ButtonType.OK) {
}
}
</pre>
 * */

fun dslAlert(action: DslDialog.() -> Unit): Optional<ButtonType>? {
    val alert = DslDialog()
    alert.action()
    return alert.showAlert()
}

fun dslInput(action: DslDialog.() -> Unit): Optional<String>? {
    val alert = DslDialog()
    alert.action()
    return alert.showInput()
}

fun alertError(error: String?, action: DslDialog.() -> Unit = {}) {
    dslAlert {
        alertType = Alert.AlertType.ERROR
        contentText = error
        action()
    }
}