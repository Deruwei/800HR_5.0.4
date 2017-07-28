package com.hr.ui.view.payquery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;


public class AndroidUtils {

	/**
	 * @param context
	 */
	public static void alertDialog(Context context, int titleId,
			boolean cancelable, int messageId, int positiveTextId, int negativeTextId, final DialogPositiveInterface dpi) {
		new AlertDialog.Builder(context)
			.setTitle(titleId)
			.setCancelable(cancelable)
			.setMessage(messageId)
			.setPositiveButton(positiveTextId,
				new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialoginterface, int i){
					dpi.click();
			    }
			})
			.setNegativeButton(negativeTextId, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int id) {
//		        	dialog.cancel();
		        }
		    })
		   	.show();

	}
	
	
	/**
	 * 得到系统版本。
	 * @return
	 */
	public static String getSDKVer() {
		String osVer = "2.2";
		switch(Build.VERSION.SDK_INT) {
		case Build.VERSION_CODES.BASE :
			osVer = "1.0";
			break;
		case Build.VERSION_CODES.BASE_1_1 :
			osVer = "1.1";
			break;
		case Build.VERSION_CODES.CUPCAKE :
			osVer = "1.5";
			break;
		case Build.VERSION_CODES.CUR_DEVELOPMENT : // �ǹٷ������汾��������1.5.0 ����
			osVer = "1.5.0";
			break;
		case Build.VERSION_CODES.DONUT :
			osVer = "1.6";
			break;
		case Build.VERSION_CODES.ECLAIR :
			osVer = "2.0";
			break;
		case Build.VERSION_CODES.ECLAIR_0_1 :
			osVer = "2.0.1";
			break;
		case Build.VERSION_CODES.ECLAIR_MR1 :
			osVer = "2.1";
			break;
		case Build.VERSION_CODES.FROYO :
			osVer = "2.2";
			break;
		case 9 /*Build.VERSION_CODES.GINGERBREAD*/: // ������ 2.2 �� sdk ���������Ժ���ĳ�ֵ�� 2.2 ��û��
			osVer = "2.3";
			break;
		case 10 /*Build.VERSION_CODES.GINGERBREAD_MR1*/:
			osVer = "2.3.3";
			break;
		case 11 /*Build.VERSION_CODES.HONEYCOMB*/:
			osVer = "3.0";
			break;
		case 12 /*Build.VERSION_CODES.HONEYCOMB_MR1*/:
			osVer = "3.1";
			break;
		case 13 /*Build.VERSION_CODES.HONEYCOMB_MR2*/:
			osVer = "3.2";
			break;
		}
		return osVer;
	}

	/**
	 * @return
	 */
	public static String get_model() {
		String model = Build.BRAND+" "+Build.DEVICE+"("+Build.MODEL+")";
		return model;
	}
}
