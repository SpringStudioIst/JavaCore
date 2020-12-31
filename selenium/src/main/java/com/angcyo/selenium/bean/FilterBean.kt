package com.angcyo.selenium.bean

/**
 * 在一组元素中过滤满足条件的元素, 所有过滤条件都会依次匹配
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/30
 */
data class FilterBean(
    /**获取一组元素中指定索引位置的元素
     * 支持范围[0~-1]第一个到倒数第一个
     * 匹配顺序:1*/
    var index: String? = null,

    /**如果元素包含指定的元素
     * 匹配顺序:2*/
    var containList: List<SelectorBean>? = null,

    /**如果元素不包含指定的元素
     * 匹配顺序:3*/
    var notContainList: List<SelectorBean>? = null,
)