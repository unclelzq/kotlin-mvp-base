package com.app.lzq.testbase.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.UiThread
import com.app.lzq.testbase.navigation.toast
import java.lang.reflect.Method
import java.util.*

/**
 *
 * @ProjectName:    TestBase
 * @Package:        com.app.lzq.testbase.utils
 * @ClassName:      BlueToothUtils
 * @Description:     java类作用描述
 * @Author:        刘智强
 * @CreateDate:     2019/2/26 10:23
 * @UpdateUser:     更新者
 * @UpdateDate:     2019/2/26 10:23
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

class BlueToothUtils(private val activity:Activity,
                     private val scanBlueCallBack: ScanBlueCallBack){


//蓝牙扫描的结果广播接受者
    private  val scanBlueReceiver by lazy {
        return@lazy  object :BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                var action=intent!!.action
               var device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                 when(action){

                     BluetoothAdapter.ACTION_DISCOVERY_STARTED->{
                         Log.e("ATG","开始扫描")
                         scanBlueCallBack.onStartScanBlue()
                     }
                     BluetoothAdapter.ACTION_DISCOVERY_FINISHED->{
                         Log.e("TAG","扫描结束")
                         scanBlueCallBack.onSCanFinish()
                     }
                     BluetoothDevice.ACTION_FOUND->{
                        if (device!=null&&device.name!=null){
                            Log.e("TAG","发现设备${device.name}")
                            scanBlueCallBack.onScanning(device)
                        }

                     }
                 }
            }

        }
    }
    private val blueToothAdapter by lazy {
            return@lazy  BluetoothAdapter.getDefaultAdapter()
        }
    //蓝牙配对的结果广播接受者
    private  val pinBluetoothReceiver by lazy{
        return@lazy object :BroadcastReceiver(){
            var pin="1234"//配对的初始密码
            override fun onReceive(context: Context?, intent: Intent?) {
                var action=intent?.action
                val device = intent?.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                when(action){
                    BluetoothDevice.ACTION_PAIRING_REQUEST->{
                        try {
                            //1.确认配对
                            var setPairingConfirmation = device!!::class.java.getDeclaredMethod("setPairingConfirmation",Boolean::class.java)
                            setPairingConfirmation.invoke(device,true);
                            //2.终止有序广播
                            Log.e("order...", "isOrderedBroadcast:$isOrderedBroadcast,isInitialStickyBroadcast:$isInitialStickyBroadcast");
                            abortBroadcast();//如果没有将广播终止，则会出现一个一闪而过的配对框。
                            //3.调用setPin方法进行配对...
//                          boolean ret = ClsUtils.setPin(device.getClass(), device, pin);
                            var removeBondMethod = device::class.java.getDeclaredMethod("setPin",  *arrayOf<Class<*>>(ByteArray::class.java));
                            var  returnValue =  removeBondMethod.invoke(device, *arrayOf<Any>(pin.toByteArray())) as Boolean;
                        } catch (e:Exception) {
                            e.printStackTrace();
                        }
                    }
                    BluetoothDevice.ACTION_BOND_STATE_CHANGED->{
                        when(device!!.bondState){
                            BluetoothDevice.BOND_NONE->{
                                Log.e("TAG","配对取消")
                            }
                            BluetoothDevice.BOND_BONDED->{
                                Log.e("TAG","配对中")
                            }
                            BluetoothDevice.BOND_BONDED->{
                                Log.e("TAG","配对成功")

                            }

                        }


                    }
                }

            }

        }
    }

    private val connectBlutootheTask by lazy {

        return@lazy  @SuppressLint("StaticFieldLeak")
        object:AsyncTask<BluetoothDevice,Int,BluetoothSocket>(){

            override fun onPreExecute() {
                super.onPreExecute()
                Log.e("TAG","开始连接...")
            }

            override fun onPostExecute(result: BluetoothSocket?) {
                super.onPostExecute(result)
                Log.e("TAG","连接成功...")
                if (result!=null&&result.isConnected){
                    //连接成功
                    Log.e("TAG","连接成功")
                }else{
                    //连接失败
                    Log.e("TAG","连接失败")
                }
            }
            override fun doInBackground(vararg params: BluetoothDevice?): BluetoothSocket {
                var uuid=UUID.randomUUID().toString()
                Log.e("TAG","该设备的uuid:$uuid")
                var socket:BluetoothSocket?=null
                try {
                    socket=params[0]?.createRfcommSocketToServiceRecord(UUID.randomUUID())
                    if (socket!=null&&!socket.isConnected){
                        socket.connect()
                    }
                }catch (e:Exception){
                    socket = params[0]!!::class.java.getMethod("createRfcommSocket", *arrayOf<Class<*>>(Int::class.java)).invoke(params[0],1) as BluetoothSocket;
                    if (socket!=null) socket.connect()
                }
                    return  socket!!

            }
        }
    }

    private val readMessageTask by lazy {
        return@lazy  object:AsyncTask<String,Int,String>(){
            override fun doInBackground(vararg params: String?): String {
                return ""
            }

        }
    }

    private  val wrirteMessageTask by lazy {
        return@lazy  object:AsyncTask<String,Int,String>(){
            override fun doInBackground(vararg params: String?): String {
                return ""

            }

        }

    }

    private fun isSupportBlueTooth()=blueToothAdapter!=null //先判断设备是否支持蓝牙功能
    fun isEnableBlueTooth()=isSupportBlueTooth()&&blueToothAdapter.isEnabled //蓝牙是否打开
    fun  openBluetoothAsyn(){//自动打开蓝牙
        if(isSupportBlueTooth()){
            blueToothAdapter.enable()

        }
    }
    fun openBluetoothSync(requestCode:Int){//跳转到设置界面打开蓝牙
        var intent=Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        activity.startActivityForResult(intent,requestCode)
    }
//开始扫描蓝牙
    fun scanBluetooth():Boolean{
        if (!isEnableBlueTooth()){
            return false
        }
        if (blueToothAdapter.isDiscovering){
            blueToothAdapter.cancelDiscovery()
        }
        return  blueToothAdapter.startDiscovery()

    }
//取消掃描
    fun cancleScanBluetooth():Boolean{
        if (isSupportBlueTooth()){
            return blueToothAdapter.cancelDiscovery()
        }
        return  true
    }
// 註冊廣播
    fun registerScanBroadcast(){
        var filter1=IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        var filter2=IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        var filter3=IntentFilter(BluetoothDevice.ACTION_FOUND)
        activity.registerReceiver(scanBlueReceiver,filter1)
        activity.registerReceiver(scanBlueReceiver,filter2)
        activity.registerReceiver(scanBlueReceiver,filter3)
    }
    //开始配对蓝牙
    fun pin(device:BluetoothDevice){
        if (device==null||!isEnableBlueTooth()) return
        if (blueToothAdapter.isDiscovering) blueToothAdapter.cancelDiscovery()//如果正在扫描，就先取消扫描
        //如果设备没有配对，就先进行配对
        if (device.bondState==BluetoothDevice.BOND_NONE){
            Log.e("TAG",device.name)
            try {
                var createBondMethod = device::class.java.getMethod("createBond")
                var  returnValue:Boolean =  createBondMethod.invoke(device) as Boolean
                Log.e("TAG","$returnValue")
                if (returnValue) connectBlutootheTask.execute(device)
            } catch (e:Exception) {
                e.printStackTrace()
                Log.e("TAG", "attemp to bond fail!")
            }
        }else{
            Log.e("ATG","已经连接了该设备")
        }


    }
    //取消配对
    fun canclePin(device:BluetoothDevice){
        if (device==null||!isEnableBlueTooth()) return
        //如果已经配对了 就取消配对
        if (device.bondState!=BluetoothDevice.BOND_NONE){
            try {
                var  removeBondMethod = device::class.java.getMethod("removeBond");
                var  returnValue =  removeBondMethod.invoke(device) as Boolean;

            } catch (e:Exception) {
                e.printStackTrace();
                Log.e("TAG", "attemp to cancel bond fail!");
            }

        }

    }
        //通过广播的方式来告知匹配的结果
    fun  registerPinBroadcast(){
            var filter4=IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST)
            var filter5=IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
            activity.registerReceiver(pinBluetoothReceiver,filter4)
            activity.registerReceiver(pinBluetoothReceiver,filter5)
    }
//断开连接
    fun cancelConnect(){

    }



    interface  ScanBlueCallBack{
        fun onStartScanBlue()
        fun onScanning(device:BluetoothDevice)
        fun onSCanFinish()
    }

}