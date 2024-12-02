package com.example.digitaltablet.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream

fun Uri.toFile(context: Context): File? {
    val contentResolver: ContentResolver = context.contentResolver
    val filename = getFileName(contentResolver, this) ?: return null
    val tempFile = File(context.cacheDir, filename)
    contentResolver.openInputStream(this)?.use { `is` ->
        FileOutputStream(tempFile).use { os ->
            `is`.copyTo(os)
        }
    }
    return tempFile
}

fun getFileName(contentResolver: ContentResolver, uri: Uri): String? {
    var name: String? = null
    val cursor = contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (index != -1) {
                name = it.getString(index)
            }
        }
    }
    return name
}