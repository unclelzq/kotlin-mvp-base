package com.vc.base.net

import android.util.MalformedJsonException
import com.google.gson.JsonSyntaxException
import com.vc.base.R
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class BaseDownloadObserver<Any> constructor(private val netItf: BaseNetItf) : Observer<Any> {

    private val view by lazy { netItf }

    override fun onComplete() {
        view.showLoadingComplete()
    }

    override fun onSubscribe(d: Disposable) {
        netItf.pstAddDisposable(d)
    }

    override fun onNext(t: Any) {
        view.dismissLoadingDlg()
        onSuccess(t)
    }

    override fun onError(e: Throwable) {
        view.dismissLoadingDlg()
        view.showLoadingComplete()
        when (e) {
            is SocketTimeoutException,
            is ConnectException,
            is UnknownHostException -> view.showNetworkException()
            is JsonSyntaxException,
            is NumberFormatException,
            is MalformedJsonException -> view.showException(view.getStringRsc(R.string.net_json_error_str))
            is HttpException -> view.showException(view.getStringRsc(R.string.net_http_error_str, e.code()))
            is NullPointerException -> view.showException(view.getStringRsc(R.string.net_null_error_str))
            else -> {
                view.showException(view.getStringRsc(R.string.net_unknown_error_str))
            }
        }
    }

    abstract fun onSuccess(response: Any)
}