package com.zero.healthmonitoring.presenter

import android.os.Bundle
import android.view.View
import com.zero.healthmonitoring.R
import com.zero.healthmonitoring.base.BaseFragmentPresenter
import com.zero.healthmonitoring.delegate.ForgetPwPresenter
import com.zero.healthmonitoring.delegate.HomePersonDelegate
import kotlinx.android.synthetic.main.fragment_home_person.*
import kotlinx.android.synthetic.main.fragment_home_person.view.*

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
class HomePersonPresenter : BaseFragmentPresenter<HomePersonDelegate>(){

    override fun doMain() {
        this.viewDelegate.toolbar?.title = "我的"
        this.person_avatar.setImageResource(R.drawable.icon_launcher)
        this.person_mobile.text = this.user?.uid?:resources.getString(R.string.app_name )
    }

    override fun bindEvenListener() {
        super.bindEvenListener()
        this.viewDelegate.setOnClickListener(this.onClick, R.id.person_spo_record, R.id.person_update_pw)
    }

    private val onClick = View.OnClickListener {
        when(it.id){
            R.id.person_spo_record -> readyGo(HistoryPresenter::class.java)
            R.id.person_update_pw -> {
                val data = Bundle()
                data.putString("from", "person")
                readyGo(ForgetPwPresenter::class.java, data)
            }
        }
    }

}