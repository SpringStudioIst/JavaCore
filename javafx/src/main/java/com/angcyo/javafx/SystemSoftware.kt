package com.angcyo.javafx

import java.io.BufferedReader
import java.io.InputStreamReader


/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/30
 */
object SystemSoftware {

    /**获取系统安装的软件列表(32/64)位*/
    fun getSystemSoftwareList(): List<SystemSoftwareBean> {
        val runtime = Runtime.getRuntime()
        val cmd64 = "cmd /c reg query HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\"
        val cmd32 =
            "cmd /c reg query HKEY_LOCAL_MACHINE\\SOFTWARE\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\"

        fun readList(cmd: String): List<SystemSoftwareBean> {
            val result = mutableListOf<SystemSoftwareBean>()
            val process: Process = runtime.exec(cmd)
            val reader = BufferedReader(InputStreamReader(process.inputStream, "GBK"))
            reader.use {
                var string: String? = null
                while (reader.readLine()?.also { string = it } != null) {
                    val bean = queryValue(string)
                    result.add(bean)
                }
            }
            process.destroy()
            return result
        }

        val result = mutableListOf<SystemSoftwareBean>()
        result.addAll(readList(cmd32))
        result.addAll(readList(cmd64))
        return result
    }

    //具体查询每一个软件的详细信息
    private fun queryValue(string: String?): SystemSoftwareBean {
        val runtime = Runtime.getRuntime()

        fun getValue(key: String): String {
            var result = ""
            val cmd = "cmd /c reg query $string /v $key"
            val process = runtime.exec(cmd)
            val reader = BufferedReader(InputStreamReader(process!!.inputStream, "GBK"))
            reader.use {
                reader.readLine()
                reader.readLine() //去掉前两行无用信息
                if ((reader.readLine()?.also { result = it }) != null) {
                    //去掉无用信息
                    result = result.replace("$key|REG_SZ".toRegex(), "").trim()
                }
            }
            process.destroy()
            return result
        }

        val result = SystemSoftwareBean()
        result.name = getValue("DisplayName")
        result.version = getValue("DisplayVersion")
        result.location = getValue("InstallLocation")
        result.publisher = getValue("Publisher")
        result.uninstallPath = getValue("UninstallString")
        return result
    }
}

data class SystemSoftwareBean(
    //软件名
    var name: String? = null,
    //安装路径
    var location: String? = null,
    //软件版本
    var version: String? = null,
    //发布者
    var publisher: String? = null,
    //卸载路径
    var uninstallPath: String? = null,
)