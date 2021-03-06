package com.angcyo.library.file

import com.angcyo.log.L
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Android Q 扩展的程序目录操作
 *
 * 卸载后会丢失, 不需要申请权限
 *
 * /storage/emulated/0/Android/data/com.angcyo.uicore.demo/files/$type
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2019/12/30
 * Copyright (c) 2019 ShenZhen O&M Cloud Co., Ltd. All rights reserved.
 */
object FileUtils {

    /**允许写入单个文件的最大大小100mb, 之后会重写*/
    var fileMaxSize: Long = 100 * 1024 * 1024

    /**所有文件写入的在此根目录下*/
    var rootFolder: String = "config"

    /**获取文件夹路径*/
    var onGetFolderPath: (folderName: String) -> String = {
        "./$rootFolder/$it"
    }

    /**扩展目录下的指定文件夹*/
    fun appRootExternalFolder(folder: String): File {
        return File(onGetFolderPath(folder))
    }

    /**
     * [folder] 文件夹名字
     * [name] 文件夹下面的文件名
     *
     * 返回对应的文件, 可以直接进行读写, 不需要权限请求
     * */
    fun appRootExternalFolderFile(folder: String, name: String): File {
        val externalFilesDir = appRootExternalFolder(folder)
        return File(externalFilesDir, name)
    }

    fun writeExternal(folder: String, name: String, data: String, append: Boolean = true): String? {
        var filePath: String? = null

        try {
            appRootExternalFolderFile(folder, name).apply {
                filePath = writeExternal(this, data, append)
            }
        } catch (e: Exception) {
            L.e("写入文件失败:$e")
        }

        return filePath
    }

    fun writeExternal(file: File, data: String, append: Boolean = true): String? {
        var filePath: String? = null

        try {
            file.parentFile?.mkdirs()
            file.apply {
                filePath = absolutePath

                when {
                    length() >= fileMaxSize || !append -> writeText(data)
                    else -> appendText(data)
                }
            }
        } catch (e: Exception) {
            L.e("写入文件失败:$e")
        }

        return filePath
    }

    /**从APP扩展目录下读取文件数据*/
    fun readExternal(folder: String, name: String): String? {
        try {
            return appRootExternalFolderFile(folder, name).readText()
        } catch (e: Exception) {
            L.e("读取文件失败:$e")
        }
        return null
    }
}

fun uuid() = UUID.randomUUID().toString()

/**随机一个文件名*/
fun fileNameUUID(suffix: String = ""): String {
    return "${uuid()}$suffix"
}

/**获取一个时间文件名*/
fun fileName(pattern: String = "yyyy-MM-dd_HH-mm-ss-SSS", suffix: String = ""): String {
    val dateFormat: DateFormat = SimpleDateFormat(pattern, Locale.CHINA)
    return dateFormat.format(Date()) + suffix
}

/**获取一个文件路径*/
fun filePath(folderName: String, fileName: String = fileNameUUID()): String {
    return "${FileUtils.appRootExternalFolder(folder = folderName).absolutePath}${File.separator}${fileName}"
}

/**获取一个文件夹路径*/
fun folderPath(folderName: String): String {
    return FileUtils.appRootExternalFolder(folder = folderName).absolutePath
}