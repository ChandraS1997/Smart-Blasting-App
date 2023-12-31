package com.mineexcellence.sblastingapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mineexcellence.sblastingapp.R;
import com.mineexcellence.sblastingapp.api.apis.response.TableFieldItemModel;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable4HoleChargingDataModel;
import com.mineexcellence.sblastingapp.app_utils.BaseRecyclerAdapter;
import com.mineexcellence.sblastingapp.databinding.TableViewBinding;

import java.util.List;

public class TableView3dAdapter extends BaseRecyclerAdapter {

    Context context;
    List<TableFieldItemModel> editModelArrayList;
    List<Response3DTable4HoleChargingDataModel> holeDetailDataList;

    public TableView3dAdapter(Context ctx, List<TableFieldItemModel> arrayList, List<Response3DTable4HoleChargingDataModel> holeDetailDataList) {
        this.context = ctx;
        this.editModelArrayList = arrayList;
        this.holeDetailDataList = holeDetailDataList;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        TableViewBinding binding = DataBindingUtil.inflate(inflater, R.layout.table_view, group, false);
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
        viewHolder.setDataBind(holeDetailDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return holeDetailDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TableViewBinding binding;

        public ViewHolder(@NonNull TableViewBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        void setDataBind(Response3DTable4HoleChargingDataModel holeDetailData) {
            try {
                boolean isHeader = holeDetailData == null && getAdapterPosition() == 0;

                boolean isSelected = false;
                for (int i = 0; i < editModelArrayList.get(getAdapterPosition()).getTableEditModelList().size(); i++) {
                    if (editModelArrayList.get(getAdapterPosition()).getTableEditModelList().get(i).isSelected()) {
                        isSelected = true;
                        break;
                    }
                }

                Hole3dTableColumnViewAdapter columnViewAdapter = new Hole3dTableColumnViewAdapter(context, editModelArrayList.get(getAdapterPosition()).getTableEditModelList(), isHeader, holeDetailData, isSelected);
                binding.columnList.setAdapter(columnViewAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}