package com.hr.ui.utils.datautils;

import android.os.Environment;
	/**
	 * result[0]:是否可用
	 * result[1]:可读写状态-->true：可读可写 ，false：只读
	 */
public class TestSD {

public  boolean[] getState() {

	boolean result[]=new boolean[]{false,false};
		String state=Environment.getExternalStorageState();
		if(Environment.MEDIA_MOUNTED.equals(state)){
			result[0]=result[1]=true;
		}else if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
			result[0]=true;
			result[1]=false;
		}else{
			result[0]=result[1]=false;
		}
		return result;
}


}
