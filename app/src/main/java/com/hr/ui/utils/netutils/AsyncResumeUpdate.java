package com.hr.ui.utils.netutils;

import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.hr.ui.activity.MainActivity;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeBaseInfo;
import com.hr.ui.model.ResumeTitle;
import com.hr.ui.utils.datautils.Rc4Md5Utils;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 简历上传/修改
 *
 * @author 800hr:xuebaohua
 */
public class AsyncResumeUpdate {
    private Context context;
    private Handler handlerUploadResume;
    private String baseInfo, languageInfo, resumeInfo;
    private String returnedResumeId;
    private String resumeIdString, resumeLanguageString;
    private Handler handlerService = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String json = (String) msg.obj;
                try {
                    // System.out.println("上传简历-返回数据：" + json);
                    JSONObject jsonObject = new JSONObject(json);
                    int error_code = jsonObject.getInt("error_code");
                    String resume_id = jsonObject.getString("resume_id");
                    switch (error_code) {
                        case 0:// 成功
                            System.out.println("上传成功：" + resumeLanguageString);
                            returnedResumeId = jsonObject.getString("resume_id");
                            DAO_DBOperator dbOperator = new DAO_DBOperator(context);
                            if (baseInfo != null && baseInfo.length() > 0) {
                                // 修改基本信息标志位
                                ResumeBaseInfo baseInfo = dbOperator
                                        .query_ResumePersonInfo_Toone(resumeLanguageString);
                                if (baseInfo != null) {
                                    baseInfo.setIsUpdate(0);
                                    dbOperator.update_ResumePersonInfo(baseInfo);
                                }
                            }
                            if (languageInfo != null && languageInfo.length() > 0) {
                                // 无操作
                            }
                            if (resumeInfo != null && resumeInfo.length() > 0) {
                                // 修改简历信息标志位
                                ResumeTitle resumeTitle = dbOperator
                                        .query_ResumeTitle_info(resumeIdString,
                                                resumeLanguageString);
                                if (resumeTitle != null) {
                                    resumeTitle.setIsUpdate(0);
                                    resumeTitle.setResume_id(returnedResumeId);
                                    dbOperator.update_ResumeList(resumeTitle);
                                }
                                // 如果所上传简历为离线简历，则删除本地离线
                                if ("-1".equals(resumeIdString)) {
                                    dbOperator.Delete_Data(resumeIdString,
                                            resumeLanguageString);
                                    System.out.println("删除本地简历，id=-1");
                                }
                            }


                            if ("zh".equalsIgnoreCase(resumeLanguageString)) {
                                // 中文简历已经上传成功
                                // 统一中英文简历id(如果是离线简历，则在中文简历上传成功后，将返回新id同步到英文简历)
                                unitedId("-1", returnedResumeId);
                                Message message = handlerUploadResume
                                        .obtainMessage();
                                message.what = 0;
                                message.arg1 = Integer.parseInt(returnedResumeId);
                                handlerUploadResume.sendMessage(message);
                            } else if ("en".equalsIgnoreCase(resumeLanguageString)) {
                                // 英文简历已经上传成功
                                handlerUploadResume.sendEmptyMessage(1);
                            }
                            MainActivity.instanceMain.refreshBaseInfo();
                            MainActivity.instanceMain.newAppResume = false;
                            break;
                        default:
                            Toast.makeText(context, Rc4Md5Utils.getErrorResourceId(error_code), Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        ;

    };

    public AsyncResumeUpdate(Context context, Handler handler, String baseInfo,
                             String languageInfo, String resumeInfo, String resumeId,
                             String resumeLanguage) {
        this.context = context;
        this.handlerUploadResume = handler;
        this.baseInfo = baseInfo;
        this.languageInfo = languageInfo;
        this.resumeInfo = resumeInfo;
        this.resumeIdString = resumeId;
        this.resumeLanguageString = resumeLanguage;

    }

    public void execute() {
        try {
            // System.out.println("baseInfo----" + baseInfo);
            // System.out.println("languageInfo----" + languageInfo);
            // System.out.println("resumeInfo----" + resumeInfo);
            DAO_DBOperator dbOperator = new DAO_DBOperator(context);
            ResumeBaseInfo baseInfoObject = dbOperator
                    .query_ResumePersonInfo_Toone(resumeLanguageString);
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", "user_resume.updateresume");
            if (baseInfoObject != null && baseInfoObject.getIsUpdate() == 1) {
                requestParams.put("base_info", baseInfo);
                requestParams.put("language_list", languageInfo);
            }
            requestParams.put("resume_info", resumeInfo);

            NetService service = new NetService(context, handlerService);
            service.execute(requestParams);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步中英文简历id
     *
     * @param newId
     */
    private void unitedId(String oldId, String newId) {
        DAO_DBOperator dbOperator = new DAO_DBOperator(context);
        dbOperator.openDB();
        // 简历列表
        ContentValues values = new ContentValues();
        values.put("resume_id", newId);
        dbOperator.db.update("ResumeList", values, "resume_id=" + oldId, null);

        // 求职意向
        ContentValues values0 = new ContentValues();
        values0.put("resume_id", newId);
        dbOperator.db.update("ResumeCareerObjective", values0, "resume_id="
                + oldId, null);
        // 教育背景
        ContentValues values1 = new ContentValues();
        values1.put("resume_id", newId);
        dbOperator.db.update("ResumeEducation", values1, "resume_id=" + oldId,
                null);
        // 工作经验
        ContentValues values2 = new ContentValues();
        values2.put("resume_id", newId);
        dbOperator.db.update("ResumeWorkExperience", values2, "resume_id="
                + oldId, null);
        // 自我评价
        ContentValues values3 = new ContentValues();
        values3.put("resume_id", newId);
        dbOperator.db.update("ResumeSelfIntroduction", values3, "resume_id="
                + oldId, null);
        // 项目经验
        ContentValues values4 = new ContentValues();
        values4.put("resume_id", newId);
        dbOperator.db.update("ResumeProject", values4, "resume_id=" + oldId,
                null);
        // 专业技能
        ContentValues values5 = new ContentValues();
        values5.put("resume_id", newId);
        dbOperator.db
                .update("ResumeSkill", values5, "resume_id=" + oldId, null);
        // 培训经历
        ContentValues values6 = new ContentValues();
        values6.put("resume_id", newId);
        dbOperator.db.update("ResumeTraining", values6, "resume_id=" + oldId,
                null);

        dbOperator.closeDB();
    }
}