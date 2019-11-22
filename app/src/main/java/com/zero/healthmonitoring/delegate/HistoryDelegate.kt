package com.zero.healthmonitoring.delegate

import android.graphics.Color
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
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
        val xAxis = this.ahChart.xAxis
        this.ahChart.setDataMax(count)

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)
        xAxis.axisMinimum = 0f

        val leftAxis = this.ahChart.axisLeft
        leftAxis.setLabelCount(5, false)
        leftAxis.axisMaximum = 100f
        leftAxis.axisMinimum = 0f
        val bpmLimitLine = LimitLine(60f, "标准脉率")
        bpmLimitLine.textColor = Color.RED
        bpmLimitLine.enableDashedLine(6f, 3f, 0f)
        leftAxis.addLimitLine(bpmLimitLine)
        val spoLimitLine = LimitLine(88f, "正常血氧饱和度")
        spoLimitLine.textColor = Color.RED
        spoLimitLine.enableDashedLine(4f, 2f, 0f)
        leftAxis.addLimitLine(spoLimitLine)

        val rightAxis = this.ahChart.axisRight
        rightAxis.setLabelCount(5, false)
        rightAxis.setDrawGridLines(false)
        rightAxis.axisMaximum = 100f
        rightAxis.axisMinimum = 0f

        this.ahChart.data = data
        this.ahChart.animateX(750)
    }
}