package com.sora.treasurer.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sora on 05/05/2018.
 */

public enum ViewScreen {
    VIEW_DASHBOARD(0),
    VIEW_CREATE_EXPENSE(1),
    VIEW_REPORTS(2),


    VIEW_CREATE_EXPENSE_CATEGORY(3);

    private int value;
    private static Map map = new HashMap<>();

    ViewScreen(int value) {
        this.value = value;
    }

    static {
        for (ViewScreen pageType : ViewScreen.values()) {
            map.put(pageType.value, pageType);
        }
    }

    public static ViewScreen valueOf(int pageType) {
        return (ViewScreen) map.get(pageType);
    }

    public int getValue() {
        return value;
    }
}
