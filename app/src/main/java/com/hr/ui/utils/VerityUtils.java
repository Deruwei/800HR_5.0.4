package com.hr.ui.utils;

import android.text.BoringLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wdr on 2017/10/26.
 */

public class VerityUtils {
    public static boolean verityIdentityCard(String s){
        Pattern pattern=Pattern.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");
        Pattern pattern1=pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$");
        Matcher matcher=pattern.matcher(s);
        Matcher matcher1=pattern1.matcher(s);
        if(matcher.matches()||matcher1.matches()){
            return true;
        }
        return false;
    }
    public static boolean verityHuZhao(String s){
        Pattern pattern=Pattern.compile("^[a-zA-Z]{5,17}$");
        Pattern pattern1=pattern.compile("^[a-zA-Z0-9]{5,17}$");
        Matcher matcher=pattern.matcher(s);
        Matcher matcher1=pattern1.matcher(s);
        if(matcher.matches()||matcher1.matches()){
            return true;
        }
        return false;
    }
    public static boolean verityArmy(String s){
        Pattern pattern=Pattern.compile("^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$");
        Pattern pattern1=pattern.compile("^[a-zA-Z0-9]{5,17}$");
        Matcher matcher=pattern.matcher(s);
        Matcher matcher1=pattern1.matcher(s);
        if(matcher.matches()||matcher1.matches()){
            return true;
        }
        return false;
    }
}
