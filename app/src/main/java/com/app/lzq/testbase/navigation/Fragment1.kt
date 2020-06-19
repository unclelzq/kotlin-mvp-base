package com.app.lzq.testbase.navigation

import com.app.lzq.testbase.R
import com.vc.base.BaseFgm
import android.os.Bundle
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment1.*
import kotlinx.android.synthetic.main.fragment_navigation1.*


/**
 *
 * @ProjectName:    TestBase
 * @Package:        com.app.lzq.testbase.navigation
 * @ClassName:      Fragment1
 * @Description:     java类作用描述
 * @Author:        刘智强
 * @CreateDate:     2019/2/20 10:09
 * @UpdateUser:     更新者
 * @UpdateDate:     2019/2/20 10:09
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

class Fragment1 :BaseFgm(){

    private  val pagerAdapter by lazy {
         val f1 by lazy { return@lazy  HomeFragment() }
         val f2 by lazy { return@lazy  ProductFragment() }
         val f3 by lazy { return@lazy  MyFragment() }
         val f4 by lazy { return@lazy  SettingFragment() }
         val fragments by lazy{ arrayOf(f1,f2,f3,f4)}
        var titles= arrayOf("首页","产品","我的","设置")
        return@lazy  object: androidx.fragment.app.FragmentPagerAdapter(this.childFragmentManager){
            override fun getItem(p0: Int)=fragments[p0]
            override fun getCount()=fragments.size
            override fun getPageTitle(position: Int)=titles[position]
        }
    }
    override fun initLayout()= R.layout.fragment1
    override fun initViews() {
//     var  title=  arguments!!.getString("title")
//        tvTitle.text=title
        viewpager.currentItem = 0
        viewpager.adapter=pagerAdapter
        tablayout.setupWithViewPager(viewpager)
    }

    override fun initEvent() {
    }

}