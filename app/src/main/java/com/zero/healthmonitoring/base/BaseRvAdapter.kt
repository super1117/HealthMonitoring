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
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView

import com.zero.healthmonitoring.R
import com.zero.library.listener.OnItemChildCheckChangeListener
import com.zero.library.listener.OnItemChildClickListener
import com.zero.library.listener.OnItemClickListener
import com.zero.library.listener.OnItemLongClickListener
import com.zero.library.listener.OnLoadListener

import java.util.ArrayList

abstract class BaseRvAdapter<T>(private val recyclerView: RecyclerView) :
    RecyclerView.Adapter<BaseViewHolder>() {

    protected var mContext: Context = this.recyclerView.context

    protected var mData: MutableList<T> = ArrayList()

    private val inflater: LayoutInflater

    private var showFooterView = true

    private var needLoadMore = true

    private var childCheckChangeListener: OnItemChildCheckChangeListener? = null

    private var childClickListener: OnItemChildClickListener? = null

    private var longClickListener: OnItemLongClickListener? = null

    private var clickListener: OnItemClickListener? = null

    private var loadListener: OnLoadListener? = null

    /**
     * 获取列表数据
     * @return
     */
    /**
     * 设置列表数据
     * @param data
     */
    var data: List<T>
        get() = this.mData
        set(data) {
            this.mData.clear()
            this.mData.addAll(data)
            this.notifyDataSetChanged()
        }

    init {
        this.inflater = LayoutInflater.from(this.mContext)
    }

    override fun getItemCount(): Int {
        return if (showFooterView) this.mData.size + 1 else this.mData.size//(this.mData.size() > 0 ? this.mData.size() + 1 : this.mData.size())
    }

    @CallSuper
    override fun getItemViewType(position: Int): Int {
        return if (showFooterView && position == itemCount - 1) FOOTER else onLayoutRes(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = inflater.inflate(viewType, parent, false)
        val holder = BaseViewHolder(this.recyclerView, view)
        if (showFooterView && viewType == FOOTER) return holder
        holder.setOnItemChildCheckChangeListener(this.childCheckChangeListener)
        holder.setOnItemChildClickListener(this.childClickListener)
        holder.setOnItemClickListener(this.clickListener)
        holder.setOnItemLongClickListener(this.longClickListener)
        this.setItemChildListener(holder, viewType)
        return holder
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (this.showFooterView && position == itemCount - 1) {
            if (this.loadListener != null && this.needLoadMore && position >= 20) {
                this.loadListener!!.onLoad()
            }
        } else {
            bindData(holder, position, getItemData(position))
        }
    }

    /**
     * 设置子控件的点击事件
     * @param holder
     * @param viewType
     */
    protected fun setItemChildListener(holder: BaseViewHolder, viewType: Int) {}

    /**
     * list item layout
     * @param position
     * @return
     */
    protected abstract fun onLayoutRes(position: Int): Int

    /**
     * 给item绑定数据
     * @param holder
     * @param position
     * @param data
     */
    protected abstract fun bindData(holder: BaseViewHolder, position: Int, data: T)

    /**
     * 设置是否展示加载更多布局
     * @param showFooterView
     */
    fun setShowFooterView(showFooterView: Boolean) {
        this.showFooterView = showFooterView
    }

    fun setNeedLoadMore(isNeed: Boolean) {
        this.needLoadMore = isNeed
    }

    /**
     * 获取指定位置的数据
     * @param position
     * @return
     */
    fun getItemData(position: Int): T {
        return this.mData[position]
    }

    /**
     * item点击事件
     * @param clickListener
     */
    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        this.clickListener = clickListener
    }

    /**
     * item 长按事件
     * @param longClickListener
     */
    fun setOnItemLongClickListener(longClickListener: OnItemLongClickListener) {
        this.longClickListener = longClickListener
    }

    /**
     * item子控件点击事件
     * @param childClickListener
     */
    fun setOnItemChildClickListener(childClickListener: OnItemChildClickListener) {
        this.childClickListener = childClickListener
    }

    /**
     * item子控件check事件
     * @param childChangeListener
     */
    fun setOnItemChildCheckChangeListener(childChangeListener: OnItemChildCheckChangeListener) {
        this.childCheckChangeListener = childChangeListener
    }

    /**
     * 加载更多监听
     * @param listener
     */
    fun setOnLoadListener(listener: OnLoadListener) {
        this.loadListener = listener
    }

    companion object {

        private val FOOTER = R.layout.view_footer
    }
}