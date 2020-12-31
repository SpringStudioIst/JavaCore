package com.angcyo.selenium.auto.action

import com.angcyo.selenium.auto.BaseControl
import com.angcyo.selenium.parse.AutoParse
import com.angcyo.selenium.parse.HandleResult
import org.openqa.selenium.WebElement

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/30
 */
class StartAction : BaseAction() {
    override fun interceptAction(control: BaseControl, action: String?): Boolean {
        return false
    }

    override fun runAction(control: BaseControl, element: WebElement, action: String): HandleResult {
        //配置window
        AutoParse.configWindow(control.driver!!, control._currentTaskBean!!.config)

        //启动网页
        control.driver?.get(control._currentTaskBean?.url)

        return HandleResult().apply {
            success = true
        }
    }
}