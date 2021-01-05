package com.angcyo.selenium.auto.action

import com.angcyo.selenium.DriverWebElement
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
        val result = HandleResult()

        control.driver?.apply {
            //配置window
            AutoParse.configWindow(this, control._currentTaskBean!!.config)

            //启动网页
            val url = control._currentTaskBean?.url
            if (!url.isNullOrBlank()) {
                get(url)
                result.success = true
                result.elementList = listOf(DriverWebElement())
            }
        }

        // 简便的方法
        //driver.get("https://selenium.dev");

        // 更长的方法
        //driver.navigate().to("https://selenium.dev");

        return result
    }
}