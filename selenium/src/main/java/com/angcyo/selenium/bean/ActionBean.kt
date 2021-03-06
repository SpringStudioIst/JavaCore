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

    /**是否激活[ActionBean], 未激活直接跳过执行*/
    var enable: Boolean = true,

    /**只有满足条件的[ActionBean]才能被执行, 不满足条件等同于[enable]为false,
     * 有一个条件满足即可*/
    var conditionList: List<ConditionBean>? = null,

    /**
     * 控制自身执行启动的延迟
     * [com.angcyo.selenium.parse.AutoParse.parseTime]
     * */
    var start: String? = null,

    /**元素解析*/
    var check: CheckBean? = null,

    /**未指定[check]时, 可以通过[checkId]在[check]库中根据id查找对应的[CheckBean]*/
    var checkId: Long = -1,
)