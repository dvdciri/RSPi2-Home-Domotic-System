package com.davidecirillo.dashboard.gui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.davidecirillo.dashboard.R;
import com.davidecirillo.dashboard.data.module.SensorV0;

import butterknife.BindView;
import butterknife.ButterKnife;

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
public class SensorsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.sensor_name)
    TextView mSensorName;

    @BindView(R.id.sensor_current_value)
    TextView mSensorCurrentValue;

    @BindView(R.id.sensor_min_value)
    TextView mMinValue;

    @BindView(R.id.sensor_max_value)
    TextView mMaxValue;

    public SensorsViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bind(SensorV0 sensorV0){
        setSensorName(sensorV0.getName());
        setSensorCurrentValue(sensorV0.getCurrent_inside());
        setMaxValue(sensorV0.getMax_inside());
        setMinValue(sensorV0.getMin_inside());
    }

    public void setSensorName(String name){
        mSensorName.setText(name);
    }

    public void setSensorCurrentValue(String currentValue) {
        mSensorCurrentValue.setText(currentValue);
    }

    public void setMinValue(String value){
        mMinValue.setText(value);
    }

    public void setMaxValue(String value){
        mMaxValue.setText(value);
    }
}
