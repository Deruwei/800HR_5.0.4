package com.hr.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hr.ui.R;
import com.hr.ui.activity.PostParticularsActivity;
import com.hr.ui.utils.datautils.FileUtil;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.DownImg;

import java.util.ArrayList;
import java.util.HashMap;

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
public class SearchJobResultRecommendAdapter extends BaseAdapter {
    private static final String TAG = "SearchJobResultRecommendAdapter";
    private static ArrayList<HashMap<String, Object>> dataList;
    private static Context mContext;
    private static HashMap<Integer, Boolean> isSelected;

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
    private ViewHolder viewHolder;
    private ViewHolderImage viewHolderImage;
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
    private LayoutInflater inflater;


    public SearchJobResultRecommendAdapter(Context context, ArrayList<HashMap<String, Object>> list) {
        this.mContext = context;
        this.dataList = list;
        inflater = LayoutInflater.from(mContext);
        sUtils = new SharedPreferencesUtils(mContext);
        if (dataList.size() == 0) {
            dataList = new ArrayList<HashMap<String, Object>>();
            notifyDataSetChanged();
        }
    }


    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
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
                    viewHolder.tv_item_searchjob_jobname = (TextView) convertView.findViewById(R.id.tv_item_searchjob_jobname);
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
                viewHolderImage.cb_item_searchjobimage_select.setVisibility(View.GONE);
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
                break;
            case TYPE_2:
                viewHolder.tv_item_searchjob_jobname.setText(dataList.get(position).get("job_name").toString());
                viewHolder.tv_item_searchjob_cityname.setText(dataList.get(position).get("workplace").toString());
                viewHolder.tv_item_searchjob_companyname.setText(dataList.get(position).get("enterprise_name").toString());
                viewHolder.tv_item_searchjoblv_releasetime.setText(dataList.get(position).get("issue_date").toString());
                viewHolder.rl_item_searchjoblv_click.setTag(dataList.get(position).get("job_id").toString());
                viewHolder.cb_item_searchjob_select.setVisibility(View.GONE);

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
                break;
        }
        return convertView;
    }


    class ViewHolder {
        TextView tv_item_searchjob_jobname, tv_item_searchjoblv_applycollection, tv_item_searchjob_companyname, tv_item_searchjob_cityname, tv_item_searchjoblv_releasetime;
        CheckBox cb_item_searchjob_select;
        ImageView iv_item_searchjoblv_jobinfo;
        RelativeLayout rl_item_searchjoblv_click;
    }

    class ViewHolderImage {
        TextView tv_item_searchjobimage_jobname, tv_item_searchjoblvimage_applycollection, tv_item_searchjobimage_companyname, tv_item_searchjobimage_cityname, tv_item_searchjoblvimage_releasetime;
        CheckBox cb_item_searchjobimage_select;
        ImageView iv_item_searchjoblvimage_jobinfo, iv_item_searchjoblvimage_poster;
        RelativeLayout rl_item_searchjoblvimage_click;
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


}
