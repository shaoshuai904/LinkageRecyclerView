package com.maple.linkagerecyclerview.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.maple.linkagerecyclerview.R
import com.maple.linkagerecyclerview.data.TestDataUtils
import com.maple.linkagerecyclerview.databinding.FragmentCustomBinding
import com.maple.linkageview.adapter.BaseQuickLinkageAdapter
import com.maple.linkageview.adapter.DefaultGroupAdapter
import com.maple.linkageview.bean.BaseItem

/**
 * 自定义样式
 *
 * @author : shaoshuai
 * @date ：2021/1/6
 */
class CustomFragment : Fragment() {
    private lateinit var binding: FragmentCustomBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_custom, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    var isGridMode: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLinkageData()

        binding.btSwitch.setOnClickListener {
            val span = if (isGridMode) 1 else 3
            binding.linkage.setSpanCount(span)
            isGridMode = !isGridMode
        }
    }

    private fun initLinkageData() {
        val data = TestDataUtils.getTestData2()
        with(binding.linkage) {
            init(data, DefaultGroupAdapter(context), CustomChildAdapter(context))
            isScrollSmoothly = false
            groupItemClickListener = BaseQuickLinkageAdapter.OnItemClickListener<BaseItem> { item, position ->
                Snackbar.make(view!!, item.itemName!!, Snackbar.LENGTH_SHORT).show()
            }
            childItemClickListener = BaseQuickLinkageAdapter.OnItemClickListener<BaseItem> { item, position ->
                Snackbar.make(view!!, item.itemName!!, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}