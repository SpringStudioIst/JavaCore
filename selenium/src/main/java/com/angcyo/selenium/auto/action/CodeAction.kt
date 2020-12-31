package com.angcyo.selenium.auto.action

import com.angcyo.selenium.auto.BaseControl
import com.angcyo.selenium.bean.putMap
import com.angcyo.selenium.parse.Code
import com.angcyo.selenium.parse.HandleResult
import com.angcyo.selenium.parse.args
import org.openqa.selenium.WebElement

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/31
 */
class CodeAction : BaseAction() {

    val base64Image = "data:image/jpeg;base64,"

    override fun interceptAction(control: BaseControl, action: String?): Boolean {
        return action?.startsWith(Action.ACTION_CODE) == true
    }

    override fun runAction(control: BaseControl, element: WebElement, action: String): HandleResult {
        val result = HandleResult()
        result.elementList = listOf(element)

        var key = "imageCode"
        var arg: String? = null
        action.args { index, str ->
            if (index == 1) {
                arg = str
            } else if (index == 2) {
                key = str
            }
        }

        val textList = control._autoParse.parseText(control, arg)
        val text = textList.firstOrNull()

        var value: String? = null
        if (!text.isNullOrEmpty()) {
            //调用api, 解析验证码
            value = Code.parseSync(
                if (text.startsWith(base64Image))
                    text.substring(base64Image.length, text.length) //去掉base64头
                else text
            )
            if (!value.isNullOrEmpty()) {
                result.success = true

                //保存起来
                control._currentTaskBean?.putMap(key, value)
            }
        }

        control.logAction?.invoke("解析验证码[$value]:${result.success}")
        return result
    }
}