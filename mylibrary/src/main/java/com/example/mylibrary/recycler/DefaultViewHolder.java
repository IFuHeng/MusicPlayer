package com.example.mylibrary.recycler;

import android.view.View;

import androidx.annotation.NonNull;

public class DefaultViewHolder extends BaseViewHolder {
    protected View rootView;

    public DefaultViewHolder(@NonNull View itemView) {
        super(itemView);
        this.rootView = itemView;
    }

    @Override
    public void onDataChanged(BaseViewHolderBean baseViewHolderBean) {

    }
}
