package com.angcyo.selenium.parse

import com.angcyo.library.ex.have
import com.angcyo.library.ex.patternList

/**
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2020/12/31
 */

/**从字符串中, 获取所有正负整数*/
fun String?.getIntList(): List<Int> {
    val regex = "[-]?\\d+".toRegex()
    return patternList(regex.toPattern()).mapTo(mutableListOf()) { it.toIntOrNull() ?: 0 }
}

/**字符串中, 是否包含分隔符 ~*/
fun String?.havePartition(text: CharSequence = "~") = have(text)

/**修正索引, 比如是负数的索引*/
fun Int.revise(size: Int) = if (this < 0) this + size else this
fun Long.revise(size: Int) = if (this < 0) this + size else this

/**列表中的每一项, 都需要匹配返回一个非空的集合, 最终才会合并返回所有的集合*/
fun <T, R> List<T>.eachMatchItem(action: (T) -> List<R>): List<R> {
    val result = mutableListOf<R>()
    var fail = false
    for (item in this) {
        val list = action(item)
        if (list.isEmpty()) {
            fail = true
            break
        }
        result.addAll(list)
    }
    return if (fail) emptyList() else result
}