package com.app.lzq.testbase.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory


/**
 *
 * @ProjectName:    TestBase
 * @Package:        com.app.lzq.testbase.utils
 * @ClassName:      ImageUtils
 * @Description:     java类作用描述
 * @Author:        刘智强
 * @CreateDate:     2020/6/12 11:14
 * @UpdateUser:     更新者
 * @UpdateDate:     2020/6/12 11:14
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

object ImageUtils {
    fun decodeSampledBitmapFromResource(res: Resources?, resId: Int, reqWidth: Int, reqHeight: Int): Bitmap? {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        //加载图片
        BitmapFactory.decodeResource(res, resId, options)
        //计算缩放比
        options.inSampleSize = calculateInSampleSize(options, reqHeight, reqWidth)
        //重新加载图片
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(res, resId, options)
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqHeight: Int, reqWidth: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            //计算缩放比，是2的指数
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

}