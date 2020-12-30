package com.angcyo.javafx.ui

import com.angcyo.javafx.base.ex.getStage
import javafx.scene.Node
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.image.Image
import java.util.*

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/30
 */

class DslAlert {

    /**窗口类型*/
    var alertType: Alert.AlertType = Alert.AlertType.INFORMATION

    /**标题*/
    var title: String? = "提示"

    /**头部文本*/
    var headerText: String? = null

    /**内容文本*/
    var contentText: String? = null

    /**提示[Node]*/
    var tipIconNode: Node? = null

    /**窗口上的图标*/
    val icons = mutableListOf<Image>()

    /**配置回调*/
    var configDialog: (Alert) -> Unit = {}

    fun show(): Optional<ButtonType>? {
        val dialog = Alert(alertType)
        dialog.title = title
        dialog.headerText = headerText
        dialog.contentText = contentText
        //提示图标
        tipIconNode?.let {
            //ImageView(getResource("logo.png").toString())
            dialog.graphic = it
        }
        //对话框图标
        if (icons.isNotEmpty()) {
            dialog.dialogPane.getStage()?.icons?.addAll(icons)
        }
        configDialog(dialog)
        return dialog.showAndWait()
    }
}

/**https://blog.csdn.net/qq_26954773/article/details/78215554*/
fun dslAlert(action: DslAlert.() -> Unit): Optional<ButtonType>? {
    val alert = DslAlert()
    alert.action()
    return alert.show()
}