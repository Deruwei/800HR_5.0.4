package com.hr.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.hr.ui.R;
import com.hr.ui.utils.datautils.Rc4Md5Utils;
import com.hr.ui.utils.netutils.NetService;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 就业指导--详细页
 */
public class ApplyValuableBookActivity extends Activity implements OnClickListener,OnGestureListener {
    private ScrollView activity_news_more2_scrollview;
    private TextView news_more2_title_text, news_more2_content, tv_applyvaluable;
    private HashMap<String, String> hashMap = null;
    private ArrayList<String> idList;// id集合
    private AsyncGetNewInfo asyncGetNewInfo;
    private static final int FLING_MIN_DISTANCE = 250;
    private ViewFlipper viewFlipper;
    private GestureDetector detector;
    private ImageView iv_applyvaluable_back;
    private int viewpage = 1;// 显示的是哪个view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_valuable_book);
        initView();
        asyncGetNewInfo = new AsyncGetNewInfo(ApplyValuableBookActivity.this);
        asyncGetNewInfo.execute();
    }
    /**
     * 初始化view
     */
    private void initView() {
        iv_applyvaluable_back = (ImageView) findViewById(R.id.iv_applyvaluable_back);
        activity_news_more2_scrollview = (ScrollView) findViewById(R.id.activity_news_more2_scrollview);
        tv_applyvaluable = (TextView) findViewById(R.id.tv_applyvaluable);
        news_more2_title_text = (TextView) findViewById(R.id.news_more2_title_text);
        news_more2_content = (TextView) findViewById(R.id.news_more2_content);
        viewFlipper = (ViewFlipper) findViewById(R.id.activity_news_more2_viewFlipper);
        detector = new GestureDetector(this);
        iv_applyvaluable_back.setOnClickListener(this);
        tv_applyvaluable.setText(VocationalCounselActivity.title);
    }

    /**
     * 获取网络数据，并加载
     */
    class AsyncGetNewInfo {
        String news_title = "";
        String news_content = "";
        private Context context;
        private Handler handServer = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == 0) {
                    try {
                        String json = (String) msg.obj;
                        JSONObject jsonObject = new JSONObject(json);
                        // System.out.println(jsonObject.toString());
                        int error_code = jsonObject.getInt("error_code");
                        if (error_code != 0) {
                            Toast.makeText(context, Rc4Md5Utils.getErrorResourceId(error_code), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        JSONArray jsonArray = jsonObject.getJSONArray("title_content_list");
                        JSONObject jsonItem = jsonArray.getJSONObject(0);
                        news_title = jsonItem.getString("title").toString();
                        news_content = jsonItem.get("content").toString();
                        news_more2_title_text.setVisibility(View.VISIBLE);
                        news_more2_content.setVisibility(View.VISIBLE);
                        news_more2_title_text.setText(news_title + "");
                        news_more2_content.setText(news_content + "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
            ;
        };

        public AsyncGetNewInfo(Context context) {
            this.context = context;
            hashMap = new HashMap<String, String>();
        }

        protected void execute() {
            try {
                hashMap.put("method", "news.gettitlecon");
                hashMap.put("news_id", VocationalCounselActivity.idList.get(VocationalCounselActivity.index));
                NetService service = new NetService(context, handServer);
                service.execute(hashMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.iv_applyvaluable_back:
                finish();
                break;
            default:
                break;
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        // System.out.println("滑动");
        if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE) {
            NewsNext();
            return true;
        }
        if (e1.getX() - e2.getX() < -FLING_MIN_DISTANCE) {
            NewsBefore();
            return true;
        }
        return false;
    }
    /**
     *
     *
     *
     *
     *
     * 上一条
     */
    private void NewsBefore() {
        // 第一项
        if (VocationalCounselActivity.index == 0) {
            Toast.makeText(this, "已经是第一页", Toast.LENGTH_SHORT).show();
        } else {
            VocationalCounselActivity.index--;
            if (viewpage == 1) {
                viewpage = 2;
            } else {
                viewpage = 1;
            }
            news_more2_title_text.setVisibility(View.GONE);
            news_more2_content.setVisibility(View.GONE);
            asyncGetNewInfo = new AsyncGetNewInfo(ApplyValuableBookActivity.this);
            asyncGetNewInfo.execute();
            this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.newsinfo_right_in));
            this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.newsinfo_right_out));
            this.viewFlipper.showPrevious();
            this.activity_news_more2_scrollview.scrollTo(0, 0);
        }
    }

    /**
     *
     *
     * 下一条
     *
     */
    private void NewsNext() {
        // 最后一项
        if (VocationalCounselActivity.index == VocationalCounselActivity.idList.size() - 1) {
            VocationalCounselActivity.myOnScrollListener.startRunningGetData();
        }
        VocationalCounselActivity.index++;
        if (viewpage == 1) {
            viewpage = 2;
        } else {
            viewpage = 1;
        }
        news_more2_title_text.setVisibility(View.GONE);
        news_more2_content.setVisibility(View.GONE);
        asyncGetNewInfo = new AsyncGetNewInfo(ApplyValuableBookActivity.this);
        asyncGetNewInfo.execute();
        // 设置View进入和退出的动画效果 下一页
        this.viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.newsinfo_left_in));
        this.viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.newsinfo_left_out));
        this.viewFlipper.showNext();
        this.activity_news_more2_scrollview.scrollTo(0, 0);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }
}
