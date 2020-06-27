package com.example.safetoast

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.Display
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

class App : Application() {
    @Volatile
    private var mWindowManager: WindowManager? = null

    override fun getSystemService(name: String): Any? {
        if (Build.VERSION.SDK_INT == 25) {// 兼容android 7.1.1 toast崩溃问题
            if (name == Context.WINDOW_SERVICE) {
                if (mWindowManager == null) {
                    mWindowManager = WindowManagerWrapper(
                        super.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                    )
                }
                return mWindowManager
            }
        }
        return super.getSystemService(name)
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