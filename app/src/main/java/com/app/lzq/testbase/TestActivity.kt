package com.app.lzq.testbase

import android.os.Bundle
import android.os.PersistableBundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.vc.base.widget.PageWidget

/**
 *
 * @ProjectName:    TestBase
 * @Package:        com.app.lzq.testbase
 * @ClassName:      TestActivity
 * @Description:     java类作用描述
 * @Author:        刘智强
 * @CreateDate:     2019/9/12 14:27
 * @UpdateUser:     更新者
 * @UpdateDate:     2019/9/12 14:27
 * @UpdateRemark:   更新说明
 * @Version:        1.0

 */

class TestActivity  :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(PageWidget(this@TestActivity))
    }

}