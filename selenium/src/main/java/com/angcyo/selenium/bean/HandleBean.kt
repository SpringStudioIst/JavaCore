package com.angcyo.selenium.bean

/**
 * 有了目标元素之后, 需要进行的处理操作
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/30
 */
data class HandleBean(

    /**重新选择新的元素, 否则直接使用[com.angcyo.selenium.bean.CheckBean.event]获取到的元素*/
    var selectList: List<SelectorBean>? = null
)
