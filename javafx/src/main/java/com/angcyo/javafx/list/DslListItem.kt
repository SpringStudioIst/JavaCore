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

    var itemData: Any? = null

    var itemTag: String? = null

    //<editor-fold desc="界面相关">

    var createItem: (itemCell: DslListItemCell) -> Node? = {
        val result = onCreateItem(it)
        bindItem(it, result)
        result
    }

    var bindItem: (itemCell: DslListItemCell, rootNode: Node?) -> Unit = { itemCell, rootNode ->
        onBindItem(itemCell, rootNode)
        bindItemOverride(itemCell, rootNode)
    }

    var bindItemOverride: (itemCell: DslListItemCell, rootNode: Node?) -> Unit = { itemCell, rootNode ->
        onBindItemOverride(itemCell, rootNode)
    }

    open fun onCreateItem(itemCell: DslListItemCell): Node? {
        return null
    }

    open fun onBindItem(itemCell: DslListItemCell, rootNode: Node?) {

    }

    open fun onBindItemOverride(itemCell: DslListItemCell, rootNode: Node?) {

    }

    //</editor-fold desc="界面相关">

}