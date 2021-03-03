package com.maple.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.maple.demo.databinding.ActivityMainBinding
import com.maple.demo.demo.QuickFragment
import com.maple.demo.demo.CustomFragment
import com.maple.demo.demo.SwitchFragment

/**
 * 示例
 *
 * @author : shaoshuai
 * @date ：2021/1/4
 */
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initView()
    }

    private fun initView() {
        val titles = arrayListOf<String>(
            "Quick",
            "Switch",
            "Custom"
        )
        val fargments = arrayListOf<Fragment>(
            QuickFragment(),
            SwitchFragment(),
            CustomFragment()
        )
        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = fargments.size
            override fun createFragment(position: Int): Fragment = fargments[position]
        }
        TabLayoutMediator(binding.tlTabs, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

}