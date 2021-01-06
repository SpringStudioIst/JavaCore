package com.angcyo.javafx

import com.angcyo.http.rx.mainScheduler
import com.angcyo.javafx.base.ex.getImageFx
import com.angcyo.log.L
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler
import javafx.application.Application
import javafx.application.Platform
import javafx.scene.image.Image
import javafx.stage.Screen
import javafx.stage.Stage
import java.nio.charset.Charset

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/28
 */
open class BaseApp : Application() {

    companion object {
        lateinit var app: BaseApp

        fun getLogo(): Image? = getImageFx("logo.png")
    }

    init {
        //JavaFX Application Thread
        mainScheduler = JavaFxScheduler.platform()
        L.init("JavaFx", true)
    }

    /**主舞台*/
    lateinit var primaryStage: Stage


    /**1: JavaFX-Launcher*/
    override fun init() {
        super.init()
        L.w(Screen.getScreens())
        //Tray.addTray()

        println("Default Charset=" + Charset.defaultCharset())
        println("file.encoding=" + System.getProperty("file.encoding"))
        /*Default Charset=UTF-8
        file.encoding=UTF-8*/

        /*Platform.runLater {

        }*/
    }

    /**2: JavaFX Application Thread*/
    override fun start(primaryStage: Stage) {
        this.primaryStage = primaryStage
        app = this
        //Tray.getSafeScreenBounds(Point(1, 1))
    }

    /**3: JavaFX Application Thread*/
    override fun stop() {
        super.stop()
        //Platform.exit()
    }

    /**退出应用程序*/
    open fun exit() {
        Platform.exit()
    }

}