package com.vc.base.net

import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSink
import okio.ForwardingSink
import java.io.File
import okio.Sink
import java.io.IOException
import okio.Okio


class UploadRequestBody<E : AppBaseResponse<*>>(private val file: File, private val upLoadObserver: UploadObserver<E>) : RequestBody() {

    private val mRequestBody by lazy { RequestBody.create(MediaType.parse("application/octet-stream"), file) }

    override fun contentType(): MediaType? = mRequestBody.contentType()

    override fun contentLength(): Long = mRequestBody.contentLength()

    override fun writeTo(sink: BufferedSink?) {//注意拦截器
        val countingSink = object : ForwardingSink(sink) {

            private var bytesWritten = 0L
            private val contentLength = contentLength()

            @Throws(IOException::class)
            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                bytesWritten += byteCount
                upLoadObserver.onProgressChange(bytesWritten, contentLength)
            }
        }

        val bufferedSink = Okio.buffer(countingSink)
        // 写入
        mRequestBody.writeTo(bufferedSink)
        // 刷新
        // 必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush()
    }
}