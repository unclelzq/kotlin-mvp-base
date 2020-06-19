package com.vc.base

import android.app.Application
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.facebook.drawee.backends.pipeline.Fresco
import com.just.agentweb.LogUtils.isDebug
import com.tencent.bugly.Bugly
import com.tencent.bugly.crashreport.CrashReport.UserStrategy
import com.vc.base.config.FrescoConfig
import com.vc.base.sp.SpHelper


abstract class BaseApc : Application() {

    override fun onCreate() {
        super.onCreate()
        initBugly()
        initFresco()
        initSp()
        initArouter()
        initOther()
    }

    private fun initArouter() {
//        if (isDebug()) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
        ARouter.openLog()     // 打印日志
        ARouter.openDebug()   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
//        }
        ARouter.init(this) // 尽可能早，推荐在Application中初始化
    }

    private fun initSp() {
        SpHelper.INSTANCE.init(this, takeSpFileName())
    }

    private fun initFresco() {
        Fresco.initialize(this, FrescoConfig.instance.getImagePipelineConfig(this))
    }

    private fun initBugly() {
        val strategy = UserStrategy(this)
        strategy.appChannel = takeAppChannel()
        strategy.appPackageName = takeAppPackageName()
        strategy.appVersion = BuildConfig.VERSION_NAME
        Bugly.init(this, takeBuglyKey(), BuildConfig.DEBUG, strategy)
    }

    abstract fun takeSpFileName(): String

    abstract fun takeBuglyKey(): String

    abstract fun takeAppChannel(): String

    abstract fun takeAppPackageName(): String

    abstract fun initOther()
}