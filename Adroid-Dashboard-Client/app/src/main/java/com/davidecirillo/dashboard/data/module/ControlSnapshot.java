package com.davidecirillo.dashboard.data.module;

import com.firebase.client.DataSnapshot;

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
public class ControlSnapshot {

    private String controlType;
    private DataSnapshot dataSnapshot;

    public ControlSnapshot(String controlType, DataSnapshot dataSnapshot) {
        this.controlType = controlType;
        this.dataSnapshot = dataSnapshot;
    }

    public String getControlType() {
        return controlType;
    }

    public DataSnapshot getDataSnapshot() {
        return dataSnapshot;
    }
}
