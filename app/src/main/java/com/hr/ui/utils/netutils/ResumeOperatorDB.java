package com.hr.ui.utils.netutils;

import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeAssessInfo;
import com.hr.ui.model.ResumeEducation;
import com.hr.ui.model.ResumeExperience;
import com.hr.ui.model.ResumeLanguageLevel;
import com.hr.ui.model.ResumeOrder;
import com.hr.ui.model.ResumePlant;
import com.hr.ui.model.ResumeProject;
import com.hr.ui.model.ResumeSkill;
import com.hr.ui.model.ResumeTitle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



/**
 * 简历与数据库的沟通
 * 
 * @author 800hr:xuebaohua
 * 
 */
public class ResumeOperatorDB {
	private DAO_DBOperator dbOperator;
	private JSONObject resumeItemJsonObject;

	public ResumeOperatorDB(DAO_DBOperator dbOperator,
			JSONObject resumeItemJsonObject) {
		this.dbOperator = dbOperator;
		try {
			this.resumeItemJsonObject = resumeItemJsonObject
					.getJSONObject("resume_info");
		} catch (JSONException e) {
			e.printStackTrace();
			//System.out.println("ResumeOperaterDB异常");
		}
	}

	// *******************insert
	/*
	 * 插入简历头信息
	 */
	public void insertResumeTitle() {
		try {
			if (!resumeItemJsonObject.has("title_info"))
				return;
			JSONArray array = resumeItemJsonObject.getJSONArray("title_info");
			if (array != null && array.length() > 0) {
				JSONObject resumeTitleJsonObject = array.getJSONObject(0);
				ResumeTitle resumeTitle = new ResumeTitle();
				resumeTitle.setUser_id(resumeTitleJsonObject
						.getString("user_id"));
				resumeTitle.setResume_id(resumeTitleJsonObject
						.getString("resume_id"));
				resumeTitle.setTitle(resumeTitleJsonObject.getString("title"));
				resumeTitle.setKey_word(resumeTitleJsonObject
						.getString("key_word"));
				resumeTitle.setResume_language(resumeTitleJsonObject
						.getString("resume_language"));
				resumeTitle.setResume_type(resumeTitleJsonObject
						.getString("resume_type"));
				resumeTitle.setOpen(resumeTitleJsonObject.getString("open"));
				resumeTitle
						.setUptime(resumeTitleJsonObject.getString("uptime"));
				resumeTitle.setFill_scale(resumeTitleJsonObject
						.getString("fill_scale"));
				resumeTitle.setCastbehalf(resumeTitleJsonObject
						.getString("castbehalf"));
				resumeTitle.setAdd_time(resumeTitleJsonObject
						.getString("add_time"));
				resumeTitle.setImportant(resumeTitleJsonObject
						.getString("important"));
				resumeTitle.setModify_time(resumeTitleJsonObject
						.getString("modify_time"));
				//System.out.println(resumeTitle.toString());
				dbOperator.Insert_ResumeList(resumeTitle);
				//System.out.println("简历头信息写入成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("简历头信息写入");
		}
	}

	/*
	 * 插入简历求职意向
	 */
	public void insertResumeOrder() {
		try {
			if (!resumeItemJsonObject.has("order_info"))
				return;
			JSONArray array = resumeItemJsonObject.getJSONArray("order_info");
			if (array != null && array.length() > 0) {
				JSONObject resumeOrderJsonObject = array.getJSONObject(0);
				ResumeOrder resumeOrder = new ResumeOrder();
				resumeOrder.setUser_id(resumeOrderJsonObject
						.getString("user_id"));
				resumeOrder.setJobtype(resumeOrderJsonObject
						.getString("jobtype"));
				resumeOrder.setIndustry(resumeOrderJsonObject
						.getString("industry"));
				resumeOrder.setFunc(resumeOrderJsonObject.getString("func"));
				resumeOrder.setWorkarea(resumeOrderJsonObject
						.getString("workarea"));
				resumeOrder.setOrder_salary(resumeOrderJsonObject
						.getString("order_salary"));
				resumeOrder.setOrder_salary_noshow(resumeOrderJsonObject
						.getString("order_salary_noshow"));
				resumeOrder.setResume_id(resumeOrderJsonObject
						.getString("resume_id"));
				resumeOrder.setResume_language(resumeOrderJsonObject
						.getString("resume_language"));
				resumeOrder.setJobname(resumeOrderJsonObject
						.getString("jobname"));
				resumeOrder
						.setLingyu(resumeOrderJsonObject.getString("lingyu"));
				dbOperator.Insert_ResumeCareerObjective(resumeOrder);
				//System.out.println("求职意向写入成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("求职意向写入---失败");
		}
	}

	/*
	 * 插入工作经验
	 */
	public void insertResumeExperienceList() {
		try {
			if (!resumeItemJsonObject.has("experience_list"))
				return;
			JSONArray orderInfoJsonArray = resumeItemJsonObject
					.getJSONArray("experience_list");
			//System.out.println("经历insert：" + orderInfoJsonArray.toString());
			ArrayList<ResumeExperience> listExperiences = new ArrayList<ResumeExperience>();
			for (int i = 0; i < orderInfoJsonArray.length(); i++) {
				JSONObject experienceItemJsonObject = orderInfoJsonArray
						.getJSONObject(i);
				ResumeExperience resumeExperience = new ResumeExperience();
				resumeExperience.setUser_id(experienceItemJsonObject
						.getString("user_id"));
				resumeExperience.setFromyear(experienceItemJsonObject
						.getString("fromyear"));
				resumeExperience.setFrommonth(experienceItemJsonObject
						.getString("frommonth"));
				resumeExperience.setToyear(experienceItemJsonObject
						.getString("toyear"));
				resumeExperience.setTomonth(experienceItemJsonObject
						.getString("tomonth"));
				resumeExperience.setCompany(experienceItemJsonObject
						.getString("company"));
				resumeExperience.setCompanyhide(experienceItemJsonObject
						.getString("companyhide"));
				resumeExperience.setIndustry(experienceItemJsonObject
						.getString("industry"));
				resumeExperience.setCompanytype(experienceItemJsonObject
						.getString("companytype"));
				resumeExperience.setStuffmunber(experienceItemJsonObject
						.getString("stuffmunber"));
				resumeExperience.setDivision(experienceItemJsonObject
						.getString("division"));
				resumeExperience.setCompanyaddress(experienceItemJsonObject
						.getString("companyaddress"));
				resumeExperience.setPosition(experienceItemJsonObject
						.getString("position"));
				resumeExperience.setResponsibility(experienceItemJsonObject
						.getString("responsiblity"));
				resumeExperience.setOffreason(experienceItemJsonObject
						.getString("offreason"));
				resumeExperience.setZhixi(experienceItemJsonObject
						.getString("zhixi"));
				resumeExperience.setIs_overseas(experienceItemJsonObject
						.getString("is_overseas"));
				resumeExperience.setCountry(experienceItemJsonObject
						.getString("country"));
				resumeExperience.setResume_id(experienceItemJsonObject
						.getString("resume_id"));
				resumeExperience.setResume_language(experienceItemJsonObject
						.getString("resume_language"));
				resumeExperience.setLingyu(experienceItemJsonObject
						.getString("lingyu"));
				resumeExperience.setFunc(experienceItemJsonObject
						.getString("func"));
				resumeExperience.setSalary(experienceItemJsonObject
						.getString("salary"));
				resumeExperience.setSalary_hide(experienceItemJsonObject
						.getString("salary_hide"));
				resumeExperience.setExperience_id(experienceItemJsonObject
						.getString("experience_id"));
				resumeExperience.setZhicheng(experienceItemJsonObject
						.getString("zhicheng"));
				listExperiences.add(resumeExperience);
			}
			dbOperator.Insert_ResumeWorkExperience(listExperiences);

		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("工作经验插入失败");
		}
	}

	/*
	 * 插入教育经历
	 */
	public void insertResumeEducationList() {
		try {
			if (!resumeItemJsonObject.has("education_list"))
				return;
			JSONArray orderInfoJsonArray = resumeItemJsonObject
					.getJSONArray("education_list");
			ArrayList<ResumeEducation> listEducations = new ArrayList<ResumeEducation>();
			for (int i = 0; i < orderInfoJsonArray.length(); i++) {
				JSONObject educationItemJsonObject = orderInfoJsonArray
						.getJSONObject(i);
				ResumeEducation resumeEducation = new ResumeEducation();
				resumeEducation.setUser_id(educationItemJsonObject
						.getString("user_id"));
				resumeEducation.setFrommonth(educationItemJsonObject
						.getString("frommonth"));
				resumeEducation.setFromyear(educationItemJsonObject
						.getString("fromyear"));
				resumeEducation.setTomonth(educationItemJsonObject
						.getString("tomonth"));
				resumeEducation.setToyear(educationItemJsonObject
						.getString("toyear"));
				resumeEducation.setSchoolname(educationItemJsonObject
						.getString("schoolname"));
				resumeEducation.setMoremajor(educationItemJsonObject
						.getString("moremajor"));
				resumeEducation.setDegree(educationItemJsonObject
						.getString("degree"));
				resumeEducation.setEdudetail(educationItemJsonObject
						.getString("edudetail"));
				resumeEducation.setIs_overseas(educationItemJsonObject
						.getString("is_overseas"));
				resumeEducation.setCountry(educationItemJsonObject
						.getString("country"));
				resumeEducation.setResume_id(educationItemJsonObject
						.getString("resume_id"));
				resumeEducation.setResume_language(educationItemJsonObject
						.getString("resume_language"));
				resumeEducation.setEducation_id(educationItemJsonObject
						.getString("education_id"));

				listEducations.add(resumeEducation);
			}
			dbOperator.Insert_ResumeEducation(listEducations);
			//System.out.println("教育经历插入成功");
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("教育经历插入失败");
		}
	}

	/*
	 * 插入自我介绍
	 */
	public void insertResumeAssessInfo() {
		try {
			if (!resumeItemJsonObject.has("assess_info"))
				return;
			JSONArray array = resumeItemJsonObject.getJSONArray("assess_info");
			if (array != null && array.length() > 0) {
				JSONObject assessInfoJsonObject = array.getJSONObject(0);
				ResumeAssessInfo resumeAssessInfo = new ResumeAssessInfo();
				resumeAssessInfo.setUser_id(assessInfoJsonObject
						.getString("user_id"));
				resumeAssessInfo.setIntroduction(assessInfoJsonObject
						.getString("introduction"));
				resumeAssessInfo.setResume_id(assessInfoJsonObject
						.getString("resume_id"));
				resumeAssessInfo.setResume_language(assessInfoJsonObject
						.getString("resume_language"));
				dbOperator.Insert_ResumeTome(resumeAssessInfo);
				//System.out.println("自我介绍插入成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("自我介绍插入失败");
		}
	}

	/*
	 * 插入项目经历
	 */
	public void insertResumeProject() {
		try {
			if (!resumeItemJsonObject.has("project_list"))
				return;
			ArrayList<ResumeProject> listProjects = new ArrayList<ResumeProject>();
			JSONArray resumeProjectArray = resumeItemJsonObject
					.getJSONArray("project_list");
			for (int i = 0; i < resumeProjectArray.length(); i++) {
				JSONObject projectItemJsonObject = resumeProjectArray
						.getJSONObject(i);
				ResumeProject resumeProject = new ResumeProject();
				resumeProject.setFromyear(projectItemJsonObject
						.getString("fromyear"));
				resumeProject.setFrommonth(projectItemJsonObject
						.getString("frommonth"));
				resumeProject.setToyear(projectItemJsonObject
						.getString("toyear"));
				resumeProject.setTomonth(projectItemJsonObject
						.getString("tomonth"));
				resumeProject.setProjectname(projectItemJsonObject
						.getString("projectname"));
				resumeProject.setPosition(projectItemJsonObject
						.getString("position"));
				resumeProject.setProjectdesc(projectItemJsonObject
						.getString("projectdesc"));
				resumeProject.setResponsibility(projectItemJsonObject
						.getString("responsibility"));
				resumeProject.setResume_id(projectItemJsonObject
						.getString("resume_id"));
				resumeProject.setResume_language(projectItemJsonObject
						.getString("resume_language"));
				resumeProject.setProject_id(projectItemJsonObject
						.getString("project_id"));

				listProjects.add(resumeProject);
			}
			dbOperator.Insert_Resumeitem(listProjects);
			//System.out.println("项目经历插入成功");
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("项目经历插入失败");
		}
	}

	/*
	 * 插入培训经历
	 */
	public void insertResumePlantList() {
		try {
			if (!resumeItemJsonObject.has("plant_list"))
				return;
			ArrayList<ResumePlant> listPlants = new ArrayList<ResumePlant>();
			JSONArray resumePlantArray = resumeItemJsonObject
					.getJSONArray("plant_list");
			for (int i = 0; i < resumePlantArray.length(); i++) {
				JSONObject plantItemJsonObject = resumePlantArray
						.getJSONObject(i);
				ResumePlant resumePlant = new ResumePlant();
				resumePlant
						.setUser_id(plantItemJsonObject.getString("user_id"));
				resumePlant.setFromyear(plantItemJsonObject
						.getString("fromyear"));
				resumePlant.setFrommonth(plantItemJsonObject
						.getString("frommonth"));
				resumePlant.setToyear(plantItemJsonObject.getString("toyear"));
				resumePlant
						.setTomonth(plantItemJsonObject.getString("tomonth"));
				resumePlant.setInstitution(plantItemJsonObject
						.getString("institution"));
				resumePlant.setCourse(plantItemJsonObject.getString("course"));
				resumePlant.setPlace(plantItemJsonObject.getString("place"));
				resumePlant.setCertification(plantItemJsonObject
						.getString("certification"));
				resumePlant.setTraindetail(plantItemJsonObject
						.getString("traindetail"));
				resumePlant.setResume_id(plantItemJsonObject
						.getString("resume_id"));
				resumePlant.setResume_language(plantItemJsonObject
						.getString("resume_language"));
				resumePlant.setPlant_id(plantItemJsonObject
						.getString("plant_id"));
				listPlants.add(resumePlant);
			}
			dbOperator.Insert_ResumeTraining(listPlants);
			//System.out.println("培训经历插入成功");
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("培训经历插入失败");
		}
	}

	/*
	 * 插入个人技能
	 */
	public void insertResumeSkillList() {
		try {
			if (!resumeItemJsonObject.has("skill_list"))
				return;
			ArrayList<ResumeSkill> listSkills = new ArrayList<ResumeSkill>();
			JSONArray resumeSkillArray = resumeItemJsonObject
					.getJSONArray("skill_list");
			for (int i = 0; i < resumeSkillArray.length(); i++) {
				JSONObject skillItemJsonObject = resumeSkillArray
						.getJSONObject(i);
				ResumeSkill resumeSkill = new ResumeSkill();
				resumeSkill
						.setUser_id(skillItemJsonObject.getString("user_id"));
				resumeSkill.setSkilltitle(skillItemJsonObject
						.getString("skilltitle"));
				resumeSkill
						.setUsetime(skillItemJsonObject.getString("usetime"));
				resumeSkill
						.setAbility(skillItemJsonObject.getString("ability"));
				resumeSkill.setResume_id(skillItemJsonObject
						.getString("resume_id"));
				resumeSkill.setResume_language(skillItemJsonObject
						.getString("resume_language"));
				resumeSkill.setSkill_id(skillItemJsonObject
						.getString("skill_id"));
				listSkills.add(resumeSkill);
			}
			dbOperator.Insert_ResumeSkill(listSkills);
			//System.out.println("个人技能插入成功");
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("个人技能插入失败");
		}
	}

	/*
	 * 语言能力技能
	 */
	// public void insertResumeLanguageLevelList() {
	// try {
	// if (!resumeItemJsonObject.has("language_list"))
	// return;
	// ArrayList<ResumeLanguageLevel> listLanguageLevels = new
	// ArrayList<ResumeLanguageLevel>();
	// JSONArray resumeLanguageLevelArray = resumeItemJsonObject
	// .getJSONArray("language_list");
	// for (int i = 0; i < resumeLanguageLevelArray.length(); i++) {
	// JSONObject languageLevelItemJsonObject = resumeLanguageLevelArray
	// .getJSONObject(i);
	// ResumeLanguageLevel resumeLanguageLevel = new ResumeLanguageLevel();
	// resumeLanguageLevel.setLanguage_id(languageLevelItemJsonObject
	// .getString("language_id"));
	// resumeLanguageLevel.setLangname(languageLevelItemJsonObject
	// .getString("langname"));
	// resumeLanguageLevel.setRead_level(languageLevelItemJsonObject
	// .getString("read_level"));
	// resumeLanguageLevel.setSpeak_level(languageLevelItemJsonObject
	// .getString("speak_level"));
	// listLanguageLevels.add(resumeLanguageLevel);
	// }
	// dbOperator.Insert_ResumeLanguageLevel(listLanguageLevels);
	// //System.out.println("语言能力插入成功");
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	// *******************update
	/*
	 * 更新简历头信息
	 */
	public void updateResumeTitle(String resume_id, String resume_language) {
		try {
			if (!resumeItemJsonObject.has("title_info"))
				return;
			JSONArray resumeTitleArray = resumeItemJsonObject
					.getJSONArray("title_info");
			if (resumeTitleArray != null) {
				if (resumeTitleArray.length() > 0) {
					JSONObject resumeTitleJsonObject = resumeTitleArray
							.getJSONObject(0);

					ResumeTitle resumeTitle = new ResumeTitle();
					resumeTitle.setUser_id(resumeTitleJsonObject
							.getString("user_id"));
					resumeTitle.setResume_id(resumeTitleJsonObject
							.getString("resume_id"));
					resumeTitle.setTitle(resumeTitleJsonObject
							.getString("title"));
					resumeTitle.setKey_word(resumeTitleJsonObject
							.getString("key_word"));
					resumeTitle.setResume_language(resumeTitleJsonObject
							.getString("resume_language"));
					resumeTitle.setResume_type(resumeTitleJsonObject
							.getString("resume_type"));
					resumeTitle
							.setOpen(resumeTitleJsonObject.getString("open"));
					resumeTitle.setUptime(resumeTitleJsonObject
							.getString("uptime"));
					resumeTitle.setFill_scale(resumeTitleJsonObject
							.getString("fill_scale"));
					resumeTitle.setCastbehalf(resumeTitleJsonObject
							.getString("castbehalf"));
					resumeTitle.setAdd_time(resumeTitleJsonObject
							.getString("add_time"));
					resumeTitle.setImportant(resumeTitleJsonObject
							.getString("important"));
					resumeTitle.setModify_time(resumeTitleJsonObject
							.getString("modify_time"));
					dbOperator.Delete_Data("ResumeList", resume_id,
							resume_language);
					dbOperator.Insert_ResumeList(resumeTitle);
					//System.out.println("简历头信息修改成功");
				} else {
					dbOperator.Delete_Data("ResumeList", resume_id,
							resume_language);
					//System.out.println("简历头信息修改成功");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("简历头信息修改失败");
		}
	}

	/*
	 * 更新简历求职意向
	 */
	public void updateResumeOrder(String resume_id, String resume_language) {
		try {
			if (!resumeItemJsonObject.has("order_info"))
				return;
			JSONArray array = resumeItemJsonObject.getJSONArray("order_info");
			if (array != null) {
				if (array.length() > 0) {
					JSONObject resumeOrderJsonObject = array.getJSONObject(0);
					ResumeOrder resumeOrder = new ResumeOrder();
					resumeOrder.setUser_id(resumeOrderJsonObject
							.getString("user_id"));
					resumeOrder.setJobtype(resumeOrderJsonObject
							.getString("jobtype"));
					resumeOrder.setIndustry(resumeOrderJsonObject
							.getString("industry"));
					resumeOrder
							.setFunc(resumeOrderJsonObject.getString("func"));
					resumeOrder.setWorkarea(resumeOrderJsonObject
							.getString("workarea"));

					resumeOrder.setOrder_salary(resumeOrderJsonObject
							.getString("order_salary"));
					resumeOrder.setOrder_salary_noshow(resumeOrderJsonObject
							.getString("order_salary_noshow"));
					resumeOrder.setResume_id(resumeOrderJsonObject
							.getString("resume_id"));
					resumeOrder.setResume_language(resumeOrderJsonObject
							.getString("resume_language"));
					resumeOrder.setJobname(resumeOrderJsonObject
							.getString("jobname"));
					resumeOrder.setLingyu(resumeOrderJsonObject
							.getString("lingyu"));
					dbOperator.Delete_Data("ResumeCareerObjective", resume_id,
							resume_language);
					dbOperator.Insert_ResumeCareerObjective(resumeOrder);
					//System.out.println("求职意向更新成功");
				} else {
					dbOperator.Delete_Data("ResumeCareerObjective", resume_id,
							resume_language);
					//System.out.println("求职意向更新成功");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 更新工作经验
	 */
	public void updateResumeExperienceList(String resume_id,
			String resume_language) {
		try {
			if (!resumeItemJsonObject.has("experience_list"))
				return;
			JSONArray orderInfoJsonArray = resumeItemJsonObject
					.getJSONArray("experience_list");
			//System.out.println("经历update：" + orderInfoJsonArray.toString());
			ArrayList<ResumeExperience> listExperiences = new ArrayList<ResumeExperience>();
			for (int i = 0; i < orderInfoJsonArray.length(); i++) {
				JSONObject experienceItemJsonObject = orderInfoJsonArray
						.getJSONObject(i);
				ResumeExperience resumeExperience = new ResumeExperience();
				resumeExperience.setUser_id(experienceItemJsonObject
						.getString("user_id"));
				resumeExperience.setFromyear(experienceItemJsonObject
						.getString("fromyear"));
				resumeExperience.setFrommonth(experienceItemJsonObject
						.getString("frommonth"));
				resumeExperience.setToyear(experienceItemJsonObject
						.getString("toyear"));
				resumeExperience.setTomonth(experienceItemJsonObject
						.getString("tomonth"));
				resumeExperience.setCompany(experienceItemJsonObject
						.getString("company"));
				resumeExperience.setCompanyhide(experienceItemJsonObject
						.getString("companyhide"));
				resumeExperience.setIndustry(experienceItemJsonObject
						.getString("industry"));
				resumeExperience.setCompanytype(experienceItemJsonObject
						.getString("companytype"));
				resumeExperience.setStuffmunber(experienceItemJsonObject
						.getString("stuffmunber"));
				resumeExperience.setDivision(experienceItemJsonObject
						.getString("division"));
				resumeExperience.setCompanyaddress(experienceItemJsonObject
						.getString("companyaddress"));
				resumeExperience.setPosition(experienceItemJsonObject
						.getString("position"));
				resumeExperience.setResponsibility(experienceItemJsonObject
						.getString("responsiblity"));
				resumeExperience.setOffreason(experienceItemJsonObject
						.getString("offreason"));
				resumeExperience.setZhixi(experienceItemJsonObject
						.getString("zhixi"));
				resumeExperience.setIs_overseas(experienceItemJsonObject
						.getString("is_overseas"));
				resumeExperience.setCountry(experienceItemJsonObject
						.getString("country"));
				resumeExperience.setResume_id(experienceItemJsonObject
						.getString("resume_id"));
				resumeExperience.setResume_language(experienceItemJsonObject
						.getString("resume_language"));
				resumeExperience.setLingyu(experienceItemJsonObject
						.getString("lingyu"));
				resumeExperience.setFunc(experienceItemJsonObject
						.getString("func"));
				resumeExperience.setSalary(experienceItemJsonObject
						.getString("salary"));
				resumeExperience.setSalary_hide(experienceItemJsonObject
						.getString("salary_hide"));
				resumeExperience.setExperience_id(experienceItemJsonObject
						.getString("experience_id"));
				resumeExperience.setZhicheng(experienceItemJsonObject
						.getString("zhicheng"));
				listExperiences.add(resumeExperience);
			}
			dbOperator.Delete_Data("ResumeWorkExperience", resume_id,
					resume_language);
			if (orderInfoJsonArray.length() > 0) {
				dbOperator.Insert_ResumeWorkExperience(listExperiences);
			}
			//System.out.println("工作经验修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("工作经验修改失败");
		}
	}

	/*
	 * 更新教育经历
	 */
	public void updateResumeEducationList(String resume_id,
			String resume_language) {
		try {
			if (!resumeItemJsonObject.has("education_list"))
				return;
			JSONArray orderInfoJsonArray = resumeItemJsonObject
					.getJSONArray("education_list");
			ArrayList<ResumeEducation> listEducations = new ArrayList<ResumeEducation>();
			for (int i = 0; i < orderInfoJsonArray.length(); i++) {
				JSONObject educationItemJsonObject = orderInfoJsonArray
						.getJSONObject(i);
				ResumeEducation resumeEducation = new ResumeEducation();
				resumeEducation.setUser_id(educationItemJsonObject
						.getString("user_id"));
				resumeEducation.setFrommonth(educationItemJsonObject
						.getString("frommonth"));
				resumeEducation.setFromyear(educationItemJsonObject
						.getString("fromyear"));
				resumeEducation.setTomonth(educationItemJsonObject
						.getString("tomonth"));
				resumeEducation.setToyear(educationItemJsonObject
						.getString("toyear"));
				resumeEducation.setSchoolname(educationItemJsonObject
						.getString("schoolname"));
				resumeEducation.setMoremajor(educationItemJsonObject
						.getString("moremajor"));
				resumeEducation.setDegree(educationItemJsonObject
						.getString("degree"));
				resumeEducation.setEdudetail(educationItemJsonObject
						.getString("edudetail"));
				resumeEducation.setIs_overseas(educationItemJsonObject
						.getString("is_overseas"));
				resumeEducation.setCountry(educationItemJsonObject
						.getString("country"));
				resumeEducation.setResume_id(educationItemJsonObject
						.getString("resume_id"));
				resumeEducation.setResume_language(educationItemJsonObject
						.getString("resume_language"));
				resumeEducation.setEducation_id(educationItemJsonObject
						.getString("education_id"));
				listEducations.add(resumeEducation);
			}
			dbOperator.Delete_Data("ResumeEducation", resume_id,
					resume_language);
			if (orderInfoJsonArray.length() > 0) {
				dbOperator.Insert_ResumeEducation(listEducations);
			}
			//System.out.println("教育经历修改成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 更新自我介绍
	 */
	public void updateResumeAssessInfo(String resume_id, String resume_language) {
		try {
			if (!resumeItemJsonObject.has("assess_info"))
				return;
			JSONArray array = resumeItemJsonObject.getJSONArray("assess_info");
			if (array != null) {
				if (array.length() > 0) {
					JSONObject assessInfoJsonObject = array.getJSONObject(0);
					ResumeAssessInfo resumeAssessInfo = new ResumeAssessInfo();
					resumeAssessInfo.setUser_id(assessInfoJsonObject
							.getString("user_id"));
					resumeAssessInfo.setIntroduction(assessInfoJsonObject
							.getString("introduction"));
					resumeAssessInfo.setResume_id(assessInfoJsonObject
							.getString("resume_id"));
					resumeAssessInfo.setResume_language(assessInfoJsonObject
							.getString("resume_language"));
					dbOperator.Delete_Data("ResumeSelfIntroduction", resume_id,
							resume_language);
					dbOperator.Insert_ResumeTome(resumeAssessInfo);
					//System.out.println("自我介绍修改成功");
				} else {
					dbOperator.Delete_Data("ResumeSelfIntroduction", resume_id,
							resume_language);
					//System.out.println("自我介绍修改成功");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println("自我介绍修改失败");
		}
	}

	/*
	 * 更新项目经历
	 */
	public void updateResumeProject(String resume_id, String resume_language) {
		try {
			if (!resumeItemJsonObject.has("project_list"))
				return;
			ArrayList<ResumeProject> listProjects = new ArrayList<ResumeProject>();
			JSONArray resumeProjectArray = resumeItemJsonObject
					.getJSONArray("project_list");
			for (int i = 0; i < resumeProjectArray.length(); i++) {
				JSONObject projectItemJsonObject = resumeProjectArray
						.getJSONObject(i);
				ResumeProject resumeProject = new ResumeProject();
				resumeProject.setFromyear(projectItemJsonObject
						.getString("fromyear"));
				resumeProject.setFrommonth(projectItemJsonObject
						.getString("frommonth"));
				resumeProject.setToyear(projectItemJsonObject
						.getString("toyear"));
				resumeProject.setTomonth(projectItemJsonObject
						.getString("tomonth"));
				resumeProject.setProjectname(projectItemJsonObject
						.getString("projectname"));
				resumeProject.setPosition(projectItemJsonObject
						.getString("position"));
				resumeProject.setProjectdesc(projectItemJsonObject
						.getString("projectdesc"));
				resumeProject.setResponsibility(projectItemJsonObject
						.getString("responsibility"));
				resumeProject.setResume_id(projectItemJsonObject
						.getString("resume_id"));
				resumeProject.setResume_language(projectItemJsonObject
						.getString("resume_language"));
				resumeProject.setProject_id(projectItemJsonObject
						.getString("project_id"));
				listProjects.add(resumeProject);
			}
			dbOperator.Delete_Data("ResumeProject", resume_id, resume_language);
			if (resumeProjectArray.length() > 0) {
				dbOperator.Insert_Resumeitem(listProjects);
			}

			//System.out.println("项目经历修改成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 更新培训经历
	 */
	public void updateResumePlantList(String resume_id, String resume_language) {
		try {
			if (!resumeItemJsonObject.has("plant_list"))
				return;
			ArrayList<ResumePlant> listPlants = new ArrayList<ResumePlant>();
			JSONArray resumePlantArray = resumeItemJsonObject
					.getJSONArray("plant_list");
			for (int i = 0; i < resumePlantArray.length(); i++) {
				JSONObject plantItemJsonObject = resumePlantArray
						.getJSONObject(i);
				ResumePlant resumePlant = new ResumePlant();
				resumePlant
						.setUser_id(plantItemJsonObject.getString("user_id"));
				resumePlant.setFromyear(plantItemJsonObject
						.getString("fromyear"));
				resumePlant.setFrommonth(plantItemJsonObject
						.getString("frommonth"));
				resumePlant.setToyear(plantItemJsonObject.getString("toyear"));
				resumePlant
						.setTomonth(plantItemJsonObject.getString("tomonth"));
				resumePlant.setInstitution(plantItemJsonObject
						.getString("institution"));
				resumePlant.setCourse(plantItemJsonObject.getString("course"));
				resumePlant.setPlace(plantItemJsonObject.getString("place"));
				resumePlant.setCertification(plantItemJsonObject
						.getString("certification"));
				resumePlant.setTraindetail(plantItemJsonObject
						.getString("traindetail"));
				resumePlant.setResume_id(plantItemJsonObject
						.getString("resume_id"));
				resumePlant.setResume_language(plantItemJsonObject
						.getString("resume_language"));
				resumePlant.setPlant_id(plantItemJsonObject
						.getString("plant_id"));
				listPlants.add(resumePlant);
			}
			dbOperator
					.Delete_Data("ResumeTraining", resume_id, resume_language);
			if (resumePlantArray.length() > 0) {
				dbOperator.Insert_ResumeTraining(listPlants);
			}
			//System.out.println("培训经历修改成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 更新个人技能
	 */
	public void updateResumeSkillList(String resume_id, String resume_language) {
		try {
			if (!resumeItemJsonObject.has("skill_list"))
				return;
			ArrayList<ResumeSkill> listSkills = new ArrayList<ResumeSkill>();
			JSONArray resumeSkillArray = resumeItemJsonObject
					.getJSONArray("skill_list");
			for (int i = 0; i < resumeSkillArray.length(); i++) {
				JSONObject skillItemJsonObject = resumeSkillArray
						.getJSONObject(i);
				ResumeSkill resumeSkill = new ResumeSkill();
				resumeSkill
						.setUser_id(skillItemJsonObject.getString("user_id"));
				resumeSkill.setSkilltitle(skillItemJsonObject
						.getString("skilltitle"));
				resumeSkill
						.setUsetime(skillItemJsonObject.getString("usetime"));
				resumeSkill
						.setAbility(skillItemJsonObject.getString("ability"));
				resumeSkill.setResume_id(skillItemJsonObject
						.getString("resume_id"));
				resumeSkill.setResume_language(skillItemJsonObject
						.getString("resume_language"));
				resumeSkill.setSkill_id(skillItemJsonObject
						.getString("skill_id"));
				listSkills.add(resumeSkill);
			}
			dbOperator.Delete_Data("ResumeSkill", resume_id, resume_language);
			if (resumeSkillArray.length() > 0) {
				dbOperator.Insert_ResumeSkill(listSkills);
			}
			//System.out.println("个人技能修改成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 更新语言能力
	 */
	 public void updateResumeLanguageLevelList(String resume_id,
	 String resume_language) {
	 try {
	 if (!resumeItemJsonObject.has("language_list"))
	 return;
	 ArrayList<ResumeLanguageLevel> listLanguageLevels = new
	 ArrayList<ResumeLanguageLevel>();
	 JSONArray resumeLanguageLevelArray = resumeItemJsonObject
	 .getJSONArray("language_list");
	 for (int i = 0; i < resumeLanguageLevelArray.length(); i++) {
	 JSONObject languageLevelItemJsonObject = resumeLanguageLevelArray
	 .getJSONObject(i);
	 ResumeLanguageLevel resumeLanguageLevel = new ResumeLanguageLevel();
	 resumeLanguageLevel.setId(Integer.parseInt(languageLevelItemJsonObject
			 .getString("language_id")));
	 resumeLanguageLevel.setLangname(languageLevelItemJsonObject
	 .getString("langname"));
	 resumeLanguageLevel.setRead_level(languageLevelItemJsonObject
	 .getString("read_level"));
	 resumeLanguageLevel.setSpeak_level(languageLevelItemJsonObject
	 .getString("speak_level"));
	 listLanguageLevels.add(resumeLanguageLevel);
	 }
	 dbOperator.Delete_Data("ResumeLanguageLevel", resume_id,
	 resume_language);
	 dbOperator.Insert_ResumeLanguageLevel(listLanguageLevels);
	 //System.out.println("语言能力修改成功");
	 } catch (Exception e) {
	 e.printStackTrace();
	 }
	 }
}
