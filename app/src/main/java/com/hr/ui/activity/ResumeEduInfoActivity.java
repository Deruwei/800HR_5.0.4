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
import com.hr.ui.adapter.ResumeEduExpLVAdapter;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeEducation;
import com.hr.ui.utils.MyUtils;

import java.util.ArrayList;

/**
 * 教育背景
 *
 * @author Colin
 */
public class ResumeEduInfoActivity extends BaseResumeActivity implements View.OnClickListener {
    private static final String TAG = "ResumeEduInfoActivity";
    private Context mContext = ResumeEduInfoActivity.this;
    private View footView;
    public ExpandableListView elv_resume_eduinfo;
    private ImageView iv_resume_eduinfo_back;
    private DAO_DBOperator dbOperator;
    private boolean isCHS;
    private String resumeIdString, resumeLanguageString;
    private TextView tv_resume_item_addeud, tv_resume_eduinfo_save;
    /**
     * 教育背景
     */
    private ResumeEducation[] resumeEducations;
    private ResumeEduExpLVAdapter adapter;
    private ArrayList<ResumeEducation> listResumeEdu;

    public static ResumeEduInfoActivity resumeEduInfoActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_edu_info);
        resumeEduInfoActivity = ResumeEduInfoActivity.this;
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
        adapter = new ResumeEduExpLVAdapter(mContext, listResumeEdu, resumeIdString, resumeLanguageString, ResumeEduInfoActivity.this);
    }

    private void initView() {
        tv_resume_eduinfo_save = (TextView) findViewById(R.id.tv_resume_eduinfo_save);
        footView = LayoutInflater.from(mContext).inflate(R.layout.item_resume_foot, null);
        elv_resume_eduinfo = (ExpandableListView) findViewById(R.id.elv_resume_eduinfo);
        iv_resume_eduinfo_back = (ImageView) findViewById(R.id.iv_resume_eduinfo_back);
        tv_resume_item_addeud = (TextView) footView.findViewById(R.id.tv_resume_item_addeud);

        tv_resume_item_addeud.setOnClickListener(this);
        iv_resume_eduinfo_back.setOnClickListener(this);
        tv_resume_eduinfo_save.setOnClickListener(this);

        elv_resume_eduinfo.addFooterView(footView);
        elv_resume_eduinfo.setAdapter(adapter);


        tv_resume_item_addeud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listResumeEdu.add(new ResumeEducation());
                adapter.notifyDataSetChanged();
            }
        });

        elv_resume_eduinfo.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < adapter.getGroupCount(); i++) {
                    if (groupPosition != i) {
                        elv_resume_eduinfo.collapseGroup(i);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_resume_eduinfo_back:
                showSaveDialog();
                break;
            case R.id.tv_resume_eduinfo_save:
                canSave = true;
                for (int i = 0; i < listResumeEdu.size(); i++) {
                    if (elv_resume_eduinfo.isGroupExpanded(i)) {
                        canSave = false;
                    }
                }
                if (MyUtils.ableInternet) {
                    if (canSave) {
                        uploadData(resumeIdString);
                    } else {
                        Toast.makeText(ResumeEduInfoActivity.this, "请先保存后再上传", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ResumeEduInfoActivity.this, "网络异常，保存失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void refresh() {
        Intent intent = new Intent(mContext, ResumeEduInfoActivity.class);

        if (resumeIdString != null) {
            intent.putExtra("resumeId", resumeIdString);
            intent.putExtra("resumeLanguage", "zh");
            intent.putExtra("isCHS", true);
        }
        startActivity(intent);
        finish();
    }

}
