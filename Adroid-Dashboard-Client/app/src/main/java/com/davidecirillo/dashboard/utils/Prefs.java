package com.davidecirillo.dashboard.utils;

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

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Handles SharedPreferences at the application level.
 */
public class Prefs {

    public static int DEFAULT_INT = 0;
    public static long DEFAULT_LONG = 0L;
    public static String DEFAULT_STRING = "";
    public static boolean DEFAULT_BOOLEAN = false;

    /*
    *
    * Preference getters
    *
    */
    public static boolean getBooleanPreference(Context context, int resId, boolean defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(resId), defaultValue);
    }

    public static boolean getBooleanPreference(Context context, int resId) {
        return getBooleanPreference(context, resId, DEFAULT_BOOLEAN);
    }

    public static int getIntPreference(Context context, int resId) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(context.getString(resId), DEFAULT_INT);
    }

    public static int getIntPreference(Context context, int resId, int defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(context.getString(resId), defaultValue);
    }

    public static int getIntPreference(Context context, String prefKey, int defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(prefKey, defaultValue);
    }


    public static String getStringPreference(Context context, int resId, String defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(resId), defaultValue);
    }

    public static String getStringPreference(Context context, String stringKey) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(stringKey, DEFAULT_STRING);
    }

    public static String getStringPreference(Context context, int resId) {
        return getStringPreference(context, resId, DEFAULT_STRING);
    }

    public static Long getLongPreference(Context context, int resId, Long defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(context.getString(resId), defaultValue);
    }

    public static Long getLongPreference(Context context, int resId) {
        return getLongPreference(context, resId, DEFAULT_LONG);
    }

    /*
    *
    * Preference savers
    *
     */
    public static void savePreference(Context context, int resId, int newValue) {
        savePreference(context, context.getString(resId), newValue);
    }

    public static void savePreference(Context context, String prefKey, int newValue) {

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(prefKey, newValue);
        editor.apply();
    }

    public static void savePreference(Context context, int resId, String newValue) {
        savePreference(context, context.getString(resId), newValue);
    }

    public static void savePreference(Context context, String prefKey, String newValue) {

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(prefKey, newValue);
        editor.apply();
    }

    public static void savePreference(Context context, int resId, Boolean newValue) {
        savePreference(context, context.getString(resId), newValue);
    }

    private static void savePreference(Context context, String prefKey, Boolean newValue) {


        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(prefKey, newValue);
        editor.apply();
    }

    public static void savePreference(Context context, int resId, long newValue) {
        savePreference(context, context.getString(resId), newValue);
    }

    private static void savePreference(Context context, String prefKey, long newValue) {

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putLong(prefKey, newValue);
        editor.apply();
    }


    public static void removePreference(Context context, int resId) {
        removePreference(context, context.getString(resId));
    }

    private static void removePreference(Context context, String prefKey) {

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.remove(prefKey);
        editor.apply();
    }

    public static void clearPreferences(Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.clear();
        editor.apply();
    }
}

