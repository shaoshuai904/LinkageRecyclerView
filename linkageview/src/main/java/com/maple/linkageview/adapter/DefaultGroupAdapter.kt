package com.maple.linkageview.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.maple.linkageview.R
import com.maple.linkageview.bean.BaseItem
import com.maple.linkageview.databinding.MsItemGroupBinding

/**
 * 默认的一级列表适配器
 *
 * @author : shaoshuai
 * @date ：2021/1/4
 */
class DefaultGroupAdapter(
    private val mContext: Context
) : BaseQuickLinkageAdapter<BaseItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding: MsItemGroupBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.ms_item_group, parent, false)
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MyHolder).bind(position, getItem(position))
    }

    inner class MyHolder(val binding: MsItemGroupBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int, item: BaseItem) {
            bindViewClickListener(this)
            val selected = mSelectedPosition == position
            binding.tvGroup.let {
                it.text = item.itemName
                it.setBackgroundColor(ContextCompat.getColor(mContext, if (selected) R.color.colorPurple else R.color.colorWhite))
                it.setTextColor(ContextCompat.getColor(mContext, if (selected) R.color.colorWhite else R.color.colorGray))
                it.ellipsize = if (selected) TextUtils.TruncateAt.MARQUEE else TextUtils.TruncateAt.END
                it.isFocusable = selected
                it.isFocusableInTouchMode = selected
                it.marqueeRepeatLimit = if (selected) -1 else 0
            }
        }
    }

}
