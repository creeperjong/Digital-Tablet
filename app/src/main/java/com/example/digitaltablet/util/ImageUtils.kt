package com.example.digitaltablet.util

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

fun Uri.toImageBitmap(context: Context): ImageBitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(this)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()
        bitmap?.asImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun Uri.toBitMap(context: Context): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(this)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()
        bitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun ContentResolver.createImageFile(fileName: String): Uri? {
    val selection = "${MediaStore.Images.Media.DISPLAY_NAME} = ?"
    val selectionArgs = arrayOf(fileName)
    val cursor = query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        arrayOf(MediaStore.Images.Media._ID),
        selection,
        selectionArgs,
        null
    )

    cursor?.use {
        if (it.moveToFirst()) {
            val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
            return ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
        }
    }

    val values = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    }
    return insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
}