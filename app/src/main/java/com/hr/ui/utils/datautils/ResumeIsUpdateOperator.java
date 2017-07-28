package com.hr.ui.utils.datautils;

import android.content.Context;

import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeBaseInfo;
import com.hr.ui.model.ResumeTitle;


/**
 * 更改简历标志位操作
 */
public class ResumeIsUpdateOperator {

    public static void setBaseInfoIsUpdate(Context context,
                                           DAO_DBOperator dbOperator, String resumeLanguageString) {
        // 标记个人信息已修改
        ResumeBaseInfo resumeBaseInfo = dbOperator
                .query_ResumePersonInfo_Toone(resumeLanguageString);
        if (resumeBaseInfo != null) {
            resumeBaseInfo.setIsUpdate(1);
            boolean isReWrite = dbOperator
                    .update_ResumePersonInfo(resumeBaseInfo);
            if (isReWrite) {
                String S_name = resumeBaseInfo.getName();
            }
        }
    }

    public static void setResumeTitleIsUpdate(Context context,
                                              DAO_DBOperator dbOperator, String resumeIdString,
                                              String resumeLanguageString) {
        // 标记此份简历已修改
        ResumeTitle resumeTitle = dbOperator.query_ResumeTitle_info(
                resumeIdString, resumeLanguageString);
        if (resumeTitle != null) {
            resumeTitle.setIsUpdate(1);
            boolean isReWrite = dbOperator.update_ResumeList(resumeTitle);
        }
    }

}
