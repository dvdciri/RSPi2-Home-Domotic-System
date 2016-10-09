package com.davidecirillo.dashboard.gui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.davidecirillo.dashboard.R;
import com.davidecirillo.dashboard.app.AppConfig;
import com.davidecirillo.dashboard.app.DashboardApplication;
import com.davidecirillo.dashboard.gui.adapter.ViewPagerAdapter;
import com.davidecirillo.dashboard.gui.fragment.DataFragment;
import com.davidecirillo.dashboard.interfaces.TabDashboardInterface;
import com.davidecirillo.dashboard.receiver.NetworkChangeReceiver;
import com.davidecirillo.dashboard.utils.FirebaseDataHelper;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabDashboardActivity extends AppCompatActivity implements TabDashboardInterface {


    @BindView(R.id.main_view_switcher)
    ViewSwitcher mMainViewSwitcher;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.last_update_time)
    public TextView mLastUpdateDateTime;

    @Inject
    FirebaseDataHelper mFirebaseDataHelper;

    NetworkChangeReceiver mNetworkChangeReceiver;

    private HashMap<String, String> settingsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        DashboardApplication.getAppComponent().inject(this);

        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mFirebaseDataHelper.setDashboardInterface(this);
        mFirebaseDataHelper.init();
        mFirebaseDataHelper.loadSettings();
    }

    @Override
    public void showLoading(boolean loading) {
        mMainViewSwitcher.setDisplayedChild(loading ? 0 : 1);
    }

    @Override
    public void onSettingsReady(HashMap<String, String> settings) {
        //Load settings into new activity
        settingsList = settings;

        mLastUpdateDateTime.setText(getString(R.string.last_update_string, settings.get(AppConfig.last_update_item)));
    }

    @Override
    public void onCategoriesReady(ArrayList<String> categories) {

        //Set up adapter with categories
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (String category : categories){
            adapter.addFragment(DataFragment.getInstance(category), category);
        }
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        //Show tabs
        mTabLayout.setVisibility(View.VISIBLE);

        this.showLoading(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                if (settingsList != null) {
                    startActivity(SettingsActivity.getSettingsActivity(getApplicationContext(), settingsList));
                }
                break;
        }
        return true;
    }
}
