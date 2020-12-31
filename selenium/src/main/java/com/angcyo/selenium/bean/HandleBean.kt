package com.angcyo.selenium.bean

/**
 * 有了目标元素之后, 需要进行的处理操作
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/30
 */
data class HandleBean(

    /**重新选择新的元素, 否则直接使用[com.angcyo.selenium.bean.CheckBean.event]获取到的元素*/
    var selectList: List<SelectorBean>? = null,

    /**过滤目标元素, 只处理之后的元素*/
    var filter: FilterBean? = null,

    /**不管执行有没有成功, 都返回[false]
     * 优先处理此属性*/
    var ignore: Boolean = false,

    /**不管执行是否成功, 都跳过之后的[HandleBean]处理*/
    var jump: Boolean = false,

    /**当执行成功后, 跳过之后的[HandleBean]处理*/
    var jumpOnSuccess: Boolean = false,

    /**需要执行的具体操作
     * [com.angcyo.selenium.auto.action.Action]*/
    var actionList: List<String>? = null,
)
