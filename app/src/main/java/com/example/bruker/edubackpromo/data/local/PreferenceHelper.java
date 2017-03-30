package com.example.bruker.edubackpromo.data.local;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mtuhus on 01.03.2017.
 */

public class PreferenceHelper {

    public static final String SHARED_PREFS_NAME = "EDUBACK_PREFS";
    public static final String PREF_KEY_IS_STUDENT = "PREF_KEY_IS_STUDENT";
    private final SharedPreferences mPref;

    public PreferenceHelper(Context context) {
        mPref = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }


    public void setIsStudent(boolean isStudent) {
        mPref.edit().putBoolean(PREF_KEY_IS_STUDENT, isStudent).apply();
    }


    public boolean getIsStudent() {
        return mPref.getBoolean(PREF_KEY_IS_STUDENT, true); // Default true
    }
}
