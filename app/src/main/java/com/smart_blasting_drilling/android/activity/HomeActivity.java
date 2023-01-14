package com.smart_blasting_drilling.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.databinding.ActivityHomeBinding;
import com.smart_blasting_drilling.android.utils.StatusBarUtils;
import com.smart_blasting_drilling.android.utils.TextUtil;

public class HomeActivity extends BaseActivity
{

    public ActivityHomeBinding binding;
    public static void openHomeActivity(Context context)
    {
        context.startActivity(new Intent(context, HomeActivity.class));
        ((Activity) context).finishAffinity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        StatusBarUtils.statusBarColor(this, R.color.white);
        setPageTitle("Dashboard");
    }
    public void setPageTitle(String title) {
        if (!TextUtil.isEmpty(title)) {
            binding.appLayout.headerLayout.pageTitle.setText(TextUtil.getString(title));
        }
    }

}
