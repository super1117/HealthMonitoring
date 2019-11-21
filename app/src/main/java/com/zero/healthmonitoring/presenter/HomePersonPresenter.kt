package com.zero.healthmonitoring.presenter

import android.os.Bundle
import android.view.View
import com.zero.healthmonitoring.ActivityManager
import com.zero.healthmonitoring.R
import com.zero.healthmonitoring.base.BaseFragmentPresenter
import com.zero.healthmonitoring.delegate.ForgetPwPresenter
import com.zero.healthmonitoring.delegate.HomePersonDelegate
import com.zero.library.utils.SPUtil
import kotlinx.android.synthetic.main.app_bar_main.*
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
        this.viewDelegate.toolbar?.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        this.person_spo_record.text = if(this.user?.is_doctor?:0 == 1) "使用者数据" else "血氧历史记录"
    }

    override fun bindEvenListener() {
        super.bindEvenListener()
        this.viewDelegate.toolbar?.setNavigationOnClickListener { (this.activity as MainActivity).view_pager_main.setCurrentItem(0, true) }
        this.viewDelegate.setOnClickListener(this.onClick, R.id.person_spo_record, R.id.person_update_pw, R.id.person_exit, R.id.person_about)
    }

    private val onClick = View.OnClickListener {
        when(it.id){
            R.id.person_spo_record -> {
                if(this.user?.is_doctor?:0 == 1){
//                    readyGo(PersonDataPresenter::class.java)
                    (activity as MainActivity).view_pager_main.setCurrentItem(0, true)
                }else{
                    readyGo(HistoryPresenter::class.java)
                }
            }
            R.id.person_update_pw -> {
                val data = Bundle()
                data.putString("from", "person")
                readyGo(ForgetPwPresenter::class.java, data)
            }
            R.id.person_exit -> {
                SPUtil.remove(activity, "user")
                ActivityManager.instance.finishAllActivity()
                readyGo(LoginPresenter::class.java)
            }
            R.id.person_about -> readyGo(AboutPresenter::class.java)
        }
    }

}