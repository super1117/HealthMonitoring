package com.zero.healthmonitoring.presenter

import android.view.MenuItem
import android.view.View
import com.zero.healthmonitoring.R
import com.zero.healthmonitoring.api.RxHelper
import com.zero.healthmonitoring.api.SystemApi
import com.zero.healthmonitoring.data.UserBean
import com.zero.healthmonitoring.delegate.ForgetPwPresenter
import com.zero.healthmonitoring.delegate.LoginDelegate
import com.zero.library.network.RxSubscribe
import com.zero.library.utils.GsonUtil
import com.zero.library.utils.SPUtil
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginPresenter : BasePresenter<LoginDelegate>(){

    override fun doMain() {
//        this.login_account.setText("123")
//        this.login_password.setText("123456")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        start(RegisterPresenter::class.java)
        return super.onOptionsItemSelected(item)
    }

    override fun bindEventListener() {
        super.bindEventListener()
        this.viewDelegate.setOnClickListener(this.onClick, R.id.login_btn, R.id.login_register, R.id.login_forget)
    }

    private val onClick = View.OnClickListener {
        when(it.id){
            R.id.login_btn -> this.login()
            R.id.login_register -> this.start(RegisterPresenter::class.java)
            R.id.login_forget -> start(ForgetPwPresenter::class.java)
        }
    }

    private fun login(){
        if(this.login_account.text.toString().isEmpty()){
            return
        }
        if(this.login_password.text.toString().isEmpty()){
            return
        }
        val params = HashMap<String, String>()
        params["uid"] = this.login_account.text.toString()
        params["pwd"] = this.login_password.text.toString()
        SystemApi.provideService()
            .login(params)
            .compose(RxHelper.applySchedulers())
            .subscribe(object: RxSubscribe<UserBean>(this.viewDelegate, true){
                override fun _onNext(t: UserBean?) {
                    SPUtil.put(this@LoginPresenter, "user", GsonUtil.setBeanToJson(t))
                    start(MainActivity::class.java)
                    finish()
                }

                override fun _onError(message: String?) {

                }
            })
    }
}