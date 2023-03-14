package com.smart_blasting_drilling.android.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.databinding.MediaItemBinding;
import com.smart_blasting_drilling.android.ui.activity.FullMediaViewActivity;
import com.smart_blasting_drilling.android.ui.models.MediaDataModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<MediaDataModel> imageList;
    MediaDeleteListener mediaDeleteListener;
    boolean isMultipleSelectionEnable = false;
    List<MediaDataModel> selectedImageList;

    public MediaAdapter(Context context, List<MediaDataModel> mediaDataModelList) {
        this.context = context;
        this.imageList = mediaDataModelList;
        this.selectedImageList = new ArrayList<>();
    }

    public void setUpListener(MediaDeleteListener mediaDeleteListener) {
        this.mediaDeleteListener = mediaDeleteListener;
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

    public interface MediaDeleteListener {
        void selectMediaCallBack();
        void clearMediaCallBack();
    }

    public List<MediaDataModel> getImageList() {
        return imageList;
    }

    public class MediaHolder extends RecyclerView.ViewHolder {
        MediaItemBinding binding;

        public MediaHolder(MediaItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setDataBind(MediaDataModel mediaDataModel) {
            Glide.with(context).load(new File(mediaDataModel.getFilePath())).placeholder(R.drawable.ic_logo).into(binding.Image);

            String extension = mediaDataModel.getFilePath().substring(mediaDataModel.getFilePath().lastIndexOf("."));
            if (extension.equals(".mp4")) {
                binding.VideoView.setVisibility(View.VISIBLE);
                binding.Image.setVisibility(View.GONE);
                Uri uri = Uri.parse(mediaDataModel.getFilePath());
                binding.VideoView.setBackgroundColor(context.getColor(R.color.black));
                binding.VideoView.setImageResource(R.drawable.play);
            } else {
                binding.Image.setVisibility(View.VISIBLE);
                binding.VideoView.setVisibility(View.GONE);
                Glide.with(context).load(mediaDataModel.getFilePath()).apply(new RequestOptions().error(R.drawable.ic_logo)).into(binding.Image);
            }

            if (isMultipleSelectionEnable) {
                if (!mediaDataModel.isSelection()) {
                    binding.selectedImageIcn.setVisibility(View.GONE);
                } else {
                    binding.selectedImageIcn.setVisibility(View.VISIBLE);
                }
            }

            itemView.setOnClickListener(view -> {
                if (isMultipleSelectionEnable) {
                    if (mediaDataModel.isSelection()) {
                        selectedImageList.remove(mediaDataModel);
                    } else {
                        selectedImageList.add(mediaDataModel);
                    }
                    mediaDataModel.setSelection(!mediaDataModel.isSelection());
                    if (selectedImageList.size() == 0) {
                        isMultipleSelectionEnable = false;
                        if (mediaDeleteListener != null)
                            mediaDeleteListener.clearMediaCallBack();
                    }
                    notifyDataSetChanged();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("media_uri", mediaDataModel.getFilePath());
                    Intent intent = new Intent(context, FullMediaViewActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (!isMultipleSelectionEnable) {
                        isMultipleSelectionEnable = true;
                        selectedImageList.add(mediaDataModel);
                        mediaDataModel.setSelection(true);
                        if (mediaDeleteListener != null)
                            mediaDeleteListener.selectMediaCallBack();
                        notifyDataSetChanged();
                    }
                    return false;
                }
            });

        }
    }
}
