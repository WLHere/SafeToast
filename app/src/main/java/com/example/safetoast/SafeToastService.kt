package com.example.safetoast

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.Display
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

/**
 * Created by baiwenlong on 2020/6/28
 */
object SafeToastService {
    private var mWindowManager: WindowManager? = null

    fun getSystemService(name: String, baseService: Any?): Any? {
        if (Build.VERSION.SDK_INT <= 25) {// 兼容android 7.1.1 toast崩溃问题
            if (name == Context.WINDOW_SERVICE && callFromToast()) {
                if (mWindowManager == null) {
                    mWindowManager = WindowManagerWrapper(baseService as WindowManager)
                }
                return mWindowManager
            }
        }
        return baseService
    }

    private fun callFromToast(): Boolean {
        var fromToast = false
        try {
            // android.widget.Toast$TN.handleShow
            val traces = Thread.currentThread().stackTrace
            if (traces != null) {
                for (trace in traces) {
                    if ("android.widget.Toast\$TN" == trace.className && "handleShow" == trace.methodName) {
                        fromToast = true
                        break
                    }
                }
            }
        } catch (ignored: Throwable) {
        }
        return fromToast
    }

    class WindowManagerWrapper(private val baseManager: WindowManager) : WindowManager {
        override fun getDefaultDisplay(): Display {
            return baseManager.defaultDisplay
        }

        override fun addView(view: View?, params: ViewGroup.LayoutParams?) {
            try {
                baseManager.addView(view, params)
            } catch (e: WindowManager.BadTokenException) {
                Log.w("bwl", "add window failed", e)
            }
        }

        override fun updateViewLayout(view: View?, params: ViewGroup.LayoutParams?) {
            baseManager.updateViewLayout(view, params)
        }

        override fun removeView(view: View?) {
            baseManager.removeView(view)
        }

        override fun removeViewImmediate(view: View?) {
            baseManager.removeViewImmediate(view)
        }
    }
}
