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
import com.hr.ui.adapter.ResumeTrainExpLVAdapter;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumePlant;
import com.hr.ui.utils.MyUtils;

import java.util.ArrayList;

/**
 * @author Colin
 *         <p/>
 *         工作经验
 */
public class ResumeTrainExpActivity extends BaseResumeActivity implements View.OnClickListener {

    private static final String TAG = "ResumetrainexpActivity";
    private Context mContext = ResumeTrainExpActivity.this;
    public ExpandableListView elv_resume_trainexp;
    private ImageView iv_resume_trainexp_back;
    private TextView tv_resume_item_addeud, tv_resume_trainexp_save;

    private DAO_DBOperator dbOperator;
    private boolean isCHS;
    private String resumeIdString, resumeLanguageString;
    private ResumePlant[] resumePlant;
    private ArrayList<ResumePlant> listResumePlant;
    private ResumeTrainExpLVAdapter adapter;

    private View footView;
    public static ResumeTrainExpActivity resumeTrainExpActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_train_exp);
        resumeTrainExpActivity = ResumeTrainExpActivity.this;
        initData();
        initView();
    }

    private void initData() {
        dbOperator = new DAO_DBOperator(mContext);
        resumeIdString = getIntent().getStringExtra("resumeId");
        resumeLanguageString = getIntent().getStringExtra("resumeLanguage");
        isCHS = getIntent().getBooleanExtra("isCHS", true);
        resumePlant = dbOperator.query_ResumeTraining(resumeIdString, resumeLanguageString);
        listResumePlant = new ArrayList<>();
        for (ResumePlant resumePlantExp : resumePlant) {
            listResumePlant.add(resumePlantExp);
        }
        adapter = new ResumeTrainExpLVAdapter(mContext, listResumePlant, resumeIdString, resumeLanguageString, ResumeTrainExpActivity.this);
    }

    private void initView() {
        footView = LayoutInflater.from(mContext).inflate(R.layout.item_resume_foot, null);
        elv_resume_trainexp = (ExpandableListView) findViewById(R.id.elv_resume_trainexp);
        iv_resume_trainexp_back = (ImageView) findViewById(R.id.iv_resume_trainexp_back);
        tv_resume_trainexp_save = (TextView) findViewById(R.id.tv_resume_trainexp_save);
        tv_resume_item_addeud = (TextView) footView.findViewById(R.id.tv_resume_item_addeud);

        iv_resume_trainexp_back.setOnClickListener(this);
        tv_resume_trainexp_save.setOnClickListener(this);

        elv_resume_trainexp.addFooterView(footView);
        elv_resume_trainexp.setAdapter(adapter);

        tv_resume_item_addeud.setText("+添加培训经历");
        tv_resume_item_addeud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listResumePlant.add(new ResumePlant());
                adapter.notifyDataSetChanged();
            }
        });
        elv_resume_trainexp.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < adapter.getGroupCount(); i++) {
                    if (groupPosition != i) {
                        elv_resume_trainexp.collapseGroup(i);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_resume_trainexp_back:
                showSaveDialog();
                break;
            case R.id.tv_resume_trainexp_save:
                canSave = true;
                for (int i = 0; i < listResumePlant.size(); i++) {
                    if (elv_resume_trainexp.isGroupExpanded(i)) {
                        canSave = false;
                    }
                }
                if (MyUtils.ableInternet) {
                    if (canSave) {
                        uploadData(resumeIdString);
                    } else {
                        Toast.makeText(ResumeTrainExpActivity.this, "请先保存后再上传", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ResumeTrainExpActivity.this, "网络异常，保存失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void refresh() {
        Intent intent = new Intent(mContext, ResumeTrainExpActivity.class);
        if (resumeIdString != null) {
            intent.putExtra("resumeId", resumeIdString);
            intent.putExtra("resumeLanguage", "zh");
            intent.putExtra("isCHS", true);
        }
        startActivity(intent);
        finish();
    }
}
