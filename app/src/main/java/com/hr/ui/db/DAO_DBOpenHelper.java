package com.hr.ui.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库创建
 */
public class DAO_DBOpenHelper extends SQLiteOpenHelper {

    public DAO_DBOpenHelper(Context context) {
        super(context, DataParam.DBNAME, null, DataParam.DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建简历列表
        db.execSQL("create table if not exists ResumeList (id integer primary key autoincrement,user_id varchar(255) ,"
                + "  resume_id varchar(50),title varchar(200), key_word varchar(200), resume_language varchar(200), "
                + "  resume_type varchar(50) ,"
                + "  open varchar(150) , uptime varchar(150), "
                + "fill_scale varchar(50),castbehalf varchar(50),add_time varchar(50),important varchar(50),"
                + "modify_time varchar(50),isUpdate integer,user_name varchar(50))");
////        // 创建简历列表
//        db.execSQL("create table if not exists  ResumeList (id integer primary key autoincrement,user_id varchar(255) ,"
//                + " resume_id varchar(200) , title varchar(200), key_word varchar(200), resume_language varchar(200), "
//                + "resume_type varchar(50), open varchar(50) , uptime  varchar(50) ,"
//                + " fill_scale varchar(50) , castbehalf varchar(50) , add_time varchar(50), "
//                + "important varchar(50),modify_time varchar(50),"
//                + "isUpdate integer)");

        // 创建个人信息表
        db.execSQL("create table if not exists ResumePersonInfo (id integer primary key autoincrement, user_id varchar(50), name varchar(250),"
                + " sex varchar(250)  , year varchar(250), month varchar(50) , day varchar(50) ,height varchar(50),nationality varchar(50),hukou varchar(50),idnumber varchar(250), "
                + "cardtype integer , marriage integer , echo_yes varchar(50) , ydphone_verify_status varchar(50),"
                + "work_beginyear varchar(250),current_workstate varchar(250),"
                + "location varchar(250), emailaddress varchar(250),address varchar(250),"
                + " zipcode varchar(250),homepage  varchar(250), resume_language varchar(250),"
                + "post_rank  varchar(250), polity varchar(250),blood  varchar(250),"
                + "pic_filekey  varchar(250),ydphone varchar(250),telephone  varchar(250),"
                + "im_type varchar(250),im_account  varchar(250),user_name varchar(50),isUpdate varchar(50),modify_time varchar(50))");

//        // 创建个人信息表
//        db.execSQL("create table if not exists ResumePersonInfo (id integer primary key autoincrement, user_id varchar(50), name varchar(250),"
//                + " sex varchar(250)  , year varchar(250), month varchar(50) , day varchar(50) ,height varchar(50),nationality varchar(50),hukou varchar(50),idnumber varchar(250), "
//                + "cardtype integer , marriage integer , echo_yes varchar(50)"
//                + "work_beginyear varchar(250),current_workstate varchar(250),"
//                + "location varchar(250), emailaddress varchar(250),address varchar(250),"
//                + " zipcode varchar(250),homepage  varchar(250), resume_language varchar(250),"
//                + "post_rank  varchar(250), polity varchar(250),blood  varchar(250),"
//                + "pic_filekey  varchar(250),ydphone varchar(250),telephone  varchar(250),"
//                + "im_type varchar(250),im_account  varchar(250),user_name varchar(50),isUpdate varchar(50),modify_time varchar(50))");

        // 创建求职意向表
        //
        db.execSQL("create table  if not exists ResumeCareerObjective (id integer primary key autoincrement,user_id varchar(255),  "
                + " jobtype varchar(255) ,industry varchar(255),"
                + "func varchar(255), workarea integer, order_salary varchar(2) ,"
                + "order_salary_noshow varchar(250) , resume_id varchar(250), resume_language varchar(250),jobname varchar(250),"
                + " lingyu varchar(100),user_name varchar(100))");


        // 求职意向
        db.execSQL("create table  if not exists ResumeCareerObjective (id integer primary key autoincrement,user_id varchar(255),  "
                + " jobtype varchar(255) ,current_salary_noshow varchar(255),order_salary varchar(255),"
                + "order_salary_noshow varchar(255), resume_id integer, resume_language varchar(2), "
                + "workarea varchar(250) , function varchar(250), lingyu varchar(250), "
                + "last_position varchar(100) ,current_salary varchar(100),current_workstate varchar(100),user_name varchar(100))");
        // 创建证书表

        db.execSQL("create table if not exists  ResumeCertificate (id integer primary key autoincrement,certificate_id varchar(255) , user_id varchar(255) ,  getyear varchar(255), "
                + "getmonth  varchar(255), certname  varchar(250), scores varchar(50), echo_yes varchar(50), cert_filekey varchar(50), "
                + "resume_id varchar(255), resume_language varchar(250),user_name varchar(50))");

        // 创建教育背景表
        db.execSQL("create table  if not exists ResumeEducation ( id integer primary key autoincrement,user_id varchar(255), frommonth varchar(255) , fromyear varchar(255), "
                + "tomonth  varchar(255), toyear  varchar(150), schoolname varchar(150), moremajor varchar(150), "
                + "degree varchar(255), edudetail varchar(250), is_overseas varchar(250), country varchar(150), resume_id varchar(150), resume_language varchar(150),education_id varchar(150),user_name varchar(150))");

        // 创建工作经验表
        db.execSQL("create table if not exists  ResumeWorkExperience (id integer primary key autoincrement,"
                + " user_id integer ,  fromyear varchar(55), frommonth varchar(55), toyear  varchar(50), tomonth  varchar(100),"
                + " company varchar(255), companyhide varchar(255),industry varchar(255),companytype varchar(255), stuffmunber varchar(255), "
                + " division varchar(250),companyaddress varchar(250),position varchar(255), responsibility varchar(250),"
                + " offreason varchar(100), zhixi varchar(100),is_overseas varchar(100),country varchar(100),resume_id varchar(255), "
                + "resume_language varchar(255),lingyu varchar(255),func varchar(255),salary varchar(255),salary_hide varchar(255),experience_id varchar(255),zhicheng varchar(255),user_name varchar(50))");

        // 创建自我评价表
        db.execSQL("create table if not exists  ResumeSelfIntroduction (id integer primary key autoincrement,user_id varchar(255), "
                + "introduction varchar(255) , resume_id varchar(55), resume_language  varchar(20),user_name varchar(50))");

        // 创建项目经验表

        db.execSQL("create table if not exists  ResumeProject (id integer primary key autoincrement,fromyear varchar(255) , frommonth integer ,"
                + "toyear varchar(200), tomonth varchar(200), projectname varchar(200),  position varchar(200), "
                + "projectdesc varchar(200), responsibility varchar(200), resume_id varchar(200), resume_language varchar(250),project_id varchar(250),"
                + "user_name varchar(200),user_id varchar(100))");

        // 创建培训经历表

        db.execSQL("create table if not exists  ResumeTraining (id integer primary key autoincrement,user_id varchar(255), "
                + "fromyear varchar(50) ,frommonth varchar(50), toyear varchar(50), "
                + "tomonth varchar(50),  institution varchar(50), course varchar(50), place varchar(50), "
                + "certification varchar(50), traindetail varchar(250),resume_id varchar(50),resume_language varchar(100),user_name varchar(50),plant_id varchar(50))");

        // 创建个人技能列表
        // db.execSQL("drop table ResumeSkill");

        db.execSQL("create table if not exists  ResumeSkill ( id integer primary key autoincrement, "
                + "user_id varchar(55) ,skilltitle varchar(250),usetime varchar(250),ability varchar(250),resume_id varchar(55), resume_language varchar(50), "
                + "skill_id varchar(255),user_name varchar(50))");
        // 创建语言能力
        db.execSQL("create table  if not exists ResumeLanguageLevel ( id integer primary key autoincrement,langname varchar(250),read_level varchar(250), speak_level varchar(250),user_name varchar(250),user_id varchar(250))");
        // 创建搜索历史记录表
        db.execSQL("create table  if not exists SearchHistory( id integer primary key autoincrement,industry_id varchar(250),function_id varchar(50), place_id  varchar(250),function_value varchar(250), place_value varchar(250))");
        db.execSQL("create table  if not exists KeyWorldHistory( id integer primary key autoincrement,industry_id varchar(250), place_id  varchar(250),place_value varchar(250),search_value varchar(250),wordtype varchar(250))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        /**
//         * 升级数据库版本
//         */
//        if (oldVersion == 1) {
//            db.execSQL("create table  if not exists KeyWorldHistory( id integer primary key autoincrement,industry_id varchar(250), place_id  varchar(250),place_value varchar(250),search_value varchar(250),wordtype varchar(250))");
//        }
    }

}
