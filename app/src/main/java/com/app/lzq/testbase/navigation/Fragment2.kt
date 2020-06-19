package com.app.lzq.testbase.navigation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.MainThread
import androidx.annotation.RequiresApi
import android.util.Log
import com.app.lzq.testbase.R
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxCompoundButton
import com.jakewharton.rxbinding2.widget.RxTextView
import com.just.agentweb.LogUtils
import com.tencent.bugly.proguard.t
import com.vc.base.BaseFgm
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function3
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_navigation2.*
import java.util.*
import java.util.concurrent.TimeUnit


/**
 *
 * @ProjectName:    TestBase
 * @Package:        com.app.lzq.testbase.navigation
 * @ClassName:      Fragment1
 * @Description:     java类作用描述
 * @Author:        刘智强
 * @CreateDate:     2019/2/20 10:09
 * @UpdateUser:     更新者
 * @UpdateDate:     2019/2/20 10:09
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

class Fragment2 :BaseFgm(){
    private  var disposable:Disposable?=null
    override fun initLayout()= R.layout.fragment_navigation2
    @RequiresApi(Build.VERSION_CODES.M)
    override fun initViews() {
       var title= arguments!!.getString("title")
//        tvContent.text=title
//        testRxjava()
//        testContact()
//        testCom()
        RxCompoundButton.checkedChanges(cbPro)
                .subscribe {
                    btnOk.isEnabled=it
                    btnOk.setBackgroundColor(if (it) activity!!.getColor(R.color.colorPrimaryDark)
                    else activity!!.getColor(R.color.colorPrimary))
                }
    }

    override fun initEvent() {

        btnOk.setOnClickListener { toast("可以点击了")
        var dialog=CustomerDialog()
        dialog.show(childFragmentManager,"Tag")}

    }

    /**
     * 无限轮询，在一定条件下，取消订阅，关闭轮询
     */
    fun testRxjava(){
       Observable.interval(2,1,TimeUnit.SECONDS)
               .doOnNext { t ->
                   run {
                       Log.e("TAG", "第$t 秒开始轮询")
                       if (t==10L){//取消订阅
                           disposable?.dispose()

                       }
                   }
               }
               .subscribe(object:Observer<Long>{
                   override fun onComplete() {

                   }

                   override fun onSubscribe(d: Disposable) {
                       disposable=d
                   }

                   override fun onNext(t: Long) {
                       Log.e("TAG","第$t 秒开始接受到消息")
                   }

                   override fun onError(e: Throwable) {
                   }

               })

    }
    fun testContact(){
        var  memoryCache:String?="从缓存中读取数据"
        var dislCache="从磁盘缓存中获取数据"
        //创建内存判断的被观察者,判断是否有该数据的缓存
       var  memory=object:ObservableSource<String> {
           override fun subscribe(observer: Observer<in String>) {
               if (memoryCache!=null){
                   observer.onNext(memoryCache)
               }else{
                   observer.onComplete()
               }

           }



       }
        var  disk= ObservableSource<String> { ob ->
            if (dislCache!=null){
                ob.onNext(dislCache)

            }else{
                ob.onComplete()
            }
        }
        var netWork=Observable.just("从网络获取数据")
        Observable.concat(memory,disk,netWork)
                .firstElement()
                .subscribe{s->
                    run {
                        Log.e("TAG", "最终获取的数据来源 =  " + s)
                    }
                }


    }
    fun testMergr(){
        var result="数据来源自:"
        var netWork=Observable.just("网络数据")
        var file=Observable.just("本地缓存数据")
        Observable.merge(netWork,file)
                .subscribe(object:Observer<String>{
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: String) {
                    }

                    override fun onError(e: Throwable) {
                    }

                })
    }
    @SuppressLint("CheckResult")
    fun testCom(){
        //对多个数据的监控，然后将结果分发出去

        var name=RxTextView.textChanges(etname).skip(1)
        var age=RxTextView.textChanges(etage).skip(1)
        var addr=RxTextView.textChanges(etname).skip(1)

       Observable.combineLatest(name,age,object:BiFunction<CharSequence,CharSequence,Boolean>{
           override fun apply(t1: CharSequence, t2: CharSequence): Boolean {
               return !etname.text.trim().isNullOrEmpty()&&!etage.text.trim().isNullOrEmpty()
           }

       }).subscribe { t -> btnOk.isEnabled=t!! }
        Observable.just("ssssss")
                .doOnNext {s->run{
                    if (s.length>5){
                        s.replace("s","a")

                    }
                } }

                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{s-> toast(s)}

    }

    fun  <E>set(key:String, value:E){
        Observable.create<E>{
            it.onNext(value)

        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {

                }
                .subscribe({
                    toast("$it")

                },{
                    toast("存储发生错误")
                })


        Observable.just("ttt")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object:Consumer<String>{
                    override fun accept(t: String?) {

                    }

                },object:Consumer<Throwable>{
                    override fun accept(t: Throwable?) {
                        toast("${t.toString()}")

                    }

                })

        RxView.clicks(btnOk)
                .throttleFirst(2,TimeUnit.SECONDS)
                .subscribe(object:Consumer<Any>{
                    override fun accept(t: Any?) {

                    }

                })


    }

}


