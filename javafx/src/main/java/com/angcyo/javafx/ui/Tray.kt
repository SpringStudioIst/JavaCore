package com.angcyo.javafx.ui

import com.angcyo.javafx.BaseApp
import com.angcyo.javafx.base.ex.onMain
import com.angcyo.log.L
import javafx.application.Platform
import java.awt.*
import java.awt.event.ActionListener
import java.util.*


/**
 * 托盘操作类
 *
 * https://stackoverflow.com/questions/12571329/javafx-app-in-system-tray
 * https://docs.oracle.com/javase/8/docs/api/java/awt/SystemTray.html
 *
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/26
 */
object Tray {

    /**添加一个托盘图标*/
    fun addTray(image: Image, tooltip: String, action: (TrayIcon, PopupMenu) -> Unit = { _, _ -> }): TrayIcon? {
        if (SystemTray.isSupported()) {
            Platform.setImplicitExit(false)//重点:关闭隐式退出, 否则界面关闭程序就没了.
            //可以通过javafx.application.Platform.exit退出程序
            val tray = SystemTray.getSystemTray()
            val showOrHideListener = ActionListener {
                //点击托盘, 显示主界面
                onMain {
                    //Not on FX application thread; currentThread = AWT-EventQueue-0
                    BaseApp.app.primaryStage.apply {
                        if (isShowing) {
                            hide()
                        } else {
                            show()
                        }
                    }
                }
            }
            //弹出菜单
            val popup = PopupMenu()
            val trayIcon = TrayIcon(image, tooltip, popup)
            trayIcon.addActionListener(showOrHideListener)
            trayIcon.isImageAutoSize = true

            action(trayIcon, popup)

            //如果出现中文乱码, 请使用vm参数: -Dfile.encoding=GBK 启动java //GB18030
            val existItem = MenuItem("退出程序")
            val exitListener = ActionListener {
                tray.remove(trayIcon)
                Platform.exit()
                //exitProcess(1)
            }
            existItem.addActionListener(exitListener)
            popup.add(existItem)

            try {
                tray.add(trayIcon)
            } catch (e: AWTException) {
                L.e(e)
            }
            return trayIcon
        } else {
            L.w("当前系统不支持托盘")
        }
        // some time later
        // the application state has changed - update the image
//        if (trayIcon != null) {
//            trayIcon.setImage(updatedImage);
//        }
        return null
    }

    /**获取[pos]在屏幕上安全的矩形*/
    fun getSafeScreenBounds(pos: Point): Rectangle? {
        val bounds = getScreenBoundsAt(pos)
        val insets = getScreenInsetsAt(pos)
        if (bounds != null && insets != null) {
            bounds.x += insets.left
            bounds.y += insets.top
            bounds.width -= insets.left + insets.right
            bounds.height -= insets.top + insets.bottom
        }
        return bounds
    }

    private fun getScreenInsetsAt(pos: Point): Insets? {
        val gd = getGraphicsDeviceAt(pos)
        return Toolkit.getDefaultToolkit().getScreenInsets(gd.defaultConfiguration)
    }

    private fun getScreenBoundsAt(pos: Point): Rectangle? {
        val gd = getGraphicsDeviceAt(pos)
        return gd.defaultConfiguration.bounds
    }

    private fun getGraphicsDeviceAt(pos: Point): GraphicsDevice {
        val device: GraphicsDevice
        val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
        //多个显示器
        val lstGDs = ge.screenDevices
        val lstDevices = ArrayList<GraphicsDevice>(lstGDs.size)
        for (gd in lstGDs) {
            val gc = gd.defaultConfiguration
            val screenBounds = gc.bounds
            if (screenBounds.contains(pos)) {
                lstDevices.add(gd)
            }
        }
        device = if (lstDevices.size > 0) {
            lstDevices[0]
        } else {
            ge.defaultScreenDevice
        }
        return device
    }
}

/**移除托盘*/
fun TrayIcon.remove() {
    SystemTray.getSystemTray().remove(this)
}