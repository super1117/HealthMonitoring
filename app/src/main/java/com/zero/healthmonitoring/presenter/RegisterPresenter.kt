package com.zero.healthmonitoring.presenter

import android.view.View
import com.zero.healthmonitoring.R
import com.zero.healthmonitoring.api.RxHelper
import com.zero.healthmonitoring.api.SystemApi
import com.zero.healthmonitoring.data.UserBean
import com.zero.healthmonitoring.delegate.RegisterDelegate
import com.zero.healthmonitoring.extend.toast
import com.zero.library.network.RxSubscribe

class RegisterPresenter : BasePresenter<RegisterDelegate>(){

    override fun doMain() {
        this.supportActionBar?.title = "注册"
    }

    override fun bindEventListener() {
        super.bindEventListener()
    }

    private val onClick = View.OnClickListener {
        when(it.id){
            R.id.register_btn -> this@RegisterPresenter.submit()
        }
    }

    private fun submit(){
        val param = HashMap<String, String>()
        SystemApi.provideService()
            .register(param)
            .compose(RxHelper.applySchedulers())
            .subscribe(object : RxSubscribe<UserBean>(this.viewDelegate, true){
                override fun _onNext(t: UserBean?) {
                    toast(this@RegisterPresenter, "注册成功")
                    finish()
                }

                override fun _onError(message: String?) {

                }

            })
    }
}