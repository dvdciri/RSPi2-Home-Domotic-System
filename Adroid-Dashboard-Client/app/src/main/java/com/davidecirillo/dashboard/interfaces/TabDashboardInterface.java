package com.davidecirillo.dashboard.interfaces;

import com.davidecirillo.dashboard.interfaces.api.BaseViewInterface;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by davidecirillo on 27/04/16.
 */
public interface TabDashboardInterface extends BaseViewInterface {

    void onSettingsReady(HashMap<String, String> settings);

    void onCategoriesReady(ArrayList<String> categories);
}
