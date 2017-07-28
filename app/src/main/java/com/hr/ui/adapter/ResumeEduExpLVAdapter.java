package com.hr.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.activity.MyResumeActivity;
import com.hr.ui.activity.ResumeEduInfoActivity;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeEducation;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.DataPickerDialog;
import com.hr.ui.utils.datautils.ResumeInfoIDToString;
import com.hr.ui.utils.datautils.ResumeIsUpdateOperator;
import com.hr.ui.view.custom.BeautifulDialog;
import com.hr.ui.view.custom.IdSpineer;

import java.util.ArrayList;

/**
 * 作者：Colin
 * 日期：2016/1/19 09:04
 * 邮箱：bestxt@qq.com
 * <p/>
 * 教育背景 Adapter
 */
public class ResumeEduExpLVAdapter extends BaseExpandableListAdapter {
    private ResumeEduInfoActivity eduInfoContext;
    private Context context;
    private ArrayList<ResumeEducation> list;
    private String resumeId, resumeLanguage;
    private DAO_DBOperator dbOperator;

    public ResumeEduExpLVAdapter(Context context, ArrayList<ResumeEducation> list, String resumeId, String resumeLanguage, ResumeEduInfoActivity eduInfoContext) {
        this.eduInfoContext = eduInfoContext;
        this.context = context;
        this.list = list;
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
        viewHolder.tv_resume_item_2.setText(list.get(groupPosition).getSchoolname());
        String startTime = list.get(groupPosition).getFromyear() + "年" + list.get(groupPosition).getFrommonth() + "月";
        String endTime = list.get(groupPosition).getToyear() + "年" + list.get(groupPosition).getTomonth() + "月";
        if ("0年0月".equals(startTime)) {
            startTime = "至今";
        }
        if ("0年0月".equals(endTime)) {
            endTime = "至今";
        }
        viewHolder.tv_resume_item_time1.setText(startTime + "--" + endTime);
        viewHolder.tv_resume_item_3.setText(list.get(groupPosition).getMoremajor());
        viewHolder.tv_resume_item_4.setText(" " + ResumeInfoIDToString
                .getEducationDegree(context, list.get(groupPosition).getDegree(), true));
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ItemViewHolder ivHolder;
        if (convertView == null) {
            ivHolder = new ItemViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_reusme_eduinfo, null);
            ivHolder.et_resume_item_resumeedu_schoolname = (EditText) convertView.findViewById(R.id.et_resume_item_resumeedu_schoolname);
            ivHolder.et_resume_item_resumeedu_describe = (EditText) convertView.findViewById(R.id.et_resume_item_resumeedu_describe);
            ivHolder.tv_resume_item_resumeedu_starttime = (TextView) convertView.findViewById(R.id.tv_resume_item_resumeedu_starttime);
            ivHolder.tv_resume_item_resumeedu_endtime = (TextView) convertView.findViewById(R.id.tv_resume_item_resumeedu_endtime);
            ivHolder.sp_resume_item_resumeedu_degree = (IdSpineer) convertView.findViewById(R.id.sp_resume_item_resumeedu_degree);
            ivHolder.rl_resume_item_resumeedu_add = (RelativeLayout) convertView.findViewById(R.id.rl_resume_item_resumeedu_add);
            ivHolder.et_resume_item_resumeedu_majorname = (EditText) convertView.findViewById(R.id.et_resume_item_resumeedu_majorname);
            ivHolder.tv_resume_item_resumeedu_delete = (TextView) convertView.findViewById(R.id.tv_resume_item_resumeedu_delete);

            convertView.setTag(ivHolder);
        } else {
            ivHolder = (ItemViewHolder) convertView.getTag();
        }
        ivHolder.sp_resume_item_resumeedu_degree.setAdapter(new SpinnerAdapter(context, android.R.layout.simple_spinner_item, context.getResources().getStringArray(R.array.array_degree_zh)));
        // 开始时间
        String fromyear = list.get(groupPosition).getFromyear();
        String frommonth = list.get(groupPosition).getFrommonth();
        if ("0".equals(fromyear) && "0".equals(frommonth)) {
            ivHolder.tv_resume_item_resumeedu_starttime.setText("至今");
        } else {
            ivHolder.tv_resume_item_resumeedu_starttime.setText(fromyear + "-" + frommonth);
        }
        // 结束时间
        String endyear = list.get(groupPosition).getToyear();
        String endmonth = list.get(groupPosition).getTomonth();
        if ("0".equals(endyear) && "0".equals(endmonth)) {
            ivHolder.tv_resume_item_resumeedu_endtime.setText("至今");
        } else {
            ivHolder.tv_resume_item_resumeedu_endtime.setText(endyear + "-" + endmonth);
        }
        String descriptionString = list.get(groupPosition).getEdudetail();


