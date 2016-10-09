package com.davidecirillo.dashboard.data.module;

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
public class SensorV0{

    String current_inside;
    String max_inside;
    String min_inside;
    private String name;

    public SensorV0() {
    }

    public String getCurrent_inside() {
        return current_inside;
    }

    public String getMax_inside() {
        return max_inside;
    }

    public String getMin_inside() {
        return min_inside;
    }

    public void setCurrent_inside(String current_inside) {
        this.current_inside = current_inside;
    }

    public void setMax_inside(String max_inside) {
        this.max_inside = max_inside;
    }

    public void setMin_inside(String min_inside) {
        this.min_inside = min_inside;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
