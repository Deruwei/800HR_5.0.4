package com.hr.ui.utils;

import android.app.Activity;
import android.content.Context;

import com.hr.ui.view.custom.CustomDialog;

/**
 * Created by wdr on 2017/8/21.
 */

public class RefleshDialogUtils {
    private CustomDialog dialog;
    private Activity context;
    public RefleshDialogUtils(Activity context){
        this.context=context;
    }
    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
    public void hideDialog() {
        if (dialog != null)
            dialog.hide();
    }

    public void showDialog() {
        getDialog().show();
    }

    public CustomDialog getDialog() {
        if (dialog == null) {
            dialog = CustomDialog.instance(context);
            dialog.setCancelable(true);
        }
        return dialog;
    }
}
