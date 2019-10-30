package com.zero.healthmonitoring.presenter

import android.app.DatePickerDialog
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import com.zero.healthmonitoring.R
import com.zero.healthmonitoring.delegate.RecordDelegate
import kotlinx.android.synthetic.main.activity_recorde.*
import java.text.DateFormat
import java.util.*

class RecordPresenter : BasePresenter<RecordDelegate>() {

    private lateinit var calendar: Calendar

    override fun doMain() {
        this.supportActionBar?.title = "血氧记录"
        this.calendar = Calendar.getInstance()
    }

    override fun bindEventListener() {
        super.bindEventListener()
        this.viewDelegate.setOnClickListener(this.onCLick, R.id.record_date_select)
    }

    private val onCLick = View.OnClickListener {
        when(it.id){
            R.id.record_date_select -> {
                selectDate()
            }
        }
    }

    private fun selectDate() {
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.time)
                record_date_select.text = date
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}