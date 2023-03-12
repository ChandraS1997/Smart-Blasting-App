package com.smart_blasting_drilling.android.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.app_utils.BaseRecyclerAdapter;
import com.smart_blasting_drilling.android.databinding.AdapterHoleStatusItemBinding;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.util.List;

public class HoleStatusAdapter extends BaseRecyclerAdapter {

    Context context;
    List<String> holeList;
    int selectedIndex = 0;
    String selectedStatus = "";

    public HoleStatusAdapter(Context context, List<String> holeList) {
        this.context = context;
        this.holeList = holeList;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        AdapterHoleStatusItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.adapter_hole_status_item, group, false);
        return new HoleStatusViewHolder(binding);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HoleStatusViewHolder viewHolder = (HoleStatusViewHolder) holder;
        viewHolder.setDataBind(holeList.get(position));
    }

    @Override
    public int getItemCount() {
        return holeList.size();
    }

    public String getSelectedStatus() {
        return selectedStatus;
    }

    class HoleStatusViewHolder extends RecyclerView.ViewHolder {

        AdapterHoleStatusItemBinding binding;

        public HoleStatusViewHolder(@NonNull AdapterHoleStatusItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        void setDataBind(String holeStatus) {
            binding.holeStatusTxt.setText(StringUtill.getString(holeStatus));

            binding.holeStatusRdBtn.setChecked(getBindingAdapterPosition() == selectedIndex);

            binding.holeStatusRdBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        selectedIndex = getBindingAdapterPosition();
                        selectedStatus = holeStatus;
                        notifyDataSetChanged();
                    }
                }
            });
        }

    }

}
