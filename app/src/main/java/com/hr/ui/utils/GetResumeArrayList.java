package com.hr.ui.utils;

import android.content.Context;

import com.hr.ui.R;
import com.hr.ui.bean.SelectBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wdr on 2017/9/4.
 */

public class GetResumeArrayList {
    //从本地获取参加工作年限的list集合
    public static List<SelectBean> getBeginJobListFromArray(Context context){
        List<SelectBean> selectBeen=new ArrayList<>();
        String[] beginJobName=context.getResources().getStringArray(R.array.array_persioninfo_workbeginyear_zh);
        String[] beginJobId=context.getResources().getStringArray(R.array.array_persioninfo_workbeginyear_ids);

        for(int i=0;i<beginJobId.length;i++){
            SelectBean selectBean=new SelectBean();
            selectBean.setId(beginJobId[i]);
            selectBean.setName(beginJobName[i]);
            selectBeen.add(selectBean);
        }

        return selectBeen;
    }
    //从本地获取现有职称的list集合
    public static List<SelectBean> getFuncListFromArray(Context context){
        List<SelectBean> selectBeen=new ArrayList<>();
        String[] funcName= context.getResources().getStringArray(R.array.array_zhicheng_zh);
        String[] funcId=context.getResources().getStringArray(R.array.array_zhicheng_ids);
        for(int i=0;i<funcId.length;i++){
            SelectBean selectBean=new SelectBean();
            selectBean.setId(funcId[i]);
            selectBean.setName(funcName[i]);
            selectBeen.add(selectBean);
        }
        return selectBeen;
    }
    //从本地获取工作性质的list集合
    public static  List<SelectBean> getJobTypeListFromArray(Context context){
        List<SelectBean> selectBeen=new ArrayList<>();
        String[] jobTypeId=context. getResources().getStringArray(R.array.array_jobtype_ids);
        String[] jobTypeName=context.getResources().getStringArray(R.array.array_jobtype_zh);
        for(int i=0;i<jobTypeId.length;i++){
            SelectBean selectBean=new SelectBean();
            selectBean.setId(jobTypeId[i]);
            selectBean.setName(jobTypeName[i]);
            selectBeen.add(selectBean);
        }
        return selectBeen;
    }
    //从本地获取求职状态的list集合
    public static  List<SelectBean> getFindJobListFromArray(Context context){
        List<SelectBean> selectBeen=new ArrayList<>();
        String[] findJobId=context. getResources().getStringArray(R.array.array_workstate_ids);
        String[] findJobName=context.getResources().getStringArray(R.array.array_workstate_zh);
        for(int i=0;i<findJobId.length;i++){
            SelectBean selectBean=new SelectBean();
            selectBean.setId(findJobId[i]);
            selectBean.setName(findJobName[i]);
            selectBeen.add(selectBean);
        }
        return selectBeen;
    }
    //从本地获取学历的list集合
    public static  List<SelectBean> getEduListFromArray(Context context){
        List<SelectBean> selectBeen=new ArrayList<>();
        String[] findJobId=context. getResources().getStringArray(R.array.array_degree_ids);
        String[] findJobName=context.getResources().getStringArray(R.array.array_degree_zh);
        for(int i=0;i<findJobId.length;i++){
            SelectBean selectBean=new SelectBean();
            selectBean.setId(findJobId[i]);
            selectBean.setName(findJobName[i]);
            selectBeen.add(selectBean);
        }
        return selectBeen;
    }
    //从本地获取技能水平的list集合
    public static  List<SelectBean> getLevelFromArray(Context context){
        List<SelectBean> selectBeen=new ArrayList<>();
        String[] levelId=context. getResources().getStringArray(R.array.array_skilllevel_ids);
        String[] levleName=context.getResources().getStringArray(R.array.array_skilllevel_zh);
        for(int i=0;i<levelId.length;i++){
            SelectBean selectBean=new SelectBean();
            selectBean.setId(levelId[i]);
            selectBean.setName(levleName[i]);
            selectBeen.add(selectBean);
        }
        return selectBeen;
    }
    //从本地获取技能水平的list集合
    public static  List<SelectBean> getLaugageTypeListFromArray(Context context){
        List<SelectBean> selectBeen=new ArrayList<>();
        String[] laugageTypeId=context. getResources().getStringArray(R.array.array_language_type_ids);
        String[] laugageTypeName=context.getResources().getStringArray(R.array.array_language_type_zh);
        for(int i=0;i<laugageTypeId.length;i++){
            SelectBean selectBean=new SelectBean();
            selectBean.setId(laugageTypeId[i]);
            selectBean.setName(laugageTypeName[i]);
            selectBeen.add(selectBean);
        }
        return selectBeen;
    }
    //从本地获取听说能力的list集合
    public static  List<SelectBean> getListenSkillFromArray(Context context){
        List<SelectBean> selectBeen=new ArrayList<>();
        String[] listenSkillId=context. getResources().getStringArray(R.array.array_language_speaklevel_ids);
        String[] listenSkillName=context.getResources().getStringArray(R.array.array_language_speaklevel_zh);
        for(int i=0;i<listenSkillId.length;i++){
            SelectBean selectBean=new SelectBean();
            selectBean.setId(listenSkillId[i]);
            selectBean.setName(listenSkillName[i]);
            selectBeen.add(selectBean);
        }
        return selectBeen;
    }
    //从本地获取读写能力的list集合
    public static  List<SelectBean> getReadWriteSkillFromArray(Context context){
        List<SelectBean> selectBeen=new ArrayList<>();
        String[] readWriteSkillId=context. getResources().getStringArray(R.array.array_language_readlevel_ids);
        String[] readWriteSkillName=context.getResources().getStringArray(R.array.array_language_readlevel_zh);
        for(int i=0;i<readWriteSkillId.length;i++){
            SelectBean selectBean=new SelectBean();
            selectBean.setId(readWriteSkillId[i]);
            selectBean.setName(readWriteSkillName[i]);
            selectBeen.add(selectBean);
        }
        return selectBeen;
    }
}
