package com.example.musicplayer.ui.two;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicplayer.databinding.FragmentTwoBinding;

public class TwoFragment extends Fragment {

    private FragmentTwoBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TwoViewModel notificationsViewModel =
                new ViewModelProvider(this).get(TwoViewModel.class);

        binding = FragmentTwoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textTwo;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}