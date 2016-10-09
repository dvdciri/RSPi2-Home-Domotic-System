package com.davidecirillo.dashboard.app;

import com.davidecirillo.dashboard.data.DataModule;
import com.davidecirillo.dashboard.gui.activity.TabDashboardActivity;
import com.davidecirillo.dashboard.gui.fragment.DataFragment;
import com.davidecirillo.dashboard.receiver.NetworkChangeReceiver;
import com.davidecirillo.dashboard.utils.FirebaseDataHelper;

import javax.inject.Singleton;

import dagger.Component;

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
@Singleton
@Component(modules={AppModule.class, DataModule.class})
public interface DashboardComponent {

    //Activity
    void inject(TabDashboardActivity activity);

    //Fragment
    void inject(DataFragment dataFragment);

    //Helpers
    void inject(FirebaseDataHelper firebaseDataHelper);

    //Receiver
    void inject(NetworkChangeReceiver networkChangeReceiver);
}