package com.vc.base.net

import android.content.Context
import android.net.Uri

/**
 * Created by 35784 on 2018/4/19.
 */
class SourceFactory private constructor() {
    companion object {
        val RETROFIT_SOURCE_MAP: MutableMap<String, BaseRetrofitSource> by lazy { mutableMapOf<String, BaseRetrofitSource>() }

        inline fun <reified T : BaseRetrofitSource> create(): T {
            if (RETROFIT_SOURCE_MAP[T::class.java.simpleName] == null) {
                synchronized(SourceFactory::class.java) {
                    if (RETROFIT_SOURCE_MAP[T::class.java.simpleName] == null) {
                        try {
                            RETROFIT_SOURCE_MAP[T::class.java.simpleName] = Class.forName(T::class.java.name).newInstance() as T
                        } catch (e: InstantiationException) {
                            e.printStackTrace()
                        } catch (e: IllegalAccessException) {
                            e.printStackTrace()
                        } catch (e: ClassNotFoundException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
            return RETROFIT_SOURCE_MAP[T::class.java.simpleName] as T
        }

        fun wrapPathToUri(path: String?): Uri = Uri.parse(path ?: "")

        fun resToUri(resId: Int, context: Context): Uri = Uri.parse("res://${context.packageName}/$resId")

        fun fileToUri(path: String): Uri = Uri.parse("file://$path")
    }
}