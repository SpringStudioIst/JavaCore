package com.angcyo.javafx.list

import javafx.scene.Node
import javafx.scene.control.ListView

/**
 * https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/ListView.html
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2021/01/06
 */
open class DslListItem {

    var listView: ListView<DslListItem>? = null

    //<editor-fold desc="界面相关">

    var bindItem: (itemCell: DslListItemCell) -> Node? = {
        val result = onBindItem(it)
        bindItemOverride(it)
        result
    }

    var bindItemOverride: (itemCell: DslListItemCell) -> Unit = {
        onBindItemOverride(it)
    }

    open fun onBindItem(itemCell: DslListItemCell): Node? {
        return null
    }

    open fun onBindItemOverride(itemCell: DslListItemCell) {

    }

    //</editor-fold desc="界面相关">

}