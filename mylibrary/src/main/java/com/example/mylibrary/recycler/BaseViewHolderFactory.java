package com.example.mylibrary.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

public abstract class BaseViewHolderFactory {
    protected LayoutInflater inflater;

    public BaseViewHolderFactory(Context context) {
        this(LayoutInflater.from(context));
    }

    public BaseViewHolderFactory(LayoutInflater inflater) {
        this.inflater = inflater;
    }

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
}
