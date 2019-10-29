package com.zero.healthmonitoring.presenter

import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import com.zero.healthmonitoring.base.BaseFragmentPresenter
import com.zero.healthmonitoring.delegate.PersonDelegate
import android.app.DatePickerDialog
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.zero.healthmonitoring.R
import com.zero.library.utils.DensityUtils
import java.text.DateFormat
import java.util.*


class PersonPresenter : BaseFragmentPresenter<PersonDelegate>() {

    private lateinit var calendar: Calendar

    override fun doMain() {
        this.calendar = Calendar.getInstance()
    }

    override fun bindEvenListener() {
        super.bindEvenListener()
        (this.viewDelegate.rootView as ViewGroup).forEach {
            it.setOnClickListener(this.onClick)
        }
    }

    private val onClick = View.OnClickListener {
        when (it.id) {
            R.id.person_sex -> this.settingInfo("性别", R.id.person_sex_tx)
            R.id.person_age -> this.settingInfo("年龄", R.id.person_age_tx)
            R.id.person_height -> this.settingInfo("身高(cm)", R.id.person_height_tx)
            R.id.person_weight -> this.settingInfo("体重(kg)", R.id.person_weight_tx)
            R.id.person_birthday -> this.selectDate()
            R.id.person_address -> this.settingInfo("居住地", R.id.person_address_tx)
        }
    }

    private fun selectDate() {
        val datePickerDialog = DatePickerDialog(
            context!!,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.time)
                (viewDelegate.get<TextView>(R.id.person_birthday_tx)).text = date
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun settingInfo(title: String, txId: Int) {
        val view = viewDelegate?.get<TextView>(txId)
        val group = LinearLayout(context)
        group.setPadding(DensityUtils.dp2px(context, 16f), 0, DensityUtils.dp2px(context, 16f), 0)
        val input = EditText(context)
        val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        input.layoutParams = param
        group.addView(input)
        AlertDialog.Builder(context!!).setTitle(title).setView(group)
            .setNegativeButton(context!!.resources.getString(R.string.cancel), null)
            .setPositiveButton(context!!.resources.getString(R.string.ok)) { _, _ ->
                view?.text = input.text.toString()
            }.show()
    }
}