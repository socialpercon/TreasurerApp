package com.sora.treasurer.utils;

/**
 * Created by Sora on 05/05/2018.
 */

public class DataService {
    public static final String GAIN_TYPE = "Gain";
    public static final String EXPENSE_TYPE = "Expense";
    private static String _ExpenseType = "";

    public static String getExpenseType() { return _ExpenseType; }
    public static void setExpenseType(String ExpenseType) { _ExpenseType = ExpenseType; }
}
