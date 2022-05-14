package com.example.mylibrary.recycler;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;
import java.util.Objects;
import java.util.Stack;

/**
 * adapter for {@link androidx.viewpager.widget.ViewPager}
 *
 * @author aluca
 * @since 2022年5月14日
 */
public class CustomPagerListAdapter<T extends BaseViewHolderBean> extends ListAdapter<T, BaseViewHolder> {

    private BaseViewHolderFactory factory;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public CustomPagerListAdapter(BaseViewHolderFactory factory
            , OnItemClickListener onItemClickListener
            , OnItemLongClickListener onItemLongClickListener) {
        super(new DiffUtil.ItemCallback<T>() {
            @Override
            public boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem) {
                boolean result = oldItem == newItem ? true : oldItem.hashCode() == newItem.hashCode();
                Log.d(getClass().getSimpleName(), "====~1 areItemsTheSame(" + oldItem + "" + newItem + ") =" + result);
                return result;
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem) {
                boolean result = oldItem == newItem ? true : oldItem.hashCode() == newItem.hashCode();
                Log.d(getClass().getSimpleName(), "====~1 areContentsTheSame(" + oldItem + "" + newItem + ") =" + result);
                return result;
            }
        });

        this.factory = factory;
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).type;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(getClass().getSimpleName(), "====~ onCreateViewHolder: viewType = " + viewType);
        BaseViewHolder holder = factory.getBaseViewHolder(viewType);
        holder.setOnItemClickListener(onItemClickListener);
        holder.setOnItemLongClickListener(onItemLongClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        Log.d(getClass().getSimpleName(), "====~ onBindViewHolder: position = " + position);
        holder.setData(getItem(position));
    }
}
