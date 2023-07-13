package com.mineexcellence.sblastingapp.ui.adapter.pilot_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mineexcellence.sblastingapp.R;
import com.mineexcellence.sblastingapp.api.apis.response.TableFieldItemModel;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable16PilotDataModel;
import com.mineexcellence.sblastingapp.app.BaseRecyclerAdapter;
import com.mineexcellence.sblastingapp.databinding.TableViewBinding;

import java.util.List;

public class TableViewPilotAdapter extends BaseRecyclerAdapter {

    Context context;
    List<TableFieldItemModel> editModelArrayList;
    List<Response3DTable16PilotDataModel> holeDetailDataList;

    public TableViewPilotAdapter(Context context, List<TableFieldItemModel> editModelArrayList, List<Response3DTable16PilotDataModel> holeDetailDataList) {
        this.context = context;
        this.editModelArrayList = editModelArrayList;
        this.holeDetailDataList = holeDetailDataList;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        TableViewBinding binding = DataBindingUtil.inflate(inflater, R.layout.table_view, group, false);
        return new PilotViewHolder(binding);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getViewHolder(LayoutInflater.from(context), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PilotViewHolder viewHolder = (PilotViewHolder) holder;
        viewHolder.setDataBind(holeDetailDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return holeDetailDataList.size();
    }

    class PilotViewHolder extends RecyclerView.ViewHolder {

        TableViewBinding binding;

        public PilotViewHolder(@NonNull TableViewBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        void setDataBind(Response3DTable16PilotDataModel holeDetailData) {
            boolean isHeader = holeDetailData == null && getAdapterPosition() == 0;

            boolean isSelected = false;
            for (int i = 0; i < editModelArrayList.get(getAdapterPosition()).getTableEditModelList().size(); i++) {
                if (editModelArrayList.get(getAdapterPosition()).getTableEditModelList().get(i).isSelected()) {
                    isSelected = true;
                    break;
                }
            }

            PilotHole3dTableColumnViewAdapter columnViewAdapter = new PilotHole3dTableColumnViewAdapter(context, editModelArrayList.get(getAdapterPosition()).getTableEditModelList(), holeDetailData, isHeader, isSelected);
            binding.columnList.setAdapter(columnViewAdapter);

        }

    }

}
