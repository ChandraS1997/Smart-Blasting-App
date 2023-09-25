package com.mineexcellence.sblastingapp.ui.adapter.pilot_adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mineexcellence.sblastingapp.R;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable16PilotDataModel;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable2DataModel;
import com.mineexcellence.sblastingapp.app.BaseRecyclerAdapter;
import com.mineexcellence.sblastingapp.databinding.HoleItemBinding;
import com.mineexcellence.sblastingapp.databinding.MapHoleColunmItemBinding;
import com.mineexcellence.sblastingapp.helper.Constants;
import com.mineexcellence.sblastingapp.interfaces.Hole3DBgListener;
import com.mineexcellence.sblastingapp.room_database.AppDatabase;
import com.mineexcellence.sblastingapp.ui.activity.BaseActivity;
import com.mineexcellence.sblastingapp.ui.activity.HoleDetail3DModelActivity;
import com.mineexcellence.sblastingapp.utils.StringUtill;

import java.util.ArrayList;
import java.util.List;

public class MapHolePoint3dForPilotAdapter extends BaseRecyclerAdapter {

    Context context;
    List<Response3DTable16PilotDataModel> pilotHoleDetailDataList;
    String patternType = "Staggered";
    int spaceVal = 0;
    int selectedPos = -1;

    public MapHolePoint3dForPilotAdapter(Context context, List<Response3DTable16PilotDataModel> pilotHoleDetailDataList) {
        this.context = context;
        this.pilotHoleDetailDataList = pilotHoleDetailDataList;

        AppDatabase appDatabase = ((BaseActivity) context).appDatabase;

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
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        HoleItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.hole_item, group, false);
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
        viewHolder.setDataBind(pilotHoleDetailDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return pilotHoleDetailDataList.size();
    }

    class MapHolePointViewHolder extends RecyclerView.ViewHolder implements Hole3DBgListener {

        HoleItemBinding binding;

        public MapHolePointViewHolder(@NonNull HoleItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            Constants.hole3DBgListener = this;
        }

        void setDataBind(Response3DTable16PilotDataModel model) {

            binding.holeStatusTxt.setGravity(Gravity.CENTER);
            binding.startSpaceView.setVisibility(View.VISIBLE);
            binding.endSpaceView.setVisibility(View.VISIBLE);

            binding.holeStatusTxt.setText(StringUtill.getString(model.getHoleID()));

            itemView.setOnClickListener(view -> {
                if (((HoleDetail3DModelActivity) context).mapPilotCallBackListener != null)
                    ((HoleDetail3DModelActivity) context).mapPilotCallBackListener.setBgOfHoleOnPilotOnNewRowChange(model.getHoleID(), getBindingAdapterPosition());
                selectedPos = getBindingAdapterPosition();
                ((HoleDetail3DModelActivity) context).pilotRowPos = getBindingAdapterPosition();
                ((HoleDetail3DModelActivity) context).pilotUpdateHoleId = model.getHoleID();
                ((HoleDetail3DModelActivity) context).rowPos = getBindingAdapterPosition();
                if (((HoleDetail3DModelActivity) context).mapPilotCallBackListener != null)
                    ((HoleDetail3DModelActivity) context).mapPilotCallBackListener.setHoleDetailForPilotCallBack(model);
                notifyDataSetChanged();
            });

            if (selectedPos == getBindingAdapterPosition()) {
                binding.mainContainerView.setBackgroundResource(R.drawable.bg_light_gray_drawable);
                binding.mainContainerView.setElevation(10f);
            } else {
                binding.mainContainerView.setBackgroundResource(R.color.white);
                binding.mainContainerView.setElevation(0f);
            }
        }

        @Override
        public void setBackgroundRefresh() {
            selectedPos = -1;
            notifyItemChanged(((HoleDetail3DModelActivity) context).pilotRowPos);
        }

    }

}
