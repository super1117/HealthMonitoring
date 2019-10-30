package com.zero.healthmonitoring.delegate

import android.widget.TextView
import com.zero.healthmonitoring.R
import com.zero.library.mvp.view.AppDelegate

class SpoDelegate : AppDelegate() {

    lateinit var para: TextView

    lateinit var wave: TextView

    lateinit var tvLog: TextView

    override fun getRootLayoutId(): Int = R.layout.activity_sop

    override fun initWidget() {
        super.initWidget()
        this.para = this.get(R.id.para)
        this.wave = this.get(R.id.wave)
        this.tvLog = this.get(R.id.tv_log)
    }
}