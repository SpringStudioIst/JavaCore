package com.angcyo.selenium.auto.action

import com.angcyo.library.ex.str
import com.angcyo.selenium.auto.BaseControl
import com.angcyo.selenium.auto.clickSafe
import com.angcyo.selenium.parse.HandleResult
import org.openqa.selenium.WebElement

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/31
 */
class ClickAction : BaseAction() {

    override fun interceptAction(control: BaseControl, action: String?): Boolean {
        return action?.startsWith(Action.ACTION_CLICK) == true
    }

    override fun runAction(control: BaseControl, element: WebElement, action: String): HandleResult {
        val result = HandleResult()
        result.elementList = listOf(element)
        result.success = element.clickSafe()

        control.logAction?.invoke("点击[${element.str()}]:${result.success}")
        return result
    }
}