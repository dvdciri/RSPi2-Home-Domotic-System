package com.davidecirillo.dashboard.gui.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.davidecirillo.dashboard.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

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
public class PIViewHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.piChard)
    PieChart mPieChart;

    @BindView(R.id.pi_title)
    TextView mPieTitle;

    @BindView(R.id.big_label)
    TextView mBigLabe;


    public PIViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void setCardTitle(String title){
        mPieTitle.setText(title);
    }

    public void setChartWithValues(ArrayList<PieEntry> entries){

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);

        mPieChart.setUsePercentValues(true);
        mPieChart.setDescription("");

        mPieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mPieChart.setRotationEnabled(true);
        mPieChart.setHighlightPerTapEnabled(true);
        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = mPieChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setYOffset(0f);

        // entry label styling
        mPieChart.setEntryLabelColor(Color.WHITE);
        mPieChart.setEntryLabelTextSize(12f);

        mPieChart.setVisibility(View.VISIBLE);

        mPieChart.setData(data);

        mPieChart.highlightValues(null);

        mPieChart.notifyDataSetChanged();
        mPieChart.invalidate();
    }

    public void setCpuTemperature(Context context, float temp){

        int resColor = 0;
        String suggestion = "";

        if(temp < 10 || temp > 75){
            //Red
            resColor = android.R.color.holo_red_dark;
            suggestion = "Not Good! Danger";
        }  else if(temp > 10 && temp < 25 || temp > 55 && temp < 75){
            //orange
            resColor = android.R.color.holo_orange_dark;
            suggestion = "Not So Good";
        }else if(temp > 25 && temp < 55){
            //green
            resColor = android.R.color.holo_green_dark;
            suggestion = "Good";
        }

        mBigLabe.setText(context.getString(R.string.cpu_temperature_string, Float.toString(temp), suggestion));
        mBigLabe.setTextColor(ContextCompat.getColor(context, resColor));
        mBigLabe.setVisibility(View.VISIBLE);

    }

}
