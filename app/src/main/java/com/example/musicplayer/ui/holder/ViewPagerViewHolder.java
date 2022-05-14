package com.example.musicplayer.ui.holder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager.widget.ViewPager;

import com.example.musicplayer.ui.vhbean.ViewPagerVHBean;
import com.example.mylibrary.recycler.BaseViewHolder;
import com.example.mylibrary.recycler.BaseViewHolderFactory;
import com.example.mylibrary.recycler.CustomPagerAdapter;

import org.jetbrains.annotations.NotNull;

/**
 * used for {@link ViewPager}
 *
 * @author aluca
 * @since 2022年5月14日
 * @deprecated
 */
public class ViewPagerViewHolder extends BaseViewHolder<ViewPagerVHBean> {
    @NonNull
    @NotNull
    private BaseViewHolderFactory factory;

    public ViewPagerViewHolder(@NonNull @NotNull View view, @NonNull @NotNull BaseViewHolderFactory factory) {
        super(view);
        this.factory = factory;
        ViewPager vp = (ViewPager) view;
        vp.setAdapter(new CustomPagerAdapter(factory, _data.getVhBean().getValue(), (view1, position, bean) -> {
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
        ViewPager vp = (ViewPager) itemView;
        int currentPosition = vp.getCurrentItem();
        vp.setAdapter(new CustomPagerAdapter(factory, viewHolderVHBean.getVhBean().getValue(), (view1, position, bean) -> {
            if (_onItemClickListener != null) {
                _onItemClickListener.onItemClick(view1, position, bean);
            }
        }, (view12, position, bean) -> {
            if (_onItemLongClickListener != null) {
                _onItemLongClickListener.onItemLongClick(view12, position, bean);
            }
        }));
        vp.setCurrentItem(Math.min(currentPosition, vp.getAdapter().getCount() - 1));
    }
}
