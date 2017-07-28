package com.hr.ui.utils.tools;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

public class InvisibleKeyboard {
    private boolean mHasInit = false;
    private boolean mHasKeyboard = false;
    private int mHeight;

    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity
                    .getCurrentFocus().getWindowToken(), 0);
        }
    }

}
