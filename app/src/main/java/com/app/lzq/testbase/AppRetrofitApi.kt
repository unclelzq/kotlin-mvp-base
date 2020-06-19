package com.app.lzq.testbase

import com.vc.base.net.AppBaseResponse
import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.http.Query

/**
 *
 * @ProjectName:    TestBase
 * @Package:        com.vc.base.net
 * @ClassName:      AppRetrofitApi
 * @Description:     java类作用描述
 * @Author:        刘智强
 * @CreateDate:     2019/1/11 11:20
 * @UpdateUser:     更新者
 * @UpdateDate:     2019/1/11 11:20
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
interface  AppRetrofitApi{
    @POST("/addr/getkeypair")
    fun  getData(): Observable<AppBaseResponse<KeyBean>>
    @POST("/asset")
    fun getAsset(@Query("pubkey") pubkey:String?):Observable<AppBaseResponse<String>>
    @POST("/login")
    fun login(@Query("name") name:String?, @Query("password") psd:String?):Observable<AppBaseResponse<String>>
}