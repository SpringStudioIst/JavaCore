package com.angcyo.javafx.annotation


/**
 * 通过反射, 自动调用[lookup]方法获取[Node]
 * Email:angcyo@126.com
 * @author angcyo
 * @date 2021/01/08
 */

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class NodeInject(val css: String = "")