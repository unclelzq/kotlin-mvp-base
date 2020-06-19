package com.vc.base.net

abstract class UploadObserver<E : AppBaseResponse<*>> constructor(netItf: BaseNetItf) : BaseObserver<E>(netItf) {

    fun onProgressChange(bytesWritten: Long, contentLength: Long) {
        onProgress((bytesWritten * 100 / contentLength).toInt())
    }

    abstract fun onProgress(progress: Int)
}