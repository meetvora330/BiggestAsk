package com.biggestAsk.util

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import java.net.URISyntaxException

/**
 * file path utils
 */
object PathUtil {

    @SuppressLint("NewApi", "Recycle")
    @Throws(URISyntaxException::class)
    fun getPath(context: Context, uri: Uri): String? {
        var mURI = uri
        val needToCheckUri = Build.VERSION.SDK_INT >= 19
        var selection: String? = null
        var selectionArgs: Array<String>? = null

        if (needToCheckUri && DocumentsContract.isDocumentUri(context.applicationContext, mURI)) {
            when {
                isExternalStorageDocument(mURI) -> {
                    val docId = DocumentsContract.getDocumentId(mURI)
                    val split =
                        docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
                isDownloadsDocument(mURI) -> {
                    val id = DocumentsContract.getDocumentId(mURI)
                    if (!TextUtils.isEmpty(id)) {
                        if (id.startsWith("raw:")) {
                            return id.replaceFirst("raw:", "");
                        }
                        mURI = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            java.lang.Long.valueOf(id)
                        )
                    }
                }
                isMediaDocument(mURI) -> {
                    val docId = DocumentsContract.getDocumentId(mURI)
                    val split =
                        docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    when (type) {
                        "image" -> mURI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        "video" -> mURI = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        "audio" -> mURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }

                    selection = "_id=?"
                    selectionArgs = arrayOf(split[1])
                }
            }
        }

        if ("content".equals(mURI.scheme!!, ignoreCase = true)) {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            var cursor: Cursor? = null
            try {
                cursor =
                    context.contentResolver.query(mURI, projection, selection, selectionArgs, null)
                val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (cursor.moveToFirst()) {
                    return cursor.getString(columnIndex)
                }
            } catch (e: Exception) {
            }

        } else if ("file".equals(mURI.scheme!!, ignoreCase = true)) {
            return mURI.path
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

}