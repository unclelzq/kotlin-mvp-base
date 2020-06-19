package com.app.lzq.testbase.ui

import android.os.Bundle
import com.app.lzq.testbase.R
import com.vc.base.BaseAct
import kotlinx.android.synthetic.main.act_test_coroutine.*
import kotlinx.coroutines.*

/**
 *
 * @ProjectName:    TestBase
 * @Package:        com.app.lzq.testbase.ui
 * @ClassName:      TestCoroutineAct
 * @Description:     java类作用描述
 * @Author:        刘智强
 * @CreateDate:     2020/6/15 14:54
 * @UpdateUser:     更新者
 * @UpdateDate:     2020/6/15 14:54
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

class TestCoroutineAct :BaseAct(){
    override fun initLayout()= R.layout.act_test_coroutine

    override fun initViews(savedInstanceState: Bundle?) {
       GlobalScope.launch(Dispatchers.Main) {
           tvContent1.text= withContext(Dispatchers.Default){
               delay(3000)
               "data loading has finished"
           }
       }
        GlobalScope.launch (Dispatchers.Main){
            tvContent2.text= withContext(Dispatchers.Default){
                delay(3000)
                "task1已经执行完成，task2开始执行"
            }
            tvContent2.text= withContext(Dispatchers.Main){
                delay(3000)
                "task2执行完成"
            }
        }


        tvContent1.text="data is loading please wait"
        tvContent2.text="data is loading please wait"
        tvContent3.text="data is loading please wait"

    }

    override fun initEvent() {
    }

}