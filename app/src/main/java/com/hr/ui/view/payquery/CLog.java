package com.hr.ui.view.payquery;

import android.util.Log;

public class CLog {
	public static void e(String tag, String mess) {
		if (Constants.TAG_OUTPUT) {
			Log.e(tag, mess);
		}
	}

	public static void i(String tag, String mess) {
		if (Constants.TAG_OUTPUT) {
			Log.i(tag, mess);
		}
	}

	public static void d(String tag, String mess) {
		if (Constants.TAG_OUTPUT) {
			Log.d(tag, mess);
		}
	}
}
