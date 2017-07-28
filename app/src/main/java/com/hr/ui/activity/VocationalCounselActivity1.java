package com.hr.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hr.ui.R;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 就业指导
 */
public class VocationalCounselActivity1 extends BaseActivity {
    int[] imgarr = new int[10];
    @Bind(R.id.iv_vc1_back)
    ImageView ivVc1Back;
    @Bind(R.id.rl_vocational_83)
    LinearLayout rlVocational83;
    @Bind(R.id.rl_vocational_120)
    LinearLayout rlVocational120;
    @Bind(R.id.rl_vocational_86)
    LinearLayout rlVocational86;
    @Bind(R.id.rl_vocational_82)
    LinearLayout rlVocational82;
    @Bind(R.id.rl_vocational_723)
    LinearLayout rlVocational723;
    @Bind(R.id.rl_vocational_121)
    LinearLayout rlVocational121;
    @Bind(R.id.rl_vocational_846)
    LinearLayout rlVocational846;
    @Bind(R.id.rl_vocational_87)
    LinearLayout rlVocational87;
    @Bind(R.id.rl_vocational_122)
    LinearLayout rlVocational122;


    //    private ImageView iv_vc1_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocational_counsel1);
        ButterKnife.bind(this);
//        GridView gridview = (GridView) findViewById(R.id.gv_vc1);
//        iv_vc1_back = (ImageView) findViewById(R.id.iv_vc1_back);
//        gridview.setAdapter(new NewsAdapter(this));
//        gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
//        gridview.setOnItemClickListener(this);
//        iv_vc1_back.setOnClickListener(this);

    }
//
//    @Override
//    public void onClick(View v) {
//        // TODO Auto-generated method stub
//        switch (v.getId()) {
//            case R.id.iv_vc1_back:
//                finish();
//                break;
//            default:
//                break;
//        }
//    }

//    @Override
//    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//        Intent intent = new Intent(this, VocationalCounselActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("indexSelected", );
//        switch (arg2) {
//            case 0: // 求职宝典
//                intent.putExtra("id", "83");
//                break;
//            case 1: // 简历指南
//                intent.putExtra("id", "120");
//                break;
//            case 2: // 面试秘籍
//                intent.putExtra("id", "121");
//                break;
//            case 3: // 薪酬行情
//                intent.putExtra("id", "122");
//                break;
//            case 4: // 职业诊断
//                intent.putExtra("id", "86");
//                break;
//            case 5: // 职场八卦
//                intent.putExtra("id", "82");
//                break;
//            case 6: // 人际关系
//                intent.putExtra("id", "87");
//                break;
//            case 7: // 创业指南
//                intent.putExtra("id", "723");
//                break;
//            case 8: // 劳动法苑
//                intent.putExtra("id", "846");
//                break;
//        }
////        View viewscanresume = AndroidUtils.currentGroup.getLocalActivityManager()
////                .startActivity("News_listtingActivity", intent).getDecorView();
////        MyUtils.currentGroup.replaceView(viewscanresume);
//        startActivity(intent);
//    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @OnClick({R.id.iv_vc1_back, R.id.rl_vocational_83, R.id.rl_vocational_120, R.id.rl_vocational_86, R.id.rl_vocational_82, R.id.rl_vocational_723, R.id.rl_vocational_121, R.id.rl_vocational_846, R.id.rl_vocational_87, R.id.rl_vocational_122})
    public void onClick(View view) {
        Intent intent = new Intent(this, VocationalCounselActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        switch (view.getId()) {
            case R.id.iv_vc1_back:
                finish();
                break;
            case R.id.rl_vocational_83:
                intent.putExtra("indexSelected", 0);
                intent.putExtra("id", "83");
                startActivity(intent);
                break;
            case R.id.rl_vocational_120:
                intent.putExtra("indexSelected", 1);
                intent.putExtra("id", "120");
                startActivity(intent);
                break;
            case R.id.rl_vocational_86:
                intent.putExtra("indexSelected", 4);
                intent.putExtra("id", "86");
                startActivity(intent);
                break;
            case R.id.rl_vocational_82:
                intent.putExtra("indexSelected", 5);
                intent.putExtra("id", "82");
                startActivity(intent);
                break;
            case R.id.rl_vocational_723:
                intent.putExtra("indexSelected", 7);
                intent.putExtra("id", "723");
                startActivity(intent);
                break;
            case R.id.rl_vocational_121:
                intent.putExtra("indexSelected", 2);
                intent.putExtra("id", "121");
                startActivity(intent);
                break;
            case R.id.rl_vocational_846:
                intent.putExtra("indexSelected", 8);
                intent.putExtra("id", "846");
                startActivity(intent);
                break;
            case R.id.rl_vocational_87:
                intent.putExtra("indexSelected", 6);
                intent.putExtra("id", "87");
                startActivity(intent);
                break;
            case R.id.rl_vocational_122:
                intent.putExtra("indexSelected", 3);
                intent.putExtra("id", "122");
                startActivity(intent);
                break;
        }
    }
}
