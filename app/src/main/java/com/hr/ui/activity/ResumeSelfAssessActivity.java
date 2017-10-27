package com.hr.ui.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.model.ResumeAssessInfo;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.ResumeIsUpdateOperator;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * @author Colin
 *         自我评价
 */
public class ResumeSelfAssessActivity extends BaseResumeActivity implements View.OnClickListener {
    private Context mContext = ResumeSelfAssessActivity.this;
    private DAO_DBOperator dbOperator;
    private boolean isCHS;
    private String resumeIdString, resumeLanguageString;
    private ResumeAssessInfo resumeAssessInfo;
    private static final String TAG = "ResumeSelfAssessActivity";
    private EditText et_resume_selfassess;
    private ImageView iv_resume_selfassess_back;
    private RelativeLayout rl_resume_selfassess_add;

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
        setContentView(R.layout.activity_resume_self_assess);
        initData();
        initView();
        modification = false;
    }

    private void initView() {
        et_resume_selfassess = (EditText) findViewById(R.id.et_resume_selfassess);
        iv_resume_selfassess_back = (ImageView) findViewById(R.id.iv_resume_selfassess_back);
        rl_resume_selfassess_add = (RelativeLayout) findViewById(R.id.rl_resume_selfassess_add);

        rl_resume_selfassess_add.setOnClickListener(this);
        iv_resume_selfassess_back.setOnClickListener(this);
        et_resume_selfassess.setOnClickListener(this);
        if (resumeAssessInfo == null) {
            resumeAssessInfo = new ResumeAssessInfo();
        } else {
            et_resume_selfassess.setText(resumeAssessInfo.getIntroduction());
        }
    }

    private void initData() {
        dbOperator = new DAO_DBOperator(mContext);
        resumeIdString = getIntent().getStringExtra("resumeId");
        resumeLanguageString = getIntent().getStringExtra("resumeLanguage");
        isCHS = getIntent().getBooleanExtra("isCHS", true);
        resumeAssessInfo = dbOperator.query_ResumeTome_Toone(resumeIdString, resumeLanguageString);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_resume_selfassess_back:
                showSaveDialog();
                break;
            case R.id.et_resume_selfassess:
                modification = true;
                break;
            case R.id.rl_resume_selfassess_add:
                if (MyUtils.ableInternet) {
                    saveData();
                } else {
                    Toast.makeText(ResumeSelfAssessActivity.this, "网络异常，保存失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void saveData() {
        MyUtils.canResumeReflesh=true;
        String inputString = et_resume_selfassess
                .getText().toString();
        if (inputString.trim().equals("")) {
            Toast.makeText(this, "请输入自我评价", Toast.LENGTH_SHORT).show();
            return;
        }
        resumeAssessInfo.setIntroduction(inputString);
        resumeAssessInfo.setResume_id(resumeIdString);
        resumeAssessInfo.setResume_language(resumeLanguageString);
        resumeAssessInfo.setUser_id(MyUtils.userID);
        if (resumeAssessInfo.getId() == -1) {// 新建自我评价
            int insertResult = dbOperator.Insert_ResumeTome(resumeAssessInfo);
            if (insertResult > 0) {
                ResumeIsUpdateOperator.setResumeTitleIsUpdate(this, dbOperator,
                        resumeIdString, resumeLanguageString);
//                Toast.makeText(this, "保存成功", Toast.LENGTH_LONG).show();
//                // 刷新简历完整度
//                MyResumeActivity.myResumeActivity.shouldUpdate = true;
//                finish();
                uploadData(resumeIdString);
            } else {
                Toast.makeText(this, "保存失败", Toast.LENGTH_LONG).show();
            }
            MyUtils.canResumeReflesh=true;
        } else {// 修改自我评价
            // 更新信息到数据库
            MyUtils.canResumeReflesh=true;
            boolean updateResult = dbOperator
                    .update_ResumeTome(resumeAssessInfo);
            if (updateResult) {
                ResumeIsUpdateOperator.setResumeTitleIsUpdate(this, dbOperator,
                        resumeIdString, resumeLanguageString);
//                MyResumeActivity.myResumeActivity.shouldUpdate = true;
//                Toast.makeText(this, "修改成功", Toast.LENGTH_LONG).show();
//                finish();
                uploadData(resumeIdString);
            } else {
                Toast.makeText(this, "修改失败", Toast.LENGTH_LONG).show();
            }
        }
    }
}
