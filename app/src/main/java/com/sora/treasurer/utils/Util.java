package com.sora.treasurer.utils;

import android.view.View;

import com.sora.treasurer.enums.TRFragment;
import com.sora.treasurer.enums.ViewScreen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Sora on 22/04/2018.
 */

public class Util {
    public static String convertDateToString(long date){
        return new SimpleDateFormat("MM-dd-yyy").format(date);
    }
    public static String getCurrentDate(){
        return convertDateToString(Calendar.getInstance().getTimeInMillis());
    }

    public static long getCurrentDateInMillis() {
        try {
            return new SimpleDateFormat("MM-dd-yyyy").parse(getCurrentDate()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String formatDateTime(long millis){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd,yyy hh:mm a");
        return simpleDateFormat.format(millis);
    }

    public static String formatDateTimeGetDay(long millis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
        return simpleDateFormat.format(millis);
    }

    public static String formatDateTimeGetMonth(long millis){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
        return simpleDateFormat.format(millis);
    }

    public static String formatDateTimeGetMonthAndDay(long millis) {
        return formatDateTimeGetMonth(millis) + " " +formatDateTimeGetDay(millis);
    }
    public static long getCurrentDateTimeInMillis(){
        return Calendar.getInstance().getTimeInMillis();
    }

    public static List<Date> getDaysOfCurrentWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.getInstance().getFirstDayOfWeek());
        List<Date> daysOfWeek = new ArrayList<Date>();
        for (int i = 0; i < 7; i++) {
            daysOfWeek.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return daysOfWeek;
    }

    public static TRFragment getFramentByScreen(ViewScreen screen) {
        TRFragment eFragment = null;
        switch(screen) {
            case VIEW_DASHBOARD:
                eFragment = TRFragment.TR_DASHBOARD_FRAG;
                break;
            case VIEW_CREATE_EXPENSE:
                eFragment = TRFragment.TR_CREATE_FRAG;
                break;
            case VIEW_REPORTS:
                eFragment = TRFragment.TR_REPORT_FRAG;
                break;
        }
        return eFragment;
    }
}
