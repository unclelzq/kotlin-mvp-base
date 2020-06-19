package com.vc.base.net

import android.content.DialogInterface
import android.os.Bundle
import com.vc.base.dlg.BaseDlg
import com.vc.base.dlg.DlgLayoutLocation
import com.vc.base.R


class NetLoadingDlg : BaseDlg() {

    companion object {
        fun newInstace() = NetLoadingDlg()
    }
    private var dlgDismissLse: () -> Unit = {}
    private var showLoadingDlg: () -> Unit = {}
    private var isShow = false

    override fun setLayout(): Int = R.layout.net_loading_dlg

    override fun initLayoutLocation(): DlgLayoutLocation = DlgLayoutLocation.CENTER

    override fun initView(savedInstanceState: Bundle?) {}

    fun init(dlgDismissLse: () -> Unit, showLoadingDlg: () -> Unit) {
        this.dlgDismissLse = dlgDismissLse
        this.showLoadingDlg = showLoadingDlg
    }
    fun showDlg() {
        if (!isShow) {
            isShow = true
            showLoadingDlg()
        }
    }

    override fun dismiss() {
        if (isShow) super.dismiss()
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog!!)
        dlgDismissLse()
        isShow = false
    }
}