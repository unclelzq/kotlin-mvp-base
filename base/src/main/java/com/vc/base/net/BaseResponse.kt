package com.vc.base.net

import com.google.gson.annotations.SerializedName

const val RESULT_CODE_SUCCESS_ZERO = 0

const val RESULT_CODE_TOKEN_OUT = 403//token过期

//interface BaseRes<out DataType> {
//    val errorCode: Int?
//    val errorMsg: String?
//    val status:String?
//    val data: DataType?
//}
data class AppBaseResponse<out DataType>(@SerializedName("errorCode")  val errorCode: Int?,
                                         @SerializedName("errorMsg")  val errorMsg: String?,
                                         @SerializedName("status")  val  status:String?,
                                         @SerializedName("data")  val data: DataType?)