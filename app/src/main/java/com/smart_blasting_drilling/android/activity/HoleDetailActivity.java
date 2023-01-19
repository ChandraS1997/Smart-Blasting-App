package com.smart_blasting_drilling.android.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.databinding.HoleDetailActivityBinding;

public class HoleDetailActivity extends BaseActivity
{
    HoleDetailActivityBinding binding;
    public NavController navController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.hole_detail_activity);
//        navController = Navigation.findNavController(this, R.id.nav_host_hole);

    }
}
