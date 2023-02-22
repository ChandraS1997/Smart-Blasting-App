package com.smart_blasting_drilling.android.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smart_blasting_drilling.android.app.BaseRecyclerAdapter;
import com.smart_blasting_drilling.android.ui.models.InitiatingDeviceModel;

import java.util.List;

public class ElectronicDetonatorAdapter extends BaseRecyclerAdapter {

    Context context;
    List<InitiatingDeviceModel> initiatingDeviceModelList;

    public ElectronicDetonatorAdapter(Context context, List<InitiatingDeviceModel> initiatingDeviceModelList) {
        this.context = context;
        this.initiatingDeviceModelList = initiatingDeviceModelList;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        return null;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
