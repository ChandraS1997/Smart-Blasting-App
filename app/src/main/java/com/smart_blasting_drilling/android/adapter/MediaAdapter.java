package com.smart_blasting_drilling.android.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.databinding.MediaItemBinding;

import java.io.File;
import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<String> imageList;

    public MediaAdapter(Context context, List<String> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    private RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        MediaItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.media_item, group, false);
        return new MediaHolder(binding);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getViewHolder(LayoutInflater.from(context), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MediaHolder vHolder = (MediaHolder) holder;
        vHolder.setDataBind(imageList.get(position));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }


    public class MediaHolder extends RecyclerView.ViewHolder {
        MediaItemBinding binding;

        public MediaHolder(MediaItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setDataBind(String path) {
           // Glide.with(context).load(new File(path)).placeholder(R.drawable.ic_logo).into(binding.Image);

            String extension = path.substring(path.lastIndexOf("."));
            if(extension.equals(".mp4"))
            {
                binding.VideoView.setVisibility(View.VISIBLE);
                Uri uri = Uri.parse(path);

                // sets the resource from the
                // videoUrl to the videoView
                binding.VideoView.setVideoURI(uri);

                // creating object of
                // media controller class
                MediaController mediaController = new MediaController(context);

                // sets the anchor view
                // anchor view for the videoView
                mediaController.setAnchorView(binding.VideoView);

                // sets the media player to the videoView
                mediaController.setMediaPlayer(binding.VideoView);

                // sets the media controller to the videoView
                binding.VideoView.setMediaController(mediaController);

                // starts the video
                binding.VideoView.start();
            }
            else
            {
                binding.Image.setVisibility(View.VISIBLE);
                Glide.with(context).load(path).apply(new RequestOptions().error(R.drawable.ic_logo)).into(binding.Image);
            }

        }
    }
}
