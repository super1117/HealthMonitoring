package com.zero.healthmonitoring.delegate

import android.app.Activity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.zero.healthmonitoring.R
import com.zero.healthmonitoring.adapter.PersonDataAdapter
import com.zero.library.mvp.view.AppDelegate

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
class PersonDataDelegate : AppDelegate(){

    lateinit var adapter: PersonDataAdapter

    override fun getRootLayoutId(): Int = R.layout.layout_recycler_with_title

    override fun initWidget() {
        super.initWidget()
        this.rootView.setBackgroundColor(this.getActivity<Activity>().resources.getColor(R.color.bg_default))
        this.get<SwipeRefreshLayout>(R.id.refresh).setOnRefreshListener { get<SwipeRefreshLayout>(R.id.refresh).isRefreshing = false }
        val rv: RecyclerView = this.get(R.id.rv)
        val manager = LinearLayoutManager(this.getActivity())
        manager.orientation = RecyclerView.VERTICAL
        rv.layoutManager = manager
        this.adapter = PersonDataAdapter(rv)
        rv.adapter = this.adapter
    }

}