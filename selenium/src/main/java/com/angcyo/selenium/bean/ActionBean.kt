package com.angcyo.selenium.bean

/**
 * 操作步骤
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/29
 */
data class ActionBean(

    /**标识[ActionBean]*/
    var actionId: Long = -1,

    /**操作的标题*/
    var title: String? = null,

    /**操作的描述*/
    var des: String? = null,

    /**
     * 控制自身执行启动的延迟
     * [com.angcyo.selenium.parse.TimeParse.parse]
     * */
    var start: String? = null,

    /**元素解析*/
    var check: CheckBean? = null,

    /**未指定[check]时, 可以通过[checkId]在[check]库中根据id查找对应的[CheckBean]*/
    var checkId: Long = -1,
)