        ivHolder.tv_resume_item_resumeedu_starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataPickerDialog.showDialog(context, ivHolder.tv_resume_item_resumeedu_starttime, 2);
            }
        });
        ivHolder.tv_resume_item_resumeedu_endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataPickerDialog.showDialog(context, ivHolder.tv_resume_item_resumeedu_endtime, 2);
            }
        });
        ivHolder.rl_resume_item_resumeedu_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData(groupPosition, ivHolder);
            }
        });
        ivHolder.sp_resume_item_resumeedu_degree.setIds(context.getResources().getStringArray(
                R.array.array_degree_ids));
        ivHolder.sp_resume_item_resumeedu_degree
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        try {
                            if (ivHolder.sp_resume_item_resumeedu_degree.idStrings != null) {
                                ivHolder.sp_resume_item_resumeedu_degree.idString = ivHolder.sp_resume_item_resumeedu_degree.idStrings[arg2];
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });
        ivHolder.et_resume_item_resumeedu_schoolname.setText(list.get(groupPosition).getSchoolname());
        ivHolder.et_resume_item_resumeedu_majorname.setText(list.get(groupPosition).getMoremajor());
        ivHolder.sp_resume_item_resumeedu_degree.setSelectedItem(list.get(groupPosition).getDegree());
        ivHolder.et_resume_item_resumeedu_describe.setText(list.get(groupPosition).getEdudetail());

        ivHolder.tv_resume_item_resumeedu_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteConfirm(groupPosition);
            }
        });
        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    class ViewHolder {
        TextView tv_resume_item_time1, tv_resume_item_2, tv_resume_item_3, tv_resume_item_4;
    }

    private void saveData(int position, ItemViewHolder ivHolder) {

        ResumeEducation resumeEducation = list.get(position);
        if (true) {
            if (ivHolder.et_resume_item_resumeedu_schoolname.getText().toString().trim()
                    .length() == 0) {
                Toast.makeText(context, "请输入学校名称", Toast.LENGTH_LONG).show();
                return;
            }
            if (!ivHolder.tv_resume_item_resumeedu_starttime.getText().toString().trim().contains("-")
                    && !"至今".equals(ivHolder.tv_resume_item_resumeedu_starttime.getText().toString().trim())) {
                // System.out.println(start_time.getText().toString());
                Toast.makeText(context, "请输入开始时间", Toast.LENGTH_LONG).show();
                return;
            }
            if (!ivHolder.tv_resume_item_resumeedu_endtime.getText().toString().trim().contains("-")
                    && !"至今".equals(ivHolder.tv_resume_item_resumeedu_endtime.getText().toString().trim())) {
                Toast.makeText(context, "请输入结束时间", Toast.LENGTH_LONG).show();
                return;
            }

            if (ivHolder.et_resume_item_resumeedu_majorname.getText().toString().trim()
                    .length() == 0) {
                Toast.makeText(context, "请输入专业", Toast.LENGTH_LONG).show();
                return;
            }
        }
        String starttimeString = ivHolder.tv_resume_item_resumeedu_starttime
                .getText().toString().trim();
        String[] starttimeStrings;
        if ("至今".equals(starttimeString)) {
            starttimeStrings = new String[]{"0", "0"};
        } else {
            starttimeStrings = starttimeString.split("-");
        }
        String endtimeString = ivHolder.tv_resume_item_resumeedu_endtime
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
            Toast.makeText(context,
                    context.getString(R.string.date0), Toast.LENGTH_SHORT).show();
            return;
        }
        if (endYear == startYear && endMonth < startMonth) {
            Toast.makeText(context, context.
                    getString(R.string.date0), Toast.LENGTH_SHORT).show();
            return;
        }

        String schoolnameString = ivHolder.et_resume_item_resumeedu_schoolname.getText()
                .toString().trim();
        String majorString = ivHolder.et_resume_item_resumeedu_majorname.getText().toString()
                .trim();
        String descriptionString = ivHolder.et_resume_item_resumeedu_describe.getText()
                .toString();
        String idsString = ivHolder.sp_resume_item_resumeedu_degree.getSelectedId();
        list.get(position).setResume_id(resumeId);
        list.get(position).setResume_language(resumeLanguage);
        list.get(position).setUser_id(MyUtils.userID);
        list.get(position).setFromyear(starttimeStrings[0]);
        list.get(position).setFrommonth(starttimeStrings[1]);
        list.get(position).setToyear(endtimeStrings[0]);
        list.get(position).setTomonth(endtimeStrings[1]);
        list.get(position).setSchoolname(schoolnameString);
        list.get(position).setMoremajor(majorString);
        list.get(position).setEdudetail(descriptionString);
        list.get(position).setDegree(idsString);

        if (list.get(position).getId() == -1) {// 新建后保存
            ArrayList<ResumeEducation> arrayList = new ArrayList<ResumeEducation>();
            arrayList.add(list.get(position));
            int resultInsert = dbOperator.Insert_ResumeEducation(arrayList);
            if (resultInsert > 0) {
                ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator,
                        resumeId, resumeLanguage);
                eduInfoContext.refresh();
                eduInfoContext.isAdd = true;
                eduInfoContext.modification = true;
//                MyResumeActivity.myResumeActivity.shouldUpdate = true;
                Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "添加失败", Toast.LENGTH_SHORT).show();
            }
        } else {// 修改后保存

            // 更新教育经历
            boolean resultUpdate = dbOperator
                    .update_ResumeEducation(resumeEducation);
            // System.out.println("传入DB的内容：" + resumeEducation.toString());
            if (resultUpdate) {
                ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator,
                        resumeId, resumeLanguage);
                eduInfoContext.refresh();
                eduInfoContext.isAdd = true;
                eduInfoContext.modification = true;
//                MyResumeActivity.myResumeActivity.shouldUpdate = true;
                Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT).show();
            }
        }

    }

    class ItemViewHolder {
        TextView tv_resume_item_resumeedu_starttime, tv_resume_item_resumeedu_endtime, tv_resume_item_resumeedu_delete;
        EditText et_resume_item_resumeedu_schoolname, et_resume_item_resumeedu_describe, et_resume_item_resumeedu_majorname;
        IdSpineer sp_resume_item_resumeedu_degree;
        RelativeLayout rl_resume_item_resumeedu_add;
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
        boolean delResule = dbOperator.Delete_Data("ResumeEducation", id);
        if (delResule) {
            ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator, resumeId, resumeLanguage);
            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < list.size(); i++) {
                if (ResumeEduInfoActivity.resumeEduInfoActivity.elv_resume_eduinfo.isGroupExpanded(i)) {
                    ResumeEduInfoActivity.resumeEduInfoActivity.elv_resume_eduinfo.collapseGroup(i);
                }
            }
            eduInfoContext.isAdd = true;
            eduInfoContext.modification = true;
//            MyResumeActivity.myResumeActivity.shouldUpdate = true;
            // 刷新简历完整度
        } else {
            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
        }
    }
}
