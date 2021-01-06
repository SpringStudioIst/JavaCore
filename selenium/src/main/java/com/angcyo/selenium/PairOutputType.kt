package com.angcyo.selenium

import com.angcyo.javafx.ui.toImage
import com.angcyo.library.file.fileName
import javafx.scene.image.Image
import org.openqa.selenium.OutputType
import org.openqa.selenium.WebDriverException
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

/**
 * 截图输出[Image]
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2021/01/05
 */
class PairOutputType : OutputType<Pair<File, Image>> {

    override fun convertFromBase64Png(base64Png: String?): Pair<File, Image>? {
        return convert(OutputType.BYTES.convertFromBase64Png(base64Png))
    }

    override fun convertFromPngBytes(png: ByteArray?): Pair<File, Image>? {
        return convert(png)
    }

    override fun toString(): String {
        return "OutputType.IMAGE"
    }

    fun convert(data: ByteArray?): Pair<File, Image>? {
        val file = try {
            save(data)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } ?: return null
        return file to file.toImage()
    }

    fun save(data: ByteArray?): File? {
        if (data == null) {
            return null
        }
        var stream: OutputStream? = null
        return try {
            val folder = File("screenshot")
            folder.mkdirs()
            val tmpFile = File(folder, fileName(suffix = ".png"))
            tmpFile.deleteOnExit()
            stream = FileOutputStream(tmpFile)
            stream.write(data)
            tmpFile
        } catch (e: IOException) {
            throw WebDriverException(e)
        } finally {
            if (stream != null) {
                try {
                    stream.close()
                } catch (e: IOException) {
                    // Nothing sane to do
                }
            }
        }
    }
}