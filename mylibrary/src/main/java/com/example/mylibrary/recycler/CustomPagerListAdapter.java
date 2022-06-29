package com.example.mylibrary.recycler;

import android.annotation.SuppressLint;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

/**
 * adapter for {@link androidx.viewpager.widget.ViewPager}
 *
 * @author aluca
 * @since 2022年5月14日
 */
public class CustomPagerListAdapter<T extends BaseViewHolderBean> extends ListAdapter<T, BaseViewHolder<T>> {

    private BaseViewHolderFactory factory;
    private OnItemClickListener<T> onItemClickListener;
    private OnItemLongClickListener<T> onItemLongClickListener;

    public CustomPagerListAdapter(BaseViewHolderFactory factory
            , OnItemClickListener<T> onItemClickListener
            , OnItemLongClickListener<T> onItemLongClickListener) {
        super(new DiffUtil.ItemCallback<T>() {
            @Override
            public boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem) {
                return oldItem == newItem || oldItem.equals(newItem);
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem) {
                return oldItem == newItem || oldItem.equals(newItem);
            }
        });

        this.factory = factory;
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public CustomPagerListAdapter(BaseViewHolderFactory factory) {
        this(factory, null, null);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).type;
    }

    @NonNull
    @Override
    public BaseViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHolder holder = factory.getBaseViewHolder(viewType);
        holder.setOnItemClickListener(onItemClickListener);
        holder.setOnItemLongClickListener(onItemLongClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.setData(getItem(position));
    }
}
