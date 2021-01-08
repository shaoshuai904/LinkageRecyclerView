package com.maple.linkageview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maple.linkageview.adapter.BaseQuickLinkageAdapter
import com.maple.linkageview.adapter.DefaultChildAdapter
import com.maple.linkageview.adapter.DefaultGroupAdapter
import com.maple.linkageview.bean.BaseLinkageItem
import com.maple.linkageview.databinding.MsLayoutLinkageViewBinding
import java.util.*

/**
 * 多级 RecyclerView
 *
 * @author : shaoshuai
 * @date ：2021/1/7
 */
open class MultiLevelRecyclerView : FrameLayout {
    lateinit var binding: MsLayoutLinkageViewBinding
    lateinit var groupAdapter: BaseQuickLinkageAdapter
    lateinit var childAdapter: BaseQuickLinkageAdapter
    var mDataList: List<BaseLinkageItem> = arrayListOf() // 数据集

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView(context: Context) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.ms_layout_linkage_view, this, true)
    }

    open fun initData(linkageItems: List<BaseLinkageItem>?) {
        initData(linkageItems, DefaultGroupAdapter(context), DefaultChildAdapter(context))
    }

    open fun initData(
        linkageItems: List<BaseLinkageItem>?,
        groupAdapter: BaseQuickLinkageAdapter,
        childAdapter: BaseQuickLinkageAdapter
    ) {
        this.mDataList = linkageItems ?: arrayListOf()
        this.groupAdapter = groupAdapter
        this.childAdapter = childAdapter

        val groupNames: MutableList<BaseLinkageItem> = ArrayList()
        mDataList.forEachIndexed { index, item ->
            if (item.isGroup) {
                groupNames.add(item)
            }
        }

        initRecyclerView()
        this.groupAdapter.refreshData(groupNames)
        childAdapter.refreshData(getChildDataList(groupNames[0].itemName))
    }

    fun getChildDataList(groupName: String?): List<BaseLinkageItem> {
        return mDataList.filter { it.parentName == groupName }
    }

    private fun initRecyclerView() {
        binding.rvGroup.layoutManager = LinearLayoutManager(context)
        binding.rvGroup.adapter = groupAdapter.apply {
            setOnItemClickListener { item: BaseLinkageItem, position: Int ->
                updateSelectItem(position)
                childAdapter.refreshData(getChildDataList(item.itemName))
                groupItemClickListener?.onItemClick(item, position)
            }
        }
        binding.rvChild.layoutManager = LinearLayoutManager(context)
        binding.rvChild.adapter = childAdapter.apply {
            setOnItemClickListener { item: BaseLinkageItem, position: Int ->
                updateSelectItem(position)
                childItemClickListener?.onItemClick(item, position)
            }
        }
    }

    var groupItemClickListener: BaseQuickLinkageAdapter.OnItemClickListener? = null
    var childItemClickListener: BaseQuickLinkageAdapter.OnItemClickListener? = null

    // 设置固定宽度
    fun setLayoutWidth(newWidth: Int) {
        this.layoutParams = this.layoutParams.apply { width = newWidth }
    }

    // 设置固定高度
    fun setLayoutHeight(newHeight: Int) {
        this.layoutParams = this.layoutParams.apply { height = newHeight }
    }

    // 自定义Group区域宽度
    fun setGroupWidth(newWidth: Int) {
        binding.rvGroup.layoutParams = binding.rvGroup.layoutParams.apply { width = newWidth }
    }

    // 自定义Child区域宽度
    fun setChildWidth(newWidth: Int) {
        binding.rvChild.layoutParams = binding.rvChild.layoutParams.apply { width = newWidth }
    }

    // 自定义Child区域一行显示个数
    fun setChildSpanSize(span: Int) {
        binding.rvChild.layoutManager = if (span > 1) {
            GridLayoutManager(context, span).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (childAdapter.getItem(position).isGroup) span else 1
                    }
                }
            }
        } else {
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
        binding.rvChild.requestLayout()
    }

}
