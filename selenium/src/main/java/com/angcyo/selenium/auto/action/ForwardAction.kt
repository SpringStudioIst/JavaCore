package com.angcyo.selenium.auto.action

import com.angcyo.selenium.auto.BaseControl
import com.angcyo.selenium.parse.HandleResult
import org.openqa.selenium.WebElement

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/31
 */
class ForwardAction : BaseAction() {
    override fun interceptAction(control: BaseControl, action: String?): Boolean {
        return action?.startsWith(Action.ACTION_FORWARD) == true
    }

    override fun runAction(control: BaseControl, element: WebElement, action: String): HandleResult {
        val result = HandleResult()
        result.elementList = listOf(element)
        control.driver?.navigate()?.apply {
            forward()
            result.success = true
        }
        control.logAction?.invoke("前进网页[${control.driver?.currentUrl}]:${result.success}")
        return result
    }
}