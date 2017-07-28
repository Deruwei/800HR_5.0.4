package com.hr.ui.utils.datautils;

import android.content.Context;
import android.widget.Toast;

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
import com.hr.ui.utils.MyUtils;

/**
 * 简历完整度操作类
 *
 * @author 800hr:xuebaohua
 */
public class ResumeComplete {
    private String resumeLanguageString, resumeIdString;
    private DAO_DBOperator dbOperator;

    public ResumeComplete(Context context, String resumeIdString,
                          String resumeLanguageString) {
        this.resumeIdString = resumeIdString;
        this.resumeLanguageString = resumeLanguageString;
        this.dbOperator = new DAO_DBOperator(context);
    }

    /**
     * 计算新建简历或未上传简历完整度
     * <p/>
     * hasExperience是否有工作经验
     * refreshDatabase是否将最新完整度更新到数据库
     *
     * @return
     */
//    public int getFullScale(boolean hasExperience, boolean refreshDatabase) {
//        System.out.println("计算完整度");
//        int count = 0;
//        // 基本项总分大于等于60分，才计算其他项。总分大于100，计100
//        // System.out.println("getFullScale:" + hasExperience);
//        if (hasExperience) {// **************社会人才
//            /*
//             * 基本项
//			 */
//            // 基本信息
//            ResumeBaseInfo baseInfo = dbOperator
//                    .query_ResumePersonInfo_Toone(resumeLanguageString);
//            if (baseInfo != null) {
//                boolean baseInfoCount = true;
//                // 如果结果为true，基本项分值为30，否则为0。
//                if (baseInfo.getName() == null
//                        || baseInfo.getName().length() == 0) {
//                    baseInfoCount = false;
//                } else if (baseInfo.getSex() == null
//                        || baseInfo.getSex().length() == 0) {
//                    baseInfoCount = false;
//                } else if (baseInfo.getYear() == null
//                        || baseInfo.getYear().length() == 0) {
//                    baseInfoCount = false;
//                } else if (baseInfo.getNationality() == null
//                        || baseInfo.getNationality().length() == 0) {
//                    baseInfoCount = false;
//                } else if (baseInfo.getLocation() == null
//                        || baseInfo.getLocation().length() == 0) {
//                    baseInfoCount = false;
//                } else if (baseInfo.getWork_beginyear() == null
//                        || baseInfo.getWork_beginyear().length() == 0
//                        || baseInfo.getWork_beginyear() == "0") {
//                    baseInfoCount = false;
//                } else if (baseInfo.getPost_rank() == null
//                        || baseInfo.getPost_rank().length() == 0) {
//                    baseInfoCount = false;
//                } else if ((baseInfo.getYdphone() == null || baseInfo
//                        .getYdphone().length() == 0)
//                        && (baseInfo.getTelephone() == null || baseInfo
//                        .getTelephone().length() == 0)) {
//                    // 手机和固话同时为空时，则此基本信息不完整
//                    baseInfoCount = false;
//                } else if (baseInfo.getEmailaddress() == null
//                        || baseInfo.getEmailaddress().length() == 0) {
//                    baseInfoCount = false;
//                }
//                if (baseInfoCount) {
//                    count = count + 30;
//                }
//            }
//            // 求职意向
//            ResumeOrder order = dbOperator.query_ResumeCareerObjective_Toone(
//                    resumeIdString, resumeLanguageString);
//            if (order != null) {
//                if ("zh".equals(resumeLanguageString)) {// 中文
//                    // 领域
//                    int numLingYu = 0;
//                    if ("11".equals(MyUtils.industryId)
//                            || "12".equals(MyUtils.industryId)
//                            || "14".equals(MyUtils.industryId)
//                            || "29".equals(MyUtils.industryId)) {// 特殊行业
//                        if (order.getLingyu().trim().toString().length() > 0) {
//                            String[] itemLingyu = order.getLingyu().trim()
//                                    .toString().split(",");
//                            for (String string : itemLingyu) {
//                                if (string.contains(MyUtils.industryId + ":")) {
//                                    ++numLingYu;// 11:111100
//                                }
//                            }
//                            if (numLingYu == 0) {
//                                count = count + 0;// 指定行业无领域，则不计分
//                            } else {
//                                if (baseInfo.getCurrent_workstate() != null) {
//                                    if (baseInfo.getCurrent_workstate().trim()
//                                            .toString().length() == 0
//                                            || order.getFunc().trim().toString()
//                                            .length() == 0
//                                            || !order
//                                            .getFunc()
//                                            .trim()
//                                            .contains(
//                                                    MyUtils.industryId
//                                                            + ":")// 如果不包含当前行业的从事职位
//                                            || order.getWorkarea().trim()
//                                            .toString().length() == 0
////                                        || order.getJobtype().trim().toString()
////                                        .length() == 0
//                                            || order.getOrder_salary().trim()
//                                            .toString().length() == 0) {
//                                        count = count + 0;// 有任何一项为空，则不计分
//                                    } else {
//                                        count = count + 10;
//                                    }
//                                }
//
//                            }
//
//                        }
//                    } else {// 特殊行业以外的行业
//                        if (baseInfo.getCurrent_workstate().trim().toString()
//                                .length() == 0
//                                || order.getFunc().trim().toString().length() == 0
//                                || !order.getFunc().trim()
//                                .contains(MyUtils.industryId + ":")// 如果不包含当前行业的从事职位
//                                || order.getWorkarea().trim().toString()
//                                .length() == 0
//                                || order.getJobtype().trim().toString()
//                                .length() == 0
//                                || order.getOrder_salary().trim().toString()
//                                .length() == 0) {
//                            count = count + 0;// 有任何一项为空，则不计分
//                        } else {
//                            count = count + 10;
//                        }
//                    }
//                    count = count + 10;
//                } else {// 英文
//                    if (baseInfo.getCurrent_workstate().trim().toString()
//                            .length() == 0
//                            || order.getJobname().trim().toString().length() == 0
//                            || order.getWorkarea().trim().toString().length() == 0
//                            || order.getJobtype().trim().toString().length() == 0
//                            || order.getOrder_salary().trim().toString()
//                            .length() == 0) {
//                        count = count + 0;// 有任何一项为空，则不计分
//                    } else {
//                        count = count + 10;
//                    }
//                }
//            }
//            // 工作经验
//            ResumeExperience[] experiences = dbOperator
//                    .query_ResumeWorkExperience(resumeIdString,
//                            resumeLanguageString);
//            if (experiences != null && experiences.length > 0) {
//                count = count + 10;
//            }
//            // 教育背景
//            ResumeEducation[] educations = dbOperator.query_ResumeEducation(
//                    resumeIdString, resumeLanguageString);
//            if (educations != null && educations.length > 0) {
//                count = count + 10;
//            }
//            // System.out.println("基本相得分：" + count);
//            /*
//             * 其他项
//			 */
//            if (count >= 60) {
//                // 培训背景
//                ResumePlant[] plants = dbOperator.query_ResumeTraining(
//                        resumeIdString, resumeLanguageString);
//                if (plants != null && plants.length > 0) {
//                    count = count + 5;
//                }
//                // 语言能力
//                ResumeLanguageLevel[] languageLevels = dbOperator
//                        .query_ResumeLanguageLevel();
//                if (languageLevels != null && languageLevels.length > 0) {
//                    count = count + 5;
//                }
////                // IT技能
////                ResumeSkill[] skills = dbOperator.query_ResumeSkill(
////                        resumeIdString, resumeLanguageString);
////                if (skills != null && skills.length > 0) {
////                    count = count + 5;
////                }
//                // 项目经验
//                ResumeProject[] projects = dbOperator.query_Resumeitem(
//                        resumeIdString, resumeLanguageString);
//                if (projects != null && projects.length > 0) {
//                    count = count + 5;
//                }
//                // 自我评价
//                ResumeAssessInfo assessInfo = dbOperator
//                        .query_ResumeTome_Toone(resumeIdString,
//                                resumeLanguageString);
//                if (assessInfo != null) {
//                    count = count + 5;
//                }
//                // 个人照片
//                if (baseInfo != null) {
//                    String pictureString = baseInfo.getPic_filekey();
//                    if (pictureString != null && pictureString.length() > 0) {
//                        count = count + 5;
//                    }
//                }
//            }
//        } else {// ***************毕业生简历
//            /*
//             * 基本项
//			 */
//            // 基本信息
//            ResumeBaseInfo baseInfo = dbOperator
//                    .query_ResumePersonInfo_Toone(resumeLanguageString);
//            if (baseInfo != null) {
//                boolean baseInfoCount = true;// 如果结果为true，基本项分值为35，否则为0。
//                if (baseInfo.getName() == null
//                        || baseInfo.getName().length() == 0) {
//                    baseInfoCount = false;
//                } else if (baseInfo.getSex() == null
//                        || baseInfo.getSex().length() == 0) {
//                    baseInfoCount = false;
//                } else if (baseInfo.getYear() == null
//                        || baseInfo.getYear().length() == 0) {
//                    baseInfoCount = false;
//                } else if (baseInfo.getNationality() == null
//                        || baseInfo.getNationality().length() == 0) {
//                    baseInfoCount = false;
//                } else if (baseInfo.getLocation() == null
//                        || baseInfo.getLocation().length() == 0) {
//                    baseInfoCount = false;
//                } else if (baseInfo.getWork_beginyear() == null
//                        || baseInfo.getWork_beginyear().length() == 0) {
//                    baseInfoCount = false;
//                } else if (baseInfo.getPost_rank() == null
//                        || baseInfo.getPost_rank().length() == 0) {
//                    baseInfoCount = false;
//                } else if ((baseInfo.getYdphone() == null || baseInfo
//                        .getYdphone().length() == 0)
//                        && (baseInfo.getTelephone() == null || baseInfo
//                        .getTelephone().length() == 0)) {
//                    // 手机和固话同时为空时，则此基本信息不完整
//                    baseInfoCount = false;
//                } else if (baseInfo.getEmailaddress() == null
//                        || baseInfo.getEmailaddress().length() == 0) {
//                    baseInfoCount = false;
//                }
//                if (baseInfoCount) {
//                    count = count + 35;
////                    count = count + 35;
//                }
//            }
//            // 求职意向
//            ResumeOrder order = dbOperator.query_ResumeCareerObjective_Toone(
//                    resumeIdString, resumeLanguageString);
//            if (order != null) {
//                count = count + 10;
//            }
//
//            // 教育背景
//            ResumeEducation[] educations = dbOperator.query_ResumeEducation(
//                    resumeIdString, resumeLanguageString);
//            if (educations != null && educations.length > 0) {
//                count = count + 15;
//            }
//            /*
//             * 其他项
//			 */
//            if (count >= 60) {
//                // 工作经验
//                ResumeExperience[] experiences = dbOperator
//                        .query_ResumeWorkExperience(resumeIdString,
//                                resumeLanguageString);
//                if (experiences != null && experiences.length > 0) {
//                    count = count + 5;
//                }
//                // 培训背景
//                ResumePlant[] plants = dbOperator.query_ResumeTraining(
//                        resumeIdString, resumeLanguageString);
//                if (plants != null && plants.length > 0) {
//                    count = count + 5;
//                }
//                // 语言能力
//                ResumeLanguageLevel[] languageLevels = dbOperator
//                        .query_ResumeLanguageLevel();
//                if (languageLevels != null && languageLevels.length > 0) {
//                    count = count + 5;
//                }
//                // 专业技能
//                ResumeSkill[] skills = dbOperator.query_ResumeSkill(resumeIdString, resumeLanguageString);
//
//                if (skills != null && skills.length > 0) {
//                    count = count + 5;
//                }
//
//                // 项目经验
//                ResumeProject[] projects = dbOperator.query_Resumeitem(resumeIdString, resumeLanguageString);
//                if (projects != null && projects.length > 0) {
//                    count = count + 5;
//                }
//                // 自我评价
//                ResumeAssessInfo assessInfo = dbOperator.query_ResumeTome_Toone(resumeIdString, resumeLanguageString);
//                if (assessInfo != null) {
//                    count = count + 5;
//                }
////                // 个人照片
////                if (baseInfo != null) {
////                    String pictureString = baseInfo.getPic_filekey();
////                    if (pictureString != null && pictureString.length() > 0) {
////                        count = count + 5;
////                    }
////                }
//
//            }
//        }
//        if (refreshDatabase) {
//            ResumeTitle resumeTitle = dbOperator.query_ResumeTitle_info(
//                    resumeIdString, resumeLanguageString);
//            if (resumeTitle != null) {
//                resumeTitle.setFill_scale(count + "");
//                dbOperator.update_ResumeList(resumeTitle);
//            }
//        }
//        System.out.println(count);
//        return count;
//    }
    public int getFullScale(boolean hasExperience, boolean refreshDatabase) {
        System.out.println("计算完整度");
        int count = 0;
        // 基本项总分大于等于60分，才计算其他项。总分大于100，计100
        //System.out.println("getFullScale:" + hasExperience);
        if (hasExperience) {// **************社会人才
            /*
             * 基本项
			 */
            // 基本信息
            ResumeBaseInfo baseInfo = dbOperator
                    .query_ResumePersonInfo_Toone(resumeLanguageString);
            if (baseInfo != null) {
                boolean baseInfoCount = true;// 如果结果为true，基本项分值为30，否则为0。
                if (baseInfo.getName() == null
                        || baseInfo.getName().length() == 0) {
                    baseInfoCount = false;
                } else if (baseInfo.getSex() == null
                        || baseInfo.getSex().length() == 0) {
                    baseInfoCount = false;
                } else if (baseInfo.getYear() == null
                        || baseInfo.getYear().length() == 0) {
                    baseInfoCount = false;
//                } else if (baseInfo.getNationality() == null
//                        || baseInfo.getNationality().length() == 0) {
//                    baseInfoCount = false;
                } else if (baseInfo.getLocation() == null
                        || baseInfo.getLocation().length() == 0) {
                    baseInfoCount = false;
                } else if (baseInfo.getWork_beginyear() == null
                        || baseInfo.getWork_beginyear().length() == 0
                        || baseInfo.getWork_beginyear() == "0") {
                    baseInfoCount = false;
                } else if (baseInfo.getPost_rank() == null
                        || baseInfo.getPost_rank().length() == 0) {
                    baseInfoCount = false;
                } else if ((baseInfo.getYdphone() == null || baseInfo
                        .getYdphone().length() == 0)
                        && (baseInfo.getTelephone() == null || baseInfo
                        .getTelephone().length() == 0)) {
                    // 手机和固话同时为空时，则此基本信息不完整
                    baseInfoCount = false;
                } else if (baseInfo.getEmailaddress() == null
                        || baseInfo.getEmailaddress().length() == 0) {
                    baseInfoCount = false;
                }
                if (baseInfoCount) {
                    count = count + 30;
                }
            }
            // 求职意向
            ResumeOrder order = dbOperator.query_ResumeCareerObjective_Toone(
                    resumeIdString, resumeLanguageString);
            if (order != null) {
                if ("zh".equals(resumeLanguageString)) {// 中文
                    // 领域
                    int numLingYu = 0;
                    if ("11".equals(MyUtils.industryId)
                            || "12".equals(MyUtils.industryId)
                            || "14".equals(MyUtils.industryId)
                            || "29".equals(MyUtils.industryId)
                            || "22".equals(MyUtils.industryId)) {//特殊行业
                        if (order.getLingyu().trim().toString().length() > 0) {
                            String[] itemLingyu = order.getLingyu().trim()
                                    .toString().split(",");
                            for (String string : itemLingyu) {
                                if (string.contains(MyUtils.industryId + ":")) {
                                    ++numLingYu;// 11:111100
                                }
                            }
                            if (numLingYu == 0) {
                                count = count + 0;// 指定行業无领域，则不计分
                            } else {
                                if (baseInfo.getCurrent_workstate() != null) {
                                    if (baseInfo.getCurrent_workstate().trim()
                                            .toString().length() == 0
                                            || order.getFunc().trim().toString().length() == 0
                                            || !order.getFunc().trim().contains(MyUtils.industryId + ":")// 如果不包含当前行业的从事职位
                                            || order.getWorkarea().trim()
                                            .toString().length() == 0
                                            || order.getJobtype().trim().toString().length() == 0
                                            || order.getOrder_salary().trim().toString().length() == 0) {
                                        count = count + 0;// 有任何一项为空，则不计分
                                    } else {
                                        count = count + 10;
                                    }
                                }
                            }

                        }
                    } else {//特殊行业以外的行业
                        if (baseInfo.getCurrent_workstate() != null) {
                            if (baseInfo.getCurrent_workstate().trim().toString()
                                    .length() == 0
                                    || order.getFunc().trim().toString().length() == 0
                                    || !order.getFunc().trim()
                                    .contains(MyUtils.industryId + ":")// 如果不包含当前行业的从事职位
                                    || order.getWorkarea().trim().toString().length() == 0
                                    || order.getJobtype().trim().toString().length() == 0
                                    || order.getOrder_salary().trim().toString()
                                    .length() == 0) {
                                count = count + 0;// 有任何一项为空，则不计分
                            } else {
                                count = count + 10;
                            }
                        }
                    }

                } else {// 英文
                    if (baseInfo.getCurrent_workstate() != null) {
                        if (baseInfo.getCurrent_workstate().trim().toString().length() == 0
                                || order.getJobname().trim().toString().length() == 0
                                || order.getWorkarea().trim().toString().length() == 0
                                || order.getJobtype().trim().toString().length() == 0
                                || order.getOrder_salary().trim().toString().length() == 0) {
                            count = count + 0;// 有任何一项为空，则不计分
                        } else {
                            count = count + 10;
                        }
                    }
                }

            }
            // 工作经验
            ResumeExperience[] experiences = dbOperator.query_ResumeWorkExperience(resumeIdString, resumeLanguageString);
            if (experiences != null && experiences.length > 0) {
                count = count + 10;
            }
            // 教育背景
            ResumeEducation[] educations = dbOperator.query_ResumeEducation(resumeIdString, resumeLanguageString);
            if (educations != null && educations.length > 0) {
                count = count + 10;
            }
            //System.out.println("基本相得分：" + count);
            /*
			 * 其他项
			 */
            if (count >= 60) {
                // 培训背景
                ResumePlant[] plants = dbOperator.query_ResumeTraining(
                        resumeIdString, resumeLanguageString);
                if (plants != null && plants.length > 0) {
                    count = count + 5;
                }
                // 语言能力
                ResumeLanguageLevel[] languageLevels = dbOperator
                        .query_ResumeLanguageLevel();
                if (languageLevels != null && languageLevels.length > 0) {
                    count = count + 5;
                }
                // IT技能
                ResumeSkill[] skills = dbOperator.query_ResumeSkill(
                        resumeIdString, resumeLanguageString);
                if (skills != null && skills.length > 0) {
                    count = count + 5;
                }
                // 项目经验
                ResumeProject[] projects = dbOperator.query_Resumeitem(
                        resumeIdString, resumeLanguageString);
                if (projects != null && projects.length > 0) {
                    count = count + 5;
                }
                // 自我评价
                ResumeAssessInfo assessInfo = dbOperator
                        .query_ResumeTome_Toone(resumeIdString,
                                resumeLanguageString);
                if (assessInfo != null) {
                    count = count + 5;
                }
                // 个人照片
                if (baseInfo != null) {
                    String pictureString = baseInfo.getPic_filekey();
                    if (pictureString != null && pictureString.length() > 0) {
                        count = count + 5;
                    }
                }
            }
        } else {// ***************毕业生简历
			/*
			 * 基本项
			 */
            // 基本信息
            ResumeBaseInfo baseInfo = dbOperator
                    .query_ResumePersonInfo_Toone(resumeLanguageString);
            if (baseInfo != null) {
                boolean baseInfoCount = true;// 如果结果为true，基本项分值为35，否则为0。
                if (baseInfo.getName() == null
                        || baseInfo.getName().length() == 0) {
                    baseInfoCount = false;
                } else if (baseInfo.getSex() == null
                        || baseInfo.getSex().length() == 0) {
                    baseInfoCount = false;
                } else if (baseInfo.getYear() == null
                        || baseInfo.getYear().length() == 0) {
                    baseInfoCount = false;
//                } else if (baseInfo.getNationality() == null
//                        || baseInfo.getNationality().length() == 0) {
//                    baseInfoCount = false;
                } else if (baseInfo.getLocation() == null
                        || baseInfo.getLocation().length() == 0) {
                    baseInfoCount = false;
                } else if (baseInfo.getWork_beginyear() == null
                        || baseInfo.getWork_beginyear().length() == 0) {
                    baseInfoCount = false;
                } else if (baseInfo.getPost_rank() == null
                        || baseInfo.getPost_rank().length() == 0) {
                    baseInfoCount = false;
                } else if ((baseInfo.getYdphone() == null || baseInfo
                        .getYdphone().length() == 0)
                        && (baseInfo.getTelephone() == null || baseInfo
                        .getTelephone().length() == 0)) {
                    // 手机和固话同时为空时，则此基本信息不完整
                    baseInfoCount = false;
                } else if (baseInfo.getEmailaddress() == null
                        || baseInfo.getEmailaddress().length() == 0) {
                    baseInfoCount = false;
                }
                if (baseInfoCount) {
                    count = count + 35;
                }
            }
            // 求职意向
            ResumeOrder order = dbOperator.query_ResumeCareerObjective_Toone(
                    resumeIdString, resumeLanguageString);
            if (order != null) {
                if ("zh".equals(resumeLanguageString)) {// 中文
                    // 领域
                    int numLingYu = 0;
                    if ("11".equals(MyUtils.industryId)
                            || "12".equals(MyUtils.industryId)
                            || "14".equals(MyUtils.industryId)
                            || "29".equals(MyUtils.industryId)) {//特殊行业
                        if (order.getLingyu().trim().toString().length() > 0) {
                            String[] itemLingyu = order.getLingyu().trim()
                                    .toString().split(",");
                            for (String string : itemLingyu) {
                                if (string.contains(MyUtils.industryId + ":")) {
                                    ++numLingYu;// 11:111100
                                }
                            }
                            if (numLingYu == 0) {
                                count = count + 0;// 指定行業无领域，则不计分
                            } else {
                                if (baseInfo.getCurrent_workstate() != null) {
                                    if (baseInfo.getCurrent_workstate().trim()
                                            .toString().length() == 0
                                            || order.getFunc().trim().toString().length() == 0
                                            || !order.getFunc().trim().contains(MyUtils.industryId + ":")// 如果不包含当前行业的从事职位
                                            || order.getWorkarea().trim()
                                            .toString().length() == 0
                                            || order.getJobtype().trim().toString().length() == 0
                                            || order.getOrder_salary().trim().toString().length() == 0) {
                                        count = count + 0;// 有任何一项为空，则不计分
                                    } else {
                                        count = count + 10;
                                    }
                                }
                            }

                        }
                    } else {//特殊行业以外的行业
                        if (baseInfo.getCurrent_workstate().trim().toString()
                                .length() == 0
                                || order.getFunc().trim().toString().length() == 0
                                || !order.getFunc().trim()
                                .contains(MyUtils.industryId + ":")// 如果不包含当前行业的从事职位
                                || order.getWorkarea().trim().toString().length() == 0
                                || order.getJobtype().trim().toString().length() == 0
                                || order.getOrder_salary().trim().toString()
                                .length() == 0) {
                            count = count + 0;// 有任何一项为空，则不计分
                        } else {
                            count = count + 10;
                        }
                    }

                } else {// 英文
                    if (baseInfo.getCurrent_workstate().trim().toString()
                            .length() == 0
                            || order.getJobname().trim().toString().length() == 0
                            || order.getWorkarea().trim().toString().length() == 0
                            || order.getJobtype().trim().toString().length() == 0
                            || order.getOrder_salary().trim().toString()
                            .length() == 0) {
                        count = count + 0;// 有任何一项为空，则不计分
                    } else {
                        count = count + 10;
                    }
                }
            }
            // 教育背景
            ResumeEducation[] educations = dbOperator.query_ResumeEducation(
                    resumeIdString, resumeLanguageString);
            if (educations != null && educations.length > 0) {
                count = count + 15;
            }
			/*
			 * 其他项
			 */
            if (count >= 60) {
                // 工作经验
                ResumeExperience[] experiences = dbOperator
                        .query_ResumeWorkExperience(resumeIdString,
                                resumeLanguageString);
                if (experiences != null && experiences.length > 0) {
                    count = count + 5;
                }
                // 培训背景
                ResumePlant[] plants = dbOperator.query_ResumeTraining(
                        resumeIdString, resumeLanguageString);
                if (plants != null && plants.length > 0) {
                    count = count + 5;
                }
                // 语言能力
                ResumeLanguageLevel[] languageLevels = dbOperator
                        .query_ResumeLanguageLevel();
                if (languageLevels != null && languageLevels.length > 0) {
                    count = count + 5;
                }
                // IT技能
                ResumeSkill[] skills = dbOperator.query_ResumeSkill(
                        resumeIdString, resumeLanguageString);
                if (skills != null && skills.length > 0) {
                    count = count + 5;
                }
                // 项目经验
                ResumeProject[] projects = dbOperator.query_Resumeitem(
                        resumeIdString, resumeLanguageString);
                if (projects != null && projects.length > 0) {
                    count = count + 5;
                }
                // 自我评价
                ResumeAssessInfo assessInfo = dbOperator
                        .query_ResumeTome_Toone(resumeIdString,
                                resumeLanguageString);
                if (assessInfo != null) {
                    count = count + 5;
                }
                // 个人照片
                if (baseInfo != null) {
                    String pictureString = baseInfo.getPic_filekey();
                    if (pictureString != null && pictureString.length() > 0) {
                        count = count + 5;
                    }
                }

            }
        }
        if (refreshDatabase) {
            ResumeTitle resumeTitle = dbOperator.query_ResumeTitle_info(
                    resumeIdString, resumeLanguageString);
            if (resumeTitle != null) {
                resumeTitle.setFill_scale(count + "");
                dbOperator.update_ResumeList(resumeTitle);
            }
        }
        System.out.println(count);
        return count;
    }
}
