package com.maple.linkagerecyclerview.data

import com.maple.linkageview.bean.BaseItem


class CustomItemInfo(
    itemName: String?,
    parentName: String?,
    isGroup: Boolean = false// 是否是头
) : BaseItem(itemName, parentName, isGroup) {
    //    var  content: String? = "好吃的食物，增肥神器，有求必应n月售10008 好评率100%"
    var imgUrl: String? = "https://upload-images.jianshu.io/upload_images/57036-203db4255e78922a.png"

    constructor(title: String?) : this(title, null, true)
    constructor(title: String?, group: String?, content: String?, imgUrl: String?) : this(title, group, false) {
        this.content = content
        this.imgUrl = imgUrl
    }
}