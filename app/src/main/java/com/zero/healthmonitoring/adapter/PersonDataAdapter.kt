package com.zero.healthmonitoring.adapter

import android.content.res.ColorStateList
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zero.healthmonitoring.R
import com.zero.healthmonitoring.data.UserBean
import com.zero.library.base.BaseRvAdapter
import com.zero.library.base.BaseViewHolder

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
 * create by szl on 2019-11-06
 */
class PersonDataAdapter(rv: RecyclerView) : BaseRvAdapter<UserBean>(rv){

    override fun onLayoutRes(position: Int): Int = R.layout.activity_data_person

    override fun bindData(holder: BaseViewHolder?, position: Int, data: UserBean?) {
        holder?.apply{
            data?.apply {
                getView<TextView>(R.id.adp_name)?.text = name?:""
                    getView<ImageView>(R.id.adp_icon)?.imageTintList = ColorStateList.valueOf(mContext.resources.getColor(when(position % 5){
                    0 -> R.color.google_blue
                    1 -> R.color.google_green
                    2 -> R.color.google_yellow
                    3 -> R.color.google_red
                    else -> R.color.gray
                }))
                getView<TextView>(R.id.adp_age)?.text = "${age?:0}岁"
            }
        }
    }


}