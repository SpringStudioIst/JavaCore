package com.angcyo.javafx.ui

import com.angcyo.core.component.file.writeTo
import com.angcyo.javafx.base.ex.getImageFx
import com.angcyo.library.ex.nowTime
import com.angcyo.log.L
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.image.ImageView
import javafx.scene.input.*
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.Region
import javafx.scene.paint.Paint
import java.io.File

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/30
 */

/**根据[Tab]的名字, 切换tab*/
fun TabPane?.switchByName(targetTabName: String) {
    this?.tabs?.forEachIndexed { index, tab ->
        if (tab.text == targetTabName) {
            selectionModel?.select(index)
        }
    }
}

/**根据[Tab]的ID, 切换tab*/
fun TabPane?.switchById(targetTabId: String) {
    this?.tabs?.forEachIndexed { index, tab ->
        if (tab.id == targetTabId) {
            selectionModel?.select(index)
        }
    }
}

/**通过资源名称, 设置[ImageView]显示的图片
 * https://blog.csdn.net/MAIMIHO/article/details/106367255
 * */
fun ImageView.setImageResource(resImage: String) {
    image = getImageFx(resImage)
}

/**[filePath]文件全路径*/
fun TextInputControl.saveOnTextChanged(filePath: String, append: Boolean = false) {
    saveOnTextChanged(File(filePath), append)
}

/**当文本改变时, 保存到文件[file]*/
fun TextInputControl?.saveOnTextChanged(file: File, append: Boolean = false) {
    this?.textProperty()?.addListener { observable, oldValue, newValue ->
        newValue?.writeTo(file, append)
    }
}

/**监听文本改变*/
fun TextInputControl?.onTextChanged(action: (observable: ObservableValue<out String>?, oldValue: String?, newValue: String?) -> Unit) {
    this?.textProperty()?.addListener { observable, oldValue, newValue ->
        action(observable, oldValue, newValue)
    }
}

fun ComboBoxBase<String>?.onTextChanged(action: (observable: ObservableValue<out String>?, oldValue: String?, newValue: String?) -> Unit) {
    this?.valueProperty()?.addListener { observable, oldValue, newValue ->
        action(observable, oldValue, newValue)
    }
}

/**是否要激活[Node]*/
fun Node?.enable(enable: Boolean = true): Boolean {
    if (this == null) {
        return false
    }
    isDisable = !enable
    return !isDisable
}

/**元素可见*/
fun Node?.visible(visible: Boolean = true): Boolean {
    if (this == null) {
        return false
    }
    isVisible = visible
    isManaged = visible
    return isVisible
}

/**不可见*/
fun Region?.invisible(invisible: Boolean = true) {
    if (this == null) {
        return
    }
    prefHeight = if (invisible) {
        0.0
    } else {
        Region.USE_COMPUTED_SIZE
    }
}

/**鼠标双击回调*/
fun Node?.setOnMouseDoubleClicked(delay: Long = 300, action: (MouseEvent) -> Unit) {
    this?.apply {
        var firstTime = 0L
        val oldClicked = onMouseClicked
        setOnMouseClicked {
            //回调原始的事件处理
            oldClicked?.handle(it)

            //双击检测
            val time = nowTime()
            if ((time - firstTime) <= delay) {
                action(it)
            } else {
                firstTime = time
            }
        }
    }
}

/**背景*/
fun background(color: Paint, radii: CornerRadii = CornerRadii.EMPTY, insets: Insets = Insets.EMPTY): Background {
    return Background(BackgroundFill(color, radii, insets))
}

fun <T> ObservableList<T>.reset(action: ObservableList<T>.() -> Unit) {
    clear()
    action()
}

/**匹配按键
 * [Ctrl+S] it.match(KeyCode.S, KeyCodeCombination.CONTROL_DOWN)
 * */
fun KeyEvent.match(code: KeyCode, vararg modifier: KeyCombination.Modifier): Boolean {
    val combination = KeyCodeCombination(code, *modifier)
    return combination.match(this)
}

fun <T> List<T>?.toObservableList(): ObservableList<T> = FXCollections.observableList(this ?: emptyList())

/**重新设置[ListView]显示的数据*/
fun <T> ListView<T>?.resetItemList(itemList: List<T>?, selectItem: T? = this?.selectionModel?.selectedItem) {
    this?.apply {
        items?.reset {
            addAll(itemList.toObservableList())
        }
        //恢复选中
        selectItem?.let { selectionModel.select(it) }
    }
}

fun <T> ComboBox<T>?.resetItemList(itemList: List<T>?, selectItem: T? = this?.selectionModel?.selectedItem) {
    this?.apply {
        items?.reset {
            addAll(itemList.toObservableList())
        }
        //恢复选中
        selectItem?.let { selectionModel.select(it) }
    }
}

/**列表选中监听*/
fun <T> ListView<T>?.onSelected(action: (selectedIndex: Number, selectedItem: T) -> Unit) {
    this?.apply {
        var selectedItem: T? = null
        selectionModel?.selectedIndexProperty()?.addListener { observable, oldValue, newValue ->
            //后回调
            L.i("selected:[$newValue] [$selectedItem]")
            selectedItem?.let {
                action(newValue, it)
            }
        }
        selectionModel?.selectedItemProperty()?.addListener { observable, oldValue, newValue ->
            //先回调
            //L.i("2...$newValue")
            selectedItem = newValue
        }
    }
}

