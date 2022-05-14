package com.example.musicplayer.ui.one;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicplayer.databinding.FragmentOneBinding;

public class OneFragment extends Fragment {

    private FragmentOneBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        OneViewModel notificationsViewModel =
                new ViewModelProvider(this).get(OneViewModel.class);

        binding = FragmentOneBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textOne;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}