package com.angcyo.log

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/11/05
 *
 * 日志输出类
 */

object L {

    val _logAngcyo: Logger = LoggerFactory.getLogger("angcyo")
    val _logHttp: Logger = LoggerFactory.getLogger("http")
    val _logTemp: Logger = LoggerFactory.getLogger("temp")

    /**调试环境*/
    var debug = false

    var tag: String = "L"

    fun init(tag: String, debug: Boolean = false) {
        L.tag = tag
        L.debug = debug
    }

    fun debug(action: () -> Unit) {
        if (debug) {
            action()
        }
    }

    /*----------------angcyo.log----------------------*/

    fun i(vararg log: Any?) {
        _logAngcyo.info(log.joinToString(" "))
    }

    fun d(vararg log: Any?) {
        _logAngcyo.debug(log.joinToString(" "))
    }

    fun w(vararg log: Any?) {
        _logAngcyo.warn(log.joinToString(" "))
    }

    fun e(vararg log: Any?) {
        _logAngcyo.error(log.joinToString(" "))
    }

    /*----------------http.log----------------------*/

    fun ih(vararg log: Any?) {
        _logHttp.info(log.joinToString(" "))
    }

    fun wh(vararg log: Any?) {
        _logHttp.warn(log.joinToString(" "))
    }

    fun eh(vararg log: Any?) {
        _logHttp.error(log.joinToString(" "))
    }

    /*----------------temp.log----------------------*/

    fun it(vararg log: Any?) {
        _logTemp.info(log.joinToString(" "))
    }

    fun wt(vararg log: Any?) {
        _logTemp.warn(log.joinToString(" "))
    }

    fun et(vararg log: Any?) {
        _logTemp.error(log.joinToString(" "))
    }
}