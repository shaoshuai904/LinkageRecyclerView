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
import kotlin.math.max
import kotlin.math.min

/**
 * 多选 item适配器
 *
 * @author : shaoshuai
 * @date ：2020/11/20
 */
class MsGroupAdapter(
    private val mContext: Context// ,
    // val config: ActionSheetRecyclerDialog.Config
) : BaseQuickAdapter<BaseItem, RecyclerView.ViewHolder>() {
    private var mSelectedPosition = 0

    fun getSelectedPosition() = mSelectedPosition

    fun updateSelectItem(index: Int) {
        // 合规化index值，大于0 且 小于 size
        val number = min(max(index, 0), data.size - 1)
        mSelectedPosition = number
        notifyDataSetChanged()
    }

    fun getStrings(): List<String> {
        val strList = arrayListOf<String>()
        data.forEach {
            strList.add(it.itemName ?: "")
        }
        return strList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding: MsItemGroupBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.ms_item_group, parent, false
        )
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MyHolder).bind(position, getItem(position))
    }

    inner class MyHolder(val binding: MsItemGroupBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int, item: BaseItem) {
            bindViewClickListener(this)
            binding.apply {
                val selected = mSelectedPosition == position
                tvGroup.let {
                    it.text = item.itemName
                    it.setBackgroundColor(
                        ContextCompat.getColor(
                            mContext,
                            if (selected) R.color.colorPurple else R.color.colorWhite
                        )
                    )
                    it.setTextColor(
                        ContextCompat.getColor(
                            mContext,
                            if (selected) R.color.colorWhite else R.color.colorGray
                        )
                    )
                    it.ellipsize =
                        if (selected) TextUtils.TruncateAt.MARQUEE else TextUtils.TruncateAt.END
                    it.isFocusable = selected
                    it.isFocusableInTouchMode = selected
                    it.marqueeRepeatLimit = if (selected) -1 else 0
                }

//                root.background = config.itemBg
//                root.setPadding(config.itemPaddingLeft, config.itemPaddingTop, config.itemPaddingRight, config.itemPaddingBottom)
//                tvName.text = item.getShowName()
//                tvName.textSize = config.itemTextSizeSp
//                if (item.isSelected) {
//                    tvName.setTextColor(config.itemTextSelectedColor)
//                    ivMark.setImageDrawable(config.selectMark)
//                    ivMark.visibility = if (config.isShowMark) View.VISIBLE else View.GONE
//                } else {
//                    tvName.setTextColor(config.itemTextColor)
//                    ivMark.visibility = View.GONE
//                }

            }
        }
    }

}
