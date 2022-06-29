package com.example.musicplayer.ui.one;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;

import com.example.mylibrary.recycler.BaseViewHolder;

import java.lang.ref.WeakReference;

public class DefaultColorViewHolder extends BaseViewHolder<DefaultColorVHBean> implements Observer<Integer> {

    private LifecycleOwner lifecycleOwner;

    public DefaultColorViewHolder(@NonNull View itemView, LifecycleOwner owner) {
        super(itemView);
        itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        itemView.setOnClickListener(v -> _onItemClickListener.onItemClick(itemView, getAdapterPosition(), _data));
        itemView.setOnLongClickListener(v -> {
            _onItemLongClickListener.onItemLongClick(itemView, getAdapterPosition(), _data);
            return false;
        });
        this.lifecycleOwner = owner;
    }

    @Override
    protected void onBeforeDataChange(DefaultColorVHBean bean) {
        bean.color.removeObserver(this);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onDataChanged(DefaultColorVHBean bean) {
        bean.color.observe(lifecycleOwner, this::onChanged);
        refresh(bean.color.getValue());
    }

    public void refresh(Integer d) {
        itemView.setBackground(new ColorDrawable(d));
        if (itemView instanceof TextView) {
            TextView textView = (TextView) itemView;
            textView.setTextColor(~d | 0xFF000000);
            textView.setText(String.format("%d --  %X", getAdapterPosition(), d));
        }
    }

    public void onChanged(Integer d) {
        refresh(d);
    }

    public static class DefaultObserver implements Observer<Integer> {

        private WeakReference<DefaultColorViewHolder> weakReference;

        public DefaultObserver(DefaultColorViewHolder t) {
            this.weakReference = new WeakReference(t);
        }

        @Override
        public void onChanged(Integer d) {
            if (weakReference.get() == null) {
                Log.d(getClass().getSimpleName(), "====~  weakReference.get() is empty");
                return;
            }
            weakReference.get().refresh(d);
        }
    }
}
