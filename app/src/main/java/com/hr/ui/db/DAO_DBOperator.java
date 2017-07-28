package com.hr.ui.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hr.ui.model.HistoyInfo;
import com.hr.ui.model.KeyWorldHistory;
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

import java.util.ArrayList;

/**
 * 数据库操作
 */
public class DAO_DBOperator {
    public SQLiteDatabase db;

    public DAO_DBOperator(Context context) {
        DatabaseManager.initialize(new DAO_DBOpenHelper(context));
    }

    /**
     * open database
     */
    public void openDB() {
        db = DatabaseManager.getInstance().openDatabase();
    }

    /**
     * close database
     */
    public void closeDB() {
        // correct way
        DatabaseManager.getInstance().closeDatabase();
        // error way--db.close();
    }

    /**
     * method:update database
     *
     * @param table
     * @param values
     * @param whereClause
     * @param whereArgs
     */
    public void update(String table, ContentValues values, String whereClause,
                       String[] whereArgs) {
        openDB();
        db.update(table, values, whereClause, whereArgs);
        closeDB();
    }

    // ------------------>>>>>>>>>>>查询简历修改时间<<<<<<<<<<<<<--------------------------
    public synchronized String query_upResumeTime(String resume_language,
                                                  int resume_id) {
        // TODO Auto-generated method stub
        openDB();
        Cursor cursor;
        String strSQLWhere = "SELECT  modify_time FROM ResumeList" + " where "
                + "resume_language ='" + resume_language + "'"
                + " and resume_id ='" + resume_id + "'";
        // cursor = db.query("ResumeList",
        // new String[] {"resume_language", "uptime", "resume_id"},
        // strSQLWhere, null, null, null, null);

        cursor = db.rawQuery(strSQLWhere, null);
        String str = null;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                str = cursor.getString(0);
            }
            cursor.close();
        }
        closeDB();
        return str;
    }

    // ---------------------->>>>>>>>>>>>>>
    // 查询本地简历是否更新过<<<<<<<<<<<<<<<<---------------------
    public boolean IS_Resume_Update(String resume_language, int resume_id) {
        // TODO Auto-generated method stub
        openDB();
        Cursor cursor;
        String strSQLWhere = "SELECT isUpdate  FROM ResumeList" + " where "
                + "user_id ='" + MyUtils.userID + "' and "
                + "resume_language ='" + resume_language + "'"
                + " and resume_id ='" + resume_id + "'";
        cursor = db.rawQuery(strSQLWhere, null);
        int INumber = 0;
        // String str = null;
        if (cursor != null) {

            while (cursor.moveToNext()) {
                INumber = cursor.getInt(0);
            }
            cursor.close();
        }
        closeDB();
        return INumber == 0 ? false : true;

    }

    // ------------------>>>>>>>>>>>>简历列表<<<<<<<<<<<_----------------------------

    // 插入简历列表
    public int Insert_ResumeList(ResumeTitle resumeTitle) {
        // TODO Auto-generated method stub
        openDB();
        ContentValues values = new ContentValues();
        values.put("user_id", resumeTitle.getUser_id());
        values.put("resume_id", resumeTitle.getResume_id());
        values.put("title", resumeTitle.getTitle());
        values.put("key_word", resumeTitle.getKey_word());
        values.put("resume_language", resumeTitle.getResume_language());
        values.put("resume_id", resumeTitle.getResume_id());
        values.put("resume_type", resumeTitle.getResume_type());
        values.put("open", resumeTitle.getOpen());
        values.put("uptime", resumeTitle.getUptime());
        values.put("fill_scale", resumeTitle.getFill_scale());
        values.put("castbehalf", resumeTitle.getCastbehalf());
        values.put("add_time", resumeTitle.getAdd_time());
        values.put("important", resumeTitle.getImportant());
        values.put("modify_time", resumeTitle.getModify_time());
        values.put("isUpdate", resumeTitle.getIsUpdate());
        long result = db.insert("ResumeList", null, values);
        int strid = -1; // 如果strid为-1，数据插入失败
        if (result > 0) {
            Cursor cursor = db.rawQuery(
                    "select last_insert_rowid() from ResumeList", null);
            if (cursor.moveToFirst())
                strid = cursor.getInt(0);
            Log.d("SQLITE", "简历列表插入成功");
            cursor.close();
        } else {
            Log.d("SQLITE", "简历列表插入失败");
        }
        closeDB();
        return strid;
    }

    // 修改简历列表的数据
    public boolean update_ResumeList(ResumeTitle resumeTitle) {
        // TODO Auto-generated method stub
        openDB();
        ContentValues values = new ContentValues();

        String strSQLWhere = "id=" + resumeTitle.getId();
        values.put("user_id", resumeTitle.getUser_id());
        values.put("resume_id", resumeTitle.getResume_id());
        values.put("title", resumeTitle.getTitle());
        values.put("key_word", resumeTitle.getKey_word());
        values.put("resume_language", resumeTitle.getResume_language());
        values.put("resume_type", resumeTitle.getResume_type());
        values.put("open", resumeTitle.getOpen());
        values.put("uptime", resumeTitle.getUptime());
        values.put("fill_scale", resumeTitle.getFill_scale());
        values.put("castbehalf", resumeTitle.getCastbehalf());
        values.put("add_time", resumeTitle.getAdd_time());
        values.put("important", resumeTitle.getImportant());
        values.put("modify_time", resumeTitle.getModify_time());
        values.put("isUpdate", resumeTitle.getIsUpdate());
        int iNumber = db.update("ResumeList", values,
                "id=" + resumeTitle.getId(), null);
        closeDB();
        if (iNumber == 0) {
            Log.d("iNumber", "修改简历列表失败");
            return false;
        } else {
            Log.d("iNumber", "修改简历列表成功");
            return true;
        }
    }

    // 查询简历列表下的数据
    public ResumeTitle[] query_ResumeList(String resume_language) {
        // TODO Auto-generated method stub
        openDB();
        Cursor cursor;
        String strSQLWhere = "user_id='" + MyUtils.userID + "' and  "
                + " resume_language= '" + resume_language + "'";

        cursor = db.query("ResumeList", new String[]{"user_id", "resume_id",
                        "title", "key_word", "resume_language", "resume_type", "open",
                        "uptime", "fill_scale", "castbehalf", "add_time", "important",
                        "modify_time", "isUpdate", "id"}, strSQLWhere, null, null,
                null, null);
        ResumeTitle retresumeTitle[] = new ResumeTitle[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            retresumeTitle[i] = new ResumeTitle();
            retresumeTitle[i].setUser_id(cursor.getString(0));// 获取第一列的值,第一列的索引从0开始
            retresumeTitle[i].setResume_id(cursor.getString(1));
            retresumeTitle[i].setTitle(cursor.getString(2));
            retresumeTitle[i].setKey_word(cursor.getString(3));
            retresumeTitle[i].setResume_language(cursor.getString(4));
            retresumeTitle[i].setResume_type(cursor.getString(5));
            retresumeTitle[i].setOpen(cursor.getString(6));
            retresumeTitle[i].setUptime(cursor.getString(7));
            retresumeTitle[i].setFill_scale(cursor.getString(8));
            retresumeTitle[i].setCastbehalf(cursor.getString(9));
            retresumeTitle[i].setAdd_time(cursor.getString(10));
            retresumeTitle[i].setImportant(cursor.getString(11));
            retresumeTitle[i].setModify_time(cursor.getString(12));
            retresumeTitle[i].setIsUpdate(cursor.getInt(13));
            retresumeTitle[i].setId(cursor.getInt(14));

            i++;
        }
        cursor.close();
        closeDB();
        return retresumeTitle;
    }

    // ----------------->>>>>>>>>>>>>>>>>>>>>>>查询Title_info<<<<<<<<<<<<<<<<-----------------------------------
    // 查询Title_info的数据
    public ResumeTitle query_ResumeTitle_info(String resume_id,
                                              String resume_language) {
        // TODO Auto-generated method stub
        openDB();
        Cursor cursor;
        String strSQLWhere = "user_id='" + MyUtils.userID + "' and "
                + " resume_id ='" + resume_id + "' and "
                + " resume_language= '" + resume_language + "'";

        // System.out.println("SQL:" + strSQLWhere);

        cursor = db.query("ResumeList", new String[]{"user_id", "resume_id",
                        "title", "key_word", "resume_language", "resume_type", "open",
                        "uptime", "fill_scale", "castbehalf", "add_time", "important",
                        "modify_time", "isUpdate", "id"}, strSQLWhere, null, null,
                null, null);
        ResumeTitle retresumeTitle = null;
        if (cursor.moveToFirst()) {
            retresumeTitle = new ResumeTitle();
            retresumeTitle.setUser_id(cursor.getString(0));// 获取第一列的值,第一列的索引从0开始
            retresumeTitle.setResume_id(cursor.getString(1));
            retresumeTitle.setTitle(cursor.getString(2));
            retresumeTitle.setKey_word(cursor.getString(3));
            retresumeTitle.setResume_language(cursor.getString(4));
            retresumeTitle.setResume_type(cursor.getString(5));
            retresumeTitle.setOpen(cursor.getString(6));
            retresumeTitle.setUptime(cursor.getString(7));
            retresumeTitle.setFill_scale(cursor.getString(8));
            retresumeTitle.setCastbehalf(cursor.getString(9));
            retresumeTitle.setAdd_time(cursor.getString(10));
            retresumeTitle.setImportant(cursor.getString(11));
            retresumeTitle.setModify_time(cursor.getString(12));
            retresumeTitle.setIsUpdate(cursor.getInt(13));
            retresumeTitle.setId(cursor.getInt(14));
        }
        cursor.close();
        closeDB();
        return retresumeTitle;
    }

    // 修改Title_info的数据
    public boolean update_ResumeTitle_info(ResumeTitle resumeTitle) {
        // TODO Auto-generated method stub
        openDB();
        ContentValues values = new ContentValues();
        String strSQLWhere = "user_id='" + MyUtils.userID + "' and  resume_id ='" + resumeTitle.getResume_id() + "' and resume_language='" + resumeTitle.getResume_language() + "'";
//        String strSQLWhere = "user_id='" + MyUtils.userID + "' and resume_language='" + resumeTitle.getResume_language() + "'";
        // String strSQLWhere=
        // "user_name='"+WorkExperience.getUser_name()+"' and  resume_id="+WorkExperience.getResume_id()+" and  resume_language= '"+WorkExperience.getResume_language();
        values.put("user_id", resumeTitle.getUser_id());
        values.put("resume_id", resumeTitle.getResume_id());
        values.put("title", resumeTitle.getTitle());
        values.put("key_word", resumeTitle.getKey_word());
        values.put("resume_language", resumeTitle.getResume_language());
        values.put("resume_type", resumeTitle.getResume_type());
        values.put("open", resumeTitle.getOpen());
        values.put("uptime", resumeTitle.getUptime());
        values.put("fill_scale", resumeTitle.getFill_scale());
        values.put("castbehalf", resumeTitle.getCastbehalf());
        values.put("add_time", resumeTitle.getAdd_time());
        values.put("important", resumeTitle.getImportant());
        values.put("modify_time", resumeTitle.getModify_time());
        values.put("isUpdate", resumeTitle.getIsUpdate());
        values.put("id", resumeTitle.getId());
        int iNumber = db.update("ResumeList", values, strSQLWhere, null);
        closeDB();
        if (iNumber == 0) {
            Log.d("iNumber", "修改个人信息失败");
            return false;
        } else {
            Log.d("iNumber", "修改个人信息成功");
            return true;
        }
    }

    // -------------------->>>>>>>>>>>>>>>>>>>>>>个人信息表<<<<<<<<<<<<<<<<<<<<<<<<<<<<------------------------------------
    // 插入个人信息
    public long Insert_ResumePersonInfo(ResumeBaseInfo resumeBaseInfo) {
        // TODO Auto-generated method stub

        openDB();
        ContentValues values = new ContentValues();
        values.put("user_id", resumeBaseInfo.getUser_id());
        values.put("name", resumeBaseInfo.getName());
        values.put("sex", resumeBaseInfo.getSex());
        values.put("year", resumeBaseInfo.getYear());
        values.put("month", resumeBaseInfo.getMonth());
        values.put("day", resumeBaseInfo.getDay());
        values.put("height", resumeBaseInfo.getHeight());
        values.put("nationality", resumeBaseInfo.getNationality());
        values.put("hukou", resumeBaseInfo.getHukou());
        values.put("idnumber", resumeBaseInfo.getIdnumber());
        values.put("cardtype", resumeBaseInfo.getCardtype());
        values.put("marriage", resumeBaseInfo.getMarriage());
        values.put("work_beginyear", resumeBaseInfo.getWork_beginyear());
        values.put("location", resumeBaseInfo.getLocation());
        values.put("emailaddress", resumeBaseInfo.getEmailaddress());
        values.put("address", resumeBaseInfo.getAddress());
        values.put("zipcode", resumeBaseInfo.getZipcode());
        values.put("homepage", resumeBaseInfo.getHomepage());
        values.put("resume_language", resumeBaseInfo.getResume_language());
        values.put("post_rank", resumeBaseInfo.getPost_rank());
        values.put("polity", resumeBaseInfo.getPolity());
        values.put("blood", resumeBaseInfo.getBlood());
        values.put("pic_filekey", resumeBaseInfo.getPic_filekey());
        values.put("ydphone", resumeBaseInfo.getYdphone());
        values.put("telephone", resumeBaseInfo.getTelephone());
        values.put("im_type", resumeBaseInfo.getIm_type());
        values.put("im_account", resumeBaseInfo.getIm_account());
        values.put("isUpdate", resumeBaseInfo.getIsUpdate());
        values.put("modify_time", resumeBaseInfo.getModify_time());
        values.put("echo_yes", resumeBaseInfo.getEcho_yes());
        values.put("ydphone_verify_status", resumeBaseInfo.getYdphone_verify_status());

        long result = db.insert("ResumePersonInfo", null, values);
        // int strid = -1; // 如果strid为-1，数据插入失败
        if (result > 0) {
            /*
             * Cursor cursor = db.rawQuery(
			 * "select last_insert_rowid() from ResumePersonInfo", null); if
			 * (cursor.moveToFirst()) strid = cursor.getInt(0); cursor.close();
			 */
            Log.d("SQLITE", "个人信息插入成功");
        } else {
            Log.d("SQLITE", "个人信息插入失败");
        }
        closeDB();
        return result;
    }

    // 修改个人信息的数据
    public boolean update_ResumePersonInfo(ResumeBaseInfo resumeBaseInfo) {
        // TODO Auto-generated method stub
        openDB();
        ContentValues values = new ContentValues();
        String strSQLWhere = "user_id='" + MyUtils.userID
                + "' and resume_language='"
                + resumeBaseInfo.getResume_language() + "'";
        // String strSQLWhere=
        // "user_name='"+WorkExperience.getUser_name()+"' and  resume_id="+WorkExperience.getResume_id()+" and  resume_language= '"+WorkExperience.getResume_language();
        values.put("user_id", resumeBaseInfo.getUser_id());
        values.put("name", resumeBaseInfo.getName());
        values.put("sex", resumeBaseInfo.getSex());
        values.put("year", resumeBaseInfo.getYear());
        values.put("month", resumeBaseInfo.getMonth());
        values.put("day", resumeBaseInfo.getDay());
        values.put("height", resumeBaseInfo.getHeight());
        values.put("nationality", resumeBaseInfo.getNationality());
        values.put("hukou", resumeBaseInfo.getHukou());
        values.put("idnumber", resumeBaseInfo.getIdnumber());
        values.put("cardtype", resumeBaseInfo.getCardtype());
        values.put("marriage", resumeBaseInfo.getMarriage());
        values.put("work_beginyear", resumeBaseInfo.getWork_beginyear());
        values.put("location", resumeBaseInfo.getLocation());
        values.put("emailaddress", resumeBaseInfo.getEmailaddress());
        values.put("address", resumeBaseInfo.getAddress());
        values.put("zipcode", resumeBaseInfo.getZipcode());
        values.put("homepage", resumeBaseInfo.getHomepage());
        values.put("resume_language", resumeBaseInfo.getResume_language());
        values.put("post_rank", resumeBaseInfo.getPost_rank());
        values.put("polity", resumeBaseInfo.getPolity());
        values.put("blood", resumeBaseInfo.getBlood());
        values.put("pic_filekey", resumeBaseInfo.getPic_filekey());
        values.put("ydphone", resumeBaseInfo.getYdphone());
        values.put("telephone", resumeBaseInfo.getTelephone());
        values.put("im_type", resumeBaseInfo.getIm_type());
        values.put("im_account", resumeBaseInfo.getIm_account());
        values.put("current_workstate", resumeBaseInfo.getCurrent_workstate());
        values.put("isUpdate", resumeBaseInfo.getIsUpdate());
        values.put("modify_time", resumeBaseInfo.getModify_time());
        values.put("echo_yes", resumeBaseInfo.getEcho_yes());
        values.put("ydphone_verify_status", resumeBaseInfo.getYdphone_verify_status());

        int iNumber = db.update("ResumePersonInfo", values, strSQLWhere, null);
        closeDB();
        if (iNumber == 0) {
            Log.d("iNumber", "修改个人信息失败");
            return false;
        } else {
            Log.d("iNumber", "修改个人信息成功");
            return true;
        }
    }
    /**
     * // 查询个人信息下的数据 public ResumeBaseInfo[] query_ResumePersonInfo(String
     * user_name, String resume_id, String resume_language) { // TODO
     * Auto-generated method stub openDB(); Cursor cursor; String strSQLWhere =
     * "user_name='" + user_name + "' and  resume_id=" + resume_id +
     * " and  resume_language= '" + resume_language + "'"; // String
     * strSQLWhere=
     *
     * //
     * "user_name='"+resumeTome.getUser_name()+"' and  resume_id="+resumeTome.
     * getResume_id
     * ()+" and  resume_language= '"+resumeTome.getResume_language()+"'"; cursor
     * = db.query("ResumePersonInfo", new String[] { "user_id", "name", "sex",
     * "year", "month", "day", "height", "nationality", "hukou", "idnumber",
     * "cardtype", "marriage", "current_salary", "workyear", "work_beginyear",
     * "high_education", "language", "location", "emailaddress", "address",
     * "zipcode", "homepage", "post_rank", "folk", "folk_other", "pic_filekey",
     * "domain", "name_echo_yes", "telephone", "im_type", "im_account" },
     * strSQLWhere, null, null, null, null); ResumeBaseInfo retresumeBaseInfo[]
     * = new ResumeBaseInfo[cursor .getCount()]; int i = 0;
     *
     * while (cursor.moveToNext()) {
     *
     * retresumeBaseInfo[i] = new ResumeBaseInfo();
     * retresumeBaseInfo[i].setUser_id(cursor.getString(0));//
     * 获取第一列的值,第一列的索引从0开始 retresumeBaseInfo[i].setName(cursor.getString(1));
     * retresumeBaseInfo[i].setSex(cursor.getString(2));
     * retresumeBaseInfo[i].setYear(cursor.getString(3));
     * retresumeBaseInfo[i].setMonth(cursor.getString(4));
     * retresumeBaseInfo[i].setYear(cursor.getString(6));
     * retresumeBaseInfo[i].setHeight(cursor.getString(24));
     * retresumeBaseInfo[i].setNationality(cursor.getString(25));
     * retresumeBaseInfo[i].setHukou(cursor.getString(9));
     * retresumeBaseInfo[i].setIdnumber(cursor.getString(10));
     * retresumeBaseInfo[i].setCardtype(cursor.getString(11));
     * retresumeBaseInfo[i].setMarriage(cursor.getString(12));
     * retresumeBaseInfo[i].setCurrent_salary(cursor.getString(14));
     * retresumeBaseInfo[i].setWorkyear(cursor.getString(16));
     * retresumeBaseInfo[i].setWork_beginyear(cursor.getString(17));
     * retresumeBaseInfo[i].setHigh_education(cursor.getString(18));
     * retresumeBaseInfo[i].setLanguage(cursor.getString(26));
     * retresumeBaseInfo[i].setLocation(cursor.getString(20));
     * retresumeBaseInfo[i].setEmailaddress(cursor.getString(22));
     * retresumeBaseInfo[i].setAddress(cursor.getString(21));
     * retresumeBaseInfo[i].setZipcode(cursor.getString(23));
     * retresumeBaseInfo[i].setHomepage(cursor.getString(23));
     * retresumeBaseInfo[i].setPost_rank(cursor.getString(27));
     * retresumeBaseInfo[i].setFolk(cursor.getString(15));
     * retresumeBaseInfo[i].setFolk_other(cursor.getString(28));
     * retresumeBaseInfo[i].setPic_filekey(cursor.getString(29));
     * retresumeBaseInfo[i].setDomain(cursor.getString(30));
     * retresumeBaseInfo[i].setName_echo_yes(cursor.getString(31));
     * retresumeBaseInfo[i].setTelephone(cursor.getString(32));
     * retresumeBaseInfo[i].setIm_type(cursor.getString(33));
     * retresumeBaseInfo[i].setIm_account(cursor.getString(34));
     *
     * i++;
     *
     * } closeDB(); return retresumeBaseInfo; }
     */
    /**
     * 查询个人信息下的数据 name 用户名称 resume_language 简历语言
     */
    public ResumeBaseInfo query_ResumePersonInfo_Toone(String resume_language) {
        // TODO Auto-generated method stub
        openDB();
        Cursor cursor;
        String strSQLWhere = "user_id='" + MyUtils.userID
                + "' and resume_language='" + resume_language + "'";
        // String strSQLWhere=
        // "user_name='"+resumeTome.getUser_name()+"' and  resume_id="+resumeTome.getResume_id()+" and  resume_language= '"+resumeTome.getResume_language()+"'";
        cursor = db.query("ResumePersonInfo", new String[]{"user_id", "name",
                        "sex", "year", "month", "day", "height", "nationality",
                        "hukou", "idnumber", "cardtype", "marriage", "work_beginyear",
                        "location", "emailaddress", "address", "zipcode", "homepage",
                        "resume_language", "post_rank", "polity", "blood",
                        "pic_filekey", "ydphone", "telephone", "im_type", "im_account",
                        "id", "current_workstate,modify_time,isUpdate", "echo_yes","ydphone_verify_status"},

                strSQLWhere, null, null, null, null);
        ResumeBaseInfo retresumeBaseInfo = null;
        // 当我们第一调用cursor.getCount()时，当前线程会锁定数据库，在该操作完成后才解锁
        cursor.getCount();
        while (cursor.moveToNext()) {
            retresumeBaseInfo = new ResumeBaseInfo();
            retresumeBaseInfo.setUser_id(cursor.getString(0));// 获取第一列的值,第一列的索引从0开始
            retresumeBaseInfo.setName(cursor.getString(1));
            retresumeBaseInfo.setSex(cursor.getString(2));
            retresumeBaseInfo.setYear(cursor.getString(3));
            retresumeBaseInfo.setMonth(cursor.getString(4));
            retresumeBaseInfo.setDay(cursor.getString(5));
            retresumeBaseInfo.setHeight(cursor.getString(6));
            retresumeBaseInfo.setNationality(cursor.getString(7));
            retresumeBaseInfo.setHukou(cursor.getString(8));
            retresumeBaseInfo.setIdnumber(cursor.getString(9));
            retresumeBaseInfo.setCardtype(cursor.getString(10));
            retresumeBaseInfo.setMarriage(cursor.getString(11));
            retresumeBaseInfo.setWork_beginyear(cursor.getString(12));
            retresumeBaseInfo.setLocation(cursor.getString(13));
            retresumeBaseInfo.setEmailaddress(cursor.getString(14));
            retresumeBaseInfo.setAddress(cursor.getString(15));
            retresumeBaseInfo.setZipcode(cursor.getString(16));
            retresumeBaseInfo.setHomepage(cursor.getString(17));
            retresumeBaseInfo.setResume_language(cursor.getString(18));
            retresumeBaseInfo.setPost_rank(cursor.getString(19));
            retresumeBaseInfo.setPolity(cursor.getString(20));
            retresumeBaseInfo.setBlood(cursor.getString(21));
            retresumeBaseInfo.setPic_filekey(cursor.getString(22));
            retresumeBaseInfo.setYdphone(cursor.getString(23));
            retresumeBaseInfo.setTelephone(cursor.getString(24));
            retresumeBaseInfo.setIm_type(cursor.getString(25));
            retresumeBaseInfo.setIm_account(cursor.getString(26));
            retresumeBaseInfo.setId(cursor.getInt(27));
            retresumeBaseInfo.setCurrent_workstate(cursor.getString(28));
            retresumeBaseInfo.setModify_time(cursor.getString(29));
            retresumeBaseInfo.setIsUpdate(cursor.getInt(30));
            retresumeBaseInfo.setEcho_yes(cursor.getString(31));
            retresumeBaseInfo.setYdphone_verify_status(cursor.getString(32));
        }
        closeDB();
        return retresumeBaseInfo;
    }

    // -------------->>>>>>>>求职意向表<<<<<<<<<<--------------------

    // 插入求职意向
    public int Insert_ResumeCareerObjective(ResumeOrder resumeOrder) {
        // TODO Auto-generated method stub
        openDB();
        ContentValues values = new ContentValues();
        values.put("user_id", resumeOrder.getUser_id());
        values.put("jobtype", resumeOrder.getJobtype());
        values.put("industry", resumeOrder.getIndustry());
        values.put("func", resumeOrder.getFunc());
        values.put("workarea", resumeOrder.getWorkarea());
        values.put("order_salary", resumeOrder.getOrder_salary());
        values.put("order_salary_noshow", resumeOrder.getOrder_salary_noshow());
        values.put("resume_id", resumeOrder.getResume_id());
        values.put("resume_language", resumeOrder.getResume_language());
        values.put("jobname", resumeOrder.getJobname());
        values.put("lingyu", resumeOrder.getLingyu());

        long result = db.insert("ResumeCareerObjective", null, values);
        int strid = -1; // 如果strid为-1，数据插入失败
        if (result > 0) {
            Cursor cursor = db.rawQuery(
                    "select last_insert_rowid() from ResumeCareerObjective",
                    null);
            if (cursor.moveToFirst())
                strid = cursor.getInt(0);
            Log.d("SQLITE", "求职意向插入成功");
            cursor.close();
        } else {
            Log.d("SQLITE", "求职意向插入失败");
        }
        closeDB();
        return strid;

    }

    // 修改求职意向的数据
    public boolean update_ResumeCareerObjective(ResumeOrder resumeOrder) {
        // TODO Auto-generated method stub
        openDB();

        ContentValues values = new ContentValues();
        String strSQLWhere = "id=" + resumeOrder.getId();
        // String strSQLWhere=
        // "user_name='"+WorkExperience.getUser_name()+"' and  resume_id="+WorkExperience.getResume_id()+" and  resume_language= '"+WorkExperience.getResume_language();
        values.put("user_id", resumeOrder.getUser_id());
        values.put("jobtype", resumeOrder.getJobtype());
        values.put("industry", resumeOrder.getIndustry());
        values.put("func", resumeOrder.getFunc());
        values.put("workarea", resumeOrder.getWorkarea());
        values.put("order_salary", resumeOrder.getOrder_salary());
        values.put("order_salary_noshow", resumeOrder.getOrder_salary_noshow());
        values.put("resume_id", resumeOrder.getResume_id());
        values.put("resume_language", resumeOrder.getResume_language());
        values.put("jobname", resumeOrder.getJobname());
        values.put("lingyu", resumeOrder.getLingyu());
        // values.put("user_name", user_name);
        int iNumber = db.update("ResumeCareerObjective", values, strSQLWhere,
                null);
        closeDB();
        if (iNumber == 0) {
            Log.d("iNumber", "修改求职意向失败");
            return false;
        } else {
            Log.d("iNumber", "修改求职意向成功");
            return true;
        }
    }

    /**
     * // 查询求职意向下的数据 public ResumeOrder[] query_ResumeCareerObjective(String
     * user_name, String resume_id, String resume_language) { // TODO
     * Auto-generated method stub openDB(); Cursor cursor; String strSQLWhere =
     * "user_name='" + user_name + "' and  resume_id=" + resume_id +
     * " and  resume_language= '" + resume_language + "'"; // String strSQLWhere
     * = "id = '"+id + "' and  resume_language= '" + // resume_language+ "'"; //
     * "user_name='"
     * +resumeTome.getUser_name()+"' and  resume_id="+resumeTome.getResume_id
     * ()+" and  resume_language= '"+resumeTome.getResume_language()+"'"; cursor
     * = db.query("ResumeCareerObjective", new String[] { "user_id", "jobtype",
     * "current_salary_noshow", "order_salary", "order_salary_noshow",
     * "resume_id", "resume_language", "jobname", "workarea", "function",
     * "lingyu", "last_position", "current_workstate", "id" }, strSQLWhere,
     * null, null, null, null); ResumeOrder retresumeOrder[] = new
     * ResumeOrder[cursor.getCount()]; int i = 0; while (cursor.moveToNext()) {
     * retresumeOrder[i] = new ResumeOrder();
     * retresumeOrder[i].setUser_id(cursor.getString(0));// 获取第一列的值,第一列的索引从0开始
     * retresumeOrder[i].setJobtype(cursor.getString(1));
     * retresumeOrder[i].setCurrent_salary_noshow(cursor.getString(2));
     * retresumeOrder[i].setOrder_salary(cursor.getString(3));
     * retresumeOrder[i].setOrder_salary_noshow(cursor.getString(4));
     * retresumeOrder[i].setResume_id(cursor.getString(5));
     * retresumeOrder[i].setResume_language(cursor.getString(6));
     * retresumeOrder[i].setJobname(cursor.getString(7));
     * retresumeOrder[i].setWorkarea(cursor.getString(8));
     * retresumeOrder[i].setFunction(cursor.getString(9));
     * retresumeOrder[i].setLingyu(cursor.getString(10));
     * retresumeOrder[i].setWorkarea(cursor.getString(11));
     * retresumeOrder[i].setLast_position(cursor.getString(12));
     * retresumeOrder[i].setCurrent_workstate(cursor.getString(13));
     * retresumeOrder[i].setId(cursor.getInt(14)); i++; } closeDB(); return
     * retresumeOrder; }
     */
    /**
     * 查询求职意向表
     */
    public ResumeOrder query_ResumeCareerObjective_Toone(String resume_id,
                                                         String resume_language) {
        // TODO Auto-generated method stub
        openDB();
        Cursor cursor;

        String strSQLWhere = "user_id='" + MyUtils.userID + "' and  resume_id="
                + resume_id + " and  resume_language= '" + resume_language
                + "'";
        // System.out.println("查询求职意向SQL:" + strSQLWhere);
        cursor = db.query("ResumeCareerObjective", new String[]{"user_id",
                        "jobtype", "industry", "func", "workarea", "order_salary",
                        "order_salary_noshow", "resume_id", "resume_language",
                        "jobname", "lingyu", "id"}, strSQLWhere, null, null, null,
                null);
        ResumeOrder retresumeOrder = null;
        int i = 0;
        // System.out.println("~~~~~~~~:" + cursor.getCount());
        while (cursor.moveToNext()) {
            retresumeOrder = new ResumeOrder();
            retresumeOrder.setUser_id(cursor.getString(0));
            retresumeOrder.setJobtype(cursor.getString(1));
            retresumeOrder.setIndustry(cursor.getString(2));
            retresumeOrder.setFunc(cursor.getString(3));
            retresumeOrder.setWorkarea(cursor.getString(4));
            retresumeOrder.setOrder_salary(cursor.getString(5));
            retresumeOrder.setOrder_salary_noshow(cursor.getString(6));
            retresumeOrder.setResume_id(cursor.getString(7));
            retresumeOrder.setResume_language(cursor.getString(8));
            retresumeOrder.setJobname(cursor.getString(9));
            retresumeOrder.setLingyu(cursor.getString(10));
            retresumeOrder.setId(cursor.getInt(11));
            i++;
        }
        cursor.close();
        closeDB();
        return retresumeOrder;
    }

    // ------------------->>>>>>>>>>教育背景表<<<<<<<<---------------------------------
    // 插入教育背景表
    public int Insert_ResumeEducation(ArrayList<ResumeEducation> resumeEducation) {
        // TODO Auto-generated method stub

        int sumSuccess = 0;

        openDB();
        for (int i = 0; i < resumeEducation.size(); i++) {

            ContentValues values = new ContentValues();
            values.put("user_id", resumeEducation.get(i).getUser_id());
            values.put("frommonth", resumeEducation.get(i).getFrommonth());
            values.put("fromyear", resumeEducation.get(i).getFromyear());
            values.put("edudetail", resumeEducation.get(i).getEdudetail());
            values.put("tomonth", resumeEducation.get(i).getTomonth());
            values.put("toyear", resumeEducation.get(i).getToyear());
            values.put("schoolname", resumeEducation.get(i).getSchoolname());
            values.put("moremajor", resumeEducation.get(i).getMoremajor());
            values.put("degree", resumeEducation.get(i).getDegree());
            values.put("edudetail", resumeEducation.get(i).getEdudetail());
            values.put("is_overseas", resumeEducation.get(i).getIs_overseas());
            values.put("country", resumeEducation.get(i).getCountry());
            System.out
                    .println(resumeEducation.get(i).getCountry() + "插入国家地区Id");
            values.put("resume_id", resumeEducation.get(i).getResume_id());
            values.put("resume_language", resumeEducation.get(i)
                    .getResume_language());
            values.put("education_id", resumeEducation.get(i).getEducation_id());
            // values.put("user_name", user_name);
            long result = db.insert("ResumeEducation", null, values);

            if (result > 0) {
                sumSuccess++;
            }

        }
        closeDB();
        return sumSuccess;
    }

    // 修改教育背景的数据
    public boolean update_ResumeEducation(ResumeEducation resumeEducation) {
        // TODO Auto-generated method stub
        openDB();
        ContentValues values = new ContentValues();
        String strSQLWhere = "id=" + resumeEducation.getId();
        // String strSQLWhere=
        // "user_name='"+WorkExperience.getUser_name()+"' and  resume_id="+WorkExperience.getResume_id()+" and  resume_language= '"+WorkExperience.getResume_language();
        values.put("user_id", resumeEducation.getUser_id());
        values.put("frommonth", resumeEducation.getFrommonth());
        values.put("fromyear", resumeEducation.getFromyear());
        values.put("edudetail", resumeEducation.getEdudetail());
        values.put("tomonth", resumeEducation.getTomonth());
        values.put("toyear", resumeEducation.getToyear());
        values.put("schoolname", resumeEducation.getSchoolname());
        values.put("moremajor", resumeEducation.getMoremajor());
        values.put("degree", resumeEducation.getDegree());
        values.put("edudetail", resumeEducation.getEdudetail());
        values.put("is_overseas", resumeEducation.getIs_overseas());
        values.put("country", resumeEducation.getCountry());
        // System.out.println(resumeEducation.getCountry() + "修改国家地区Id");
        values.put("resume_id", resumeEducation.getResume_id());
        values.put("resume_language", resumeEducation.getResume_language());
        values.put("education_id", resumeEducation.getEducation_id());

        int iNumber = db.update("ResumeEducation", values, strSQLWhere, null);
        closeDB();
        if (iNumber == 0) {
            Log.d("iNumber", "修改教育背景失败");
            return false;
        } else {
            Log.d("iNumber", "修改教育背景成功");
            return true;
        }
    }

    // 查询教育背景下的数据
    public ResumeEducation[] query_ResumeEducation(String resume_id,
                                                   String resume_language) {
        // TODO Auto-generated method stub
        openDB();
        Cursor cursor;
        String strSQLWhere = "user_id='" + MyUtils.userID
                + "' and  resume_id= '" + resume_id
                + "' and  resume_language= '" + resume_language + "'";
        // String strSQLWhere=
        // "user_name='"+resumeTome.getUser_name()+"' and  resume_id="+resumeTome.getResume_id()+" and  resume_language= '"+resumeTome.getResume_language()+"'";
        cursor = db.query("ResumeEducation", new String[]{"user_id",
                        "frommonth", "fromyear", "edudetail", "tomonth", "toyear",
                        "schoolname", "moremajor", "degree", "edudetail",
                        "is_overseas", "country", "resume_id", "resume_language",
                        "education_id", "user_name", "id"}, strSQLWhere, null, null,
                null, null);
        ResumeEducation retresumeEducation[] = new ResumeEducation[cursor.getCount()];

        int i = 0;
        while (cursor.moveToNext()) {
            retresumeEducation[i] = new ResumeEducation();
            retresumeEducation[i].setUser_id(cursor.getString(0));// 获取第一列的值,第一列的索引从0开始
            retresumeEducation[i].setFrommonth(cursor.getString(1));
            retresumeEducation[i].setFromyear(cursor.getString(2));
            retresumeEducation[i].setEdudetail(cursor.getString(3));
            retresumeEducation[i].setTomonth(cursor.getString(4));
            retresumeEducation[i].setToyear(cursor.getString(5));
            retresumeEducation[i].setSchoolname(cursor.getString(6));
            retresumeEducation[i].setMoremajor(cursor.getString(7));
            retresumeEducation[i].setDegree(cursor.getString(8));
            retresumeEducation[i].setEdudetail(cursor.getString(9));
            retresumeEducation[i].setIs_overseas(cursor.getString(10));
            retresumeEducation[i].setCountry(cursor.getString(11));
            // System.out.println(cursor.getString(11) + "查询国家地区Id");
            retresumeEducation[i].setResume_id(cursor.getString(12));
            retresumeEducation[i].setResume_language(cursor.getString(13));
            retresumeEducation[i].setEducation_id(cursor.getString(14));
            retresumeEducation[i].setUser_id(cursor.getString(15));
            retresumeEducation[i].setId(cursor.getInt(16));
            i++;
        }
        cursor.close();
        closeDB();
        return retresumeEducation;
    }

    // ---------------------->>>>>>工作经验表<<<<<<<<<-------------------

    /**
     * 插入工作经验表
     */
    public int Insert_ResumeWorkExperience(
            ArrayList<ResumeExperience> WorkExperience) {
        // TODO Auto-generated method stub
        openDB();
        int sumSuccess = 0;
        // WorkExperience = new ArrayList<ResumeExperience>();
        ContentValues values = new ContentValues();
        for (int i = 0; i < WorkExperience.size(); i++) {

            values.put("user_id", WorkExperience.get(i).getUser_id());
            values.put("fromYear", WorkExperience.get(i).getFromyear());
            values.put("fromMonth", WorkExperience.get(i).getFrommonth());
            values.put("toYear", WorkExperience.get(i).getToyear());
            values.put("toMonth", WorkExperience.get(i).getTomonth());
            values.put("company", WorkExperience.get(i).getCompany());
            values.put("companyhide", WorkExperience.get(i).getCompanyhide());
            values.put("industry", WorkExperience.get(i).getIndustry());
            values.put("companytype", WorkExperience.get(i).getCompanytype());
            values.put("stuffmunber", WorkExperience.get(i).getStuffmunber());
            values.put("division", WorkExperience.get(i).getDivision());
            values.put("companyaddress", WorkExperience.get(i)
                    .getCompanyaddress());
            values.put("position", WorkExperience.get(i).getPosition());
            values.put("responsibility", WorkExperience.get(i)
                    .getResponsibility());
            values.put("offreason", WorkExperience.get(i).getOffreason());
            values.put("zhixi", WorkExperience.get(i).getZhixi());
            values.put("zhicheng", WorkExperience.get(i).getZhicheng());
            values.put("is_overseas", WorkExperience.get(i).getIs_overseas());
            values.put("country", WorkExperience.get(i).getCountry());
            values.put("resume_id", WorkExperience.get(i).getResume_id());
            values.put("resume_language", WorkExperience.get(i)
                    .getResume_language());
            values.put("lingyu", WorkExperience.get(i).getLingyu());
            values.put("func", WorkExperience.get(i).getFunc());
            values.put("salary", WorkExperience.get(i).getSalary());
            values.put("salary_hide", WorkExperience.get(i).getSalary_hide());
            values.put("experience_id", WorkExperience.get(i)
                    .getExperience_id());
            // values.put("user_name", user_name);
            long result = db.insert("ResumeWorkExperience", null, values);
            int strid = -1; // 如果strid为-1，数据插入失败
            if (result > 0) {
                sumSuccess++;
            }
        }

        closeDB();
        return sumSuccess;
    }

    // 修改工作经验下的数据
    public boolean update_ResumeWorkExperience(ResumeExperience WorkExperience) {
        // TODO Auto-generated method stub
        openDB();
        ContentValues values = new ContentValues();
        String strSQLWhere = "id=" + WorkExperience.getId();
        // String strSQLWhere=
        // "user_name='"+WorkExperience.getUser_name()+"' and  resume_id="+WorkExperience.getResume_id()+" and  resume_language= '"+WorkExperience.getResume_language();
        values.put("user_id", WorkExperience.getUser_id());
        values.put("fromYear", WorkExperience.getFromyear());
        values.put("fromMonth", WorkExperience.getFrommonth());
        values.put("toYear", WorkExperience.getToyear());
        values.put("toMonth", WorkExperience.getTomonth());
        values.put("company", WorkExperience.getCompany());
        values.put("companyhide", WorkExperience.getCompanyhide());
        values.put("industry", WorkExperience.getIndustry());
        values.put("companytype", WorkExperience.getCompanytype());
        values.put("stuffmunber", WorkExperience.getStuffmunber());
        values.put("division", WorkExperience.getDivision());
        values.put("companyaddress", WorkExperience.getCompanyaddress());
        values.put("position", WorkExperience.getPosition());
        values.put("responsibility", WorkExperience.getResponsibility());
        values.put("offreason", WorkExperience.getOffreason());
        values.put("zhixi", WorkExperience.getZhixi());
        values.put("zhicheng", WorkExperience.getZhicheng());
        values.put("is_overseas", WorkExperience.getIs_overseas());
        values.put("country", WorkExperience.getCountry());
        values.put("resume_id", WorkExperience.getResume_id());
        values.put("resume_language", WorkExperience.getResume_language());
        values.put("lingyu", WorkExperience.getLingyu());
        values.put("func", WorkExperience.getFunc());
        values.put("salary", WorkExperience.getSalary());
        values.put("salary_hide", WorkExperience.getSalary_hide());
        values.put("experience_id", WorkExperience.getExperience_id());
        int iNumber = db.update("ResumeWorkExperience", values, strSQLWhere,
                null);
        closeDB();
        if (iNumber == 0) {
            Log.d("iNumber", "修改工作经验失败");
            return false;
        } else {
            Log.d("iNumber", "修改工作经验成功");
            return true;
        }
    }

    // 查询工作经验下的数据
    public ResumeExperience[] query_ResumeWorkExperience(String resume_id,
                                                         String resume_language) {
        // TODO Auto-generated method stub
        openDB();
        Cursor cursor;
        String strSQLWhere = "user_id='" + MyUtils.userID
                + "' and  resume_id= '" + resume_id
                + "' and  resume_language= '" + resume_language + "'";
        // System.out.println("wwwww" + strSQLWhere);

        cursor = db.query("ResumeWorkExperience", new String[]{"user_id",
                        "fromYear", "fromMonth", "toYear", "toMonth", "company",
                        "companyhide", "industry", "companytype", "stuffmunber",
                        "division", "companyaddress", "position", "responsibility",
                        "offreason", "zhixi", "is_overseas", "country", "resume_id",
                        "resume_language", "lingyu", "func", "salary", "salary_hide",
                        "experience_id", "zhicheng", "id"}, strSQLWhere, null, null,
                null, null);
        ResumeExperience retresumeExperience[] = new ResumeExperience[cursor
                .getCount()];
        int i = 0;
        while (cursor.moveToNext()) {

            retresumeExperience[i] = new ResumeExperience();
            retresumeExperience[i].setUser_id(cursor.getString(0)); // 获取第一列的值,第一列的索引从0开始
            retresumeExperience[i].setFromyear(cursor.getString(1));
            retresumeExperience[i].setFrommonth(cursor.getString(2));
            retresumeExperience[i].setToyear(cursor.getString(3));
            retresumeExperience[i].setTomonth(cursor.getString(4));
            retresumeExperience[i].setCompany(cursor.getString(5));
            retresumeExperience[i].setCompanyhide(cursor.getString(6));
            retresumeExperience[i].setIndustry(cursor.getString(7));
            retresumeExperience[i].setCompanytype(cursor.getString(8));
            retresumeExperience[i].setStuffmunber(cursor.getString(9));
            retresumeExperience[i].setDivision(cursor.getString(10));
            retresumeExperience[i].setCompanyaddress(cursor.getString(11));
            retresumeExperience[i].setPosition(cursor.getString(12));
            retresumeExperience[i].setResponsibility(cursor.getString(13));
            retresumeExperience[i].setOffreason(cursor.getString(14));
            retresumeExperience[i].setZhixi(cursor.getString(15));
            retresumeExperience[i].setIs_overseas(cursor.getString(16));
            retresumeExperience[i].setCountry(cursor.getString(17));
            retresumeExperience[i].setResume_id(cursor.getString(18));
            retresumeExperience[i].setResume_language(cursor.getString(19));
            retresumeExperience[i].setLingyu(cursor.getString(20));
            retresumeExperience[i].setFunc(cursor.getString(21));
            retresumeExperience[i].setSalary(cursor.getString(22));
            retresumeExperience[i].setSalary_hide(cursor.getString(23));
            retresumeExperience[i].setExperience_id(cursor.getString(24));
            retresumeExperience[i].setZhicheng(cursor.getString(25));
            retresumeExperience[i].setId(cursor.getInt(26));
            i++;
        }
        cursor.close();
        closeDB();
        return retresumeExperience;
    }

    // ---------------------->>>>>>自我评价表<<<<<<<<<-------------------

    /**
     * 自我评价表
     */
    // 插入自我评价列表
    public int Insert_ResumeTome(ArrayList<ResumeAssessInfo> resumeTome) {
        // TODO Auto-generated method stub
        openDB();
        int sumSuccess = 0;
        // resumeTome = new ArrayList<ResumeAssessInfo>();
        for (int i = 0; i < resumeTome.size(); i++) {

            ContentValues values = new ContentValues();
            values.put("user_id", resumeTome.get(i).getUser_id());
            values.put("introduction", resumeTome.get(i).getIntroduction());
            values.put("resume_id", resumeTome.get(i).getResume_id());
            values.put("resume_language", resumeTome.get(i)
                    .getResume_language());
            // values.put("user_name", user_name);
            long result = db.insert("ResumeSelfIntroduction", null, values);

            if (result > 0) {
                sumSuccess++;
            }
        }
        closeDB();
        return sumSuccess;
    }

    // 插入自我评价列表
    public int Insert_ResumeTome(ResumeAssessInfo resumeTome) {
        // TODO Auto-generated method stub
        openDB();
        ContentValues values = new ContentValues();
        values.put("user_id", resumeTome.getUser_id());
        values.put("introduction", resumeTome.getIntroduction());
        values.put("resume_id", resumeTome.getResume_id());
        values.put("resume_language", resumeTome.getResume_language());

        long result = db.insert("ResumeSelfIntroduction", null, values);
        int strid = -1; // 如果strid为-1，数据插入失败
        if (result > 0) {
            Cursor cursor = db.rawQuery(
                    "select last_insert_rowid() from ResumeSelfIntroduction",
                    null);
            if (cursor.moveToFirst())
                strid = cursor.getInt(0);
            Log.d("SQLITE", "自我评价插入成功");
            cursor.close();
        } else {
            Log.d("SQLITE", "自我评价插入失败");
        }

        closeDB();
        return strid;
    }

    // 修改自我评价下的数据
    public boolean update_ResumeTome(ResumeAssessInfo resumeTome) {
        // TODO Auto-generated method stub
        openDB();
        ContentValues values = new ContentValues();
        String strSQLWhere = "id=" + resumeTome.getId();
        // String strSQLWhere=
        // "user_name='"+resumeTome.getUser_name()+"' and  resume_id="+resumeTome.getResume_id()+" and  resume_language= '"+resumeTome.getResume_language();
        values.put("user_id", resumeTome.getUser_id());
        values.put("introduction", resumeTome.getIntroduction());
        values.put("resume_id", resumeTome.getResume_id());
        values.put("resume_language", resumeTome.getResume_language());

        int iNumber = db.update("ResumeSelfIntroduction", values, strSQLWhere,
                null);
        closeDB();
        if (iNumber == 0) {
            Log.d("iNumber", "修改自我评价失败");
            return false;

        } else {
            Log.d("iNumber", "修改自我评价成功");

            return true;
        }
    }

    /**
     * // 查询自我评价下的数据 public ResumeAssessInfo[] query_ResumeTome(String
     * user_name, String resume_id, String resume_language) { // TODO
     * Auto-generated method stub openDB(); Cursor cursor; String strSQLWhere =
     * "user_name='" + user_name + "' and  resume_id=" + resume_id +
     * " and  resume_language= '" + resume_language + "'"; // String
     * strSQLWhere= //
     * "user_name='"+resumeTome.getUser_name()+"' and  resume_id="
     * +resumeTome.getResume_id
     * ()+" and  resume_language= '"+resumeTome.getResume_language()+"'"; cursor
     * = db.query("ResumeSelfIntroduction", new String[] { "user_id",
     * "introduction", "resume_id", "resume_language", "id" }, strSQLWhere,
     * null, null, null, null); ResumeAssessInfo retResumeAssessInfo[] = new
     * ResumeAssessInfo[cursor .getCount()];
     * <p/>
     * int i = 0; while (cursor.moveToNext()) {
     * <p/>
     * retResumeAssessInfo[i] = new ResumeAssessInfo();
     * retResumeAssessInfo[i].setUser_id(cursor.getString(0));//
     * 获取第一列的值,第一列的索引从0开始
     * retResumeAssessInfo[i].setIntroduction(cursor.getString(1));
     * retResumeAssessInfo[i].setResume_id(cursor.getString(2));
     * retResumeAssessInfo[i].setResume_language(cursor.getString(3));
     * retResumeAssessInfo[i].setId(cursor.getInt(4));
     * <p/>
     * i++; } closeDB(); return retResumeAssessInfo; }
     */
    // 查询自我评价下的数据
    public ResumeAssessInfo query_ResumeTome_Toone(String resume_id,
                                                   String resume_language) {
        // TODO Auto-generated method stub
        openDB();
        Cursor cursor;
        String strSQLWhere = "user_id='" + MyUtils.userID + "' and  resume_id="
                + resume_id + " and  resume_language= '" + resume_language
                + "'";
        // String strSQLWhere=
        // "user_name='"+resumeTome.getUser_name()+"' and  resume_id="+resumeTome.getResume_id()+" and  resume_language= '"+resumeTome.getResume_language()+"'";
        cursor = db.query("ResumeSelfIntroduction", new String[]{"user_id",
                        "introduction", "resume_id", "resume_language", "id"},
                strSQLWhere, null, null, null, null);
        ResumeAssessInfo retResumeAssessInfo = null;

        int i = 0;
        while (cursor.moveToNext()) {

            retResumeAssessInfo = new ResumeAssessInfo();
            retResumeAssessInfo.setUser_id(cursor.getString(0));// 获取第一列的值,第一列的索引从0开始
            retResumeAssessInfo.setIntroduction(cursor.getString(1));
            retResumeAssessInfo.setResume_id(cursor.getString(2));
            retResumeAssessInfo.setResume_language(cursor.getString(3));
            retResumeAssessInfo.setId(cursor.getInt(4));

            i++;
        }
        cursor.close();
        closeDB();
        return retResumeAssessInfo;
    }

    // ----------------->>>>>>>>证书<<<<<<<<<---------------------
    // 插入证书列表
    /*
     * public int Insert_ResumeCertificate( ArrayList<ResumeCertificate>
	 * resumeCertificate, String user_name) { // TODO Auto-generated method stub
	 * 
	 * int sumSuccess = 0; openDB(); resumeCertificate = new
	 * ArrayList<ResumeCertificate>(); for (int i = 0; i <
	 * resumeCertificate.size(); i++) { ContentValues values = new
	 * ContentValues(); values.put("certificate_id", resumeCertificate.get(i)
	 * .getCertificate_id()); values.put("user_id",
	 * resumeCertificate.get(i).getUser_id()); values.put("getyear",
	 * resumeCertificate.get(i).getGetyear()); values.put("getmonth",
	 * resumeCertificate.get(i).getGetmonth()); values.put("certname",
	 * resumeCertificate.get(i).getCertname()); values.put("scores",
	 * resumeCertificate.get(i).getScores()); values.put("echoYes",
	 * resumeCertificate.get(i).getEcho_yes()); values.put("cert_filekey",
	 * resumeCertificate.get(i) .getCert_filekey()); values.put("resume_id",
	 * resumeCertificate.get(i).getResume_id()); values.put("resume_language",
	 * resumeCertificate.get(i) .getResume_language()); values.put("user_name",
	 * user_name); long result = db.insert("ResumeCertificate", null, values);
	 * 
	 * if (result != 0) { sumSuccess++; } }
	 * 
	 * closeDB(); return sumSuccess;
	 * 
	 * }
	 */

    // 修改证书的数据
    /*
     * public boolean update_ResumeCertificate(ResumeCertificate
	 * resumeCertificate) { // TODO Auto-generated method stub openDB();
	 * ContentValues values = new ContentValues(); String strSQLWhere = "id=" +
	 * resumeCertificate.getId(); // String strSQLWhere= //
	 * "user_name='"+resumeitem
	 * .getUser_name()+"' and  resume_id="+resumeitem.getResume_id
	 * ()+" and  resume_language= '"
	 * +resumeitem.getResume_language()+"' and trainingId="
	 * +resumeitem.getProjectId(); values.put("certificate_id",
	 * resumeCertificate.getCertificate_id()); values.put("user_id",
	 * resumeCertificate.getUser_id()); values.put("getyear",
	 * resumeCertificate.getGetyear()); values.put("getmonth",
	 * resumeCertificate.getGetmonth()); values.put("certname",
	 * resumeCertificate.getCertname()); values.put("scores",
	 * resumeCertificate.getScores()); values.put("echoYes",
	 * resumeCertificate.getEcho_yes()); values.put("cert_filekey",
	 * resumeCertificate.getCert_filekey()); values.put("resume_id",
	 * resumeCertificate.getResume_id()); values.put("resume_language",
	 * resumeCertificate.getResume_language());
	 * 
	 * int iNumber = db.update("ResumeCertificate", values, strSQLWhere, null);
	 * if (iNumber == 0) { Log.d("iNumber", "修改项目经验成功"); closeDB(); return
	 * false; } else { Log.d("iNumber", "修改项目经验失败"); closeDB(); return true; }
	 * 
	 * }
	 */

    // 查询证书下的数据
    /*
     * public ResumeCertificate[] query_ResumeCertificate(String user_name,
	 * String resume_id, String resume_language) { // TODO Auto-generated method
	 * stub openDB(); Cursor cursor; String strSQLWhere = "user_name='" +
	 * user_name + "' and  resume_id=" + resume_id + " and  resume_language= '"
	 * + resume_language + "'"; // String strSQLWhere= //
	 * "user_name='"+resumeitem
	 * .getUser_name()+"' and  resume_id="+resumeitem.getResume_id
	 * ()+" and  resume_language= '"+resumeitem.getResume_language()+"'"; cursor
	 * = db.query("ResumeCertificate", new String[] { "certificate_id",
	 * "user_id", "getyear", "getmonth", "certname", "scores", "echoYes",
	 * "cert_filekey", "resume_id", "resume_language" }, strSQLWhere, null,
	 * null, null, "certificate_id asc"); ResumeCertificate
	 * retResumeCertificate[] = new ResumeCertificate[cursor .getCount()];
	 * 
	 * int i = 0; while (cursor.moveToNext()) { retResumeCertificate[i] = new
	 * ResumeCertificate();
	 * 
	 * retResumeCertificate[i].setCertificate_id(cursor.getString(0));//
	 * 获取第一列的值,第一列的索引从0开始
	 * retResumeCertificate[i].setUser_id(cursor.getString(1));
	 * retResumeCertificate[i].setGetyear(cursor.getString(2));
	 * retResumeCertificate[i].setGetmonth(cursor.getString(3));
	 * retResumeCertificate[i].setCertname(cursor.getString(4));
	 * retResumeCertificate[i].setScores(cursor.getString(5));
	 * retResumeCertificate[i].setEcho_yes(cursor.getString(6));
	 * retResumeCertificate[i].setCert_filekey(cursor.getString(7));
	 * retResumeCertificate[i].setResume_id(cursor.getString(8));
	 * retResumeCertificate[i].setResume_language(cursor.getString(9)); i++; }
	 * cursor.close(); closeDB(); return retResumeCertificate; }
	 */

    // ------------------->>>>>>>项目经验<<<<<<---------------------

    /**
     * 项目经验表
     */
    // 插入项目经验列表
    public int Insert_Resumeitem(ArrayList<ResumeProject> resumeitem) {
        // TODO Auto-generated method stub
        int sumSuccess = 0;
        openDB();
        // resumeitem = new ArrayList<ResumeProject>();
        for (int i = 0; i < resumeitem.size(); i++) {

            ContentValues values = new ContentValues();
            values.put("user_id", MyUtils.userID);
            values.put("fromyear", resumeitem.get(i).getFromyear());
            values.put("frommonth", resumeitem.get(i).getFrommonth());
            values.put("toyear", resumeitem.get(i).getToyear());
            values.put("tomonth", resumeitem.get(i).getTomonth());
            values.put("projectname", resumeitem.get(i).getProjectname());
            values.put("position", resumeitem.get(i).getPosition());
            values.put("projectdesc", resumeitem.get(i).getProjectdesc());
            values.put("responsibility", resumeitem.get(i).getResponsibility());
            values.put("resume_id", resumeitem.get(i).getResume_id());
            values.put("resume_language", resumeitem.get(i)
                    .getResume_language());
            values.put("project_id", resumeitem.get(i).getProject_id());
            // values.put("user_name", user_name);
            long result = db.insert("ResumeProject", null, values);

            if (result != 0) {
                sumSuccess++;

            }

        }

        closeDB();
        return sumSuccess;

    }

    // 修改项目经验下的数据
    public boolean update_Resumeitem(ResumeProject resumeitem) {
        // TODO Auto-generated method stub
        openDB();
        ContentValues values = new ContentValues();
        String strSQLWhere = "id=" + resumeitem.getId();

        values.put("fromyear", resumeitem.getFromyear());
        values.put("frommonth", resumeitem.getFrommonth());
        values.put("toyear", resumeitem.getToyear());
        values.put("tomonth", resumeitem.getTomonth());
        values.put("projectname", resumeitem.getProjectname());
        values.put("position", resumeitem.getPosition());
        values.put("projectdesc", resumeitem.getProjectdesc());
        values.put("responsibility", resumeitem.getResponsibility());
        values.put("resume_id", resumeitem.getResume_id());
        values.put("resume_language", resumeitem.getResume_language());
        values.put("project_id", resumeitem.getProject_id());
        int iNumber = db.update("ResumeProject", values, strSQLWhere, null);
        if (iNumber == 0) {
            closeDB();
            Log.d("iNumber", "修改项目经验失败");
            return false;
        } else {
            closeDB();
            Log.d("iNumber", "修改项目经验成功");
            return true;
        }
    }

    // //删除项目经验的数据
    // public boolean Delete_Resumeitem(Resumeitem resumeitem) {
    // // TODO Auto-generated method stub
    // String strSQLWhere=
    // "user_name='"+resumeitem.getUser_name()+"' and  resume_id="+resumeitem.getResume_id()+" and  resume_language= '"+resumeitem.getResume_language()+"' and trainingId="+resumeitem.getProjectId();
    // int iNumber=db.delete("Resumeitem",strSQLWhere, null);
    // if(iNumber==0){
    // Log.d("iNumber","修改培训经历失败" );
    // return false;
    // }
    // else{
    // Log.d("iNumber","修改培训经历成功" );
    // return true;
    // }
    // }

    // 查询项目经验下的数据
    public ResumeProject[] query_Resumeitem(String resume_id,
                                            String resume_language) {
        // TODO Auto-generated method stub
        openDB();
        Cursor cursor;
        String strSQLWhere = "user_id='" + MyUtils.userID + "' and  resume_id="
                + resume_id + " and  resume_language= '" + resume_language
                + "'";
        cursor = db.query("ResumeProject", new String[]{"fromyear",
                        "frommonth", "toyear", "tomonth", "projectname", "position",
                        "projectdesc", "responsibility", "resume_id",
                        "resume_language", "project_id", "id"}, strSQLWhere, null,
                null, null, null);
        ResumeProject retResumeitem[] = new ResumeProject[cursor.getCount()];

        int i = 0;
        while (cursor.moveToNext()) {
            retResumeitem[i] = new ResumeProject();
            retResumeitem[i].setFromyear(cursor.getString(0));
            retResumeitem[i].setFrommonth(cursor.getString(1));
            retResumeitem[i].setToyear(cursor.getString(2));
            retResumeitem[i].setTomonth(cursor.getString(3));
            retResumeitem[i].setProjectname(cursor.getString(4));
            retResumeitem[i].setPosition(cursor.getString(5));
            retResumeitem[i].setProjectdesc(cursor.getString(6));
            retResumeitem[i].setResponsibility(cursor.getString(7));
            retResumeitem[i].setResume_id(cursor.getString(8));
            retResumeitem[i].setResume_language(cursor.getString(9));
            retResumeitem[i].setProject_id(cursor.getString(10));
            retResumeitem[i].setId(cursor.getInt(11));
            i++;
        }
        cursor.close();
        closeDB();
        return retResumeitem;
    }

    // ----------------------->>>>>>>培训经历模块<<<<<<<<<<<----------------------------

    /**
     * 培训经历模块
     */
    // 插入培训经历列表
    public int Insert_ResumeTraining(ArrayList<ResumePlant> resumePlant) {
        // TODO Auto-generated method stub
        openDB();
        int sumSuccess = 0;
        // resumePlant = new ArrayList<ResumePlant>();

        for (int i = 0; i < resumePlant.size(); i++) {
            ContentValues values = new ContentValues();
            values.put("plant_id", resumePlant.get(i).getPlant_id());
            values.put("user_id", resumePlant.get(i).getUser_id());
            values.put("fromYear", resumePlant.get(i).getFromyear());
            values.put("fromMonth", resumePlant.get(i).getFrommonth());
            values.put("toYear", resumePlant.get(i).getToyear());
            values.put("toMonth", resumePlant.get(i).getTomonth());
            values.put("institution", resumePlant.get(i).getInstitution());
            values.put("course", resumePlant.get(i).getCourse());
            values.put("place", resumePlant.get(i).getPlace());
            values.put("certification", resumePlant.get(i).getCertification());
            values.put("trainDetail", resumePlant.get(i).getTraindetail());
            values.put("resume_id", resumePlant.get(i).getResume_id());
            values.put("resume_language", resumePlant.get(i)
                    .getResume_language());

            long result = db.insert("ResumeTraining", null, values);

            if (result != 0) {
                sumSuccess++;
            }
        }
        closeDB();
        return sumSuccess;
    }

    // 修改培训经历下的数据
    public boolean update_ResumeTraining(ResumePlant resumePlant) {
        // TODO Auto-generated method stub
        openDB();
        ContentValues values = new ContentValues();
        String strSQLWhere = "id=" + resumePlant.getId();
        // String strSQLWhere=
        // "id'"+resumePlant.getUser_name()+"' and  resume_id="+resumePlant.getResume_id()+" and  resume_language= '"+resumePlant.getResume_language()+"' and trainingId="+resumePlant.getTrainingId();
        values.put("plant_id", resumePlant.getPlant_id());
        values.put("user_id", resumePlant.getUser_id());
        values.put("fromYear", resumePlant.getFromyear());
        values.put("fromMonth", resumePlant.getFrommonth());
        values.put("toYear", resumePlant.getToyear());
        values.put("toMonth", resumePlant.getTomonth());
        values.put("institution", resumePlant.getInstitution());
        values.put("course", resumePlant.getCourse());
        values.put("place", resumePlant.getPlace());
        values.put("certification", resumePlant.getCertification());
        values.put("trainDetail", resumePlant.getTraindetail());
        values.put("resume_id", resumePlant.getResume_id());
        values.put("resume_language", resumePlant.getResume_language());
        int iNumber = db.update("ResumeTraining", values, strSQLWhere, null);
        closeDB();
        if (iNumber == 0) {
            Log.d("iNumber", "修改培训经历失败");
            return false;
        } else {
            Log.d("iNumber", "修改培训经历成功");
            return true;
        }

    }

    // 查询培训经历下的数据
    public ResumePlant[] query_ResumeTraining(String resume_id,
                                              String resume_language) {
        // TODO Auto-generated method stub
        openDB();
        Cursor cursor;

        String strSQLWhere = "user_id='" + MyUtils.userID + "' and  resume_id="
                + resume_id + " and  resume_language= '" + resume_language
                + "'";
        cursor = db.query("ResumeTraining", new String[]{"plant_id",
                        "user_id", "fromYear", "fromMonth", "toYear", "toMonth",
                        "institution", "course", "place", "certification",
                        "trainDetail", "resume_id", "resume_language", "id"},
                strSQLWhere, null, null, null, "plant_id asc");
        ResumePlant retResumePlant[] = new ResumePlant[cursor.getCount()];

        int i = 0;
        while (cursor.moveToNext()) {
            retResumePlant[i] = new ResumePlant();

            retResumePlant[i].setPlant_id(cursor.getString(0));// 获取第一列的值,第一列的索引从0开始
            retResumePlant[i].setUser_id(cursor.getString(1));
            retResumePlant[i].setFromyear(cursor.getString(2));
            retResumePlant[i].setFrommonth(cursor.getString(3));
            retResumePlant[i].setToyear(cursor.getString(4));
            retResumePlant[i].setTomonth(cursor.getString(5));
            retResumePlant[i].setInstitution(cursor.getString(6));
            retResumePlant[i].setCourse(cursor.getString(7));
            retResumePlant[i].setPlace(cursor.getString(8));
            retResumePlant[i].setCertification(cursor.getString(9));
            retResumePlant[i].setTraindetail(cursor.getString(10));
            retResumePlant[i].setResume_id(cursor.getString(11));
            retResumePlant[i].setResume_language(cursor.getString(12));
            retResumePlant[i].setId(cursor.getInt(13));

            i++;
        }
        cursor.close();
        closeDB();
        // Log.d(" retResumeskill[i]", retResumePlant[0].getUser_name());
        return retResumePlant;
    }

    // ---------------------->>>>>>>>>>个人技能模块<<<<<<<<---------------------------

    /**
     * 个人技能模块
     */
    // 插入个人技能列表
    public int Insert_ResumeSkill(ArrayList<ResumeSkill> resume_skill) {
        // TODO Auto-generated method stub
        int sumSuccess = 0;
        // resume_skill = new ArrayList<ResumeSkill>();
        openDB();

        for (int i = 0; i < resume_skill.size(); i++) {
            ContentValues values = new ContentValues();
            values.put("user_id", resume_skill.get(i).getUser_id());
            values.put("skilltitle", resume_skill.get(i).getSkilltitle());
            values.put("usetime", resume_skill.get(i).getUsetime());
            values.put("ability", resume_skill.get(i).getAbility());
            values.put("resume_id", resume_skill.get(i).getResume_id());
            values.put("resume_language", resume_skill.get(i)
                    .getResume_language());
            values.put("skill_id", resume_skill.get(i).getSkill_id());

            long result = db.insert("ResumeSkill", null, values);

            if (result != 0) {
                sumSuccess++;
            }

        }
        closeDB();
        return sumSuccess;

    }

    // 修改个人技能下的数据
    public boolean update_ResumeSkill(ResumeSkill resume_skill) {
        // TODO Auto-generated method stub
        openDB();

        int iNumber = -100;
        ContentValues values = new ContentValues();
        String strSQLWhere = "id=" + resume_skill.getId();

        values.put("user_id", resume_skill.getUser_id());
        values.put("skilltitle", resume_skill.getSkilltitle());
        values.put("usetime", resume_skill.getUsetime());
        values.put("ability", resume_skill.getAbility());
        values.put("resume_id", resume_skill.getResume_id());
        values.put("resume_language", resume_skill.getResume_language());
        values.put("skill_id", resume_skill.getSkill_id());

        iNumber = db.update("ResumeSkill", values, strSQLWhere, null);
        closeDB();
        if (iNumber == 0) {
            Log.d("iNumber", "修改个人技能失败");

            return false;
        } else {
            Log.d("iNumber", "修改个人技能成功");
            return true;
        }
    }

    // 查询个人技能下的所有数据
    public ResumeSkill[] query_ResumeSkill(String resume_id,
                                           String resume_language) {
        // TODO Auto-generated method stub
        openDB();
        Cursor cursor;

        String strSQLWhere = "user_id='" + MyUtils.userID + "' and  resume_id="
                + resume_id + " and  resume_language= '" + resume_language
                + "'";
        cursor = db.query("ResumeSkill", new String[]{"skill_id", "user_id",
                        "resume_id", "resume_language", "skilltitle", "usetime",
                        "ability", "id"}, strSQLWhere, null, null, null,
                "skill_id asc");
        ResumeSkill retResumeskill[] = new ResumeSkill[cursor.getCount()];

        int i = 0;
        while (cursor.moveToNext()) {
            retResumeskill[i] = new ResumeSkill();
            retResumeskill[i].setSkill_id(cursor.getString(0));// 获取第一列的值,第一列的索引从0开始
            retResumeskill[i].setUser_id(cursor.getString(1));
            retResumeskill[i].setResume_id(cursor.getString(2));
            retResumeskill[i].setResume_language(cursor.getString(3));
            retResumeskill[i].setSkilltitle(cursor.getString(4));
            retResumeskill[i].setUsetime(cursor.getString(5));
            retResumeskill[i].setAbility(cursor.getString(6));
            retResumeskill[i].setId(cursor.getInt(7));
            i++;
        }
        cursor.close();
        closeDB();

        return retResumeskill;
    }

    // --------------------------->>>>>>>>>>>>>语言能力<<<<<<<<<<<--------------------

    // 插入语言能力列表
    public int Insert_ResumeLanguageLevel(
            ArrayList<ResumeLanguageLevel> LanguageLevel) {
        // TODO Auto-generated method stub
        openDB();
        int sumSuccess = 0;
        // LanguageLevel = new ArrayList<ResumeLanguageLevel>();

        for (int i = 0; i < LanguageLevel.size(); i++) {
            ContentValues values = new ContentValues();
            values.put("langname", LanguageLevel.get(i).getLangname());
            values.put("user_id", LanguageLevel.get(i).getUser_id());
            values.put("read_level", LanguageLevel.get(i).getRead_level());
            values.put("speak_level", LanguageLevel.get(i).getSpeak_level());
            values.put("user_id", MyUtils.userID);
            long result = db.insert("ResumeLanguageLevel", null, values);
            if (result != 0) {
                sumSuccess++;
            }
        }
        closeDB();
        return sumSuccess;
    }

    // 修改个人技能下的数据
    public boolean update_ResumeLanguageLevel(ResumeLanguageLevel LanguageLevel) {
        // TODO Auto-generated method stub
        openDB();

        int iNumber = -100;
        ContentValues values = new ContentValues();
        String strSQLWhere = "id=" + LanguageLevel.getId();

        values.put("langname", LanguageLevel.getLangname());
        values.put("user_id", LanguageLevel.getUser_id());

        values.put("read_level", LanguageLevel.getRead_level());
        values.put("speak_level", LanguageLevel.getSpeak_level());

        iNumber = db.update("ResumeLanguageLevel", values, strSQLWhere, null);
        closeDB();
        if (iNumber == 0) {
            Log.d("iNumber", "修改个人技能失败");
            return false;
        } else {
            Log.d("iNumber", "修改个人技能成功");
            return true;
        }
    }

    // 查询语言能力下的所有数据
    public ResumeLanguageLevel[] query_ResumeLanguageLevel() {
        // TODO Auto-generated method stub
        openDB();
        Cursor cursor;
        String strSQLWhere = "user_id='" + MyUtils.userID + "'";
        cursor = db.query("ResumeLanguageLevel", new String[]{"langname",
                        "user_id", "read_level", "speak_level", "id"}, strSQLWhere,
                null, null, null, "user_id asc");
        ResumeLanguageLevel LanguageLevel[] = new ResumeLanguageLevel[cursor
                .getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            LanguageLevel[i] = new ResumeLanguageLevel();
            LanguageLevel[i].setLangname(cursor.getString(0));// 获取第一列的值,第一列的索引从0开始
            LanguageLevel[i].setUser_id(cursor.getString(1));
            LanguageLevel[i].setRead_level(cursor.getString(2));
            LanguageLevel[i].setSpeak_level(cursor.getString(3));
            LanguageLevel[i].setId(cursor.getInt(4));
            i++;
        }
        cursor.close();
        closeDB();

        return LanguageLevel;
    }

    /**
     * 删除某一类型下的所有数据(区分简历语言) Tabname 要删除数据的表名 username用户名 resume_id 简历ID
     * resume_language 简历语言
     */
    public boolean Delete_Data(String Tabname, String resume_id,
                               String resume_language) {
        // TODO Auto-generated method stub
        openDB();
        String strSQLWhere = "user_id= '" + MyUtils.userID
                + "'and resume_id= '" + resume_id + "'and resume_language= '"
                + resume_language + "'";
        int iNumber = db.delete(Tabname, strSQLWhere, null);
        closeDB();
        if (iNumber == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 删除表中关于该用户id的所有信息
     */
    public boolean deleteData(String tabName, String userId) {
        openDB();
        String strSQLWhere = "user_id= '" + userId + "'";
        int iNumber = db.delete(tabName, strSQLWhere, null);
        closeDB();
        if (iNumber == 0) {
            return false;
        } else {
            return true;
        }
    }

    // /**
    // * 删除某一类型下的Tabname 要删除数据的表名 username用户名 resume_id 简历ID resume_language
    // * 简历语言 key_name 要删除数据的ID名字(例如 education_id) id_value 要删除数据的ID的值
    // *
    // * */
    // public boolean Delete_Data(String Tabname, String user_name,
    // String resume_id, String resume_language, String key_name,
    // String id_value) {
    // // TODO Auto-generated method stub
    // openDB();
    // String strSQLWhere = "user_name= '" + user_name + "'and resume_id= '"
    // + resume_id + "'and resume_language= '" + resume_language
    // + "'and" + "key_name = '" + key_name + "' and" + "id_value = '"
    // + id_value + "'";
    // int iNumber = db.delete(Tabname, strSQLWhere, null);
    // closeDB();
    // if (iNumber == 0) {
    // return false;
    // } else {
    // return true;
    // }
    // }

    /**
     * 删除一个表下所有数据
     */
    public boolean Delete_Data(String Tabname) {
        // TODO Auto-generated method stub
        openDB();
        // String strSQLWhere = "id=" + id;
        int iNumber = db.delete(Tabname, null, null);
        closeDB();
        if (iNumber == 0) {

            return false;
        } else {

            return true;
        }

    }

    /**
     * 删除一个表下的一条数据
     */
    public boolean Delete_Data(String Tabname, int id) {
        // TODO Auto-generated method stub
        openDB();
        String strSQLWhere = "id=" + id;
        int iNumber = db.delete(Tabname, strSQLWhere, null);
        closeDB();
        if (iNumber == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 删除一份简历4v
     */
    public boolean Delete_Data(String resume_id, String resume_language) {
        // TODO Auto-generated method stub
        openDB();
        String strSQLWhere = "user_id= '" + MyUtils.userID
                + "' and resume_id='" + resume_id + "' and resume_language= '"
                + resume_language + "'";
        // System.out.println("sql:" + strSQLWhere);
        db.delete("ResumeCareerObjective", strSQLWhere, null);
        db.delete("ResumeSelfIntroduction", strSQLWhere, null);
        db.delete("ResumeWorkExperience", strSQLWhere, null);
        db.delete("ResumeEducation", strSQLWhere, null);
        db.delete("ResumeProject", strSQLWhere, null);
        db.delete("ResumeTraining", strSQLWhere, null);
        db.delete("ResumeSkill", strSQLWhere, null);
        int iNumber = db.delete("ResumeList", strSQLWhere, null);
        closeDB();
        // System.out.println(resume_language + " " + resume_id + "简历删除结果："
        // + iNumber);
        if (iNumber == 0) {
            return false;
        } else {
            return true;
        }

    }

    // 插入搜索记录

    public int Insert_SearchHistory(HistoyInfo histoyInfo) {
        // TODO Auto-generated method stub

        openDB();
        ContentValues values = new ContentValues();

        values.put("function_id", histoyInfo.getFunction_id());
        values.put("industry_id", histoyInfo.getIndustry_id());
        values.put("place_id", histoyInfo.getPlace_id());
        values.put("function_value", histoyInfo.getFunction_value());
        values.put("place_value", histoyInfo.getPlace_value());
        long result = db.insert("SearchHistory", null, values);
        int strid = -1; // 如果strid为-1，数据插入失败
        if (result > 0) {
            Cursor cursor = db.rawQuery("select last_insert_rowid() from SearchHistory", null);
            if (cursor.moveToFirst())
                strid = cursor.getInt(0);
            Log.d("SQLITE", "搜索记录插入成功");
            cursor.close();
        } else {
            Log.d("SQLITE", "搜索记录插入失败");
        }
        closeDB();
        return strid;
    }

    // 插入搜索记录

    public int Insert_KeyWorldHistory(KeyWorldHistory keyWorldHistory) {
        // TODO Auto-generated method stub

        openDB();
        ContentValues values = new ContentValues();
        values.put("industry_id", keyWorldHistory.getIndustry_id());
        values.put("place_id", keyWorldHistory.getPlace_id());
        values.put("place_value", keyWorldHistory.getPlace_value());
        values.put("search_value", keyWorldHistory.getSearch_value());
        values.put("wordtype", keyWorldHistory.getWordtype());
        long result = db.insert("KeyWorldHistory", null, values);
        int strid = -1; // 如果strid为-1，数据插入失败
        if (result > 0) {
            Cursor cursor = db.rawQuery("select last_insert_rowid() from KeyWorldHistory", null);
            if (cursor.moveToFirst())
                strid = cursor.getInt(0);
            Log.d("SQLITE", "搜索记录插入成功");
            cursor.close();
        } else {
            Log.d("SQLITE", "搜索记录插入失败");
        }
        closeDB();
        return strid;
    }

    // 查询搜索记录
    public HistoyInfo[] query_SearchHistory(String industry_id) {
        // TODO Auto-generated method stub
        openDB();
        Cursor cursor;
        String strSQLWhere = "industry_id= '" + industry_id + "'";

        cursor = db.query("SearchHistory", new String[]{"industry_id",
                "function_id", "place_id", "function_value", "place_value"}, strSQLWhere, null, null, null, null);

        HistoyInfo[] histoyInfo = new HistoyInfo[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {

            histoyInfo[i] = new HistoyInfo();
            // histoyInfo.setId(id);// 获取第一列的值,第一列的索引从0开始
            histoyInfo[i].setIndustry_id(cursor.getString(0));
            histoyInfo[i].setFunction_id(cursor.getString(1));
            histoyInfo[i].setPlace_id(cursor.getString(2));
            histoyInfo[i].setFunction_value(cursor.getString(3));
            histoyInfo[i].setPlace_value(cursor.getString(4));
            i++;
        }
        cursor.close();
        closeDB();
        return histoyInfo;
    }

    // 查询搜索记录
    public KeyWorldHistory[] query_KeyWorldHistory(String industry_id) {
        // TODO Auto-generated method stub
        openDB();
        Cursor cursor;
        String strSQLWhere = "industry_id= '" + industry_id + "'";
        cursor = db.query("KeyWorldHistory", new String[]{"industry_id",
                "place_id", "place_value",
                "search_value", "wordtype"}, strSQLWhere, null, null, null, null);

        KeyWorldHistory[] keyWorldHistories = new KeyWorldHistory[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            keyWorldHistories[i] = new KeyWorldHistory();
            // histoyInfo.setId(id);// 获取第一列的值,第一列的索引从0开始
            keyWorldHistories[i].setIndustry_id(cursor.getString(0));
            keyWorldHistories[i].setPlace_id(cursor.getString(1));
            keyWorldHistories[i].setPlace_value(cursor.getString(2));
            keyWorldHistories[i].setSearch_value(cursor.getString(3));
            keyWorldHistories[i].setWordtype(cursor.getString(4));
            i++;
        }
        cursor.close();
        closeDB();
        return keyWorldHistories;
    }

    // 查询单条数据
    public int query_KeyWorldHistory(String industry_id, String search_value, String place_value, String wordtype) {
        // TODO Auto-generated method stub
        openDB();
        Cursor cursor;
        String strSQLWhere = "industry_id= '" + industry_id
                + "' and search_value= '" + search_value
                + "' and place_value= '" + place_value
                + "' and wordtype= '" + wordtype + "'";
        cursor = db.query("KeyWorldHistory", new String[]{"industry_id",
                "place_id", "place_value",
                "search_value", "wordtype"}, strSQLWhere, null, null, null, null);

        KeyWorldHistory keyWorldHistories;

        while (cursor.moveToNext()) {
            keyWorldHistories = new KeyWorldHistory();
            // histoyInfo.setId(id);// 获取第一列的值,第一列的索引从0开始
            keyWorldHistories.setIndustry_id(cursor.getString(0));
            keyWorldHistories.setPlace_id(cursor.getString(1));
            keyWorldHistories.setPlace_value(cursor.getString(2));
            keyWorldHistories.setSearch_value(cursor.getString(3));
            keyWorldHistories.setWordtype(cursor.getString(4));
        }
        cursor.close();
        closeDB();
        return cursor.getCount();
    }

    // 查询单条数据
    public int query_SearchHistory(String industry_id, String function_value, String place_value) {
        // TODO Auto-generated method stub
        openDB();
        Cursor cursor;
        String strSQLWhere = "industry_id= '" + industry_id + "'"
                + " and place_value= '" + place_value + "'"
                + " and function_value= '" + function_value + "'";

        cursor = db.query("SearchHistory", new String[]{"industry_id",
                "function_id", "place_id", "function_value", "place_value"}, strSQLWhere, null, null, null, null);
        HistoyInfo histoyInfo;
        while (cursor.moveToNext()) {
            histoyInfo = new HistoyInfo();
            // histoyInfo.setId(id);// 获取第一列的值,第一列的索引从0开始
            histoyInfo.setIndustry_id(cursor.getString(0));
            histoyInfo.setFunction_id(cursor.getString(1));
            histoyInfo.setPlace_id(cursor.getString(2));
            histoyInfo.setFunction_value(cursor.getString(3));
            histoyInfo.setPlace_value(cursor.getString(4));
        }
        cursor.close();
        closeDB();
        return cursor.getCount();
    }

    /**
     * 删除历史记录下的单条数据
     */
    public boolean Delete_SearchHistory(String industry_id, String function_value, String place_value) {
        // TODO Auto-generated method stub
        openDB();
        String strSQLWhere = "industry_id= '" + industry_id
                + "' and place_value= '" + place_value + "'"
                + " and function_value= '" + function_value + "'";
        int iNumber = db.delete("SearchHistory", strSQLWhere, null);
        closeDB();
        if (iNumber == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 删除keyworld历史记录下的单条数据
     */
    public boolean Delete_KeyWorldHistory(String industry_id, String search_value, String wordtype, String place_value) {
        // TODO Auto-generated method stub
        openDB();
        String strSQLWhere = "industry_id= '" + industry_id
                + "' and place_value= '" + place_value
                + "' and wordtype= '" + wordtype
                + "' and search_value= '" + search_value + "'";
        int iNumber = db.delete("KeyWorldHistory", strSQLWhere, null);
        //
        closeDB();
        if (iNumber == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 删除keyworld历史记录下的单条数据
     *
     * @param id
     * @param industry_id
     * @return
     */
    public boolean Delete_KeyWorldHistory(int id, String industry_id) {
        // TODO Auto-generated method stub
        openDB();
        String strSQLWhere = "id=" + id + " and industry_id= '" + industry_id + "'";
        int iNumber = db.delete("KeyWorldHistory", strSQLWhere, null);
        closeDB();
        if (iNumber == 0) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * 删除keyworld历史记录下的单条数据
     *
     * @param id
     * @param industry_id
     * @return
     */
    public boolean Delete_SearchHistory(int id, String industry_id) {
        // TODO Auto-generated method stub
        openDB();
        String strSQLWhere = "id=" + id + " and industry_id= '" + industry_id
                + "'";
        int iNumber = db.delete("SearchHistory", strSQLWhere, null);
        closeDB();
        if (iNumber == 0) {
            return false;
        } else {

            return true;
        }

    }

    /**
     * 删除历史记录下的所有数据
     */
    public boolean Delete_SearchHistory(String industry_id) {
        // TODO Auto-generated method stub
        openDB();
        String strSQLWhere = "industry_id= '" + industry_id + "'";
        int iNumber = db.delete("SearchHistory", strSQLWhere, null);
        closeDB();
        if (iNumber == 0) {
            return false;
        } else {

            return true;
        }

    }

    /**
     * 删除历史记录下的所有数据
     */
    public boolean Delete_KeyWorldHistory(String industry_id) {
        // TODO Auto-generated method stub
        openDB();
        String strSQLWhere = "industry_id= '" + industry_id + "'";
        int iNumber = db.delete("KeyWorldHistory", strSQLWhere, null);
        closeDB();
        if (iNumber == 0) {
            return false;
        } else {

            return true;
        }

    }

}
