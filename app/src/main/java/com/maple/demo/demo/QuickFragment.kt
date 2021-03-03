package com.maple.demo.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.maple.demo.R
import com.maple.demo.data.TestDataUtils
import com.maple.demo.databinding.FragmentQuickBinding
import com.maple.demo.utils.DensityUtils.dp2px
import com.maple.linkageview.LinkageRecyclerView
import com.maple.linkageview.MultiLevelRecyclerView
import com.maple.linkageview.adapter.BaseQuickLinkageAdapter

/**
 * 快速使用
 *
 * @author : shaoshuai
 * @date ：2021/1/6
 */
class QuickFragment : Fragment() {
    private lateinit var binding: FragmentQuickBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_quick, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLinkageData(binding.linkage)
        initMultiLevelData(binding.multiLevel)
    }


    private fun initMultiLevelData(linkage: MultiLevelRecyclerView) {
        val data = TestDataUtils.getTestData1()
        with(linkage) {
            initData(data)
            setLayoutWidth(260f.dp2px(context))// 设置总布局宽度
            setLayoutHeight(200f.dp2px(context))// 设置总布局高度
            setGroupWidth(70f.dp2px(context))// 设置左侧父View宽度
            // setChildWidth(70f.dp2px(context))// 设置右侧子View宽度
            groupItemClickListener = BaseQuickLinkageAdapter.OnItemClickListener { item, position ->
                Snackbar.make(view!!, item.itemName!!, Snackbar.LENGTH_SHORT).show()
            }
            childItemClickListener = BaseQuickLinkageAdapter.OnItemClickListener { item, position ->
                Snackbar.make(view!!, item.itemName!!, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun initLinkageData(linkage: LinkageRecyclerView) {
        val data = TestDataUtils.getTestData()
        with(linkage) {
            initData(data)
            isScrollSmoothly = true // 平滑滚动
            setLayoutWidth(ViewGroup.LayoutParams.MATCH_PARENT)
            setLayoutHeight(400f.dp2px(context))
            setGroupWidth(80f.dp2px(context))
            groupItemClickListener = BaseQuickLinkageAdapter.OnItemClickListener { item, position ->
                Snackbar.make(view!!, item.itemName!!, Snackbar.LENGTH_SHORT).show()
            }
            childItemClickListener = BaseQuickLinkageAdapter.OnItemClickListener { item, position ->
                Snackbar.make(view!!, item.itemName!!, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}