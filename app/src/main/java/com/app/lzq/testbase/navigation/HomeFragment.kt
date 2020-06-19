package com.app.lzq.testbase.navigation

import android.content.Intent
import com.app.lzq.testbase.KeyBean
import com.app.lzq.testbase.R
import com.app.lzq.testbase.ui.TestCoroutineAct
import com.jakewharton.rxbinding2.view.RxView
import com.vc.base.BaseFgm
import kotlinx.android.synthetic.main.fragment_navigation1.*
import java.util.concurrent.TimeUnit

/**
 *
 * @ProjectName:    TestBase
 * @Package:        com.app.lzq.testbase.navigation
 * @ClassName:      HomeFragment
 * @Description:     java类作用描述
 * @Author:        刘智强
 * @CreateDate:     2019/2/20 14:43
 * @UpdateUser:     更新者
 * @UpdateDate:     2019/2/20 14:43
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

class  HomeFragment:BaseFgm(),HomeFragmentCtt.View{
    override fun showData(key: KeyBean) {
        toast(key.pubkey!!)
    }

    private val pst by lazy { HomeFragmentPst(netLoadingDlg) }
    override fun initLayout()= R.layout.fragment_navigation1
    override fun initViews() {
        pst.takeView(this)
        pst.loadData()
    }

    override fun initEvent() {
        RxView.clicks(btnTest).throttleFirst(1000,TimeUnit.MILLISECONDS)
                .subscribe {
                    startActivity(Intent(activity,TestCoroutineAct::class.java))
                }
    }

    override fun onDetach() {
        super.onDetach()
        pst.dropView()
    }

}