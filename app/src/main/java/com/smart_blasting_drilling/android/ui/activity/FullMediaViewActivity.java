package com.smart_blasting_drilling.android.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.databinding.ActivityFullMediaViewBinding;
import com.smart_blasting_drilling.android.utils.StatusBarUtils;

public class FullMediaViewActivity extends BaseActivity {

    ActivityFullMediaViewBinding binding;
    String uri = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_full_media_view);

        StatusBarUtils.statusBarColor(this, R.color._FFA722);

        if (isBundleIntentNotEmpty()) {
            uri = getIntent().getExtras().getString("media_uri");
            String extension = uri.substring(uri.lastIndexOf("."));
            if (extension.equalsIgnoreCase(".MP4")) {
                binding.VideoView.setVideoURI(Uri.parse(uri));
                MediaController mediaController = new MediaController(this);
                mediaController.setAnchorView(binding.VideoView);
                mediaController.setMediaPlayer(binding.VideoView);
                binding.VideoView.setMediaController(mediaController);
                binding.VideoView.start();
            } else {
                Glide.with(this).load(uri).into(binding.photoView);
            }
        }

    }
}