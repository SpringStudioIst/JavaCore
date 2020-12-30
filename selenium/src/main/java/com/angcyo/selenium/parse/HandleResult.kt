package com.angcyo.selenium.parse

import org.openqa.selenium.WebElement

/**
 * 处理结果返回
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/30
 */
class HandleResult {
    /**是否处理成功*/
    var success: Boolean = false

    /**被处理的元素*/
    var elementList: List<WebElement>? = null
}