package com.sora.treasurer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.sora.treasurer.enums.ViewScreen;

/**
 * Created by Sora on 05/05/2018.
 */

public class TRPref {

    private static final String PREF_CURRENT_SCREEN                 = "CURRENT_SCREEN";
    private static final String PREF_ONE_OFF_SCREEN                 = "ONE OFF SCREEN";

    public static void setCurrentScreen(Context context, ViewScreen screenType) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(PREF_CURRENT_SCREEN, screenType.getValue()).apply();
    }

    public static ViewScreen getCurrentScreen(Context context) {
        return ViewScreen.valueOf(PreferenceManager.getDefaultSharedPreferences(context).
                getInt(PREF_CURRENT_SCREEN, ViewScreen.VIEW_DASHBOARD.getValue()));
    }


    public static void setOneOffScreen(Context context, ViewScreen screenType) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(PREF_ONE_OFF_SCREEN, screenType == null ? -1 : screenType.getValue()).apply();
    }

    public static ViewScreen getOneOffScreen(Context context) {
        int screenInt = PreferenceManager.
                getDefaultSharedPreferences(context).getInt(PREF_ONE_OFF_SCREEN, -1);
        return (screenInt < 0 ? null : ViewScreen.valueOf(screenInt));
    }
}
