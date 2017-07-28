package com.hr.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.hr.ui.activity.ResumeProjectExpActivity;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeProject;
import com.hr.ui.utils.datautils.DataPickerDialog;
import com.hr.ui.utils.datautils.ResumeIsUpdateOperator;

import java.util.ArrayList;

/**
 * 作者：Colin
 * 日期：2016/1/19 09:04
 * 邮箱：bestxt@qq.com
 * <p/>
 * 工作经验 Adapter
 */
public class ResumeProjectExpAdapter extends BaseExpandableListAdapter {
    private Context context;
    private static ArrayList<ResumeProject> list;
    private String resumeId, resumeLanguage;
    private DAO_DBOperator dbOperator;
    private ResumeProjectExpActivity resumeProjectContext;

    public ResumeProjectExpAdapter(Context context, ArrayList<ResumeProject> list, String resumeId, String resumeLanguage, ResumeProjectExpActivity resumeProjectContext) {
        this.context = context;
        this.list = list;
        this.resumeId = resumeId;
        this.resumeLanguage = resumeLanguage;
        this.resumeProjectContext = resumeProjectContext;
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
        viewHolder.tv_resume_item_2.setText(list.get(groupPosition).getProjectname());
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
        final ItemViewHolder ivHolder;
        if (convertView == null) {
            ivHolder = new ItemViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_resume_explv_projectexp, null);
            ivHolder.et_resume_item_projectexp_describe = (EditText) convertView.findViewById(R.id.et_resume_item_projectexp_describe);
            ivHolder.et_resume_item_projectexp_post = (EditText) convertView.findViewById(R.id.et_resume_item_projectexp_post);
            ivHolder.et_resume_item_projectexp_duty = (EditText) convertView.findViewById(R.id.et_resume_item_projectexp_duty);
            ivHolder.tv_resume_item_projectexp_endtime = (TextView) convertView.findViewById(R.id.tv_resume_item_projectexp_endtime);
            ivHolder.tv_resume_item_projectexp_starttime = (TextView) convertView.findViewById(R.id.tv_resume_item_projectexp_starttime);
            ivHolder.et_resume_item_projectexp_projectname = (EditText) convertView.findViewById(R.id.et_resume_item_projectexp_projectame);
            ivHolder.rl_resume_item_projectexp_add = (RelativeLayout) convertView.findViewById(R.id.rl_resume_item_projectexp_add);
            ivHolder.tv_resume_item_projectexp_delete = (TextView) convertView.findViewById(R.id.tv_resume_item_projectexp_delete);

            convertView.setTag(ivHolder);
        } else {
            ivHolder = (ItemViewHolder) convertView.getTag();
        }
        ivHolder.et_resume_item_projectexp_projectname.setText(list.get(groupPosition).getProjectname());
        String startTime = list.get(groupPosition).getFromyear() + "-" + list.get(groupPosition).getFrommonth();
        String endTime = list.get(groupPosition).getToyear() + "-" + list.get(groupPosition).getTomonth();
        if ("0-0".equals(startTime)) {
            startTime = "至今";
        }
        if ("0-0".equals(endTime)) {
            endTime = "至今";
        }
        ivHolder.tv_resume_item_projectexp_starttime.setText(startTime);
        ivHolder.tv_resume_item_projectexp_endtime.setText(endTime);
        ivHolder.et_resume_item_projectexp_post.setText(list.get(groupPosition).getPosition());
        ivHolder.et_resume_item_projectexp_describe.setText(list.get(groupPosition).getProjectdesc());
        ivHolder.et_resume_item_projectexp_duty.setText(list.get(groupPosition).getResponsibility());
        ivHolder.tv_resume_item_projectexp_starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataPickerDialog.showDialog(context, ivHolder.tv_resume_item_projectexp_starttime, 2);
            }
        });
        ivHolder.tv_resume_item_projectexp_endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataPickerDialog.showDialog(context, ivHolder.tv_resume_item_projectexp_endtime, 2);
            }
        });
        ivHolder.rl_resume_item_projectexp_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData(groupPosition, ivHolder);
            }
        });
        ivHolder.tv_resume_item_projectexp_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteConfirm(groupPosition);
            }
        });
        return convertView;
    }

    private void saveData(int position, ItemViewHolder ivHolder) {
        ResumeProject resumeProject = list.get(position);
        if (ivHolder.et_resume_item_projectexp_projectname.getText()
                .toString().trim().length() == 0) {
            Toast.makeText(context, "请输入项目名称", Toast.LENGTH_LONG).show();
            return;
        }
        if (!ivHolder.tv_resume_item_projectexp_starttime.getText()
                .toString().contains("-")
                && !"至今".equals(ivHolder.tv_resume_item_projectexp_starttime
                .getText().toString())) {
            Toast.makeText(context, "请输入开始时间", Toast.LENGTH_LONG).show();
            return;
        }
        if (!ivHolder.tv_resume_item_projectexp_endtime.getText()
                .toString().contains("-")
                && !"至今".equals(ivHolder.tv_resume_item_projectexp_endtime
                .getText().toString())) {
            Toast.makeText(context, "请输入结束时间", Toast.LENGTH_LONG).show();
            return;
        }

        if (ivHolder.et_resume_item_projectexp_post.getText()
                .toString().trim().length() == 0) {
            Toast.makeText(context, "请输入项目职务", Toast.LENGTH_LONG).show();
            return;
        }
        if (ivHolder.et_resume_item_projectexp_describe.getText()
                .toString().trim().length() == 0) {
            Toast.makeText(context, "请输入项目描述", Toast.LENGTH_LONG).show();
            return;
        }
        if (ivHolder.et_resume_item_projectexp_duty.getText()
                .toString().trim().length() == 0) {
            Toast.makeText(context, "请输入项目职责", Toast.LENGTH_LONG).show();
            return;
        }


        String starttimeString = ivHolder.tv_resume_item_projectexp_starttime
                .getText().toString().trim();
        String[] starttimeStrings;
        if ("至今".equals(starttimeString)) {
            starttimeStrings = new String[]{"0", "0"};
        } else {
            starttimeStrings = starttimeString.split("-");
        }
        String endtimeString = ivHolder.tv_resume_item_projectexp_endtime
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
        String companynameString = ivHolder.et_resume_item_projectexp_projectname
                .getText().toString().trim();
        // 所任职位
        String positionString = ivHolder.et_resume_item_projectexp_post
                .getText().toString().trim();
        // 职责描述
        String projectDes = ivHolder.et_resume_item_projectexp_describe
                .getText().toString().trim();
        String responsibilityString = ivHolder.et_resume_item_projectexp_duty
                .getText().toString().trim();
        resumeProject.setFromyear(starttimeStrings[0]);
        resumeProject.setFrommonth(starttimeStrings[1]);
        resumeProject.setToyear(endtimeStrings[0]);
        resumeProject.setTomonth(endtimeStrings[1]);
        resumeProject.setProjectname(companynameString);
        resumeProject.setPosition(positionString);
        resumeProject.setResponsibility(responsibilityString);
        resumeProject.setProjectdesc(projectDes);
        resumeProject.setResume_id(resumeId);
        resumeProject.setResume_language(resumeLanguage);
        if (resumeProject.getId() == -1) {// 新建后保存

            ArrayList<ResumeProject> arrayList = new ArrayList<ResumeProject>();
            arrayList.add(resumeProject);
            int resultInsert = dbOperator
                    .Insert_Resumeitem(arrayList);
            if (resultInsert > 0) {
                ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator,
                        resumeId, resumeLanguage);
                resumeProjectContext.refresh();
//                MyResumeActivity.myResumeActivity.shouldUpdate = true;
                resumeProjectContext.modification = true;
                resumeProjectContext.isAdd = true;

                Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "添加失败", Toast.LENGTH_SHORT).show();
            }
        } else {// 修改后保存
            // 更新到数据库
            boolean resultUpdate = dbOperator
                    .update_Resumeitem(resumeProject);
            if (resultUpdate) {
                ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator, resumeId, resumeLanguage);
                resumeProjectContext.refresh();
//                MyResumeActivity.myResumeActivity.shouldUpdate = true;
                resumeProjectContext.modification = true;
                resumeProjectContext.isAdd = true;
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
        TextView tv_resume_item_projectexp_starttime, tv_resume_item_projectexp_endtime, tv_resume_item_projectexp_delete;
        EditText et_resume_item_projectexp_duty, et_resume_item_projectexp_post, et_resume_item_projectexp_describe, et_resume_item_projectexp_projectname;
        RelativeLayout rl_resume_item_projectexp_add;
    }

    private void deleteConfirm(final int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
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
        dialog.show();
    }

    /**
     * 删除数据
     */
    private void deleteData(int id) {
        boolean delResule = dbOperator.Delete_Data("ResumeProject", id);
        if (delResule) {
            ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator, resumeId, resumeLanguage);
            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < list.size(); i++) {
                if (ResumeProjectExpActivity.resumeProjectExpActivity.elv_resume_projectexp.isGroupExpanded(i)) {
                    ResumeProjectExpActivity.resumeProjectExpActivity.elv_resume_projectexp.collapseGroup(i);
                }
            }
            resumeProjectContext.isAdd = true;
            resumeProjectContext.modification = true;
//            MyResumeActivity.myResumeActivity.shouldUpdate = true;
            // 刷新简历完整度
        } else {
            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
        }
    }
}
