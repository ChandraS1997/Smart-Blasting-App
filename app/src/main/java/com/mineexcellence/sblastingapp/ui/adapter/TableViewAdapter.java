package com.mineexcellence.sblastingapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mineexcellence.sblastingapp.R;
import com.mineexcellence.sblastingapp.api.apis.response.ResponseHoleDetailData;
import com.mineexcellence.sblastingapp.api.apis.response.TableFieldItemModel;
import com.mineexcellence.sblastingapp.app_utils.BaseRecyclerAdapter;
import com.mineexcellence.sblastingapp.databinding.TableViewBinding;

import java.util.List;

public class TableViewAdapter extends BaseRecyclerAdapter {

    Context context;
    List<TableFieldItemModel> editModelArrayList;
    List<ResponseHoleDetailData> holeDetailDataList;

    public TableViewAdapter(Context ctx, List<TableFieldItemModel> arrayList, List<ResponseHoleDetailData> holeDetailDataList) {
        this.context = ctx;
        this.editModelArrayList = arrayList;
        this.holeDetailDataList = holeDetailDataList;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        TableViewBinding binding = DataBindingUtil.inflate(inflater, R.layout.table_view, group, false);
        return new TableViewAdapter.ViewHolder(binding);
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

        void setDataBind(ResponseHoleDetailData holeDetailData) {
            boolean isHeader = holeDetailData == null && getAdapterPosition() == 0;

            boolean isSelected = false;
            for (int i = 0; i < editModelArrayList.get(getAdapterPosition()).getTableEditModelList().size(); i++) {
                if (editModelArrayList.get(getAdapterPosition()).getTableEditModelList().get(i).isSelected()) {
                    isSelected = true;
                    break;
                }
            }

            HoleTableColumnViewAdapter columnViewAdapter = new HoleTableColumnViewAdapter(context, editModelArrayList.get(getAdapterPosition()).getTableEditModelList(), isHeader, holeDetailData, isSelected);
            binding.columnList.setAdapter(columnViewAdapter);

        }

    }
}
