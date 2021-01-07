package com.angcyo.selenium.auto.action

import com.angcyo.selenium.auto.BaseControl
import com.angcyo.selenium.parse.HandleResult
import org.openqa.selenium.WebElement

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/31
 */
class ClearAction : BaseAction() {
    override fun interceptAction(control: BaseControl, action: String?): Boolean {
        return action?.startsWith(Action.ACTION_CLEAR) == true
    }

    override fun runAction(control: BaseControl, element: WebElement, action: String): HandleResult {
        val result = HandleResult()
        result.elementList = listOf(element)
        try {
            element.clear()
            result.success = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        control.logAction?.invoke("清除内容:${result.success}")
        return result
    }
}