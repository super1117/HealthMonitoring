package com.zero.healthmonitoring.presenter

import com.zero.healthmonitoring.delegate.SplashDelegate
import com.zero.library.utils.SPUtil

class SplashPresenter : BasePresenter<SplashDelegate>(){

    override fun doMain() {
        viewDelegate.rootView.postDelayed({
            val isLogin = SPUtil.get(this@SplashPresenter, "login", false) as Boolean
            start(if(!isLogin) LoginPresenter::class.java else MainActivity::class.java)
            finish()
        }, 3000)
    }

}