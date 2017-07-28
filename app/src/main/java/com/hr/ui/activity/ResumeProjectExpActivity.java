package com.hr.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.ResumeProjectExpAdapter;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeProject;
import com.hr.ui.utils.MyUtils;

import java.util.ArrayList;

public class ResumeProjectExpActivity extends BaseResumeActivity implements View.OnClickListener {

    private static final String TAG = "ResumeProjectExpActivity";
    private Context mContext = ResumeProjectExpActivity.this;
    public ExpandableListView elv_resume_projectexp;
    private ImageView iv_resume_projectexp_back;
    private TextView tv_resume_projectexp_save, tv_resume_item_addeud;

    private DAO_DBOperator dbOperator;
    private boolean isCHS;
    private String resumeIdString, resumeLanguageString;
    private ResumeProject[] resumeProject;
    private ArrayList<ResumeProject> listResumeProjectExp;
    private ResumeProjectExpAdapter adapter;

    private View footView;
    public static ResumeProjectExpActivity resumeProjectExpActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_project_exp);
        resumeProjectExpActivity = ResumeProjectExpActivity.this;
        initData();
        initView();
    }

    private void initView() {
        tv_resume_projectexp_save = (TextView) findViewById(R.id.tv_resume_projectexp_save);
        footView = LayoutInflater.from(mContext).inflate(R.layout.item_resume_foot, null);
        elv_resume_projectexp = (ExpandableListView) findViewById(R.id.elv_resume_projectexp);
        iv_resume_projectexp_back = (ImageView) findViewById(R.id.iv_resume_projectexp_back);

        tv_resume_item_addeud = (TextView) footView.findViewById(R.id.tv_resume_item_addeud);
        iv_resume_projectexp_back.setOnClickListener(this);
        tv_resume_projectexp_save.setOnClickListener(this);

        elv_resume_projectexp.addFooterView(footView);
        elv_resume_projectexp.setAdapter(adapter);

        tv_resume_item_addeud.setText("+添加项目经验");
        tv_resume_item_addeud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listResumeProjectExp.add(new ResumeProject());
                adapter.notifyDataSetChanged();
            }
        });


        elv_resume_projectexp.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < adapter.getGroupCount(); i++) {
                    if (groupPosition != i) {
                        elv_resume_projectexp.collapseGroup(i);
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
        resumeProject = dbOperator.query_Resumeitem(resumeIdString, resumeLanguageString);
        listResumeProjectExp = new ArrayList<>();
        for (ResumeProject resumeProjectExp : resumeProject) {
            listResumeProjectExp.add(resumeProjectExp);
        }
        adapter = new ResumeProjectExpAdapter(mContext, listResumeProjectExp, resumeIdString, resumeLanguageString, ResumeProjectExpActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_resume_projectexp_back:
                showSaveDialog();
                break;
            case R.id.tv_resume_projectexp_save:
                canSave = true;
                for (int i = 0; i < listResumeProjectExp.size(); i++) {
                    if (elv_resume_projectexp.isGroupExpanded(i)) {
                        canSave = false;
                    }
                }
                if (MyUtils.ableInternet) {
                    if (canSave) {
                        uploadData(resumeIdString);
                    } else {
                        Toast.makeText(ResumeProjectExpActivity.this, "请先保存后再上传", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ResumeProjectExpActivity.this, "网络异常，保存失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void refresh() {
        Intent intent = new Intent(mContext, ResumeProjectExpActivity.class);
        if (resumeIdString != null) {
            intent.putExtra("resumeId", resumeIdString);
            intent.putExtra("resumeLanguage", "zh");
            intent.putExtra("isCHS", true);
        }
        startActivity(intent);
        finish();
    }
}
