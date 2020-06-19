package com.app.lzq.testbase.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.app.lzq.testbase.R
import com.app.lzq.testbase.bean.UserInfo
import com.app.lzq.testbase.databinding.ActivityTestDatabindingBinding
import com.tencent.bugly.crashreport.biz.UserInfoBean

/**
 *
 * @ProjectName:    TestBase
 * @Package:        com.app.lzq.testbase.ui
 * @ClassName:      TestDataBindingAct
 * @Description:     java类作用描述
 * @Author:        刘智强
 * @CreateDate:     2020/6/15 18:01
 * @UpdateUser:     更新者
 * @UpdateDate:     2020/6/15 18:01
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

class TestDataBindingAct :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     var dataBindingUtil=  DataBindingUtil.setContentView<ActivityTestDatabindingBinding>(this,R.layout.activity_test_databinding)
        dataBindingUtil.user= UserInfo("1","Lizi","110")
    }

}