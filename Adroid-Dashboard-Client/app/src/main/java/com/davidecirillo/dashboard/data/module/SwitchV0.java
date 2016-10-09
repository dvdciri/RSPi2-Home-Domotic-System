package com.davidecirillo.dashboard.data.module;

/**
 * Created by davidecirillo on 21/04/16.
 */
public class SwitchV0 {

    private int pin_number;
    private int value;
    private String icon;
    private String name;

    public SwitchV0() {
    }

    public int getPin_number() {
        return pin_number;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
