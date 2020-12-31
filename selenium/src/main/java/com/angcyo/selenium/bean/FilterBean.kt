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

    /**在当前元素的附近, 查找相对定位的元素
     * 格式 [tagName:xxx] [tagName:xxx:0~-2] 结果当中取第几个
     * above 元素上
     * below 元素下
     * toLeftOf 元素左
     * toRightOf 元素右
     * near 附近
     * https://www.selenium.dev/documentation/zh-cn/getting_started_with_webdriver/locating_elements/
     */
    var relativeBy: String? = null
)