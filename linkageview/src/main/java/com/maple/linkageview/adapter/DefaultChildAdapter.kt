package com.maple.linkageview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.maple.linkageview.R
import com.maple.linkageview.bean.BaseLinkageItem
import com.maple.linkageview.databinding.MsItemChildBinding

/**
 * 默认的二级列表适配器
 *
 * @author : shaoshuai
 * @date ：2021/1/4
 */
open class DefaultChildAdapter(
    private val mContext: Context
) : BaseQuickLinkageAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val binding: MsItemChildBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.ms_item_child, parent, false)
        return ItemHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemHolder).bind(position, getItem(position))
    }

    inner class ItemHolder(val binding: MsItemChildBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, item: BaseLinkageItem) {
            bindViewClickListener(this)
            val selected = mSelectedPosition == position
            binding.tvName.text = item.itemName
            binding.tvName.setTextColor(ContextCompat.getColor(mContext, if (selected) R.color.ms_child_text_sel else R.color.ms_child_text_def))
            binding.ivMarker.visibility = if (selected) View.VISIBLE else View.GONE
        }
    }

}
