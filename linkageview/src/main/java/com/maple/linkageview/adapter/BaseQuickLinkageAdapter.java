package com.maple.linkageview.adapter;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.maple.linkageview.bean.BaseLinkageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : shaoshuai
 * @date ：2021/1/4
 */
public abstract class BaseQuickLinkageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<BaseLinkageItem> mDataList = new ArrayList<BaseLinkageItem>();

    public void refreshData(List<BaseLinkageItem> newData) {
        mDataList = newData != null ? newData : new ArrayList<BaseLinkageItem>();
        this.notifyDataSetChanged();
    }

    public List<BaseLinkageItem> getData() {
        return mDataList;
    }

    public BaseLinkageItem getItem(int position) {
        return mDataList.get(position);
    }

    public void add(BaseLinkageItem t) {
        if (t == null)
            return;
        mDataList.add(t);
        this.notifyDataSetChanged();
    }

    public void add(List<BaseLinkageItem> data) {
        if (data != null && data.size() > 0) {
            mDataList.addAll(data);
            this.notifyDataSetChanged();
        }
    }

//    public void remove(int index) {
//        if (index < 0 || index >= mDataList.size())
//            return;
//        mDataList.remove(index);
//        this.notifyDataSetChanged();
//    }
//
//    public void remove(T data) {
//        if (data == null)
//            return;
//        mDataList.remove(data);
//        this.notifyDataSetChanged();
//    }
//
//    // 测试此列表是否包含指定的对象。
//    public boolean contains(T t) {
//        return mDataList.contains(t);
//    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    // ------------------------ update selected ---------------------------

    protected int mSelectedPosition = 0;

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    public void updateSelectItem(int index) {
        // 合规化index值，大于0 且 小于 size
        int number = Math.min(Math.max(index, 0), getData().size() - 1);
        mSelectedPosition = number;
        notifyDataSetChanged();
    }

    // ---------------------------- listener -------------------------------

    // 在holder中手动绑定
    protected void bindViewClickListener(RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            if (itemClickListener != null) {
                viewHolder.itemView.setOnClickListener(v ->
                        itemClickListener.onItemClick(getItem(position), position)
                );
            }
            if (itemLongClickListener != null) {
                viewHolder.itemView.setOnLongClickListener(v ->
                        itemLongClickListener.onLongClick(v, position)
                );
            }
        }
    }

    // item click
    private OnItemClickListener itemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    public interface OnItemClickListener {
        // void onItemClick(View v, int position);
        void onItemClick(BaseLinkageItem item, int position);
    }

    // item long click
    private OnItemLongClickListener itemLongClickListener = null;

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        itemLongClickListener = listener;
    }

    public interface OnItemLongClickListener {
        boolean onLongClick(View v, int position);
    }

}
