package com.smart_blasting_drilling.android.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.databinding.ActivityMediaBinding;
import com.smart_blasting_drilling.android.databinding.PerformanceActivityBinding;

public class MediaActivity extends BaseActivity
{
      ActivityMediaBinding binding;
  //  PerformanceActivityBinding binding;
    public NavController navController;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_media);
       // binding = DataBindingUtil.setContentView(this, R.layout.performance_activity);
        navController = Navigation.findNavController(this, R.id.media_nav);
      //  binding.headerPerformance.perfrmanceTitle.setText(getString(R.string.performance));

    }
}
