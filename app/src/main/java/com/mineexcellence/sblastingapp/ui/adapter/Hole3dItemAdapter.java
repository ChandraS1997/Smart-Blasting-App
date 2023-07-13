package com.mineexcellence.sblastingapp.ui.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mineexcellence.sblastingapp.R;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable4HoleChargingDataModel;
import com.mineexcellence.sblastingapp.app.BaseRecyclerAdapter;
import com.mineexcellence.sblastingapp.databinding.HoleItemBinding;
import com.mineexcellence.sblastingapp.helper.Constants;
import com.mineexcellence.sblastingapp.interfaces.HoleBgListener;
import com.mineexcellence.sblastingapp.ui.activity.HoleDetail3DModelActivity;
import com.mineexcellence.sblastingapp.utils.StringUtill;

import java.util.List;

public class Hole3dItemAdapter extends BaseRecyclerAdapter {

    Context context;
    List<Response3DTable4HoleChargingDataModel> holeDetailDataList;
    int spaceVal;
    String patternType = "Rectangular/Square";
    int selectedPos = -1;
    int rowPos = -1;

    public Hole3dItemAdapter(Context context, List<Response3DTable4HoleChargingDataModel> holeDetailDataList, int spaceVal, String patternType, int rowPos) {
        this.context = context;
        this.holeDetailDataList = holeDetailDataList;
        this.spaceVal = spaceVal;
        this.patternType = patternType;
        this.rowPos = rowPos;
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

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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
                if (((HoleDetail3DModelActivity) context).holeDetailCallBackListener != null)
                    ((HoleDetail3DModelActivity) context).holeDetailCallBackListener.setBgOfHoleOnNewRowChange(detailData.getRowNo(), detailData.getHoleNo(), getBindingAdapterPosition());
                selectedPos = getBindingAdapterPosition();
                ((HoleDetail3DModelActivity) context).updateValPos = getBindingAdapterPosition();
                ((HoleDetail3DModelActivity) context).updateRowNo = detailData.getRowNo();
                ((HoleDetail3DModelActivity) context).updateHoleNo = detailData.getHoleNo();
                ((HoleDetail3DModelActivity) context).rowPos = rowPos;
                if (((HoleDetail3DModelActivity) context).holeDetailCallBackListener != null)
                    ((HoleDetail3DModelActivity) context).holeDetailCallBackListener.setHoleDetailCallBack(detailData);
                notifyDataSetChanged();
            });

            if (selectedPos == getBindingAdapterPosition()) {
                binding.mainContainerView.setBackgroundResource(R.drawable.bg_light_gray_drawable);
                binding.mainContainerView.setElevation(10f);
            } else {
                binding.mainContainerView.setBackgroundResource(R.color.white);
                binding.mainContainerView.setElevation(0f);
            }

        }

    }

}