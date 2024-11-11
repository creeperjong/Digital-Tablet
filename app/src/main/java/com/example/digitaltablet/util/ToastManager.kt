package com.example.digitaltablet.util

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import java.util.LinkedList
import java.util.Queue

object ToastManager {
    private var currentToast: Toast? = null
    private val toastQueue: Queue<String> = LinkedList()

    private fun showNextToast(context: Context) {
        if (toastQueue.isNotEmpty()) {
            val message = toastQueue.poll()
            currentToast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            currentToast?.show()

            Handler(Looper.getMainLooper()).postDelayed({
                currentToast = null
                showNextToast(context)
            }, 2000)
        }
    }

    fun showToast(context: Context, message: String) {
        toastQueue.add(message)
        if (currentToast == null) {
            showNextToast(context)
        }
    }
}