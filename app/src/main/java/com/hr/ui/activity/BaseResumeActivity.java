package com.hr.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.Toast;

import com.hr.ui.db.DAO_DBOperator;
import com.hr.ui.fragment.MyResumeFragment;
import com.hr.ui.model.ResumeBaseInfo;
import com.hr.ui.model.ResumeTitle;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.ResumeInfoToJsonString;
import com.hr.ui.utils.netutils.AsyncResumeUpdate;
import com.hr.ui.view.custom.BeautifulDialog;
import com.umeng.analytics.MobclickAgent;

public class BaseResumeActivity extends BaseActivity {

    private Context mContext = BaseResumeActivity.this;
    private static DAO_DBOperator dbOperator;
    private BeautifulDialog.Builder beautifulDialog;
    /**
     * 判断是否修改过
     */
    public static Boolean modification = false;
    /**
     * 判断是否修添加删除
     */
    public static Boolean isAdd = false;
    /**
     *是否可以保存
     */
    public boolean canSave = true;
    /**
     * 更新判断语句
     */
    private Handler handlerUploadResume = new Handler() {
        public void handleMessage(Message msg) {
            if (msg != null) {
                int value = msg.what;
                switch (value) {
                    case -1:
                        Toast.makeText(mContext, "网络异常，保存失败", Toast.LENGTH_SHORT).show();
                        break;
                    case 0:// 中文简历已上传成功
                        MobclickAgent.onEvent(mContext, "cv-upload");
                        MobclickAgent.onEvent(mContext, "cv-edit");
                        Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
                        isAdd = false;
                        modification = false;
//                        MyResumeActivity.myResumeActivity.refreshUI();
                        MyResumeFragment.isRefresh=true;
                        MyUtils.canResumeReflesh=true;
                        MyUtils.canReflesh = true;
                        finish();
                        break;
                    default:
                        break;
                }
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbOperator = new DAO_DBOperator(mContext);
    }

    /**
     * 上传简历（无问题）
     */
    public void uploadData(String resumeId) {
        // 检测本地中文简历
        ResumeTitle titleZhUpdate = dbOperator.query_ResumeTitle_info(resumeId, "zh");
        // 检测本地中文基本信息
        ResumeBaseInfo baseInfoZh = dbOperator.query_ResumePersonInfo_Toone("zh");
        if ((titleZhUpdate != null && titleZhUpdate.getIsUpdate() == 1) || (baseInfoZh != null && baseInfoZh.getIsUpdate() == 1)) {
            // --------- 上传中文简历
            ResumeInfoToJsonString resumeInfoToJsonString = new ResumeInfoToJsonString(mContext, resumeId, "zh");
            String baseInfoString = resumeInfoToJsonString.getBaseInofJsonString();
            String languageString = resumeInfoToJsonString.getLanguageJsonString();
            String resumeInfoString = resumeInfoToJsonString.getResumeDetailInfoJsonString();
            AsyncResumeUpdate asyncResumeUpdate = new AsyncResumeUpdate(mContext, handlerUploadResume, baseInfoString,
                    languageString, resumeInfoString, resumeId, "zh");
            asyncResumeUpdate.execute();
        } else if ((titleZhUpdate != null && titleZhUpdate.getIsUpdate() == 0) || (baseInfoZh != null && baseInfoZh.getIsUpdate() == 0)) {
            // ---------中文不需要上传,检测英文简历
            if(resumeId!=null) {
                Message message = handlerUploadResume.obtainMessage();
                message.what = 0;
                message.arg1 = Integer.parseInt(resumeId);
                handlerUploadResume.sendMessage(message);
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showSaveDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showSaveDialog() {
        if (modification) {
            beautifulDialog = new BeautifulDialog.Builder(mContext);
            beautifulDialog.setMessage("尚未保存，是否退出？");
//        beautifulDialog.setTitle("提示");
            beautifulDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    MyResumeActivity.myResumeActivity.refreshUI();
                    MyResumeFragment.isRefresh=true;
                    modification = false;
                    finish();
                }
            });
            beautifulDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            beautifulDialog.create().show();
        } else {
            modification = false;
            finish();
        }
    }
    @Override
    protected void onDestroy() {
        showSaveDialog();
        super.onDestroy();
    }
}
