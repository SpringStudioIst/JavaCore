package com.angcyo.selenium

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/24
 */

fun Int.isValid() = this != Int.MIN_VALUE

fun Float.isValid() = isFinite() && this != Float.MIN_VALUE

/**转成css中的px单位*/
fun Any.toPx() = "${this}px"