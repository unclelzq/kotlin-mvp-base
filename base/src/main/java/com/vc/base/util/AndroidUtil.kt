package com.vc.base.util

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Environment
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStream
import java.math.BigDecimal
import java.math.BigInteger
import java.security.MessageDigest
import android.provider.MediaStore




//淘宝
const val TAOBAO_PACKAGE_NAME = "com.taobao.taobao"
const val TAOBAO_GO_TO_SHOP_URL_SCHEME = "taobao://shop.m.taobao.com/shop/shop_index.htm?shop_id="

/**
 * 添加到剪切板
 */
fun String.copy(context: Context) {
    //获取剪贴板管理器：
    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    // 创建普通字符型ClipData
    val mClipData = ClipData.newPlainText("Label", this)
    // 将ClipData内容放到系统剪贴板里。
    cm.primaryClip = mClipData
}

/**
 * 检测是否安装了该包名的apk
 */
@SuppressLint("PackageManagerGetSignatures")
fun String.isAppInstalled(context: Context): Boolean =
        try {
            context.packageManager.getPackageInfo(this, PackageManager.GET_SIGNATURES)
            true
        } catch (var5: PackageManager.NameNotFoundException) {
            false
        }

/**
 * 字符串做md5
 */
fun String.toMD5(): String {
    val md = MessageDigest.getInstance("MD5")
    val bigInt = BigInteger(1, md.digest(this.toByteArray()))
    return String.format("%032x", bigInt)
}

/**
 * 隐藏键盘
 */
fun View.hideKeyboard() {
    (this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(this.windowToken, 0)
}

/**
 * 保存bitmap 到文件
 */
fun Bitmap.saveBitmap2file(filePath: String): String? {

    val format = Bitmap.CompressFormat.JPEG
    val quality = 100
    var stream: OutputStream? = null
    try {
        // 判断SDcard状态
        if (Environment.MEDIA_MOUNTED != Environment.getExternalStorageState()) {
            // 错误提示
            return null
        }

        // 检查SDcard空间
        val SDCardRoot = Environment.getExternalStorageDirectory()
        if (SDCardRoot.freeSpace < 10000) {
            // 弹出对话框提示用户空间不够
            return null
        }

        // 在SDcard创建文件夹及文件
        val bitmapFile = File(SDCardRoot.path + filePath)
        bitmapFile.parentFile.mkdirs()// 创建文件夹
        stream = FileOutputStream(SDCardRoot.path + filePath)// "/sdcard/"
        val isSaveOk = this.compress(format, quality, stream)
        stream.flush()
        stream.close()
        return if (isSaveOk)
            SDCardRoot.path + filePath
        else
            null
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        return null
    }
}

/**
 * 移动光标到指定字符串尾巴
 */
fun EditText.cursorInsert(data: String) {
    this.text.insert(this.selectionStart, data)
}

/**
 * 替换EditText 内容
 */
fun EditText.changeText(start: Int, end: Int, text: String) {
    this.text.replace(start, end, text)
}

/**
 * 获取某个字符得 所有出现过的位置
 */
fun String.getCharIndexList(findText: String): MutableList<Int> {
    var index = 0
    val indexList = mutableListOf<Int>()

    while (this.indexOf(findText, index) != -1) {
        indexList.add(this.indexOf(findText, index))
        index = this.indexOf(findText, index) + 1
    }
    return indexList
}

/**
 * 大小格式话
 */
fun Double.getForMatSize(): String {
    val kiloByte = this / 1024
    if (kiloByte < 1) {
        return "0KB"
    }

    val megaByte = kiloByte / 1024
    if (megaByte < 1) {
        val result = BigDecimal(java.lang.Double.toString(kiloByte))
        return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB"
    }

    val gigaByte = megaByte / 1024
    if (gigaByte < 1) {
        val result = BigDecimal(java.lang.Double.toString(megaByte))
        return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
    }

    val teraBytes = gigaByte / 1024
    if (teraBytes < 1) {
        val result = BigDecimal(java.lang.Double.toString(gigaByte))
        return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
    }

    val result = BigDecimal(java.lang.Double.toString(teraBytes))
    return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
}

/**
 * 清空文件夹
 */
fun File.deleteDirAll() {
    if (this.isDirectory) {
        val children = this.list()
        for (i in children.indices) {
            File(this, children[i]).deleteDirAll()
        }
    }
    this.delete()
}

/**
 * 获取文件夹大小
 */
fun File.getDirSize(): Double {
    var size = 0.00
    if (this.isDirectory) {
        val fileList = this.listFiles()
        for (i in fileList.indices) {
            size += if (fileList[i].isDirectory)
                fileList[i].getDirSize()
            else
                fileList[i].length().toDouble()
        }
    } else
        size += this.length().toDouble()
    return size
}