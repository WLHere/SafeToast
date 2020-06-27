package com.example.safetoast

import android.app.Application

class App : Application() {

    override fun getSystemService(name: String): Any? {
        return SafeToastService.getSystemService(name, super.getSystemService(name))
    }
}