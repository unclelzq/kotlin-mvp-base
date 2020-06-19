package com.app.lzq.testbase.cache

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *
 * @ProjectName:    TestBase
 * @Package:        com.vc.base.sp
 * @ClassName:      LoginBean
 * @Description:     数据类
 * @Author:        刘智强
 * @CreateDate:     2019/2/19 15:31
 * @UpdateUser:     更新者
 * @UpdateDate:     2019/2/19 15:31
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@Parcelize
data class  LoginBean(val name:String):Parcelable
@Parcelize
data class  UserInfo(val username:String):Parcelable