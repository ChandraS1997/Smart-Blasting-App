package com.mineexcellence.sblastingapp.ui.adapter;

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
import com.mineexcellence.sblastingapp.R;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable2DataModel;
import com.mineexcellence.sblastingapp.app.BaseRecyclerAdapter;
import com.mineexcellence.sblastingapp.databinding.MapHoleColunmItemBinding;
import com.mineexcellence.sblastingapp.helper.Constants;
import com.mineexcellence.sblastingapp.interfaces.Hole3DBgListener;
import com.mineexcellence.sblastingapp.room_database.AppDatabase;
import com.mineexcellence.sblastingapp.ui.activity.BaseActivity;
import com.mineexcellence.sblastingapp.ui.activity.HoleDetail3DModelActivity;
import com.mineexcellence.sblastingapp.ui.models.MapHole3DDataModel;

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
