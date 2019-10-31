package com.zero.healthmonitoring.delegate

import android.view.View
import android.widget.CheckBox
import com.zero.healthmonitoring.R
import com.zero.library.mvp.view.AppDelegate

class RegisterDelegate : AppDelegate(){

    override fun getRootLayoutId(): Int = R.layout.activity_register

    override fun initWidget() {
        super.initWidget()
        this.get<CheckBox>(R.id.check_doctor).setOnCheckedChangeListener { _, isChecked ->
            this.get<View>(R.id.user_doctor_id).visibility = if (isChecked) View.VISIBLE else View.GONE
        }
    }

}