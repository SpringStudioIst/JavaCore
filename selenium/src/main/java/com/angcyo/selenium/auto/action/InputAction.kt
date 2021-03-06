package com.angcyo.selenium.auto.action

import com.angcyo.library.ex.subEnd
import com.angcyo.selenium.auto.BaseControl
import com.angcyo.selenium.auto.sendKeysSafe
import com.angcyo.selenium.parse.HandleResult
import org.openqa.selenium.WebElement

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/31
 */
class InputAction : BaseAction() {
    override fun interceptAction(control: BaseControl, action: String?): Boolean {
        return action?.startsWith(Action.ACTION_INPUT) == true
    }

    override fun runAction(control: BaseControl, element: WebElement, action: String): HandleResult {
        val result = HandleResult()
        result.elementList = listOf(element)
        val arg = action.subEnd(":")
        val textList = control._autoParse.parseText(control, arg)
        val text = textList.firstOrNull()
        result.success = element.sendKeysSafe(text)

        control.logAction?.invoke("输入[$text]:${result.success}")
        return result
    }
}