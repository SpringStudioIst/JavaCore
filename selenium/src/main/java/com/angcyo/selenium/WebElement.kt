package com.angcyo.selenium

import org.openqa.selenium.remote.RemoteWebElement

/**
 * 特殊含义的元素
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/30
 */

/**驱动元素*/
class DriverWebElement : PlaceholderWebElement() {

    init {
        setId("DriverWebElement")
    }

    override fun toString(): String {
        return "驱动根元素[DriverWebElement]"
    }

}

/**log保持*/
class LogWebElement(val log: String) : PlaceholderWebElement() {

    init {
        setId("LogWebElement")
    }

    override fun toString(): String {
        return log
    }
}

/**占位元素*/
open class PlaceholderWebElement : RemoteWebElement() {

    init {
        setId("PlaceholderWebElement")
    }

    override fun toString(): String {
        return "占位元素[PlaceholderWebElement]"
    }
}