package com.hr.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.activity.MyResumeActivity;
import com.hr.ui.activity.ResumeLanguageInfoActivity;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeLanguageLevel;
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
 * <p>
 * 语言能力 Adapter
 */
public class ResumeLanguageExpLVAdapter extends BaseExpandableListAdapter {
    private Context context;
    private static ArrayList<ResumeLanguageLevel> list;
    private String resumeId, resumeLanguage;
    private DAO_DBOperator dbOperator;
    private ResumeLanguageInfoActivity resumeLanguageContext;

    public ResumeLanguageExpLVAdapter(Context context, ArrayList<ResumeLanguageLevel> list, String resumeId, String resumeLanguage, ResumeLanguageInfoActivity resumeLanguageContext) {
        this.context = context;
        this.list = list;
        this.resumeId = resumeId;
        this.resumeLanguage = resumeLanguage;
        this.resumeLanguageContext = resumeLanguageContext;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_resume_explistview2, null);
            viewHolder.tv_resume_item_21 = (TextView) convertView.findViewById(R.id.tv_resume_item_21);
            viewHolder.tv_resume_item_22 = (TextView) convertView.findViewById(R.id.tv_resume_item_22);
            viewHolder.tv_resume_item_23 = (TextView) convertView.findViewById(R.id.tv_resume_item_23);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_resume_item_21.setText(ResumeInfoIDToString.getLanguageTpye(context, list.get(groupPosition).getLangname(), true));
        viewHolder.tv_resume_item_22.setText(ResumeInfoIDToString.getLanguageSpeakLevel(context, list.get(groupPosition).getSpeak_level(), true));
        viewHolder.tv_resume_item_23.setText(ResumeInfoIDToString.getLanguageReadLevel(context, list.get(groupPosition).getRead_level(), true));
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ItemViewHolder ivHolder;
        if (convertView == null) {
            ivHolder = new ItemViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_resume_explv_language, null);
            ivHolder.sp_resume_language_langname = (IdSpineer) convertView.findViewById(R.id.sp_resume_language_langname);
            ivHolder.sp_resume_language_read_level = (IdSpineer) convertView.findViewById(R.id.sp_resume_language_read_level);
            ivHolder.sp_resume_language_speak_level = (IdSpineer) convertView.findViewById(R.id.sp_resume_language_speak_level);
            ivHolder.rl_resume_item_language_add = (RelativeLayout) convertView.findViewById(R.id.rl_resume_item_language_add);
            ivHolder.tv_resume_item_language_delete = (TextView) convertView.findViewById(R.id.tv_resume_item_language_delete);

