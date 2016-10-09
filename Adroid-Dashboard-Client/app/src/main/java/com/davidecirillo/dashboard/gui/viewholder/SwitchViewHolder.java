package com.davidecirillo.dashboard.gui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.davidecirillo.dashboard.R;
import com.davidecirillo.dashboard.data.module.SwitchV0;
import com.squareup.picasso.Picasso;

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
public class SwitchViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.light_name)
    TextView mLightName;

    @BindView(R.id.light_icon)
    ImageView mLightIcon;

    @BindView(R.id.light_status_switch)
    public SwitchCompat mLightSwitchState;

    private boolean currentSwitchState;

    public SwitchViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bind(SwitchV0 switchV0){
        setLightName(switchV0.getName());
        setLightStatus(switchV0.getValue() != 1);

        if(switchV0.getIcon() != null && !switchV0.getIcon().isEmpty()){
            setLightIcon(switchV0.getIcon());
        }
    }

    public void showIcon(boolean shouldShow){
        mLightIcon.setVisibility(shouldShow ? View.VISIBLE : View.GONE);
    }

    public void setAction(View.OnClickListener listener){
        itemView.setOnClickListener(listener);
        mLightSwitchState.setOnClickListener(listener);
        currentSwitchState = mLightSwitchState.isChecked();
    }

    public void restoreSwitchState(){
        mLightSwitchState.setChecked(currentSwitchState);
    }

    private void setLightName(String name){
        mLightName.setText(name);
    }

    private void setLightStatus(boolean status){
//        int background = status ? R.drawable.circle_on : R.drawable.circle_off;
//        mStatusView.setBackground(ContextCompat.getDrawable(mStatusView.getContext(), background));

        mLightSwitchState.setChecked(status);

        //int color = status ? android.R.color.holo_green_dark : android.R.color.holo_red_dark;
        //mLightName.setTextColor(ContextCompat.getColor(context, color));
    }

    private void setLightIcon(String url){
        Picasso.with(mLightIcon.getContext())
                .load(url)
                .into(mLightIcon);
    }

}
