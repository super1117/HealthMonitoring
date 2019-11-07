package com.zero.healthmonitoring.presenter

import android.graphics.Color
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.zero.healthmonitoring.delegate.HistoryDelegate
import java.util.ArrayList

class HistoryPresenter : BasePresenter<HistoryDelegate>(){

    override fun doMain() {
        viewDelegate.fillDataToChart(generateDataLine())
        val array = ArrayList<String>()
        for(i in 0..30){
            array.add(i.toString())
        }
        viewDelegate.adapter.data = array
    }

    override fun bindEventListener() {
        super.bindEventListener()
        this.viewDelegate.adapter.setOnItemClickListener { _, _, _ ->  }
    }

    private fun generateDataLine(): LineData {

        val values1 = ArrayList<Entry>()

        for (i in 0..30) {
            values1.add(Entry(i.toFloat(), ((Math.random() * 65).toInt() + 40).toFloat()))
        }

        val d1 = LineDataSet(values1, "Spo2")
        d1.lineWidth = 2.5f
        d1.circleRadius = 4.5f
        d1.highLightColor = Color.rgb(244, 117, 117)
        d1.setDrawValues(false)

        val values2 = ArrayList<Entry>()

        for (i in 0..30) {
            values2.add(Entry(i.toFloat(), values1[i].y - 30))
        }

        val d2 = LineDataSet(values2, "BPM")
        d2.lineWidth = 2.5f
        d2.circleRadius = 4.5f
        d2.highLightColor = Color.rgb(244, 117, 117)
        d2.color = ColorTemplate.VORDIPLOM_COLORS[0]
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0])
        d2.setDrawValues(false)

        val sets = ArrayList<ILineDataSet>()
        sets.add(d1)
        sets.add(d2)

        return LineData(sets)
    }

}