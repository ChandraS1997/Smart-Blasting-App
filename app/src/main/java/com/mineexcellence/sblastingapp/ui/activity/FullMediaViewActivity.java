package com.mineexcellence.sblastingapp.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.mineexcellence.sblastingapp.R;
import com.mineexcellence.sblastingapp.databinding.ActivityFullMediaViewBinding;
import com.mineexcellence.sblastingapp.utils.StatusBarUtils;

public class FullMediaViewActivity extends BaseActivity {

    ActivityFullMediaViewBinding binding;
    String uri = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_full_media_view);

        StatusBarUtils.statusBarColor(this, R.color._FFA722);

        binding.headerMedia.backImg.setOnClickListener(view -> finish());

        if (isBundleIntentNotEmpty()) {
            uri = getIntent().getExtras().getString("media_uri");
            String extension = uri.substring(uri.lastIndexOf("."));
            if (extension.equalsIgnoreCase(".MP4")) {
                binding.VideoView.setVisibility(View.VISIBLE);
                binding.VideoView.setVideoURI(Uri.parse(uri));
                MediaController mediaController = new MediaController(this);
                mediaController.setAnchorView(binding.VideoView);
                mediaController.setMediaPlayer(binding.VideoView);
                binding.VideoView.setMediaController(mediaController);
                binding.VideoView.start();
            } else {
                binding.photoView.setVisibility(View.VISIBLE);
                Glide.with(this).load(uri).into(binding.photoView);
            }
        }

    }
}
