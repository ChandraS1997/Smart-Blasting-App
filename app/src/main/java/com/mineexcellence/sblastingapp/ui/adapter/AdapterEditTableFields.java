package com.mineexcellence.sblastingapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mineexcellence.sblastingapp.R;
import com.mineexcellence.sblastingapp.app.BaseRecyclerAdapter;
import com.mineexcellence.sblastingapp.databinding.ViewTableEditFieldsBinding;
import com.mineexcellence.sblastingapp.ui.models.TableEditModel;
import com.mineexcellence.sblastingapp.utils.StringUtill;

import java.util.ArrayList;
import java.util.List;

public class AdapterEditTableFields extends BaseRecyclerAdapter {
    ViewTableEditFieldsBinding binding;
    Context context;
    List<TableEditModel> editModelArrayList;
    ArrayList<TableEditModel> selectedData = new ArrayList<>();


    public AdapterEditTableFields(Context context, List<TableEditModel> editModelArrayList) {
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

    public List<TableEditModel> getSelectedData() {
        return editModelArrayList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewTableEditFieldsBinding binding;

        public ViewHolder(@NonNull ViewTableEditFieldsBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setData(TableEditModel tableEditModel) {
            binding.projectNameTitle.setText(StringUtill.getString(tableEditModel.getCheckBox()));
            binding.checkboxTableEdit.setChecked(tableEditModel.isSelected());
            binding.checkboxTableEdit.setOnCheckedChangeListener((compoundButton, b) -> {
                tableEditModel.setSelected(b);
                editModelArrayList.set(getAdapterPosition(), tableEditModel);
            });
        }
    }
}
