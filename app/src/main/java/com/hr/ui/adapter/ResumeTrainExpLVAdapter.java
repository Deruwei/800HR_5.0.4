package com.hr.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.activity.MyResumeActivity;
import com.hr.ui.activity.ResumeTrainExpActivity;
import com.hr.ui.activity.SelectPlaceToResumeActivity;
import com.hr.ui.config.Constants;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumePlant;
import com.hr.ui.utils.CityNameConvertCityID;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.DataPickerDialog;
import com.hr.ui.utils.datautils.ResumeIsUpdateOperator;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.utils.netutils.NetUtils;
import com.hr.ui.view.custom.BeautifulDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * 作者：Colin
 * 日期：2016/1/20 19:29
 * 邮箱：bestxt@qq.com
 */
public class ResumeTrainExpLVAdapter extends BaseExpandableListAdapter {
    private Context context;
    private static ArrayList<ResumePlant> list;
    private String resumeId, resumeLanguage;
    private DAO_DBOperator dbOperator;
    private ResumeTrainExpActivity resumeTrainContext;

    private static String placeId = ""; // 地区 ID
    private boolean isCHS = true;
    private static ItemViewHolder ivHolder;

    public ResumeTrainExpLVAdapter(Context context, ArrayList<ResumePlant> list, String resumeId, String resumeLanguage, ResumeTrainExpActivity resumeTrainContext) {
        this.context = context;
        this.list = list;
        this.resumeTrainContext = resumeTrainContext;
        this.resumeId = resumeId;
        this.resumeLanguage = resumeLanguage;
        dbOperator = new DAO_DBOperator(context);
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
        viewHolder.tv_resume_item_2.setText(list.get(groupPosition).getInstitution());
        String startTime = list.get(groupPosition).getFromyear() + "年" + list.get(groupPosition).getFrommonth() + "月";
        String endTime = list.get(groupPosition).getToyear() + "年" + list.get(groupPosition).getTomonth() + "月";
        if ("0年0月".equals(startTime)) {
            startTime = "至今";
        }
        if ("0年0月".equals(endTime)) {
            endTime = "至今";
        }
        viewHolder.tv_resume_item_time1.setText(startTime + "--" + endTime);
        viewHolder.tv_resume_item_3.setText(list.get(groupPosition).getCourse());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            ivHolder = new ItemViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_resume_explv_trainexp, null);
            ivHolder.et_resume_item_trainexp_describe = (EditText) convertView.findViewById(R.id.et_resume_item_trainexp_describe);
            ivHolder.et_resume_item_trainexp_certificate = (EditText) convertView.findViewById(R.id.et_resume_item_trainexp_certificate);
            ivHolder.et_resume_item_trainexp_course = (EditText) convertView.findViewById(R.id.et_resume_item_trainexp_course);
            ivHolder.tv_resume_item_trainexp_endtime = (TextView) convertView.findViewById(R.id.tv_resume_item_trainexp_endtime);
            ivHolder.tv_resume_item_trainexp_starttime = (TextView) convertView.findViewById(R.id.tv_resume_item_trainexp_starttime);
            ivHolder.tv_resume_item_trainexp_trainplace = (TextView) convertView.findViewById(R.id.tv_resume_item_trainexp_trainplace);
            ivHolder.et_resume_item_trainexp_comname = (EditText) convertView.findViewById(R.id.et_resume_item_trainexp_comname);
            ivHolder.rl_resume_item_trainexp_add = (RelativeLayout) convertView.findViewById(R.id.rl_resume_item_trainexp_add);
            ivHolder.tv_resume_item_trainexp_delete = (TextView) convertView.findViewById(R.id.tv_resume_item_trainexp_delete);
            convertView.setTag(ivHolder);
        } else {
            ivHolder = (ItemViewHolder) convertView.getTag();
        }
        String startTime = list.get(groupPosition).getFromyear() + "-" + list.get(groupPosition).getFrommonth();
        String endTime = list.get(groupPosition).getToyear() + "-" + list.get(groupPosition).getTomonth();
        if ("0-0".equals(startTime)) {
            startTime = "至今";
        }
        if ("0-0".equals(endTime)) {
            endTime = "至今";
        }
        ivHolder.tv_resume_item_trainexp_starttime.setText(startTime);
        ivHolder.tv_resume_item_trainexp_endtime.setText(endTime);
        ivHolder.et_resume_item_trainexp_certificate.setText(list.get(groupPosition).getCertification());
        ivHolder.et_resume_item_trainexp_course.setText(list.get(groupPosition).getCourse());
        ivHolder.et_resume_item_trainexp_describe.setText(list.get(groupPosition).getTraindetail());
        ivHolder.et_resume_item_trainexp_comname.setText(list.get(groupPosition).getInstitution());
        ivHolder.tv_resume_item_trainexp_starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataPickerDialog.showDialog(context, ivHolder.tv_resume_item_trainexp_starttime, 2);
            }
        });
        ivHolder.tv_resume_item_trainexp_endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataPickerDialog.showDialog(context, ivHolder.tv_resume_item_trainexp_endtime, 2);
            }
        });
        ivHolder.rl_resume_item_trainexp_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData(groupPosition, ivHolder);
            }
        });
        ivHolder.tv_resume_item_trainexp_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteConfirm(groupPosition);
            }
        });
        // 期望工作地点
        ivHolder.tv_resume_item_trainexp_trainplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SelectPlaceToResumeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("fromtag", 103);
                intent.putExtra("isCHS", true);
                intent.putExtra("filter", "place");
                intent.putExtra("value", "省市选择");
                context.startActivity(intent);
            }
        });
        JSONArray cityJSONArray = null;
