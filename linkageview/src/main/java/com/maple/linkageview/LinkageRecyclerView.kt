package com.maple.linkageview

import android.content.Context
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
    private var mInitItems: List<BaseLinkageItem> = arrayListOf() // 数据集

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

    var isScrollSmoothly: Boolean = true // 平滑滚动
    var groupItemClickListener: BaseQuickLinkageAdapter.OnItemClickListener? = null
    var childItemClickListener: BaseQuickLinkageAdapter.OnItemClickListener? = null

    private fun initRecyclerView() {
        mRvGroup.layoutManager = LinearLayoutManager(context)
        mRvGroup.adapter = groupAdapter.apply {
            setOnItemClickListener { item: BaseLinkageItem?, position: Int ->
                val layoutManager = mRvChild.layoutManager
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
        mRvChild.layoutManager = LinearLayoutManager(context)
        mRvChild.adapter = childAdapter.apply {
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
            val view = LayoutInflater.from(context).inflate(R.layout.ms_item_child_header, null)
            mHeaderContainer.addView(view)
            mTvHeader = view.findViewById(R.id.tv_name)
            mTvHeader?.text = mInitItems.first().itemName
        }
        mRvChild.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val mChildLayoutManager = mRvChild.layoutManager as LinearLayoutManager
                // 针对于每一个Group Item View ： 将固定header上推
                val firstCompletePosition = mChildLayoutManager.findFirstCompletelyVisibleItemPosition()
                if (firstCompletePosition > 0 && firstCompletePosition < mInitItems.size && mInitItems[firstCompletePosition].isGroup) {
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
                    val currentGroupName = mInitItems[mFirstVisiblePosition].getShowGroupName()
                    if (mCurrentGroupName != currentGroupName) {
                        mCurrentGroupName = currentGroupName
                        mTvHeader?.text = mCurrentGroupName
                        // 组标题更改和联动 : change group adapter selected
                        groupAdapter.data.forEachIndexed { index, item ->
                            if (item.itemName == mCurrentGroupName) {
                                groupAdapter.updateSelectItem(index)
                                val layoutManager = mRvGroup.layoutManager
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
