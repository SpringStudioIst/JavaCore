package com.angcyo.javafx.annotation

import com.angcyo.javafx.base.ex.toSelector
import com.angcyo.log.L
import javafx.scene.Node
import javafx.stage.Stage

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2021/01/08
 */
fun Any.inject(stage: Stage?) {
    inject(stage?.scene?.root)
}

fun Any.inject(node: Node?) {
    if (node != null) {
        for (field in this::class.java.declaredFields) {
            try {
                field.isAccessible = true
                if (field.isAnnotationPresent(NodeInject::class.java)) {
                    val nodeInject = field.getDeclaredAnnotation(NodeInject::class.java)
                    val css = if (nodeInject.css.isEmpty()) field.name else nodeInject.css
                    field.set(this, node.lookup(css.toSelector()))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    } else {
        L.w("node is null!")
    }
}