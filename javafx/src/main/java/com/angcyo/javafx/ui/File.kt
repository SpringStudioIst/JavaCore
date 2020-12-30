package com.angcyo.javafx.ui

import com.angcyo.javafx.BaseApp
import com.angcyo.log.L
import javafx.stage.DirectoryChooser
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

class DslChooser {
    /**标题*/
    var title: String? = null

    /**初始目录*/
    var initialDirectory: File? = File(".")

    /**舞台*/
    var stage: Stage? = null

    /**文件扩展过滤*/
    val extList = mutableListOf<ExtensionFilter>()

    /**
     * 调用系统的选择文件的窗口
     * ExtensionFilter("Text Files", "*.txt"),
     * ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
     * ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
     * ExtensionFilter("All Files", "*.*")
     * */
    fun showChooserFile(): File? {
        val chooser = FileChooser()
        chooser.title = title ?: "选择文件"
        chooser.initialDirectory = initialDirectory
        if (extList.isEmpty()) {
            chooser.extensionFilters.add(ext("所有文件", "*.*"))
        } else {
            chooser.extensionFilters.addAll(extList)
        }
        val selectedFile = chooser.showOpenDialog(stage ?: BaseApp.app.primaryStage)
        L.i("选择文件:$selectedFile")
        return selectedFile
    }

    /**选择文件夹*/
    fun showChooserFolder(): File? {
        val chooser = DirectoryChooser()
        chooser.title = title ?: "选择文件夹"
        chooser.initialDirectory = initialDirectory
        val selectedFile = chooser.showDialog(stage ?: BaseApp.app.primaryStage)
        L.i("选择文件夹:$selectedFile")
        return selectedFile
    }
}

fun dslChooserFile(action: DslChooser.() -> Unit): File? {
    val chooser = DslChooser()
    chooser.action()
    return chooser.showChooserFile()
}

fun dslChooserFolder(action: DslChooser.() -> Unit): File? {
    val chooser = DslChooser()
    chooser.action()
    return chooser.showChooserFolder()
}