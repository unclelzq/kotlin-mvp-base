package com.app.lzq.testbase.navigation

import com.app.lzq.testbase.KeyBean
import com.app.lzq.testbase.RetrofitSourceImpl
import com.vc.base.BasePst
import com.vc.base.BasePstItf
import com.vc.base.net.AppBaseResponse
import com.vc.base.net.BaseObserver
import com.vc.base.net.NetLoadingDlg

/**
 *
 * @ProjectName:    TestBase
 * @Package:        com.app.lzq.testbase.navigation
 * @ClassName:      HomeFragmentPst
 * @Description:     java类作用描述
 * @Author:        刘智强
 * @CreateDate:     2019/2/20 17:07
 * @UpdateUser:     更新者
 * @UpdateDate:     2019/2/20 17:07
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

class HomeFragmentPst(val dialog:NetLoadingDlg):BasePst<HomeFragmentCtt.View>(dialog),HomeFragmentCtt.Pst{
    private  val retrofitSource by lazy { RetrofitSourceImpl() }
    override fun loadData() {
        view!!.let {
            retrofitSource.getData().apiSubscribes(object:BaseObserver<AppBaseResponse<KeyBean>>(it){
                override fun onSuccess(response: AppBaseResponse<KeyBean>) {
                    response.data!!.let { data->it.showData(data) }
                }
            })
        }
    }
}