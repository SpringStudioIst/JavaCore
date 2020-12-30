package com.angcyo.selenium.bean

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/30
 */
data class CheckBean(
    /**check的id, 不参与识别逻辑*/
    var checkId: Long = 1,

    /**check的描述, 不参与识别逻辑*/
    var des: String? = null,

    /**如果包含目标元素*/
    var event: List<SelectorBean>? = null,

    /**目标元素需要进行的操作*/
    var handle: List<HandleBean>? = null,

    /**如果未包含目标元素时, 需要进行的操作*/
    var other: List<HandleBean>? = null,
)
