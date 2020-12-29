package com.angcyo.selenium

import com.angcyo.library.ex.isFileExist

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/29
 */
object DslSelenium {

    const val KEY_DRIVER = "webdriver.edge.driver"

    /**设置驱动路径*/
    fun initDriver(path: String?) {
        if (path != null) {
            System.setProperty(KEY_DRIVER, path)
        }
    }

    /**检查驱动程序是否有效*/
    fun checkDriver(): Boolean = System.getProperty(KEY_DRIVER).isFileExist()
}