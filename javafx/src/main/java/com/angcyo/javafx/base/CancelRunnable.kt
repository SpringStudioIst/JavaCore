package com.angcyo.javafx.base

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2021/01/05
 */
class CancelRunnable(val action: () -> Unit) : Runnable {

    @Volatile
    var isEnd: Boolean = false

    override fun run() {
        if (!isEnd) {
            isEnd = true
            action()
        }
    }
}