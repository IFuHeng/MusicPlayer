package com.example.mylibrary.recycler;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder<T extends BaseViewHolderBean> extends RecyclerView.ViewHolder {
    protected OnItemClickListener onItemClickListener;
    protected OnItemLongClickListener onItemLongClickListener;
    protected T _data;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    void setData(T t) {
        this._data = t;
        onDataChanged(t);
    }

    public abstract void onDataChanged(T t);

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
