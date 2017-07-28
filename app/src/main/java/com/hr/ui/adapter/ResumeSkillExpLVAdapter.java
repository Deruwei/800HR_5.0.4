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
import com.hr.ui.activity.ResumeSkillInfoActivity;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeSkill;
import com.hr.ui.utils.MyUtils;
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
 * 专业技能 Adapter
 */
public class ResumeSkillExpLVAdapter extends BaseExpandableListAdapter {

    private Context context;
    private static ArrayList<ResumeSkill> list;
    private String resumeId, resumeLanguage;
    private DAO_DBOperator dbOperator;
    private ResumeSkillInfoActivity resumeSkillContext;

    public ResumeSkillExpLVAdapter(Context context, ArrayList<ResumeSkill> list, String resumeId, String resumeLanguage, ResumeSkillInfoActivity resumeSkillContext) {
        this.context = context;
        this.list = list;
        this.resumeId = resumeId;
        this.resumeLanguage = resumeLanguage;
        this.resumeSkillContext = resumeSkillContext;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_resume_explistview3, null);
            viewHolder.tv_resume_item_31 = (TextView) convertView.findViewById(R.id.tv_resume_item_31);
            viewHolder.tv_resume_item_32 = (TextView) convertView.findViewById(R.id.tv_resume_item_32);
            viewHolder.tv_resume_item_33 = (TextView) convertView.findViewById(R.id.tv_resume_item_33);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_resume_item_31.setText(list.get(groupPosition).getSkilltitle());
        viewHolder.tv_resume_item_32.setText(list.get(groupPosition).getUsetime());
        viewHolder.tv_resume_item_33.setText(ResumeInfoIDToString.getSkillLevel(context, list.get(groupPosition).getAbility(), true));
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ItemViewHolder ivHolder;
        if (convertView == null) {
            ivHolder = new ItemViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_resume_explv_skill, null);
            ivHolder.et_resume_item_skill_skillname = (EditText) convertView.findViewById(R.id.et_resume_item_skill_skillname);
            ivHolder.et_resume_item_skill_skilltime = (EditText) convertView.findViewById(R.id.et_resume_item_skill_skilltime);
            ivHolder.sp_resume_skill_level = (IdSpineer) convertView.findViewById(R.id.sp_resume_skill_level);
            ivHolder.rl_resume_item_skill_add = (RelativeLayout) convertView.findViewById(R.id.rl_resume_item_skill_add);
            ivHolder.tv_resume_item_skill_delete = (TextView) convertView.findViewById(R.id.tv_resume_item_skill_delete);

            convertView.setTag(ivHolder);
        } else {
            ivHolder = (ItemViewHolder) convertView.getTag();
        }

        ivHolder.sp_resume_skill_level.setAdapter(new SpinnerAdapter(context, android.R.layout.simple_spinner_item, context.getResources().getStringArray(R.array.array_skilllevel_zh)));
        ivHolder.sp_resume_skill_level.setIds(context.getResources().getStringArray(R.array.array_skilllevel_ids));
        ivHolder.sp_resume_skill_level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                try {
                    if (ivHolder.sp_resume_skill_level.idStrings != null) {
                        ivHolder.sp_resume_skill_level.idString = ivHolder.sp_resume_skill_level.idStrings[arg2];
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        ivHolder.et_resume_item_skill_skillname.setText(list.get(groupPosition).getSkilltitle());
        ivHolder.et_resume_item_skill_skilltime.setText(list.get(groupPosition).getUsetime());
        ivHolder.sp_resume_skill_level.setSelectedItem(list.get(groupPosition).getAbility());
        ivHolder.rl_resume_item_skill_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData(groupPosition, ivHolder);
            }
        });
        ivHolder.tv_resume_item_skill_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteConfirm(groupPosition);
            }
        });
        return convertView;
    }

    /*
     * 保存数据
     */
    private void saveData(int position, ItemViewHolder ivHolder) {
        ResumeSkill resumeSkill = list.get(position);

        if (ivHolder.et_resume_item_skill_skillname.getText()
                .toString().trim().length() == 0) {
            Toast.makeText(context, "请输入技能名称", Toast.LENGTH_LONG).show();
            return;
        }
        if (ivHolder.et_resume_item_skill_skilltime.getText()
                .toString().trim().length() == 0) {
            Toast.makeText(context, "请输入使用时间", Toast.LENGTH_LONG).show();
            return;
        }

        String skillnameString = ivHolder.et_resume_item_skill_skillname
                .getText().toString().trim();
        String skilltimeString = ivHolder.et_resume_item_skill_skilltime
                .getText().toString().trim();
        String idsString = ivHolder.sp_resume_skill_level.getSelectedId();
        resumeSkill.setSkilltitle(skillnameString);
        resumeSkill.setUsetime(skilltimeString);
        resumeSkill.setAbility(idsString);
        resumeSkill.setResume_id(resumeId);
        resumeSkill.setResume_language(resumeLanguage);
        resumeSkill.setUser_id(MyUtils.userID);
        if (resumeSkill.getId() == -1) {// 新建后保存
            ArrayList<ResumeSkill> arrayList = new ArrayList<ResumeSkill>();
            arrayList.add(resumeSkill);
            int resultInsert = dbOperator.Insert_ResumeSkill(arrayList);
            if (resultInsert > 0) {
                ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator,
                        resumeId, resumeLanguage);
                resumeSkillContext.refresh();
//                MyResumeActivity.myResumeActivity.shouldUpdate = true;
                resumeSkillContext.isAdd = true;
                resumeSkillContext.modification = true;
                Toast.makeText(context, "添加成功", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "添加失败", Toast.LENGTH_SHORT).show();
            }
        } else {// 修改后保存
            // 更新到数据库
            boolean resultUpdate = dbOperator.update_ResumeSkill(resumeSkill);
            if (resultUpdate) {
                ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator,
                        resumeId, resumeLanguage);
                resumeSkillContext.refresh();
//                MyResumeActivity.myResumeActivity.shouldUpdate = true;
                resumeSkillContext.isAdd = true;
                resumeSkillContext.modification = true;
                Toast.makeText(context, "修改成功", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "修改失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return false;
    }

    class ViewHolder {
        TextView tv_resume_item_31, tv_resume_item_32, tv_resume_item_33;
    }

    class ItemViewHolder {
        TextView tv_resume_item_skill_delete;
        EditText et_resume_item_skill_skillname, et_resume_item_skill_skilltime;
        IdSpineer sp_resume_skill_level;
        RelativeLayout rl_resume_item_skill_add;
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
        boolean delResule = dbOperator.Delete_Data("ResumeSkill", id);
        if (delResule) {
            ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator, resumeId, resumeLanguage);
            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < list.size(); i++) {
                if (ResumeSkillInfoActivity.resumeSkillInfoActivity.elv_resume_skillinfo.isGroupExpanded(i)) {
                    ResumeSkillInfoActivity.resumeSkillInfoActivity.elv_resume_skillinfo.collapseGroup(i);
                }
            }
            resumeSkillContext.isAdd = true;
            resumeSkillContext.modification = true;
//            MyResumeActivity.myResumeActivity.shouldUpdate = true;
            // 刷新简历完整度
        } else {
            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
        }
    }
}
