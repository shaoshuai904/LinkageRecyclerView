package com.maple.linkageview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.maple.linkageview.R
import com.maple.linkageview.bean.BaseItem
import com.maple.linkageview.databinding.MsItemChildBinding
import com.maple.linkageview.databinding.MsItemChildFooterBinding
import com.maple.linkageview.databinding.MsItemChildGridBinding
import com.maple.linkageview.databinding.MsItemChildHeaderBinding

/**
 * 基本信息 item适配器
 *
 * @author : shaoshuai27
 * @date ：2020/1/6
 */
open class MsChildAdapter(
        private val mContext: Context
) : BaseQuickAdapter<BaseItem, RecyclerView.ViewHolder>() {
    private var spanCount: Int = 1

    fun setSpanCount(span: Int) {
        spanCount = span
    }

    fun getSpanCount() = spanCount

    fun isGridMode() = getSpanCount() > 1

    val IS_ITEM = 1
    val IS_HEADER = 0
    val IS_FOOTER = 3
    val IS_GRID = 2

    override fun getItemViewType(position: Int): Int {
        val item: BaseItem = getItem(position)
        return if (item.isGroup) {
            IS_HEADER
        } else if (item.itemName.isNullOrEmpty() && !item.parentName.isNullOrEmpty()) {
            IS_FOOTER
        } else if (isGridMode()) {
            IS_GRID
        } else {
            IS_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            IS_ITEM -> TitleHolder(DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.ms_item_child, parent, false))
            IS_GRID -> GridHolder(DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.ms_item_child_grid, parent, false))
            IS_HEADER -> HeaderHolder(DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.ms_item_child_header, parent, false))
            IS_FOOTER -> FooterHolder(DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.ms_item_child_footer, parent, false))
            else -> customizedViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type = getItemViewType(holder.adapterPosition)
        val item = getItem(position)
        when (type) {
            IS_ITEM -> (holder as TitleHolder).bind(item)
            IS_GRID -> (holder as GridHolder).bind(item)
            IS_HEADER -> (holder as HeaderHolder).bind(item)
            IS_FOOTER -> (holder as FooterHolder).bind(item)
        }
    }

    // 标题
    inner class TitleHolder(val binding: MsItemChildBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BaseItem) {
            bindViewClickListener(this)
            binding.tvTitle.text = item.itemName
            binding.tvDes.text = "详情说明"
        }
    }

    inner class GridHolder(val binding: MsItemChildGridBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BaseItem) {
            bindViewClickListener(this)
            binding.tvName.text = item.itemName
            binding.tvDes.text = "详情说明"
        }
    }

    inner class HeaderHolder(val binding: MsItemChildHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BaseItem) {
            // bindViewClickListener(this)
            binding.tvName.text = item.getShowGroupName()
        }
    }

    inner class FooterHolder(val binding: MsItemChildFooterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BaseItem) {
            // bindViewClickListener(this)
            binding.tvName.text = item.itemName
        }
    }

    // 分割线
    inner class LineHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(height: Int) {
            view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
        }
    }

    // Customized 定制款，对于需要填充自定义Item View的，重写以下两个方法。
    open fun customizedViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LineHolder(View(mContext))
    }

}
