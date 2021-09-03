/*
 * *********************************************************
 *   author   zhuxuetong
 *   company  telchina
 *   email    zhuxuetong123@163.com
 *   date     18-6-19 下午1:38
 * ********************************************************
 */

package com.pivot.myandroiddemo.util;

import androidx.viewpager.widget.ViewPager;
import android.view.View;

/**
 * viewpager的切换动画渐显渐出
 */
public class ScaleInOutTransformer implements ViewPager.PageTransformer {
    /**
     * Called each {@link #transformPage(View, float)}.
     *
     * @param view     Apply the transformation to this page
     * @param position Position of page relative to the current front-and-center position of the pager. 0 is front and
     *                 center. 1 is one full page position to the right, and -1 is one page position to the left.
     */
    protected void onTransform(View view, float position) {
        view.setPivotX(position < 0 ? 0 : view.getWidth());
        view.setPivotY(view.getHeight() / 2f);
        float scale = position < 0 ? 1f + position : 1f - position;
        view.setScaleX(scale);
        view.setScaleY(scale);
    }

    /**
     * Apply a property transformation to the given page. For most use cases, this method should not be overridden.
     * Instead use {@link #transformPage(View, float)} to perform typical transformations.
     *
     * @param page     Apply the transformation to this page
     * @param position Position of page relative to the current front-and-center position of the pager. 0 is front and
     *                 center. 1 is one full page position to the right, and -1 is one page position to the left.
     */
    @Override
    public void transformPage(View page, float position) {
        onPreTransform(page, position);
        onTransform(page, position);
        onPostTransform(page, position);
    }


    /**
     * Called each {@link #transformPage(View, float)} before {{@link #onTransform(View, float)}.
     * <p>
     * The default implementation attempts to reset all view properties. This is useful when toggling transforms that do
     * not modify the same page properties. For instance changing from a transformation that applies rotation to a
     * transformation that fades can inadvertently leave a fragment stuck with a rotation or with some degree of applied
     * alpha.
     *
     * @param page     Apply the transformation to this page
     * @param position Position of page relative to the current front-and-center position of the pager. 0 is front and
     *                 center. 1 is one full page position to the right, and -1 is one page position to the left.
     */
    protected void onPreTransform(View page, float position) {
        final float width = page.getWidth();

        page.setRotationX(0);
        page.setRotationY(0);
        page.setRotation(0);
        page.setScaleX(1);
        page.setScaleY(1);
        page.setPivotX(0);
        page.setPivotY(0);
        page.setTranslationY(0);
        page.setTranslationX(isPagingEnabled() ? 0f : -width * position);

        if (hideOffscreenPages()) {
            page.setAlpha(position <= -1f || position >= 1f ? 0f : 1f);
            page.setEnabled(false);
        } else {
            page.setEnabled(true);
            page.setAlpha(1f);
        }
    }

    /**
     * Called each {@link #transformPage(View, float)} after {@link #onTransform(View, float)}.
     *
     * @param page     Apply the transformation to this page
     * @param position Position of page relative to the current front-and-center position of the pager. 0 is front and
     *                 center. 1 is one full page position to the right, and -1 is one page position to the left.
     */
    protected void onPostTransform(View page, float position) {
    }

    /**
     * If the position offset of a fragment is less than negative one or greater than one, returning true will set the
     * fragment alpha to 0f. Otherwise fragment alpha is always defaulted to 1f.
     */
    protected boolean hideOffscreenPages() {
        return true;
    }

    /**
     * Indicates if the default animations of the view pager should be used.
     */
    protected boolean isPagingEnabled() {
        return false;
    }
}
