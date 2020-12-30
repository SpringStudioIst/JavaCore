package com.angcyo.selenium.parse

import com.angcyo.selenium.auto.BaseControl
import com.angcyo.selenium.auto.TAGS
import com.angcyo.selenium.auto.findByCss
import com.angcyo.selenium.auto.findByText
import com.angcyo.selenium.bean.HandleBean
import com.angcyo.selenium.bean.SelectorBean
import org.openqa.selenium.SearchContext
import org.openqa.selenium.WebElement

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/30
 */
class CheckParse {

    fun selectorParse(context: SearchContext, selectorBean: SelectorBean, result: MutableList<WebElement>) {
        val list = mutableListOf<WebElement>()
        if (!selectorBean.css.isNullOrEmpty()) {
            list.addAll(context.findByCss(selectorBean.css!!))
        }
        if (selectorBean.text != null) {
            list.addAll(context.findByText(selectorBean.text!!, selectorBean.tags ?: TAGS))
        }
    }

    /**通过指定的选择器, 返回目标元素*/
    fun parse(control: BaseControl, selectorList: List<SelectorBean>?): List<WebElement> {
        val result = mutableListOf<WebElement>()

        selectorList?.forEach {

        }

        return result
    }

    /**处理目标, 返回处理过的元素*/
    fun handle(
        control: BaseControl,
        handleList: List<HandleBean>?,
        originList: List<WebElement>? = null /*之前解析返回的元素列表*/
    ): HandleResult {
        val result = HandleResult()
        return result
    }
}