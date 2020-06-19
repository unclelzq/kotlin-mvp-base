package com.app.lzq.testbase;

import android.content.Context;
import android.util.Log;
import android.util.Printer;
import android.widget.Toast;

import com.google.gson.Gson;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @ProjectName: TestBase
 * @Package: com.app.lzq.testbase
 * @ClassName: TestRx
 * @Description: java类作用描述
 * @Author: 刘智强
 * @CreateDate: 2019/1/18 10:43
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/1/18 10:43
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class TestRx {
    private Context context;

    public TestRx(Context context) {
        this.context = context;
    }

    private   String TAG="11111";



    public    void  test(){
        Observable.create(new ObservableOnSubscribe<Response>() {
            //创建被观察者
            @Override
            public void subscribe(@NonNull ObservableEmitter<Response> e) throws Exception {
                Request.Builder builder = new Request.Builder()
                        .url("http://api.avatardata.cn/MobilePlace/LookUp?key=ec47b85086be4dc8b5d941f5abd37a4e&mobileNumber=13021671512")
                        .get();
                Request request = builder.build();
                Call call = new OkHttpClient().newCall(request);
                Response response = call.execute();
                e.onNext(response);
            }
        }).map(new Function<Response, MobileAddress>() {
            //过滤数据
            @Override
            public MobileAddress apply(@NonNull Response response) throws Exception {
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        Log.e(TAG, "map:转换前:" + response.body());
                        return new Gson().fromJson(body.string(), MobileAddress.class);
                    }
                }
                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<MobileAddress>() {
                    //处理数据
                    @Override
                    public void accept(@NonNull MobileAddress s) throws Exception {
                        Log.e(TAG, "doOnNext: 保存成功：" + s.toString() + "\n");
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MobileAddress>() {
                    //创建观察者
                    @Override
                    public void accept(@NonNull MobileAddress data) throws Exception {
//                        Toast.makeText(context, "成功:" + data.getReason() + "\n",Toast.LENGTH_LONG).show();
                        Log.e(TAG, data.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG,"数据解析异常");
                    }
                }).dispose();
}
}


