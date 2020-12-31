package com.angcyo.selenium.bean

/**
 * 窗口配置
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/29
 */
class TaskConfigBean {

    /**是否最大化窗口, 优先级:1*/
    var maximize: Boolean = true

    /**是否最小化窗口, 优先级:2*/
    var minimize: Boolean = false

    /**是否全屏, 优先级:3*/
    var fullscreen: Boolean = false

    /**窗口的矩形,  优先级:最大0
     * 格式: l:x t:x r:x b:x w:x h:x
     * 小数表示比例
     * [com.angcyo.selenium.parse.RectParse.parse]
     * */
    var rect: String? = null

    /**浏览器页面加载策略
     * [org.openqa.selenium.PageLoadStrategy.NONE]
     * [org.openqa.selenium.PageLoadStrategy.EAGER]
     * [org.openqa.selenium.PageLoadStrategy.NORMAL]
     * */
    var pageLoadStrategy: String? = "none"
}