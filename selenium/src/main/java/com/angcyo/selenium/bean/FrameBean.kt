package com.angcyo.selenium.bean

import org.openqa.selenium.WebDriver

/**
 * iframe选择
 * https://www.selenium.dev/documentation/zh-cn/webdriver/browser_manipulation/#frames-and-iframes
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2021/01/02
 */
data class FrameBean(

    /**使用选择元素iframe
     * driver.switchTo().frame(iframe)
     * https://www.selenium.dev/documentation/zh-cn/webdriver/browser_manipulation/#使用-webelement
     * */
    var select: SelectorBean? = null,

    /**
     * driver.switchTo().frame("myframe");
     * https://www.selenium.dev/documentation/zh-cn/webdriver/browser_manipulation/#使用-name-或-id
     * */
    var frameId: String? = null,

    /**[frameId]*/
    var frameName: String? = null,

    /**
     * >=0生效
     * https://www.selenium.dev/documentation/zh-cn/webdriver/browser_manipulation/#使用索引*/
    var frameIndex: Int = -1,

    /**主动离开框架, 否则当前[ActionBean]执行结束后, 才会离开框架
     * driver.switchTo().defaultContent();
     * https://www.selenium.dev/documentation/zh-cn/webdriver/browser_manipulation/#离开框架
     * */
    var defaultContent: Boolean? = null,

    //内部变量, 保存选择的iframe
    var _frame: WebDriver? = null
)
