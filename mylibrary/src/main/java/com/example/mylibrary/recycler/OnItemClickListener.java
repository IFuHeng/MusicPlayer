package com.example.mylibrary.recycler;

import android.view.View;

public interface OnItemClickListener<T extends BaseViewHolderBean> {
    void onItemClick(View view, int position, T bean);
}
