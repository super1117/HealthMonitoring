package com.zero.healthmonitoring.presenter

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import com.zero.healthmonitoring.ActivityManager
import com.zero.healthmonitoring.R
import com.zero.healthmonitoring.data.UserBean
import com.zero.library.mvp.presenter.ActivityPresenter
import com.zero.library.mvp.view.IDelegate
import com.zero.library.utils.GsonUtil
import com.zero.library.utils.SPUtil
import com.zero.library.utils.StatusBarUtil
import kotlinx.android.synthetic.main.view_recycler.*
import kotlinx.android.synthetic.main.view_recycler.view.*

abstract class BasePresenter<T : IDelegate> : ActivityPresenter<T>() {

    var user: UserBean? = null

    protected fun contentView(): View = this.viewDelegate.rootView

    override fun getResources(): Resources {
        val res = super.getResources()
        val configuration = res.configuration
        if (configuration.fontScale != 1.0f) {
            configuration.fontScale = 1.0f
            res.updateConfiguration(configuration, res.displayMetrics)
        }
        return res
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        this.onWindow()
        super.onCreate(savedInstanceState)
        Log.e("currentPage", this::class.java.simpleName)
        ActivityManager.instance.addActivity(this)
        this.setITitle()
        this.verifyData()
        this.doMain()
    }

    protected open fun onWindow(){}

    protected fun setFullScreen(){
        val decorView = window.decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        decorView.systemUiVisibility = uiOptions
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    protected fun setStatusBar() {
        StatusBarUtil.setTranslucentStatus(this)
    }

    protected open fun getClick() : View.OnClickListener = View.OnClickListener { v -> }

    private fun setITitle() {
        this.viewDelegate.toolbar
    }

    private fun verifyData(){
        if(!TextUtils.isEmpty(SPUtil.get(this, "user", "").toString())){
            this.user = GsonUtil.setJsonToBean(SPUtil.get(this, "user", "").toString(), UserBean::class.java)
        }
        if(this.findViewById<View>(R.id.refresh) != null){
            this.refresh.setColorSchemeResources(R.color.colorAccent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        ActivityManager.instance.removeActivity(this)
        if (viewDelegate != null) {
            viewDelegate.onDestroyed()
        }
        super.onDestroy()
    }

    protected fun start(cls: Class<*>) {
        startActivity(Intent(this, cls))
    }

    protected fun start(cls: Class<*>, bundle: Bundle?) {
        val intent = Intent()
        intent.setClass(this, cls)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

    protected fun startForResult(cls: Class<*>, requesetCode: Int) {
        startActivityForResult(Intent(this, cls), requesetCode)
    }

    protected fun startForResult(cls: Class<*>, requestCode: Int, bundle: Bundle?) {
        val intent = Intent(this, cls)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivityForResult(intent, requestCode)
    }

    abstract fun doMain()

}