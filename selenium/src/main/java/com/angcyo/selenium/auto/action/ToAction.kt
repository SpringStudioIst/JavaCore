package com.angcyo.selenium.auto.action

import com.angcyo.library.ex.subEnd
import com.angcyo.selenium.auto.BaseControl
import com.angcyo.selenium.parse.HandleResult
import org.openqa.selenium.WebElement

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/31
 */
class ToAction : BaseAction() {
    override fun interceptAction(control: BaseControl, action: String?): Boolean {
        return action?.startsWith(Action.ACTION_TO) == true
    }

    override fun runAction(control: BaseControl, element: WebElement, action: String): HandleResult {
        val result = HandleResult()
        result.elementList = listOf(element)
        val to = action.subEnd(":")
        control.driver?.navigate()?.apply {
            if (to.isNotEmpty()) {
                to(to)
                result.success = true
            }
        }
        control.logAction?.invoke("跳转网页[${to}]:${result.success}")
        return result
    }
}