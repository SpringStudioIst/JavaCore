package com.angcyo.selenium.auto

import com.angcyo.log.L
import com.angcyo.selenium.bean.TaskBean
import org.openqa.selenium.WebDriver

/**
 * 自动辅助操作控制类
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/29
 */
class AutoControl(driver: WebDriver) : BaseControl(driver), Runnable {

    fun start(task: TaskBean) {
        L.i()
    }

    /**线程运行*/
    override fun run() {

    }
}