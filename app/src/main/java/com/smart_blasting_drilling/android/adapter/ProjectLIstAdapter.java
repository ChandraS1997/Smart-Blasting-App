package com.bdapp.hospital_erp.ui.home.Patient.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.app_utils.BaseRecyclerAdapter;
import com.smart_blasting_drilling.android.databinding.AdapterHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class ProjectLIstAdapter extends BaseRecyclerAdapter {

    Context ctx;
    List<String> patintList = new ArrayList<>();

    public ProjectLIstAdapter(Context ctx, List<String> patintList) {
        this.ctx = ctx;
        this.patintList = patintList;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        AdapterHomeBinding binding = DataBindingUtil.inflate(inflater,R.layout.adapter_home,group,false);
        return new ProjectLIstAdapter.ViewHolder(binding);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getViewHolder(LayoutInflater.from(ctx),parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
       /* ViewHolder vholder = (ViewHolder) holder;
        vholder.itemView.setOnClickListener(v ->{
            Navigation.findNavController(v).navigate(R.id.patientInfoFragment);
        });*/
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        AdapterHomeBinding binding;
        public ViewHolder(@NonNull AdapterHomeBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
