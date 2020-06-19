package com.app.lzq.testbase

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import kotlinx.android.parcel.Parcelize

/**
 *
 * @ProjectName:    TestBase
 * @Package:        com.app.lzq.testbase
 * @ClassName:      DataClass
 * @Description:     java类作用描述
 * @Author:        刘智强
 * @CreateDate:     2019/1/11 15:04
 * @UpdateUser:     更新者
 * @UpdateDate:     2019/1/11 15:04
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
@Parcelize
data  class KeyBean(val prikey:String?,
                    val pubkey:String?):Parcelable