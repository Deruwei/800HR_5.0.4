package com.hr.ui.view.payquery;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.hr.ui.R;

import android.util.Log;


public class MyUtils {

	public static String session_key;
	public static final String pre_secret_key = "2cb7G62b2drGdJobcQJ1O8813aV7fds7";
	public static String secret_key = "2cb7G62b2drGdJobcQJ1O8813aV7fds7.800hr.com.800hr.";
	public static String init_secret_key = "2cb7G62b2drGdJobcQJ1O8813aV7fds7.800hr.com.800hr.";
	public static String svr_api_ver;
	public static boolean autoLogin; // 是否由自动登录进入程序
	public static boolean isLogin; // 记录用户是否登录
	public static boolean isFavour; // 用于职位详情页面收藏按钮 登录返回时是否收藏操作
	
	/**
	 * 请求数据的加解密函数
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String AuthCode(String str, String operation, String expiry) throws UnsupportedEncodingException {
		// 动态密匙长度，相同的明文会生成不同密文就是依靠动态密匙
		int ckey_length = 6;

		int keyLen = secret_key.length();
		while(keyLen < 16) {
			secret_key += "8hr";
			keyLen += 3;
			if(keyLen > 16) {
				secret_key = secret_key.substring(0, 16);
			}
		}
		
		// 密匙
		String secret_keyn = md5(secret_key);
		// 密匙a会参与加解密
		String secret_keya = md5(secret_keyn.substring(0, 16)) + secret_key.substring(0, 8);  
		// 密匙b会用来做数据完整性验证
		String secret_keyb = md5(secret_keyn.substring(16, 32)) + secret_key.substring(8, 16);
		// 密匙c用于变化生成的密文
		long currentSecond = 0/*System.currentTimeMillis() / 1000*/;
		
		String secret_keyc = operation.equals("DECODE") ? str.substring(0, ckey_length) : md5(/*"0"*/String.valueOf(currentSecond)).substring(32-ckey_length);
		// 参与运算的密匙
		String cryptkey = secret_keya + md5(secret_keya + secret_keyc);
		int key_length = cryptkey.length();
		// 明文，前10位用来保存时间戳，解密时验证数据有效性，10到26位用来保存$keyb(密匙b)，解密时会通过这个密匙验证数据完整性   
		// 如果是解码的话，会从第$ckey_length位开始，因为密文前$ckey_length位保存 动态密匙，以保证解密正确   
		str = operation.equals("DECODE") ? new String(Base64.decode(str.substring(ckey_length), Base64.DEFAULT), "iso8859-1") : (!expiry.equals("0") ? expiry + currentSecond : "0000000000") + md5(new String(str.getBytes("iso8859-1"), Constants.ENCODE) + secret_keyb).substring(0, 16) + str;
		Log.i("life", "str: "+str);
		int string_length = str.length();
		String result = "";
		int[] box = new int[256];
		for(int i=0; i<256; i++) {
			box[i] = i;
		}
		int[] rndkey = new int[256];
		// 产生密匙簿
		for(int i=0; i<256; i++) {
			rndkey[i] = (int) cryptkey.charAt(i % key_length);
		}
		// 用固定的算法，打乱密匙簿，增加随机性，好像很复杂，实际上对并不会增加密文的强度
		for(int j=0, i=0;i < 256; i++) {
			j = (j + box[i] + rndkey[i]) % 256;
			int temp = box[i];
			box[i] = box[j];
			box[j] = temp;
		}
		
