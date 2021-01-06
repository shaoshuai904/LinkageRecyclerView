package com.maple.linkageview.bean

import java.io.Serializable

/**
 *
 * @author : shaoshuai
 * @date ：2021/1/4
 */

open class BaseItem(
    var itemName: String?,
    var parentName: String?,
    var isGroup: Boolean = false// 是否是头
) : Serializable {
    var content: String? = null

    constructor(itemName: String?) : this(itemName, null, true)
    constructor(itemName: String?, parentName: String?, content: String?) : this(itemName, parentName, false) {
        this.content = content
    }

    fun getShowGroupName() = if (isGroup) itemName else parentName

}