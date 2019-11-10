package com.zero.healthmonitoring.presenter

import android.graphics.Color
import android.text.TextUtils
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
import kotlinx.android.synthetic.main.activity_demo.*
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

    private var isTouchSpinner = false

    override fun doMain() {
        this.list = this.viewDelegate.adapter.data

        this.ah_year.prompt = "年"
        this.ah_month.prompt = "月"
        this.ah_day.prompt = "日"

        this.yearAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, this.yearList)
        this.yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        this.ah_year.adapter = this.yearAdapter
        this.monthAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, this.monthList)
        this.monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        this.ah_month.adapter = this.monthAdapter
        this.dayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, this.dayList)
        this.dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        this.ah_day.adapter = this.dayAdapter

        this.getYears()
    }

    override fun bindEventListener() {
        super.bindEventListener()
        this.viewDelegate.adapter.setOnItemClickListener { _, _, _ ->  }
        this.viewDelegate.adapter.setOnLoadListener {
            Log.e("aiya", "load more ...")
        }
        this.ah_month.setOnTouchListener { _, motionEvent ->
            isTouchSpinner = true
            super.onTouchEvent(motionEvent)
        }
        this.ah_day.setOnTouchListener { _, motionEvent ->
            isTouchSpinner = true
            super.onTouchEvent(motionEvent)
        }
        this.ah_year.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                getListByYear(yearList[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        this.ah_month.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position == 0){
                    if(isTouchSpinner){
                        isTouchSpinner = false
                        getListByYear(yearList[ah_year.selectedItemPosition])
                    }
                    return
                }
                getListByMonty(monthList[position], yearList[ah_year.selectedItemPosition])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        this.ah_day.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position == 0){
                    if(isTouchSpinner){
                        isTouchSpinner = false
                        getListByMonty(monthList[ah_month.selectedItemPosition], yearList[ah_year.selectedItemPosition])
                    }
                    return
                }
                getListByDay(dayList[position], monthList[ah_month.selectedItemPosition], yearList[ah_year.selectedItemPosition])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    /**
     * 填充图标数据
     * @param type 1年 2月 3日
     */
    private fun generateDataLine(type: Int): LineData {

        val values1 = ArrayList<Entry>()
        val values2 = ArrayList<Entry>()

        list.forEach {
            when(type){
                1 -> {
                    values1.add(Entry(it.year?.toFloat()?:1970F, it.spo?.toFloat()?:0F))
                    values2.add(Entry(it.year?.toFloat()?:1970F, it.bpm?.toFloat()?:0F))
                }
                2 -> {
                    values1.add(Entry(it.month?.toFloat()?:1F, it.spo?.toFloat()?:0F))
                    values2.add(Entry(it.month?.toFloat()?:1970F, it.bpm?.toFloat()?:0F))
                }
                3 -> {
                    values1.add(Entry(it.day?.toFloat()?:1F, it.spo?.toFloat()?:0F))
                    values2.add(Entry(it.day?.toFloat()?:1970F, it.bpm?.toFloat()?:0F))
                }
            }
        }

        val d1 = LineDataSet(values1, "Spo2")
        d1.lineWidth = 2.5f
        d1.circleRadius = 4.5f
        d1.highLightColor = Color.rgb(244, 117, 117)
        d1.setDrawValues(false)

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

    private fun clearData(){
        monthList.clear()
        dayList.clear()
        list.clear()
        monthAdapter.notifyDataSetChanged()
        dayAdapter.notifyDataSetChanged()
        viewDelegate.adapter.notifyDataSetChanged()
    }

    private fun getListByYear(year: String){
        clearData()
        val param = HashMap<String, String?>()
        param["uid"] = this.user?.uid
        param["year"] = year
        SystemApi.provideService()
            .getBloListByYear(param)
            .compose(RxHelper.applySchedulers())
            .subscribe(object : RxSubscribe<UserTestBean>(this.viewDelegate, false){
                override fun _onNext(t: UserTestBean?) {
                    doResult(1, t)
                }

                override fun _onError(message: String?) {

                }
            })
    }

    private fun getListByMonty(month: String, year: String){
        val params = HashMap<String, String>()
        params["uid"] = this.user?.uid!!
        params["year"] = year
        params["month"] = month
        SystemApi.provideService()
            .getBloListByMonth(params)
            .compose(RxHelper.applySchedulers())
            .subscribe(object : RxSubscribe<UserTestBean>(this.viewDelegate, false){

                override fun _onNext(t: UserTestBean?) {
                    doResult(2, t)
                }

                override fun _onError(message: String?) {

                }

            })
    }

    private fun getListByDay(day: String, month: String, year: String){
        val params = HashMap<String, String>()
        params["uid"] = this.user?.uid.toString()
        params["year"] = year
        params["month"] = month
        params["day"] = day
        SystemApi.provideService()
            .getBloListByDay(params)
            .compose(RxHelper.applySchedulers())
            .subscribe(object : RxSubscribe<UserTestBean>(this.viewDelegate, false){

                override fun _onNext(t: UserTestBean?) {
                    doResult(3, t)
                }

                override fun _onError(message: String?) {

                }

            })
    }

    private fun doResult(type: Int, t: UserTestBean?){
        t?.apply {
            list.clear()
            bloinfo?.apply {
                list.addAll(this)
                viewDelegate.adapter.notifyDataSetChanged()
                viewDelegate.fillDataToChart(generateDataLine(type))
                if(type == 1){
                    if(this.isEmpty()) return
                    getMonth(this, "${this[0].year}")
                }else if(type == 2){
                    if(this.isEmpty()) return
                    getDay(this, "${this[0].month}", "${this[0].year}")
                }
            }
        }
    }

    /**
     * 获取年份
     */
    private fun getYears(){
        this.user?.apply {
            val params = HashMap<String, String>()
            params["uid"] = this.uid.toString()
            SystemApi.provideService()
                .getYears(params)
                .compose(RxHelper.applySchedulers())
                .subscribe(object : RxSubscribe<List<String>>(this@HistoryPresenter.viewDelegate, false){

                    override fun _onNext(t: List<String>?) {
                        t?.let {
                            yearList.clear()
                            yearList.addAll(t)
                            yearAdapter.notifyDataSetChanged()
                        }
                    }

                    override fun _onError(message: String?) {

                    }

                })
        }
    }

    private fun getMonth(t: List<UserTestBean.BloinfoBean>, year: String){
        monthList.clear()
        t.forEach {
            if(TextUtils.equals(year, it.year)){
                monthList.add("${it.month}")
            }
        }
        monthList.distinct()
        monthList.add(0, "全部")
        monthAdapter.notifyDataSetChanged()
    }

    private fun getDay(t: List<UserTestBean.BloinfoBean>, month: String, year: String){
        dayList.clear()
        t.forEach {
            if(TextUtils.equals(it.year, year) && TextUtils.equals(it.month, month)){
                dayList.add("${it.day}")
            }
        }
        dayList.distinct()
        dayList.add(0, "全部")
        dayAdapter.notifyDataSetChanged()
    }

}