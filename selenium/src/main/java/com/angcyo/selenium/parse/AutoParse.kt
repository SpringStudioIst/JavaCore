package com.angcyo.selenium.parse

import com.angcyo.javafx.base.OSInfo
import com.angcyo.library.ex.getLongNum
import com.angcyo.library.ex.patternList
import com.angcyo.log.L
import com.angcyo.selenium.auto.BaseControl
import com.angcyo.selenium.auto.TAGS
import com.angcyo.selenium.auto.findByCss
import com.angcyo.selenium.auto.findByText
import com.angcyo.selenium.bean.FilterBean
import com.angcyo.selenium.bean.HandleBean
import com.angcyo.selenium.bean.SelectorBean
import com.angcyo.selenium.bean.TaskConfigBean
import com.angcyo.selenium.isValid
import org.openqa.selenium.*
import kotlin.math.max
import kotlin.random.Random

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/31
 */
class AutoParse {

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
                            AutoParse().parseRect(
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

        /**根据系统, 随机获取一个时长*/
        fun randomTime(): Long {
            return if (OSInfo.isMacOS) {
                Random.nextLong(1000, 3000)
            } else {
                Random.nextLong(2000, 7000)
            }
        }
    }

    /**解析矩形格式
     * 矩形参数解析
     * 无效的值使用[Int.MIN_VALUE]
     * 如果值在 0.00000001~0.99999999 之间, 则使用屏幕比例
     * */
    fun parseRect(arg: String?, bounds: Rectangle): Rectangle? {
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

    /**
     * 解析数值
     * [ref] 比例引用参考值*/
    fun parseValue(value: Float, ref: Int): Float {
        return if (value in minRatio..maxRatio) {
            value * ref
        } else {
            value
        }
    }

    /**
     * 解析时间格式
     * 格式[5000,500,5] 解释:5000+500*[1-5),
     * 返回解析后的时间, 毫秒*/
    fun parseTime(arg: String?, def: Long = 0): Long {
        return if (arg.isNullOrEmpty()) {
            def
        } else {
            val split = arg.split(",")

            //时长
            val start = split.getOrNull(0)?.toLongOrNull() ?: def

            //基数
            val base = split.getOrNull(1)?.toLongOrNull() ?: randomTime()

            //倍数
            val factor = split.getOrNull(2)?.toLongOrNull() ?: 1 //nextLong(2, 5)

            start + base * Random.nextLong(1, max(2L, factor + 1))
        }
    }

    /**解析文本
     * $0 从[com.angcyo.selenium.bean.TaskBean.wordList] 取第一个
     * $-2 从[com.angcyo.selenium.bean.TaskBean.wordList] 取倒数第二个
     * $0~$-2 取范围内的字符
     * */
    fun parseText(control: BaseControl, arg: String?): List<String> {
        if (arg.isNullOrEmpty()) {
            return emptyList()
        }
        val wordList = control._currentTaskBean?.wordList ?: emptyList()
        val result = mutableListOf<String>()
        val indexStringList = arg.patternList("\\$[-]?\\d+")
        if (indexStringList.isNotEmpty()) {
            //$xxx 的情况
            if (arg.havePartition()) {
                //$0~$1
                if (indexStringList.size >= 2) {
                    val startIndex = indexStringList[0].getLongNum()?.revise(wordList.size) ?: 0
                    val endIndex = indexStringList[1].getLongNum()?.revise(wordList.size) ?: 0

                    wordList.forEachIndexed { index, word ->
                        if (index in startIndex..endIndex) {
                            result.add(word)
                        }
                    }
                } else {
                    indexStringList.forEach { indexString ->
                        indexString.getLongNum()?.let { index ->
                            wordList.getOrNull(index.toInt())?.let { word ->
                                result.add(word)
                            }
                        }
                    }
                }
            } else {
                //$0
                indexStringList.forEach { indexString ->
                    indexString.getLongNum()?.let { index ->
                        wordList.getOrNull(index.toInt())?.let { word ->
                            result.add(word)
                        }
                    }
                }
            }
        } else {
            //不包含$
            result.add(arg)
        }
        return result
    }

    /**选择器解析*/
    fun _selectorParse(context: SearchContext, selectorBean: List<SelectorBean>, result: MutableList<WebElement>) {
        selectorBean.forEach {
            _selectorParse(context, it, result)
        }
    }

    /**选择器解析*/
    fun _selectorParse(context: SearchContext, selectorBean: SelectorBean, result: MutableList<WebElement>) {
        val list = mutableListOf<WebElement>()

        //css
        if (!selectorBean.cssList.isNullOrEmpty()) {
            list.addAll(selectorBean.cssList!!.eachMatchItem { context.findByCss(it) })
        }

        //text
        if (!selectorBean.textList.isNullOrEmpty()) {
            list.addAll(selectorBean.textList!!.eachMatchItem { context.findByText(it, selectorBean.tags ?: TAGS) })
        }

        //需要过滤
        if (selectorBean.filter != null) {
            list.removeAll(_filterElement(list, selectorBean.filter))
        }

        //递归处理
        var afterList: MutableList<WebElement>? = null
        if (selectorBean.after != null) {
            afterList = mutableListOf()
            list.forEach { element ->
                _selectorParse(element, selectorBean.after!!, afterList)
            }
        }

        //返回选择的目标元素
        result.addAll(afterList ?: list)
    }

    /**
     * 过滤元素
     * 返回过滤之后的元素集合
     * */
    fun _filterElement(list: List<WebElement>?, filterBean: FilterBean?): List<WebElement> {
        val removeList = _filterElementByRemove(list, filterBean)
        return list?.filter { !removeList.contains(it) } ?: emptyList()
    }

    /**
     * 过滤元素
     * 返回不满足条件的元素集合
     * */
    fun _filterElementByRemove(list: List<WebElement>?, filterBean: FilterBean?): List<WebElement> {
        if (list == null) {
            return emptyList()
        }
        if (filterBean == null) {
            return list
        }

        //不满足条件的元素
        val removeList = mutableListOf<WebElement>()

        //过滤筛选步骤1:
        val indexString = filterBean.index
        if (indexString != null) {
            val intList = indexString.getIntList()
            if (indexString.havePartition()) {
                if (intList.size >= 2) {
                    //有分隔符 0~-2:取从0到倒数第二个
                    val startIndex = intList[0].revise(list.size)
                    val endIndex = intList[1].revise(list.size)

                    list.forEachIndexed { index, webElement ->
                        if (index in startIndex..endIndex) {
                            //需要选择的目标元素, 不需要过滤
                        } else {
                            //需要移除的元素
                            removeList.add(webElement)
                        }
                    }
                }
            } else {
                //如果没有分隔符 1 2 3:取第1个 第2个 第3个
                list.forEachIndexed { index, webElement ->
                    if (index in intList) {
                        //需要选择的目标元素, 不需要过滤
                    } else {
                        //需要移除的元素
                        removeList.add(webElement)
                    }
                }
            }
        }

        //过滤筛选步骤2:
        //剩下的元素集合
        val originList = if (removeList.isEmpty()) list else list.filter { !removeList.contains(it) }
        if (!filterBean.containList.isNullOrEmpty()) {
            originList.forEach { element ->
                val result = mutableListOf<WebElement>()
                _selectorParse(element, filterBean.containList ?: emptyList(), result)
                if (result.isEmpty()) {
                    //不包含目标, 不符合过滤条件
                    removeList.add(element)
                }
            }
        }

        //过滤筛选步骤3:
        val originList2 = if (removeList.isEmpty()) originList else originList.filter { !removeList.contains(it) }
        if (!filterBean.notContainList.isNullOrEmpty()) {
            originList2.forEach { element ->
                val result = mutableListOf<WebElement>()
                _selectorParse(element, filterBean.notContainList ?: emptyList(), result)
                if (result.isNotEmpty()) {
                    //包含目标, 不符合过滤条件
                    removeList.add(element)
                }
            }
        }

        return removeList
    }

    /**通过指定的选择器, 返回目标元素*/
    fun parseSelector(control: BaseControl, selectorList: List<SelectorBean>?): List<WebElement> {
        val result = mutableListOf<WebElement>()

        when {
            control.driver == null -> L.e("驱动为空!")
            selectorList == null -> L.e("无选择器!")
            else -> _selectorParse(control.driver!!, selectorList, result)
        }

        return result
    }

    /**处理目标, 返回处理过的元素*/
    fun handle(
        control: BaseControl,
        handleList: List<HandleBean>?,
        originList: List<WebElement>? = null /*之前解析返回的元素列表*/
    ): HandleResult {
        val result = HandleResult()
        if (handleList.isNullOrEmpty()) {
            result.success = true
        } else {
            //处理

            //处理成功了的元素列表
            val handleElementList = mutableListOf<WebElement>()
            for (handleBean in handleList) {
                val handleResult = handle(control, handleBean, originList)
                result.success = handleResult.success || result.success

                if (handleResult.success) {
                    handleElementList.addAll(handleResult.elementList ?: emptyList())
                }
                if (handleBean.jump) {
                    //跳过后续处理
                    break
                }
                if (handleBean.jumpOnSuccess && handleResult.success) {
                    //成功之后跳过后续处理
                    break
                }
            }

            //总体处理成功
            if (result.success) {
                result.elementList = handleElementList
            }
        }
        return result
    }

    /**执行[HandleBean]*/
    fun handle(control: BaseControl, handleBean: HandleBean, originList: List<WebElement>? = null): HandleResult {
        val result = HandleResult()

        //需要操作的元素
        val elementList: List<WebElement>? = if (handleBean.selectList == null) {
            //如果没有重新执行选择列表, 则使用之前选择的元素
            originList
        } else {
            //重新选择元素
            parseSelector(control, handleBean.selectList)
        }

        //满足过滤条件之后, 需要处理的元素集合
        val filterElementList = _filterElement(elementList, handleBean.filter)

        //处理成功了的元素列表
        val handleElementList = mutableListOf<WebElement>()
        filterElementList.forEach { element ->
            handleBean.actionList?.forEach {
                control.handleAction(element, it).apply {
                    result.success = success || result.success
                    if (success) {
                        //把处理成功的元素收集起来
                        if (!handleElementList.containsAll(this.elementList ?: emptyList())) {
                            handleElementList.addAll(this.elementList!!)
                        }
                    }
                }
            }
        }
        if (result.success) {
            result.elementList = handleElementList
        }

        //后置处理
        if (handleBean.ignore) {
            result.success = false
        }

        return result
    }
}