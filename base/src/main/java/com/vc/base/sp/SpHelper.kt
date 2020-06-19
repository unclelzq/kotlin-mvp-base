package com.vc.base.sp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

@SuppressLint("CommitPrefEdits")
class SpHelper {

    private val LOG_TAG = SpHelper::class.java.simpleName

    private val defaultFileName = "SpFile"
    private var context: Application? = null
    private var fileName: String? = null
    val sp: SharedPreferences by lazy {
        context!!.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    }

    private val editor: SharedPreferences.Editor by lazy {
        sp.edit()
    }

    companion object {
        val INSTANCE: SpHelper by lazy {
            InnerInstance.spHelper
        }
    }

    private object InnerInstance {
        val spHelper = SpHelper()
    }

    fun init(context: Application, fileName: String = defaultFileName) {
        this.context = context
        this.fileName = fileName
    }

    fun <T> set(key: String, value: T) {
        Observable.create<T> {
            it.onNext(value)
        }.subscribeOn(Schedulers.io())
                .map {
                    when (value) {
                        is Boolean -> editor.putBoolean(key, value)
                        is String -> editor.putString(key, value)
                        is Int -> editor.putInt(key, value)
                        is Float -> editor.putFloat(key, value)
                        is Long -> editor.putLong(key, value)
                        else -> throw UnsupportedOperationException("Type not supported: $value")
                    }
                    editor.commit()
                }
                .subscribe({
                    Log.i(LOG_TAG, "存储结果 $it")
                }, {
                    Log.i(LOG_TAG, "存储发生异常 $it")
                })
    }

    inline fun <reified T> get(key: String, defValue: T): T =
            when (defValue) {
                is Boolean -> sp.getBoolean(key, defValue) as T
                is String -> sp.getString(key, defValue) as T
                is Int -> sp.getInt(key, defValue) as T
                is Float -> sp.getFloat(key, defValue) as T
                is Long -> sp.getLong(key, defValue) as T
                else -> throw UnsupportedOperationException("Type not supported: ${T::class.java.simpleName}")
            }

    fun getBoolean(key: String) = get(key, false)

    fun getString(key: String) = get(key, "")

    fun getInt(key: String) = get(key, 0)

    fun getFloat(key: String) = get(key, 0F)

    fun getLong(key: String) = get(key, 0L)

    fun remove(key: String) {
        Observable.create<String> { it.onNext(key) }
                .subscribeOn(Schedulers.io())
                .subscribe {
                    editor.remove(key).commit()
                }

    }

    fun clear() {
        editor.clear()
        editor.commit()
    }
}