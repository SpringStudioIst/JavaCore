package com.angcyo.selenium.bean

/**
 * 描述选择器
 * 通过描述脚本, 获取对应的元素
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/30
 */
data class SelectorBean(

    /**指定需要操作的iframe, 如果指定了, 那么之后的元素操作都在此iframe中进行*/
    var frame: FrameBean? = null,

    /**通过css选择器获取元素, 同时满足列表中的所有选择条件
     * 支持$0 $[xxx]表达式
     * [com.angcyo.selenium.parse.AutoParse.parseText]*/
    var cssList: List<String>? = null,

    /**通过文本获取元素, 支持正则表达式, 同时满足列表中的所有选择条件,需要指定[tags]
     * 支持$0 $[xxx]表达式
     * [com.angcyo.selenium.parse.AutoParse.parseText]
     * */
    var textList: List<String>? = null,

    /**通过[text]获取元素时, 需要指定在那些标签中查找文本. 空格分隔多个标签*/
    var tags: String? = null,

    /**根据过滤条件, 过滤一层*/
    var filter: FilterBean? = null,

    /**根据本次选择元素列表, 继续查找子子元素*/
    var after: SelectorBean? = null
)
