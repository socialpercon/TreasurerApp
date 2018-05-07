package com.sora.treasurer.utils;

import com.sora.treasurer.database.entities.ExpenseEntity;

/**
 * Created by Sora on 05/05/2018.
 */

public class DataService {
    public static final int GAIN_TYPE = 1;
    public static final int EXPENSE_TYPE = 0;
    private static int _ExpenseType = 0;
    private static String _ExpenseCategory = "";

    public static final String CATEGORY_FOOD = "Food";
    public static final String CATEGORY_TRANSPORT = "Transport";
    public static final String CATEGORY_RENT = "Rent";
    public static final String CATEGORY_LAUNDRY = "Laundry";
    public static final String CATEGORY_MEDICAL = "Medical";
    public static final String CATEGORY_FITNESS = "Fitness";
    public static final String CATEGORY_GROCERY = "Grocery";
    public static final String CATEGORY_ENTERTAINMENT = "Entertainment";
    public static final String CATEGORY_ELECTRONICS = "Electronics";
    public static final String CATEGORY_OTHERS = "Others";
    public static final String CATEGORY_ATM = "ATM";
    public static final String CATEGORY_SALARY = "Salary";
    public static final String CATEGORY_ALLOWANCE = "Allowance";
    public static final String CATEGORY_GIFT = "Gift";
    public static final String CATEGORY_STOCKS = "Stocks";

    private static ExpenseEntity _ExpenseEntity = null;



    public static int getExpenseType() { return _ExpenseType; }
    public static void setExpenseType(int ExpenseType) { _ExpenseType = ExpenseType; }

    public static String getExpenseCategory() { return _ExpenseCategory; }
    public static void setExpenseCategory(String ExpenseCategory) { _ExpenseCategory = ExpenseCategory; }

    public static ExpenseEntity getExpenseEntity() { return _ExpenseEntity; }
    public static void setExpenseEntity(ExpenseEntity expenseEntity) { _ExpenseEntity = expenseEntity; }
    public static void destroyExpenseEntity() { _ExpenseEntity = null; }
}
