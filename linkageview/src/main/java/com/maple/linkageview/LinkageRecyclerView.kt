package com.maple.linkageview

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.maple.linkageview.adapter.BaseQuickLinkageAdapter
import com.maple.linkageview.adapter.DefaultChildAdapter
import com.maple.linkageview.adapter.DefaultGroupAdapter
import com.maple.linkageview.bean.BaseLinkageItem
import com.maple.linkageview.utils.RecyclerViewScrollHelper
import java.util.*

/**
 * 二级联动 RecyclerView
 *
 * @author : shaoshuai
 * @date ：2021/1/4
 */
class LinkageRecyclerView : RelativeLayout {
    private lateinit var mRvGroup: RecyclerView
    private lateinit var mRvChild: RecyclerView
    private lateinit var mHeaderContainer: FrameLayout

    private lateinit var groupAdapter: BaseQuickLinkageAdapter
    private lateinit var childAdapter: BaseQuickLinkageAdapter
    private val mGroupPositions: MutableList<Int> = ArrayList() // group 的索引值
    private var mInitItems: List<BaseLinkageItem> = arrayListOf()
    private var mLastGroupName: String? = null

    private var mTvHeader: TextView? = null
    private var mTitleHeight = 0

    private var mFirstVisiblePosition = 0
    private var mGroupClicked = false // 组item点击

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.ms_layout_linkage_view, this, true)
        mRvGroup = findViewById(R.id.rv_group)
        mRvChild = findViewById(R.id.rv_child)
        mHeaderContainer = findViewById(R.id.header_container)
    }

    fun init(linkageItems: List<BaseLinkageItem>?) {
        init(linkageItems, DefaultGroupAdapter(context), DefaultChildAdapter(context))
    }

    fun init(
        linkageItems: List<BaseLinkageItem>?,
        groupAdapter: BaseQuickLinkageAdapter,
        childAdapter: BaseQuickLinkageAdapter
    ) {
        this.mInitItems = linkageItems ?: arrayListOf()
        this.groupAdapter = groupAdapter
        this.childAdapter = childAdapter

        val groupNames: MutableList<BaseLinkageItem> = ArrayList()
        mInitItems.forEachIndexed { index, item ->
            if (item.isGroup) {
                mGroupPositions.add(index)
                groupNames.add(item)
            }
        }

        initRecyclerView()
        this.groupAdapter.refreshData(groupNames)
        childAdapter.refreshData(mInitItems)
        initLinkageChild()
    }

    var isScrollSmoothly: Boolean = true
    var groupItemClickListener: BaseQuickLinkageAdapter.OnItemClickListener? = null
    var childItemClickListener: BaseQuickLinkageAdapter.OnItemClickListener? = null

    private fun initRecyclerView() {
        mRvGroup.layoutManager = LinearLayoutManager(context)
        mRvGroup.adapter = groupAdapter.apply {
            setOnItemClickListener { item: BaseLinkageItem?, position: Int ->
                if (isScrollSmoothly) {
                    // 点击主条目时，次条目平滑滚动
                    RecyclerViewScrollHelper.smoothScrollToPosition(mRvChild, LinearSmoothScroller.SNAP_TO_START, mGroupPositions[position])
                } else {
                    val manager = mRvChild.layoutManager as LinearLayoutManager
                    manager.scrollToPositionWithOffset(mGroupPositions[position], 0)
                }
                updateSelectItem(position)
                mGroupClicked = true
                groupItemClickListener?.onItemClick(item, position)
            }
        }
        mRvChild.layoutManager = LinearLayoutManager(context)
        mRvChild.adapter = childAdapter.apply {
            setOnItemClickListener { item: BaseLinkageItem?, position: Int ->
                childItemClickListener?.onItemClick(item, position)
            }
        }
    }

    private fun initLinkageChild() {
        if (mTvHeader == null) {
            val view = LayoutInflater.from(context).inflate(R.layout.ms_item_child_header, null)
            mHeaderContainer.addView(view)
            mTvHeader = view.findViewById(R.id.tv_name)
        }
        val item = mInitItems[mFirstVisiblePosition]
        if (item.isGroup) {
            mTvHeader?.text = item.itemName
        }
        mRvChild.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                mTitleHeight = mTvHeader!!.measuredHeight
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val mChildLayoutManager = mRvChild.layoutManager as LinearLayoutManager
                val firstPosition = mChildLayoutManager.findFirstVisibleItemPosition()
                val firstCompletePosition = mChildLayoutManager.findFirstCompletelyVisibleItemPosition()
                val items = mInitItems

                // 粘性逻辑
                if (firstCompletePosition > 0 && firstCompletePosition < items.size && items[firstCompletePosition].isGroup) {
                    val view = mChildLayoutManager.findViewByPosition(firstCompletePosition)
                    if (view != null && view.top <= mTitleHeight) {
                        mTvHeader?.y = (view.top - mTitleHeight).toFloat()
                    }
                }

                // 组标题更改和联动
                var groupNameChanged = false
                if (mFirstVisiblePosition != firstPosition && firstPosition >= 0) {
                    mFirstVisiblePosition = firstPosition
                    mTvHeader?.y = 0f
                    val item = items[mFirstVisiblePosition]
                    if (item.isGroup) {
                        mTvHeader?.text = item.itemName
                    }
                    val currentGroupName = items[mFirstVisiblePosition].getShowGroupName()
                    if (TextUtils.isEmpty(mLastGroupName) || mLastGroupName != currentGroupName) {
                        mLastGroupName = currentGroupName
                        groupNameChanged = true
                        mTvHeader?.text = mLastGroupName
                    }
                }

                // the following logic can not be perfect, because tvHeader's title may not
                // always equals to the title of selected primaryItem, while there
                // are several groups which has little items to stick group item to tvHeader.
                //
                // To avoid to this extreme situation, my idea is to add a footer on the bottom,
                // to help wholly execute this logic.
                if (groupNameChanged) {
                    val groupNames = groupAdapter.data
                    for (i in groupNames.indices) {
                        if (groupNames[i].itemName == mLastGroupName) {
                            if (mGroupClicked) {
                                if (groupAdapter.selectedPosition == i) {
                                    mGroupClicked = false
                                }
                            } else {
                                groupAdapter.updateSelectItem(i)
                                RecyclerViewScrollHelper.smoothScrollToPosition(mRvGroup, LinearSmoothScroller.SNAP_TO_END, i)
                            }
                        }
                    }
                }
            }
        })
    }


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
        mRvGroup.layoutParams = mRvGroup.layoutParams.apply { width = newWidth }
    }

    // 自定义Child区域宽度
    fun setChildWidth(newWidth: Int) {
        mRvChild.layoutParams = mRvChild.layoutParams.apply { width = newWidth }
    }

    // 自定义Child区域一行显示个数
    fun setChildSpanSize(span: Int) {
        mRvChild.layoutManager = if (span > 1) {
            GridLayoutManager(context, span).apply {
                spanSizeLookup = object : SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (childAdapter.getItem(position).isGroup) span else 1
                    }
                }
            }
        } else {
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
        mRvChild.requestLayout()
    }
}
