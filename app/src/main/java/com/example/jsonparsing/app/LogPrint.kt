package com.example.jsonparsing.app

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement

object LogPrint {
    private const val TAG = "JSONPARSING"

    private val currentThread = Thread.currentThread()

    fun d(message: String = "") {
        var index = 0
        for ((_index, trace) in currentThread.stackTrace.withIndex()) {
            trace.fileName?.let {
                if (it.startsWith("AppLog")) {
                    index = (_index + 1)
                }
            }
        }

        val traceElement = currentThread.stackTrace[index]

        val link = "${traceElement.fileName}:${traceElement.lineNumber}"
        val method = traceElement.methodName

        Log.d(TAG, "[($link).$method()] $message")
    }

    fun e(message: String) {
        Log.e(TAG, message)
    }

    fun i(message: String) {
        Log.i(TAG, message)
    }

    fun json(jsonElement: JsonElement) {
        Log.i(TAG, "${GsonBuilder().setPrettyPrinting().create().toJson(jsonElement)}")
    }
}