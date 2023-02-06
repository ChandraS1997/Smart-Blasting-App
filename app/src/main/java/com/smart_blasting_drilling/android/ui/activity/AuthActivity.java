package com.smart_blasting_drilling.android.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.utils.StatusBarUtils;

public class AuthActivity extends BaseActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        StatusBarUtils.statusBarColor(this, R.color._FFA722);
    }
}
