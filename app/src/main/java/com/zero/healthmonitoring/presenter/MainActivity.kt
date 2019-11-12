package com.zero.healthmonitoring.presenter

import com.zero.healthmonitoring.delegate.MainDelegate

class MainActivity : BasePresenter<MainDelegate>() {

    override fun doMain() {

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

}
