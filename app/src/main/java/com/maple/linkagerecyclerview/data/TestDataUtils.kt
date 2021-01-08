package com.maple.linkagerecyclerview.data

import com.maple.linkageview.bean.BaseLinkageItem


object TestDataUtils {

    @JvmStatic
    fun getTestData(): List<BaseLinkageItem> {
        val data = arrayListOf<BaseLinkageItem>(
            BaseLinkageItem("2020"),
            BaseLinkageItem("just1", "2020", "Complex data traversal"),
            BaseLinkageItem("just2", "2020", "Timed task"),
            BaseLinkageItem("just3", "20202", "Complex data traversal2"),
            BaseLinkageItem("just4", "20201", "Timed task"),
            BaseLinkageItem("just5", "2020", "Complex data traversal2"),
            BaseLinkageItem("2021"),
            BaseLinkageItem("just1", "2021", "Complex data traversal"),
            BaseLinkageItem("just2", "2021", "Timed task"),
            BaseLinkageItem("just3", "2021", "Complex data traversal2"),
            BaseLinkageItem("just4", "2021", "Timed task"),
            BaseLinkageItem("just5", "2021", "Complex data traversal2"),
            BaseLinkageItem("2023"),
            BaseLinkageItem("just1", "2023", "Complex data traversal"),
            BaseLinkageItem("just2", "2023", "Timed task"),
            BaseLinkageItem("just3", "20231", "Complex data traversal2"),
            BaseLinkageItem("just4", "20232", "Timed task"),
            BaseLinkageItem("just5", "2023", "Complex data traversal2"),
            BaseLinkageItem("2024"),
            BaseLinkageItem("just1", "2024", "Complex data traversal"),
            BaseLinkageItem("just2", "2024", "Timed task"),
            BaseLinkageItem("just3", "2024", "Complex data traversal2"),
            BaseLinkageItem("just4", "2024", "Timed task"),
            BaseLinkageItem("just5", "2024", "Complex data traversal2"),
            BaseLinkageItem("111"),
            BaseLinkageItem("just1", "111", "Complex data traversal"),
            BaseLinkageItem("222"),
            BaseLinkageItem("just1", "222", "Complex data traversal"),
            BaseLinkageItem("333"),
            BaseLinkageItem("just1", "333", "Complex data traversal"),
            BaseLinkageItem("444"),
            BaseLinkageItem("just1", "444", "Complex data traversal"),
            BaseLinkageItem("555"),
            BaseLinkageItem("just1", "555", "Complex data traversal"),
            BaseLinkageItem("666"),
            BaseLinkageItem("just1", "666", "Complex data traversal"),
            BaseLinkageItem("777"),
            BaseLinkageItem("just1", "777", "Complex data traversal"),
            BaseLinkageItem("888"),
            BaseLinkageItem("just1", "888", "Complex data traversal"),
            BaseLinkageItem("999"),
            BaseLinkageItem("just1", "999", "Complex data traversal"),
            BaseLinkageItem("000"),
            BaseLinkageItem("just1", "000", "Complex data traversal"),
            BaseLinkageItem("Creating"),
            BaseLinkageItem("just1", "Creating", "Complex data traversal"),
            BaseLinkageItem("just2", "Creating", "Timed task"),
            BaseLinkageItem("just3", "Creating", "Complex data traversal2"),
            BaseLinkageItem("just4", "Creating", "Timed task"),
            BaseLinkageItem("just5", "Creating", "Complex data traversal2"),
            BaseLinkageItem("just1", "Creating", "Complex data traversal"),
            BaseLinkageItem("just2", "Creating", "Timed task"),
            BaseLinkageItem("just3", "Creating", "Complex data traversal2"),
            BaseLinkageItem("just4", "Creating", "Timed task"),
            BaseLinkageItem("just4", "Creating", "Timed task"),
            BaseLinkageItem("just4", "Creating", "Timed task"),
            BaseLinkageItem("just4", "Creating", "Timed task"),
            BaseLinkageItem("just4", "Creating", "Timed task"),
            BaseLinkageItem("just4", "Creating", "Timed task"),
            BaseLinkageItem("just4", "Creating", "Timed task"),
            BaseLinkageItem("just4", "Creating", "Timed task"),
            BaseLinkageItem("just4", "Creating", "Timed task"),
            BaseLinkageItem("just5", "Creating", "Complex data traversal2")
        )
        return data
    }


    @JvmStatic
    fun getTestData1(): List<BaseLinkageItem> {
        val data = arrayListOf<BaseLinkageItem>()
        data.add(BaseLinkageItem("2020"))
        for (i in 1..10) {
            data.add(BaseLinkageItem("2020 child item $i", "2020"))
        }
        data.add(BaseLinkageItem("2021"))
        for (i in 1..10) {
            data.add(BaseLinkageItem("2021 child item $i", "2021"))
        }
        data.add(BaseLinkageItem("2022"))
        for (i in 1..10) {
            data.add(BaseLinkageItem("2022 child item $i", "2022"))
        }
        return data
    }

    @JvmStatic
    fun getTestData2(): List<CustomItemInfo> {
        val imgs = arrayOf(
            "https://upload-images.jianshu.io/upload_images/57036-7d50230a36c40a94.png",
            "https://upload-images.jianshu.io/upload_images/57036-2c0645f9b1a588de.png",
            "https://upload-images.jianshu.io/upload_images/57036-4a6889ba228feb46.png"
        )
        val des = arrayOf(
            "好吃的食物，有求必应\n月售5608 好评率98%",
            "好吃的食物，有求必应\n月售1008 好评率100%",
            "好吃的食物，有求必应\n月售10086 好评率82%"
        )
        val data = arrayListOf<CustomItemInfo>(
            CustomItemInfo("优惠"),
            CustomItemInfo("全家桶", "优惠", des.random(), imgs.random()),
            CustomItemInfo("可乐", "优惠", des.random(), imgs.random()),
            CustomItemInfo("小食四拼", "优惠", des.random(), imgs.random()),
            CustomItemInfo("烤全翅", "优惠", des.random(), imgs.random()),
            CustomItemInfo("全家桶", "优惠", des.random(), imgs.random()),
            CustomItemInfo("可乐", "优惠", des.random(), imgs.random()),
            CustomItemInfo("主食"),
            CustomItemInfo("全家桶", "主食", des.random(), imgs.random()),
            CustomItemInfo("可乐", "主食", des.random(), imgs.random()),
            CustomItemInfo("全家桶", "主食", des.random(), imgs.random()),
            CustomItemInfo("全家桶", "主食", des.random(), imgs.random()),
            CustomItemInfo("全家桶", "主食", des.random(), imgs.random()),
            CustomItemInfo("全家桶", "主食", des.random(), imgs.random()),
            CustomItemInfo("小食四拼", "主食", des.random(), imgs.random()),
            CustomItemInfo("全家桶", "主食", des.random(), imgs.random()),
            CustomItemInfo("全家桶", "主食", des.random(), imgs.random()),
            CustomItemInfo("全家桶", "主食", des.random(), imgs.random()),
            CustomItemInfo("2023"),
            CustomItemInfo("全家桶", "主食", des.random(), imgs.random()),
            CustomItemInfo("全家桶", "主食", des.random(), imgs.random()),
            CustomItemInfo("全家桶", "主食", des.random(), imgs.random()),
        )
        return data
    }
}