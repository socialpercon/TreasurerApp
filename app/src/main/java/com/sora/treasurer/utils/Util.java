package com.sora.treasurer.utils;

import android.content.Context;
import android.view.View;

import com.sora.treasurer.R;
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

    public static String formatDateTimeGetHourMinute(long millis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        return simpleDateFormat.format(millis);
    }


    public static String formatDateTimeGetDay(long millis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
        return simpleDateFormat.format(millis);
    }

    public static String formatDateTimeGetDayText(long millis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE");
        return simpleDateFormat.format(millis);
    }

    public static String formatDateTimeGetMonth(long millis){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM");
        return simpleDateFormat.format(millis);
    }

    public static String formatDateTimeGetYear(long millis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        return simpleDateFormat.format(millis);
    }

    public static String formatDateTimeGetMonthAndYear(long millis) {
        return formatDateTimeGetMonth(millis) + " " +formatDateTimeGetYear(millis);
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
            case VIEW_CREATE_EXPENSE_CATEGORY:
                eFragment = TRFragment.TR_CREATE_EXPENSE_CATEGORY_FRAG;
                break;
            case VIEW_CREATE_EXPENSE_FORM:
                eFragment = TRFragment.TR_CREATE_EXPENSE_CATEGORY_FORM_FRAG;
                break;
        }
        return eFragment;
    }

    public static int convertDPtoPixel(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (100 * scale + 0.5f);
    }

    public static int getCategoryImageResource(Context context, String category) {
        int resourceId = 0;
        switch(category){
            case DataService.CATEGORY_ALLOWANCE:
                resourceId = R.drawable.ic_gain_allowance;
                break;
            case DataService.CATEGORY_ATM:
                resourceId = R.drawable.ic_gain_atm_card;
                break;
            case DataService.CATEGORY_ELECTRONICS:
                resourceId = R.drawable.ic_expense_electronics;
                break;
            case DataService.CATEGORY_ENTERTAINMENT:
                resourceId = R.drawable.ic_expense_entertainment;
                break;
            case DataService.CATEGORY_FITNESS:
                resourceId = R.drawable.ic_expense_fitness;
                break;
            case DataService.CATEGORY_FOOD:
                resourceId = R.drawable.ic_expense_food;
                break;
            case DataService.CATEGORY_GIFT:
                resourceId = R.drawable.ic_gain_gift;
                break;
            case DataService.CATEGORY_GROCERY:
                resourceId = R.drawable.ic_expense_grocery;
                break;
            case DataService.CATEGORY_LAUNDRY:
                resourceId = R.drawable.ic_expense_laundry;
                break;
            case DataService.CATEGORY_MEDICAL:
                resourceId = R.drawable.ic_expense_medical;
                break;
            case DataService.CATEGORY_OTHERS:
                resourceId = R.drawable.ic_expense_others;
                break;
            case DataService.CATEGORY_RENT:
                resourceId = R.drawable.ic_expense_rent;
                break;
            case DataService.CATEGORY_SALARY:
                resourceId = R.drawable.ic_gain_salary;
                break;
            case DataService.CATEGORY_STOCKS:
                resourceId = R.drawable.ic_gain_stocks;
                break;
            case DataService.CATEGORY_TRANSPORT:
                resourceId = R.drawable.ic_expense_transpo;
                break;
        }
        return resourceId;
    }
}
