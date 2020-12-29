package com.angcyo.selenium.bean

/**
 * 自动操作的一个任务
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/29
 */
class TaskBean {

    /**目标网址*/
    var url: String? = null

    /**窗口配置*/
    var window: WindowBean? = null

    /**操作步骤*/
    var actionList: List<ActionBean>? = null

    /**[actionList]未处理时的操作步骤*/
    var backActionList: List<ActionBean>? = null
}