package com.vc.base.util

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import com.yalantis.ucrop.util.FileUtils.getDataColumn

private const val EXTERNALSTORAGE_DOCUMENTS = "com.android.externalstorage.documents"
private const val DOWNLOADS_DOCUMENTS = "com.android.providers.downloads.documents"
private const val MEDIA_DOCUMENTS = "com.android.providers.media.documents"
private const val GOOGLE_PHOTOS_CONTENT = "com.google.android.apps.photos.content"

fun Uri.getImgPath(context: Context): String? =
        when {
            DocumentsContract.isDocumentUri(context, this) -> when (this.authority) {
                EXTERNALSTORAGE_DOCUMENTS -> {
                    val docId = DocumentsContract.getDocumentId(this)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]
                    if ("primary".equals(type, ignoreCase = true))
                        Environment.getExternalStorageDirectory().absolutePath + "/" + split[1]
                    null
                }
                DOWNLOADS_DOCUMENTS -> {
                    val id = DocumentsContract.getDocumentId(this)
                    val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                    getDataColumn(context, contentUri, null, null)
                }
                MEDIA_DOCUMENTS -> {
                    val docId = DocumentsContract.getDocumentId(this)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    when (type) {
                        "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    val selection = MediaStore.Images.Media._ID + "=?"
                    val selectionArgs = arrayOf(split[1])
                    getDataColumn(context, contentUri, selection, selectionArgs)
                }
                else ->
                    null
            }
            "content".equals(this.scheme, ignoreCase = true) -> {
                if (GOOGLE_PHOTOS_CONTENT == this.authority)
                    this.lastPathSegment
                this.getDataColumn(context, null, null)
            }
            "file".equals(this.scheme, ignoreCase = true) ->
                this.path
            else ->
                null
        }


fun Uri.getDataColumn(context: Context, selection: String?, selectionArgs: Array<String>?): String? {
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    var cursor: Cursor? = null
    try {
        cursor = context.contentResolver.query(this, projection, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            val index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            return cursor.getString(index)
        }
    } finally {
        cursor?.close()
    }
    return null
}