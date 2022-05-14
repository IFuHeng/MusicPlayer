package com.example.musicplayer.ui.holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager2.widget.ViewPager2;

import com.example.musicplayer.ui.vhbean.ViewPagerVHBean;
import com.example.mylibrary.recycler.BaseViewHolder;
import com.example.mylibrary.recycler.BaseViewHolderBean;
import com.example.mylibrary.recycler.BaseViewHolderFactory;
import com.example.mylibrary.recycler.CustomPagerAdapter;
import com.example.mylibrary.recycler.CustomRecyclerViewAdapter;
import com.example.mylibrary.recycler.OnItemClickListener;
import com.example.mylibrary.recycler.OnItemLongClickListener;

import org.jetbrains.annotations.NotNull;

public class ViewPager2ViewHolder extends BaseViewHolder<ViewPagerVHBean> {
    @NonNull
    @NotNull
    private BaseViewHolderFactory factory;

    public ViewPager2ViewHolder(@NonNull @NotNull View view, @NonNull @NotNull BaseViewHolderFactory factory) {
        super(view);
        this.factory = factory;
        ViewPager2 vp = (ViewPager2) view;
        vp.setAdapter(new CustomRecyclerViewAdapter(factory, _data.getVhBean().getValue(), (view1, position, bean) -> {
            if (_onItemClickListener != null) {
                _onItemClickListener.onItemClick(view1, position, bean);
            }
        }, (view12, position, bean) -> {
            if (_onItemLongClickListener != null) {
                _onItemLongClickListener.onItemLongClick(view12, position, bean);
            }
        }));
    }

    @Override
    public void onDataChanged(ViewPagerVHBean viewHolderVHBean) {
        ViewPager2 vp = (ViewPager2) itemView;
        int currentPosition = vp.getCurrentItem();
        vp.setAdapter(new CustomRecyclerViewAdapter(factory, viewHolderVHBean.getVhBean().getValue() , (view1, position, bean) -> {
            if (_onItemClickListener != null) {
                _onItemClickListener.onItemClick(view1, position, bean);
            }
        }, (view12, position, bean) -> {
            if (_onItemLongClickListener != null) {
                _onItemLongClickListener.onItemLongClick(view12, position, bean);
            }
        }));
        vp.setCurrentItem(Math.min(currentPosition, vp.getAdapter().getItemCount() - 1));
    }
}
