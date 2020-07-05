package com.example.dogsapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class SharedPreferenceHelper {
    public static final String PREF_TIME="Pref_time";
    public static SharedPreferenceHelper instance;
    private SharedPreferences prefs;
    private SharedPreferenceHelper(Context context)
    {
        prefs= PreferenceManager.getDefaultSharedPreferences(context);
    }
    public static SharedPreferenceHelper getInstance(Context context)
    {
        if(instance==null)
        {
            instance=new SharedPreferenceHelper(context);
        }
        return instance;
    }
    public void saveUpdateTime(long time)
    {
        prefs.edit().putLong(PREF_TIME,time).apply();
    }
    public long getUpdateTime()
    {
        return prefs.getLong(PREF_TIME,0);
    }
}
