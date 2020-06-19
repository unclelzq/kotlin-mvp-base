package com.app.lzq.testbase
import android.Manifest
import androidx.lifecycle.Transformations.map
import android.os.Bundle
import android.view.Gravity
import com.app.lzq.testbase.cache.LocalDataManager
import com.tencent.bugly.proguard.s
import com.vc.base.BaseAct
import com.vc.base.pms.PmsDlg
import com.vc.base.pms.PmsMag
import com.vc.base.pop.BasePop
import com.vc.base.rcy.CommonApt
import com.vc.base.rcy.CommonViewHolder
import com.vc.base.rcy.OnItemClickListener
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_item_main.view.*

class MainActivity : BaseAct(), MainActivityCtt.View {
    private var datas= mutableListOf<String>()
    private val pst by lazy { MainActivityPst(netLoadingDlg) }
    private  val pemMag by lazy{
       var pmg= PmsMag(this,{
           toast("权限申请成功")

        },{
           toast("权限申请失败")
        })

        return@lazy  pmg
    }
      private val mainAdapter by lazy {
          for (index  in 0..20){
              datas.add("测试数据->$index")
          }
        val adapter=MainAdapter(datas)
        adapter.clickFun={
            s:String,i:Int ->
            run {
                toast(s)
                toast("$i")
            }
        }
          return@lazy  adapter
    }

    private  val adapter by lazy {
        for (index  in 0..20){
            datas.add("测试数据->$index")
        }

         return@lazy object:CommonApt<String>(datas,R.layout.view_item_main){
             override fun dispay(holder: CommonViewHolder, item: String, position: Int) {
                holder. itemView.tvName.text=item
             }

         }.setClick(object:OnItemClickListener<String>{
             override fun onItemClickListener(position: Int, item: String, apt: CommonApt<String>) {
                 toast(item)

             }

         })



    }
    private  val popupWindow by lazy{

        return@lazy  object:BasePop(this){
            override fun initPopupLayout()=R.layout.view_mypopupwindow

            override fun initCreateView(mContext: BaseAct) {


            }

            override fun viewDrawCompleted() {

            }

        }

    }



//    private val adapter by lazy {
//        var apt=object:CommonApt<String>(dataList = datas, layout =R.layout.item_main_adapter){
//            override fun dispay(holder: CommonViewHolder, item: String, position: Int) {
//                holder.itemView.tvName.text=item
//            }
//        }.setClick(object:OnItemClickListener<String>{
//            override fun onItemClickListener(position: Int, item: String, apt: CommonApt<String>) {
//
//            }
//
//        })
//    }

    override fun initLayout()=R.layout.activity_main
    override fun initViews(savedInstanceState: Bundle?) {
        rvContent.adapter=adapter
        pemMag.requestRxPms(R.array.pm_array,R.array.pm_name_array)// 申請權限



//        pst.takeView(this)
//            RxPermissions(this).request(Manifest.permission.CAMERA).subscribe { t ->
//                if (t!!){
//                    toast("有权限")
//
//                }else{
//                    toast("无权限")
//                }
//            }
//        TestRx(this).test()

//Observable.interval(3, TimeUnit.SECONDS)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe { t->
//                    run {
//                        Toast.makeText(this,"RxJava间隔3秒周期性操作,当前时间:"+System.currentTimeMillis(),Toast.LENGTH_LONG).show();
//                    }
//                }


//       Observable.create(ObservableOnSubscribe<String> { emitter ->
//           emitter.onNext("hello");
//           emitter.onNext("android");
//           emitter.onComplete()
//       }).subscribe{  name("name",24)}

        Observable.just("sss")
                .map { if (it.length>3){
                    it.substring(0,1)
                } }
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()



    }


    override fun initEvent() {
//        tvButton.setOnClickListener {    pst.loadData("") }
//        tvButton.setOnClickListener {    pst.loadAsset(Config.PUBKEY) }
        btnOk.setOnClickListener {
//            finish()
//            LocalDataManager.logout(this)
            popupWindow.showAtLocation(btnOk,0,0,Gravity.BOTTOM)
        }
    }

    override fun showData(data:KeyBean) {
        toast(data?.prikey!!)

     }
    override fun showAsset(data: String) {
        toast(data)
    }

    override fun onDestroy() {
        super.onDestroy()
        pst.dropView()
    }

    override fun onPause() {
        super.onPause()
        pemMag.onPause()
    }

    override fun onRestart() {
        super.onRestart()
        pemMag.onRestart()
    }



    enum class  ParEnum{
        TOP,RIGHT,LEFT,BOTTOM
    }

}


