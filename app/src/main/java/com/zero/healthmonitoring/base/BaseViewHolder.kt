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
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView

import androidx.annotation.IdRes
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView

import com.zero.library.listener.OnItemChildCheckChangeListener
import com.zero.library.listener.OnItemChildClickListener
import com.zero.library.listener.OnItemClickListener
import com.zero.library.listener.OnItemLongClickListener

class BaseViewHolder(
    private val mRecyclerView: RecyclerView,
    /**
     * 获取item根view
     * @return
     */
    private val rvItemView: View
) : RecyclerView.ViewHolder(rvItemView) {

    private val views: SparseArrayCompat<View> = SparseArrayCompat()

    private var childClickListener: OnItemChildClickListener? = null

    private var childCheckChangeListener: OnItemChildCheckChangeListener? = null

    /**
     * 通过控件的Id获取对应的控件，如果没有则加入mViews，则从item根控件中查找并保存到mViews中
     *
     * @param viewId
     * @return
     */
    fun <T : View> getView(@IdRes viewId: Int): T? {
        if (viewId == View.NO_ID) {
            return null
        }
        var view = this.views.get(viewId)
        if (view == null) {
            view = this.rvItemView.findViewById(viewId)
            this.views.put(viewId, view)
        }
        return view as T?
    }

    /**
     * 通过id设置文字
     * @param id
     * @param text
     */
    fun setText(id: Int, text: String) {
        (this.getView<View>(id) as TextView).text = text
    }

    /**
     * 设置item点击事件
     * @param clickListener
     */
    fun setOnItemClickListener(clickListener: OnItemClickListener?) {
        if (clickListener == null) return
        rvItemView.setOnClickListener { v ->
            clickListener.onItemClick(
                mRecyclerView,
                this.rvItemView,
                this.adapterPosition
            )
        }
    }

    /**
     * 设置item长按事件
     * @param longClickListener
     */
    fun setOnItemLongClickListener(longClickListener: OnItemLongClickListener?) {
        if (longClickListener == null) return
        rvItemView.setOnLongClickListener { v ->
            longClickListener.onItemLongClick(
                mRecyclerView,
                this.rvItemView,
                this.adapterPosition
            )
        }
    }

    /**
     * 设置item中子控件的点击监听
     * @param childClickListener
     */
    fun setOnItemChildClickListener(childClickListener: OnItemChildClickListener) {
        this.childClickListener = childClickListener
    }

    /**
     * 根据控件的ID设置点击
     * @param id
     */
    fun setOnItemChildClickById(@IdRes id: Int) {
        if (this.childClickListener == null) return
        val child = getView<View>(id) ?: return
        child.setOnClickListener { v ->
            this.childClickListener!!.onItemChildClick(
                this.rvItemView,
                child,
                this.adapterPosition
            )
        }
    }

    /**
     * 设置item子控件的选中事件监听
     * @param childCheckChangeListener
     */
    fun setOnItemChildCheckChangeListener(childCheckChangeListener: OnItemChildCheckChangeListener) {
        this.childCheckChangeListener = childCheckChangeListener
    }

    /**
     * 根据控件ID设置check事件
     * @param id
     */
    fun setOnItemChildCheckChangeById(@IdRes id: Int) {
        if (this.childCheckChangeListener == null) return
        val checkView = getView<View>(id) ?: return
        (checkView as CompoundButton).setOnCheckedChangeListener { buttonView, isChecked ->
            this.childCheckChangeListener!!.onItemChildCheckChange(
                this.rvItemView as ViewGroup, buttonView,
                adapterPosition, isChecked
            )
        }
    }
}
