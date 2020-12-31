package com.angcyo.selenium.bean

/**
 * 自动操作的一个任务
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/29
 */
data class TaskBean(

    /**目标网址*/
    var url: String? = null,

    /**窗口配置*/
    var config: TaskConfigBean? = null,

    /**任务的标题*/
    var title: String? = null,

    /**任务的描述*/
    var des: String? = null,

    /**操作步骤*/
    var actionList: List<ActionBean>? = null,

    /**[actionList]未处理时的操作步骤*/
    var backActionList: List<ActionBean>? = null,

    /**字符串输入供给*/
    var wordList: List<String>? = null
)