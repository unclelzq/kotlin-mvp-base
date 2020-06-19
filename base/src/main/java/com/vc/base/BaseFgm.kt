package com.vc.base

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.vc.base.net.BaseNetItf
import com.vc.base.net.NetLoadingDlg
import com.vc.base.util.ARouterUtil
import io.reactivex.disposables.Disposable

abstract class BaseFgm : Fragment(), BaseNetItf, View.OnTouchListener {

     val netLoadingDlg by lazy {
        val netLoadingDlg = NetLoadingDlg.newInstace()
        netLoadingDlg.init({
            this.netLoadingDlgDismiss()
        }, {
            netLoadingDlg.show(childFragmentManager, NetLoadingDlg::class.java.simpleName)
        })
        return@lazy netLoadingDlg
    }

    override var pstAddDisposable: (Disposable) -> Unit = {}

    override var netLoadingDlgDismiss: () -> Unit = {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(initLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnTouchListener(this)
        initViews()
        initEvent()
    }

    protected abstract fun initLayout(): Int

    protected abstract fun initViews()

    protected abstract fun initEvent()

    protected fun toast(rsc: Int) {
        Toast.makeText(activity, rsc, Toast.LENGTH_SHORT).show()
    }

    protected fun toast(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
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
        Log.i("BaseFgm", "showLoadingComplete")
    }

    override fun showLoadingDlg() {
        Log.i("BaseFgm", "showLoadingDlg")
        netLoadingDlg.showDlg()
    }

    override fun dismissLoadingDlg() {
        Log.i("BaseFgm", "dismissLoadingDlg")
        netLoadingDlg.dismiss()
    }

    override fun userLoginInvalid() {
        ARouterUtil.userLoginInvalid()
    }

    override fun getStringRsc(rsc: Int, vararg formatArgs: Any): String = getString(rsc, *formatArgs)

    override fun getRec(): Resources = resources

    override fun onTouch(v: View?, event: MotionEvent?): Boolean = true
}