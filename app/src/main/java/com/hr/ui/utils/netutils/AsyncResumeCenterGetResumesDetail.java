package com.hr.ui.utils.netutils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.utils.datautils.Rc4Md5Utils;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 获取简历详细信息
 */
public class AsyncResumeCenterGetResumesDetail {
    private Context context;
    private String resumeLanguage;
    private String resumeId;
    private Handler handler;
    private JSONObject resumeJsonObject;// 获取到的简历
    private Handler handlerService = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String json = (String) msg.obj;
                try {
                    resumeJsonObject = new JSONObject(json);
                    int error_code = resumeJsonObject.getInt("error_code");

                    switch (error_code) {
                        case 0:// 成功
                            DAO_DBOperator dbOperator = new DAO_DBOperator(context);
                            ResumeOperatorDB resumeOperatorDB = new ResumeOperatorDB(dbOperator, resumeJsonObject);
                            resumeOperatorDB.updateResumeTitle(resumeId, resumeLanguage);
                            resumeOperatorDB.updateResumeOrder(resumeId, resumeLanguage);
                            resumeOperatorDB.updateResumeExperienceList(resumeId, resumeLanguage);
                            resumeOperatorDB.updateResumeEducationList(resumeId, resumeLanguage);
                            resumeOperatorDB.updateResumeAssessInfo(resumeId, resumeLanguage);
                            resumeOperatorDB.updateResumeProject(resumeId, resumeLanguage);
                            resumeOperatorDB.updateResumePlantList(resumeId, resumeLanguage);
                            resumeOperatorDB.updateResumeSkillList(resumeId, resumeLanguage);
                            resumeOperatorDB.updateResumeLanguageLevelList(resumeId, resumeLanguage);
                            if (handler != null) {
                                Message message = handler.obtainMessage();
                                message.obj = resumeJsonObject;
                                handler.sendMessage(message);
                            }
                            break;
                        default:
                            if (error_code == 304) {// 简历不存在
                                break;
                            }
                            Toast.makeText(context,
                                    Rc4Md5Utils.getErrorResourceId(error_code),
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        ;

    };

    public AsyncResumeCenterGetResumesDetail(Context context, Handler handler, String resumeId, String resumeLanguage) {
        this.context = context;
        this.handler = handler;
        this.resumeId = resumeId;
        this.resumeLanguage = resumeLanguage;

    }

    public void execute() {
        try {
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", "user_resume.resumeget");
            requestParams.put("resume_id", resumeId);
            requestParams.put("resume_language", resumeLanguage);
            NetService service = new NetService(context, handlerService);
            service.execute(requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}