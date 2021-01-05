package com.angcyo.selenium.js

import com.angcyo.library.ex.eachField
import com.angcyo.library.ex.getString
import com.angcyo.log.L
import org.openqa.selenium.JavascriptExecutor

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/31
 */

/**执行js文件
 * [resJsName] 资源文件的名字
 * [model] 数据模型, 用于替换 $key = value*/
fun JavascriptExecutor.exeJsByModel(resJsName: String, model: Any, async: Boolean = false): Any? {
    var js = getString(resJsName)
    if (js.isNullOrEmpty()) {
        return false
    }
    model.eachField { field, value ->
        js = js!!.replace("$${field.name}", value.toString(), true)
    }
    return if (async) {
        executeAsyncScript(js)
    } else {
        executeScript(js)
    }
}

/**同步执行js, js通过[arguments]获取参数*/
fun JavascriptExecutor.exeJs(resJsName: String, vararg args: Any?): Any? {
    val js = getString(resJsName)
    if (js.isNullOrEmpty()) {
        return false
    }
    return try {
        executeScript(js, *args)
    } catch (e: Exception) {
        L.e("异常:$e")
        e.printStackTrace()
        null
    }
}

/**异步执行js, js通过[arguments]获取参数*/
fun JavascriptExecutor.exeJsAsync(resJsName: String, vararg args: Any?): Any? {
    val js = getString(resJsName)
    if (js.isNullOrEmpty()) {
        return false
    }
    return try {
        executeAsyncScript(js, *args)
    } catch (e: Exception) {
        L.e("异常:$e")
        e.printStackTrace()
        null
    }
}


