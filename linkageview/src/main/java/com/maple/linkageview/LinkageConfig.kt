package com.maple.linkageview

import android.content.Context

/**
 *
 * @author : shaoshuai
 * @date ：2021/1/4
 */
open class LinkageConfig(
        var context: Context
) {
    var spanCount: Int = 3
    var groupWidthScale: Double = 0.25

    var childItemLayoutId = R.layout.ms_item_child
}