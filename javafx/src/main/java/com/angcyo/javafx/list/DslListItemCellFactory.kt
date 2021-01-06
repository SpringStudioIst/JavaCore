package com.angcyo.javafx.list

import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.util.Callback


/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2021/01/06
 */
class DslListItemCellFactory(val dslListView: DslListView) : Callback<ListView<DslListItem>, ListCell<DslListItem>> {
    override fun call(param: ListView<DslListItem>): ListCell<DslListItem> {
        return DslListItemCell()
    }
}