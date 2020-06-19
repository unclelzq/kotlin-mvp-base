package com.app.lzq.testbase

import android.annotation.SuppressLint
import android.os.Bundle
import com.vc.base.BaseAct
import com.vc.base.dlg.BaseDlg
import com.vc.base.dlg.DlgLayoutLocation
import kotlinx.android.synthetic.main.activity_test_mutil_adapter.*


/**
 *
 * @ProjectName:    TestBase
 * @Package:        com.app.lzq.testbase
 * @ClassName:      TestMutilRecyclerView
 * @Description:     java类作用描述
 * @Author:        刘智强
 * @CreateDate:     2019/1/23 17:56
 * @UpdateUser:     更新者
 * @UpdateDate:     2019/1/23 17:56
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

class TestMutilRecyclerView:BaseAct(){
    private  val myDlg by lazy {

        return@lazy (@SuppressLint("ValidFragment")
        object:BaseDlg(){
            override fun setLayout()=R.layout.dlg_test


            override fun initLayoutLocation()=DlgLayoutLocation.BOT

            override fun initView(savedInstanceState: Bundle?) {

            }

        })
    }

    override fun initLayout()=R.layout.activity_test_mutil_adapter

    @SuppressLint("StringFormatMatches")
    override fun initViews(savedInstanceState: Bundle?) {
        rvContent.text=String.format(getString(R.string.test_para),"刘",23,"男")
//        myDlg.show(supportFragmentManager,"TAG")
    }

    override fun initEvent() {


    }


}

