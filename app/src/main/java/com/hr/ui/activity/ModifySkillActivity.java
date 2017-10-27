package com.hr.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.ResumeSkillExpLVAdapter;
import com.hr.ui.adapter.SpinnerAdapter;
import com.hr.ui.bean.SelectBean;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeLanguageLevel;
import com.hr.ui.model.ResumeSkill;
import com.hr.ui.utils.GetResumeArrayList;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.ResumeIsUpdateOperator;
import com.hr.ui.utils.netutils.NetRequest;
import com.hr.ui.view.custom.BeautifulDialog;
import com.hr.ui.view.custom.CustomDatePicker;
import com.hr.ui.view.custom.IdSpineer;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifySkillActivity extends BaseResumeActivity {
    @Bind(R.id.iv_resume_modifynewskill_back)
    ImageView ivResumeModifynewskillBack;
    @Bind(R.id.et_resume_item_modifyskill_skillname)
    EditText etResumeItemModifyskillSkillname;
    @Bind(R.id.et_resume_item_modifyskill_skilltime)
    EditText etResumeItemModifyskillSkilltime;
    @Bind(R.id.sp_resume_modifyskill_level)
    TextView spResumeModifyskillLevel;
    @Bind(R.id.tv_resume_item_skillmodify_delete)
    TextView tvResumeItemSkillmodifyDelete;
    @Bind(R.id.tv_resume_item_skillmodify_save)
    TextView tvResumeItemSkillmodifySave;
    private Context context = ModifySkillActivity.this;
    private DAO_DBOperator dbOperator;
    private ResumeSkill resumeSkill;
    private String resumeId, resumeLanguage;
    private CustomDatePicker datePickerSkill;
    private List<SelectBean> levelList=new ArrayList<>();
    private String selectLevelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.new_main));// 通知栏所需颜色
        }
        setContentView(R.layout.activity_modify_skill);
        ButterKnife.bind(this);
        initData();
        initDialog();
    }
    private void initData() {
        levelList= GetResumeArrayList.getLevelFromArray(this);
        dbOperator = new DAO_DBOperator(context);
        resumeSkill = (ResumeSkill) getIntent().getSerializableExtra("resumeSkill");
        resumeId = getIntent().getStringExtra("resumeId");
        resumeLanguage = getIntent().getStringExtra("resumeLanguage");
        if(getIntent().getStringExtra("isAdd").equals("1")){
            tvResumeItemSkillmodifyDelete.setVisibility(View.GONE);
        }
        etResumeItemModifyskillSkillname.setText(resumeSkill.getSkilltitle());
        etResumeItemModifyskillSkilltime.setText(resumeSkill.getUsetime());
        if (resumeSkill.getAbility() != null) {
            String levelId = resumeSkill.getAbility();
            selectLevelId = levelId;
            for (int i = 0; i < levelList.size(); i++) {
                if (levelId.equals(levelList.get(i).getId())) {
                    spResumeModifyskillLevel.setText(levelList.get(i).getName());
                    break;
                }
            }
        }
    }
    private void initDialog() {
        datePickerSkill = new CustomDatePicker(ModifySkillActivity.this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                if ("".equals(time) || time == null) {
                    spResumeModifyskillLevel.setText("请选择");
                } else {
                    spResumeModifyskillLevel.setText(time);
                    for (int i = 0; i < levelList.size(); i++) {
                        if (time.equals(levelList.get(i).getName())) {
                            selectLevelId = levelList.get(i).getId();
                            break;
                        }
                    }
                }
            }
        }, getResources().getStringArray(R.array.array_skilllevel_zh));
    }
    @OnClick({R.id.iv_resume_modifynewskill_back, R.id.tv_resume_item_skillmodify_delete, R.id.tv_resume_item_skillmodify_save,R.id.sp_resume_modifyskill_level})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_resume_modifynewskill_back:
                finish();
                break;
            case R.id.tv_resume_item_skillmodify_delete:
                deleteConfirm();
                break;
            case R.id.tv_resume_item_skillmodify_save:
                saveData();
                break;
            case R.id.sp_resume_modifyskill_level:
                datePickerSkill.show(spResumeModifyskillLevel.getText().toString());
                break;
        }
    }

    /*
    * 保存数据
    */
    private void saveData() {
        MyUtils.canResumeReflesh=true;
        if (etResumeItemModifyskillSkillname.getText()
                .toString().trim().length() == 0) {
            Toast.makeText(context, "请输入技能名称", Toast.LENGTH_LONG).show();
            return;
        }
        if (etResumeItemModifyskillSkilltime.getText()
                .toString().trim().length() == 0) {
            Toast.makeText(context, "请输入使用时间", Toast.LENGTH_LONG).show();
            return;
        }

        String skillnameString = etResumeItemModifyskillSkillname
                .getText().toString().trim();
        String skilltimeString = etResumeItemModifyskillSkilltime
                .getText().toString().trim();
        String idsString = selectLevelId;
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
                ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator, resumeId, resumeLanguage);
                next();
            } else {
                Toast.makeText(context, "编辑失败", Toast.LENGTH_SHORT).show();
            }
        } else {// 修改后保存
            // 更新到数据库
            MyUtils.canResumeReflesh=true;
            boolean resultUpdate = dbOperator.update_ResumeSkill(resumeSkill);
            if (resultUpdate) {
                ResumeIsUpdateOperator.setResumeTitleIsUpdate(context, dbOperator, resumeId, resumeLanguage);
                next();
            } else {
                Toast.makeText(context, "编辑失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void deleteConfirm() {
        BeautifulDialog.Builder dialog = new BeautifulDialog.Builder(context);
        dialog.setTitle("温馨提示");
        dialog.setMessage("您确定要删除吗？");
        dialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        deleteData(resumeSkill.getId());
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
            next();
            // 刷新简历完整度
        } else {
            next();
        }
    }

    private void next() {
        MyUtils.canResumeReflesh=true;
        if (MyUtils.ableInternet) {
            uploadData(resumeId);
        } else {
            Toast.makeText(context, "网络异常，保存失败", Toast.LENGTH_SHORT).show();
        }
    }
}
