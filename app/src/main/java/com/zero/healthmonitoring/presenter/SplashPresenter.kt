package com.zero.healthmonitoring.presenter

import com.zero.healthmonitoring.delegate.SplashDelegate

class SplashPresenter : BasePresenter<SplashDelegate>(){

    override fun doMain() {
        viewDelegate.rootView.postDelayed({
            start(if(user == null) LoginPresenter::class.java else MainActivity::class.java)
        }, 3000)
    }

}