# LinkageRecyclerView - 多级联动RecyclerView

[![API](https://img.shields.io/badge/API-19%2B-green.svg?style=flat)](https://android-arsenal.com/api?level=19)
[![demo](https://img.shields.io/badge/download-demo-blue.svg)](/screens/app-demo.apk) <-- 点击下载demo


LinkageRecyclerView中内置多种常见样式的多级联动。可自定义Adapter满足各种自定义设置。

```
    FrameLayout
        └ MultiLevelRecyclerView
            └ LinkageRecyclerView
```

基础自定义设置

 - initData(dataList) 自定义数据集（使用默认适配器）
 - initData(dataList,groupAdapter,childAdapter) 自定义数据集 和 group、child 适配器
 - groupItemClickListener 自定义Group条目点击监听
 - childItemClickListener 自定义Child条目点击监听
 - setLayoutWidth setLayoutHeight 自定义`窗体宽高`
 - setGroupWidth 自定义Group区域宽度
 - setChildWidth 自定义Child区域宽度
 - setChildSpanSize 自定义Child区域一行显示个数
 - isScrollSmoothly 自定义是否平滑滚动


![show_01](/screens/show_01.png)


###  MultiLevelRecyclerView

```java
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

```

###  LinkageRecyclerView

```java                
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
```

