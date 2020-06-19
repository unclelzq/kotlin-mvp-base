package com.vc.base.net

import android.content.res.Resources
import io.reactivex.disposables.Disposable

interface BaseNetItf {
    var pstAddDisposable: (Disposable) -> Unit

    var netLoadingDlgDismiss: () -> Unit

    /**
     * 数据请求发生网络异常时调用。
     */
    fun showNetworkException()

    /**
     * 发生error
     */
    fun showException(errorMsg: String)

    /**
     * 数据成功返回但不是预期值时调用。
     */
    fun showDataException(errorDataMsg: String)

    /**
     * 显示加载完成的UI(e.g. 复位 Ultra-Ptr头部或尾部)
     */
    fun showLoadingComplete()

    /**
     * 显示进度条dlg
     */
    fun showLoadingDlg()

    /**
     * 关闭进度条dlg
     */
    fun dismissLoadingDlg()

    /**
     * 获取一些错误字符串拉 和其他用处拉
     */
    fun getStringRsc(rsc: Int, vararg formatArgs: Any): String

    /**
     * 资源对象
     */
    fun getRec(): Resources

    /**
     * 登录过期
     */
    fun userLoginInvalid()
}