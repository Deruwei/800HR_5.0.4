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
import com.hr.ui.adapter.ResumeJobExpLVAdapter;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeExperience;
import com.hr.ui.model.ResumeTitle;
import com.hr.ui.utils.MyUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;

/**
 * @author Colin
 *         工作经验
 */
public class ResumeJobExpActivity extends BaseResumeActivity implements View.OnClickListener {

    private static final String TAG = "ResumeJobExpActivity";
    private Context mContext = ResumeJobExpActivity.this;
    public ExpandableListView elv_resume_jobexp;
    private ImageView iv_resume_jobexp_back;
    private TextView tv_resume_item_addeud, tv_resume_jobexp_save;

    private DAO_DBOperator dbOperator;
    private boolean isCHS;
    private String resumeIdString, resumeLanguageString;
    private ResumeExperience[] resumeExperience;
    private ArrayList<ResumeExperience> listResumeJobExp;
    private ResumeJobExpLVAdapter adapter;

    private View footView;
    private ResumeTitle resumeTitle;
    public static ResumeJobExpActivity resumeJobExpActivity;

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
        setContentView(R.layout.activity_resume_job_exp);
        resumeJobExpActivity = ResumeJobExpActivity.this;
        initData();
        initView();
    }

    private void initData() {
        dbOperator = new DAO_DBOperator(mContext);
        resumeIdString = getIntent().getStringExtra("resumeId");
        resumeLanguageString = getIntent().getStringExtra("resumeLanguage");
        isCHS = getIntent().getBooleanExtra("isCHS", true);
        resumeExperience = dbOperator.query_ResumeWorkExperience(resumeIdString,
                resumeLanguageString);
        listResumeJobExp = new ArrayList<>();

        for (ResumeExperience resumeJobExp : resumeExperience) {
            listResumeJobExp.add(resumeJobExp);
        }
        adapter = new ResumeJobExpLVAdapter(mContext, listResumeJobExp, resumeIdString, resumeLanguageString, ResumeJobExpActivity.this);
    }

    private void initView() {
        tv_resume_jobexp_save = (TextView) findViewById(R.id.tv_resume_jobexp_save);
        footView = LayoutInflater.from(mContext).inflate(R.layout.item_resume_foot, null);
        elv_resume_jobexp = (ExpandableListView) findViewById(R.id.elv_resume_jobexp);
        iv_resume_jobexp_back = (ImageView) findViewById(R.id.iv_resume_jobexp_back);
        tv_resume_item_addeud = (TextView) footView.findViewById(R.id.tv_resume_item_addeud);

        iv_resume_jobexp_back.setOnClickListener(this);
        tv_resume_jobexp_save.setOnClickListener(this);

        elv_resume_jobexp.addFooterView(footView);
        elv_resume_jobexp.setAdapter(adapter);


        tv_resume_item_addeud.setText("+添加工作经验");
        tv_resume_item_addeud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listResumeJobExp.add(new ResumeExperience());
                adapter.notifyDataSetChanged();
            }
        });
        resumeTitle = dbOperator.query_ResumeTitle_info(resumeIdString,
                resumeLanguageString);
        if (resumeTitle != null) {
            if ("2".equalsIgnoreCase(resumeTitle.getResume_type())) {// 实习经验
                if (isCHS) {
                    ((TextView) findViewById(R.id.tv_resume_jobexp_jobnum))
                            .setText("实习经验");
                    tv_resume_item_addeud.setText("+添加实习经验");
                } else {
                    ((TextView) findViewById(R.id.tv_resume_jobexp_jobnum))
                            .setText("Experience");
                }
            }
        }

        elv_resume_jobexp.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < adapter.getGroupCount(); i++) {
                    if (groupPosition != i) {
                        elv_resume_jobexp.collapseGroup(i);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_resume_jobexp_back:
                showSaveDialog();
                break;
            case R.id.tv_resume_jobexp_save:
                canSave = true;
                for (int i = 0; i < listResumeJobExp.size(); i++) {
                    if (elv_resume_jobexp.isGroupExpanded(i)) {
                        canSave = false;
                    }
                }
                if (MyUtils.ableInternet) {
                    if (canSave) {
                        uploadData(resumeIdString);
                    } else {
                        Toast.makeText(ResumeJobExpActivity.this, "请先保存后再上传", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ResumeJobExpActivity.this, "网络异常，保存失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void refresh() {
        Intent intent = new Intent(mContext, ResumeJobExpActivity.class);
        if (resumeIdString != null) {
            intent.putExtra("resumeId", resumeIdString);
            intent.putExtra("resumeLanguage", "zh");
            intent.putExtra("isCHS", true);
        }
        startActivity(intent);
        finish();
    }
}
