package com.smart_blasting_drilling.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.activity.HoleDetailActivity;
import com.smart_blasting_drilling.android.activity.HomeActivity;
import com.smart_blasting_drilling.android.app_utils.BaseRecyclerAdapter;
import com.smart_blasting_drilling.android.databinding.AdapterHomeBinding;
import com.smart_blasting_drilling.android.databinding.AdapterHomenewBinding;

import java.util.ArrayList;
import java.util.List;

public class ProjectLIstAdapter extends BaseRecyclerAdapter {

    Context ctx;
    List<String> projectList = new ArrayList<>();

    public ProjectLIstAdapter(Context ctx, List<String> projectList) {
        this.ctx = ctx;
        this.projectList = projectList;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        AdapterHomenewBinding binding = DataBindingUtil.inflate(inflater,R.layout.adapter_homenew,group,false);
        return new ProjectLIstAdapter.ViewHolder(binding);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getViewHolder(LayoutInflater.from(ctx),parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder vholder = (ViewHolder) holder;
        vholder.itemView.setOnClickListener(v ->{
          //  Navigation.findNavController(v).navigate(R.id.HoleDetailsTableViewFragment);
         //   Navigation.findNavController(v)
            Intent i = new Intent(ctx, HoleDetailActivity.class);
            ctx.startActivity(i);

        });
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        AdapterHomenewBinding binding;
        public ViewHolder(@NonNull AdapterHomenewBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
