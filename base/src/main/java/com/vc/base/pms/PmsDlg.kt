package com.vc.base.pms

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import com.jakewharton.rxbinding2.view.RxView
import com.vc.base.dlg.BaseDlg
import com.vc.base.dlg.DlgLayoutLocation
import com.vc.base.R
import com.vc.base.util.VIEW_THROTTLE_TIME
import kotlinx.android.synthetic.main.pms_dlg_layout.*
import java.util.concurrent.TimeUnit

class PmsDlg : BaseDlg() {


    companion object {
        private const val PMS_NAMES = "pms_names"

        fun newInstance(pmsNames: String): PmsDlg {
            val pmsDlg = PmsDlg()
            val args = Bundle()
            args.putString(PMS_NAMES, pmsNames)
            pmsDlg.arguments = args
            return pmsDlg
        }
    }

    override fun setLayout(): Int = R.layout.pms_dlg_layout

    override fun initLayoutLocation(): DlgLayoutLocation = DlgLayoutLocation.CENTER

    override fun initView(savedInstanceState: Bundle?) {
        pms_dlg_content.text = getString(R.string.pms_dlg_content, arguments?.getString(PMS_NAMES))
        RxView.clicks(pms_dlg_btn)
                .throttleFirst(VIEW_THROTTLE_TIME, TimeUnit.MILLISECONDS)
                .subscribe {
                    activity?.let {
                        getAppDetailSettingIntent(it)
                    }
                }
    }

    /**
     * 跳转到权限设置界面
     */
    @SuppressLint("ObsoleteSdkInt")
    private fun getAppDetailSettingIntent(context: Context) {
        // vivo 点击设置图标>加速白名单>我的app
        //      点击软件管理>软件管理权限>软件>我的app>信任该软件
        var appIntent = context.packageManager.getLaunchIntentForPackage("com.iqoo.secure")
        if (appIntent != null) {
            context.startActivity(appIntent)
            return
        }

        // oppo 点击设置图标>应用权限管理>按应用程序管理>我的app>我信任该应用
        //      点击权限隐私>自启动管理>我的app
        appIntent = context.packageManager.getLaunchIntentForPackage("com.oppo.safe")
        if (appIntent != null) {
            context.startActivity(appIntent)
            return
        }

        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= 9) {
            intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            intent.data = Uri.fromParts("package", context.packageName, null)
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.action = Intent.ACTION_VIEW
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails")
            intent.putExtra("com.android.settings.ApplicationPkgName", context.packageName)
        }
        context.startActivity(intent)
    }
}