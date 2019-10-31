package com.zero.healthmonitoring.presenter

import android.view.MenuItem
import com.zero.healthmonitoring.api.RxHelper
import com.zero.healthmonitoring.api.SystemApi
import com.zero.healthmonitoring.data.UserBean
import com.zero.healthmonitoring.delegate.LoginDelegate
import com.zero.library.network.RxSubscribe
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginPresenter : BasePresenter<LoginDelegate>(){

    override fun doMain() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        start(RegisterPresenter::class.java)
        return super.onOptionsItemSelected(item)
    }

    override fun bindEventListener() {
        super.bindEventListener()
        this.login_btn.setOnClickListener{ this.login() }
    }

    private fun login(){
//        if(this.login_account.text.toString().isEmpty()){
//            return
//        }
//        if(this.login_password.text.toString().isEmpty()){
//            return
//        }
        val params = HashMap<String, String>()
        params["uid"] = "123"//this.login_account.text.toString()
        params["pwd"] = "123456"//this.login_password.text.toString()
        SystemApi.provideService()
            .login(params)
            .compose(RxHelper.applySchedulers())
            .subscribe(object: RxSubscribe<UserBean>(this.viewDelegate, true){
                override fun _onNext(t: UserBean?) {
                    start(MainActivity::class.java)
                    finish()
                }

                override fun _onError(message: String?) {

                }
            })
    }
}