package com.smart_blasting_drilling.android.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.response.hole_tables.AllTablesData;
import com.smart_blasting_drilling.android.api.apis.response.hole_tables.Table1Item;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable2DataModel;
import com.smart_blasting_drilling.android.app.BaseRecyclerAdapter;
import com.smart_blasting_drilling.android.databinding.MapHoleColunmItemBinding;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.interfaces.Hole3DBgListener;
import com.smart_blasting_drilling.android.room_database.AppDatabase;
import com.smart_blasting_drilling.android.ui.activity.BaseActivity;
import com.smart_blasting_drilling.android.ui.activity.HoleDetail3DModelActivity;
import com.smart_blasting_drilling.android.ui.activity.HoleDetailActivity;
import com.smart_blasting_drilling.android.ui.models.MapHole3DDataModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MapHolePoint3dAdapter extends BaseRecyclerAdapter {

    Context context;
    List<MapHole3DDataModel> colHoleDetailDataList;

    public MapHolePoint3dAdapter(Context context, List<MapHole3DDataModel> colHoleDetailDataList) {
        this.context = context;
        this.colHoleDetailDataList = colHoleDetailDataList;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        MapHoleColunmItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.map_hole_colunm_item, group, false);
        return new MapHolePoint3dAdapter.MapHolePointViewHolder(binding);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MapHolePoint3dAdapter.MapHolePointViewHolder viewHolder = (MapHolePoint3dAdapter.MapHolePointViewHolder) holder;
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

        void setDataBind(MapHole3DDataModel mapHoleDataModel) {
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
                        JsonArray array = new Gson().fromJson(new Gson().fromJson(new Gson().fromJson(appDatabase.projectHoleDetailRowColDao().getAllBladesProject(((HoleDetail3DModelActivity) context).bladesRetrieveData.get(0).getDesignId()).getProjectHole(), JsonObject.class).get(Constants._3D_TBALE_NAME).getAsJsonPrimitive(), String.class), JsonArray.class);
                        List<Response3DTable2DataModel> holeDetailDataList = new ArrayList<>();
                        for (JsonElement e : new Gson().fromJson(new Gson().fromJson(array.get(1), String.class), JsonArray.class)) {
                            holeDetailDataList.add(new Gson().fromJson(e, Response3DTable2DataModel.class));
                        }
                        if (!Constants.isListEmpty(holeDetailDataList)) {
                            String[] drillPatternDataItem = new String[holeDetailDataList.size()];
                            for (int i = 0; i < holeDetailDataList.size(); i++) {
                                drillPatternDataItem[i] = holeDetailDataList.get(i).getPatternType();
                            }
                            patternType = drillPatternDataItem[0];
                        }
                    }
                }
            } catch (Exception e) {
                e.getLocalizedMessage();
            }

            Hole3dItemAdapter adapter = new Hole3dItemAdapter(context, mapHoleDataModel.getHoleDetailDataList(), spaceVal, patternType, getBindingAdapterPosition());
            binding.rowHolePoint.setAdapter(adapter);
        }

        @Override
        public void setBackgroundRefresh() {
            notifyItemChanged(((HoleDetail3DModelActivity) context).rowPos);
        }
    }

}
