package com.smart_blasting_drilling.android.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.response.hole_tables.AllTablesData;
import com.smart_blasting_drilling.android.api.apis.response.hole_tables.Table1Item;
import com.smart_blasting_drilling.android.app.AppDelegate;
import com.smart_blasting_drilling.android.app.BaseRecyclerAdapter;
import com.smart_blasting_drilling.android.databinding.MapHoleColunmItemBinding;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.room_database.AppDatabase;
import com.smart_blasting_drilling.android.ui.activity.BaseActivity;
import com.smart_blasting_drilling.android.ui.activity.HoleDetailActivity;
import com.smart_blasting_drilling.android.ui.models.MapHoleDataModel;
import com.smart_blasting_drilling.android.utils.StringUtill;

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

    class MapHolePointViewHolder extends RecyclerView.ViewHolder {
        MapHoleColunmItemBinding binding;

        public MapHolePointViewHolder(@NonNull MapHoleColunmItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
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

            HoleItemAdapter adapter = new HoleItemAdapter(context, mapHoleDataModel.getHoleDetailDataList(), spaceVal, patternType);
            binding.rowHolePoint.setAdapter(adapter);
        }

    }

}
