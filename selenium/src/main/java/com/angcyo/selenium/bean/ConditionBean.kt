package com.angcyo.selenium.bean

/**
 * 条件约束, 定义一些需要满足的条件. 所有生命的条件, 都必须满足.
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2021/01/02
 */
data class ConditionBean(

    /**当[com.angcyo.selenium.bean.TaskBean.textMap]包含指定key对应的值时.
     * 则满足条件!*/
    var textMapList: List<String>? = null,

    /**当指定[com.angcyo.selenium.bean.ActionBean.actionId]的[ActionBean]执行成功时.
     * 则满足条件*/
    var actionResultList: List<Long>? = null,
)