package com.zero.healthmonitoring.presenter

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.zero.healthmonitoring.api.RxHelper
import com.zero.healthmonitoring.api.SystemApi
import com.zero.healthmonitoring.data.UserTestBean
import com.zero.healthmonitoring.delegate.HistoryDelegate
import com.zero.library.network.RxSubscribe
import kotlinx.android.synthetic.main.activity_history.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class HistoryPresenter : BasePresenter<HistoryDelegate>(){

    private lateinit var list: ArrayList<UserTestBean.BloinfoBean>

    private val yearList = LinkedList<String>()

    private val monthList = LinkedList<String>()

    private val dayList = LinkedList<String>()

    private lateinit var yearAdapter : ArrayAdapter<String>

    private lateinit var monthAdapter: ArrayAdapter<String>

    private lateinit var dayAdapter: ArrayAdapter<String>

    override fun doMain() {
        viewDelegate.fillDataToChart(generateDataLine())
        this.list = this.viewDelegate.adapter.data

        this.ah_year.prompt = "年"
        this.ah_month.prompt = "月"
        this.ah_day.prompt = "日"

        this.yearAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, this.yearList)
        this.yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        this.ah_year.adapter = this.yearAdapter
        this.monthList.add(0, "全部")
        this.monthAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, this.monthList)
        this.monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        this.ah_month.adapter = this.monthAdapter
        this.dayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, this.dayList)
        this.dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        this.ah_day.adapter = this.dayAdapter
        
        this.getData(1)
    }

    override fun bindEventListener() {
        super.bindEventListener()
        this.viewDelegate.adapter.setOnItemClickListener { _, _, _ ->  }
        this.viewDelegate.adapter.setOnLoadListener {
            Log.e("aiya", "load more ...")
        }

        this.ah_year.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        this.ah_month.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        this.ah_day.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
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

    private fun getData(page: Int){
        val param = HashMap<String, String?>()
        param["uid"] = this.user?.uid
        param["pagenum"] = page.toString()
        SystemApi.provideService()
            .bloList(param)
            .compose(RxHelper.applySchedulers())
            .subscribe(object : RxSubscribe<UserTestBean>(this.viewDelegate, true){
                override fun _onNext(t: UserTestBean?) {
                    t?.apply {
                        if(page == 1){
                            list.clear()
                        }
                        bloinfo?.apply {
                            list.addAll(this)
                            viewDelegate.adapter.notifyDataSetChanged()
                            formatTime(this)
                        }
                    }
                }

                override fun _onError(message: String?) {

                }
            })
    }

    private fun formatTime(t: List<UserTestBean.BloinfoBean>){
        t.forEach {
            it.addtime?.apply {
                val arr = this.split(" ")
                if(arr.size > 1){
                    val date = arr[0].split("-")
                    if(date.size > 2){
                        yearList.add(date[0])
                        monthList.add(date[1])
                        dayList.add(date[2])
                    }
                }
            }

        }
        this.clearRepeat(this.yearList)
        this.clearRepeat(this.monthList)
        this.clearRepeat(this.dayList)

        this.yearList.add("2019")
        this.yearList.add("2018")
        this.monthList.add("01")
        this.yearAdapter.notifyDataSetChanged()
        this.monthAdapter.notifyDataSetChanged()
        this.dayAdapter.notifyDataSetChanged()
    }

    private fun clearRepeat(list: LinkedList<String>){
        val set = HashSet<String>()
        set.addAll(list)
        list.clear()
        list.addAll(set)
        list.sortedDescending()
    }
}