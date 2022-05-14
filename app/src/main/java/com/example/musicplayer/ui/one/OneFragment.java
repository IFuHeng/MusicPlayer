package com.example.musicplayer.ui.one;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.musicplayer.databinding.FragmentOneBinding;
import com.example.musicplayer.ui.holder.GeneralViewHolderFactory;
import com.example.mylibrary.recycler.BaseViewHolderBean;
import com.example.mylibrary.recycler.CustomPagerListAdapter;
import com.example.mylibrary.recycler.OnItemClickListener;
import com.example.mylibrary.recycler.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class OneFragment extends Fragment implements OnItemClickListener, OnItemLongClickListener {

    private FragmentOneBinding binding;
    private OneViewModel notificationsViewModel;
    private CustomPagerListAdapter<DefaultColorVHBean> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(OneViewModel.class);

        binding = FragmentOneBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textOne;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        adapter = new CustomPagerListAdapter<>(new GeneralViewHolderFactory(getContext(), getViewLifecycleOwner()), this, this);
        binding.viewpaget2.setAdapter(adapter);
        binding.viewpaget2.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                page.setAlpha(1f - position);
            }
        });
        binding.textOne.setOnClickListener(v -> {
            adapter.submitList(createColorVHBean(notificationsViewModel.getColors().getValue()));
        });

        notificationsViewModel.getColors().observe(getViewLifecycleOwner(), it -> {
            int cur = binding.viewpaget2.getCurrentItem();
            adapter.submitList(createColorVHBean(it));
            binding.viewpaget2.setCurrentItem(Math.min(adapter.getItemCount() - 1, cur), false);
            Log.d(getClass().getSimpleName(), "====~ " + cur + "   <-----> " + binding.viewpaget2.getCurrentItem());
        });
        return root;
    }

    private List<DefaultColorVHBean> createColorVHBean(List<MutableLiveData<Integer>> value) {
        List<DefaultColorVHBean> result = new ArrayList<>();
        for (MutableLiveData<Integer> integerLiveData : value) {
            result.add(new DefaultColorVHBean(integerLiveData));

        }
        return result;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(View view, int position, BaseViewHolderBean bean) {
        Log.d(getClass().getSimpleName(), "====~on item click " + position + " , bean = " + bean);
        MutableLiveData<Integer> aim = notificationsViewModel.getColors().getValue().get(position);
        int alpha = aim.getValue() & 0xFF000000;
        aim.postValue(~aim.getValue() & 0xFFFFFF | alpha);
    }

    @Override
    public void onItemLongClick(View view, int position, BaseViewHolderBean bean) {
        Log.d(getClass().getSimpleName(), "====~on item long click " + position + " , bean = " + bean);
    }

}