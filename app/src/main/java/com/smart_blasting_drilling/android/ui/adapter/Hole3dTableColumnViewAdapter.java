package com.smart_blasting_drilling.android.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.app.BaseRecyclerAdapter;
import com.smart_blasting_drilling.android.databinding.HoleTableColumnViewBinding;
import com.smart_blasting_drilling.android.ui.activity.HoleDetail3DModelActivity;
import com.smart_blasting_drilling.android.ui.activity.HoleDetailActivity;
import com.smart_blasting_drilling.android.ui.models.TableEditModel;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.util.List;

public class Hole3dTableColumnViewAdapter extends BaseRecyclerAdapter {

    Context context;
    List<TableEditModel> editModelArrayList;
    boolean isHeader;

    public Hole3dTableColumnViewAdapter(Context context, List<TableEditModel> editModelArrayList, boolean isHeader) {
        this.context = context;
        this.editModelArrayList = editModelArrayList;
        this.isHeader = isHeader;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        HoleTableColumnViewBinding binding = DataBindingUtil.inflate(inflater, R.layout.hole_table_column_view, group, false);
        return new Hole3dTableColumnViewAdapter.HoleColumnViewHolder(binding);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Hole3dTableColumnViewAdapter.HoleColumnViewHolder viewHolder = (Hole3dTableColumnViewAdapter.HoleColumnViewHolder) holder;
        viewHolder.setDataBind(editModelArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return editModelArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class HoleColumnViewHolder extends RecyclerView.ViewHolder {
        HoleTableColumnViewBinding binding;

        public HoleColumnViewHolder(@NonNull HoleTableColumnViewBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        void setDataBind(TableEditModel model) {

            if (((HoleDetail3DModelActivity) context).isTableHeaderFirstTimeLoad) {
                binding.holeIdVal.setVisibility(View.VISIBLE);
            } else {
                if (model.isSelected()) {
                    binding.holeIdVal.setVisibility(View.VISIBLE);
                } else {
                    binding.holeIdVal.setVisibility(View.GONE);
                }
            }
            binding.holeIdVal.setText(StringUtill.getString(model.getCheckBox()));

            LinearLayout.LayoutParams layoutParams;
            if (isHeader) {
                layoutParams = new LinearLayout.LayoutParams(300, 100);
            } else {
                layoutParams = new LinearLayout.LayoutParams(300, LinearLayout.LayoutParams.WRAP_CONTENT);
            }

            binding.holeIdVal.setLayoutParams(layoutParams);

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
