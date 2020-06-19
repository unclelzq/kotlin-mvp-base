package com.vc.base.widget

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.vc.base.BaseFgm
import android.view.MotionEvent
import com.zhouwei.mzbanner.MZBannerView.ViewPagerScroller
import android.view.ViewParent


class TabFgmPageApt(fm: androidx.fragment.app.FragmentManager, private val fragments: List<BaseFgm>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): androidx.fragment.app.Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {}
}

class TabViewPageApt(private val views: List<View>) : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = views.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(views[position])
        //每次滑动的时候把视图添加到viewpager
        return views[position]
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // TODO Auto-generated method stub
        // 将当前位置的View移除
        container.removeView(views[position])
    }
}

class NoScrollViewPager : androidx.viewpager.widget.ViewPager {
    private var isScroll: Boolean = true

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
    constructor(context: Context) : super(context) {}

    /**
     * 1.dispatchTouchEvent一般情况不做处理
     * ,如果修改了默认的返回值,子孩子都无法收到事件
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return super.dispatchTouchEvent(ev)   // return true;不行
    }

    /**
     * 是否拦截
     * 拦截:会走到自己的onTouchEvent方法里面来
     * 不拦截:事件传递给子孩子
     */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        // return false;//可行,不拦截事件,
        // return true;//不行,孩子无法处理事件
        //return super.onInterceptTouchEvent(ev);//不行,会有细微移动
        return if (isScroll) {
            parent.requestDisallowInterceptTouchEvent(true)
            super.onInterceptTouchEvent(ev)
        } else {
            false
        }
    }

    /**
     * 是否消费事件
     * 消费:事件就结束
     * 不消费:往父控件传
     */
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        //return false;// 可行,不消费,传给父控件
        //return true;// 可行,消费,拦截事件
        //super.onTouchEvent(ev); //不行,
        //虽然onInterceptTouchEvent中拦截了,
        //但是如果viewpage里面子控件不是viewgroup,还是会调用这个方法.

        return if (isScroll) {
            super.onTouchEvent(ev)
        } else {
            true// 可行,消费,拦截事件
        }
    }

    fun setScroll(scroll: Boolean) {
        isScroll = scroll
    }
}