package com.sora.treasurer.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

    public static long getCurrentDateTimeInMillis(){
        return Calendar.getInstance().getTimeInMillis();
    }
}
