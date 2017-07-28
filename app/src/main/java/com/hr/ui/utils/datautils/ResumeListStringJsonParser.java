package com.hr.ui.utils.datautils;

/**
 * 作者：Colin
 * 日期：2016/1/16 14:37
 * 邮箱：bestxt@qq.com
 */

import com.hr.ui.model.ResumeBaseInfo;
import com.hr.ui.model.ResumeLanguageLevel;
import com.hr.ui.model.ResumeList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * 简历列表信息解析
 *
 * @author 800hr:xuebaohua
 */
public class ResumeListStringJsonParser {

    public ResumeListStringJsonParser() {
    }

    /**
     * @return 个人基本信息
     */
    public ArrayList<ResumeBaseInfo> getBaseInfos(JSONArray baseinfoJsonArray) {
        ArrayList<ResumeBaseInfo> list = new ArrayList<ResumeBaseInfo>();
        for (int i = 0; i < baseinfoJsonArray.length(); i++) {
            try {
                JSONObject baseinfoJsonObject = baseinfoJsonArray.getJSONObject(i);
                ResumeBaseInfo baseInfo = new ResumeBaseInfo();
                baseInfo.setUser_id(baseinfoJsonObject.getString("user_id"));
                baseInfo.setName(baseinfoJsonObject.getString("name"));
                baseInfo.setSex(baseinfoJsonObject.getString("sex"));
                baseInfo.setYear(baseinfoJsonObject.getString("year"));
                baseInfo.setMonth(baseinfoJsonObject.getString("month"));
                baseInfo.setDay(baseinfoJsonObject.getString("day"));
                baseInfo.setHeight(baseinfoJsonObject.getString("height"));
                baseInfo.setNationality(baseinfoJsonObject.getString("nationality"));
                baseInfo.setHukou(baseinfoJsonObject.getString("hukou"));
                baseInfo.setIdnumber(baseinfoJsonObject.getString("idnumber"));
                baseInfo.setCardtype(baseinfoJsonObject.getString("cardtype"));
                baseInfo.setMarriage(baseinfoJsonObject.getString("marriage"));
                baseInfo.setWork_beginyear(baseinfoJsonObject.getString("work_beginyear"));
                baseInfo.setLocation(baseinfoJsonObject.getString("location"));
                baseInfo.setEmailaddress(baseinfoJsonObject.getString("emailaddress"));
                baseInfo.setAddress(baseinfoJsonObject.getString("address"));
                baseInfo.setZipcode(baseinfoJsonObject.getString("zipcode"));
                baseInfo.setHomepage(baseinfoJsonObject.getString("homepage"));
                baseInfo.setResume_language(baseinfoJsonObject.getString("resume_language"));
                baseInfo.setPost_rank(baseinfoJsonObject.getString("post_rank"));
                baseInfo.setPolity(baseinfoJsonObject.getString("polity"));
                baseInfo.setBlood(baseinfoJsonObject.getString("blood"));
                baseInfo.setPic_filekey(baseinfoJsonObject.getString("pic_filekey"));
                baseInfo.setYdphone(baseinfoJsonObject.getString("ydphone"));
                baseInfo.setTelephone(baseinfoJsonObject.getString("telephone"));
                baseInfo.setIm_type(baseinfoJsonObject.getString("im_type"));
                baseInfo.setIm_account(baseinfoJsonObject.getString("im_account"));
                baseInfo.setModify_time(baseinfoJsonObject.getString("modify_time"));
                baseInfo.setCurrent_workstate(baseinfoJsonObject.getString("current_workstate"));
                baseInfo.setYdphone_verify_status(baseinfoJsonObject.getString("ydphone_verify_status"));
                list.add(baseInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * @return 语言能力信息
     */
    public ArrayList<ResumeLanguageLevel> getLanguageLevels(
            JSONArray languageinfoJsonArray) {
        ArrayList<ResumeLanguageLevel> list = new ArrayList<ResumeLanguageLevel>();
        for (int i = 0; i < languageinfoJsonArray.length(); i++) {
            try {
                JSONObject languageObject = languageinfoJsonArray
                        .getJSONObject(i);
                ResumeLanguageLevel resumeLanguageLevel = new ResumeLanguageLevel();
                resumeLanguageLevel.setLangname(languageObject
                        .getString("langname"));
                resumeLanguageLevel.setUser_id(languageObject
                        .getString("user_id"));
                resumeLanguageLevel.setRead_level(languageObject
                        .getString("read_level"));
                resumeLanguageLevel.setSpeak_level(languageObject
                        .getString("speak_level"));
                list.add(resumeLanguageLevel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * @return 简历列表信息
     */
    public ArrayList<ResumeList> getResumeLists(JSONArray resumeArray) {
        ArrayList<ResumeList> list = new ArrayList<ResumeList>();
        for (int i = 0; i < resumeArray.length(); i++) {
            try {

                JSONObject resumeListoObject = resumeArray.getJSONObject(i);
                ResumeList resumeList = new ResumeList();
                if (resumeListoObject.has("user_id")) {
                    resumeList.setUser_id(resumeListoObject
                            .getString("user_id"));
                }
                if (resumeListoObject.has("resume_id")) {
                    resumeList.setResume_id(resumeListoObject
                            .getString("resume_id"));
                }
                if (resumeListoObject.has("open")) {
                    resumeList.setOpen(resumeListoObject.getString("open"));
                }
                if (resumeListoObject.has("uptime")) {
                    resumeList.setUptime(resumeListoObject.getString("uptime"));
                }
                if (resumeListoObject.has("castbehalf")) {
                    resumeList.setCastbehalf(resumeListoObject
                            .getString("castbehalf"));
                }
                if (resumeListoObject.has("important")) {
                    resumeList.setImportant(resumeListoObject
                            .getString("important"));
                }
                if (resumeListoObject.has("func")) {
                    resumeList.setFunc(resumeListoObject.getString("func"));
                }
                if (resumeListoObject.has("jobtype")) {
                    resumeList.setJobtype(resumeListoObject
                            .getString("jobtype"));
                }
                if (resumeListoObject.has("order_salary")) {
                    resumeList.setOrder_salary(resumeListoObject
                            .getString("order_salary"));
                }
                if (resumeListoObject.has("add_time")) {
                    resumeList.setAdd_time(resumeListoObject
                            .getString("add_time"));
                }
                if (resumeListoObject.has("title")) {
                    resumeList.setTitle(resumeListoObject.getString("title"));
                }
                if (resumeListoObject.has("modify_time")) {
                    resumeList.setModifyTime(resumeListoObject
                            .getString("modify_time"));
                }
                if (resumeListoObject.has("fill_scale")) {
                    resumeList.setFill_scale(resumeListoObject
                            .getString("fill_scale"));
                }
                if (resumeListoObject.has("resume_type")) {
                    resumeList.setResume_type(resumeListoObject
                            .getString("resume_type"));
                }
                if (resumeListoObject.has("other_language")) {
                    resumeList.setOther_language(resumeListoObject
                            .getString("other_language"));
                }
                if (resumeListoObject.has("is_app")) {
                    resumeList.setIs_app(resumeListoObject
                            .getString("is_app"));
                }
                list.add(resumeList);
            } catch (Exception e) {
                e.printStackTrace();
                //System.out.println("简历列表解析异常");
            }
        }

        return list;
    }
}

