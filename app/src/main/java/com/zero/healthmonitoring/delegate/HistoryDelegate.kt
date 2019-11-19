package com.zero.healthmonitoring.delegate

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
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

        this.ahRv = this.get(R.id.ah_rv)
        val manager = LinearLayoutManager(this.getActivity())
        manager.orientation = RecyclerView.VERTICAL
        this.ahRv.layoutManager = manager
        this.adapter = HistoryAdapter(this.ahRv)
        this.ahRv.adapter = this.adapter
    }

    fun fillDataToChart(data: LineData, count: Int){
        Log.e("aiya", "count = $count")
        val xAxis = this.ahChart.xAxis
        this.ahChart.setDataMax(count)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
//        xAxis.typeface = mTf
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)
//        val c = data.dataSets[0].entryCount
//        xAxis.setLabelCount(count, false)
//        xAxis.labelRotationAngle = if(count > 12) 45f else 0f
        xAxis.axisMinimum = 0f

        val leftAxis = this.ahChart.axisLeft
//        leftAxis.typeface = mTf
        leftAxis.setLabelCount(5, false)
        leftAxis.axisMaximum = 100f
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

        val rightAxis = this.ahChart.axisRight
//        rightAxis.typeface = mTf
        rightAxis.setLabelCount(5, false)
        rightAxis.setDrawGridLines(false)
        rightAxis.axisMaximum = 100f
        rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

        this.ahChart.data = data
        this.ahChart.animateX(750)
    }
}