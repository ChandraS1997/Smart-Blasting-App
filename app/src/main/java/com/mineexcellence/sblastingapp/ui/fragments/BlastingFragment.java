package com.mineexcellence.sblastingapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.mineexcellence.sblastingapp.R;
import com.mineexcellence.sblastingapp.ui.activity.BaseActivity;
import com.mineexcellence.sblastingapp.ui.adapter.ProjectLIstAdapter;
import com.mineexcellence.sblastingapp.app.BaseFragment;
import com.mineexcellence.sblastingapp.databinding.FragmentDrillingFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class BlastingFragment extends BaseFragment {

    FragmentDrillingFragmentBinding binding;
    List<String> projectList = new ArrayList<>();
    ProjectLIstAdapter projectLIstAdapter;

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
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_drilling_fragment, container, false);

            ((BaseActivity) mContext).hideKeyboard((BaseActivity) mContext);

//            projectLIstAdapter = new ProjectLIstAdapter(mContext, projectList);
//            binding.projectListRv.setAdapter(projectLIstAdapter);

        }
        return binding.getRoot();
    }

}
