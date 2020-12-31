package com.angcyo.selenium.auto.action

import com.angcyo.selenium.auto.BaseControl
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
}