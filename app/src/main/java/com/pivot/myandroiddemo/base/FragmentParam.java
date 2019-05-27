/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 下午5:16
 * ********************************************************
 */

package com.pivot.myandroiddemo.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface FragmentParam {
    boolean ISSHOWTOOLBAR_DEF_VALUE       = false;
    boolean ISSHOWRETURN_DEF_VALUE        = false;
    boolean ISIMMERSEPADDINGTOP_DEF_VALUE = false;

    /**
     * 是否显示toolbar
     */
    boolean isShowToolBar() default ISSHOWTOOLBAR_DEF_VALUE;

    /**
     * 是否显示返回按钮，默认不显示，带返回按钮，带返回操作
     */
    boolean isShowReturn() default ISSHOWRETURN_DEF_VALUE;

    /**
     * 如果是沉浸式状态栏，是否空出顶部距离
     */
    boolean isImmersePaddingTop() default ISIMMERSEPADDINGTOP_DEF_VALUE;
}