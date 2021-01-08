package com.angcyo.javafx.ui

import com.angcyo.library.ex.simpleClassName
import javafx.event.ActionEvent
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TitledPane
import javafx.scene.layout.*

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
 * https://docs.oracle.com/javase/8/javafx/api/javafx/scene/layout/GridPane.html
 * */
fun gridPane(rowCount: Int = -1, columnCount: Int = -1, action: GridPane.() -> Unit): GridPane {
    return GridPane().apply {
        //children.addAll()
        for (rowIndex in 0 until rowCount) {
            val row = RowConstraints()
            //纵向拉伸模式
            row.vgrow = Priority.SOMETIMES
            rowConstraints.add(row)
        }
        for (columnIndex in 0 until columnCount) {
            val column = ColumnConstraints()
            //横向拉伸模式
            column.hgrow = Priority.SOMETIMES
            columnConstraints.add(column)
        }
        action()
    }
}

/**设置[Node]在网格中行列的索引
 * 从0开始的索引*/
fun Node.setGridIndex(rowIndex: Int, columnIndex: Int) {
    GridPane.setRowIndex(this, rowIndex)
    GridPane.setColumnIndex(this, columnIndex)
    //GridPane.setConstraints(this, columnIndex, rowIndex)
}

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

/**
 * https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Label.html
 * */
fun label(action: Label.() -> Unit): Label {
    return Label().apply {
        id = this.simpleClassName()
        action()
    }
}

fun label(text: String): Label {
    return label {
        this.text = text
    }
}