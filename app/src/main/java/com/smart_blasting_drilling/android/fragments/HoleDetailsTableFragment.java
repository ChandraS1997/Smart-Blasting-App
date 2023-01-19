package com.smart_blasting_drilling.android.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.activity.HomeActivity;
import com.smart_blasting_drilling.android.adapter.ProjectLIstAdapter;
import com.smart_blasting_drilling.android.adapter.TableViewAdapter;
import com.smart_blasting_drilling.android.databinding.FragmentHoleDetailsTableBinding;

import java.util.ArrayList;
import java.util.List;

public class HoleDetailsTableFragment extends Fragment {
    public Context mContext;
    FragmentHoleDetailsTableBinding binding;
    List<String> tabList = new ArrayList<>();
    TableViewAdapter tableViewAdapter;
   // public Context mContext;
   Intent intent;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getView() != null)
                    requireActivity().finish();
                  //  Navigation.findNavController(binding.getRoot()).navigateUp();
                    // intent = new Intent(getActivity(), HomeActivity.class);
                  //startActivity(intent);
               // ((HomeActivity) mContext).navController.navigateUp();
                    //Navigation.findNavController(getView()).navigateUp();
               // ((HomeActivity) getContext()).navController.navigateUp();

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hole_details_table, container, false);

        }
        tableViewAdapter = new TableViewAdapter(getContext(), tabList);
        binding.tableRv.setAdapter(tableViewAdapter);
        return binding.getRoot();
    }
}