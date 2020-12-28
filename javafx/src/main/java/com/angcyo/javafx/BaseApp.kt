package com.angcyo.javafx

import com.angcyo.http.rx.mainScheduler
import com.angcyo.javafx.ui.Tray
import com.angcyo.log.L
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler
import javafx.application.Application
import javafx.stage.Screen
import javafx.stage.Stage
import java.awt.Point

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/28
 */
open class BaseApp : Application() {

    companion object {
        lateinit var app: BaseApp
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

        /*Platform.runLater {

        }*/
    }

    /**2: JavaFX Application Thread*/
    override fun start(primaryStage: Stage) {
        this.primaryStage = primaryStage
        app = this
        Tray.getSafeScreenBounds(Point(1, 1))
    }

    /**3: JavaFX Application Thread*/
    override fun stop() {
        super.stop()
        //Platform.exit()
    }
}