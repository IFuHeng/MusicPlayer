package com.example.mylibrary.recycler;

import android.view.View;

public interface OnItemLongClickListener<T extends BaseViewHolderBean> {
    void onItemLongClick(View view, int position, T bean);
}
