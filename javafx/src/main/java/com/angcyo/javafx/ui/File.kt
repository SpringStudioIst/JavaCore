package com.angcyo.javafx.ui

import com.angcyo.javafx.BaseApp
import com.angcyo.log.L
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import javafx.stage.Stage
import java.io.File


/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/29
 */

/**快速获取文件过滤扩展对象*/
fun ext(des: String, vararg ext: String) = ExtensionFilter(des, *ext)

fun exts(vararg ext: ExtensionFilter) = ext

/**
 * 调用系统的选择文件的窗口
 * ExtensionFilter("Text Files", "*.txt"),
 * ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
 * ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
 * ExtensionFilter("All Files", "*.*")
 * */
fun chooserFile(title: String = "选择文件", stage: Stage = BaseApp.app.primaryStage, vararg ext: ExtensionFilter): File? {
    val fileChooser = FileChooser()
    fileChooser.title = title
    fileChooser.initialDirectory = File(".")
    if (ext.isEmpty()) {
        fileChooser.extensionFilters.add(ext("所有文件", "*.*"))
    } else {
        fileChooser.extensionFilters.addAll(ext)
    }
    val selectedFile = fileChooser.showOpenDialog(stage)
    L.i("选择文件:$selectedFile")
    return selectedFile
}