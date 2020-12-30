package com.angcyo.javafx.ui

import javafx.animation.Interpolator
import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.beans.property.IntegerPropertyBase
import javafx.beans.value.WritableValue
import javafx.util.Duration

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/30
 */

fun dslTimeline(action: Timeline.() -> Unit): Timeline {
    val timeline = Timeline()
    timeline.cycleCount = 1
    timeline.isAutoReverse = false
    //timeline.keyFrames.add(KeyFrame(Duration(0.0), KeyValue()))
    timeline.action()
    timeline.play()
    //timeline.stop()
    return timeline
}

/**添加关键帧*/
fun <T> Timeline.addKeyFrame(
    millis: Double,//帧的时间, 毫秒
    target: WritableValue<T>,//操作的属性
    endValue: T,//属性值
    interpolator: Interpolator = Interpolator.LINEAR //差值器
) {
    keyFrames.add(keyFrameOf(millis, target, endValue, interpolator))
}

/**定义关键帧*/
fun <T> keyFrameOf(
    millis: Double,//帧的时间, 毫秒
    target: WritableValue<T>,//操作的属性
    endValue: T,//属性值
    interpolator: Interpolator = Interpolator.LINEAR //差值器
): KeyFrame {
    return KeyFrame(Duration(millis), KeyValue(target, endValue, interpolator))
}