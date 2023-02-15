package com.smart_blasting_drilling.android.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.app.BaseRecyclerAdapter;
import com.smart_blasting_drilling.android.databinding.HoleTableColumnViewBinding;
import com.smart_blasting_drilling.android.ui.models.TableEditModel;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.util.ArrayList;
import java.util.List;

public class HoleTableColumnViewAdapter extends BaseRecyclerAdapter {

    Context context;
    List<TableEditModel> editModelArrayList;
    boolean isHeader;

    public HoleTableColumnViewAdapter(Context context, List<TableEditModel> editModelArrayList, boolean isHeader) {
        this.context = context;
        this.editModelArrayList = editModelArrayList;
        this.isHeader = isHeader;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        HoleTableColumnViewBinding binding = DataBindingUtil.inflate(inflater, R.layout.hole_table_column_view, group, false);
        return new HoleColumnViewHolder(binding);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HoleColumnViewHolder viewHolder = (HoleColumnViewHolder) holder;
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

//            if (!model.isFirstTime()) {
                if (model.isSelected()) {
                    binding.holeIdVal.setVisibility(View.VISIBLE);
                } else {
                    binding.holeIdVal.setVisibility(View.GONE);
                }
//            }
            binding.holeIdVal.setText(StringUtill.getString(model.getCheckBox()));

            LinearLayout.LayoutParams layoutParams;
            if (isHeader) {
                layoutParams = new LinearLayout.LayoutParams(300, 100);
            } else {
                layoutParams = new LinearLayout.LayoutParams(300, LinearLayout.LayoutParams.WRAP_CONTENT);
            }

            binding.holeIdVal.setLayoutParams(layoutParams);

        }

    }

}
