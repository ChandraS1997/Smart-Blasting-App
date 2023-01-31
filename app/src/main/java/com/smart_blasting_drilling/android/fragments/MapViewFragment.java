package com.smart_blasting_drilling.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.app.BaseFragment;
import com.smart_blasting_drilling.android.databinding.FragmentMapviewBinding;
import com.smart_blasting_drilling.android.helper.Constants;

public class MapViewFragment extends BaseFragment {
    FragmentMapviewBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getView() != null)
                    Navigation.findNavController(getView()).navigate(R.id.HoleDetailsTableViewFragment);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {

            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mapview, container, false);
            binding.linlay.setOnClickListener(view -> {
                if (Constants.onHoleClickListener != null)
                    Constants.onHoleClickListener.onHoleClick("mapviewFragment");
            });

        }

        return binding.getRoot();
    }
}
