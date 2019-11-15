package com.zero.healthmonitoring.base

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
 * create by szl on 2019-10-29
 */
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.zero.healthmonitoring.R
import com.zero.healthmonitoring.data.UserBean
import com.zero.healthmonitoring.presenter.BasePresenter

import com.zero.library.mvp.presenter.FragmentPresenter
import com.zero.library.mvp.view.AppDelegate
import com.zero.library.mvp.view.IDelegate
import com.zero.library.utils.GsonUtil
import com.zero.library.utils.SPUtil
import kotlinx.android.synthetic.main.view_recycler.*

abstract class BaseFragmentPresenter<T : AppDelegate> : FragmentPresenter<T>() {

    var user: UserBean? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        if(view?.findViewById<View>(R.id.refresh) != null){
            this.viewDelegate.get<SwipeRefreshLayout>(R.id.refresh).setColorSchemeResources(R.color.colorAccent)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        verifyData()
        doMain()
    }

    protected abstract fun doMain()

    private fun verifyData(){
        if (this.activity is BasePresenter<*>) {
            this.user = (this.activity as BasePresenter<*>).user
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        this.viewDelegate = null
    }

    /**
     * startActivity
     *
     * @param clazz
     */
    fun readyGo(clazz: Class<*>) {
        val intent = Intent(activity, clazz)
        startActivity(intent)
    }

    /**
     * startActivity with bundle
     *
     * @param clazz
     * @param bundle
     */
    protected fun readyGo(clazz: Class<*>, bundle: Bundle?) {
        val intent = Intent(activity, clazz)
        if (null != bundle) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    /**
     * startActivityForResult
     *
     * @param clazz
     * @param requestCode
     */
    protected fun readyGoForResult(clazz: Class<*>, requestCode: Int) {
        val intent = Intent(activity, clazz)
        startActivityForResult(intent, requestCode)
    }

    /**
     * startActivityForResult with bundle
     *
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    protected fun readyGoForResult(clazz: Class<*>, requestCode: Int, bundle: Bundle?) {
        val intent = Intent(activity, clazz)
        if (null != bundle) {
            intent.putExtras(bundle)
        }
        startActivityForResult(intent, requestCode)
    }

    /**
     * startActivity then finish
     *
     * @param clazz
     */
    protected fun readyGoThenKill(clazz: Class<*>) {
        val intent = Intent(activity, clazz)
        if (activity != null) {
            activity!!.finish()
        }
        startActivity(intent)
    }

}
