package com.zero.healthmonitoring.delegate

import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.zero.healthmonitoring.R
import com.zero.healthmonitoring.adapter.HistoryAdapter
import com.zero.library.mvp.view.AppDelegate

class HistoryDelegate : AppDelegate(){

    lateinit var ahChart: LineChart

    lateinit var ahRv: RecyclerView

    lateinit var adapter: HistoryAdapter

    override fun getRootLayoutId(): Int = R.layout.activity_history

    override fun initWidget() {
        super.initWidget()
        this.ahChart = this.get(R.id.ah_chart)

        this.ahChart.description.isEnabled = false
        this.ahChart.setDrawGridBackground(false)

        val xAxis = this.ahChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
//        xAxis.typeface = mTf
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)

        val leftAxis = this.ahChart.axisLeft
//        leftAxis.typeface = mTf
        leftAxis.setLabelCount(5, false)
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

        val rightAxis = this.ahChart.axisRight
//        rightAxis.typeface = mTf
        rightAxis.setLabelCount(5, false)
        rightAxis.setDrawGridLines(false)
        rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

        this.ahRv = this.get(R.id.ah_rv)
        val manager = LinearLayoutManager(this.getActivity())
        manager.orientation = RecyclerView.VERTICAL
        this.ahRv.layoutManager = manager
        this.adapter = HistoryAdapter(this.ahRv)
        this.ahRv.adapter = this.adapter
    }

    fun fillDataToChart(data: LineData){
        this.ahChart.data = data
        this.ahChart.animateX(750)
    }
}