package com.angcyo.selenium.auto

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/30
 */
data class ControlTip(
    /**提示的标题*/
    var title: String? = null,

    /**提示的描述*/
    var des: String? = null,

    /**下一次执行时间间隔*/
    var nextTime: Long = -1
)