            convertView.setTag(ivHolder);
        } else {
            ivHolder = (ItemViewHolder) convertView.getTag();
        }
        ivHolder.sp_resume_language_langname.setAdapter(new SpinnerAdapter(context, android.R.layout.simple_spinner_item, context.getResources().getStringArray(R.array.array_language_type_zh)));
        ivHolder.sp_resume_language_langname.setIds(context.getResources().getStringArray(R.array.array_language_type_ids));
        ivHolder.sp_resume_language_langname
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        try {
                            if (ivHolder.sp_resume_language_langname.idStrings != null) {
                                ivHolder.sp_resume_language_langname.idString = ivHolder.sp_resume_language_langname.idStrings[arg2];
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
        ivHolder.sp_resume_language_read_level.setAdapter(new SpinnerAdapter(context, android.R.layout.simple_spinner_item, context.getResources().getStringArray(R.array.array_language_readlevel_zh)));
        ivHolder.sp_resume_language_read_level.setIds(context.getResources()
                .getStringArray(R.array.array_language_readlevel_ids));
        ivHolder.sp_resume_language_read_level
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        try {
                            if (ivHolder.sp_resume_language_read_level.idStrings != null) {
                                ivHolder.sp_resume_language_read_level.idString = ivHolder.sp_resume_language_read_level.idStrings[arg2];
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
        ivHolder.sp_resume_language_speak_level.setAdapter(new SpinnerAdapter(context, android.R.layout.simple_spinner_item, context.getResources().getStringArray(R.array.array_language_readlevel_zh)));
        ivHolder.sp_resume_language_speak_level.setIds(context.getResources()
                .getStringArray(R.array.array_language_speaklevel_ids));
        ivHolder.sp_resume_language_speak_level
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        try {
                            if (ivHolder.sp_resume_language_speak_level.idStrings != null) {
                                ivHolder.sp_resume_language_speak_level.idString = ivHolder.sp_resume_language_speak_level.idStrings[arg2];
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
        ivHolder.sp_resume_language_speak_level.setAdapter(new SpinnerAdapter(context, android.R.layout.simple_spinner_item, context.getResources().getStringArray(R.array.array_language_speaklevel_zh)));
        ivHolder.sp_resume_language_langname.setSelectedItem(list.get(groupPosition).getLangname());
        ivHolder.sp_resume_language_read_level.setSelectedItem(list.get(groupPosition).getRead_level());
        ivHolder.sp_resume_language_speak_level.setSelectedItem(list.get(groupPosition).getSpeak_level());
        ivHolder.rl_resume_item_language_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData(groupPosition, ivHolder);
            }
        });
        ivHolder.tv_resume_item_language_delete.setOnClickListener(new View.OnClickListener() {
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
        ResumeLanguageLevel resumeLanguageLevel = list.get(position);
        String languageNameIdString = ivHolder.sp_resume_language_langname.getSelectedId();
        String readlevelIdString = ivHolder.sp_resume_language_read_level.getSelectedId();
        String speaklevelString = ivHolder.sp_resume_language_speak_level.getSelectedId();
        resumeLanguageLevel.setLangname(languageNameIdString);
        resumeLanguageLevel.setUser_id(MyUtils.userID);
        resumeLanguageLevel.setRead_level(readlevelIdString);
        resumeLanguageLevel.setSpeak_level(speaklevelString);
        if (resumeLanguageLevel.getId() == -1) {// 新建后保存
            ArrayList<ResumeLanguageLevel> arrayList = new ArrayList<ResumeLanguageLevel>();
            arrayList.add(resumeLanguageLevel);
            int resultInsert = dbOperator.Insert_ResumeLanguageLevel(arrayList);
            if (resultInsert != 0) {
                ResumeIsUpdateOperator.setBaseInfoIsUpdate(context, dbOperator, resumeLanguage);
                resumeLanguageContext.refresh();
                resumeLanguageContext.isAdd = true;
                resumeLanguageContext.modification = true;
//                MyResumeActivity.myResumeActivity.shouldUpdate = true;
                Toast.makeText(context, "添加成功", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "添加失败", Toast.LENGTH_SHORT).show();
            }
        } else {// 修改后保存
            // 更新到数据库
            boolean resultUpdate = dbOperator.update_ResumeLanguageLevel(resumeLanguageLevel);
            if (resultUpdate) {
                ResumeIsUpdateOperator.setBaseInfoIsUpdate(context, dbOperator, resumeLanguage);
                resumeLanguageContext.refresh();
                resumeLanguageContext.isAdd = true;
                resumeLanguageContext.modification = true;
//                MyResumeActivity.myResumeActivity.shouldUpdate = true;
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
        TextView tv_resume_item_21, tv_resume_item_22, tv_resume_item_23;
    }

    class ItemViewHolder {
        TextView tv_resume_item_language_delete;
        IdSpineer sp_resume_language_langname, sp_resume_language_speak_level, sp_resume_language_read_level;
        RelativeLayout rl_resume_item_language_add;
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
        boolean delResule = dbOperator.Delete_Data("ResumeLanguageLevel", id);
        if (delResule) {
            ResumeIsUpdateOperator.setBaseInfoIsUpdate(context, dbOperator, resumeLanguage);
            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < list.size(); i++) {
                if (ResumeLanguageInfoActivity.resumeLanguageInfoActivity.elv_resume_language.isGroupExpanded(i)) {
                    ResumeLanguageInfoActivity.resumeLanguageInfoActivity.elv_resume_language.collapseGroup(i);
                }
            }
            resumeLanguageContext.isAdd = true;
            resumeLanguageContext.modification = true;
//            MyResumeActivity.myResumeActivity.shouldUpdate = true;
            // 刷新简历完整度
        } else {
            Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
        }
    }


//    public void saveAll() {
//        for (int i = 0; i < list.size(); i++) {
//            ResumeLanguageLevel resumeLanguageLevel = list.get(i);
//            String position = (i + 1) + "";
//            resumeLanguageLevel.setLangname(position);
//            resumeLanguageLevel.setUser_id(MyUtils.userID);
//            resumeLanguageLevel.setRead_level(position);
//            resumeLanguageLevel.setSpeak_level(position);
//            if (resumeLanguageLevel.getId() == -1) {// 新建后保存
//                ArrayList<ResumeLanguageLevel> arrayList = new ArrayList<ResumeLanguageLevel>();
//                arrayList.add(resumeLanguageLevel);
//                int resultInsert = dbOperator.Insert_ResumeLanguageLevel(arrayList);
//                if (resultInsert != 0) {
//                    ResumeIsUpdateOperator.setBaseInfoIsUpdate(context, dbOperator, resumeLanguage);
////                resumeLanguageContext.refresh();
//                    MyResumeActivity.myResumeActivity.shouldUpdate = true;
////                    Toast.makeText(context, "添加成功", Toast.LENGTH_LONG).show();
//                } else {
////                    Toast.makeText(context, "添加失败", Toast.LENGTH_SHORT).show();
//                }
//            } else {// 修改后保存
//                // 更新到数据库
//                boolean resultUpdate = dbOperator.update_ResumeLanguageLevel(resumeLanguageLevel);
//                if (resultUpdate) {
//                    ResumeIsUpdateOperator.setBaseInfoIsUpdate(context, dbOperator, resumeLanguage);
////                resumeLanguageContext.refresh();
//                    MyResumeActivity.myResumeActivity.shouldUpdate = true;
////                    Toast.makeText(context, "添加成功", Toast.LENGTH_LONG).show();
//                } else {
////                    Toast.makeText(context, "添加失败", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//}
}
