package com.angcyo.selenium.auto.action

import com.angcyo.library.ex.patternList
import com.angcyo.selenium.auto.BaseControl
import com.angcyo.selenium.bean.putMap
import com.angcyo.selenium.parse.HandleResult
import com.angcyo.selenium.parse.args
import org.openqa.selenium.WebElement

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/31
 */
class GetCssAction : BaseAction() {

    override fun interceptAction(control: BaseControl, action: String?): Boolean {
        return action?.startsWith(Action.ACTION_GET_CSS) == true
    }

    override fun runAction(control: BaseControl, element: WebElement, action: String): HandleResult {
        val result = HandleResult()
        result.elementList = listOf(element)

        var key: String? = null
        action.args { index, arg ->
            if (index == 1) {
                key = arg
            }
        }

        //读取[xxx] xxx属性名
        val cssName = action.patternList("(?<=\\[).+(?=\\])").firstOrNull()

        val text: String?
        if (!cssName.isNullOrEmpty()) {
            text = element.getCssValue(cssName)

            //保存起来
            if (!key.isNullOrEmpty()) {
                control._currentTaskBean?.putMap(key, text)
            }

            result.success = text != null
        } else {
            text = null
        }

        control.logAction?.invoke("获取样式[$cssName]=[$text]:${result.success}")
        return result
    }
}