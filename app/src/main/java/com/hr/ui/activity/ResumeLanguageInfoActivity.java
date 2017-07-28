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
import com.hr.ui.adapter.ResumeLanguageExpLVAdapter;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeLanguageLevel;
import com.hr.ui.utils.MyUtils;

import java.util.ArrayList;

public class ResumeLanguageInfoActivity extends BaseResumeActivity implements View.OnClickListener {
    private static final String TAG = "";
    private Context mContext = ResumeLanguageInfoActivity.this;
    private View footView;
    public ExpandableListView elv_resume_language;
    private ImageView iv_resume_language_back;
    private DAO_DBOperator dbOperator;
    private boolean isCHS;
    private String resumeIdString, resumeLanguageString;
    private TextView tv_resume_item_addeud, tv_resume_language_save;
    /**
     * 教育背景
     */
    private ResumeLanguageLevel[] resumeLanguageLevels;
    private ResumeLanguageExpLVAdapter adapter;
    private ArrayList<ResumeLanguageLevel> listResumeLang;
    public static ResumeLanguageInfoActivity resumeLanguageInfoActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_language_info);
        resumeLanguageInfoActivity = ResumeLanguageInfoActivity.this;
        initData();
        initView();
    }

    private void initData() {
        dbOperator = new DAO_DBOperator(mContext);
        resumeIdString = getIntent().getStringExtra("resumeId");
        resumeLanguageString = getIntent().getStringExtra("resumeLanguage");
        isCHS = getIntent().getBooleanExtra("isCHS", true);
        resumeLanguageLevels = dbOperator.query_ResumeLanguageLevel();
        listResumeLang = new ArrayList<>();
        for (ResumeLanguageLevel resumeLang : resumeLanguageLevels) {
            listResumeLang.add(resumeLang);
        }
        adapter = new ResumeLanguageExpLVAdapter(mContext, listResumeLang, resumeIdString, resumeLanguageString, ResumeLanguageInfoActivity.this);
    }

    private void initView() {
        footView = LayoutInflater.from(mContext).inflate(R.layout.item_resume_foot, null);
        elv_resume_language = (ExpandableListView) findViewById(R.id.elv_resume_language);
        iv_resume_language_back = (ImageView) findViewById(R.id.iv_resume_language_back);
        tv_resume_language_save = (TextView) findViewById(R.id.tv_resume_language_save);
        tv_resume_item_addeud = (TextView) footView.findViewById(R.id.tv_resume_item_addeud);

        tv_resume_item_addeud.setOnClickListener(this);
        iv_resume_language_back.setOnClickListener(this);
        tv_resume_language_save.setOnClickListener(this);


        elv_resume_language.addFooterView(footView);
        elv_resume_language.setAdapter(adapter);


        tv_resume_item_addeud.setText("+添加语言");
        tv_resume_item_addeud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listResumeLang.add(new ResumeLanguageLevel());
                adapter.notifyDataSetChanged();
            }
        });

        elv_resume_language.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < adapter.getGroupCount(); i++) {
                    if (groupPosition != i) {
                        elv_resume_language.collapseGroup(i);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_resume_language_back:
                showSaveDialog();
                break;
            case R.id.tv_resume_language_save:
                canSave = true;
                for (int i = 0; i < listResumeLang.size(); i++) {
                    if (elv_resume_language.isGroupExpanded(i)) {
                        canSave = false;
                    }
                }
                if (MyUtils.ableInternet) {
                    if (canSave) {
                        uploadData(resumeIdString);
                    } else {
                        Toast.makeText(ResumeLanguageInfoActivity.this, "请先保存后再上传", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ResumeLanguageInfoActivity.this, "网络异常，保存失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void refresh() {
        Intent intent = new Intent(mContext, ResumeLanguageInfoActivity.class);
        if (resumeIdString != null) {
            intent.putExtra("resumeId", resumeIdString);
            intent.putExtra("resumeLanguage", "zh");
            intent.putExtra("isCHS", true);
        }
        startActivity(intent);
        finish();
    }
}
