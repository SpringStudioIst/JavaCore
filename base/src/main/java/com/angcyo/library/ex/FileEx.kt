package com.angcyo.library.ex

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.security.DigestInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

/**
 * /kotlin/io/FileReadWrite.kt
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/01/22
 */

enum class SizeUnit {
    Byte, KB, MB, GB, TB, PB, Auto
}

/**文件是否存在*/
fun String?.isFileExist(): Boolean {
    return try {
        if (this.isNullOrBlank()) {
            return false
        }
        val file = File(this)
        file.exists() && file.canRead()
    } catch (e: Exception) {
        false
    }
}

/**将文件内容, 转移到另一个文件*/
fun String.transferToFile(filePath: String) {
    if (this.equals(filePath, ignoreCase = true)) {
        return
    }
    var outputChannel: FileChannel? = null
    var inputChannel: FileChannel? = null
    try {
        inputChannel = FileInputStream(File(this)).channel
        outputChannel = FileOutputStream(File(filePath)).channel
        inputChannel.transferTo(0, inputChannel.size(), outputChannel)
        inputChannel.close()
    } finally {
        inputChannel?.close()
        outputChannel?.close()
    }
}

/**获取文件md5值*/
fun File.md5(): String? {
    return getFileMD5()?.toHexString()
}

fun File.copyTo(path: String) {
    copyTo(File(path), true)
}

fun String.file(): File? {
    return try {
        File(this)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun String?.fileSize(): Long {
    if (TextUtils.isEmpty(this)) {
        return 0L
    }
    val file = this?.file()
    return if (file?.exists() == true) {
        file.length()
    } else {
        0L
    }
}

/**格式化文件大小, 根据系统版本号选择实现方式*/
fun formatFileSize(size: Long): String {
    return size.fileSizeString()
}

fun String?.fileSizeString(): String {
    return fileSize().fileSizeString()
}

/**b*/
fun Long.fileSizeString(unit: SizeUnit = SizeUnit.Auto): String {
    val size = this
    var _unit = unit
    if (size < 0) {
        return ""
    }
    val KB = 1024.0
    val MB = KB * 1024
    val GB = MB * 1024
    val TB = GB * 1024
    val PB = TB * 1024
    if (_unit == SizeUnit.Auto) {
        _unit = if (size < KB) {
            SizeUnit.Byte
        } else if (size < MB) {
            SizeUnit.KB
        } else if (size < GB) {
            SizeUnit.MB
        } else if (size < TB) {
            SizeUnit.GB
        } else if (size < PB) {
            SizeUnit.TB
        } else {
            SizeUnit.PB
        }
    }
    return when (_unit) {
        SizeUnit.Byte -> size.toString() + "B"
        SizeUnit.KB -> String.format(Locale.US, "%.2fKB", size / KB)
        SizeUnit.MB -> String.format(Locale.US, "%.2fMB", size / MB)
        SizeUnit.GB -> String.format(Locale.US, "%.2fGB", size / GB)
        SizeUnit.TB -> String.format(Locale.US, "%.2fTB", size / TB)
        SizeUnit.PB -> String.format(Locale.US, "%.2fPB", size / PB)
        else -> size.toString() + "B"
    }
}

/**
 * 获取文件的MD5校验码
 *
 * @param file 文件
 * @return 文件的MD5校验码
 */
fun File.getFileMD5(): ByteArray? {
    var dis: DigestInputStream? = null
    try {
        val fis = FileInputStream(this)
        var md = MessageDigest.getInstance("MD5")
        dis = DigestInputStream(fis, md)
        val buffer = ByteArray(1024 * 256)
        while (dis.read(buffer) > 0);
        md = dis.messageDigest
        return md.digest()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        dis?.close()
    }
    return null
}

fun File?.isFile(): Boolean = this?.isFile == true

fun File?.isFolder(): Boolean = this?.isDirectory == true

fun File?.readText() = try {
    if (this?.exists() == true) {
        this.readText(Charsets.UTF_8)
    } else {
        null
    }
} catch (e: Exception) {
    e.printStackTrace()
    null
}

/**从最后一行开始读取文本, 倒序一行一行读取
 * [limit] 限制需要多少行
 * [truncated] 超限后追加的字符
 * [reversed]  是否是文件末尾开始一行一行读取
 */
fun File?.readReverseText(
    limit: Int = -1,
    truncated: CharSequence = "...",
    reversed: Boolean = true,
) = try {
    this?.readLines(Charsets.UTF_8)?.run {
        (if (reversed) reversed() else this).joinToString(
            "\n",
            limit = limit,
            truncated = truncated
        )
    }
} catch (e: Exception) {
    e.printStackTrace()
    null
}

/**[readReverseText]*/
fun File?.readTextLines(
    limit: Int = -1,
    truncated: CharSequence = "...",
    reversed: Boolean = false
) = readReverseText(limit, truncated, reversed)

/**读取文件最后多少行的数据*/
fun File?.readTextLastLines(
    limit: Int = -1,
    truncated: CharSequence = "..."
) = try {
    this?.readLines(Charsets.UTF_8)?.run {
        val lastLineIndex = if (limit >= 0) this.size - limit else 0
        this.filterIndexed { index, _ -> index >= lastLineIndex }.joinToString(
            "\n",
            limit = limit,
            truncated = truncated
        )
    }
} catch (e: Exception) {
    e.printStackTrace()
    null
}