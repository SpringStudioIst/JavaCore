package com.angcyo.javafx.list

import com.angcyo.javafx.base.CancelRunnable
import com.angcyo.javafx.base.ex.onDelay
import javafx.collections.FXCollections

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2021/01/06
 */
class DslListItemFilter {

    var _lastRunnable: CancelRunnable? = null

    /**过滤数据之后, 再显示到界面上*/
    fun filter(listView: DslListView, listItems: List<DslListItem>) {
        _lastRunnable?.isEnd = true
        _lastRunnable = onDelay(16) {
            listView.listView.items = FXCollections.observableList(listItems)
        }
    }
}