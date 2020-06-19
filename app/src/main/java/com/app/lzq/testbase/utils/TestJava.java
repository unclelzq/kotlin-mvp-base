package com.app.lzq.testbase.utils;

import android.app.ActivityManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.WindowManager;

/**
 * @ProjectName: TestBase
 * @Package: com.app.lzq.testbase.utils
 * @ClassName: TestJava
 * @Description: java类作用描述
 * @Author: 刘智强
 * @CreateDate: 2020/6/9 10:10
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/6/9 10:10
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class TestJava {
    HandlerThread handlerThread;
    Handler handler;
    void test(){
    handlerThread =  new HandlerThread("id");
      handlerThread.start();
    handler  =new Handler(handlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){

                }
            }
        };
    }

    void postMsg(){
        Message msg=Message.obtain();
        msg.what=1;
        handler.sendMessage(msg);

    }
    void quitLooper(){
        handlerThread.quit();
    }


}
