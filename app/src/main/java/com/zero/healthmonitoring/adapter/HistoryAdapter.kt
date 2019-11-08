package com.zero.healthmonitoring.adapter

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zero.healthmonitoring.R
import com.zero.healthmonitoring.data.UserTestBean
import com.zero.library.base.BaseRvAdapter
import com.zero.library.base.BaseViewHolder

class HistoryAdapter(rv: RecyclerView) : BaseRvAdapter<UserTestBean.BloinfoBean>(rv){

    override fun onLayoutRes(position: Int): Int = R.layout.item_history

    override fun bindData(holder: BaseViewHolder?, position: Int, data: UserTestBean.BloinfoBean?) {
        holder?.apply {
            getView<TextView>(R.id.ih_time)?.text = data?.addtime?:"-"
            getView<TextView>(R.id.ih_spo2)?.text = data?.spo?:"-"
            getView<TextView>(R.id.ih_bpm)?.text = data?.bpm?:""
        }

    }


}