package com.angcyo.selenium.auto.action

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/31
 */
object Action {
    /**点击[WebElement]*/
    const val ACTION_CLICK = "click"

    /**输入文本*/
    const val ACTION_INPUT = "input"

    /**可以用于清除输入框的内容*/
    const val ACTION_CLEAR = "clear"

    /**获取元素文本保存到
     * [com.angcyo.selenium.bean.TaskBean.textMap]
     * [getText:key]*/
    const val ACTION_GET_TEXT = "getText"

    /**获取元素属性保存到 [x:x]
     * [com.angcyo.selenium.bean.TaskBean.textMap]
     * [getAttr[xxx]:key]*/
    const val ACTION_GET_ATTR = "getAttr"

    /**获取元素样式保存到
     * [com.angcyo.selenium.bean.TaskBean.textMap]
     * [getCss[xxx]:key]*/
    const val ACTION_GET_CSS = "getCss"

    /**回退网页*/
    const val ACTION_BACK = "back"

    /**前进网页*/
    const val ACTION_FORWARD = "forward"

    /**跳转网页*/
    const val ACTION_TO = "to"

    /**刷新网页*/
    const val ACTION_REFRESH = "refresh"

    /**将文本进行验证码识别,
     * 并且将解析结果保存到 [com.angcyo.selenium.bean.TaskBean.textMap]
     * [code:$[key]:key]*/
    const val ACTION_CODE = "code"

    /**移除指定元素的指定属性
     * [removeAttr:css:attr]*/
    const val ACTION_REMOVE_ATTR = "removeAttr"

    /**设置指定元素的属性值
     * [setAttr:css:attr=value]*/
    const val ACTION_SET_ATTR = "setAttr"
}