package com.hr.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.config.Constants;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.FileUtil;
import com.hr.ui.utils.datautils.MobileUrl;
import com.hr.ui.utils.datautils.Rc4Md5Utils;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.datautils.ViewUtils;
import com.hr.ui.utils.netutils.AsyncCompanyIntroduce;
import com.hr.ui.utils.netutils.AsyncPositionDetail;
import com.hr.ui.utils.netutils.Async_DeleteCollectPostion;
import com.hr.ui.utils.netutils.DownImg;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.view.custom.PosterDialog;
import com.hr.ui.view.custom.ViewArea;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;

public class PostParticularsActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "PostParticularsActivity";
    private Context mContext = PostParticularsActivity.this;
    private SharedPreferencesUtils sUtils;
    private RelativeLayout rl_postparticulars_top;
    private ImageView iv_postparticulars_back;
    private TextView tv_post_particulars_unfold;
    private ImageView iv_postparticulars_share;
    private ScrollView sl_postparticular1;
    private ImageView iv_postparticular_comlogo;
    private TextView tv_postparticular_postname;
    private TextView tv_postparticular_comname;
    private TextView tv_postparticular_edu;
    private TextView tv_postparticular_exp;
    private TextView tv_postparticular_sal;
    private TextView tv_postparticular_city;
    private TextView tv_postparticular_releasetime;
    private TextView tv_postparticular_postparticular;
    private ScrollView sl_postparticular2;
    private ImageView iv_postparticular_comlogo2;
    private TextView tv_postparticular_comname2;
    private TextView tv_postparticular_nature;
    private TextView tv_postparticular_scale;
    private TextView tv_postparticular_email;
    private TextView tv_postparticular_place;
    private TextView tv_postparticular_comparticular;
    private RelativeLayout rl_postparticular_clickother;
    private LinearLayout rl_postparticulars_visible;
    private Button bt_postparticulars_collect;
    private Button bt_postparticulars_send;
    private DownLoadImg dLoadImg;
    private long currTime = 0;// 分享的点击时间
    private int error_code;
    /**
     * 是否收藏 是否已经收藏，0没有；1有（如果是未登录用户此处为0）
     */
    public static String is_favourite;
    /**
     * 是否投递 是否已经收藏，0没有；1有（如果是未登录用户此处为0）
     */
    private String is_apply = "";
    private LinearLayout ll_postparticular_poster;
    private LinearLayout ll_viewArea;
    private LinearLayout.LayoutParams parm;
    private ViewArea viewArea;
    private PosterDialog.Builder posterDialog;

    /**
     * UIL配置信息
     */
    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    /**
     * 海报图片
     */
    private Bitmap drawable;
    /**
     * 海报地址
     */
    private String posterPath;
    /**
     * 返回的职位详情
     */
    private HashMap<String, String> resultPostMap;
    /**
     * 返回的公司详情
     */
    private HashMap<String, String> resultComMap;
    /**
     * 职位 ID
     */
    private String jobId;
    /**
     * 公司ID
     */
    private String comId;
    /**
     * 本地缓存图片名字
     */
    private String comLogoFileName;
    /**
     * 是否已经过期 0未过期 1 已经过期
     */
    private String is_expire;
    /**
     * logo地址
     */
    private String logoUrl;

    private Handler handlerSend;
    public Handler handlerCollectJob = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String json = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    error_code = jsonObject.getInt("error_code");
                    switch (error_code) {
                        case 0:// 成功
                            is_favourite = "1";
                            bt_postparticulars_collect.setText("取消收藏");
                            if (handlerSend != null) {
                                Message message = handlerSend.obtainMessage();
                                message.what = 0;
                                handlerSend.sendMessage(message);
                            }
                            Toast.makeText(mContext, "收藏成功", Toast.LENGTH_SHORT)
                                    .show();
                            MobclickAgent.onEvent(PostParticularsActivity.this, "job-add-favorites");
                            break;
                        case 304:// 为设置默认简历
                            Toast.makeText(mContext, "请先到简历中心设置默认简历",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(mContext,
                                    Rc4Md5Utils.getErrorResourceId(error_code),
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "收藏失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    // 设置页面的value
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg != null) {
//                onCreate(null);// 刷新页面
                resultPostMap = (HashMap<String, String>) msg.obj;
                tv_postparticular_postname.setText(resultPostMap.get("job_name"));
                tv_postparticular_place.setText(resultPostMap.get("workplace"));
                tv_postparticular_edu.setText(resultPostMap.get("study"));
                tv_postparticular_sal.setText(resultPostMap.get("salary"));
                tv_postparticular_releasetime.setText(resultPostMap.get("issue_date"));
                tv_postparticular_exp.setText(resultPostMap.get("workyear"));
                tv_postparticular_postparticular.setText(resultPostMap.get("synopsis"));
                tv_postparticular_comname.setText(resultPostMap.get("enterprise_name"));
                tv_postparticular_city.setText(resultPostMap.get("workplace"));
                if (resultPostMap.get("email").equals("")) {
                    tv_postparticular_email.setText("企业邮箱：" + "保密");
                } else {
                    tv_postparticular_email.setText("企业邮箱：" + resultPostMap.get("email"));
                }

                comId = resultPostMap.get("enterprise_id");
                loadComNetData();
                Log.i(TAG, "=====poster" + resultPostMap.get("posterimg").toString());
                posterPath = resultPostMap.get("posterimg").toString();
                if (!posterPath.equals("")) {
                    ll_postparticular_poster.setVisibility(View.VISIBLE);
                    DownLoadImg();
                }
                is_favourite = resultPostMap.get("is_favourite");
                is_apply = resultPostMap.get("is_apply");
                is_expire = resultPostMap.get("is_expire");
                if (is_favourite.equals("0")) {
                    bt_postparticulars_collect.setText("收藏该职位");
                } else {
                    bt_postparticulars_collect.setText("取消收藏");
                }

                if (is_apply.equals("0")) {
                    bt_postparticulars_send.setText("投递该职位");
                } else {
                    bt_postparticulars_send.setText("已投递");
                }
            }
        }
    };
    Handler handlerForCom = new Handler() {
        public void handleMessage(Message msg) {
            if (msg != null) {
                resultComMap = (HashMap<String, String>) msg.obj;
                tv_postparticular_comname2.setText(resultComMap.get("enterprise_name"));
                tv_postparticular_nature.setText(resultComMap.get("company_type"));
                tv_postparticular_place.setText(resultComMap.get("address"));
                tv_postparticular_scale.setText(resultComMap.get("stuff_munber"));
                tv_postparticular_comparticular.setText(resultComMap.get("synopsis"));

                logoUrl = resultComMap.get("ent_logo").toString();
                if (!logoUrl.equals("")) {
                    imageLoader.displayImage(Constants.LOGO_ROOTPATH + logoUrl, iv_postparticular_comlogo, options);
                    imageLoader.displayImage(Constants.LOGO_ROOTPATH + logoUrl, iv_postparticular_comlogo2, options);
                }
            }
        }
    };

    /**
     * 简历投递handler
     */
    private Handler handlerSendResume = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String json = (String) msg.obj;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    error_code = jsonObject.getInt("error_code");
                    switch (error_code) {
                        case 0:// 成功
                            bt_postparticulars_send.setText("已投递");
                            if (handlerSend != null) {
                                Message message = handler.obtainMessage();
                                message.what = 0;
                                handlerSend.sendMessage(message);
                            }
                            Toast.makeText(mContext, "投递职位成功", Toast.LENGTH_SHORT).show();
                            MobclickAgent.onEvent(PostParticularsActivity.this, "cv-send");
                            break;
                        case 304:// 为设置默认简历
                            Toast.makeText(mContext, Constants.APPLY_NO_RESUME,
                                    Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(mContext,
                                    Rc4Md5Utils.getErrorResourceId(error_code),
                                    Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "申请失败", Toast.LENGTH_SHORT).show();
                }
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_particulars);
        MobclickAgent.onEvent(this, " job-show-job-info");
