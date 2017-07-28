package com.hr.ui.utils.tools;

import android.os.Build;

import com.hr.ui.R;
import com.hr.ui.config.Constants;

/**
 * 
 * @author 800hr:xuebaohua
 * 
 */
public class AndroidUtils {
	/**
	 * 得到系统版本。
	 * 
	 * @return
	 */
	public static String getSDKVer() {
		String osVer = "2.2";
		switch (Build.VERSION.SDK_INT) {
		case Build.VERSION_CODES.BASE:
			osVer = "1.0";
			break;
		case Build.VERSION_CODES.BASE_1_1:
			osVer = "1.1";
			break;
		case Build.VERSION_CODES.CUPCAKE:
			osVer = "1.5";
			break;
		case Build.VERSION_CODES.CUR_DEVELOPMENT: //
			osVer = "1.5.0";
			break;
		case Build.VERSION_CODES.DONUT:
			osVer = "1.6";
			break;
		case Build.VERSION_CODES.ECLAIR:
			osVer = "2.0";
			break;
		case Build.VERSION_CODES.ECLAIR_0_1:
			osVer = "2.0.1";
			break;
		case Build.VERSION_CODES.ECLAIR_MR1:
			osVer = "2.1";
			break;
		case Build.VERSION_CODES.FROYO:
			osVer = "2.2";
			break;
		case 9 /* Build.VERSION_CODES.GINGERBREAD */:
			osVer = "2.3";
			break;
		case 10 /* Build.VERSION_CODES.GINGERBREAD_MR1 */:
			osVer = "2.3.3";
			break;
		case 11 /* Build.VERSION_CODES.HONEYCOMB */:
			osVer = "3.0";
			break;
//		case 12 /* Build.VERSION_CODES.HONEYCOMB_MR1 */:
//			osVer = "3.1";
//			break;
//		case 12 /* Build.VERSION_CODES.HONEYCOMB_MR1 */:
//			osVer = "3.1";
//			break;
//		case 14 /* Build.VERSION_CODES.HONEYCOMB_MR2 */:
//			osVer = "4.0.x";
//			break;
//		case 15/* Build.VERSION_CODES.HONEYCOMB_MR2 */:
//			osVer = "3.2";
//			break;
//		case 16/* Build.VERSION_CODES.HONEYCOMB_MR2 */:
//			osVer = "3.2";
//			break;
//		case 17 /* Build.VERSION_CODES.HONEYCOMB_MR2 */:
//			osVer = "3.2";
//			break;
		}
		return osVer;
	}

	/**
	 * 获取手机硬件信息 ---BRAND:手机系统定制商; DEVICE:设备参数; MODEL:版本
	 * 
	 * @return 手机硬件信息
	 */
	public static String get_model() {
		String model = Build.BRAND + " " + Build.DEVICE + "(" + Build.MODEL
				+ ")";
		return model;
	}

	/**
	 * 获取行业
	 * 
	 * @param industryId
	 * @return
	 */
	public static int getIndustryResourceId(int industryId) {
		switch (industryId) {
		case Constants.INDUSTRY_BANK_ID:
			return R.string.industry_bank;
		case Constants.INDUSTRY_BUILD_ID:
			return R.string.industry_build;
		case Constants.INDUSTRY_CHEN_ID:
			return R.string.industry_chen;
		case Constants.INDUSTRY_CLOTH_ID:
			return R.string.industry_cloth;
		case Constants.INDUSTRY_CNEDU_ID:
			return R.string.industry_cnedu;
		case Constants.INDUSTRY_CNIT_ID:
			return R.string.industry_cnit;
		case Constants.INDUSTRY_CTV_ID:
			return R.string.industry_ctv;
		case Constants.INDUSTRY_ELE_ID:
			return R.string.industry_ele;
		case Constants.INDUSTRY_HEALT_ID:
			return R.string.industry_healt;
		case Constants.INDUSTRY_MEC_ID:
			return R.string.industry_mec;
		}
		return R.string.industry_bank;
	}
}
