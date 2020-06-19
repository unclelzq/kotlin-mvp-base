package com.app.lzq.testbase.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 *
 * @ProjectName:    TestBase
 * @Package:        com.app.lzq.testbase.bean
 * @ClassName:      DataBean
 * @Description:     java类作用描述
 * @Author:        刘智强
 * @CreateDate:     2020/6/12 10:25
 * @UpdateUser:     更新者
 * @UpdateDate:     2020/6/12 10:25
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@Parcelize
data class  UserInfo(
        val id:String?,
        val  name:String?,
        val phone :String?
):Parcelable


data class  AAA( @SerializedName ("id") val id:String?)