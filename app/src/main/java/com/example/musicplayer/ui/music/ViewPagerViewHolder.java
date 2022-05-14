package com.example.musicplayer.ui.music;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.example.mylibrary.recycler.BaseViewHolder;
import com.example.mylibrary.recycler.BaseViewHolderFactory;
import com.example.mylibrary.recycler.CustomPagerAdapter;

import org.jetbrains.annotations.NotNull;

public class ViewPagerViewHolder extends BaseViewHolder<ViewPagerVHBean> {
    @NonNull
    @NotNull
    private BaseViewHolderFactory factory;

    public ViewPagerViewHolder(@NonNull @NotNull View view, @NonNull @NotNull BaseViewHolderFactory factory) {
        super(view);
        this.factory = factory;
        ViewPager vp = (ViewPager) view;
        vp.setAdapter(new CustomPagerAdapter(factory, _data.getVhBean().getValue(), onItemClickListener, onItemLongClickListener));
    }

    @Override
    public void onDataChanged(ViewPagerVHBean viewHolderVHBean) {
        ViewPager vp = (ViewPager) itemView;
        int currentPosition = vp.getCurrentItem();
        vp.setAdapter(new CustomPagerAdapter(factory, viewHolderVHBean.getVhBean().getValue(), onItemClickListener, onItemLongClickListener));
        vp.setCurrentItem(Math.min(currentPosition, vp.getAdapter().getCount() - 1));
    }
}
