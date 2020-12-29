package com.angcyo.core.component.file

import com.angcyo.library.Constant
import com.angcyo.library.file.FileUtils
import com.angcyo.library.file.fileName
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

/**
 * APP扩展数据扩展目录下的 文件操作
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2019/12/26
 * Copyright (c) 2019 ShenZhen O&M Cloud Co., Ltd. All rights reserved.
 */
object DslFileHelper {

    /**异步文件写入*/
    var async = false

    val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS", Locale.CHINA)

    /**返回文件路径
     *
     * adb pull /sdcard/android/data/xxx/files/xxx/ ./
     * code .
     * */
    fun write(
        folder: String /*文件夹名, 相对于应用目录下的/files/文件夹*/,
        name: String = logFileName() /*文件名, 默认当天日期*/,
        data: String /*需要写入的数据*/
    ): String? {
        if (async) {
            thread {
                FileUtils.writeExternal(folder, name, data)
            }
        } else {
            FileUtils.writeExternal(folder, name, data)
        }
        //返回文件路径
        return FileUtils.appRootExternalFolderFile(folder, name).absolutePath
    }

    fun log(name: String = logFileName(), data: String) =
        write(Constant.LOG_FOLDER_NAME, name, _wrapData(data))

    fun http(name: String = logFileName(), data: String) =
        write(Constant.HTTP_FOLDER_NAME, name, _wrapData(data))

    fun ui(name: String = logFileName(), data: String) =
        write(Constant.UI_FOLDER_NAME, name, _wrapData(data))

    fun crash(name: String = fileName(suffix = ".log"), data: String) =
        write(Constant.CRASH_FOLDER_NAME, name, _wrapData(data))

    fun down(name: String = logFileName(), data: String) =
        write(Constant.DOWN_FOLDER_NAME, name, _wrapData(data))

    fun camera(name: String = logFileName(), data: String) =
        write(Constant.CAMERA_FOLDER_NAME, name, _wrapData(data))

    fun other(name: String = logFileName(), data: String) =
        write(Constant.OTHER_FOLDER_NAME, name, _wrapData(data))

    fun error(name: String = logFileName(), data: String) =
        write(Constant.ERROR_FOLDER_NAME, name, _wrapData(data))

    fun push(name: String = logFileName(), data: String) =
        write(Constant.PUSH_FOLDER_NAME, name, _wrapData(data))

    fun _wrapData(data: String): String {
        return buildString {
            appendln()
            append(dateFormat.format(Date()))
            appendln()
            append(data)
        }
    }

    fun _wrapData2(data: CharSequence): String {
        return buildString {
            append(dateFormat.format(Date()))
            appendln()
            append(data)
            appendln()
        }
    }
}

fun logFileName() = fileName("yyyy-MM-dd", ".log")

fun CharSequence.wrapData() = DslFileHelper._wrapData2(this)

/**将数据写入到指定文件*/
fun String?.writeTo(
    folder: String = Constant.LOG_FOLDER_NAME,
    name: String = logFileName()
) {
    DslFileHelper.write(folder, name, this ?: "null")
}

fun String?.writeTo(file: File, append: Boolean = true) {
    FileUtils.writeExternal(file, this ?: "null", append)
}

/**获取文件夹全路径*/
fun String.logFilePath(name: String = logFileName()): String? {
    return FileUtils.appRootExternalFolderFile(this, name).absolutePath
}