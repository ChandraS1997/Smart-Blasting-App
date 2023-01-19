package com.smart_blasting_drilling.android.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.databinding.FragmentHoleDetailsTableBinding;

public class FragmentHoleDetailsTableView extends Fragment {
    FragmentHoleDetailsTableBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hole_details_table, container, false);

        }
        return binding.getRoot();
    }
}