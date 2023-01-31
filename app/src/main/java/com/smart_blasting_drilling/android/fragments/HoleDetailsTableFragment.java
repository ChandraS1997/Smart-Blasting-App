package com.smart_blasting_drilling.android.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.activity.HoleDetailActivity;
import com.smart_blasting_drilling.android.adapter.TableViewAdapter;
import com.smart_blasting_drilling.android.app.BaseFragment;
import com.smart_blasting_drilling.android.databinding.FragmentHoleDetailsTableBinding;

import java.util.ArrayList;
import java.util.List;

public class HoleDetailsTableFragment extends BaseFragment {

    FragmentHoleDetailsTableBinding binding;
    List<String> tabList = new ArrayList<>();
    TableViewAdapter tableViewAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getView() != null)
                    ((HoleDetailActivity) mContext).finish();
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hole_details_table, container, false);

            tableViewAdapter = new TableViewAdapter(mContext, tabList);
            binding.tableRv.setAdapter(tableViewAdapter);
        }
        return binding.getRoot();
    }
}