package com.app.lzq.testbase.navigation

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.util.Log
import com.app.lzq.testbase.R
import com.app.lzq.testbase.R.id.btnsave
import com.app.lzq.testbase.R.id.rvDevice
import com.app.lzq.testbase.utils.BlueToothUtils
import com.vc.base.BaseFgm
import com.vc.base.net.NetLoadingDlg
import com.vc.base.rcy.CommonApt
import com.vc.base.rcy.CommonViewHolder
import com.vc.base.rcy.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_navigation3.*
import kotlinx.android.synthetic.main.view_item_main.view.*

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

class Fragment3 :BaseFgm(){
    private  var currentDevice:BluetoothDevice?=null

    private var deviceList= mutableListOf<BluetoothDevice>()
    private val loadingDialog by lazy {
        var dialog= NetLoadingDlg.newInstace()
        dialog.init({this.netLoadingDlgDismiss},{dialog.show(fragmentManager!!,"111")})

        return@lazy dialog
    }

    private var scanCallBack:BlueToothUtils.ScanBlueCallBack= object :BlueToothUtils.ScanBlueCallBack{
            override fun onStartScanBlue() {
                loadingDialog.showDlg()
            }

            override fun onScanning(device: BluetoothDevice) {
                deviceList.add(device)
            }

            override fun onSCanFinish() {
                loadingDialog.dismiss()
                rvDevice.adapter= adapter
                adapter.notifyDataSetChanged()
            }

        }

    private  val bluetoothUtil by lazy{BlueToothUtils(activity!!,scanCallBack)}

    private val adapter by lazy {
        return@lazy  object:CommonApt<BluetoothDevice>(deviceList,R.layout.view_item_main){
            override fun dispay(holder: CommonViewHolder, item: BluetoothDevice, position: Int) {
                holder.itemView.tvName.text="蓝牙名称:${item.name},地址:${item.toString()}"
                holder.itemView.setOnClickListener{
                }
            }
        }.setClick(object:OnItemClickListener<BluetoothDevice>{
            override fun onItemClickListener(position: Int, item: BluetoothDevice, apt: CommonApt<BluetoothDevice>) {
                bluetoothUtil.pin(item)
                currentDevice=item
            }

        })
    }
    override fun initLayout()= R.layout.fragment_navigation3
    override fun initViews() {
//        var title=arguments!!.getString("title")
//        tvContent.text=title
        bluetoothUtil.registerScanBroadcast()
        bluetoothUtil.registerPinBroadcast()

    }

    override fun initEvent() {
        btnsave.setOnClickListener {

            Thread {
                if (bluetoothUtil.isEnableBlueTooth()){
                bluetoothUtil.scanBluetooth()
            }else{
                bluetoothUtil.openBluetoothSync(999)
            }

            }.start()
        }
        btnclear.setOnClickListener {

           if (currentDevice!=null) bluetoothUtil.canclePin(currentDevice!!)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==999){

            if(resultCode==RESULT_OK){
                //有权限
                toast("蓝牙权限获取成功")

            }else{
                toast("蓝牙权限获取失败了")

            }

        }
    }


}