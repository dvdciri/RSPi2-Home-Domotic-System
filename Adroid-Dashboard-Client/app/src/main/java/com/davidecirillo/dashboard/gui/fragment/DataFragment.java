package com.davidecirillo.dashboard.gui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.davidecirillo.dashboard.R;
import com.davidecirillo.dashboard.app.AppConfig;
import com.davidecirillo.dashboard.app.DashboardApplication;
import com.davidecirillo.dashboard.data.module.ControlSnapshot;
import com.davidecirillo.dashboard.data.module.SwitchV0;
import com.davidecirillo.dashboard.gui.adapter.RecyclerViewDataAdapter;
import com.davidecirillo.dashboard.interfaces.ControlDataAdapterInterface;
import com.davidecirillo.dashboard.interfaces.DataFragmentInterface;
import com.davidecirillo.dashboard.utils.FirebaseDataHelper;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.davidecirillo.dashboard.app.AppConfig.blinds_node;
import static com.davidecirillo.dashboard.app.AppConfig.firebase_base_reference;
import static com.davidecirillo.dashboard.app.AppConfig.firebase_control_reference;
import static com.davidecirillo.dashboard.app.AppConfig.lights_node;
import static com.davidecirillo.dashboard.app.AppConfig.slash;

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

public class DataFragment extends Fragment implements DataFragmentInterface, ControlDataAdapterInterface {

    private static final String KEY_CATEGORY_NAME = "key_category_name";

    @BindView(R.id.main_recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.loading_view)
    ProgressBar mLoadingView;

    @Inject
    FirebaseDataHelper mFirebaseDataHelper;

    private RecyclerViewDataAdapter mAdapter;

    public static DataFragment getInstance(String categoryName) {
        DataFragment fragment = new DataFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_CATEGORY_NAME, categoryName);
        fragment.setArguments(bundle);
        return fragment;
    }

    public DataFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DashboardApplication.getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.data_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFirebaseDataHelper.setDataFragmentInterface(this);

        setUpRecyclerView();
    }

    @Override
    public void onStart() {
        super.onStart();

        String categoryName = getArguments().getString(KEY_CATEGORY_NAME);

        //If is the pi node we have a different url
        if (categoryName != null && categoryName.equals(AppConfig.pi_node))
            mFirebaseDataHelper.loadDataControl(firebase_base_reference, categoryName);
        else
            mFirebaseDataHelper.loadDataControl(firebase_control_reference + slash, categoryName);
    }

    private void setUpRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        mAdapter = new RecyclerViewDataAdapter(new ArrayList<ControlSnapshot>(), getContext(), this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void showLoading(boolean status) {
        mLoadingView.setVisibility(status ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onNewControl(ControlSnapshot controlSnapshot, boolean shouldClearList) {
        if (mAdapter != null) {
            if (shouldClearList)
                mAdapter.clearAdapterList();

            mAdapter.addNewDataControl(controlSnapshot);
        }
    }

    @Override
    public void onActionResult(String message) {
        if (getView() != null)
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void OnItemClickListener(ControlSnapshot controlSnapshot) {

        //Action only if is a light or blinds
        if (controlSnapshot.getControlType().equals(lights_node)) {

            int pin_number = controlSnapshot.getDataSnapshot().getValue(SwitchV0.class).getPin_number();
            String name = controlSnapshot.getDataSnapshot().getKey();
            mFirebaseDataHelper.toggleLightAction(name, pin_number);

        }else if(controlSnapshot.getControlType().equals(blinds_node)){

            int pin_number = controlSnapshot.getDataSnapshot().getValue(SwitchV0.class).getPin_number();
            String name = controlSnapshot.getDataSnapshot().getKey();
            mFirebaseDataHelper.toggleBlindAction(name, pin_number);
        }
    }
}
