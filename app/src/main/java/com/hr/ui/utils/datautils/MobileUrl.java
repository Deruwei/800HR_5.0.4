package com.hr.ui.utils.datautils;

import com.hr.ui.R;
import com.hr.ui.config.Constants;
import com.hr.ui.utils.MyUtils;

/**
 * 拼接手机链接
 */
public class MobileUrl {
    /**
     * 获取公司手机链接
     *
     * @param e_id
     * @return
     */
    public static String getCompanyUrl(String e_id) {
        StringBuilder builder = new StringBuilder("http://m.");
        if (MyUtils.industryId.equals("11")) {// 建筑行业
            builder.append("buildhr");
        } else if (MyUtils.industryId.equals("12")) {// 金融行业
            builder.append("bankhr");
        } else if (MyUtils.industryId.equals("13")) {// 传媒行业
            builder.append("media.800hr");
        } else if (MyUtils.industryId.equals("14")) {// 医药行业
            builder.append("healthr");
        } else if (MyUtils.industryId.equals("15")) {// 教培行业
            builder.append("edu.800hr");
        } else if (MyUtils.industryId.equals("19")) {// 电子行业
            builder.append("ele.800hr");
        } else if (MyUtils.industryId.equals("22")) {// 机械行业
            builder.append("michr");
        } else if (MyUtils.industryId.equals("26")) {// 服装行业
            builder.append("clothr");
        } else if (MyUtils.industryId.equals("29")) {// 化工行业
            builder.append("chenhr");
        }else if (MyUtils.industryId.equals("23")) {// IT行业
            builder.append("it.800hr");
        }
        else if (MyUtils.industryId.equals("40")) {// 酒店行业
            builder.append("hotel.800hr");
        }
        else if (MyUtils.industryId.equals("16")) {// 运输行业
            builder.append("56.800hr");
        }
        else if (MyUtils.industryId.equals("21")) {// 通信行业
            builder.append("telecom.800hr");
        }
        else if (MyUtils.industryId.equals("20")) {// 电力行业
            builder.append("ep.800hr");
        }
        else if (MyUtils.industryId.equals("30")) {// 旅游行业
            builder.append("tour.800hr");
        }
        else if (MyUtils.industryId.equals("6")) {// 旅游行业
            builder.append("tour.800hr");
        }
        builder.append(".com/company/");
        builder.append(e_id);

        return builder.toString();
    }

    public static String getJobUrl(String j_id) {

        StringBuilder builder = new StringBuilder("http://m.");
        if (MyUtils.industryId.equals("11")) {// 建筑行业
            builder.append("buildhr");
        } else if (MyUtils.industryId.equals("12")) {// 金融行业
            builder.append("bankhr");
        } else if (MyUtils.industryId.equals("13")) {// 传媒行业
            builder.append("media.800hr");
        } else if (MyUtils.industryId.equals("14")) {// 医药行业
            builder.append("healthr");
        } else if (MyUtils.industryId.equals("15")) {// 教培行业
            builder.append("edu.800hr");
        } else if (MyUtils.industryId.equals("19")) {// 电子行业
            builder.append("ele.800hr");
        } else if (MyUtils.industryId.equals("22")) {// 机械行业
            builder.append("michr");
        } else if (MyUtils.industryId.equals("26")) {// 服装行业
            builder.append("clothr");
        } else if (MyUtils.industryId.equals("29")) {// 化工行业
            builder.append("chenhr");
        } else if (MyUtils.industryId.equals("23")) {// IT行业
            builder.append("it.800hr");
        }
        else if (MyUtils.industryId.equals("40")) {// 酒店行业
            builder.append("hotel.800hr");
        }
        else if (MyUtils.industryId.equals("16")) {// 运输行业
            builder.append("56.800hr");
        }
        else if (MyUtils.industryId.equals("21")) {// 通信行业
            builder.append("telecom.800hr");
        }
        else if (MyUtils.industryId.equals("20")) {// 电力行业
            builder.append("ep.800hr");
        }
        else if (MyUtils.industryId.equals("30")) {// 旅游行业
            builder.append("tour.800hr");
        }
        builder.append(".com/job/");
        builder.append(j_id);
        builder.append(".html");

        return builder.toString();

    }

}
