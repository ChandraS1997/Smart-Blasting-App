package com.smart_blasting_drilling.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.activity.BaseActivity;
import com.smart_blasting_drilling.android.app.BaseFragment;
import com.smart_blasting_drilling.android.databinding.FragmentHomeFragmentBinding;
import com.smart_blasting_drilling.android.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment
{
FragmentHomeFragmentBinding binding;
    List<String> projectList = new ArrayList<>();
    com.bdapp.hospital_erp.ui.home.Patient.Adapters.ProjectLIstAdapter projectLIstAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        StatusBarUtils.statusBarColor(requireActivity(), R.color.white);
        ((BaseActivity) mContext).hideKeyboard((BaseActivity) mContext);
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_fragment, container, false);
        }
        projectLIstAdapter = new com.bdapp.hospital_erp.ui.home.Patient.Adapters.ProjectLIstAdapter(mContext, projectList);
        binding.projectListRv.setAdapter(projectLIstAdapter);
        return binding.getRoot();
    }

}
