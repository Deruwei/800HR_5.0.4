package com.hr.ui.view.payquery;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class OperatorSharedPreference {

	Context context;
	SharedPreferences sharedPreferences;

	@SuppressWarnings("static-access")
	public OperatorSharedPreference(Context context) {
		this.context = context;
		sharedPreferences = context.getSharedPreferences("payquery",
				context.MODE_PRIVATE);
	}

	public void writeToShared(String key, String value) {
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
		editor = null;
	}

	public String readFromShared(String key) {
		return sharedPreferences.getString(key, null);
	}

	public boolean clear() {
		Editor editor = sharedPreferences.edit();
		editor.clear();
		return editor.commit();
	}

	public boolean clearone(String key) {
		Editor editor = sharedPreferences.edit();
		editor.remove(key);
		return editor.commit();
	}
}
