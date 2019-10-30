package com.zero.healthmonitoring

import android.app.Application
import com.tencent.bugly.crashreport.CrashReport
import com.zero.library.Library

class SuperApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        Library.init(this)
        CrashReport.initCrashReport(applicationContext, "9626cab689", true)
    }

}