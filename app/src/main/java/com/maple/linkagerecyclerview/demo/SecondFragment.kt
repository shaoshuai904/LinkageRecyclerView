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
import com.maple.linkagerecyclerview.databinding.FragmentSecondBinding
import com.maple.linkageview.adapter.BaseQuickAdapter
import com.maple.linkageview.bean.BaseItem


class SecondFragment : Fragment() {
    private lateinit var binding: FragmentSecondBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_second, container, false)
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
        val data = TestDataUtils.getTestData()
        with(binding.linkage) {
            init(data)
            isScrollSmoothly = false
            groupItemClickListener = BaseQuickAdapter.OnItemClickListener<BaseItem> { item, position ->
                Snackbar.make(view!!, item.itemName!!, Snackbar.LENGTH_SHORT).show()
            }
            childItemClickListener = BaseQuickAdapter.OnItemClickListener<BaseItem> { item, position ->
                Snackbar.make(view!!, item.itemName!!, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}