		byte[] resultByte = new byte[string_length];
		// 核心加解密部分 
		for(int a=0, j=0, i=0; i < string_length; i++) {
			a = (a + 1) % 256;
			j = (j + box[a]) % 256;
			int temp = box[a];
			box[a] = box[j];
			box[j] = temp;
			// 从密匙簿得出密匙进行异或，再转成字符
			resultByte[i] = (byte) (str.charAt(i) ^ (box[(box[a] + box[j]) % 256]));
		}
/*		
Log.i("HRService", "keyn=" + secret_keyn);
Log.i("HRService", "keya=" + secret_keya);
Log.i("HRService", "keyb=" + secret_keyb);
Log.i("HRService", "keyc=" + secret_keyc);
Log.i("HRService", "cryptkey=" + cryptkey);
Log.i("HRService", "string=" + str);
Log.i("HRService", "string_length=" + string_length);
		*/
		if(operation.equals("DECODE")) {
         // substr($result, 0, 10) == 0 验证数据有效性   
         // substr($result, 0, 10) - time() > 0 验证数据有效性   
         // substr($result, 10, 16) == substr(md5(substr($result, 26).$keyb), 0, 16) 验证数据完整性   
         // 验证数据有效性，请看未加密明文的格式  
			result = new String(resultByte, "iso8859-1");
			if((Integer.parseInt(result.substring(0, 10)) == 0 || Integer.parseInt(result.substring(0, 10)) - currentSecond > 0) 
					&& result.substring(10, 26).equals(md5(result.substring(26) + secret_keyb).substring(0, 16))) {
				return new String(resultByte, Constants.ENCODE).substring(26);
			} else {
				return "";
			}
		} else {
         // 把动态密匙保存在密文里，这也是为什么同样的明文，生产不同密文后能解密的原因   
         // 因为加密后的密文可能是一些特殊字符，复制过程可能会丢失，所以用base64编码   		
			return secret_keyc + Base64.encodeToString(resultByte, Base64.DEFAULT);
		}
	}
	
	public static String md5(String str) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("md5 : " + e.getMessage());
		}
		md.update(str.getBytes());
		byte[] byteArray = md.digest();  
		  
        StringBuilder md5Str = new StringBuilder();  
  
        for (int i = 0; i < byteArray.length; i++) {              
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)  
                md5Str.append("0").append(Integer.toHexString(0xFF & byteArray[i]));  
            else  
                md5Str.append(Integer.toHexString(0xFF & byteArray[i]));  
        }  
  
        return md5Str.toString();  
	}
	
	public static int parseInt(String value){
		if("".equals(value)){
			return 0;
		}else{
			return Integer.parseInt(value);
		}
	}
	
	public static String valueString(int value){
		if(value <= 0){
			return "";
		}else{
			return String.valueOf(value);
		}
	}
	
	
	
	/**
	 * 根据 error_code 获得错误资源 id
	 * @param error_code
	 * @return
	 */
	public static int getErrorResourceId(int error_code) {
		switch(error_code) {
		case 101 :
			return R.string.error_101;
		case 102 :
			return R.string.error_102;
		case 103 :
			return R.string.error_103;
		case 104 :
			return R.string.error_104;
		case 105 :
			return R.string.error_105;
		case 106 :
			return R.string.error_106;
		case 201 :
			return R.string.error_201;
		case 202 :
			return R.string.error_202;
		case 203 :
			return R.string.error_203;
		case 204 :
			//session key已过期
			return R.string.error_204;
		case 205 :
			return R.string.error_205;
		case 206 :
			return R.string.error_206;
		case 301 :
			return R.string.error_301;
		case 302 :
			return R.string.error_302;
		case 303 :
			return R.string.error_303;
		case 304 :
			return R.string.error_304;
		case 311 :
			return R.string.error_311;
		case 312 :
			return R.string.error_312;
		case 401 :
			return R.string.error_401;
		case 402 :
			return R.string.error_402;
		case 403 :
			return R.string.error_403;
		case 404 :
			return R.string.error_404;
		case 405 :
			return R.string.error_405;
		case 406 :
			return R.string.error_406;
		case 407 :
			return R.string.error_407;
		case 411 :
			return R.string.error_411;
		case 412 :
			return R.string.error_412;
		}
		return R.string.error_101;
	}
}
