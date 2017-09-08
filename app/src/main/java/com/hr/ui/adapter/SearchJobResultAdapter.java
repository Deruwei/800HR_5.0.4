package com.hr.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.activity.PostParticularsActivity;
import com.hr.ui.config.Constants;
import com.hr.ui.utils.MyUtils;
import com.hr.ui.utils.datautils.FileUtil;
import com.hr.ui.utils.datautils.Rc4Md5Utils;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.DownImg;
import com.hr.ui.utils.netutils.NetService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：Colin
 * 日期：2015/12/23 17:22
 * 邮箱：bestxt@qq.com
 * 搜索结果页 适配器
 * <p>
 * 我用的时候一直报
 * holder.cb.setChecked(getIsSelected.get(position))这一行空指针，之后调试发现isSelected.size一直为0，怀疑是构造函数还没完全初始化
 * 也就是initData还没有运行的时候系统就自动运行getView了，之后将initData设成static就可以成功了。 但是就不能多个对象用了。
 * 初始化的时候for循环里的长度是从Activity传递过来的集合的长度，而不是Adapter中HashMap的长度..
 */
public class SearchJobResultAdapter extends RecyclerView.Adapter<SearchJobResultAdapter.MyViewHolder> {
    private  final String TAG = "SearchJobResultAdapter";
    private  ArrayList<HashMap<String, Object>> dataList;
    private  Context mContext;
    private  HashMap<Integer, Boolean> isSelected;

    private int is_expire;// 是否过期的标识 0：未过期 1：过期
    private int is_favourite;// 是否已经收藏 0：未收藏 1：收藏
    private int is_apply;// 是否投递过该职位 0：未投递 1：已投递
    /**
     * 本地缓存图片名字
     */
    private DownLoadImg dLoadImg;
    private String comLogoFileName;
    private Bitmap drawable;
    private String posterPath;
    private boolean isImage;
    private SharedPreferencesUtils sUtils;
    /**
     * 带海报图片的Item
     */
    final int TYPE_1 = 0;
    /**
     * 纯文字的Item
     */
    final int TYPE_2 = 1;
    private int error_code;
    private Handler handler;

    public  void setDataList(ArrayList<HashMap<String, Object>> dataList) {
        this.dataList = dataList;
        initData();

    }

    public SearchJobResultAdapter(Context context) {
        this.mContext = context;
        isSelected = new HashMap<>();
        sUtils = new SharedPreferencesUtils(mContext);
    }


    // 初始化isSelected的数据
    public void initData() {
        for (int i = 0; i < dataList.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

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
                        case 0:
                            if (handler != null) {
                                Message message = handler.obtainMessage();
                                message.what = 0;
                                handler.sendMessage(message);
                            }
                            Toast.makeText(mContext, "申请职位成功", Toast.LENGTH_SHORT)
                                    .show();
                            break;
                        case 304:// 为设置默认简历
                            Toast.makeText(mContext, "请先到简历中心设置APP简历",
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
                            if (handler != null) {
                                Message message = handler.obtainMessage();
                                message.what = 0;
                                handler.sendMessage(message);
                            }
                            Toast.makeText(mContext, "收藏成功", Toast.LENGTH_SHORT)
                                    .show();
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


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_1) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_searchjob_resultimage, parent, false);
            return new ViewHolderImage(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_searchjob_result, parent, false);
            return new ViewHolderInfo(view);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (holder instanceof ViewHolderImage) {
            ((ViewHolderImage) holder).tvItemSearchjobimageJobname.setText(dataList.get(position).get("job_name").toString());
            ((ViewHolderImage) holder).tvItemSearchjobimageCityname.setText(dataList.get(position).get("workplace").toString());
            ((ViewHolderImage) holder).tvItemSearchjoblvimageCompanyname.setText(dataList.get(position).get("enterprise_name").toString());
            ((ViewHolderImage) holder).tvItemSearchjoblvimageReleasetime.setText(dataList.get(position).get("issue_date").toString());
            ((ViewHolderImage) holder).rlItemSearchjoblvimageClick.setTag(dataList.get(position).get("job_id").toString());

            StringBuffer sb = new StringBuffer();
            ((ViewHolderImage) holder).tvItemSearchjoblvimageApplycollection.setVisibility(View.GONE);
            sb.delete(0, sb.length());
            sb.append("");
            if (dataList.get(position).get("is_apply").toString().equals("1")) {
                sb.append("   已投递");
            }
            if (dataList.get(position).get("is_favourite").toString().equals("1")) {
                sb.append("   已收藏");
            }
            if (sb.toString().trim().length() > 0) {
                ((ViewHolderImage) holder).tvItemSearchjoblvimageApplycollection.setVisibility(View.VISIBLE);
                ((ViewHolderImage) holder).tvItemSearchjoblvimageApplycollection.setText(sb.toString());
            }

//                viewHolderImage.tv_item_searchjoblvimage_releasetime.setText(dataList.get(position).get("is_favourite").toString());
//                viewHolderImage.tv_item_searchjobimage_companyname.setText(dataList.get(position).get("is_apply").toString());
            ((ViewHolderImage) holder).rlItemSearchjoblvimageClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position < dataList.size()) {
                        String job_id = (String) v.getTag();
                        // Toast.makeText(context, "文本信息" + url, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, PostParticularsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("job_id", job_id);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                }
            });
            DownLoadImg(((ViewHolderImage) holder).ivItemSearchjoblvimagePoster);

