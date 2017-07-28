package com.hr.ui.view.payquery;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularUtils {
	public static boolean isEmail(String email) {
		if (email.getBytes().length != email.length()) {
			return false;
		}
		Pattern pattern = Pattern
				.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public static boolean isEnglish(String inStr) {
		if (inStr.getBytes().length != inStr.length()) {
			return false;
		}
		return true;
	}

}
