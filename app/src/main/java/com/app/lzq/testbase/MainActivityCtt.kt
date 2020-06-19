package com.app.lzq.testbase

import com.vc.base.BasePstItf
import com.vc.base.BaseViewItf

/**
 *
 * @ProjectName:    TestBase
 * @Package:        com.app.lzq.testbase
 * @ClassName:      MainActivityCtt
 * @Description:     java类作用描述
 * @Author:        刘智强
 * @CreateDate:     2019/1/9 14:22
 * @UpdateUser:     更新者
 * @UpdateDate:     2019/1/9 14:22
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

interface  MainActivityCtt{
     interface View:BaseViewItf{
         fun showData(data:KeyBean)
         fun showAsset(data:String)
     }
    interface  Pst:BasePstItf<View>{
        fun loadData(id:String)
        fun loadAsset(pubkey:String)
        fun test()
    }
}