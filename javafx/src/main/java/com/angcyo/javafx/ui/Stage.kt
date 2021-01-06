package com.angcyo.javafx.ui

import com.angcyo.javafx.BaseApp
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import javafx.stage.StageStyle

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2021/01/06
 */

class DslStage {

    /**舞台的样式*/
    var stageStyle: StageStyle = StageStyle.DECORATED
    var icons: List<Image>? = null

    /**场景的参数*/
    var root: Parent? = null
    var sceneWidth = -1.0
    var sceneHeight = -1.0

    fun config(): Stage {
        val stage = Stage()
        stage.initStyle(stageStyle)
        if (icons == null) {
            BaseApp.getLogo()?.let {
                stage.icons.add(it)
            }
        } else {
            stage.icons.addAll(icons!!)
        }
        stage.scene = Scene(root, sceneWidth, sceneHeight)
        return stage
    }

    fun show(): Stage {
        return config().apply {
            show()
        }
    }

    fun showAndWait(): Stage {
        return config().apply {
            showAndWait()
        }
    }
}

fun dslStage(root: Parent, action: DslStage.() -> Unit = {}): Stage {
    val dslStage = DslStage()
    dslStage.root
    dslStage.action()
    return dslStage.show()
}