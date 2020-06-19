package com.app.lzq.testbase

import android.os.Build
import android.util.Log
import com.vc.base.net.BaseRetrofitSource
import okhttp3.OkHttpClient
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.Proxy
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

/**
 *
 * @ProjectName:    TestBase
 * @Package:        com.vc.base.net
 * @ClassName:      RetrofitSourceImpl
 * @Description:     java类作用描述
 * @Author:        刘智强
 * @CreateDate:     2019/1/11 11:19
 * @UpdateUser:     更新者
 * @UpdateDate:     2019/1/11 11:19
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
class  RetrofitSourceImpl: BaseRetrofitSource {
    private var api: AppRetrofitApi

//    private fun token() = LocalDataMag.INSTANCE.getLoginUserBean()?.token ?: ""
//    private fun userId() = LocalDataMag.INSTANCE.getLoginUserBean()?.id ?: ""

    init {
        val okHttpClient = OkHttpClient()
        val clientBuilder = okHttpClient.newBuilder()
                .addNetworkInterceptor {
                    val request = it.request()
                    Log.e("LzqOKHTTP请求的地址",request?.url().toString())
                    val requestBody = request.body()
                    val bufferRequest = Buffer()
                    requestBody?.writeTo(bufferRequest)
                    var charset = Charset.forName("UTF-8")
                    val contentType = requestBody?.contentType()
                    if (contentType != null) {
                        charset = contentType.charset(Charset.forName("UTF-8"))
                    }
                    Log.e("LzqOKHTTP请求的参数",bufferRequest.readString(charset))
                    bufferRequest.close()

                    val builder = request.url()
                            .newBuilder()
                            .addQueryParameter("DEVICE_KEY", Build.MODEL)
                    return@addNetworkInterceptor it.proceed(request.newBuilder().url(builder.build()).build())
                }
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .proxy(Proxy.NO_PROXY)

//        if (BuildConfig.DEBUG) {
//            val logging = HttpLoggingInterceptor()
//            logging.level = HttpLoggingInterceptor.Level.BODY
//            clientBuilder.addNetworkInterceptor(logging)
//        }

        val mRetrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(clientBuilder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        api = mRetrofit.create(AppRetrofitApi::class.java)
    }

    fun getData()=api.getData()
    fun getAsset(pubkey:String)=api.getAsset(pubkey)

}