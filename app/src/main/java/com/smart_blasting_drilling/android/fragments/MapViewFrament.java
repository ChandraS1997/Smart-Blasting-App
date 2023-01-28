package com.smart_blasting_drilling.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.adapter.TableViewAdapter;
import com.smart_blasting_drilling.android.databinding.FragmentMapviewBinding;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.interfaces.onHoleClickListener;

public class MapViewFrament extends Fragment {
    FragmentMapviewBinding binding;

    // private com.smart_blasting_drilling.android.interfaces.onHoleClickListener onHoleClickListener;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getView() != null)
                    // Navigation.findNavController(getView()).navigateUp();
                    Navigation.findNavController(getView()).navigate(R.id.HoleDetailsTableViewFragment);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {

            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mapview, container, false);
            binding.linlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Constants.onHoleClickListener.onHoleClicik("mapviewFragment");
                }
            });

        }

        return binding.getRoot();
    }
}
