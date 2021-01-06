package com.maple.linkagerecyclerview.data

import com.maple.linkageview.bean.BaseItem


object TestDataUtils {

    @JvmStatic
    fun getTestData(): List<BaseItem> {
        val data = arrayListOf<BaseItem>(
                BaseItem("2020"),
                BaseItem("just1", "2020", "Complex data traversal"),
                BaseItem("just2", "2020", "Timed task"),
                BaseItem("just3", "2020", "Complex data traversal2"),
                BaseItem("just4", "2020", "Timed task"),
                BaseItem("just5", "2020", "Complex data traversal2"),
                BaseItem("2021"),
                BaseItem("just1", "2021", "Complex data traversal"),
                BaseItem("just2", "2021", "Timed task"),
                BaseItem("just3", "2021", "Complex data traversal2"),
                BaseItem("just4", "2021", "Timed task"),
                BaseItem("just5", "2021", "Complex data traversal2"),
                BaseItem("2023"),
                BaseItem("just1", "2023", "Complex data traversal"),
                BaseItem("just2", "2023", "Timed task"),
                BaseItem("just3", "2023", "Complex data traversal2"),
                BaseItem("just4", "2023", "Timed task"),
                BaseItem("just5", "2023", "Complex data traversal2"),
                BaseItem("2024"),
                BaseItem("just1", "2024", "Complex data traversal"),
                BaseItem("just2", "2024", "Timed task"),
                BaseItem("just3", "2024", "Complex data traversal2"),
                BaseItem("just4", "2024", "Timed task"),
                BaseItem("just5", "2024", "Complex data traversal2"),
                BaseItem("Creating"),
                BaseItem("just1", "Creating", "Complex data traversal"),
                BaseItem("just2", "Creating", "Timed task"),
                BaseItem("just3", "Creating", "Complex data traversal2"),
                BaseItem("just4", "Creating", "Timed task"),
                BaseItem("just5", "Creating", "Complex data traversal2"),
                BaseItem("just1", "Creating", "Complex data traversal"),
                BaseItem("just2", "Creating", "Timed task"),
                BaseItem("just3", "Creating", "Complex data traversal2"),
                BaseItem("just4", "Creating", "Timed task"),
                BaseItem("just4", "Creating", "Timed task"),
                BaseItem("just4", "Creating", "Timed task"),
                BaseItem("just4", "Creating", "Timed task"),
                BaseItem("just4", "Creating", "Timed task"),
                BaseItem("just4", "Creating", "Timed task"),
                BaseItem("just4", "Creating", "Timed task"),
                BaseItem("just4", "Creating", "Timed task"),
                BaseItem("just4", "Creating", "Timed task"),
                BaseItem("just5", "Creating", "Complex data traversal2")
        )
        return data
    }

    @JvmStatic
    fun getTestData2(): List<CustomItemInfo> {
        val data = arrayListOf<CustomItemInfo>(
                CustomItemInfo("优惠"),
                CustomItemInfo("全家桶", "优惠", "好吃的食物，增肥神器，有求必应n月售10008 好评率100%", "https://upload-images.jianshu.io/upload_images/57036-7d50230a36c40a94.png"),
                CustomItemInfo("可乐", "优惠", "好吃的食物，增肥神器，有求必应n月售10008 好评率100%", "https://upload-images.jianshu.io/upload_images/57036-2c0645f9b1a588de.png"),
                CustomItemInfo("小食四拼", "优惠", "好吃的食物，增肥神器，有求必应n月售10008 好评率100%", "https://upload-images.jianshu.io/upload_images/57036-4a6889ba228feb46.png"),
                CustomItemInfo("烤全翅", "优惠", "好吃的食物，增肥神器，有求必应n月售10008 好评率100%", "https://upload-images.jianshu.io/upload_images/57036-636381a9f7a855d6.png"),
                CustomItemInfo("全家桶", "优惠", "好吃的食物，增肥神器，有求必应n月售10008 好评率100%", "https://upload-images.jianshu.io/upload_images/57036-7d50230a36c40a94.png"),
                CustomItemInfo("可乐", "优惠", "好吃的食物，增肥神器，有求必应n月售10008 好评率100%", "https://upload-images.jianshu.io/upload_images/57036-2c0645f9b1a588de.png"),
                CustomItemInfo("主食"),
                CustomItemInfo("全家桶", "主食", "好吃的食物，增肥神器，有求必应n月售10008 好评率100%", "https://upload-images.jianshu.io/upload_images/57036-7d50230a36c40a94.png"),
                CustomItemInfo("可乐", "主食", "好吃的食物，增肥神器，有求必应n月售10008 好评率100%", "https://upload-images.jianshu.io/upload_images/57036-2c0645f9b1a588de.png"),
                CustomItemInfo("全家桶", "主食", "好吃的食物，增肥神器，有求必应n月售10008 好评率100%", "https://upload-images.jianshu.io/upload_images/57036-7d50230a36c40a94.png"),
                CustomItemInfo("可乐", "主食", "好吃的食物，增肥神器，有求必应n月售10008 好评率100%", "https://upload-images.jianshu.io/upload_images/57036-2c0645f9b1a588de.png"),
                CustomItemInfo("2023")
        )
        return data
    }
}