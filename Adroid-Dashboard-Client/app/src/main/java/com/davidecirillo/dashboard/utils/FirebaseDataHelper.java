package com.davidecirillo.dashboard.utils;

import android.content.Context;
import android.util.Log;

import com.davidecirillo.dashboard.app.AppConfig;
import com.davidecirillo.dashboard.app.DashboardApplication;
import com.davidecirillo.dashboard.data.RestService;
import com.davidecirillo.dashboard.data.module.ControlSnapshot;
import com.davidecirillo.dashboard.interfaces.DataFragmentInterface;
import com.davidecirillo.dashboard.interfaces.TabDashboardInterface;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by davidecirillo on 27/04/16.
 */
public class FirebaseDataHelper {

    private static final String FIREBASE_DATA_HELPER_KEY = "DATA_HELPER_KEY";

    private Context mContext;

    @Inject
    NetworkUtils mNetworkUtils;

    //Interfaces
    private TabDashboardInterface mDashboardInterface;
    private DataFragmentInterface mDataFragmentInterface;

    public FirebaseDataHelper(Context context, NetworkUtils networkUtils) {
        mContext = context;
        mNetworkUtils = networkUtils;
        Firebase.setAndroidContext(mContext);

        DashboardApplication.getAppComponent().inject(this);
    }

    public void setDataFragmentInterface(DataFragmentInterface mDataFragmentInterface) {
        this.mDataFragmentInterface = mDataFragmentInterface;
    }

    public void setDashboardInterface(TabDashboardInterface mDashboardInterface) {
        this.mDashboardInterface = mDashboardInterface;
    }

    /**
     * Load control in tabs
     */
    public void init() {
        if (mDashboardInterface != null)
            mDashboardInterface.showLoading(true);

        //Load settings and categories
        Firebase mFirebase = new Firebase(AppConfig.firebase_base_reference + AppConfig.controls_node);
        mFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<String> categories = new ArrayList<>();

                if (mDashboardInterface != null) {
                    for (DataSnapshot childControls : dataSnapshot.getChildren()) {
                        categories.add(childControls.getKey());
                    }

                    //Add hardcoded pi category
                    categories.add(AppConfig.pi_node);

                    mDashboardInterface.onCategoriesReady(categories);
                }

                if (mDashboardInterface != null)
                    mDashboardInterface.showLoading(false);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                throw new IllegalStateException(firebaseError.getMessage());
            }
        });
    }

    public void loadSettings() {

        if (mDashboardInterface != null)
            mDashboardInterface.showLoading(true);

        //Load settings and categories
        Firebase mFirebase = new Firebase(AppConfig.firebase_base_reference + AppConfig.settings_node);
        mFirebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<String, String> settingsList = new HashMap<>();

                for (DataSnapshot childSetting : dataSnapshot.getChildren()) {

                    //For all of them we'll add into an hashmap and lod the settings
                    settingsList.put(childSetting.getKey(), ((String) childSetting.getValue()));

                    //And save them to the shared preference
                    Prefs.savePreference(mContext, childSetting.getKey(), (String) childSetting.getValue());


                    //Update the rest service with the new address
                    if (childSetting.getKey().equals(AppConfig.pi_local_address_item)) {

                        RestService.getInstance().updatePiLocalAddress(((String) childSetting.getValue()));

                    } else if (childSetting.getKey().equals(AppConfig.pi_external_address_item)) {

                        RestService.getInstance().updatePiRemoteAddress(((String) childSetting.getValue()));
                    }
                }

                //Now we've got all ip address we can create the service
                RestService.getInstance().create();

                if (mDashboardInterface != null)
                    mDashboardInterface.onSettingsReady(settingsList);

                if (mDashboardInterface != null)
                    mDashboardInterface.showLoading(false);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                throw new IllegalStateException(firebaseError.getMessage());
            }
        });

    }

    public void loadDataControl(String baseUrl, final String category) {

        if (mDataFragmentInterface != null) {

            mDataFragmentInterface.showLoading(true);

            String reference = baseUrl + category;
            Firebase mControlFirebase = new Firebase(reference);

            mControlFirebase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    mDataFragmentInterface.showLoading(false);

                    int c = 0;
                    for (DataSnapshot content : dataSnapshot.getChildren()) {
                        mDataFragmentInterface.onNewControl(new ControlSnapshot(category, content), c == 0);
                        c++;
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    throw new IllegalStateException(firebaseError.getMessage());
                }
            });
        } else throw new IllegalStateException("No Data Fragment interface attached");
    }

    public void toggleLightAction(String name, int pin_number) {

        if (mDataFragmentInterface != null) {

            mDataFragmentInterface.showLoading(true);

            Observable<String> toggleLight = null;
            if (mNetworkUtils.isInPILocalNetwork()) {
                toggleLight = RestService.getInstance().getLocalPiApi().toggleLight(
                        name,
                        pin_number
                );
            } else {
                toggleLight = RestService.getInstance().getRemotePiApi().toggleLight(
                        name,
                        pin_number
                );
            }

            toggleLight
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {
                            mDataFragmentInterface.showLoading(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e != null)
                                Log.e("APP", e.getMessage());

                            mDataFragmentInterface.showLoading(false);
                            mDataFragmentInterface.onActionResult("Something went wrong or PI not connected");
                        }

                        @Override
                        public void onNext(String result) {
                            mDataFragmentInterface.onActionResult(result);
                        }
                    });
        }
    }

    public void toggleBlindAction(String name, int pin_number) {

        if (mDataFragmentInterface != null) {

            mDataFragmentInterface.showLoading(true);

            Observable<String> toggleBlind = null;
            if (mNetworkUtils.isInPILocalNetwork()) {
                toggleBlind = RestService.getInstance().getLocalPiApi().toggleBlind(
                        name,
                        pin_number
                );
            } else {
                toggleBlind = RestService.getInstance().getRemotePiApi().toggleBlind(
                        name,
                        pin_number
                );
            }

            toggleBlind
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {
                            mDataFragmentInterface.showLoading(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e != null)
                                Log.e("APP", e.getMessage());

                            mDataFragmentInterface.showLoading(false);
                            mDataFragmentInterface.onActionResult("Something went wrong or PI not connected");
                        }

                        @Override
                        public void onNext(String result) {
                            mDataFragmentInterface.onActionResult(result);
                        }
                    });
        }
    }
}
