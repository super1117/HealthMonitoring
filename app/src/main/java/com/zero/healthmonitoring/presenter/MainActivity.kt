package com.zero.healthmonitoring.presenter

import androidx.core.view.get
import com.zero.healthmonitoring.delegate.MainDelegate
import com.zero.library.bean.ConfigBean
import com.zero.library.network.config.ConfigApi
import com.zero.library.utils.GsonUtil
import kotlinx.android.synthetic.main.app_bar_main.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.lang.Exception
import kotlin.system.exitProcess

class MainActivity : BasePresenter<MainDelegate>() {

    override fun doMain() {
        this.view_navigation_main.menu[0].title = if(this.user?.is_doctor?:0 == 1) "使用者数据" else "首页"
        this.getConfig()
    }

    override fun bindEventListener() {
        super.bindEventListener()
//        this.nav_view.setNavigationItemSelectedListener {
//            when(it.itemId){
//                R.id.nav_spo -> start(RecordPresenter::class.java)
//                R.id.nav_pb -> start(RecordPresenter::class.java)
//                R.id.nav_heart -> start(RecordPresenter::class.java)
//                R.id.nav_sleep -> start(RecordPresenter::class.java)
//                R.id.nav_fh -> start(RecordPresenter::class.java)
//                R.id.nav_setting -> start(RecordPresenter::class.java)
//            }
//            false
//        }
    }

    private fun getConfig(){
        ConfigApi.get(object : Callback{
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                try{
                    val json = response.body()?.string()
                    val config = GsonUtil.setJsonToBean(json, ConfigBean::class.java)
                    if(config.status == 0){
                       exitProcess(0)
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        })
    }

    override fun onBackPressed() {
        if(this.view_pager_main.currentItem != 0){
            this.view_pager_main.setCurrentItem(0, true)
        }else{
            super.onBackPressed()
        }
    }

}
