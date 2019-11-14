package com.zero.healthmonitoring.delegate

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.zero.healthmonitoring.ActivityManager
import com.zero.healthmonitoring.R
import com.zero.healthmonitoring.api.RxHelper
import com.zero.healthmonitoring.api.SystemApi
import com.zero.healthmonitoring.presenter.BasePresenter
import com.zero.healthmonitoring.presenter.LoginPresenter
import com.zero.library.network.RxSubscribe
import com.zero.library.widget.snakebar.Prompt
import kotlinx.android.synthetic.main.activity_pw_forget.*
import kotlinx.android.synthetic.main.activity_pw_forget.view.*

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
 * create by szl on 2019-11-12
 */
class ForgetPwPresenter : BasePresenter<ForgetPwDelegate>(){

    companion object{
        fun start(context: Context, from: String){
            val intent = Intent(context, ForgetPwPresenter::class.java)
            intent.putExtra("from", from)
            context.startActivity(intent)
        }
    }

    override fun doMain() {
        supportActionBar?.title = "修改密码"
        if(TextUtils.equals(intent.getStringExtra("from"), "person")){
            this.forget_verify_btn.visibility = View.GONE
            this.forget_verify.visibility = View.GONE
            this.forget_verify_text.visibility = View.GONE
        }
    }

    override fun bindEventListener() {
        super.bindEventListener()
        this.viewDelegate.setOnClickListener(this.onClick, R.id.forget_btn)
    }

    private val onClick = View.OnClickListener {
        when(it.id){
            R.id.forget_btn -> this.verifyData()
        }
    }

    private fun verifyData(){
        if(this.forget_mobile.text.toString().isEmpty()){
            return
        }
        if(!TextUtils.equals(intent.getStringExtra("from"), "person") && this.forget_verify.text.toString().isEmpty()){
            return
        }
        if(this.forget_password.text.toString().isEmpty()){
            return
        }
        if(forget_password_confirm.text.toString().isEmpty()){
            return
        }
        if(!TextUtils.equals(this.forget_password.text.toString(), this.forget_password_confirm.text.toString())){
            viewDelegate.snakebar("两次密码输入不一致", Prompt.WARNING)
            return
        }
        viewDelegate.showLoading()
        if(TextUtils.equals(intent.getStringExtra("from"), "person")){
            this.onSubmit()
        }else{
            this.onSubmitByCode()
        }
    }

    private fun onSubmit(){
        val params = HashMap<String, String>()
        params["uid"] = this.user?.uid?:""
        params["pwd"] = this.forget_password.text.toString()
        SystemApi.provideService()
            .editPw(params)
            .compose(RxHelper.applySchedulers())
            .subscribe(object : RxSubscribe<String>(this.viewDelegate, false){

                override fun _onNext(t: String?) {
                    viewDelegate?.snakebar("修改成功，请重新登录", Prompt.SUCCESS)
                    viewDelegate?.rootView?.postDelayed({
                        viewDelegate?.dismissLoading()
                        ActivityManager.instance.finishAllActivity()
                        start(LoginPresenter::class.java)
                    }, 3000)
                }

                override fun _onError(message: String?) {
                    viewDelegate?.dismissLoading()
                }
            })
    }

    private fun onSubmitByCode(){
        val params = HashMap<String, String>()
        params["type"] = "2"
        params["mobile"] = this.forget_mobile.text.toString()
        params["code"] = this.forget_verify.text.toString()
        params["pwd"] = this.forget_password.text.toString()
        SystemApi.provideService()
            .registerOrForget(params)
            .compose(RxHelper.applySchedulers())
            .subscribe(object : RxSubscribe<String>(this.viewDelegate, false){

                override fun _onNext(t: String?) {
                    viewDelegate?.apply {
                        snakebar("修改成功，请重新登录", Prompt.SUCCESS)
                        rootView.postDelayed({
                            dismissLoading()
                            getActivity<Activity>()?.finish()
                        }, 3000)
                    }
                }

                override fun _onError(message: String?) {
                    viewDelegate?.dismissLoading()
                }

            })
    }
}