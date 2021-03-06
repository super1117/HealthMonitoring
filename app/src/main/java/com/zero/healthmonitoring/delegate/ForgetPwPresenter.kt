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
import kotlinx.android.synthetic.main.activity_demo.*
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
            this.forget_verify_layout.visibility = View.GONE
        }
    }

    override fun bindEventListener() {
        super.bindEventListener()
        this.viewDelegate.setOnClickListener(this.onClick, R.id.forget_btn, R.id.forget_verify_btn)
    }

    private val onClick = View.OnClickListener {
        when(it.id){
            R.id.forget_btn -> this.verifyData()
            R.id.forget_verify_btn -> this.getCode()
        }
    }

    private fun getCode(){
        if(this.forget_mobile.text.toString().isEmpty()){
            return
        }
        val params = HashMap<String, String>()
        params["type"] = "forget"
        params["mobile"] = this.forget_mobile.text.toString()
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

    private fun verifyData(){
        val fromPerson = TextUtils.equals(intent.getStringExtra("from"), "person")
        if(!fromPerson && this.forget_mobile.text.toString().isEmpty()){
            return
        }
        if(!fromPerson && this.forget_verify.text.toString().isEmpty()){
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
        if(fromPerson){
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