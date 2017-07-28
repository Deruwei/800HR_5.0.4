package com.hr.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.activity.MyResumeActivity;
import com.hr.ui.activity.ResumeJobExpActivity;
import com.hr.ui.activity.SelectPlaceToResumeActivity;
import com.hr.ui.config.Constants;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeExperience;
import com.hr.ui.utils.CityNameConvertCityID;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.DataPickerDialog;
import com.hr.ui.utils.datautils.ResumeIsUpdateOperator;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.utils.netutils.NetUtils;
import com.hr.ui.view.custom.BeautifulDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * 作者：Colin
 * 日期：2016/1/19 09:04
 * 邮箱：bestxt@qq.com
 * <p/>
 * 工作经验 Adapter
 */
public class ResumeJobExpLVAdapter extends BaseExpandableListAdapter {
    private Context context;
    private static ArrayList<ResumeExperience> list;
    private static String resumeId;
    private String resumeLanguage;
    private DAO_DBOperator dbOperator;
    private static String workplace;
    private static String placeId;
    private ResumeJobExpActivity resumeJobContext;
    private static ItemViewHolder ivHolder;

    public ResumeJobExpLVAdapter(Context context, ArrayList<ResumeExperience> list, String resumeId, String resumeLanguage, ResumeJobExpActivity resumeJobContext) {
        this.context = context;
        this.resumeJobContext = resumeJobContext;
        this.list = list;
        this.resumeId = resumeId;
        this.resumeLanguage = resumeLanguage;
        dbOperator = new DAO_DBOperator(context);
    }

