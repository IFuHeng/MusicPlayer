package com.example.mylibrary.recycler;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

public abstract class BaseBindingViewHolder<T, B extends ViewBinding> extends BaseViewHolder<T> {
    protected B binding;

    public BaseBindingViewHolder(@NonNull B binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    void setData(T t) {
        onBeforeDataChange(t);
        onBeforeDataChange(t, binding);
        this._data = t;
        onDataChanged(t);
        onDataChanged(t, binding);
    }

    @Override
    @Deprecated
    public void onDataChanged(T t) {

    }

    @Deprecated
    @Override
    public void onBeforeDataChange(T t) {

    }

    protected abstract void onBeforeDataChange(T t, B binding);

    protected abstract void onDataChanged(T t, B binding);

}
