package com.zero.healthmonitoring.adapter

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zero.healthmonitoring.R
import com.zero.library.base.BaseRvAdapter
import com.zero.library.base.BaseViewHolder
import com.zero.library.utils.TimeUtils

class HistoryAdapter(rv: RecyclerView) : BaseRvAdapter<String>(rv){

    override fun onLayoutRes(position: Int): Int = R.layout.item_history

    override fun bindData(holder: BaseViewHolder?, position: Int, data: String?) {
        holder?.apply {
            getView<TextView>(R.id.ih_time)?.text = TimeUtils.getTimeNow()
            getView<TextView>(R.id.ih_spo2)?.text = "98"
            getView<TextView>(R.id.ih_bpm)?.text = "64"
        }

    }


}