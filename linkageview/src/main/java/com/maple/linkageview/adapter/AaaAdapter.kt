package com.maple.linkageview.adapter

import androidx.recyclerview.widget.RecyclerView
import com.maple.linkageview.bean.BaseItem
import java.util.*

/**
 * @author : shaoshuai
 * @date ：2021/1/4
 */
abstract class AaaAdapter<T : BaseItem?> : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    var data: List<T> = ArrayList()
        private set

    fun refreshData(newData: List<T>?) {
        data = newData ?: ArrayList()
        notifyDataSetChanged()
    }

    fun getItem(position: Int): T {
        return data[position]
    }

    override fun getItemCount(): Int {
        return data.size
    }
}