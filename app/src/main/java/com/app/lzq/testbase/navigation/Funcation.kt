package com.app.lzq.testbase.navigation

import android.content.Context
import android.widget.Toast

/**
 *
 * @ProjectName:    TestBase
 * @Package:        com.app.lzq.testbase.navigation
 * @ClassName:      Funcation
 * @Description:     java类作用描述
 * @Author:        刘智强
 * @CreateDate:     2019/2/25 11:49
 * @UpdateUser:     更新者
 * @UpdateDate:     2019/2/25 11:49
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */


fun  Context.toast(msg:String, durarion: Int =Toast.LENGTH_LONG){
    Toast.makeText(this,msg,durarion).show()
}