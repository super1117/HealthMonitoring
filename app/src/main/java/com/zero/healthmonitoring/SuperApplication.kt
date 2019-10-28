package com.zero.healthmonitoring

import android.app.Application
import com.zero.library.Library

class SuperApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        Library.init(this)
    }

}