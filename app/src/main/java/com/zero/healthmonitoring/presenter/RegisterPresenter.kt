package com.zero.healthmonitoring.presenter

import com.zero.healthmonitoring.api.RxHelper
import com.zero.healthmonitoring.api.SystemApi
import com.zero.healthmonitoring.delegate.RegisterDelegate
import com.zero.healthmonitoring.extend.toast
import com.zero.library.network.RxSubscribe
import com.zero.library.widget.snakebar.Prompt

class RegisterPresenter : BasePresenter<RegisterDelegate>(){

    override fun doMain() {
        this.supportActionBar?.title = "注册"
    }

    fun submit(name: String, age: String, mobile: String, code: String, docid: String?, pw: String){
        val params = HashMap<String, String?>()
        params["name"] = name
        params["age"] = age
        params["type"] = "1"
        params["mobile"] = mobile
        params["code"] = code
        params["docid"] = docid
        params["pwd"] = pw
        SystemApi.provideService()
            .registerOrForget(params)
            .compose(RxHelper.applySchedulers())
            .subscribe(object : RxSubscribe<String>(this.viewDelegate, true){
                override fun _onError(message: String?) {

                }

                override fun _onNext(t: String?) {
                    toast(this@RegisterPresenter, "注册成功")
                    finish()
                }
            })
    }

    fun getVerifyCode(mobile: String){
        val params = HashMap<String, String>()
        params["type"] = "register"
        params["mobile"] = mobile
        SystemApi.provideService()
            .getVerifyCode(params)
            .compose(RxHelper.applySchedulers())
            .subscribe(object : RxSubscribe<String>(this.viewDelegate, true){

                override fun _onNext(t: String?) {
                    viewDelegate?.snakebar("已发送", Prompt.SUCCESS)
                }

                override fun _onError(message: String?) {

                }

            })
    }
}