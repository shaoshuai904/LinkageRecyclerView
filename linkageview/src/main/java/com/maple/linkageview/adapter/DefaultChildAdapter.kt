package com.maple.linkageview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.maple.linkageview.R
import com.maple.linkageview.bean.BaseLinkageItem
import com.maple.linkageview.databinding.MsItemChildBinding
import com.maple.linkageview.databinding.MsItemChildFooterBinding
import com.maple.linkageview.databinding.MsItemChildHeaderBinding

/**
 * 默认的二级列表适配器
 *
 * @author : shaoshuai
 * @date ：2021/1/4
 */
open class DefaultChildAdapter(
    private val mContext: Context
) : BaseQuickLinkageAdapter<BaseLinkageItem>() {

    companion object {
        const val type_item = 0
        const val type_header = 1
        const val type_footer = 2
    }

    override fun getItemViewType(position: Int): Int {
        val item: BaseLinkageItem = getItem(position)
        return if (item.isGroup) {
            type_header
        } else if (item.itemName.isNullOrEmpty() && !item.parentName.isNullOrEmpty()) {
            type_footer
        } else {
            type_item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            type_header -> HeaderHolder(DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.ms_item_child_header, parent, false))
            type_footer -> FooterHolder(DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.ms_item_child_footer, parent, false))
            // type_item -> ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.ms_item_child, parent, false))
            else -> ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.ms_item_child, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type = getItemViewType(holder.adapterPosition)
        val item = getItem(position)
        when (type) {
            type_item -> (holder as ItemHolder).bind(item)
            type_header -> (holder as HeaderHolder).bind(item)
            type_footer -> (holder as FooterHolder).bind(item)
        }
    }

    // 基础条目
    inner class ItemHolder(val binding: MsItemChildBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BaseLinkageItem) {
            bindViewClickListener(this)
            binding.tvTitle.text = item.itemName
            binding.tvDes.text = "详情说明"
        }
    }

    // 头
    inner class HeaderHolder(val binding: MsItemChildHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BaseLinkageItem) {
            // bindViewClickListener(this)
            binding.tvName.text = item.getShowGroupName()
        }
    }

    // 尾
    inner class FooterHolder(val binding: MsItemChildFooterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BaseLinkageItem) {
            // bindViewClickListener(this)
            binding.tvName.text = item.itemName
        }
    }
}
