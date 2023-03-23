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
import com.smart_blasting_drilling.android.api.apis.response.ResponseHoleDetailData;
import com.smart_blasting_drilling.android.app.BaseRecyclerAdapter;
import com.smart_blasting_drilling.android.databinding.HoleItemBinding;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.interfaces.HoleBgListener;
import com.smart_blasting_drilling.android.ui.activity.BaseActivity;
import com.smart_blasting_drilling.android.ui.activity.HoleDetail3DModelActivity;
import com.smart_blasting_drilling.android.ui.activity.HoleDetailActivity;
import com.smart_blasting_drilling.android.utils.AppUtill;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.util.List;

public class HoleItemAdapter extends BaseRecyclerAdapter {

    Context context;
    List<ResponseHoleDetailData> holeDetailDataList;
    int spaceVal;
    String patternType = "Rectangular/Square";
    int selectedPos = -1;
    int rowPos = -1;

    public HoleItemAdapter(Context context, List<ResponseHoleDetailData> holeDetailDataList, int spaceVal, String patternType, int rowPos) {
        this.context = context;
        this.holeDetailDataList = holeDetailDataList;
        this.spaceVal = spaceVal;
        this.patternType = patternType;
        this.rowPos = rowPos;
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

    class HoleItemViewHolder extends RecyclerView.ViewHolder implements HoleBgListener {
        HoleItemBinding binding;

        public HoleItemViewHolder(@NonNull HoleItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            Constants.holeBgListener = this;
        }

        void setDataBind(ResponseHoleDetailData detailData) {
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
                binding.mainContainerView.setElevation(10f);
            } else {
                binding.mainContainerView.setBackgroundResource(0);
                binding.mainContainerView.setElevation(0f);
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
                if (((HoleDetailActivity) context).holeDetailCallBackListener != null)
                    ((HoleDetailActivity) context).holeDetailCallBackListener.setBgOfHoleOnNewRowChange(detailData.getRowNo(), detailData.getHoleNo(), getBindingAdapterPosition());
                selectedPos = getBindingAdapterPosition();
                ((HoleDetailActivity) context).updateValPos = getBindingAdapterPosition();
                ((HoleDetailActivity) context).updateRowNo = detailData.getRowNo();
                ((HoleDetailActivity) context).updateHoleNo = detailData.getHoleNo();
                ((HoleDetailActivity) context).rowPos = rowPos;
                if (((HoleDetailActivity) context).holeDetailCallBackListener != null)
                    ((HoleDetailActivity) context).holeDetailCallBackListener.setHoleDetailCallBack(((HoleDetailActivity) context).bladesRetrieveData,detailData);
                notifyDataSetChanged();
            });

        }

        @Override
        public void setBackgroundRefresh() {
            selectedPos = -1;
            notifyDataSetChanged();
        }
    }

}
