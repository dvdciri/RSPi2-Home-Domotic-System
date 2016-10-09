package com.davidecirillo.dashboard.app;

import android.app.Application;

import com.davidecirillo.dashboard.data.DataModule;

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
public class DashboardApplication extends Application {

    static DashboardComponent mDashboardComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mDashboardComponent = DaggerDashboardComponent.builder()
                .appModule(new AppModule(this))
                .dataModule(new DataModule())
                .build();

    }

    public static DashboardComponent getAppComponent() {
        return mDashboardComponent;
    }
}
