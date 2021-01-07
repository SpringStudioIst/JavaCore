package com.angcyo.javafx.ui

import com.angcyo.library.ex.simpleClassName
import javafx.event.ActionEvent
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.TitledPane
import javafx.scene.layout.FlowPane

/**
 * https://docs.oracle.com/javase/8/javafx/api/overview-summary.html
 *
 * https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/package-summary.html
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2021/01/07
 */

/**
 * https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/TitledPane.html
 * */
fun titledPane(action: TitledPane.() -> Node?): TitledPane {
    return TitledPane().apply {
        id = this.simpleClassName()
        text = id
        content = action() //设置内容
    }
}

/**
 * https://docs.oracle.com/javase/8/javafx/api/javafx/scene/layout/FlowPane.html
 */
fun flowPane(action: FlowPane.() -> List<Node>?): FlowPane {
    return FlowPane().apply {
        id = this.simpleClassName()
        vgap = 5.0
        hgap = 5.0
        prefWrapLength = 0.0
        action()?.forEach {
            children.clear()
            children.add(it)
        }
    }
}

/**
 * https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Button.html
 * */
fun button(action: Button.() -> Unit): Button {
    return Button().apply {
        id = this.simpleClassName()
        //isMouseTransparent = true //鼠标穿透
        action()
    }
}

fun button(text: String, clickAction: (ActionEvent) -> Unit): Button {
    return button {
        this.text = text
        setOnAction { clickAction(it) }
    }
}