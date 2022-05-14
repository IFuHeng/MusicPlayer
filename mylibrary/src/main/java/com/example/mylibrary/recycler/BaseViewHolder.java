package com.example.mylibrary.recycler;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    protected OnItemClickListener _onItemClickListener;
    protected OnItemLongClickListener _onItemLongClickListener;
    protected T _data;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    void setData(T t) {
        onBeforeDataChange(t);
        this._data = t;
        onDataChanged(t);
    }

    protected void onBeforeDataChange(T t) {

    }

    protected abstract void onDataChanged(T t);

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this._onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this._onItemClickListener = onItemClickListener;
    }

}
