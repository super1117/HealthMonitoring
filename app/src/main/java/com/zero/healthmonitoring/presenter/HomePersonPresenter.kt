package com.zero.healthmonitoring.presenter

import android.view.View
import com.zero.healthmonitoring.R
import com.zero.healthmonitoring.base.BaseFragmentPresenter
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
        this.person_avatar.setImageResource(R.mipmap.ic_launcher_round)
        this.person_mobile.text = "(010)1234567"
    }

    override fun bindEvenListener() {
        super.bindEvenListener()
        this.viewDelegate.setOnClickListener(this.onClick, R.id.person_spo_record)
    }

    private val onClick = View.OnClickListener {
        when(it.id){
            R.id.person_spo_record -> readyGo(PersonDataPresenter::class.java)
        }
    }

}