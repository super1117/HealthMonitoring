package com.zero.healthmonitoring.delegate

import android.widget.SeekBar
import android.widget.TextView
import com.zero.healthmonitoring.R
import com.zero.library.mvp.view.AppDelegate
import com.zero.library.widget.DrawableView

class SpoDelegate : AppDelegate() {

    lateinit var tvStatus: DrawableView

    lateinit var tvSpo2: TextView

    lateinit var tvBpm: TextView

    lateinit var spoSeek: SeekBar

    lateinit var tvSeek: TextView

    override fun getRootLayoutId(): Int = R.layout.activity_sop

    override fun initWidget() {
        super.initWidget()
        this.tvStatus = this.get(R.id.tv_status)
        this.tvSpo2 = this.get(R.id.tv_spo2)
        this.tvBpm = this.get(R.id.tv_bpm)
        this.spoSeek = this.get(R.id.spo_seek)
        this.tvSeek = this.get(R.id.tv_seek)
        this.spoSeek.setOnTouchListener { _, _ ->  true}
    }
}