package com.app.lzq.testbase.navigation

import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import com.app.lzq.testbase.R
import com.vc.base.BaseFgm
import kotlinx.android.synthetic.main.fragment_navigation4.*

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

class Fragment4 :BaseFgm(){
    override fun initLayout()= R.layout.fragment_navigation4
    override fun initViews() {
        var title=arguments!!.getString("title")
        tvContent.text=title
        var anim=AnimationUtils.loadAnimation(activity,R.anim.video_loding)
        progress.startAnimation(anim)
    }

    override fun initEvent() {
    }

}