package com.hr.ui.utils.tools;

import com.hr.ui.model.MoreAccountInfo;
import com.hr.ui.model.MoreAccountInfoResumeItem;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * 多账号绑定中，解析json
 *
 * @author 800hr:xuebaohua
 */
public class MoreAccountBindJsonParse {
    public MoreAccountBindJsonParse() {
        // TODO Auto-generated constructor stub
    }

    public ArrayList<MoreAccountInfo> getMoAccountInfo(String jsonString) {
        if (jsonString == null || jsonString.trim().length() == 0) {
            return null;
        }

        try {
            ArrayList<MoreAccountInfo> lisAccountInfos = new ArrayList<MoreAccountInfo>();
            // 遍历每个行业信息
            JSONArray itemArray = new JSONArray(jsonString);
            for (int i = 0; i < itemArray.length(); i++) {
                MoreAccountInfo moreAccountInfo = new MoreAccountInfo();
                JSONObject jsonObject = itemArray.getJSONObject(i);
                String user_id = jsonObject.getString("user_id");
                moreAccountInfo.setUser_id(user_id);
                String account_name = jsonObject.getString("account_name");
                moreAccountInfo.setAccount_name(account_name);
                String resume_list = jsonObject.getString("resume_list");
                if (resume_list != null && resume_list.length() > 0) {
                    JSONArray resumeArray = new JSONArray(resume_list);
                    // 遍历该行业下的简历信息
                    for (int j = 0; j < resumeArray.length(); j++) {
                        JSONObject resumeObject = resumeArray.getJSONObject(j);
                        MoreAccountInfoResumeItem resumeItem = new MoreAccountInfoResumeItem();
                        resumeItem.setTitle(resumeObject.getString("title"));
                        resumeItem.setFill_scale(resumeObject
                                .getString("fill_scale"));
                        moreAccountInfo.getResume_list().add(resumeItem);
                    }
                }
                lisAccountInfos.add(moreAccountInfo);
            }
            return lisAccountInfos;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
