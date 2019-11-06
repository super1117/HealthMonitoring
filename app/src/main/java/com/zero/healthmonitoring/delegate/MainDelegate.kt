package com.zero.healthmonitoring.delegate

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zero.library.mvp.view.AppDelegate
import androidx.appcompat.app.AppCompatActivity
import com.zero.healthmonitoring.R
import com.zero.healthmonitoring.presenter.HomePersonPresenter
import com.zero.healthmonitoring.presenter.HomePresenter
import com.zero.healthmonitoring.presenter.PersonPresenter
import com.zero.healthmonitoring.presenter.SpoJavaPresenter


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
 * create by szl on 2019-10-28
 */
class MainDelegate : AppDelegate(){

    private lateinit var contentPager: ViewPager

    private lateinit var drawer: DrawerLayout

    private val fragments = ArrayList<Fragment>()

    override fun getRootLayoutId(): Int = R.layout.activity_main

    override fun initWidget() {
        super.initWidget()
        this.contentPager = this.get(R.id.view_pager_main)
        this.drawer = this.get(R.id.drawer_layout)

        val nav = this.get<BottomNavigationView>(R.id.view_navigation_main)
        nav.setOnNavigationItemSelectedListener(this.mOnNavigationItemSelectedListener)

        this.fragments.add(SpoJavaPresenter())
        this.fragments.add(HomePersonPresenter())
        this.contentPager.offscreenPageLimit = 2
        this.contentPager.adapter = object : FragmentStatePagerAdapter(this.getActivity<AppCompatActivity>().supportFragmentManager) {
            override fun getItem(position: Int): Fragment? {
                return fragments[position]
            }

            override fun getCount(): Int {
                return fragments.size
            }
        }
        this.contentPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageSelected(position: Int) {
                nav.selectedItemId = if(position == 0) R.id.nav_home else R.id.nav_user
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }
        })
        rootView.postDelayed({
            val toolbar = (fragments[0] as SpoJavaPresenter).viewDelegate?.toolbar
            val toggle = ActionBarDrawerToggle(this.getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
            this.drawer.addDrawerListener(toggle)
            toggle.syncState()
        }, 1000)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {
                contentPager.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_user -> {
                contentPager.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}