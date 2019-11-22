package com.zero.healthmonitoring.presenter

import android.text.TextUtils
import android.view.View
import com.zero.healthmonitoring.R
import com.zero.healthmonitoring.base.BaseFragmentPresenter
import com.zero.healthmonitoring.delegate.DoctorRegisterDelegate
import com.zero.library.widget.snakebar.Prompt
import kotlinx.android.synthetic.main.view_register_doctor.*
import kotlinx.android.synthetic.main.view_register_user.register_mobile
import kotlinx.android.synthetic.main.view_register_user.register_name
import kotlinx.android.synthetic.main.view_register_user.register_password
import kotlinx.android.synthetic.main.view_register_user.register_password_confirm
import kotlinx.android.synthetic.main.view_register_user.register_verify

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
        if(!this.isPas()){
            viewDelegate.snakebar("请将信息填写完整", Prompt.WARNING)
            return
        }
        if(!TextUtils.equals(this.register_password_confirm.text.toString(), this.register_password.text.toString())){
            viewDelegate.snakebar("两次密码输入不一致", Prompt.WARNING)
            return
        }
        if(this.register_mobile.text.toString().isEmpty()){
            viewDelegate.snakebar("请输入手机号", Prompt.WARNING)
            return
        }
        (this.activity as RegisterPresenter).getVerifyCode(this.register_mobile.text.toString())
    }

    private fun isPas() : Boolean{
        return when{
            this.register_name.text.toString().isEmpty() -> false
            this.register_unit.text.toString().isEmpty() -> false
            this.register_doctor_id.text.toString().isEmpty() -> false
            this.register_password.text.toString().isEmpty() -> false
            this.register_password_confirm.text.toString().isEmpty() -> false
            else -> true
        }
    }

    private fun onSubmit(){
        if(!this.isPas()){
            viewDelegate.snakebar("请将信息填写完整", Prompt.WARNING)
            return
        }
        if(!TextUtils.equals(this.register_password_confirm.text.toString(), this.register_password.text.toString())){
            viewDelegate.snakebar("两次密码输入不一致", Prompt.WARNING)
            return
        }
        if(this.register_mobile.text.toString().isEmpty()){
            viewDelegate.snakebar("请输入手机号", Prompt.WARNING)
            return
        }
        if(this.register_verify.text.toString().isEmpty()){
            viewDelegate.snakebar("请输入验证码", Prompt.WARNING)
            return
        }
        (this.activity as RegisterPresenter).submit(
            this.register_name.text.toString(),
            "",
            this.register_unit.text.toString(),
            this.register_mobile.text.toString(),
            this.register_verify.text.toString(),
            this.register_doctor_id.text.toString(),
            this.register_password.text.toString())
    }

}