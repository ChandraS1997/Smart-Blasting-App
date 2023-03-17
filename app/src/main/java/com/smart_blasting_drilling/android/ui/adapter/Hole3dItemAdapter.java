package com.smart_blasting_drilling.android.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable4HoleChargingDataModel;
import com.smart_blasting_drilling.android.app.BaseRecyclerAdapter;
import com.smart_blasting_drilling.android.databinding.HoleItemBinding;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.interfaces.HoleBgListener;
import com.smart_blasting_drilling.android.ui.activity.HoleDetail3DModelActivity;
import com.smart_blasting_drilling.android.ui.activity.HoleDetailActivity;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.util.List;

public class Hole3dItemAdapter extends BaseRecyclerAdapter {

    Context context;
    List<Response3DTable4HoleChargingDataModel> holeDetailDataList;
    int spaceVal;
    String patternType = "Rectangular/Square";
    int selectedPos = -1;

    public Hole3dItemAdapter(Context context, List<Response3DTable4HoleChargingDataModel> holeDetailDataList, int spaceVal, String patternType) {
        this.context = context;
        this.holeDetailDataList = holeDetailDataList;
        this.spaceVal = spaceVal;
        this.patternType = patternType;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        HoleItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.hole_item, group, false);
        return new Hole3dItemAdapter.HoleItemViewHolder(binding);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Hole3dItemAdapter.HoleItemViewHolder viewHolder = (Hole3dItemAdapter.HoleItemViewHolder) holder;
        viewHolder.setDataBind(holeDetailDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return holeDetailDataList.size();
    }

    class HoleItemViewHolder extends RecyclerView.ViewHolder implements HoleBgListener {
        HoleItemBinding binding;

        public HoleItemViewHolder(@NonNull HoleItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            Constants.holeBgListener = this;
        }

        @Override
        public void setBackgroundRefresh() {
            selectedPos = -1;
            notifyDataSetChanged();
        }

        void setDataBind(Response3DTable4HoleChargingDataModel detailData) {
            if (patternType.equals("Staggered")) {
                if (spaceVal == 1) {
                    binding.holeStatusTxt.setGravity(Gravity.START);
                    binding.startSpaceView.setVisibility(View.GONE);
                    binding.endSpaceView.setVisibility(View.VISIBLE);
                } else {
                    binding.holeStatusTxt.setGravity(Gravity.END);
                    binding.startSpaceView.setVisibility(View.VISIBLE);
                    binding.endSpaceView.setVisibility(View.GONE);
                }
            } else if (patternType.equals("Rectangular/Square"))  {
                binding.holeStatusTxt.setGravity(Gravity.START);
                binding.startSpaceView.setVisibility(View.GONE);
                binding.endSpaceView.setVisibility(View.VISIBLE);
            }

            if (selectedPos == getBindingAdapterPosition()) {
                binding.mainContainerView.setBackgroundResource(R.drawable.bg_light_gray_drawable);
            } else {
                binding.mainContainerView.setBackgroundResource(0);
            }

            binding.holeStatusTxt.setVisibility(View.VISIBLE);
            if (!StringUtill.isEmpty(detailData.getHoleStatus())) {
                switch (detailData.getHoleStatus()) {
                    case "Completed":
                        binding.holeIcon.setImageResource(R.drawable.green_circle);
                        break;
                    case "Work in Progress":
                        binding.holeIcon.setImageResource(R.drawable.blue_circle);
                        break;
                    case "Deleted/ Blocked holes/ Do not blast":
                        binding.holeIcon.setImageResource(R.drawable.red_circle);
                        break;
                    default:
                        binding.holeIcon.setImageResource(R.drawable.circle_hole_pending);
                        break;
                }
            }

            binding.holeStatusTxt.setText(StringUtill.getString(String.format("R%sH%s", detailData.getRowNo(), detailData.getHoleNo())));

            itemView.setOnClickListener(view -> {
                selectedPos = getBindingAdapterPosition();
                if (((HoleDetail3DModelActivity) context).holeDetailCallBackListener != null)
                    ((HoleDetail3DModelActivity) context).holeDetailCallBackListener.setHoleDetailCallBack(detailData);
                notifyDataSetChanged();
            });

        }

    }

}