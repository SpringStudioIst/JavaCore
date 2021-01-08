package com.angcyo.library.ex

import java.awt.image.BufferedImage
import java.io.InputStream
import java.net.URL
import java.util.*
import java.util.concurrent.CountDownLatch
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
    return "${this.simpleClassName()}(${this.hash()})"
}

fun Any.simpleClassName(): String {
    if (this is Class<*>) {
        return this.simpleName
    }
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

fun CharSequence?.des(): String {
    return if (this.isNullOrEmpty()) {
        ""
    } else {
        "($this)"
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

/**从资源中获取图片
 * [java.awt.Image]*/
fun Any.getImage(imageName: String): BufferedImage? {
    return try {
        ImageIO.read(getResource(imageName))
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**从资源中获取字符串*/
fun Any.getString(name: String): String? {
    return try {
        getResourceAsStream(name)?.bufferedReader()?.readText()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**休眠*/
fun sleep(millis: Long = 160) {
    Thread.sleep(millis)
}

/**异步代码, 同步执行. 会阻塞当前线程*/
fun <R> sync(count: Int = 1, action: (CountDownLatch) -> R?): R? {
    val latch = CountDownLatch(count)
    val result = action(latch)
    latch.await()
    return result
}

fun <T> Optional<T>?.getOrNull(): T? {
    return if (this?.isPresent == true) {
        this.get()
    } else {
        null
    }
}

/**如果为空, 则执行[action].
 * 原样返回*/
fun <T> T?.elseNull(action: () -> Unit = {}): T? {
    if (this == null) {
        action()
    }
    return this
}

/**更新具有历史属性的[List]*/
fun <T> List<T>?.updateHistoryList(value: T?): List<T> {
    val list = this
    return if (value is String && value.isEmpty()) {
        list ?: emptyList()
    } else if (value == null) {
        list ?: emptyList()
    } else {
        (list?.toMutableList() ?: mutableListOf()).apply {
            remove(value)
            add(0, value)
        }
    }
}