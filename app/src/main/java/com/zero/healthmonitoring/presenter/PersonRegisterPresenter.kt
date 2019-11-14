package com.zero.healthmonitoring.presenter

import android.text.TextUtils
import android.view.View
import com.zero.healthmonitoring.R
import com.zero.healthmonitoring.base.BaseFragmentPresenter
import com.zero.healthmonitoring.delegate.PersonRegisterDelegate
import com.zero.library.widget.snakebar.Prompt
import kotlinx.android.synthetic.main.view_register_user.*

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
class PersonRegisterPresenter : BaseFragmentPresenter<PersonRegisterDelegate>() {

    override fun doMain() {

    }

    override fun bindEvenListener() {
        super.bindEvenListener()
        this.viewDelegate.setOnClickListener(View.OnClickListener {
            when(it.id){
                R.id.register_verify_btn -> this.getVerifyCode()
                R.id.register_btn -> this.onSubmit()
            }
        }, R.id.register_btn, R.id.register_verify_btn)
    }

    private fun getVerifyCode(){
        if(this.register_mobile.text.toString().isEmpty()){
            viewDelegate.snakebar("请填写手机号", Prompt.WARNING)
            return
        }
        (this.activity as RegisterPresenter).getVerifyCode(this.register_mobile.text.toString())
    }

    private fun onSubmit(){
//        if(this.register_name.text.toString().isEmpty()){
//            return
//        }
//        if(this.register_age.text.toString().isEmpty()){
//            return
//        }
        if(this.register_verify.text.toString().isEmpty()){
            return
        }
        if(this.register_mobile.text.toString().isEmpty()){
            return
        }
        if(this.register_doctor_id.text.toString().isEmpty()){
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
        (this.activity as RegisterPresenter).submit(this.register_mobile.text.toString(),
            this.register_verify.text.toString(),
            this.register_doctor_id.text.toString(),
            this.register_password.text.toString())
    }

}