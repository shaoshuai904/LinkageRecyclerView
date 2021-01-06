package com.maple.linkagerecyclerview.demo

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maple.linkagerecyclerview.R
import com.maple.linkagerecyclerview.data.CustomItemInfo
import com.maple.linkagerecyclerview.databinding.ItemChildCustomBinding
import com.maple.linkageview.adapter.BaseQuickLinkageAdapter
import com.maple.linkageview.bean.BaseItem
import com.maple.linkageview.databinding.MsItemChildFooterBinding
import com.maple.linkageview.databinding.MsItemChildHeaderBinding

/**
 * 自定义的二级列表适配器
 *
 * @author : shaoshuai
 * @date ：2021/1/4
 */
open class CustomChildAdapter(
    private val mContext: Context
) : BaseQuickLinkageAdapter<BaseItem>() {

    companion object {
        const val type_item = 0
        const val type_header = 1
        const val type_footer = 2
    }

    override fun getItemViewType(position: Int): Int {
        val item: BaseItem = getItem(position)
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
            type_header -> HeaderHolder(
                DataBindingUtil.inflate(LayoutInflater.from(mContext),
                    com.maple.linkageview.R.layout.ms_item_child_header, parent, false)
            )
            type_footer -> FooterHolder(
                DataBindingUtil.inflate(LayoutInflater.from(mContext),
                    com.maple.linkageview.R.layout.ms_item_child_footer, parent, false)
            )
            // type_item -> ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.ms_item_child, parent, false))
            else -> ItemHolder(DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_child_custom, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type = getItemViewType(holder.adapterPosition)
        val item: CustomItemInfo = getItem(position) as CustomItemInfo
        when (type) {
            type_item -> (holder as ItemHolder).bind(item)
            type_header -> (holder as HeaderHolder).bind(item)
            type_footer -> (holder as FooterHolder).bind(item)
        }
    }

    // 基础条目
    inner class ItemHolder(val binding: ItemChildCustomBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CustomItemInfo) {
            bindViewClickListener(this)
            binding.tvName.text = item.itemName
            binding.tvDetail.text = item.content
            Glide.with(mContext).load(item.imgUrl).into(binding.ivImage)
        }
    }

    // 头
    inner class HeaderHolder(val binding: MsItemChildHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BaseItem) {
            // bindViewClickListener(this)
            binding.tvName.text = item.getShowGroupName()
        }
    }

    // 尾
    inner class FooterHolder(val binding: MsItemChildFooterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BaseItem) {
            // bindViewClickListener(this)
            binding.tvName.text = item.itemName
        }
    }
}
