package com.zero.healthmonitoring.presenter

import com.zero.healthmonitoring.delegate.AboutDelegate

class AboutPresenter : BasePresenter<AboutDelegate>(){

    override fun doMain() {
        supportActionBar?.title = "关于我们"
    }

}