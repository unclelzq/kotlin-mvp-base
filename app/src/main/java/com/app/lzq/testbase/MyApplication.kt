package com.app.lzq.testbase

import android.content.Context
import android.os.Build
import androidx.multidex.MultiDex
import com.vc.base.BaseApc

/**
 *
 * @ProjectName:    TestBase
 * @Package:        com.app.lzq.testbase
 * @ClassName:      MyApplication
 * @Description:     java类作用描述
 * @Author:        刘智强
 * @CreateDate:     2019/1/11 10:15
 * @UpdateUser:     更新者
 * @UpdateDate:     2019/1/11 10:15
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
 class  MyApplication:BaseApc(),Thread.UncaughtExceptionHandler{
   override fun attachBaseContext(base: Context?) {
      super.attachBaseContext(base)
      MultiDex.install(this)
   }

    override fun takeSpFileName()=""

    override fun takeBuglyKey()=""

    override fun takeAppChannel()="web"
    override fun takeAppPackageName()= packageName

    override fun initOther() {
    }

    override fun uncaughtException(t: Thread?, e: Throwable?) {

    }

}