package com.hr.ui.utils.datautils;

import android.content.Context;
import android.util.Log;

import com.hr.ui.R;
import com.hr.ui.config.Constants;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.utils.netutils.NetUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.jar.Attributes;

/**
 * 根据提供id，返回对应文字信息
 * 
 * @author 800hr:xuebaohua
 * 
 */
public class ResumeInfoIDToString {
	/**
	 * 获取工作类型
	 */
	public static String getWorkType(Context context, String id, boolean isCHS) {
		if (id == null || id.length() == 0) {
			return "";
		}
		String[] workTypeIdsStrings = context.getResources().getStringArray(
				R.array.array_jobtype_ids);
		String[] workTypeStrings;
		if (!isCHS) {// en
			workTypeStrings = context.getResources().getStringArray(
					R.array.array_jobtype_en);
		} else {// zh
			workTypeStrings = context.getResources().getStringArray(
					R.array.array_jobtype_zh);
		}
		for (int i = 0; i < workTypeIdsStrings.length; i++) {
			if (workTypeIdsStrings[i].equals(id)) {
				return workTypeStrings[i];
			}
		}
		return "";
	}

	/**
	 * 获取国家/地区
	 */
	public static String getNation(Context context, String id, boolean isCHS) {
		String[] nationIdsStrings = context.getResources().getStringArray(R.array.array_nationality_ids);
		String[] nationTextStrings;
		if (!isCHS) {// en
			nationTextStrings = context.getResources().getStringArray(
					R.array.array_nationality_en);
		} else {// zh
			nationTextStrings = context.getResources().getStringArray(
					R.array.array_nationality_zh);
		}
		for (int i = 0; i < nationIdsStrings.length; i++) {
			if (nationIdsStrings[i].equals(id)) {
				return nationTextStrings[i];
			}
		}
		return "";
	}

	/**
	 * 获取婚姻状况
	 */
	public static String getMarriageState(Context context, String id,
			boolean isCHS) {
		String[] marriageStateidsStrings = context.getResources()
				.getStringArray(R.array.array_marriage_ids);
		String[] marriageStatetextStrings;
		if (!isCHS) {// en
			marriageStatetextStrings = context.getResources().getStringArray(
					R.array.array_marriage_en);
		} else {// zh
			marriageStatetextStrings = context.getResources().getStringArray(
					R.array.array_marriage_zh);
		}
		for (int i = 0; i < marriageStateidsStrings.length; i++) {
			if (marriageStateidsStrings[i].equals(id)) {
				return marriageStatetextStrings[i];
			}
		}
		return "";
	}

	/**
	 * 获取政治面貌
	 */
	public static String getPolity(Context context, String id, boolean isCHS) {
		String[] polityidsStrings = context.getResources().getStringArray(
				R.array.array_polity_ids);
		String[] polityTextStrings;
		if (!isCHS) {// en
			polityTextStrings = context.getResources().getStringArray(
					R.array.array_polity_en);
		} else {// zh
			polityTextStrings = context.getResources().getStringArray(
					R.array.array_polity_zh);
		}
		for (int i = 0; i < polityidsStrings.length; i++) {
			if (polityidsStrings[i].equals(id)) {
				return polityTextStrings[i];
			}
		}
		return "";
	}

	/**
	 * 获取即时通讯类型
	 */
	public static String getIM(Context context, String id, boolean isCHS) {
		String[] imIdStrings = context.getResources().getStringArray(
				R.array.array_messageonline_ids);
		String[] imStrings;
		if (!isCHS) {// en
			imStrings = context.getResources().getStringArray(
					R.array.array_messageonline_en);
		} else {// zh
			imStrings = context.getResources().getStringArray(
					R.array.array_messageonline_zh);
		}
		for (int i = 0; i < imIdStrings.length; i++) {
			if (imIdStrings[i].equals(id)) {
				return imStrings[i];
			}
		}
		return "";
	}

