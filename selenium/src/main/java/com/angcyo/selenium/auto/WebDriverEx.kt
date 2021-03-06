package com.angcyo.selenium.auto

import com.angcyo.library.ex.have
import com.angcyo.library.ex.str
import com.angcyo.log.L
import com.angcyo.selenium.PlaceholderWebElement
import org.openqa.selenium.*
import org.openqa.selenium.remote.RemoteWebElement
import org.openqa.selenium.support.ui.FluentWait
import java.time.Duration

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/30
 */
const val TAGS = "span a button i li p ul"

/**枚举查找[WebElement]by[TagName]*/
fun String.eachTagWebElement(context: SearchContext, action: WebElement.() -> Unit) {
    split(" ").forEach { tag ->
        context.findElements(By.tagName(tag))?.forEach { element ->
            element.action()
        }
    }
}

/**先通过[Tag]获取标签, 再通过文本进行匹配元素*/
fun SearchContext.findByText(text: String, tags: String = TAGS, noText: (List<String>) -> Unit = {}): List<WebElement> {
    val result = mutableListOf<WebElement>()
    val textList = mutableListOf<String>()
    tags.eachTagWebElement(this) {
        try {
            val elementText = this.text
            textList.add(elementText)
            if (elementText.have(text)) {
                result.add(this)
            }
        } catch (e: Exception) {
            L.e("异常:$e")
            e.printStackTrace()
        }
    }
    if (result.isEmpty() && !tags.contains(" ")) {
        //未匹配到文本
        noText(textList)
    }
    return result
}

/**通过标签[TAG]进行匹配元素*/
fun SearchContext.findByTag(
    tags: String = TAGS,
    predicate: (WebElement) -> Boolean = { tags.contains(it.tagName) }
): List<WebElement> {
    val result = mutableListOf<WebElement>()
    tags.eachTagWebElement(this) {
        try {
            if (predicate(this)) {
                result.add(this)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return result
}

/**css选择器选择元素
 * document.querySelectorAll("input[placeholder=请输入账号]")
 * https://developer.mozilla.org/zh-CN/docs/Web/CSS/CSS_Selectors
 * */
fun SearchContext.findByCss(css: String): List<WebElement> {
    return findElements(By.cssSelector(css))
}

/**查找链接<a>标签的文本*/
fun SearchContext.findByLinkText(linkText: String): List<WebElement>? {
    return findElements(By.partialLinkText(linkText))
}

/**等待网页指定约束准备就绪
 * [timeout] 等待超时,秒
 * [pollingEvery] 检查频率,秒
 *
 * [org.openqa.selenium.support.ui.FluentWait.timeoutException]
 * */
@Throws(TimeoutException::class)
fun SearchContext.waitBy(
    timeout: Long = 10,
    pollingEvery: Long = 1,
    action: () -> List<WebElement>?
): List<WebElement>? {
    var result: List<WebElement>? = null
    FluentWait(this)
        .withTimeout(Duration.ofSeconds(timeout))
        .pollingEvery(Duration.ofSeconds(pollingEvery))
        .until {
            result = action()
            !result.isNullOrEmpty()
        }
    return result
}

/**是否是有效的[WebElement]*/
fun WebElement.isValidElement(): Boolean {
    if (this is PlaceholderWebElement) {
        return false
    }
    return true
}

/**toString*/
fun WebElement.toStr(): String {
    if (this is PlaceholderWebElement) {
        return this.toString()
    }
    return buildString {
        try {
            append("标签:${tagName}")
            append(" 文本:${text}")
            append(" 位置:${location} 大小:${size} 矩形:${rect.toStr()}")
            append(" 显示:${isDisplayed}")
            if (this@toStr is RemoteWebElement) {
                appendLine()
                //${coordinates.onScreen()}  Not supported yet.
                append("坐标 inViewPort:${coordinates.inViewPort()} onPage:${coordinates.onPage()} ${coordinates.auxiliary} json:${toJson()}")
            }
        } catch (e: Exception) {
            L.e("异常:$e")
            e.printStackTrace()
        }
    }
}

fun WebElement.clickSafe(): Boolean {
    return try {
        click()
        true
    } catch (e: Exception) {
        L.e("异常:${this.str()}:$e")
        e.printStackTrace()
        false
    }
}

fun WebElement.sendKeysSafe(vararg keysToSend: CharSequence?): Boolean {
    return try {
        sendKeys(*keysToSend)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun Rectangle.toStr(): String {
    return buildString {
        append("(")
        append("$x,$y")
        append("~")
        append("$width,$height")
        append(")")
    }
}

/**矩形log*/
fun Rectangle.str() = "x:$x,y:$y - w:$width,h:$height"