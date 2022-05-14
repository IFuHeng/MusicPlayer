package com.example.mylibrary.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

public abstract class BaseViewHolderFactory {
    @NonNull
    protected LayoutInflater inflater;
    @NonNull
    protected LifecycleOwner owner;

    public BaseViewHolderFactory(@NonNull Context context, @NonNull LifecycleOwner owner) {
        this(LayoutInflater.from(context), owner);
    }

    public BaseViewHolderFactory(@NonNull LayoutInflater inflater, @NonNull LifecycleOwner owner) {
        this.inflater = inflater;
        this.owner = owner;
    }

    @NonNull
    public BaseViewHolder getBaseViewHolder(int type) {
        BaseViewHolder result = _getBaseViewHolder(type);
        if (result != null) {
            return result;
        }
        TextView textView = (TextView) inflater.inflate(android.R.layout.test_list_item, null, false);
        textView.setText(android.R.string.no);
        return new DefaultViewHolder(textView);
    }

    public abstract BaseViewHolder _getBaseViewHolder(int type);

    @NonNull
    public LifecycleOwner getLifecycleOwner() {
        return owner;
    }
}
