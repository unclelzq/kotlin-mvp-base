package com.vc.base

import com.vc.base.net.AppBaseResponse
import com.vc.base.net.BaseNetItf
import com.vc.base.net.NetLoadingDlg
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream


open class BasePst<T : BaseNetItf>(dialog:NetLoadingDlg) : BasePstItf<T> {
    private val composite by lazy { CompositeDisposable() }
    var view: T? = null
    override fun takeView(view: T) {
        this.view = view
        this.view?.pstAddDisposable = {
            this.composite.add(it)
        }
        this.view?.netLoadingDlgDismiss = {
            this.composite.clear()
        }
    }
    override fun dropView() {
        this.composite.clear()
        this.view = null
    }
    protected fun <E> Observable<out AppBaseResponse<E>>.apiSubscribes(observer: Observer<AppBaseResponse<E>>) {
        this.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
    }
    protected fun Observable<out ResponseBody>.downloadFile(filePath: String, observer: Observer<Any>, showProgress: (progress: Int) -> Unit) {
        this.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .doOnNext {
                    val contentLength = it.contentLength()
                    val input = it.byteStream()
                    val file = File(filePath)
                    if (file.exists()) {
                        file.delete()
                    }

                    var fos: FileOutputStream? = null
                    try {
                        fos = FileOutputStream(file)
                        val b = ByteArray(8192)
                        var len: Int

                        var count = 0L
                        while (true) {
                            len = input.read(b)
                            if (len == -1)
                                break
                            fos.write(b, 0, len)
                            count += len
                            showProgress((count * 100 / contentLength).toInt())
                        }
                    } finally {
                        input.close()
                        fos?.close()
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
    }
}

