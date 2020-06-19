package com.vc.base.pms

import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.vc.base.R
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions


class PmsMag(private val act: AppCompatActivity, private val pmsAllGranted: () -> Unit, private val pmsError: (msg:String) -> Unit) {
    private var noPmsMap: MutableMap<String, String>? = null//还未申请的权限map
    private val rxPms by lazy { RxPermissions(act) }
    private var dlg: PmsDlg? = null

    fun requestRxPms(pmsArrayRsc: Int, pmsNameArrayRsc: Int){
        val pmsArray = act.resources.getStringArray(pmsArrayRsc)
        val pmsNameArray = act.resources.getStringArray(pmsNameArrayRsc)
        if (pmsArray.size != pmsNameArray.size) {
            pmsError(act.getString(R.string.pms_array_error_str))
        }
        noPmsMap = mutableMapOf()
        for (i in 0 until pmsArray.size) {
            if (!rxPms.isGranted(pmsArray[i]))
                noPmsMap!![pmsArray[i]] = pmsNameArray[i]
        }
        return if (noPmsMap!!.isNotEmpty()) {
            pmsRequest(*pmsArray) //有权限申请失败
        } else {
            pmsAllGranted()// 权限申请通过
        }
    }

    private fun pmsRequest(vararg pmsArray: String) {//开始申请权限，或者验证权限
        rxPms.requestEach(*pmsArray)
                .subscribe({
                    pmsFilter(it)
                }, {
                    pmsEnd()
                }, {
                    pmsEnd()
                })
    }

    private fun pmsFilter(pms: Permission) {//申请的权限过滤
        when {
            pms.granted -> noPmsMap?.remove(pms.name)
            pms.shouldShowRequestPermissionRationale -> Log.i(PmsMag::class.java.simpleName, "权限申请：Denied permission without ask never again")
            else -> Log.i(PmsMag::class.java.simpleName, "权限申请：Denied permission with ask never again Need to go to the settings")
        }
    }

    private fun noPmsFilter() {//对还未申请的权限map做过滤
        val pmsNames = StringBuffer()
        noPmsMap!!.forEach {
            pmsNames.append(it.value).append(',')
        }
        dlg = PmsDlg.newInstance(pmsNames.toString())
        dlg!!.show(act.supportFragmentManager, PmsDlg::class.java.simpleName)
    }

    private fun pmsEnd() {
        if (noPmsMap?.size ?: 0 > 0) {
            noPmsFilter()
        } else
            pmsAllGranted()
    }

    fun onPause() {//需设置到生命周期
        dlg?.let {
            if (it.isAdded)
                it.dismiss()
        }
    }

    fun onRestart() {//需设置到生命周期 ,验证权限
        dlg?.let {
            if (it.isAdded)
                it.dismiss()
        }
        if (noPmsMap?.size ?: 0 > 0) {
            val pmsArray = mutableListOf<String>()
            noPmsMap!!.forEach {
                pmsArray.add(it.key)
            }
            pmsRequest(*pmsArray.toTypedArray())
        }
    }
}