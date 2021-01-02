package com.angcyo.selenium.auto.action

import com.angcyo.selenium.auto.BaseControl
import com.angcyo.selenium.auto.toStr
import com.angcyo.selenium.parse.HandleResult
import org.openqa.selenium.WebElement

/** 具体的操作执行
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/30
 */
abstract class BaseAction {

    /**是否需要拦截[action]执行*/
    abstract fun interceptAction(control: BaseControl, action: String?): Boolean

    abstract fun runAction(control: BaseControl, element: WebElement, action: String): HandleResult

    /**快速返回, 主要是[WebElement]toStr的操作, 需要提前执行. 否则如果网页进行了跳转, 元素就会不存在, 此时操作元素就会异常*/
    fun WebElement.actionResult(
        control: BaseControl,
        action: WebElement.(elementStr: String) -> Pair<Boolean, String>
    ): HandleResult {
        val result = HandleResult()
        result.elementList = listOf(this)
        val str = this.toStr()
        this.action(str).apply {
            result.success = first
            control.logAction?.invoke("$second->${first}")
        }
        return result
    }
}