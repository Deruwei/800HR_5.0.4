package com.hr.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.ResumeSkillExpLVAdapter;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeSkill;
import com.hr.ui.utils.MyUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;

public class ResumeSkillInfoActivity extends BaseResumeActivity implements View.OnClickListener {
    private static final String TAG = "ResumeskillinfoActivity";
    private Context mContext = ResumeSkillInfoActivity.this;
    public ExpandableListView elv_resume_skillinfo;
    private ImageView iv_resume_skillinfo_back;
    private TextView tv_resume_item_addeud, tv_resume_skillinfo_save;

    private DAO_DBOperator dbOperator;
    private boolean isCHS;
    private String resumeIdString, resumeLanguageString;
    private ResumeSkill[] resumeSkills;
    private ArrayList<ResumeSkill> listResumeSkillInfo;
    private ResumeSkillExpLVAdapter adapter;
    public static ResumeSkillInfoActivity resumeSkillInfoActivity;
    private View footView;

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
        resumeSkillInfoActivity = ResumeSkillInfoActivity.this;
        setContentView(R.layout.activity_resume_skill_info);
        initData();
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_resume_skillinfo_back:
                showSaveDialog();
                break;
            case R.id.tv_resume_skillinfo_save:
                canSave = true;
                for (int i = 0; i < listResumeSkillInfo.size(); i++) {
                    if (elv_resume_skillinfo.isGroupExpanded(i)) {
                        canSave = false;
                    }
                }
                if (MyUtils.ableInternet) {
                    if (canSave) {
                        uploadData(resumeIdString);
                    } else {
                        Toast.makeText(ResumeSkillInfoActivity.this, "请先保存后再上传", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ResumeSkillInfoActivity.this, "网络异常，保存失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void refresh() {
        Intent intent = new Intent(mContext, ResumeSkillInfoActivity.class);
        if (resumeIdString != null) {
            intent.putExtra("resumeId", resumeIdString);
            intent.putExtra("resumeLanguage", "zh");
            intent.putExtra("isCHS", true);
        }
        startActivity(intent);
        finish();
    }

    private void initView() {
        footView = LayoutInflater.from(mContext).inflate(R.layout.item_resume_foot, null);
        elv_resume_skillinfo = (ExpandableListView) findViewById(R.id.elv_resume_skillinfo);
        iv_resume_skillinfo_back = (ImageView) findViewById(R.id.iv_resume_skillinfo_back);
        tv_resume_skillinfo_save = (TextView) findViewById(R.id.tv_resume_skillinfo_save);

        tv_resume_item_addeud = (TextView) footView.findViewById(R.id.tv_resume_item_addeud);
        iv_resume_skillinfo_back.setOnClickListener(this);
        tv_resume_skillinfo_save.setOnClickListener(this);

        elv_resume_skillinfo.addFooterView(footView);
        elv_resume_skillinfo.setAdapter(adapter);

        tv_resume_item_addeud.setText("+添加技能");
        tv_resume_item_addeud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listResumeSkillInfo.add(new ResumeSkill());
                adapter.notifyDataSetChanged();
            }
        });

        elv_resume_skillinfo.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < adapter.getGroupCount(); i++) {
                    if (groupPosition != i) {
                        elv_resume_skillinfo.collapseGroup(i);
                    }
                }
            }
        });
    }

    private void initData() {
        dbOperator = new DAO_DBOperator(mContext);
        resumeIdString = getIntent().getStringExtra("resumeId");
        resumeLanguageString = getIntent().getStringExtra("resumeLanguage");
        isCHS = getIntent().getBooleanExtra("isCHS", true);
        resumeSkills = dbOperator.query_ResumeSkill(resumeIdString,
                resumeLanguageString);
        listResumeSkillInfo = new ArrayList<>();
        for (ResumeSkill resumeskillinfo : resumeSkills) {
            listResumeSkillInfo.add(resumeskillinfo);
        }
        adapter = new ResumeSkillExpLVAdapter(mContext, listResumeSkillInfo, resumeIdString, resumeLanguageString, ResumeSkillInfoActivity.this);
    }
}