//        initView();
//        initData();
//        initUIL();
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
        initData();
        initUIL();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    /**
     * 初始化UIL
     */
    private void initUIL() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.logo_gongsi)
                .showImageForEmptyUri(R.mipmap.logo_gongsi)
                .showImageOnFail(R.mipmap.logo_gongsi)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(100))
                .build();
    }

    private void initView() {
        rl_postparticulars_top = (RelativeLayout) findViewById(R.id.rl_postparticulars_top);
        rl_postparticulars_visible = (LinearLayout) findViewById(R.id.rl_postparticulars_visible);
        iv_postparticulars_back = (ImageView) findViewById(R.id.iv_postparticulars_back);
        iv_postparticulars_share = (ImageView) findViewById(R.id.iv_postparticulars_share);
        iv_postparticular_comlogo2 = (ImageView) findViewById(R.id.iv_postparticular_comlogo2);
        iv_postparticular_comlogo = (ImageView) findViewById(R.id.iv_postparticular_comlogo);

        sl_postparticular1 = (ScrollView) findViewById(R.id.sl_postparticular1);
        sl_postparticular2 = (ScrollView) findViewById(R.id.sl_postparticular2);

        tv_postparticular_postname = (TextView) findViewById(R.id.tv_postparticular_postname);
        tv_postparticular_comname = (TextView) findViewById(R.id.tv_postparticular_comname);
        tv_postparticular_edu = (TextView) findViewById(R.id.tv_postparticular_edu);
        tv_postparticular_exp = (TextView) findViewById(R.id.tv_postparticular_exp);
        tv_postparticular_sal = (TextView) findViewById(R.id.tv_postparticular_sal);
        tv_postparticular_city = (TextView) findViewById(R.id.tv_postparticular_city);
        tv_postparticular_email = (TextView) findViewById(R.id.tv_postparticular_email);
        tv_postparticular_releasetime = (TextView) findViewById(R.id.tv_postparticular_releasetime);
        tv_postparticular_postparticular = (TextView) findViewById(R.id.tv_postparticular_postparticular);
        tv_postparticular_comname2 = (TextView) findViewById(R.id.tv_postparticular_comname2);
        tv_postparticular_nature = (TextView) findViewById(R.id.tv_postparticular_nature);
        tv_postparticular_scale = (TextView) findViewById(R.id.tv_postparticular_scale);
        tv_postparticular_place = (TextView) findViewById(R.id.tv_postparticular_place);
        tv_postparticular_comparticular = (TextView) findViewById(R.id.tv_postparticular_comparticular);
        rl_postparticular_clickother = (RelativeLayout) findViewById(R.id.rl_postparticular_clickother);
        tv_post_particulars_unfold = (TextView) findViewById(R.id.tv_post_particulars_unfold);

        bt_postparticulars_send = (Button) findViewById(R.id.bt_postparticulars_send);
        bt_postparticulars_collect = (Button) findViewById(R.id.bt_postparticulars_collect);

        ll_postparticular_poster = (LinearLayout) findViewById(R.id.ll_postparticular_poster);

        ll_postparticular_poster.setOnClickListener(this);
        iv_postparticulars_back.setOnClickListener(this);
        iv_postparticulars_share.setOnClickListener(this);
        bt_postparticulars_collect.setOnClickListener(this);
        bt_postparticulars_send.setOnClickListener(this);
        tv_post_particulars_unfold.setOnClickListener(this);
        rl_postparticular_clickother.setOnClickListener(this);

    }

    private void initData() {
        sUtils = new SharedPreferencesUtils(mContext);
        Bundle bundle = getIntent().getExtras();
        jobId = bundle.getString("job_id");
        loadPostNetData();
    }

    /**
     * 加载职位详情数据
     */
    private void loadPostNetData() {
        new AsyncPositionDetail(this, handler).execute("job.jobinfo", jobId);
    }

    /**
     * 加载公司详情数据
     */
    private void loadComNetData() {
        new AsyncCompanyIntroduce(this, handlerForCom).execute("job.enterprise", comId);
    }

    /**
     * 下载海报
     */
    private void DownLoadImg() {
        if (!posterPath.equals("") && posterPath != null) {
            comLogoFileName = FileUtil.getRootDir() + "/800HR/Poster/"
                    + posterPath.substring(posterPath.lastIndexOf("/") + 1);
//            comLogoFileName = FileUtil.getRootDir() + Constants.IMAGE_ROOTPATH
//                    + posterPath.substring(posterPath.lastIndexOf("/") + 1);
            if (FileUtil.isFileExist(comLogoFileName)) {
                drawable = BitmapFactory.decodeFile(comLogoFileName);
                showImg(drawable);
            } else {// 下载海报
                dLoadImg = new DownLoadImg(iv_postparticular_comlogo2);
                dLoadImg.execute(posterPath);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_postparticular_poster:
                showImg(drawable);
                break;
            case R.id.iv_postparticulars_back:
                finish();
                break;
            case R.id.bt_postparticulars_collect:
                collectJob();
                break;
            case R.id.bt_postparticulars_send:
                applyJob();
                break;
            case R.id.tv_post_particulars_unfold:
                if (tv_post_particulars_unfold.getText().equals("展开")) {
                    tv_post_particulars_unfold.setText("收起");
                    ViewUtils.expandTextView(tv_postparticular_comparticular);
                } else {
                    tv_post_particulars_unfold.setText("展开");
                    ViewUtils.expandTextView(tv_postparticular_comparticular);
                }
                break;
            case R.id.rl_postparticular_clickother:
                Intent intent = new Intent(PostParticularsActivity.this, CompanyOtherJobActivity.class);
                intent.putExtra("enterprise_id", comId);
                startActivity(intent);
                break;
            case R.id.iv_postparticulars_share:
//                ShareSDK.initSDK(this);
                OnekeyShare oks = new OnekeyShare();
                // 关闭sso授权
                oks.disableSSOWhenAuthorize();
//                oks.addHiddenPlatform(QQ.NAME);
                String text = "";
                String mobilUrl = null;
                String enterprise_name = resultComMap.get("enterprise_name");
                String jobnameString = resultPostMap.get("job_name");
                if (jobnameString == null || jobnameString.length() == 0) {
                    text = "我在行业找工作上看到了" + enterprise_name + "发布了招聘职位。";
                    mobilUrl = MobileUrl.getCompanyUrl(resultPostMap.get("enterprise_id"));
                } else {
                    text = "我在行业找工作上看到了" + enterprise_name + "的" + jobnameString + "职位";
                    mobilUrl = MobileUrl.getJobUrl(resultPostMap.get("job_id"));
                }

                System.out.println("mobilUrl==" + mobilUrl);
                text = text + " " + mobilUrl;
                // text是分享文本，所有平台都需要这个字段
                oks.setText(text);
                // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                oks.setTitle(text);
                // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
                oks.setTitleUrl(mobilUrl);
                // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//                if (!Image_path.equals("")) {
//                    String fileName = FileUtil.getRootDir() + "/800HR/Poster/"
//                            + Image_path.substring(Image_path.lastIndexOf("/") + 1);
//                    oks.setImagePath(fileName);
//                }
                // url仅在微信（包括好友和朋友圈）中使用
                oks.setUrl(mobilUrl);
                // 启动分享GUI,防止用户多次点击
                if (System.currentTimeMillis() - currTime > 500) {
                    oks.show(this);
                }
                currTime = System.currentTimeMillis();
                break;
        }
    }

    /**
     * 投递职位
     */
    private void applyJob() {
        if (MyUtils.isLogin) {
//            Toast.makeText(this, resultPostMap.get("is_expire"), Toast.LENGTH_SHORT).show();
            if (is_apply != null) {
                if (is_apply.equals("0")) {
                    //没有申请
                    HashMap<String, String> requestParams = new HashMap<String, String>();
                    requestParams.put("method", "job.apply");
                    requestParams.put("job_id", jobId);
                    if (sUtils.getStringValue("is_app_resumeid" + MyUtils.userID, "000").equals("000")) {
                        Toast.makeText(mContext, Constants.APPLY_NO_RESUME, Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        requestParams.put("resume_id", sUtils.getStringValue("is_app_resumeid" + MyUtils.userID, "000"));
                    }
                    requestParams.put("resume_language", "zh");
                    NetService service = new NetService(mContext, handlerSendResume);
                    service.execute(requestParams);
                } else {
                    Toast.makeText(this, "已经申请过该职位", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            goLoginActivity();
        }
    }

    /**
     * 收藏职位
     */
    private void collectJob() {
        if (MyUtils.isLogin) {
            if (is_favourite.equals("0")) {
                HashMap<String, String> requestParams = new HashMap<String, String>();
                requestParams.put("method", "job.favour");
                requestParams.put("job_id", jobId);
                NetService service = new NetService(mContext, handlerCollectJob);
                service.execute(requestParams);
            } else {
                new Async_DeleteCollectPostion(mContext, bt_postparticulars_collect, null, null, -1, null).execute("user_stow.delefavourite", jobId, "");
            }
        } else {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            goLoginActivity();
        }
    }

    /**
     * 异步下载图片
     */
    private class DownLoadImg extends AsyncTask<String, Integer, Bitmap> {

        ImageView imageView;

        DownLoadImg(ImageView img) {
            this.imageView = img;
        }

        protected Bitmap doInBackground(String... params) {
            if (params != null) {
                String TalkPath = null;
                DownImg fileDownload = new DownImg();
                TalkPath = fileDownload.DownLoadFile(params[0], "/800HR/Poster/");
                Log.i("PosterActivity", "poster path=" + TalkPath);
                drawable = BitmapFactory.decodeFile(TalkPath);
                return drawable;
            }
            return null;
        }

        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            if (result != null) {
                showImg(result);
            }
            super.onPostExecute(result);
        }
    }

    /**
     * 弹出自定义 Dialog
     */
    private void showImg(Bitmap res) {
        if (res != null) {
            // 初始化一个自定义的Dialog
            posterDialog = new PosterDialog.Builder(this);
            LayoutInflater inflater = LayoutInflater.from(PostParticularsActivity.this);
            View view = inflater.inflate(R.layout.poster_dialog_background, null);
            ImageView closeBtn = (ImageView) view.findViewById(R.id.close_btn);
            ll_viewArea = (LinearLayout) view.findViewById(R.id.ll_viewArea);
            parm = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
            parm.gravity = Gravity.CENTER;
            // 自定义布局控件，用来初始化并存放自定义imageView
            viewArea = new ViewArea(PostParticularsActivity.this, res);
            ll_viewArea.addView(viewArea, parm);
            posterDialog.setView(view);
            if (posterDialog != null) {
                posterDialog.show();
            }
            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    posterDialog.dismiss();
                }
            });
        }
    }

    private void goLoginActivity() {
        Intent intentLogin = new Intent(PostParticularsActivity.this, NewLoginActivity.class);
        startActivity(intentLogin);
    }
}
