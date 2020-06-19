package com.vc.base.dlg

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.jakewharton.rxbinding2.view.RxView
import com.vc.base.util.VIEW_THROTTLE_TIME
import com.vc.base.R
import java.util.concurrent.TimeUnit

abstract class BaseDlg : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, 0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val window = dialog!!.window
        val rootView = initView(inflater, window)
        initWindow(window)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(savedInstanceState)
    }

    private fun initWindow(window: Window) {
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))//设置背景透明
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//        window.setWindowAnimations(R.style.wmbase_dlg_anim)
    }

    private fun initView(inflater: LayoutInflater, window: Window): View {
        val rootLayout = inflater.inflate(R.layout.base_dlg_layout, window.findViewById(android.R.id.content), false) as ConstraintLayout
        val view: ConstraintLayout = inflater.inflate(setLayout(), rootLayout, false) as ConstraintLayout

        val pst: ConstraintLayout.LayoutParams = view.layoutParams as ConstraintLayout.LayoutParams

        when (initLayoutLocation()) {
            DlgLayoutLocation.TOP -> pst.topToTop = rootLayout.top
            DlgLayoutLocation.BOT -> pst.bottomToBottom = rootLayout.bottom
            DlgLayoutLocation.CENTER -> {
                pst.topToTop = rootLayout.top
                pst.bottomToBottom = rootLayout.bottom
            }
        }
        //默认横向剧中哦
        pst.leftToLeft = rootLayout.left
        pst.rightToRight = rootLayout.right
        rootLayout.addView(view, pst)

        RxView.clicks(rootLayout)
                .throttleFirst(VIEW_THROTTLE_TIME, TimeUnit.MILLISECONDS)
                .subscribe {
                    if (isAdded)
                        dismiss()
                }
        return rootLayout
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (isAdded)
            return
        super.show(manager!!, tag)
    }
    protected abstract fun setLayout(): Int

    protected abstract fun initLayoutLocation(): DlgLayoutLocation

    protected abstract fun initView(savedInstanceState: Bundle?)
}

//木钱 大小自适应，只进行位置设定 木钱根据需求只需要 top center 和bot  暂时不增加left 和reiht 因为基本不存在这种需求
enum class DlgLayoutLocation { TOP, CENTER, BOT }