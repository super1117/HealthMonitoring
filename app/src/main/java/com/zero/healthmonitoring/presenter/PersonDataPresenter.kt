package com.zero.healthmonitoring.presenter

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.zero.healthmonitoring.R
import com.zero.healthmonitoring.api.RxHelper
import com.zero.healthmonitoring.api.SystemApi
import com.zero.healthmonitoring.base.BaseFragmentPresenter
import com.zero.healthmonitoring.data.UserBean
import com.zero.healthmonitoring.delegate.PersonDataDelegate
import com.zero.library.network.RxSubscribe
import kotlinx.android.synthetic.main.activity_demo.*
import kotlinx.android.synthetic.main.view_recycler.view.*

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
class PersonDataPresenter : BaseFragmentPresenter<PersonDataDelegate>(){

    private lateinit var list: ArrayList<UserBean>

    override fun doMain() {
        this.viewDelegate.get<Toolbar>(R.id.toolbar).title = "使用者数据"
        this.list = this.viewDelegate.adapter.data
        this.viewDelegate?.get<SwipeRefreshLayout>(R.id.refresh)?.isRefreshing = true
        this.getData()
    }

    override fun bindEvenListener() {
        super.bindEvenListener()
        this.viewDelegate.adapter.setOnItemClickListener { _, _, position ->
            val data = Bundle()
            data.putString("uid", list[position].uid)
            readyGo(HistoryPresenter::class.java, data)
        }
    }

    private fun getData(){
        val params = HashMap<String, String>()
        params["uid"] = this.user?.uid?:""
        SystemApi.provideService()
            .getPatientList(params)
            .compose(RxHelper.applySchedulers())
            .subscribe(object : RxSubscribe<List<UserBean>>(this.viewDelegate, false){
                override fun _onNext(t: List<UserBean>?) {
                    viewDelegate?.get<SwipeRefreshLayout>(R.id.refresh)?.isRefreshing = false
                    t?.let {
                        if(it.isEmpty()) return
                        list.addAll(t)
                        viewDelegate?.adapter?.notifyDataSetChanged()
                    }
                }

                override fun _onError(message: String?) {
                    viewDelegate?.get<SwipeRefreshLayout>(R.id.refresh)?.isRefreshing = false
                }
            })
    }
}