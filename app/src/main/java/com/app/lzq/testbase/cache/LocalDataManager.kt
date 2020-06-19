package com.app.lzq.testbase.cache

import android.app.Activity
import android.content.Intent
import com.app.lzq.testbase.MainActivity
import com.google.gson.Gson
import com.vc.base.sp.SpHelper

/**
 *
 * @ProjectName:    TestBase
 * @Package:        com.vc.base.sp
 * @ClassName:      LocalDataManager
 * @Description:    本地数据存储类
 * @Author:        刘智强
 * @CreateDate:     2019/2/19 15:26
 * @UpdateUser:     更新者
 * @UpdateDate:     2019/2/19 15:26
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

private const val  USER_LOGIN_INFO="user_login_info"//用户登录之后获取的数据信息

class  LocalDataManager{

    companion object {
        fun saveUSserLoginInfo(loginBean: LoginBean){
            SpHelper.INSTANCE.set(USER_LOGIN_INFO,Gson().toJson(loginBean))
        }
        fun getUserLoginInfo()= Gson().fromJson(SpHelper.INSTANCE.getString(USER_LOGIN_INFO),LoginBean::class.java )

        fun logout(activity:Activity){
            var intent=Intent(activity,MainActivity::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK
            activity.startActivity(intent)
            clearLocalData()
        }


        fun clearLocalData(){
       SpHelper.INSTANCE.clear()
   }

    }

}