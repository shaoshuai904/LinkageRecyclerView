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
import com.maple.linkagerecyclerview.databinding.FragmentFirstBinding
import com.maple.linkageview.LinkageRecyclerView
import com.maple.linkageview.adapter.BaseQuickAdapter
import com.maple.linkageview.bean.BaseItem


class FirstFragment : Fragment() {
    private lateinit var binding: FragmentFirstBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_first, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLinkageData(binding.linkage)
    }

    private fun initLinkageData(linkage: LinkageRecyclerView) {
        val data = TestDataUtils.getTestData()
        with(linkage) {
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