            if (isSelected.get(position) == null) {
                isSelected.put(position, false);
            }
            ((ViewHolderImage) holder).cbItemSearchjobimageSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        Toast.makeText(mContext, "" + position + isChecked, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(mContext, ""+isSelectMap.get(position), Toast.LENGTH_SHORT).show();
                    isSelected.put(position, isChecked);
                }
            });
            ((ViewHolderImage) holder).cbItemSearchjobimageSelect.setChecked(isSelected.get(position));
        }
        if (holder instanceof ViewHolderInfo) {
            ((ViewHolderInfo) holder).tvItemSearchJobJobName.setText(dataList.get(position).get("job_name").toString());
            ((ViewHolderInfo) holder).tvItemSearchjobCityname.setText(dataList.get(position).get("workplace").toString());
            ((ViewHolderInfo) holder).tvItemSearchjobCompanyname.setText(dataList.get(position).get("enterprise_name").toString());
            ((ViewHolderInfo) holder).tvItemSearchjoblvReleasetime.setText(dataList.get(position).get("issue_date").toString());
            ((ViewHolderInfo) holder).rlItemSearchjoblvClick.setTag(dataList.get(position).get("job_id").toString());
            ((ViewHolderInfo) holder).rlItemSearchjoblvClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position < dataList.size()) {
                        String job_id = (String) v.getTag();
                        // Toast.makeText(context, "文本信息" + url, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, PostParticularsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("job_id", job_id);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                }
            });
            if (isSelected.get(position) == null) {
                isSelected.put(position, false);
            }

            StringBuffer sb2 = new StringBuffer();
            ((ViewHolderInfo) holder).tvItemSearchjoblvApplycollection.setVisibility(View.GONE);
            sb2.delete(0, sb2.length());
            sb2.append("");

            if (dataList.get(position).get("is_apply").toString().equals("1")) {
                sb2.append("   已投递");
            }
            if (dataList.get(position).get("is_favourite").toString().equals("1")) {
                sb2.append("   已收藏");
            }
            if (sb2.toString().trim().length() > 0) {
                ((ViewHolderInfo) holder).tvItemSearchjoblvApplycollection.setVisibility(View.VISIBLE);
                ((ViewHolderInfo) holder).tvItemSearchjoblvApplycollection.setText(sb2.toString());
            }
            ((ViewHolderInfo) holder).cbItemSearchjobSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    isSelected.put(position, isChecked);
                }
            });
            ((ViewHolderInfo) holder).cbItemSearchjobSelect.setChecked(isSelected.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        posterPath = dataList.get(position).get("posterimg").toString();
        if (!posterPath.equals("") && posterPath != null) {
            return TYPE_1;
        } else {
            return TYPE_2;
        }
    }

    @Override
    public int getItemCount() {
        return dataList==null ? 0 : dataList.size();
    }

   /* @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_1:
                    viewHolderImage = new ViewHolderImage();
                    convertView = inflater.inflate(R.layout.item_searchjob_resultimage, null);
                    viewHolderImage.tv_item_searchjobimage_jobname = (TextView) convertView.findViewById(R.id.tv_item_searchjobimage_jobname);
                    viewHolderImage.tv_item_searchjobimage_companyname = (TextView) convertView.findViewById(R.id.tv_item_searchjoblvimage_companyname);
                    viewHolderImage.tv_item_searchjobimage_cityname = (TextView) convertView.findViewById(R.id.tv_item_searchjobimage_cityname);
                    viewHolderImage.tv_item_searchjoblvimage_releasetime = (TextView) convertView.findViewById(R.id.tv_item_searchjoblvimage_releasetime);
                    viewHolderImage.iv_item_searchjoblvimage_jobinfo = (ImageView) convertView.findViewById(R.id.iv_item_searchjoblvimage_jobinfo);
                    viewHolderImage.cb_item_searchjobimage_select = (CheckBox) convertView.findViewById(R.id.cb_item_searchjobimage_select);
                    viewHolderImage.rl_item_searchjoblvimage_click = (RelativeLayout) convertView.findViewById(R.id.rl_item_searchjoblvimage_click);
                    viewHolderImage.iv_item_searchjoblvimage_poster = (ImageView) convertView.findViewById(R.id.iv_item_searchjoblvimage_poster);
                    viewHolderImage.tv_item_searchjoblvimage_applycollection = (TextView) convertView.findViewById(R.id.tv_item_searchjoblvimage_applycollection);
                    convertView.setTag(viewHolderImage);
                    break;
                case TYPE_2:
                    viewHolder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.item_searchjob_result, null);
                    viewHolder.tv_item_searchjob_jobname = (TextView) convertView.findViewById(R.id.tv_item_searchJob_jobName);
                    viewHolder.tv_item_searchjob_companyname = (TextView) convertView.findViewById(R.id.tv_item_searchjob_companyname);
                    viewHolder.tv_item_searchjob_cityname = (TextView) convertView.findViewById(R.id.tv_item_searchjob_cityname);
                    viewHolder.tv_item_searchjoblv_releasetime = (TextView) convertView.findViewById(R.id.tv_item_searchjoblv_releasetime);
                    viewHolder.tv_item_searchjoblv_applycollection = (TextView) convertView.findViewById(R.id.tv_item_searchjoblv_applycollection);
                    viewHolder.iv_item_searchjoblv_jobinfo = (ImageView) convertView.findViewById(R.id.iv_item_searchjoblv_jobinfo);
                    viewHolder.cb_item_searchjob_select = (CheckBox) convertView.findViewById(R.id.cb_item_searchjob_select);
                    viewHolder.rl_item_searchjoblv_click = (RelativeLayout) convertView.findViewById(R.id.rl_item_searchjoblv_click);
                    convertView.setTag(viewHolder);
                    break;
            }
        } else {
            switch (type) {
                case TYPE_1:
                    viewHolderImage = (ViewHolderImage) convertView.getTag();
                    break;
                case TYPE_2:
                    viewHolder = (ViewHolder) convertView.getTag();
                    break;
            }
        }
        switch (type) {
            case TYPE_1:
                viewHolderImage.tv_item_searchjobimage_jobname.setText(dataList.get(position).get("job_name").toString());
                viewHolderImage.tv_item_searchjobimage_cityname.setText(dataList.get(position).get("workplace").toString());
                viewHolderImage.tv_item_searchjobimage_companyname.setText(dataList.get(position).get("enterprise_name").toString());
                viewHolderImage.tv_item_searchjoblvimage_releasetime.setText(dataList.get(position).get("issue_date").toString());
                viewHolderImage.rl_item_searchjoblvimage_click.setTag(dataList.get(position).get("job_id").toString());

                StringBuffer sb = new StringBuffer();
                viewHolderImage.tv_item_searchjoblvimage_applycollection.setVisibility(View.GONE);
                sb.delete(0, sb.length());
                sb.append("");
                if (dataList.get(position).get("is_apply").toString().equals("1")) {
                    sb.append("   已投递");
                }
                if (dataList.get(position).get("is_favourite").toString().equals("1")) {
                    sb.append("   已收藏");
                }
                if (sb.toString().trim().length() > 0) {
                    viewHolderImage.tv_item_searchjoblvimage_applycollection.setVisibility(View.VISIBLE);
                    viewHolderImage.tv_item_searchjoblvimage_applycollection.setText(sb.toString());
                }

//                viewHolderImage.tv_item_searchjoblvimage_releasetime.setText(dataList.get(position).get("is_favourite").toString());
//                viewHolderImage.tv_item_searchjobimage_companyname.setText(dataList.get(position).get("is_apply").toString());
                viewHolderImage.rl_item_searchjoblvimage_click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position < dataList.size()) {
                            String job_id = (String) v.getTag();
                            // Toast.makeText(context, "文本信息" + url, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(mContext, PostParticularsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("job_id", job_id);
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                        }
                    }
                });
                DownLoadImg(viewHolderImage.iv_item_searchjoblvimage_poster);

                if (isSelected.get(position) == null) {
                    isSelected.put(position, false);
                }
                viewHolderImage.cb_item_searchjobimage_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        Toast.makeText(mContext, "" + position + isChecked, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(mContext, ""+isSelectMap.get(position), Toast.LENGTH_SHORT).show();
                        isSelected.put(position, isChecked);
                    }
                });
                viewHolderImage.cb_item_searchjobimage_select.setChecked(isSelected.get(position));
                break;
            case TYPE_2:
                viewHolder.tv_item_searchjob_jobname.setText(dataList.get(position).get("job_name").toString());
                viewHolder.tv_item_searchjob_cityname.setText(dataList.get(position).get("workplace").toString());
                viewHolder.tv_item_searchjob_companyname.setText(dataList.get(position).get("enterprise_name").toString());
                viewHolder.tv_item_searchjoblv_releasetime.setText(dataList.get(position).get("issue_date").toString());
                viewHolder.rl_item_searchjoblv_click.setTag(dataList.get(position).get("job_id").toString());
                viewHolder.rl_item_searchjoblv_click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position < dataList.size()) {
                            String job_id = (String) v.getTag();
                            // Toast.makeText(context, "文本信息" + url, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(mContext, PostParticularsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("job_id", job_id);
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                        }
                    }
                });
                if (isSelected.get(position) == null) {
                    isSelected.put(position, false);
                }

                StringBuffer sb2 = new StringBuffer();
                viewHolder.tv_item_searchjoblv_applycollection.setVisibility(View.GONE);
                sb2.delete(0, sb2.length());
                sb2.append("");

                if (dataList.get(position).get("is_apply").toString().equals("1")) {
                    sb2.append("   已投递");
                }
                if (dataList.get(position).get("is_favourite").toString().equals("1")) {
                    sb2.append("   已收藏");
                }
                if (sb2.toString().trim().length() > 0) {
                    viewHolder.tv_item_searchjoblv_applycollection.setVisibility(View.VISIBLE);
                    viewHolder.tv_item_searchjoblv_applycollection.setText(sb2.toString());
                }
                viewHolder.cb_item_searchjob_select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        isSelected.put(position, isChecked);
                    }
                });
                viewHolder.cb_item_searchjob_select.setChecked(isSelected.get(position));
                break;
        }
        return convertView;
    }*/

    class ViewHolderInfo extends MyViewHolder {
        @Bind(R.id.cb_item_searchjob_select)
        CheckBox cbItemSearchjobSelect;
        @Bind(R.id.tv_item_searchJob_jobName)
        TextView tvItemSearchJobJobName;
        @Bind(R.id.tv_item_searchjob_companyname)
        TextView tvItemSearchjobCompanyname;
        @Bind(R.id.tv_item_searchjob_cityname)
        TextView tvItemSearchjobCityname;
        @Bind(R.id.tv_item_searchjoblv_releasetime)
        TextView tvItemSearchjoblvReleasetime;
        @Bind(R.id.iv_item_searchjoblv_jobinfo)
        ImageView ivItemSearchjoblvJobinfo;
        @Bind(R.id.tv_item_searchjoblv_applycollection)
        TextView tvItemSearchjoblvApplycollection;
        @Bind(R.id.rl_item_searchjoblv_click)
        RelativeLayout rlItemSearchjoblvClick;

        public ViewHolderInfo(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    class ViewHolderImage extends MyViewHolder {
        @Bind(R.id.cb_item_searchjobimage_select)
        CheckBox cbItemSearchjobimageSelect;
        @Bind(R.id.iv_item_searchjoblvimage_poster)
        ImageView ivItemSearchjoblvimagePoster;
        @Bind(R.id.tv_item_searchjobimage_jobname)
        TextView tvItemSearchjobimageJobname;
        @Bind(R.id.tv_item_searchjoblvimage_companyname)
        TextView tvItemSearchjoblvimageCompanyname;
        @Bind(R.id.tv_item_searchjobimage_cityname)
        TextView tvItemSearchjobimageCityname;
        @Bind(R.id.textView28)
        TextView textView28;
        @Bind(R.id.tv_item_searchjoblvimage_releasetime)
        TextView tvItemSearchjoblvimageReleasetime;
        @Bind(R.id.iv_item_searchjoblvimage_jobinfo)
        ImageView ivItemSearchjoblvimageJobinfo;
        @Bind(R.id.tv_item_searchjoblvimage_applycollection)
        TextView tvItemSearchjoblvimageApplycollection;
        @Bind(R.id.rl_item_searchjoblvimage_click)
        RelativeLayout rlItemSearchjoblvimageClick;

        public ViewHolderImage(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
//    /**
//     * 跳转到职位详情页
//     *
//     * @param v
//     */
//    @Override
//    public void onClick(View v) {
//        String job_id = (String) v.getTag();
//        // Toast.makeText(context, "文本信息" + url, Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(mContext, PostParticularsActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("job_id", job_id);
//        intent.putExtras(bundle);
//        mContext.startActivity(intent);
//    }

    /**
     * 异步下载图片
     */
    private class DownLoadImg extends AsyncTask<String, Integer, Bitmap> {
        ImageView imageView;

        DownLoadImg(ImageView img) {
            this.imageView = img;
        }

        protected Bitmap doInBackground(String... params) {
            String TalkPath = null;
            DownImg fileDownload = new DownImg();
            TalkPath = fileDownload.DownLoadFile(params[0], "/800HR/Poster");
            Log.i("PosterActivity", "poster path=" + TalkPath);
            drawable = BitmapFactory.decodeFile(TalkPath);
            return drawable;
        }

        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result != null) {
                imageView.setImageBitmap(drawable);
            }
        }
    }

    /**
     * 下载海报
     */
    private void DownLoadImg(ImageView imageView) {
        if (!posterPath.equals("") && posterPath != null) {
            comLogoFileName = FileUtil.getRootDir() + "/800HR/Poster/"
                    + posterPath.substring(posterPath.lastIndexOf("/") + 1);
            if (FileUtil.isFileExist(comLogoFileName)) {
                imageView.setImageBitmap(BitmapFactory.decodeFile(comLogoFileName));
            } else {// 下载海报
                dLoadImg = new DownLoadImg(imageView);
                dLoadImg.execute(posterPath);
            }
        }
    }

    /**
     * 多选分页
     *
     * @return
     */
    public  HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public  void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        this.isSelected = isSelected;
    }

    /**
     * 判断是否已经投递
     *
     * @return
     */
    private boolean isApplyed() {
        return is_apply == 1 ? true : false;
    }

    /**
     * 投递职位
     */
    public void deliverJob() {
        if (getJobIds() != null) {
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", "job.apply");
            requestParams.put("job_id", getJobIds());
            if (sUtils.getStringValue("is_app_resumeid" + MyUtils.userID, "000").equals("000")) {
                Toast.makeText(mContext, Constants.APPLY_NO_RESUME, Toast.LENGTH_SHORT).show();
                return;
            } else {
                requestParams.put("resume_id", sUtils.getStringValue("is_app_resumeid" + MyUtils.userID, "000"));
            }
            requestParams.put("resume_language", "zh");
            NetService service = new NetService(mContext, handlerSendResume);
            service.execute(requestParams);
        }
    }

    /**
     * 收藏职位
     */
    public void collectJob() {
        if (getJobIds() != null) {
            HashMap<String, String> requestParams = new HashMap<String, String>();
            requestParams.put("method", "job.favour");
            requestParams.put("job_id", getJobIds());
            NetService service = new NetService(mContext, handlerCollectJob);
            service.execute(requestParams);
        }
    }

    /**
     * 获取职位数组集合
     */
    private String getJobIds() {
        StringBuffer sbJobId = new StringBuffer();
        for (int i = 0; i < isSelected.size(); i++) {
            if (isSelected.get(i) == true) {
                if (dataList.get(i).get("is_expire").equals("1")) {
                    Toast.makeText(mContext, "该职位已过期", Toast.LENGTH_SHORT).show();
                    return null;
                } else {
                    sbJobId.append(dataList.get(i).get("job_id") + ",");
                }
            }
        }
        if (sbJobId.toString().length() == 0 || sbJobId == null) {
            Toast.makeText(mContext, "请先选择职位", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            String jobIds = sbJobId.substring(0, sbJobId.length() - 1);
            return jobIds;
        }
    }
}
