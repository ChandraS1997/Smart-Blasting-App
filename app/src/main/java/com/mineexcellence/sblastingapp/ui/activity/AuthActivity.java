package com.mineexcellence.sblastingapp.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.mineexcellence.sblastingapp.R;
import com.mineexcellence.sblastingapp.utils.StatusBarUtils;

public class AuthActivity extends BaseActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        StatusBarUtils.statusBarColor(this, R.color._FFA722);
    }
}
