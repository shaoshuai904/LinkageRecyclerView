package com.maple.linkageview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.maple.linkageview.adapter.BaseQuickLinkageAdapter
import com.maple.linkageview.adapter.DefaultGroupAdapter
import com.maple.linkageview.adapter.DefaultLinkageChildAdapter
import com.maple.linkageview.bean.BaseLinkageItem
import java.util.*

/**
 * 二级联动 RecyclerView
 *
 * @author : shaoshuai
 * @date ：2021/1/4
 */
class LinkageRecyclerView : MultiLevelRecyclerView {
    private val mGroupPositions: MutableList<Int> = ArrayList() // group 的索引值

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    override fun initData(linkageItems: List<BaseLinkageItem>?) {
        initData(linkageItems, DefaultGroupAdapter(context), DefaultLinkageChildAdapter(context))
    }

    override fun initData(
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
                mGroupPositions.add(index)
                groupNames.add(item)
            }
        }

        initRecyclerView()
        this.groupAdapter.refreshData(groupNames)
        childAdapter.refreshData(mDataList)
        initLinkageChild()
    }

    var isScrollSmoothly: Boolean = true // 平滑滚动

    private fun initRecyclerView() {
        binding.rvGroup.layoutManager = LinearLayoutManager(context)
        binding.rvGroup.adapter = groupAdapter.apply {
            setOnItemClickListener { item: BaseLinkageItem?, position: Int ->
                val layoutManager = binding.rvChild.layoutManager
                if (layoutManager is LinearLayoutManager) {
                    if (isScrollSmoothly) {
                        // 点击主条目时，次条目平滑滚动
                        val mScroller: LinearSmoothScroller = object : LinearSmoothScroller(context) {
                            override fun getHorizontalSnapPreference(): Int = SNAP_TO_START
                            override fun getVerticalSnapPreference(): Int = SNAP_TO_START
                        }
                        mScroller.targetPosition = mGroupPositions[position]
                        layoutManager.startSmoothScroll(mScroller)
                    } else {
                        layoutManager.scrollToPositionWithOffset(mGroupPositions[position], 0)
                    }
                }
                updateSelectItem(position)
                groupItemClickListener?.onItemClick(item, position)
            }
        }
        binding.rvChild.layoutManager = LinearLayoutManager(context)
        binding.rvChild.adapter = childAdapter.apply {
            setOnItemClickListener { item: BaseLinkageItem?, position: Int ->
                childItemClickListener?.onItemClick(item, position)
            }
        }
    }

    private var mTvHeader: TextView? = null // 固定标题View
    private var mFirstVisiblePosition = 0 // 第一个显示的item索引
    private var mCurrentGroupName: String? = null // 当前选中的group的名称

    private fun initLinkageChild() {
        if (mTvHeader == null) {
            val view = LayoutInflater.from(context).inflate(R.layout.ms_item_linkage_child_header, null)
            binding.flFixedHeader.addView(view)
            mTvHeader = view.findViewById(R.id.tv_name)
            mTvHeader?.text = mDataList.first().itemName
        }
        binding.rvChild.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val mChildLayoutManager = binding.rvChild.layoutManager as LinearLayoutManager
                // 针对于每一个Group Item View ： 将固定header上推
                val firstCompletePosition = mChildLayoutManager.findFirstCompletelyVisibleItemPosition()
                if (firstCompletePosition > 0 && firstCompletePosition < mDataList.size && mDataList[firstCompletePosition].isGroup) {
                    val view = mChildLayoutManager.findViewByPosition(firstCompletePosition)
                    if (view != null && mTvHeader != null && view.top <= mTvHeader!!.height) {
                        mTvHeader?.y = (view.top - mTvHeader!!.height).toFloat()
                    }
                }

                // 首个可见条目发生变化时：
                val firstPosition = mChildLayoutManager.findFirstVisibleItemPosition()
                if (mFirstVisiblePosition != firstPosition && firstPosition >= 0) {
                    mFirstVisiblePosition = firstPosition
                    mTvHeader?.y = 0f
                    val currentGroupName = mDataList[mFirstVisiblePosition].getShowGroupName()
                    if (mCurrentGroupName != currentGroupName) {
                        mCurrentGroupName = currentGroupName
                        mTvHeader?.text = mCurrentGroupName
                        // 组标题更改和联动 : change group adapter selected
                        groupAdapter.data.forEachIndexed { index, item ->
                            if (item.itemName == mCurrentGroupName) {
                                groupAdapter.updateSelectItem(index)
                                val layoutManager = binding.rvGroup.layoutManager
                                if (layoutManager is LinearLayoutManager) {
                                    val mScroller = LinearSmoothScroller(context).apply { targetPosition = index }
                                    layoutManager.startSmoothScroll(mScroller)
                                }
                            }
                        }
                    }
                }

            }
        })
    }


}
