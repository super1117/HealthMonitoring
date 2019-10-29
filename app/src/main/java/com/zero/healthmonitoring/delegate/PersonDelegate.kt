package com.zero.healthmonitoring.delegate

import com.zero.healthmonitoring.R
import com.zero.library.mvp.view.AppDelegate

class PersonDelegate : AppDelegate(){

    override fun getRootLayoutId(): Int = R.layout.fragment_person

    override fun initWidget() {
        super.initWidget()
    }

}