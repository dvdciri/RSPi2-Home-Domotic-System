package com.davidecirillo.dashboard.gui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davidecirillo.dashboard.R;
import com.davidecirillo.dashboard.data.module.ControlSnapshot;
import com.davidecirillo.dashboard.data.module.SwitchV0;
import com.davidecirillo.dashboard.data.module.SensorV0;
import com.davidecirillo.dashboard.gui.viewholder.SwitchViewHolder;
import com.davidecirillo.dashboard.gui.viewholder.PIViewHolder;
import com.davidecirillo.dashboard.gui.viewholder.SensorsViewHolder;
import com.davidecirillo.dashboard.interfaces.ControlDataAdapterInterface;
import com.firebase.client.DataSnapshot;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

import static com.davidecirillo.dashboard.app.AppConfig.blinds_node;
import static com.davidecirillo.dashboard.app.AppConfig.lights_node;
import static com.davidecirillo.dashboard.app.AppConfig.pi_cpu;
import static com.davidecirillo.dashboard.app.AppConfig.pi_disk;
import static com.davidecirillo.dashboard.app.AppConfig.pi_node;
import static com.davidecirillo.dashboard.app.AppConfig.pi_ram;
import static com.davidecirillo.dashboard.app.AppConfig.sensors_node;

/*
  ~ Copyright (c) 2014 Davide Cirillo
  ~
  ~     Licensed under the Apache License, Version 2.0 (the "License");
  ~     you may not use this file except in compliance with the License.
  ~     You may obtain a copy of the License at
  ~
  ~        http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~     Unless required by applicable law or agreed to in writing, software
  ~     distributed under the License is distributed on an "AS IS" BASIS,
  ~     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~     See the License for the specific language governing permissions and
  ~     limitations under the License.
  ~     Come on, don't tell me you read that.
*/
public class RecyclerViewDataAdapter extends RecyclerView.Adapter {

    private final String RECYCLER_DATA_ADAPTER_TAG = "recycler data TAG";

    private final int VIEW_TYPE_SWITCH = 0;
    private final int VIEW_TYPE_SENSOR = 1;
    private final int VIEW_TYPE_PI = 2;

    private ArrayList<ControlSnapshot> mDataList;
    private Context mContext;
    private ControlDataAdapterInterface mControlDataAdapterInterface;

    public RecyclerViewDataAdapter(ArrayList<ControlSnapshot> mDataList, Context mContext, ControlDataAdapterInterface controlDataAdapterInterface) {
        this.mDataList = mDataList;
        this.mContext = mContext;
        this.mControlDataAdapterInterface = controlDataAdapterInterface;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;

        if (viewType == VIEW_TYPE_SWITCH) {
            viewHolder = new SwitchViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.switch_item_recyclerview, parent, false));
        } else if (viewType == VIEW_TYPE_SENSOR) {
            viewHolder = new SensorsViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.sensor_item_recyclerview, parent, false));
        } else if (viewType == VIEW_TYPE_PI) {
            viewHolder = new PIViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.pi_item_recyclerview, parent, false));
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ControlSnapshot currentItem = mDataList.get(position);

        if (holder instanceof SwitchViewHolder) {
            final SwitchViewHolder switchViewHolder = (SwitchViewHolder) holder;

            SwitchV0 switchV0 = currentItem.getDataSnapshot().getValue(SwitchV0.class);
            switchV0.setName(currentItem.getDataSnapshot().getKey());

            switchViewHolder.bind(switchV0);
            switchViewHolder.showIcon(switchV0.getIcon() != null && !switchV0.getIcon().isEmpty());

            switchViewHolder.setAction(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchViewHolder.restoreSwitchState();
                    mControlDataAdapterInterface.OnItemClickListener(currentItem);
                }
            });

        } else if (holder instanceof SensorsViewHolder) {

            SensorV0 sensorV0 = currentItem.getDataSnapshot().getValue(SensorV0.class);
            sensorV0.setName(currentItem.getDataSnapshot().getKey());

            ((SensorsViewHolder) holder).bind(sensorV0);

        } else if (holder instanceof PIViewHolder) {

            String piCategoryType = currentItem.getDataSnapshot().getKey();

            ((PIViewHolder) holder).setCardTitle(piCategoryType);

            ArrayList<PieEntry> entries = new ArrayList<>();
            for (DataSnapshot ramSnapshot : currentItem.getDataSnapshot().getChildren()) {

                switch (piCategoryType) {
                    case pi_cpu:
                        ((PIViewHolder) holder).setCpuTemperature(mContext, (Float.parseFloat((String) ramSnapshot.getValue())));
                        return;
                    case pi_disk:
                        if (ramSnapshot.getKey().equals("free") || ramSnapshot.getKey().equals("used")) {
                            entries.add(new PieEntry(Float.parseFloat(((String) ramSnapshot.getValue())), ramSnapshot.getKey()));
                        }
                        break;
                    case pi_ram:
                        if (ramSnapshot.getKey().equals("free") || ramSnapshot.getKey().equals("used")) {
                            entries.add(new PieEntry(Float.parseFloat(((String) ramSnapshot.getValue())), ramSnapshot.getKey()));
                        }
                        break;
                }
            }

            ((PIViewHolder) holder).setChartWithValues(entries);
        }
    }

    @Override
    public int getItemViewType(int position) {

        switch (mDataList.get(position).getControlType()) {
            case blinds_node:
            case lights_node:
                return VIEW_TYPE_SWITCH;
            case sensors_node:
                return VIEW_TYPE_SENSOR;
            case pi_node:
                return VIEW_TYPE_PI;
        }

        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void addNewDataControl(ControlSnapshot controlSnapshot) {
        mDataList.add(controlSnapshot);

        notifyItemInserted(mDataList.size() - 1);
    }

    public void clearAdapterList() {
        mDataList.clear();

        notifyDataSetChanged();
    }
}
