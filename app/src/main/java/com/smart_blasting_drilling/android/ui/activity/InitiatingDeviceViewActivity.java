package com.smart_blasting_drilling.android.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.databinding.ActivityInitiatingDeviceViewBinding;
import com.smart_blasting_drilling.android.utils.StatusBarUtils;

public class InitiatingDeviceViewActivity extends BaseActivity {

    ActivityInitiatingDeviceViewBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_initiating_device_view);

        StatusBarUtils.statusBarColor(this, R.color._FFA722);
        binding.headerMedia.mediaTitle.setText(getString(R.string.initiating_device));

        binding.headerMedia.backImg.setOnClickListener(view -> finish());
    }
}
