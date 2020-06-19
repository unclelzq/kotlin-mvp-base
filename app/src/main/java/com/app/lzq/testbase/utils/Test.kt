package com.app.lzq.testbase.utils

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.util.Log
import com.google.gson.Gson

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * @ProjectName: TestBase
 * @Package: com.app.lzq.testbase.utils
 * @ClassName: Test
 * @Description: java类作用描述
 * @Author: 刘智强
 * @CreateDate: 2019/2/26 13:42
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/2/26 13:42
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
class Test {
    private val device: BluetoothDevice? = null
    private val pin = ""

    fun test() {
        var removeBondMethod: Method? = null
        try {
            removeBondMethod = device!!.javaClass.getDeclaredMethod("setPin", *arrayOf<Class<*>>(ByteArray::class.java))
            val returnValue = removeBondMethod!!.invoke(device, *arrayOf<Any>(pin.toByteArray())) as Boolean
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }


    }

    inline  fun  <reified  T> Activity.optObj( key :String,value:String=""):T?{

       return  try {
         var json=  this.intent.extras.getString(key)
          json?.let { Gson().fromJson<T>(json,T::class.java) }
        }catch (e:Exception){
            null
        }

    }
}
