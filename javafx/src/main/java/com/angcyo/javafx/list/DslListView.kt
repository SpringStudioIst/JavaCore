package com.angcyo.javafx.list

import javafx.scene.control.ListView

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2021/01/06
 */
class DslListView(val listView: ListView<DslListItem>) {

    companion object {
        /**初始化*/
        fun init(listView: ListView<DslListItem>?, action: DslListView.() -> Unit) {
            if (listView == null) {
                return
            }
            if (listView.cellFactory is DslListItemCellFactory) {
                (listView.cellFactory as DslListItemCellFactory).dslListView.action()
                return
            }
            val dslListView = DslListView(listView)
            listView.cellFactory = DslListItemCellFactory(dslListView)
            dslListView.action()
        }
    }

    var itemFilter = DslListItemFilter()

    val itemsList = mutableListOf<DslListItem>()

    //<editor-fold desc="Item操作">

    fun addLastItem(item: DslListItem) {
        itemsList.add(item)
        updateList()
    }

    fun addLastItem(item: List<DslListItem>) {
        itemsList.addAll(item)
        updateList()
    }

    /**移除一组数据*/
    fun removeItem(list: List<DslListItem>) {
        val listInclude = mutableListOf<DslListItem>()

        list.filterTo(listInclude) {
            itemsList.contains(it)
        }

        if (itemsList.removeAll(listInclude)) {
            updateList()
        }
    }

    /**移除数据*/
    fun removeItem(item: DslListItem) {
        if (itemsList.remove(item)) {
            updateList()
        }
    }

    /**更新列表, 展示数据*/
    fun updateList() {
        itemFilter.filter(this, itemsList)
    }

    //</editor-fold desc="Item操作">

    //<editor-fold desc="操作符重载">

    /**
     * <pre>
     *  DslDemoItem()(){}
     * </pre>
     * */
    operator fun <T : DslListItem> T.invoke(config: T.() -> Unit = {}) {
        this.config()
        addLastItem(this)
    }

    /**
     * <pre>
     * this + DslListItem()
     * </pre>
     * */
    operator fun <T : DslListItem> plus(item: T): DslListView {
        addLastItem(item)
        return this
    }

    operator fun <T : DslListItem> plus(list: List<T>): DslListView {
        addLastItem(list)
        return this
    }

    /**
     * <pre>
     * this - DslListItem()
     * </pre>
     * */
    operator fun <T : DslListItem> minus(item: T): DslListView {
        removeItem(item)
        return this
    }

    operator fun <T : DslListItem> minus(list: List<T>): DslListView {
        removeItem(list)
        return this
    }

//    /**
//     * ```
//     * this[1]
//     * this[index]
//     * this[index, false]
//     *
//     * 负数表示倒数
//     * ```
//     * */
//    operator fun get(
//        index: Int,
//        useFilterList: Boolean = true,
//        reverse: Boolean = true //是否开启反序, 倒数
//    ): DslListItem? {
//        return getDataList(useFilterList).run {
//            if (index >= 0 || !reverse)
//                getOrNull(index)
//            else
//                getOrNull(size + index)
//        }
//    }
//
//    /**
//     * ```
//     * this["tag"]
//     * this["tag", false]
//     * ```
//     * */
//    operator fun get(tag: String?, useFilterList: Boolean = true): DslListItem? {
//        return tag?.run { findItemByTag(tag, useFilterList) }
//    }

    //</editor-fold desc="操作符重载">
}

fun ListView<DslListItem>.renderList(action: DslListView.() -> Unit) {
    DslListView.init(this, action)
}