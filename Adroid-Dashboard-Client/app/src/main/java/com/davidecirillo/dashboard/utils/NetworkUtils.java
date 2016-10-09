package com.davidecirillo.dashboard.utils;

import android.content.Context;

import com.davidecirillo.dashboard.R;
import com.davidecirillo.dashboard.app.AppConfig;

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
public class NetworkUtils {

    Context mContext;

    public NetworkUtils(Context mContext) {
        this.mContext = mContext;
    }

    public boolean isInPILocalNetwork(){
        return Prefs.getBooleanPreference(mContext, R.string.pref_is_in_local_lan, false);
    }

    public void setIsInPiLocalNetwork(boolean set){
        Prefs.savePreference(mContext, R.string.pref_is_in_local_lan, set);
    }

    public String getLocalUrl(){
        return AppConfig.http + Prefs.getStringPreference(mContext, AppConfig.pi_local_address_item);
    }

    public String getRemoteUrl(){
        return AppConfig.http + Prefs.getStringPreference(mContext, AppConfig.pi_external_address_item);
    }
}
