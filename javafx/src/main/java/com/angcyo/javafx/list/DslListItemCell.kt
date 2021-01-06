package com.angcyo.javafx.list

import javafx.scene.control.ListCell

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2021/01/06
 */
class DslListItemCell : ListCell<DslListItem>() {

    override fun startEdit() {
        super.startEdit()
    }

    override fun cancelEdit() {
        super.cancelEdit()
    }

    override fun commitEdit(newValue: DslListItem?) {
        super.commitEdit(newValue)
    }

    override fun updateItem(item: DslListItem?, empty: Boolean) {
        //L.i("updateItem:$index $empty")
        super.updateItem(item, empty)
        //background = null
        if (empty || item == null) {
            text = null
            graphic = null
        } else {
            graphic = item.bindItem(this)
        }
    }
}