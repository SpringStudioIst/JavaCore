package com.angcyo.selenium.parse

import com.angcyo.library.ex.patternList
import com.angcyo.selenium.bean.TaskConfigBean
import com.angcyo.selenium.isValid
import org.openqa.selenium.Dimension
import org.openqa.selenium.Point
import org.openqa.selenium.Rectangle
import org.openqa.selenium.WebDriver


/** 矩形参数解析
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/29
 */
class RectParse {

    var leftPattern = "(?<=l:|left:)([-]?[\\d.]*\\d+)"
    var rightPattern = "(?<=r:|right:)([-]?[\\d.]*\\d+)"
    var topPattern = "(?<=t:|top:)([-]?[\\d.]*\\d+)"
    var bottomPattern = "(?<=b:|bottom:)([-]?[\\d.]*\\d+)"
    var widthPattern = "(?<=w:|width:)([-]?[\\d.]*\\d+)"
    var heightPattern = "(?<=h:|height:)([-]?[\\d.]*\\d+)"

    var minRatio = 0.00000001
    var maxRatio = 0.99999999

    companion object {

        /**配置浏览器窗口大小*/
        fun configWindow(driver: WebDriver, taskConfigBean: TaskConfigBean?) {
            if (taskConfigBean != null) {
                driver.manage().window().apply {
                    if (taskConfigBean.rect == null) {
                        when {
                            taskConfigBean.maximize -> maximize()
                            taskConfigBean.minimize -> minimize()
                            taskConfigBean.fullscreen -> fullscreen()
                        }
                    } else {
                        configWindow(
                            driver,
                            RectParse().parse(
                                taskConfigBean.rect,
                                Rectangle(0, 0, size.height, size.width)
                            )
                        )
                    }
                }
            }
        }

        /**配置浏览器窗口大小*/
        fun configWindow(driver: WebDriver, rect: Rectangle?) {
            if (rect != null) {
                driver.manage().window().apply {
                    //设置窗口位置
                    val x = if (rect.x.isValid()) rect.x else position.x
                    val y = if (rect.y.isValid()) rect.y else position.y
                    position = Point(x, y)

                    //设置窗口大小
                    val w = if (rect.width.isValid()) rect.width else size.width
                    val h = if (rect.height.isValid()) rect.height else size.height
                    size = Dimension(w, h)
                }
            }
        }
    }

    /**无效的值使用[Int.MIN_VALUE]
     * 如果值在 0.00000001~0.99999999 之间, 则使用屏幕比例
     * */
    fun parse(arg: String?, bounds: Rectangle): Rectangle? {
        if (arg.isNullOrEmpty()) {
            return null
        } else {
            val left = arg.patternList(leftPattern).firstOrNull()
            val right = arg.patternList(rightPattern).firstOrNull()
            val top = arg.patternList(topPattern).firstOrNull()
            val bottom = arg.patternList(bottomPattern).firstOrNull()
            val width = arg.patternList(widthPattern).firstOrNull()
            val height = arg.patternList(heightPattern).firstOrNull()

            val l: Float = left?.toFloatOrNull() ?: Float.MIN_VALUE
            val r: Float = right?.toFloatOrNull() ?: Float.MIN_VALUE
            val t: Float = top?.toFloatOrNull() ?: Float.MIN_VALUE
            val b: Float = bottom?.toFloatOrNull() ?: Float.MIN_VALUE

            val w: Float =
                width?.toFloatOrNull() ?: (if (r.isValid() && l.isValid()) r - l else Float.MIN_VALUE)
            val h: Float =
                height?.toFloatOrNull() ?: (if (b.isValid() && t.isValid()) r - l else Float.MIN_VALUE)

            return Rectangle(
                parseValue(l, bounds.width).toInt(),
                parseValue(t, bounds.height).toInt(),
                parseValue(h, bounds.height).toInt(),
                parseValue(w, bounds.width).toInt()
            )
        }
    }

    /**[ref] 比例引用参考值*/
    fun parseValue(value: Float, ref: Int): Float {
        return if (value in minRatio..maxRatio) {
            value * ref
        } else {
            value
        }
    }
}