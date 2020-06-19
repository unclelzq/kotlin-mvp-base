package com.app.lzq.testbase

import com.app.lzq.testbase.cache.LocalDataManager
import com.vc.base.BasePst
import com.vc.base.net.AppBaseResponse
import com.vc.base.net.BaseObserver
import com.vc.base.net.NetLoadingDlg

/**
 *
 * @ProjectName:    TestBase
 * @Package:        com.app.lzq.testbase
 * @ClassName:      MainActivityPst
 * @Description:     java类作用描述
 * @Author:        刘智强
 * @CreateDate:     2019/1/9 14:24
 * @UpdateUser:     更新者
 * @UpdateDate:     2019/1/9 14:24
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

class  MainActivityPst(val dialog:NetLoadingDlg): BasePst<MainActivityCtt.View>(dialog),MainActivityCtt.Pst{
    override fun test() {

    }

    private  val retrofitSource by lazy { RetrofitSourceImpl() }
    override fun loadAsset(pubkey: String) {
        dialog.showDlg()
        view?.let { retrofitSource.getAsset(pubkey).apiSubscribes(object:BaseObserver<AppBaseResponse<String>>(it){
            override fun onSuccess(response: AppBaseResponse<String>) {
                response.data?.let { data->it.showAsset(data) }
            }
            override fun onComplete() {
                super.onComplete()
                dialog.dismiss()
            }
        }) }
    }
    override fun loadData(id:String) {
        dialog.showDlg()
        view?.let {
           retrofitSource.getData().apiSubscribes(object :BaseObserver<AppBaseResponse<KeyBean>>(it){
               override fun onSuccess(response: AppBaseResponse<KeyBean>) {
                   response.data?.let { data->it.showData(data) }
               }
               override fun onComplete() {
                   super.onComplete()
                   dialog.dismiss()
               }

           })
        }

    }

}

