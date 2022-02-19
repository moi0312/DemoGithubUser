package com.edlo.mydemoapp.util

import android.util.Log
import com.edlo.mydemoapp.BuildConfig

class Log {
    companion object {
        const val DEFAULT_TAG = BuildConfig.APPLICATION_ID
        val PRINT_LOG = BuildConfig.PRINT_LOG

        fun v(tag: String=DEFAULT_TAG, msg: String) {
            if(PRINT_LOG) Log.v(tag, msg)
        }

        fun i(tag: String=DEFAULT_TAG, msg: String) {
            if(PRINT_LOG) Log.i(tag, msg)
        }

        fun d(tag: String=DEFAULT_TAG, msg: String) {
            if(PRINT_LOG) Log.d(tag, msg)
        }

        fun w(tag: String=DEFAULT_TAG, msg: String) {
            if(PRINT_LOG) Log.w(tag, msg)
        }

        fun e(tag: String=DEFAULT_TAG, msg: String) {
            Log.e(tag, msg)
        }
    }
}