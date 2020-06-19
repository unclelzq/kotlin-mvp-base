package com.vc.base.net

import android.util.Log
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.MalformedJsonException
import com.vc.base.R
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


abstract class BaseObserver<E : AppBaseResponse<*>> constructor(private val netItf: BaseNetItf) : Observer<E> {

    private val view by lazy { netItf }


    override fun onComplete() {
        view.showLoadingComplete()
    }

    override fun onSubscribe(d: Disposable) {
        netItf.pstAddDisposable(d)
    }

    override fun onNext(response: E) {
        Log.e("LzqOkhttp",response.toString())
        Log.e("LzqOkhttp",response.errorMsg.toString())
        Log.e("LzqOkhttp",response.status.toString())
        Log.e("LzqOkhttp",response.data.toString())
        when (response.errorCode) {
            RESULT_CODE_SUCCESS_ZERO -> {
                onSuccess(response)
            }
            RESULT_CODE_TOKEN_OUT -> {
                view.userLoginInvalid()
            }
            else -> onDataFailure(response)
        }
    }

    override fun onError(e: Throwable) {
        view.dismissLoadingDlg()
        view.showLoadingComplete()
        Log.e("1111",e.toString())
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

    open fun onDataFailure(response: E) {
        view.dismissLoadingDlg()

        response.errorMsg?.let {
            view.showDataException(it)
            return
        }
        view.showDataException(view.getStringRsc(R.string.net_data_not_error_str))
    }

    abstract fun onSuccess(response: E)
}