//        try {
//            if (MyUtils.USE_ONLINE_ARRAY && true) {
//                cityJSONArray = NetService.getCityAsJSONArray(context, "city.json");
//            } else {
//                if (true) {
//                    InputStream inputStream = context.getAssets().open("city_zh.json");
//                    cityJSONArray = new JSONArray(NetUtils.readAsString(
//                            inputStream, Constants.ENCODE));
//                } else {
////                    InputStream inputStream = getAssets().open("city_en.json");
////                    cityJSONArray = new JSONArray(NetUtils.readAsString(
////                            inputStream, Constants.ENCODE));
//                }
//            }
//            placeId = list.get(groupPosition).getPlace();
//            for (int i = 0; i < cityJSONArray.length(); i++) {
//                JSONObject object = cityJSONArray.getJSONObject(i);
//                if (object.has(placeId)) {
//                    ivHolder.tv_resume_item_trainexp_trainplace.setText(object.getString(placeId));
//                    break;
//                }
//            }
//
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        try {
            if (MyUtils.USE_ONLINE_ARRAY) {
                cityJSONArray = NetService.getCityAsJSONArray(context, "city.json");
            } else {
                InputStream inputStream = context.getAssets().open("city_zh.json");
                cityJSONArray = new JSONArray(NetUtils.readAsString(
                        inputStream, Constants.ENCODE));
            }
            placeId = list.get(groupPosition).getPlace();
            for (int i = 0; i < cityJSONArray.length(); i++) {
                JSONObject object = cityJSONArray.getJSONObject(i);
                if (object.has(placeId)) {
                    ivHolder.tv_resume_item_trainexp_trainplace.setText(object.getString(placeId));
                    break;
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertView;
    }

    private void saveData(int position, ItemViewHolder ivHolder) {
        ResumePlant resumePlant = list.get(position);
        if (ivHolder.et_resume_item_trainexp_comname.getText()
                .toString().trim().length() == 0) {
            Toast.makeText(context, "请输入培训机构名称", Toast.LENGTH_LONG).show();
            return;
        }
        if (!ivHolder.tv_resume_item_trainexp_starttime.getText()
                .toString().contains("-")
                && !"至今".equals(ivHolder.tv_resume_item_trainexp_starttime
                .getText().toString())) {
            Toast.makeText(context, "请输入开始时间", Toast.LENGTH_LONG).show();
            return;
        }
        if (!ivHolder.tv_resume_item_trainexp_endtime.getText()
                .toString().contains("-")
                && !"至今".equals(ivHolder.tv_resume_item_trainexp_endtime
                .getText().toString())) {
            Toast.makeText(context, "请输入结束时间", Toast.LENGTH_LONG).show();
            return;
        }
        if (ivHolder.et_resume_item_trainexp_course.getText().toString()
                .trim().length() == 0) {
            Toast.makeText(context, "请输入培训课程", Toast.LENGTH_LONG).show();
            return;
        }

        if (ivHolder.et_resume_item_trainexp_certificate.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "请输入所获得证书", Toast.LENGTH_LONG).show();
            return;
        }
        if (ivHolder.tv_resume_item_trainexp_trainplace.getText()
                .toString().trim().length() == 0 && ivHolder.tv_resume_item_trainexp_trainplace.getText()
                .toString().trim().equals("请选择地点")) {
            Toast.makeText(context, "请输入培训地点", Toast.LENGTH_LONG).show();
            return;
        }

        if (ivHolder.et_resume_item_trainexp_describe.getText()
                .toString().trim().length() == 0) {
            Toast.makeText(context, "请输入培训描述", Toast.LENGTH_LONG).show();
            return;
        }
        String starttimeString = ivHolder.tv_resume_item_trainexp_starttime
                .getText().toString().trim();
        String[] starttimeStrings;
        if ("至今".equals(starttimeString)) {
            starttimeStrings = new String[]{"0", "0"};
        } else {
            starttimeStrings = starttimeString.split("-");
        }
        String endtimeString = ivHolder.tv_resume_item_trainexp_endtime.getText().toString().trim();
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
        String companynameString = ivHolder.et_resume_item_trainexp_comname
                .getText().toString().trim();
        String courseString = ivHolder.et_resume_item_trainexp_course.getText().toString().trim();
        // 所任职位
        String certificationString = ivHolder.et_resume_item_trainexp_certificate.getText().toString().trim();
        // 职责描述
        String responsibilityString = ivHolder.et_resume_item_trainexp_describe.getText().toString().trim();
        String workPlaceId = CityNameConvertCityID.convertCityID(context, ivHolder.tv_resume_item_trainexp_trainplace.getText().toString());
        resumePlant.setFromyear(starttimeStrings[0]);
        resumePlant.setFrommonth(starttimeStrings[1]);
        resumePlant.setToyear(endtimeStrings[0]);
        resumePlant.setTomonth(endtimeStrings[1]);
        resumePlant.setPlace(workPlaceId);
        resumePlant.setInstitution(companynameString);
        resumePlant.setCourse(courseString);
        resumePlant.setCertification(certificationString);
        resumePlant.setTraindetail(responsibilityString);
        resumePlant.setResume_id(resumeId);
        resumePlant.setResume_language(resumeLanguage);
        resumePlant.setUser_id(MyUtils.userID);
        if (resumePlant.getId() == -1) {// 新建后保存

            ArrayList<ResumePlant> arrayList = new ArrayList<ResumePlant>();
            arrayList.add(resumePlant);
            int resultInsert = dbOperator.Insert_ResumeTraining(arrayList);
            if (resultInsert > 0) {
                ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator, resumeId, resumeLanguage);
                resumeTrainContext.refresh();
//                MyResumeActivity.myResumeActivity.shouldUpdate = true;
                resumeTrainContext.isAdd = true;
                resumeTrainContext.modification = true;
                Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "添加失败", Toast.LENGTH_SHORT).show();
            }
        } else {// 修改后保存
            // 更新到数据库
            boolean resultUpdate = dbOperator.update_ResumeTraining(resumePlant);
            if (resultUpdate) {
                ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator, resumeId, resumeLanguage);
                resumeTrainContext.refresh();
                resumeTrainContext.isAdd = true;
                resumeTrainContext.modification = true;
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
        TextView tv_resume_item_trainexp_delete, tv_resume_item_trainexp_starttime, tv_resume_item_trainexp_endtime, tv_resume_item_trainexp_trainplace;
        EditText et_resume_item_trainexp_course, et_resume_item_trainexp_certificate, et_resume_item_trainexp_describe, et_resume_item_trainexp_comname;
        RelativeLayout rl_resume_item_trainexp_add;
    }

    public static void setPlaceText(String value) {
        ivHolder.tv_resume_item_trainexp_trainplace.setText(value);
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
        boolean delResule = dbOperator.Delete_Data("ResumeTraining", id);
        if (delResule) {
            ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator, resumeId, resumeLanguage);
            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < list.size(); i++) {
                if (ResumeTrainExpActivity.resumeTrainExpActivity.elv_resume_trainexp.isGroupExpanded(i)) {
                    ResumeTrainExpActivity.resumeTrainExpActivity.elv_resume_trainexp.collapseGroup(i);
                }
            }
            resumeTrainContext.isAdd = true;
            resumeTrainContext.modification = true;
//            MyResumeActivity.myResumeActivity.shouldUpdate = true;
            // 刷新简历完整度
        } else {
            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
        }
    }
}
