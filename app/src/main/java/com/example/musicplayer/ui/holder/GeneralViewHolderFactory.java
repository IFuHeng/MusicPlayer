package com.example.musicplayer.ui.holder;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.musicplayer.databinding.ItemMusicBinding;
import com.example.musicplayer.ui.one.DefaultColorViewHolder;
import com.example.mylibrary.recycler.BaseViewHolder;
import com.example.mylibrary.recycler.BaseViewHolderFactory;

public class GeneralViewHolderFactory extends BaseViewHolderFactory {
    public static final int TYPE_MUSIC_ITEM = 0x101;
    public static final int TYPE_MUSIC_BANNER_PAGER = 0x102;
    public static final int TYPE_VIEW_PAGER = 0x104;
    public static final int TYPE_VIEW_PAGER_ITEM = 0x105;
    public static final int TYPE_NEXT_LEVEL_LIST = 0x106;
    public static final int TYPE_VIEW_PAGER2 = 0x107;
    public static final int TYPE_SIMPLE_COLOR = 0x51;

    public GeneralViewHolderFactory(Context context, LifecycleOwner owner) {
        super(context, owner);
    }

    @Override
    public BaseViewHolder _getBaseViewHolder(int type) {
        switch (type) {
            case TYPE_MUSIC_ITEM: {
                @NonNull ItemMusicBinding binding = ItemMusicBinding.inflate(inflater, null, false);
                MusicViewHolder holder = new MusicViewHolder(binding, getLifecycleOwner());
                return holder;
            }
            case TYPE_MUSIC_BANNER_PAGER: {
                @NonNull ItemMusicBinding binding = ItemMusicBinding.inflate(inflater, null, false);
                MusicViewHolder holder = new MusicViewHolder(binding, getLifecycleOwner());
                return holder;
            }
            case TYPE_VIEW_PAGER: {
                ViewPager viewPager = new ViewPager(inflater.getContext());
                ViewPagerViewHolder holder = new ViewPagerViewHolder(viewPager, this);
                return holder;
            }
            case TYPE_VIEW_PAGER2: {
                ViewPager2 viewPager = new ViewPager2(inflater.getContext());
                ViewPagerViewHolder holder = new ViewPagerViewHolder(viewPager, this);
                return holder;
            }
            case TYPE_SIMPLE_COLOR: {
                BaseViewHolder holder = new DefaultColorViewHolder(new TextView(inflater.getContext()), owner);
                return holder;
            }
            case TYPE_NEXT_LEVEL_LIST: {
                RecyclerView recyclerView = new RecyclerView(inflater.getContext());
                recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
                ViewPagerViewHolder holder = new ViewPagerViewHolder(recyclerView, this);
                return holder;
            }
        }
        return null;
    }
}
