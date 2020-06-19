package com.app.lzq.testbase.navigation

import com.app.lzq.testbase.KeyBean
import com.vc.base.BasePstItf
import com.vc.base.BaseViewItf

/**
 *
 * @ProjectName:    TestBase
 * @Package:        com.app.lzq.testbase.navigation
 * @ClassName:      HomeFragmentCtt
 * @Description:     java类作用描述
 * @Author:        刘智强
 * @CreateDate:     2019/2/20 17:05
 * @UpdateUser:     更新者
 * @UpdateDate:     2019/2/20 17:05
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
interface  HomeFragmentCtt{
    interface  View:BaseViewItf{
        fun showData(key:KeyBean)

    }
    interface  Pst:BasePstItf<View>{
        fun  loadData()

    }
}