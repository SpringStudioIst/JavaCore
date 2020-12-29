package com.angcyo.library.ex

import java.awt.image.BufferedImage
import java.io.InputStream
import java.net.URL
import javax.imageio.ImageIO

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/24
 */

//<editor-fold desc="Java平台">

fun Any?.hash(): String? {
    return this?.hashCode()?.run { Integer.toHexString(this) }
}

fun Any.simpleHash(): String {
    return "${this.javaClass.simpleName}(${this.hash()})"
}

fun Any.simpleClassName(): String {
    return this.javaClass.simpleName
}

fun Any.className(): String {
    if (this is Class<*>) {
        return this.name
    }
    return this.javaClass.name
}

fun Any?.str(): String {
    return if (this is String) {
        this
    } else {
        this.toString()
    }
}

//</editor-fold desc="Java平台">

//<editor-fold desc="JavaFX平台">

/**获取jar中的资源*/
fun Any.getResource(name: String): URL? {
    var url: URL? = null
    try {
        url = javaClass.getResource(name)
    } catch (e: Exception) {
        //
    }
    if (url == null) {
        url = javaClass.classLoader.getResource(name)
    }
    return url
}

fun Any.getResourceAsStream(name: String): InputStream? {
    var stream: InputStream? = null
    try {
        stream = javaClass.getResourceAsStream(name)
    } catch (e: Exception) {
        //
    }
    if (stream == null) {
        stream = javaClass.classLoader.getResourceAsStream(name)
    }
    return stream
}

/**从资源中获取图片*/
fun Any.getImage(imageName: String): BufferedImage? {
    return try {
        ImageIO.read(getResource(imageName))
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}