	/**
	 * 获取职位
	 */
	public static String getFunc(Context context, String industryId,
			String funcId) {
		if (industryId == null || industryId.length() == 0 || funcId == null
				|| funcId.length() == 0) {
			return "";
		}

		JSONArray jobJsonArray;
		try {
			if (MyUtils.USE_ONLINE_ARRAY) {// 使用网络提供的数据
				jobJsonArray = NetService.getJobAsJSONArray(context, "job.json", industryId);
			} else {// 使用程序内置数据
				InputStream in = context.getAssets().open("job.json");
				JSONObject jsonObject = new JSONObject(NetUtils.readAsString(
						in, Constants.ENCODE));
				jobJsonArray = jsonObject.getJSONArray(industryId + "");
			}
			for (int i = 0; i < jobJsonArray.length(); i++) {
				JSONObject jobJsonObject = jobJsonArray.getJSONObject(i);
				if (jobJsonObject.has(funcId)) {
					return jobJsonObject.getString(funcId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取现居住地
	 */
	public static String getPlace(Context context, String id, boolean isCHS) {
		if (id == null || id.length() == 0) {
			return "";
		}
		JSONArray cityJSONArray;
		try {
			if (MyUtils.USE_ONLINE_ARRAY && isCHS) {
				cityJSONArray = NetService.getCityAsJSONArray(context,
						"city.json");
			} else {
				if (isCHS) {
					InputStream inputStream = context.getAssets().open(
							"city_zh.json");
					cityJSONArray = new JSONArray(NetUtils.readAsString(
							inputStream, Constants.ENCODE));
				} else {
					InputStream inputStream = context.getAssets().open(
							"city_en.json");
					cityJSONArray = new JSONArray(NetUtils.readAsString(
							inputStream, Constants.ENCODE));
				}
			}
			for (int i = 0; i < cityJSONArray.length(); i++) {
				JSONObject object = cityJSONArray.getJSONObject(i);

				if (object.has(id)) {
					return object.getString(id);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 通过城市获取城市ID
	 */
	public static String getCityID(Context context,String city,boolean isCHS){
		if(city==null||"".equals(city)){
			return "";
		}
		JSONArray cityJSONArray;
		try {
			if (MyUtils.USE_ONLINE_ARRAY && isCHS) {
				cityJSONArray = NetService.getCityAsJSONArray(context,
						"city.json");
			} else {
				if (isCHS) {
					InputStream inputStream = context.getAssets().open(
							"city_zh.json");
					cityJSONArray = new JSONArray(NetUtils.readAsString(
							inputStream, Constants.ENCODE));
				} else {
					InputStream inputStream = context.getAssets().open(
							"city_en.json");
					cityJSONArray = new JSONArray(NetUtils.readAsString(
							inputStream, Constants.ENCODE));
				}
			}
			if("北京市".equals(city)||"上海市".equals(city)||"重庆市".equals(city)||"天津市".equals(city)) {
				city = city.substring(0,city.length()-1);
			}
			for (int i = 0; i < cityJSONArray.length(); i++) {
				JSONObject object = cityJSONArray.getJSONObject(i);
                String name=object.toString();
                String cityName=name.substring(name.indexOf(":")+2,name.length()-2);
				if(cityName.equals(city)){
                    return name.substring(2,name.indexOf(":")-1);
                }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 获取职称
	 */
	public static String getZhiCheng(Context context, String id, boolean isCHS) {
		String[] zhichengIdStrings = context.getResources().getStringArray(
				R.array.array_zhicheng_ids);
		String[] zhichengStrings;
		if (!isCHS) {// en
			zhichengStrings = context.getResources().getStringArray(R.array.array_zhicheng_en);
		} else {// zh
			zhichengStrings = context.getResources().getStringArray(R.array.array_zhicheng_zh);
		}
		for (int i = 0; i < zhichengIdStrings.length; i++) {
			if (zhichengIdStrings[i].equals(id)) {
				return zhichengStrings[i];
			}
		}
		return "";
	}

	/**
	 * 获取公司性质
	 */
	public static String getCompanyType(Context context, String id,
			boolean isCHS) {
		String[] xinzhiIdStrings;
		String[] xinzhiStrings;
		if (!isCHS) {// en
			xinzhiIdStrings = context.getResources().getStringArray(R.array.company_xingzhi_en_ids);
			xinzhiStrings = context.getResources().getStringArray(R.array.company_xingzhi_en);
		} else {// zh
			xinzhiIdStrings = context.getResources().getStringArray(R.array.company_xingzhi_zh_ids);
			xinzhiStrings = context.getResources().getStringArray(R.array.company_xingzhi_zh);
		}

		for (int i = 0; i < xinzhiIdStrings.length; i++) {
			if (xinzhiIdStrings[i].equals(id)) {
				return xinzhiStrings[i];
			}
		}
		return "";
	}

	/**
	 * 获取公司规模
	 */
	public static String getCompanySize(Context context, String id,
			boolean isCHS) {
		String[] sizeIdStrings = context.getResources().getStringArray(
				R.array.company_guimo_ids);
		String[] sizeStrings;
		if (!isCHS) {// en
			sizeStrings = context.getResources().getStringArray(
					R.array.company_guimo_en);
		} else {// zh
			sizeStrings = context.getResources().getStringArray(
					R.array.company_guimo_zh);
		}

		for (int i = 0; i < sizeIdStrings.length; i++) {
			if (sizeIdStrings[i].equals(id)) {
				return sizeStrings[i];
			}
		}
		return "";
	}

	/**
	 * 获取当前求职状态
	 */
	public static String getCurrentState(Context context, String id,
			boolean isCHS) {
		String[] workstateIdStrings = context.getResources().getStringArray(
				R.array.array_workstate_ids);
		if (id == null || id.trim().length() == 0) {
			id = workstateIdStrings[0];
		}
		String[] workstateStrings;
		if (!isCHS) {// en
			workstateStrings = context.getResources().getStringArray(
					R.array.array_workstate_en);
		} else {// zh
			workstateStrings = context.getResources().getStringArray(
					R.array.array_workstate_zh);
		}
		for (int i = 0; i < workstateIdStrings.length; i++) {
			if (workstateIdStrings[i].equals(id)) {
				return workstateStrings[i];
			}
		}
		return "";
	}

	/**
	 * 获取学历
	 */
	public static String getEducationDegree(Context context, String id,
			boolean isCHS) {
		String[] degreeIdStrings = context.getResources().getStringArray(
				R.array.array_degree_ids);
		String[] degreeStrings;
		if (!isCHS) {// en
			degreeStrings = context.getResources().getStringArray(
					R.array.array_degree_en);
		} else {// zh
			degreeStrings = context.getResources().getStringArray(
					R.array.array_degree_zh);
		}
		for (int i = 0; i < degreeIdStrings.length; i++) {
			if (degreeIdStrings[i].equals(id)) {
				return degreeStrings[i];
			}
		}
		return "";
	}

	/**
	 * 获取语种
	 */
	public static String getLanguageTpye(Context context, String id,
			boolean isCHS) {
		String[] languageTypeIdStrings = context.getResources().getStringArray(
				R.array.array_language_type_ids);
		String[] languageTypeStrings;
		if (!isCHS) {// en
			languageTypeStrings = context.getResources().getStringArray(
					R.array.array_language_type_en);
		} else {// zh
			languageTypeStrings = context.getResources().getStringArray(
					R.array.array_language_type_zh);
		}
		for (int i = 0; i < languageTypeIdStrings.length; i++) {
			if (languageTypeIdStrings[i].equals(id)) {
				return languageTypeStrings[i];
			}
		}
		return "";
	}

	/**
	 * 获取语言读写能力
	 */
	public static String getLanguageReadLevel(Context context, String id,
			boolean isCHS) {
		String[] readLevelIdStrings = context.getResources().getStringArray(
				R.array.array_language_readlevel_ids);

		String[] readLevelStrings;
		if (!isCHS) {// en
			readLevelStrings = context.getResources().getStringArray(
					R.array.array_language_readlevel_en);
		} else {// zh
			readLevelStrings = context.getResources().getStringArray(
					R.array.array_language_readlevel_zh);
		}
		for (int i = 0; i < readLevelIdStrings.length; i++) {
			if (readLevelIdStrings[i].equals(id)) {
				return readLevelStrings[i];
			}
		}
		return "";
	}

	/**
	 * 获取语言听说能力
	 */
	public static String getLanguageSpeakLevel(Context context, String id,
			boolean isCHS) {
		String[] speakLevelIdStrings = context.getResources().getStringArray(
				R.array.array_language_speaklevel_ids);
		String[] speakLevelStrings;
		if (!isCHS) {// en
			speakLevelStrings = context.getResources().getStringArray(
					R.array.array_language_speaklevel_en);
		} else {// zh
			speakLevelStrings = context.getResources().getStringArray(
					R.array.array_language_speaklevel_zh);
		}

		for (int i = 0; i < speakLevelIdStrings.length; i++) {
			if (speakLevelIdStrings[i].equals(id)) {
				return speakLevelStrings[i];
			}
		}
		return "";
	}

	/**
	 * 获取技能水平
	 */
	public static String getSkillLevel(Context context, String id, boolean isCHS) {
		String[] skillLevelIdStrings = context.getResources().getStringArray(
				R.array.array_skilllevel_ids);
		String[] skillLevelStrings;
		if (!isCHS) {// en
			skillLevelStrings = context.getResources().getStringArray(
					R.array.array_skilllevel_en);
		} else {// zh
			skillLevelStrings = context.getResources().getStringArray(
					R.array.array_skilllevel_zh);
		}
		for (int i = 0; i < skillLevelIdStrings.length; i++) {
			if (skillLevelIdStrings[i].equals(id)) {
				return skillLevelStrings[i];
			}
		}
		return "";
	}
	/**
	 * 获取职系文字
	 * 
	 * @param id
	 * @return
	 */
	public static String getZhixiString(Context context, String id) {
		if (id == null || id.length() == 0) {
			return "";
		}
		String[] zhixiStrings = context.getResources().getStringArray(
				R.array.array_zhixi_medicine);
		String[] zhixiIdStrings = context.getResources().getStringArray(
				R.array.array_zhixi_medicine_ids);
		for (int i = 0; i < zhixiIdStrings.length; i++) {
			String string = zhixiIdStrings[i];
			if (string.equals(id)) {
				return zhixiStrings[i];
			}
		}
		return "";
	}

	/**
	 * 获取领域
	 * 
	 * @param context
	 * @param industryId
	 * @param id
	 * @return
	 */
	public static String getLingYuString(Context context, String industryId,
			String id) {
		if (id == null || id.length() == 0) {
			return "";
		}
		String[] lingyuStrings = null;
		String[] lingyuIdStrings = null;
		int industry = Integer.parseInt(industryId);
		switch (industry) {
		case 11:// 建筑
			lingyuStrings = context.getResources().getStringArray(
					R.array.lingyu_jianzhu);
			lingyuIdStrings = context.getResources().getStringArray(
					R.array.lingyu_jianzhu_ids);
			break;
		case 12:// 金融
			lingyuStrings = context.getResources().getStringArray(
					R.array.lingyu_jinrong);
			lingyuIdStrings = context.getResources().getStringArray(
					R.array.lingyu_jinrong_ids);
			break;
		case 14:// 医药
			lingyuStrings = context.getResources().getStringArray(
					R.array.lingyu_yiyao);
			lingyuIdStrings = context.getResources().getStringArray(
					R.array.lingyu_yiyao_ids);
			break;
		case 29:// 化工
			lingyuStrings = context.getResources().getStringArray(
					R.array.lingyu_huagong);
			lingyuIdStrings = context.getResources().getStringArray(
					R.array.lingyu_huagong_ids);
			break;
		case 22:// 化工
			lingyuStrings = context.getResources().getStringArray(
					R.array.lingyu_zhizao);
			lingyuIdStrings = context.getResources().getStringArray(
					R.array.lingyu_zhizao_ids);
			break;
		default:
			break;
		}
		if (lingyuStrings != null && lingyuIdStrings != null) {
			for (int i = 0; i < lingyuIdStrings.length; i++) {
				String string = lingyuIdStrings[i];
				if (string.equals(id)) {
					return lingyuStrings[i];
				}
			}
		}
		return "";
	}
}
