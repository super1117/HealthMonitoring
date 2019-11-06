package com.zero.healthmonitoring.delegate

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.zero.healthmonitoring.R
import com.zero.healthmonitoring.presenter.DoctorRegisterPresenter
import com.zero.healthmonitoring.presenter.PersonRegisterPresenter
import com.zero.library.mvp.view.AppDelegate

class RegisterDelegate : AppDelegate(){

    private lateinit var viewPager: ViewPager

    private lateinit var tabLayout: TabLayout

    private val fragments = ArrayList<Fragment>()

    private lateinit var pagerAdapter: FragmentAdapter

    private val titles = ArrayList<String>()

    override fun getRootLayoutId(): Int = R.layout.activity_register

    override fun initWidget() {
        super.initWidget()
        this.viewPager = this.get(R.id.register_pager)
        this.tabLayout = this.get(R.id.register_tab)

        this.titles.add("使用者注册")
        this.titles.add("医生注册")
        this.fragments.add(PersonRegisterPresenter())
        this.fragments.add(DoctorRegisterPresenter())

        this.tabLayout.addTab(this.tabLayout.newTab().setText(this.titles[0]))
        this.tabLayout.addTab(this.tabLayout.newTab().setText(this.titles[1]))
        this.viewPager.offscreenPageLimit = 2

        this.pagerAdapter = FragmentAdapter(this.getActivity<AppCompatActivity>().supportFragmentManager)
        this.viewPager.adapter = this.pagerAdapter
        this.tabLayout.setupWithViewPager(this.viewPager)
        this.tabLayout.setTabsFromPagerAdapter(this.pagerAdapter)
    }

    inner class FragmentAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager){

        override fun getItem(position: Int): Fragment = this@RegisterDelegate.fragments[position]

        override fun getCount(): Int = this@RegisterDelegate.fragments.size

        override fun getPageTitle(position: Int): CharSequence? = this@RegisterDelegate.titles[position]

    }

}