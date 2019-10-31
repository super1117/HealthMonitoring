package com.zero.healthmonitoring.presenter

import android.text.TextUtils
import android.view.View
import com.zero.healthmonitoring.R
import com.zero.healthmonitoring.api.RxHelper
import com.zero.healthmonitoring.api.SystemApi
import com.zero.healthmonitoring.data.UserBean
import com.zero.healthmonitoring.delegate.RegisterDelegate
import com.zero.healthmonitoring.extend.toast
import com.zero.library.network.RxSubscribe
import kotlinx.android.synthetic.main.activity_register.*

class RegisterPresenter : BasePresenter<RegisterDelegate>(){

    override fun doMain() {
        this.supportActionBar?.title = "注册"
    }

    override fun bindEventListener() {
        super.bindEventListener()
        this.viewDelegate.setOnClickListener(this.onClick, R.id.register_btn)
    }

    private val onClick = View.OnClickListener {
        when(it.id){
            R.id.register_btn -> this@RegisterPresenter.submit()
        }
    }

    private fun submit(){
        if(this.user_account.text.toString().isEmpty()){
            return
        }
        if(this.check_doctor.isChecked && this.user_doctor_id.text.toString().isEmpty()){
            return
        }
        if(this.user_password.text.toString().isEmpty()){
            return
        }
        if(this.user_password_confirm.text.toString().isEmpty()){
            return
        }
        if(TextUtils.equals(this.user_password.text.toString(), this.user_password_confirm.text.toString())){
            toast(this, "两次密码输入不一致")
            return
        }
        val param = HashMap<String, String>()
        param["uid"] = this.user_account.text.toString()
        param["pwd"] = this.user_password.text.toString()
        if(this.check_doctor.isChecked){
            param["docid"] = this.user_doctor_id.text.toString()
        }
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