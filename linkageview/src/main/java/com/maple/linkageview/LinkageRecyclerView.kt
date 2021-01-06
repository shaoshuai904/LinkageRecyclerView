package com.maple.linkageview

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.maple.linkageview.adapter.BaseQuickAdapter
import com.maple.linkageview.adapter.MsChildAdapter
import com.maple.linkageview.adapter.MsGroupAdapter
import com.maple.linkageview.bean.BaseItem
import com.maple.linkageview.utils.RecyclerViewScrollHelper
import java.util.*

/**
 * 二级联动 RecyclerView
 *
 * @author : shaoshuai
 * @date ：2021/1/4
 */
class LinkageRecyclerView : RelativeLayout {
    private lateinit var mRoot: ConstraintLayout
    private lateinit var mRvGroup: RecyclerView
    private lateinit var mRvChild: RecyclerView
    private lateinit var mHeaderContainer: FrameLayout
    private var mTvHeader: TextView? = null

    // group
    private val primaryAdapter: MsGroupAdapter by lazy { MsGroupAdapter(context) }
    private val mGroupPositions: MutableList<Int> = ArrayList() // group 的索引值
    private var mLastGroupName: String? = null

    // child
    private val childAdapter: MsChildAdapter by lazy { MsChildAdapter(context) }
    private lateinit var mChildLayoutManager: LinearLayoutManager
    private var mInitItems: List<BaseItem> = arrayListOf()

    private var mConfig: LinkageConfig = LinkageConfig(context)
    private var mTitleHeight = 0
    private var mFirstVisiblePosition = 0
    private var mScrollSmoothly = true
    private var mGroupClicked = false // 组item点击

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.ms_layout_linkage_view, this, true)
        mRoot = findViewById(R.id.linkage_layout)
        mRvGroup = findViewById(R.id.rv_group)
        mRvChild = findViewById(R.id.rv_child)
        mHeaderContainer = findViewById(R.id.header_container)
    }


    fun init(linkageItems: List<BaseItem>?) {
        init(linkageItems, LinkageConfig(context))
    }

    fun init(linkageItems: List<BaseItem>?, config: LinkageConfig = LinkageConfig(context)) {
        mConfig = config
        initRecyclerView()
        mInitItems = linkageItems ?: arrayListOf()

        val groupNames: MutableList<BaseItem> = ArrayList()
        mInitItems.forEachIndexed { index, item ->
            if (item.isGroup) {
                mGroupPositions.add(index)
                groupNames.add(item)
            }
        }

        primaryAdapter.refreshData(groupNames)
        childAdapter.refreshData(mInitItems)
        initLinkageChild()
    }

    var groupItemClickListener: BaseQuickAdapter.OnItemClickListener<BaseItem>? = null
    var childItemClickListener: BaseQuickAdapter.OnItemClickListener<BaseItem>? = null

    private fun initRecyclerView() {
        mRvGroup.layoutManager = LinearLayoutManager(context)
        mRvGroup.adapter = primaryAdapter.apply {
            setOnItemClickListener { item: BaseItem?, position: Int ->
                if (isScrollSmoothly) {
                    // 点击主条目时，次条目平滑滚动
                    RecyclerViewScrollHelper.smoothScrollToPosition(mRvChild, LinearSmoothScroller.SNAP_TO_START, mGroupPositions[position])
                } else {
                    mChildLayoutManager.scrollToPositionWithOffset(mGroupPositions[position], 0)
                }
                primaryAdapter.updateSelectItem(position)
                mGroupClicked = true
                groupItemClickListener?.onItemClick(item, position)
            }
        }
        setLevel2LayoutManager()
        mRvChild.adapter = childAdapter.apply {
            setOnItemClickListener { item: BaseItem?, position: Int ->
                childItemClickListener?.onItemClick(item, position)
            }
        }
    }

    private fun setLevel2LayoutManager() {
        if (childAdapter.isGridMode()) {
            mChildLayoutManager = GridLayoutManager(context, mConfig.spanCount)
            (mChildLayoutManager as GridLayoutManager).spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (childAdapter.getItem(position).isGroup) {
                        mConfig.spanCount
                    } else 1
                }
            }
        } else {
            mChildLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
        mRvChild.layoutManager = mChildLayoutManager
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
                    val groupNames = primaryAdapter.data
                    for (i in groupNames.indices) {
                        if (groupNames[i].itemName == mLastGroupName) {
                            if (mGroupClicked) {
                                if (primaryAdapter.getSelectedPosition() == i) {
                                    mGroupClicked = false
                                }
                            } else {
                                primaryAdapter.updateSelectItem(i)
                                RecyclerViewScrollHelper.smoothScrollToPosition(mRvGroup, LinearSmoothScroller.SNAP_TO_END, i)
                            }
                        }
                    }
                }
            }
        })
    }

    fun setSpanCount(span: Int) {
        childAdapter.setSpanCount(span)
        setLevel2LayoutManager()
        mRvChild.requestLayout()
    }

    var isScrollSmoothly: Boolean
        get() = mScrollSmoothly
        set(scrollSmoothly) {
            mScrollSmoothly = scrollSmoothly
        }

    // 设置固定高度
    fun setLayoutHeight(dp: Float) {
        mRoot.layoutParams = mRoot.layoutParams.apply { height = dp2px(context, dp) }
    }

    // 自定义Group区域宽度
    fun setGroupWidget(dp: Float) {
        mRvGroup.layoutParams = mRvGroup.layoutParams.apply { width = dp2px(context, dp) }
        mRvChild.layoutParams = mRvChild.layoutParams.apply { width = ViewGroup.LayoutParams.MATCH_PARENT }
    }

    // 自定义Group区域宽度 百分比
    fun setGroupPercent(percent: Float) {
        val guideline: Guideline = findViewById(R.id.gl_line)
        guideline.setGuidelinePercent(percent)
    }

    private fun dp2px(context: Context, dpVal: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.resources.displayMetrics).toInt()
    }



}