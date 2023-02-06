package com.smart_blasting_drilling.android.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.app.BaseRecyclerAdapter;
import com.smart_blasting_drilling.android.databinding.ViewTableEditFieldsBinding;
import com.smart_blasting_drilling.android.ui.models.TableEditModel;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.util.ArrayList;

public class AdapterEditTableFields extends BaseRecyclerAdapter {
    ViewTableEditFieldsBinding binding;
    Context context;
    ArrayList<TableEditModel> editModelArrayList;
    ArrayList<TableEditModel> selectedData = new ArrayList<>();


    public AdapterEditTableFields(Context context, ArrayList<TableEditModel> editModelArrayList) {
        this.context = context;
        this.editModelArrayList = editModelArrayList;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        binding = DataBindingUtil.inflate(inflater, R.layout.view_table_edit_fields, group, false);
        return new ViewHolder(binding);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getViewHolder(LayoutInflater.from(context), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
       viewHolder.setData(editModelArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return editModelArrayList.size();
    }

    public ArrayList<TableEditModel> getSelectedData() {
        return selectedData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewTableEditFieldsBinding binding;

        public ViewHolder(@NonNull ViewTableEditFieldsBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(TableEditModel tableEditModel) {
            binding.checkboxTableEdit.setText(StringUtill.getString(tableEditModel.getCheckBox()));
            binding.checkboxTableEdit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b){
                        selectedData.add(tableEditModel);
                    }else{
                        selectedData.remove(tableEditModel);
                    }
                }
            });
        }
    }
}
