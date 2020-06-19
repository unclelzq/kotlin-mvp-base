package com.vc.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.jakewharton.rxbinding2.view.RxView
import com.vc.base.R.id.*
import com.vc.base.net.BaseNetItf
import com.vc.base.net.NetLoadingDlg
import com.vc.base.util.ARouterUtil
import com.vc.base.util.VIEW_THROTTLE_TIME
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.base_toolbar.*
import java.util.concurrent.TimeUnit
import android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.os.Build.VERSION.SDK_INT
import android.view.WindowManager


abstract class BaseAct : AppCompatActivity(), BaseNetItf {

     val netLoadingDlg by lazy {
        val netLoadingDlg = NetLoadingDlg.newInstace()
        netLoadingDlg.init({
            this.netLoadingDlgDismiss()
        }, {
            netLoadingDlg.show(supportFragmentManager, NetLoadingDlg::class.java.simpleName)
        })
        return@lazy netLoadingDlg
    }

    override var pstAddDisposable: (Disposable) -> Unit = {}
    override var netLoadingDlgDismiss: () -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //全屏的设置
//        supportActionBar!!.hide()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.statusBarColor = Color.TRANSPARENT
//        } else {
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        }
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        setContentView(initLayout())
        initViews(savedInstanceState)
        initEvent()
    }

    protected abstract fun initLayout(): Int

    protected abstract fun initViews(savedInstanceState: Bundle?)

    protected abstract fun initEvent()

    @SuppressLint("NewApi")
    protected fun initToolbar(titleText: String, backShow: Boolean = true, toolbarColor: Int = R.color.app_them_color) {
        if (backShow) {
            base_toolbar_back_img.visibility = View.VISIBLE
            RxView.clicks(base_toolbar_back_img)
                    .throttleFirst(VIEW_THROTTLE_TIME, TimeUnit.MILLISECONDS)
                    .subscribe {
                        finish()
                    }
        } else
            base_toolbar_back_img.visibility = View.INVISIBLE
        base_toolbar_bg_view.setBackgroundColor(resources.getColor(toolbarColor, theme))
        base_toolbar_title_tv.text = titleText
    }

    fun toast(rsc: Int) {
        Toast.makeText(this, rsc, Toast.LENGTH_SHORT).show()
    }

    fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun showNetworkException() {
        toast(R.string.net_work_error_str)
    }

    override fun showException(errorMsg: String) {
        toast(errorMsg)
    }

    override fun showDataException(errorDataMsg: String) {
        toast(errorDataMsg)
    }

    override fun showLoadingComplete() {
        Log.i("BaseAct", "showLoadingComplete")
        netLoadingDlg.showDlg()
    }

    override fun showLoadingDlg() {
        Log.i("BaseAct", "showLoadingDlg")
        netLoadingDlg.showDlg()
    }

    override fun dismissLoadingDlg() {
        Log.i("BaseAct", "dismissLoadingDlg")
        netLoadingDlg.dismiss()
    }

    override fun userLoginInvalid() {
        ARouterUtil.userLoginInvalid()
    }

    override fun getStringRsc(rsc: Int, vararg formatArgs: Any): String = getString(rsc, *formatArgs)

    override fun getRec(): Resources = resources
}

