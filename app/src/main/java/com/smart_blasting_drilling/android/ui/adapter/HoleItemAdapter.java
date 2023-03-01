package com.smart_blasting_drilling.android.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.response.ResponseHoleDetailData;
import com.smart_blasting_drilling.android.app.BaseRecyclerAdapter;
import com.smart_blasting_drilling.android.databinding.HoleItemBinding;
import com.smart_blasting_drilling.android.ui.activity.HoleDetailActivity;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.util.List;

public class HoleItemAdapter extends BaseRecyclerAdapter {

    Context context;
    List<ResponseHoleDetailData> holeDetailDataList;
    int spaceVal;

    public HoleItemAdapter(Context context, List<ResponseHoleDetailData> holeDetailDataList, int spaceVal) {
        this.context = context;
        this.holeDetailDataList = holeDetailDataList;
        this.spaceVal = spaceVal;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        HoleItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.hole_item, group, false);
        return new HoleItemViewHolder(binding);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HoleItemViewHolder viewHolder = (HoleItemViewHolder) holder;
        viewHolder.setDataBind(holeDetailDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return holeDetailDataList.size();
    }

    class HoleItemViewHolder extends RecyclerView.ViewHolder {
        HoleItemBinding binding;

        public HoleItemViewHolder(@NonNull HoleItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        void setDataBind(ResponseHoleDetailData detailData) {
            if (spaceVal == 1) {
                binding.startSpaceView.setVisibility(View.GONE);
                binding.endSpaceView.setVisibility(View.VISIBLE);
            } else {
                binding.startSpaceView.setVisibility(View.VISIBLE);
                binding.endSpaceView.setVisibility(View.GONE);
            }

            binding.holeStatusTxt.setVisibility(View.GONE);
            if (!StringUtill.isEmpty(detailData.getHoleStatus())) {
                switch (detailData.getHoleStatus()) {
                    case "Completed":
                        binding.holeStatusTxt.setVisibility(View.VISIBLE);
                        binding.holeStatusTxt.setText(context.getString(R.string.completed));
                        binding.holeIcon.setImageResource(R.drawable.green_circle);
                        break;
                    case "Work in Progress":
                        binding.holeStatusTxt.setVisibility(View.VISIBLE);
                        binding.holeStatusTxt.setText(context.getString(R.string.progress));
                        binding.holeIcon.setImageResource(R.drawable.blue_circle);
                        break;
                    case "Deleted/ Blocked holes/ Do not blast":
                        binding.holeStatusTxt.setVisibility(View.VISIBLE);
                        binding.holeStatusTxt.setText(context.getString(R.string.blocked));
                        binding.holeIcon.setImageResource(R.drawable.red_circle);
                        break;
                    default:
                        binding.holeStatusTxt.setVisibility(View.GONE);
                        binding.holeIcon.setImageResource(R.drawable.circle_hole_pending);
                        break;
                }
            }

            itemView.setOnClickListener(view -> {
                ((HoleDetailActivity) context).openHoleDetailDialog(detailData);
            });

        }

    }

}