    public static void setPlace(int position, String place) {
        list.get(position).setCompanyaddress(place);
        workplace = place;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_resume_explistview, null);
            viewHolder.tv_resume_item_time1 = (TextView) convertView.findViewById(R.id.tv_resume_item_time1);
            viewHolder.tv_resume_item_2 = (TextView) convertView.findViewById(R.id.tv_resume_item_2);
            viewHolder.tv_resume_item_3 = (TextView) convertView.findViewById(R.id.tv_resume_item_3);
            viewHolder.tv_resume_item_4 = (TextView) convertView.findViewById(R.id.tv_resume_item_4);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_resume_item_2.setText(list.get(groupPosition).getCompany());
        String startTime = list.get(groupPosition).getFromyear() + "年" + list.get(groupPosition).getFrommonth() + "月";
        String endTime = list.get(groupPosition).getToyear() + "年" + list.get(groupPosition).getTomonth() + "月";
        if ("0年0月".equals(startTime)) {
            startTime = "至今";
        }
        if ("0年0月".equals(endTime)) {
            endTime = "至今";
        }
        viewHolder.tv_resume_item_time1.setText(startTime + "--" + endTime);
        viewHolder.tv_resume_item_3.setText(list.get(groupPosition).getPosition());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            ivHolder = new ItemViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_resume_explv_jobexp, null);
            ivHolder.et_resume_item_jobexp_describe = (EditText) convertView.findViewById(R.id.et_resume_item_jobexp_describe);
            ivHolder.et_resume_item_jobexp_post = (EditText) convertView.findViewById(R.id.et_resume_item_jobexp_post);
            ivHolder.et_resume_item_jobexp_salary = (EditText) convertView.findViewById(R.id.et_resume_item_jobexp_salary);
            ivHolder.tv_resume_item_jobexp_endtime = (TextView) convertView.findViewById(R.id.tv_resume_item_jobexp_endtime);
            ivHolder.tv_resume_item_jobexp_starttime = (TextView) convertView.findViewById(R.id.tv_resume_item_jobexp_starttime);
            ivHolder.tv_resume_item_jobexp_workplace = (TextView) convertView.findViewById(R.id.tv_resume_item_jobexp_workplace);
            ivHolder.et_resume_item_jobexp_comname = (EditText) convertView.findViewById(R.id.et_resume_item_jobexp_comname);
            ivHolder.rl_resume_item_jobexp_add = (RelativeLayout) convertView.findViewById(R.id.rl_resume_item_jobexp_add);
            ivHolder.tv_resume_item_jobexp_delete = (TextView) convertView.findViewById(R.id.tv_resume_item_jobexp_delete);
            ivHolder.cb_item_ischeck = (CheckBox) convertView.findViewById(R.id.cb_item_ischeck);

            convertView.setTag(ivHolder);
        } else {
            ivHolder = (ItemViewHolder) convertView.getTag();
        }
        ivHolder.et_resume_item_jobexp_comname.setText(list.get(groupPosition).getCompany());
        String startTime = list.get(groupPosition).getFromyear() + "-" + list.get(groupPosition).getFrommonth();
        String endTime = list.get(groupPosition).getToyear() + "-" + list.get(groupPosition).getTomonth();
        if ("0-0".equals(startTime)) {
            startTime = "至今";
        }
        if ("0-0".equals(endTime)) {
            endTime = "至今";
        }
        if (list.get(groupPosition).getSalary_hide().equals("0")) {
            ivHolder.cb_item_ischeck.setChecked(false);
        } else if (list.get(groupPosition).getSalary_hide().equals("1")) {
            ivHolder.cb_item_ischeck.setChecked(true);
        }
        ivHolder.cb_item_ischeck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    list.get(groupPosition).setSalary_hide("1");
                } else {
                    list.get(groupPosition).setSalary_hide("0");
                }
            }
        });
        ivHolder.tv_resume_item_jobexp_starttime.setText(startTime);
        ivHolder.tv_resume_item_jobexp_endtime.setText(endTime);
        ivHolder.et_resume_item_jobexp_post.setText(list.get(groupPosition).getPosition());
        ivHolder.et_resume_item_jobexp_salary.setText(list.get(groupPosition).getSalary());
        ivHolder.et_resume_item_jobexp_describe.setText(list.get(groupPosition).getResponsibility());
        ivHolder.tv_resume_item_jobexp_starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataPickerDialog.showDialog(context, ivHolder.tv_resume_item_jobexp_starttime, 2);
            }
        });
        ivHolder.tv_resume_item_jobexp_endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataPickerDialog.showDialog(context, ivHolder.tv_resume_item_jobexp_endtime, 2);
            }
        });
        ivHolder.rl_resume_item_jobexp_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData(groupPosition, ivHolder);
            }
        });
        ivHolder.tv_resume_item_jobexp_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteConfirm(groupPosition);
            }
        });
        ivHolder.tv_resume_item_jobexp_workplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SelectPlaceToResumeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("fromtag", 102);
                intent.putExtra("filter", "place");
                intent.putExtra("isCHS", true);
                intent.putExtra("value", "省市选择");
                context.startActivity(intent);
            }
        });
        JSONArray cityJSONArray = null;
        try {
            if (MyUtils.USE_ONLINE_ARRAY) {
                cityJSONArray = NetService.getCityAsJSONArray(context, "city.json");
            } else {
                InputStream inputStream = context.getAssets().open("city_zh.json");
                cityJSONArray = new JSONArray(NetUtils.readAsString(
                        inputStream, Constants.ENCODE));
            }
            placeId = list.get(groupPosition).getCompanyaddress();
            for (int i = 0; i < cityJSONArray.length(); i++) {
                JSONObject object = cityJSONArray.getJSONObject(i);
                if (object.has(placeId)) {
                    ivHolder.tv_resume_item_jobexp_workplace
                            .setText(object.getString(placeId));
                    break;
                }
            }
//            ivHolder.tv_resume_item_jobexp_workplace.setText(list.get(groupPosition).getCompanyaddress());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private void saveData(int position, ItemViewHolder ivHolder) {
        ResumeExperience resumeExperience = list.get(position);
        if (ivHolder.et_resume_item_jobexp_comname.getText()
                .toString().trim().length() == 0) {
            Toast.makeText(context, "请输入公司名称", Toast.LENGTH_LONG).show();
            return;
        }
        if (!ivHolder.tv_resume_item_jobexp_starttime.getText()
                .toString().contains("-")
                && !"至今".equals(ivHolder.tv_resume_item_jobexp_starttime
                .getText().toString())) {
            Toast.makeText(context, "请输入开始时间", Toast.LENGTH_LONG).show();
            return;
        }
        if (!ivHolder.tv_resume_item_jobexp_endtime.getText()
                .toString().contains("-")
                && !"至今".equals(ivHolder.tv_resume_item_jobexp_endtime
                .getText().toString())) {
            Toast.makeText(context, "请输入结束时间", Toast.LENGTH_LONG).show();
            return;
        }
        if (ivHolder.tv_resume_item_jobexp_workplace.getText()
                .toString().trim().length() == 0 || ivHolder.tv_resume_item_jobexp_workplace.getText().toString().trim().equals("请选择地点")) {
            Toast.makeText(context, "请选择工作地点", Toast.LENGTH_LONG).show();
            return;
        }
        if (ivHolder.et_resume_item_jobexp_salary.getText().toString()
                .trim().length() == 0) {
            Toast.makeText(context, "请输入税前月薪", Toast.LENGTH_LONG).show();
            return;
        }
        if (ivHolder.et_resume_item_jobexp_salary.getText().toString()
                .trim().substring(0, 1).equals("0")) {
            Toast.makeText(context, "请输入大于0的税前月薪", Toast.LENGTH_LONG).show();
            return;
        }

        if (ivHolder.et_resume_item_jobexp_post.getText()
                .toString().trim().length() == 0) {
            Toast.makeText(context, "请输入所任职位", Toast.LENGTH_LONG).show();
            return;
        }
        if (ivHolder.et_resume_item_jobexp_describe.getText()
                .toString().trim().length() == 0) {
            Toast.makeText(context, "请输入职位描述", Toast.LENGTH_LONG).show();
            return;
        }
        String starttimeString = ivHolder.tv_resume_item_jobexp_starttime
                .getText().toString().trim();
        String[] starttimeStrings;
        if ("至今".equals(starttimeString)) {
            starttimeStrings = new String[]{"0", "0"};
        } else {
            starttimeStrings = starttimeString.split("-");
        }
        String endtimeString = ivHolder.tv_resume_item_jobexp_endtime
                .getText().toString().trim();
        String[] endtimeStrings;
        if ("至今".equals(endtimeString)) {
            endtimeStrings = new String[]{"0", "0"};
        } else {
            endtimeStrings = endtimeString.split("-");
        }
        if (starttimeStrings.length < 1) {
            Toast.makeText(context, "请填写开始时间", Toast.LENGTH_SHORT).show();
            return;
        }
        if (endtimeStrings.length < 1) {
            Toast.makeText(context, "请填写结束时间", Toast.LENGTH_SHORT).show();
            return;
        }

        // 时间段过滤
        int startYear = Integer.parseInt(starttimeStrings[0]);
        int startMonth = Integer.parseInt(starttimeStrings[1]);
        int endYear = Integer.parseInt(endtimeStrings[0]);
        int endMonth = Integer.parseInt(endtimeStrings[1]);
        if (endYear < startYear) {
            Toast.makeText(context, context.getString(R.string.date0), Toast.LENGTH_SHORT).show();
            return;
        }
        if (endYear == startYear && endMonth < startMonth) {
            Toast.makeText(context, context.getString(R.string.date0), Toast.LENGTH_SHORT).show();
            return;
        }

        String companynameString = ivHolder.et_resume_item_jobexp_comname
                .getText().toString().trim();
        String salaryString = ivHolder.et_resume_item_jobexp_salary.getText().toString().trim();
        // 所任职位
        String positionString = ivHolder.et_resume_item_jobexp_post
                .getText().toString().trim();
        // 职责描述
        String responsibilityString = ivHolder.et_resume_item_jobexp_describe
                .getText().toString().trim();
        String workPlaceId = CityNameConvertCityID.convertCityID(context, ivHolder.tv_resume_item_jobexp_workplace.getText().toString());

        resumeExperience.setFromyear(starttimeStrings[0]);
        resumeExperience.setFrommonth(starttimeStrings[1]);
        resumeExperience.setToyear(endtimeStrings[0]);
        resumeExperience.setTomonth(endtimeStrings[1]);
        resumeExperience.setCompanyaddress(workPlaceId);
        resumeExperience.setCompany(companynameString);
        resumeExperience.setSalary(salaryString);
        resumeExperience.setPosition(positionString);
        resumeExperience.setResponsibility(responsibilityString);
        resumeExperience.setResume_id(resumeId);
        resumeExperience.setResume_language(resumeLanguage);
        resumeExperience.setUser_id(MyUtils.userID);

        if (resumeExperience.getId() == -1) {// 新建后保存
            ArrayList<ResumeExperience> arrayList = new ArrayList<ResumeExperience>();
            arrayList.add(resumeExperience);
            int resultInsert = dbOperator
                    .Insert_ResumeWorkExperience(arrayList);
            if (resultInsert > 0) {
                ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator,
                        resumeId, resumeLanguage);
                resumeJobContext.refresh();
//                MyResumeActivity.myResumeActivity.shouldUpdate = true;
                resumeJobContext.isAdd = true;
                resumeJobContext.modification = true;
                Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "添加失败", Toast.LENGTH_SHORT).show();
            }
        } else {// 修改后保存
            // 更新到数据库
            boolean resultUpdate = dbOperator
                    .update_ResumeWorkExperience(resumeExperience);
            if (resultUpdate) {
                ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator,
                        resumeId, resumeLanguage);
                resumeJobContext.refresh();
                resumeJobContext.isAdd = true;
                resumeJobContext.modification = true;
//                MyResumeActivity.myResumeActivity.shouldUpdate = true;
                Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
                // 刷新简历完整度
            } else {
                Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class ViewHolder {
        TextView tv_resume_item_time1, tv_resume_item_2, tv_resume_item_3, tv_resume_item_4;
    }

    class ItemViewHolder {
        TextView tv_resume_item_jobexp_delete, tv_resume_item_jobexp_starttime, tv_resume_item_jobexp_endtime, tv_resume_item_jobexp_workplace;
        EditText et_resume_item_jobexp_salary, et_resume_item_jobexp_post, et_resume_item_jobexp_describe, et_resume_item_jobexp_comname;
        RelativeLayout rl_resume_item_jobexp_add;
        CheckBox cb_item_ischeck;
    }

    public static void setPlaceText(String value) {
        ivHolder.tv_resume_item_jobexp_workplace.setText(value);
    }

    public static void setPlaceId(String string) {
        placeId = string;
    }

    private void deleteConfirm(final int position) {
        BeautifulDialog.Builder dialog = new BeautifulDialog.Builder(context);
        dialog.setTitle("温馨提示");
        dialog.setMessage("您确定要删除吗？");
        dialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        deleteData(list.get(position).getId());
                        list.remove(position);
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
        dialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.create().show();
    }

    /**
     * 删除数据
     */
    private void deleteData(int id) {
        boolean delResule = dbOperator.Delete_Data("ResumeWorkExperience", id);
        if (delResule) {
            ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator, resumeId, resumeLanguage);
            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < list.size(); i++) {
                if (ResumeJobExpActivity.resumeJobExpActivity.elv_resume_jobexp.isGroupExpanded(i)) {
                    ResumeJobExpActivity.resumeJobExpActivity.elv_resume_jobexp.collapseGroup(i);
                }
            }
            resumeJobContext.isAdd = true;
            resumeJobContext.modification = true;
//            MyResumeActivity.myResumeActivity.shouldUpdate = true;
            // 刷新简历完整度
        } else {
            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
        }
    }
}
