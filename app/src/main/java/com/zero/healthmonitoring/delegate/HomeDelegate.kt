package com.zero.healthmonitoring.delegate

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import com.zero.healthmonitoring.R
import com.zero.library.mvp.view.AppDelegate
import com.zero.library.utils.DensityUtils
import com.zero.library.utils.ScreenUtils
import android.view.animation.AccelerateInterpolator
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.view.animation.DecelerateInterpolator
import com.zero.healthmonitoring.presenter.SpoJavaPresenter
import com.zero.healthmonitoring.presenter.SpoPresenter


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
class HomeDelegate : AppDelegate(){

    override fun getRootLayoutId(): Int = R.layout.fragment_home

    override fun initWidget() {
        super.initWidget()
        val content = this.get<ViewGroup>(R.id.content_view)
        val wh = (ScreenUtils.getScreenWidth(this.getActivity()) - DensityUtils.dp2px(this.getActivity(), 36f)) / 2
        content.forEach { view ->
            view.layoutParams.height = wh
            view.layoutParams.width = wh
            view.requestLayout()
            view.setOnClickListener(this.onclick)
            view.setOnTouchListener(this.onTouchListener)
        }
    }

    private val onclick = View.OnClickListener {
        val intent = Intent()
        when(it.id){
            R.id.card_0 -> intent.setClass(this.getActivity(), SpoJavaPresenter::class.java)
            R.id.card_1 -> intent.setClass(this.getActivity(), SpoJavaPresenter::class.java)
            R.id.card_2 -> intent.setClass(this.getActivity(), SpoJavaPresenter::class.java)
            R.id.card_3 -> intent.setClass(this.getActivity(), SpoJavaPresenter::class.java)
            R.id.card_4 -> intent.setClass(this.getActivity(), SpoJavaPresenter::class.java)
        }
        this.getActivity<Activity>().startActivity(intent)
    }

    private val onTouchListener = View.OnTouchListener { view, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val upAnim = ObjectAnimator.ofFloat(view, "translationZ", 16F)
                upAnim.duration = 150
                upAnim.interpolator = DecelerateInterpolator()
                upAnim.start()
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val downAnim = ObjectAnimator.ofFloat(view, "translationZ", 0F)
                downAnim.duration = 150
                downAnim.interpolator = AccelerateInterpolator()
                downAnim.start()
            }
        }
        false
    }

}