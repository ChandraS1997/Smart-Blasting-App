package com.smart_blasting_drilling.android.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.smart_blasting_drilling.android.ui.activity.FullMediaViewActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<String> imageList;
    MediaDeleteListener mediaDeleteListener;
    boolean isMultipleSelectionEnable = false;
    List<String> selectedImageList;
    List<Integer> selectedImagePositionList;

    public MediaAdapter(Context context, List<String> imageList) {
        this.context = context;
        this.imageList = imageList;
        this.selectedImageList = new ArrayList<>();
        this.selectedImagePositionList = new ArrayList<>();
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
        void deleteMedia();
        void deleteMultipleMedia();
    }

    public class MediaHolder extends RecyclerView.ViewHolder {
        MediaItemBinding binding;

        public MediaHolder(MediaItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setDataBind(String path) {
            Glide.with(context).load(new File(path)).placeholder(R.drawable.ic_logo).into(binding.Image);

            String extension = path.substring(path.lastIndexOf("."));
            if (extension.equals(".mp4")) {
                binding.VideoView.setVisibility(View.VISIBLE);
                binding.Image.setVisibility(View.GONE);
                Uri uri = Uri.parse(path);
                binding.VideoView.setBackgroundColor(context.getColor(R.color.black));
                binding.VideoView.setImageResource(R.drawable.play);
            } else {
                binding.Image.setVisibility(View.VISIBLE);
                binding.VideoView.setVisibility(View.GONE);
                Glide.with(context).load(path).apply(new RequestOptions().error(R.drawable.ic_logo)).into(binding.Image);
            }

            itemView.setOnClickListener(view -> {
                if (isMultipleSelectionEnable) {
                    if (binding.selectedImageIcn.getVisibility() == View.VISIBLE) {
                        binding.selectedImageIcn.setVisibility(View.GONE);
                        selectedImageList.remove(path);
                    } else {
                        binding.selectedImageIcn.setVisibility(View.VISIBLE);
                        selectedImageList.add(path);
                    }
                    if (selectedImageList.size() == 0) {
                        isMultipleSelectionEnable = false;
                    }
                    notifyItemChanged(getBindingAdapterPosition());
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("media_uri", path);
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
                        binding.selectedImageIcn.setVisibility(View.VISIBLE);
                        selectedImageList.add(path);
                        selectedImagePositionList.add(getBindingAdapterPosition());
                        notifyItemChanged(getBindingAdapterPosition());
                    }
                    return true;
                }
            });

        }
    }
}
