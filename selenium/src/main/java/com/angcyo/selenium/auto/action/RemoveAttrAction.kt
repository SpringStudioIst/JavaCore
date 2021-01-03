package com.angcyo.selenium.auto.action

import com.angcyo.selenium.auto.BaseControl
import com.angcyo.selenium.js.exeJs
import com.angcyo.selenium.parse.HandleResult
import com.angcyo.selenium.parse.args
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebElement

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/31
 */
class RemoveAttrAction : BaseAction() {

    override fun interceptAction(control: BaseControl, action: String?): Boolean {
        return action?.startsWith(Action.ACTION_REMOVE_ATTR) == true
    }

    override fun runAction(control: BaseControl, element: WebElement, action: String): HandleResult {
        val result = HandleResult()
        result.elementList = listOf(element)

        var css: String? = null
        var attr: String? = null
        action.args { index, arg ->
            if (index == 1) {
                css = arg
            } else if (index == 2) {
                attr = arg
            }
        }

        var jsResult: Any? = null
        if (!css.isNullOrEmpty() && !attr.isNullOrEmpty()) {
            jsResult = (control.driver as? JavascriptExecutor)?.exeJs("remove_attribute.js", css, attr)
            if (/*jsResult == null || */jsResult == false) {
                //执行失败
            } else {
                result.success = true
            }
        }

        control.logAction?.invoke("移除CSS[$css]属性[$attr]:[$jsResult]:${result.success}")
        return result
    }
}