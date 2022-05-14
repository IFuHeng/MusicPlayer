package com.example.mylibrary.recycler;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;
import java.util.Stack;

public class CustomPagerAdapter extends PagerAdapter {

    private BaseViewHolderFactory factory;
    private List<? extends BaseViewHolderBean> data;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private Stack<BaseViewHolder> stack = new Stack<>();

    public CustomPagerAdapter(BaseViewHolderFactory factory
            , List<? extends BaseViewHolderBean> data
            , OnItemClickListener onItemClickListener
            , OnItemLongClickListener onItemLongClickListener) {
        this.factory = factory;
        this.data = data;
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return ((BaseViewHolder) object).itemView == view;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (stack.empty()) {
            return stack.pop();
        }
        BaseViewHolder holder = factory.getBaseViewHolder(data.get(position).type);
        holder.setData(data.get(position));
        container.addView(holder.itemView);
        return holder;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        stack.push((BaseViewHolder) object);
    }
}
