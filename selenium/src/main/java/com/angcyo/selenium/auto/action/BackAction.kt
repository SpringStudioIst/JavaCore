package com.angcyo.selenium.auto.action

import com.angcyo.selenium.auto.BaseControl
import com.angcyo.selenium.parse.HandleResult
import org.openqa.selenium.WebElement

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/31
 */
class BackAction : BaseAction() {
    override fun interceptAction(control: BaseControl, action: String?): Boolean {
        return action?.startsWith(Action.ACTION_BACK) == true
    }

    override fun runAction(control: BaseControl, element: WebElement, action: String): HandleResult {
        return element.actionResult(control) { elementStr ->
            (control.driver?.navigate()?.run {
                back()
                true
            } ?: false) to "回退网页[${control.driver?.currentUrl}]"
        }
    }
}