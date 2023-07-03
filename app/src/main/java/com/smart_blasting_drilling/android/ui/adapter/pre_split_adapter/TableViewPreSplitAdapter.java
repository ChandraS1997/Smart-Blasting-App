package com.smart_blasting_drilling.android.ui.adapter.pre_split_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.response.TableFieldItemModel;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.pre_spilit_table.HoleDetailItem;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.pre_spilit_table.Response3DTable18PreSpilitDataModel;
import com.smart_blasting_drilling.android.app.BaseRecyclerAdapter;
import com.smart_blasting_drilling.android.databinding.TableViewBinding;
import com.smart_blasting_drilling.android.ui.adapter.Hole3dTableColumnViewAdapter;

import java.util.List;

public class TableViewPreSplitAdapter extends BaseRecyclerAdapter {

    Context context;
    List<TableFieldItemModel> editModelArrayList;
    List<HoleDetailItem> modelList;

    public TableViewPreSplitAdapter(Context context, List<TableFieldItemModel> editModelArrayList, List<HoleDetailItem> modelList) {
        this.context = context;
        this.editModelArrayList = editModelArrayList;
        this.modelList = modelList;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        TableViewBinding binding = DataBindingUtil.inflate(inflater, R.layout.table_view, group, false);
        return new PreSplitViewHolder(binding);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getViewHolder(LayoutInflater.from(context), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PreSplitViewHolder viewHolder = (PreSplitViewHolder) holder;
        viewHolder.setDataBind(modelList.get(position));
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class PreSplitViewHolder extends RecyclerView.ViewHolder {

        TableViewBinding binding;

        public PreSplitViewHolder(@NonNull TableViewBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        void setDataBind(HoleDetailItem model) {
            boolean isHeader = model == null && getAdapterPosition() == 0;

            boolean isSelected = false;
            for (int i = 0; i < editModelArrayList.get(getAdapterPosition()).getTableEditModelList().size(); i++) {
                if (editModelArrayList.get(getAdapterPosition()).getTableEditModelList().get(i).isSelected()) {
                    isSelected = true;
                    break;
                }
            }

            PreSplitHole3dTableColumnViewAdapter columnViewAdapter = new PreSplitHole3dTableColumnViewAdapter(
                    context,
                    editModelArrayList.get(getAdapterPosition()).getTableEditModelList(),
                    model,
                    isHeader,
                    isSelected);

            binding.columnList.setAdapter(columnViewAdapter);
        }

    }

}
