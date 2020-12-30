package com.angcyo.selenium.bean

/**
 * 在一组元素中过滤满足条件的元素
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/30
 */
data class FilterBean(
    /**获取一组元素中指定索引位置的元素
     * 支持范围[0~-1]第一个到倒数第一个*/
    var index: String? = null
)