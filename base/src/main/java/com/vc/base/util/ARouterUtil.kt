package com.vc.base.util

import android.content.Intent
import com.alibaba.android.arouter.launcher.ARouter
import com.vc.base.sp.SpHelper

class ARouterUtil {
    companion object {
        const val APP_MAIN_URL = "/app/main"

        fun startAppMain() {
            ARouter.getInstance().build(APP_MAIN_URL)
                    .navigation()
        }

        fun userLoginInvalid() {
            SpHelper.INSTANCE.clear()//清空 sp
            ARouter.getInstance().build(APP_MAIN_URL)
                    .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    .navigation()
        }
    }
}