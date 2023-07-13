package com.mineexcellence.sblastingapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mineexcellence.sblastingapp.R;
import com.mineexcellence.sblastingapp.api.apis.response.hole_tables.AllTablesData;
import com.mineexcellence.sblastingapp.api.apis.response.hole_tables.Table1Item;
import com.mineexcellence.sblastingapp.app.BaseRecyclerAdapter;
import com.mineexcellence.sblastingapp.databinding.MapHoleColunmItemBinding;
import com.mineexcellence.sblastingapp.helper.Constants;
import com.mineexcellence.sblastingapp.interfaces.Hole3DBgListener;
import com.mineexcellence.sblastingapp.room_database.AppDatabase;
import com.mineexcellence.sblastingapp.ui.activity.BaseActivity;
import com.mineexcellence.sblastingapp.ui.activity.HoleDetailActivity;
import com.mineexcellence.sblastingapp.ui.models.MapHoleDataModel;

import java.lang.reflect.Type;
import java.util.List;

public class MapHolePointAdapter extends BaseRecyclerAdapter {

    Context context;
    List<MapHoleDataModel> colHoleDetailDataList;

    public MapHolePointAdapter(Context context, List<MapHoleDataModel> colHoleDetailDataList) {
        this.context = context;
        this.colHoleDetailDataList = colHoleDetailDataList;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        MapHoleColunmItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.map_hole_colunm_item, group, false);
        return new MapHolePointViewHolder(binding);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MapHolePointViewHolder viewHolder = (MapHolePointViewHolder) holder;
        viewHolder.setDataBind(colHoleDetailDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return colHoleDetailDataList.size();
    }

    class MapHolePointViewHolder extends RecyclerView.ViewHolder implements Hole3DBgListener {
        MapHoleColunmItemBinding binding;

        public MapHolePointViewHolder(@NonNull MapHoleColunmItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            Constants.hole3DBgListener = this;
        }

        void setDataBind(MapHoleDataModel mapHoleDataModel) {
            int spaceVal = 0;
            if (getAdapterPosition() % 2 == 0) {
                spaceVal = 1;
            } else {
                spaceVal = 0;
            }

            AppDatabase appDatabase = ((BaseActivity) context).appDatabase;
            String patternType = "Staggered";

            try {
                if (!Constants.isListEmpty(appDatabase.projectHoleDetailRowColDao().getAllBladesProjectList())) {
                    if (appDatabase.projectHoleDetailRowColDao().getAllBladesProjectList().get(0) != null) {
                        Type teamList = new TypeToken<List<Table1Item>>() {
                        }.getType();
                        AllTablesData tablesData = new Gson().fromJson(appDatabase.projectHoleDetailRowColDao().getAllBladesProject(((HoleDetailActivity) context).bladesRetrieveData.getDesignId()).getProjectHole(), AllTablesData.class);
                        List<Table1Item> drillPatternDataItemList = tablesData.getTable1();
                        if (!Constants.isListEmpty(drillPatternDataItemList)) {
                            String[] drillPatternDataItem = new String[drillPatternDataItemList.size()];
                            for (int i = 0; i < drillPatternDataItemList.size(); i++) {
                                drillPatternDataItem[i] = drillPatternDataItemList.get(i).getPatternType();
                            }
                            patternType = drillPatternDataItem[0];
                        }
                    }
                }
            } catch (Exception e) {
                e.getLocalizedMessage();
            }

            HoleItemAdapter adapter = new HoleItemAdapter(context, mapHoleDataModel.getHoleDetailDataList(), spaceVal, patternType, getBindingAdapterPosition());
            binding.rowHolePoint.setAdapter(adapter);
        }

        @Override
        public void setBackgroundRefresh() {
            notifyItemChanged(((HoleDetailActivity) context).rowPos);
        }
    }

}
