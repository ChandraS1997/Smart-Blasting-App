package com.smart_blasting_drilling.android.ui.adapter;

import android.content.Context;
import android.util.Log;
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
import com.smart_blasting_drilling.android.ui.activity.HoleDetail3DModelActivity;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.util.List;

public class Hole3dItemAdapter extends BaseRecyclerAdapter {

    Context context;
    List<Response3DTable4HoleChargingDataModel> holeDetailDataList;
    int spaceVal;
    String patternType = "Rectangular/Square";

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

    class HoleItemViewHolder extends RecyclerView.ViewHolder {
        HoleItemBinding binding;

        public HoleItemViewHolder(@NonNull HoleItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        void setDataBind(Response3DTable4HoleChargingDataModel detailData) {
            if (patternType.equals("Staggered")) {
                if (spaceVal == 1) {
                    binding.startSpaceView.setVisibility(View.GONE);
                    binding.endSpaceView.setVisibility(View.VISIBLE);
                } else {
                    binding.startSpaceView.setVisibility(View.VISIBLE);
                    binding.endSpaceView.setVisibility(View.GONE);
                }
            } else if (patternType.equals("Rectangular/Square"))  {
                binding.startSpaceView.setVisibility(View.GONE);
                binding.endSpaceView.setVisibility(View.VISIBLE);
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
                ((HoleDetail3DModelActivity) context).openHoleDetailDialog(detailData);
            });

            try {
                ViewTreeObserver vto = binding.mainContainerView.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        if (binding.mainContainerView.getMeasuredHeight() > 0) {
                            binding.mainContainerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            int width = binding.mainContainerView.getMeasuredWidth();
                            int height = binding.mainContainerView.getMeasuredHeight();

                            Log.e("Width" + width, "  ->  Hieght" + height);
                        }
                    }
                });
            } catch (Exception e) {
                e.getLocalizedMessage();
            }

        }

    }

}