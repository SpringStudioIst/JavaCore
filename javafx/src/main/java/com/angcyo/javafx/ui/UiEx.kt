package com.angcyo.javafx.ui

import com.angcyo.core.component.file.writeTo
import com.angcyo.javafx.base.ex.getImageFx
import javafx.scene.Node
import javafx.scene.control.TabPane
import javafx.scene.control.TextInputControl
import javafx.scene.image.ImageView
import javafx.scene.layout.Region
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