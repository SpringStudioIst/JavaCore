package com.angcyo.javafx

import com.angcyo.http.rx.mainScheduler
import com.angcyo.log.L
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler
import javafx.application.Application
import javafx.stage.Screen
import javafx.stage.Stage

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

    override fun start(primaryStage: Stage) {
        //JavaFX Application Thread
        this.primaryStage = primaryStage
        app = this
    }

    override fun init() {
        super.init()
        //JavaFX-Launcher
        L.w(Screen.getScreens())
        //Tray.addTray()

        /*Platform.runLater {

        }*/
    }

    override fun stop() {
        super.stop()
        //JavaFX Application Thread
        //Platform.exit()
    }
}