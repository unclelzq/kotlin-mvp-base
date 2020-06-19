package com.app.lzq.testbase.navigation

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.app.lzq.testbase.R
import kotlinx.android.synthetic.main.activity_test_mutil_adapter.*
import kotlinx.android.synthetic.main.activity_test_mutil_adapter.view.*

/**
 *
 * @ProjectName:    TestBase
 * @Package:        com.app.lzq.testbase.navigation
 * @ClassName:      CustomerDialog
 * @Description:     java类作用描述
 * @Author:        刘智强
 * @CreateDate:     2019/2/22 17:57
 * @UpdateUser:     更新者
 * @UpdateDate:     2019/2/22 17:57
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

class CustomerDialog:DialogFragment(){

    override fun onStart() {
        super.onStart()

        if (dialog != null) {
           var dm =  DisplayMetrics();
            activity!!.windowManager.defaultDisplay.getMetrics(dm)
            dialog!!.window.setLayout((dm.widthPixels * 0.75).toInt(), ViewGroup.LayoutParams.MATCH_PARENT)
//            dialog!!.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE,0);

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog!!.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var rootView=inflater.inflate(R.layout.activity_test_mutil_adapter,container,false)
        rootView. btnOK.setOnClickListener {
      rootView.context.toast(rootView.rvContent.text.toString())
}
        return  rootView
    }

}