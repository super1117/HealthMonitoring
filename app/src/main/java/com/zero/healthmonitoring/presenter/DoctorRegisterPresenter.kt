package com.zero.healthmonitoring.presenter

import android.text.TextUtils
import android.view.View
import com.zero.healthmonitoring.R
import com.zero.healthmonitoring.api.RxHelper
import com.zero.healthmonitoring.api.SystemApi
import com.zero.healthmonitoring.base.BaseFragmentPresenter
import com.zero.healthmonitoring.data.UserBean
import com.zero.healthmonitoring.delegate.DoctorRegisterDelegate
import com.zero.library.network.RxSubscribe
import com.zero.library.widget.snakebar.Prompt
import kotlinx.android.synthetic.main.activity_sop.*
import kotlinx.android.synthetic.main.view_register_user.*
import kotlinx.android.synthetic.main.view_register_user.view.*
import kotlinx.android.synthetic.main.view_register_user.view.register_mobile

////////////////////////////////////////////////////////////////////
//                          _ooOoo_                               //
//                         o8888888o                              //
//                         88" . "88                              //
//                         (| ^_^ |)                              //
//                         O\  =  /O                              //
//                      ____/`---'\____                           //
//                    .'  \\|     |//  `.                         //
//                   /  \\|||  :  |||//  \                        //
//                  /  _||||| -:- |||||-  \                       //
//                  |   | \\\  -  /// |   |                       //
//                  | \_|  ''\---/''  |   |                       //
//                  \  .-\__  `-`  ___/-. /                       //
//                ___`. .'  /--.--\  `. . ___                     //
//              ."" '<  `.___\_<|>_/___.'  >'"".                  //
//            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
//            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
//      ========`-.____`-.___\_____/___.-`____.-'========         //
//                           `=---='                              //
//      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
//                     佛祖保佑       永无BUG                       //
//                    此代码模块已经经过开光处理                      //
////////////////////////////////////////////////////////////////////
/**
 * create by szl on 2019-11-02
 */
class DoctorRegisterPresenter : BaseFragmentPresenter<DoctorRegisterDelegate>() {

    override fun doMain() {

    }

    override fun bindEvenListener() {
        super.bindEvenListener()
        this.viewDelegate.setOnClickListener(View.OnClickListener {
            when(it.id){
                R.id.register_btn -> this.onSubmit()
                R.id.register_verify_btn -> this.getVerifyCode()
            }
        }, R.id.register_btn, R.id.register_verify_btn)
    }

    private fun getVerifyCode(){
        if(this.register_mobile.text.toString().isEmpty()){
            viewDelegate.snakebar("请输入手机号", Prompt.WARNING)
            return
        }
        if(this.register_mobile.text.toString().length < 11){
            viewDelegate.snakebar("请输入正确的手机号", Prompt.WARNING)
            return
        }

    }

    private fun onSubmit(){
        if(this.register_mobile.text.toString().isEmpty()){
            return
        }
        if(this.register_verify.text.toString().isEmpty()){
            return
        }
        if(this.register_password.text.toString().isEmpty()){
            return
        }
        if(this.register_password_confirm.text.toString().isEmpty()){
            return
        }
        if(!TextUtils.equals(this.register_password.text.toString(), this.register_password_confirm.text.toString())){
            viewDelegate.snakebar("两次密码输入不一致", Prompt.WARNING)
            return
        }
        val params = HashMap<String, String>()
        params["uid"] = ""
        params["pwd"] = ""
        params["docid"] = ""
        SystemApi.provideService()
            .register(params)
            .compose(RxHelper.applySchedulers())
            .subscribe(object : RxSubscribe<UserBean>(this.viewDelegate, true){
                override fun _onNext(t: UserBean?) {
                    this@DoctorRegisterPresenter.activity?.finish()
                }

                override fun _onError(message: String?) {

                }
            })
    }

}