package com.angcyo.selenium.parse

import com.angcyo.javafx.base.OSInfo
import kotlin.math.max
import kotlin.random.Random
import kotlin.random.Random.Default.nextLong

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/30
 */
class TimeParse {

    companion object {

        /**根据系统, 随机获取一个时长*/
        fun randomTime(): Long {
            return if (OSInfo.isMacOS) {
                nextLong(1000, 5000)
            } else {
                nextLong(2000, 8000)
            }
        }
    }

    /**格式[5000,500,5] 解释:5000+500*[1-5),
     * 返回解析后的时间, 毫秒*/
    fun parse(arg: String?, def: Long = 0): Long {
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
}