package com.hr.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.CreateResumeEduExpLVAdapter;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeEducation;

import java.util.ArrayList;

/**
 * 教育背景
 *
 * @author Colin
 */
public class CreateResumeEduInfoActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "CreateResumeEduInfoActivity";
    private Context mContext = CreateResumeEduInfoActivity.this;
    private View footView;
    private ExpandableListView elv_create_resume_eduinfo;
    private ImageView iv_create_resume_eduinfo_back;
    private DAO_DBOperator dbOperator;
    private boolean isCHS;
    private String resumeIdString, resumeLanguageString;
    private TextView tv_createresume_item_addeud;
    private RelativeLayout rl_createresume_info_save;
    /**
     * 教育背景
     */
    private ResumeEducation[] resumeEducations, resumeEducations2;
    private CreateResumeEduExpLVAdapter adapter;
    private ArrayList<ResumeEducation> listResumeEdu, listResumeEdu2;

    public CreateResumeEduInfoActivity CreateResumeEduInfoActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_resume_edu_info);
        CreateResumeEduInfoActivity = CreateResumeEduInfoActivity.this;
        initData();
        initView();
    }
    private void initData() {
        dbOperator = new DAO_DBOperator(mContext);
        resumeIdString = getIntent().getStringExtra("resumeId");
        resumeLanguageString = getIntent().getStringExtra("resumeLanguage");
        isCHS = getIntent().getBooleanExtra("isCHS", true);
        resumeEducations = dbOperator.query_ResumeEducation(resumeIdString,
                resumeLanguageString);
        listResumeEdu = new ArrayList<>();
        for (ResumeEducation resumeEdu : resumeEducations) {
            listResumeEdu.add(resumeEdu);
        }
        adapter = new CreateResumeEduExpLVAdapter(mContext, listResumeEdu, resumeIdString, resumeLanguageString, CreateResumeEduInfoActivity.this);
    }

    private void initView() {
        footView = LayoutInflater.from(mContext).inflate(R.layout.item_createresume_foot, null);
        elv_create_resume_eduinfo = (ExpandableListView) findViewById(R.id.elv_create_resume_eduinfo);
        tv_createresume_item_addeud = (TextView) footView.findViewById(R.id.tv_createresume_item_addeud);
        rl_createresume_info_save = (RelativeLayout) footView.findViewById(R.id.rl_createresume_info_save);
        iv_create_resume_eduinfo_back = (ImageView) findViewById(R.id.iv_create_resume_eduinfo_back);

        tv_createresume_item_addeud.setOnClickListener(this);
        iv_create_resume_eduinfo_back.setOnClickListener(this);
        elv_create_resume_eduinfo.addFooterView(footView);
        elv_create_resume_eduinfo.setAdapter(adapter);
        tv_createresume_item_addeud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listResumeEdu.add(new ResumeEducation());
                adapter.notifyDataSetChanged();
            }
        });
        rl_createresume_info_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumeEducations2 = dbOperator.query_ResumeEducation(resumeIdString,
                        resumeLanguageString);
                listResumeEdu2 = new ArrayList<>();
                for (ResumeEducation resumeEdu : resumeEducations2) {
                    listResumeEdu2.add(resumeEdu);
                }
                if (listResumeEdu2.size() > 0) {
                    Intent intent = new Intent(mContext, CreateResumeJobExpActivity.class);
                    if (resumeIdString != null) {
                        intent.putExtra("resumeId", resumeIdString);
                        intent.putExtra("resumeLanguage", "zh");
                        intent.putExtra("isCHS", true);
                    }
                    startActivity(intent);
//                    finish();
                } else {
                    Toast.makeText(mContext, "请填写完整", Toast.LENGTH_SHORT).show();
                }
            }
        });
        elv_create_resume_eduinfo.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < adapter.getGroupCount(); i++) {
                    if (groupPosition != i) {
                        elv_create_resume_eduinfo.collapseGroup(i);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_create_resume_eduinfo_back:
                finish();
                break;
        }
    }

    public void refresh() {
        Intent intent = new Intent(mContext, CreateResumeEduInfoActivity.class);
        if (resumeIdString != null) {
            intent.putExtra("resumeId", resumeIdString);
            intent.putExtra("resumeLanguage", "zh");
            intent.putExtra("isCHS", true);
        }
        startActivity(intent);
//        finish();
    }
}
