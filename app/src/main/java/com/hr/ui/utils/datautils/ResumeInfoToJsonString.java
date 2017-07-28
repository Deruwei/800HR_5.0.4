package com.hr.ui.utils.datautils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeAssessInfo;
import com.hr.ui.model.ResumeBaseInfo;
import com.hr.ui.model.ResumeEducation;
import com.hr.ui.model.ResumeExperience;
import com.hr.ui.model.ResumeLanguageLevel;
import com.hr.ui.model.ResumeOrder;
import com.hr.ui.model.ResumePlant;
import com.hr.ui.model.ResumeProject;
import com.hr.ui.model.ResumeSkill;
import com.hr.ui.model.ResumeTitle;

/**
 * 简历信息转json
 * 
 * @author 800hr:xuebaohua
 * 
 */
public class ResumeInfoToJsonString {
	private String resumeIdString;
	private String resumeLanguageString;
	private DAO_DBOperator dbOperator;

	// 信息部分
	private ResumeBaseInfo resumeBaseInfo;
	private ResumeLanguageLevel[] resumeLanguageLevels;
	private ResumeTitle resumeTitle;
	private ResumeOrder resumeOrder;
	private ResumeAssessInfo resumeAssessInfo;
	private ResumeExperience[] resumeExperiences;
	private ResumeEducation[] resumeEducations;
	private ResumeProject[] resumeProjects;
	private ResumePlant[] resumePlants;
	private ResumeSkill[] resumeSkills;

	public ResumeInfoToJsonString(Context context, String resumeIdString,
			String resumeLanguageString) {
		this.resumeIdString = resumeIdString;
		this.resumeLanguageString = resumeLanguageString;
		this.dbOperator = new DAO_DBOperator(context);
	}

	/**
	 * 
	 * @return 个人信息（不含语言）
	 */
	public String getBaseInofJsonString() {
		// 将java对象的属性转换成指定的json名字
		Gson gson = new GsonBuilder().create();
		StringBuffer buffer = new StringBuffer();
		resumeBaseInfo = dbOperator
				.query_ResumePersonInfo_Toone(resumeLanguageString);

		if (resumeBaseInfo != null && resumeBaseInfo.getIsUpdate() == 1) {
			String jsonBaseInfoString = gson.toJson(resumeBaseInfo);
			// 基本信息
			buffer.append(jsonBaseInfoString);
		}
		return buffer.toString();
	}
	/**
	 * 
	 * @return 语言信息
	 */
	public String getLanguageJsonString() {
		// 将java对象的属性转换成指定的json名字
		Gson gson = new GsonBuilder().create();
		StringBuffer buffer = new StringBuffer();
		resumeLanguageLevels = dbOperator.query_ResumeLanguageLevel();
		if (resumeLanguageLevels != null && resumeBaseInfo != null
				&& resumeBaseInfo.getIsUpdate() == 1) {
			String jsonLanguageLevelString = gson.toJson(resumeLanguageLevels);
			// 语言能力
			buffer.append(jsonLanguageLevelString);
		}
		return buffer.toString();
	}

	/**
	 * 
	 * @return 简历信息
	 */
	public String getResumeDetailInfoJsonString() {
		// 将java对象的属性转换成指定的json名字
		Gson gson = new GsonBuilder().create();
		StringBuffer buffer = new StringBuffer();
		// 简历信息
		// ----头信息
		resumeTitle = dbOperator.query_ResumeTitle_info(resumeIdString,
				resumeLanguageString);
		// ----求职意向
		resumeOrder = dbOperator.query_ResumeCareerObjective_Toone(
				resumeIdString, resumeLanguageString);
		// ----自我评价
		resumeAssessInfo = dbOperator.query_ResumeTome_Toone(resumeIdString,
				resumeLanguageString);
		// ----工作经验
		resumeExperiences = dbOperator.query_ResumeWorkExperience(
				resumeIdString, resumeLanguageString);
		// ----教育经历
		resumeEducations = dbOperator.query_ResumeEducation(resumeIdString,
				resumeLanguageString);
		// ----项目经验
		resumeProjects = dbOperator.query_Resumeitem(resumeIdString,
				resumeLanguageString);
		// ----培训经历
		resumePlants = dbOperator.query_ResumeTraining(resumeIdString,
				resumeLanguageString);
		// ----专业技能
		resumeSkills = dbOperator.query_ResumeSkill(resumeIdString,
				resumeLanguageString);
		if (resumeTitle != null && resumeTitle.getIsUpdate() == 1) {
			String jsonTitleString = gson.toJson(resumeTitle);
			String jsonOderString = gson.toJson(resumeOrder);
			String jsonAssessString = gson.toJson(resumeAssessInfo);
			String jsonExperienceString = gson.toJson(resumeExperiences);
			String jsonEducationString = gson.toJson(resumeEducations);
			String jsonProjectString = gson.toJson(resumeProjects);
			String jsonPlantsString = gson.toJson(resumePlants);
			String jsonSkillString = gson.toJson(resumeSkills);
			// 头信息
			buffer.append("{\"title_info\":");
			buffer.append(jsonTitleString + ",");
			// 求职意向
			buffer.append("\"order_info\":");
			buffer.append(jsonOderString + ",");
			// 自我评价
			buffer.append("\"assess_info\":");
			buffer.append(jsonAssessString.length() == 0 ? "{},"
					: jsonAssessString + ",");
			// 工作经验
			buffer.append("\"experience_list\":");
			buffer.append(jsonExperienceString + ",");
			// 教育经历
			buffer.append("\"education_list\":");
			buffer.append(jsonEducationString + ",");
			// 项目经验
			buffer.append("\"project_list\":");
			buffer.append(jsonProjectString + ",");
			// 培训经历
			buffer.append("\"plant_list\":");
			buffer.append(jsonPlantsString + ",");
			// 专业技能
			buffer.append("\"skill_list\":");
			buffer.append(jsonSkillString + "}");
		}

		return buffer.toString();
	}
}
