package com.angcyo.javafx.ui

import javafx.scene.image.Image
import javafx.scene.image.PixelFormat
import javafx.scene.image.WritableImage
import java.util.*


/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2021/01/05
 */

/**从[Image]中, 获取指定矩形位置的[Image]*/
fun Image.clipRect(x: Int, y: Int, width: Int, height: Int): WritableImage =
    WritableImage(pixelReader, x, y, width, height)

/**
 * https://stackoverflow.com/questions/38095984/convert-javafx-image-object-to-byte-array
 * */
fun Image.toByteArray(): ByteArray {
    val width = width.toInt()
    val height = height.toInt()

    val pixelBytes = ByteArray(width * height * 4)
    //println(pixelBytes.size) // 367928 bytes

    pixelReader.getPixels(
        0, 0, width, height,
        PixelFormat.getByteBgraInstance(),
        pixelBytes, 0, width * 4
    )

    return pixelBytes
}

/**
 * https://stackoverflow.com/questions/38095984/convert-javafx-image-object-to-byte-array
 * */
fun ByteArray.toImage(width: Int, height: Int): WritableImage {
    val image = WritableImage(width, height)
    image.pixelWriter.setPixels(
        0, 0, width, height,
        PixelFormat.getByteBgraInstance(),
        this, 0, width * 4
    )
    return image
}

/**将[ByteArray]转换成[Base64]字符串*/
fun ByteArray.toBase64(): String = Base64.getEncoder().encodeToString(this)