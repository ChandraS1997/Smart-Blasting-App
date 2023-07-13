package com.mineexcellence.sblastingapp.utils;

import android.content.Context;
import android.content.Intent;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

public class GetContentWithMultiFilter extends ActivityResultContracts.GetContent {
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, @NonNull String input) {
        String[] inputString = input.split(";");
        Intent intent = super.createIntent(context, "*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, inputString);
        return intent;
    }
}
