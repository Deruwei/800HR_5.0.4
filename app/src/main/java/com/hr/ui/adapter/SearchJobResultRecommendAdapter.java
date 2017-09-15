package com.hr.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class SearchJobResultRecommendAdapter extends RecyclerView.Adapter<SearchJobResultRecommendAdapter.MyViewHolder> {
    /* private static final String TAG = "SearchJobResultRecommendAdapter";*/
    private  ArrayList<HashMap<String, Object>> dataList;
    private static Context mContext;
  /*  private static HashMap<Integer, Boolean> isSelected;*/

 /*   private int is_expire;// 是否过期的标识 0：未过期 1：过期
    private int is_favourite;// 是否已经收藏 0：未收藏 1：收藏
    private int is_apply;// 是否投递过该职位 0：未投递 1：已投递*/
    /**
     * 本地缓存图片名字
     */
    private DownLoadImg dLoadImg;
    private String comLogoFileName;
    private Bitmap drawable;
    private String posterPath;
    private SharedPreferencesUtils sUtils;
    /**
     * 带海报图片的Item
     */
    final int TYPE_1 = 0;
    /**
     * 纯文字的Item
     */
    final int TYPE_2 = 1;

    public  void setDataList(ArrayList<HashMap<String, Object>> dataList) {
        this.dataList = dataList;
    }

    public SearchJobResultRecommendAdapter(Context context) {
        this.mContext = context;
        sUtils = new SharedPreferencesUtils(mContext);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_2) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.item_searchjob_result, parent, false);
            return new ViewHolderInfo(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_searchjob_resultimage, parent, false);
            return new ViewHolderImage(view);
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
            ((ViewHolderImage) holder).cbItemSearchjobimageSelect.setVisibility(View.GONE);
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
        }
        if (holder instanceof ViewHolderInfo) {
            ((ViewHolderInfo) holder).tvItemSearchjobJobname.setText(dataList.get(position).get("job_name").toString());
            ((ViewHolderInfo) holder).tvItemSearchjobCityname.setText(dataList.get(position).get("workplace").toString());
            ((ViewHolderInfo) holder).tvItemSearchjobCompanyname.setText(dataList.get(position).get("enterprise_name").toString());
            ((ViewHolderInfo) holder).tvItemSearchjoblvReleasetime.setText(dataList.get(position).get("issue_date").toString());
            ((ViewHolderInfo) holder).rlItemSearchjoblvClick.setTag(dataList.get(position).get("job_id").toString());
            ((ViewHolderInfo) holder).cbItemSearchjobSelect.setVisibility(View.GONE);

            ((ViewHolderInfo) holder).rlItemSearchjoblvClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position < dataList.size()) {
                        String job_id =(String) v.getTag();
                        // Toast.makeText(context, "文本信息" + url, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, PostParticularsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("job_id", job_id);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                }
            });
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


    static class ViewHolderInfo extends MyViewHolder {
        @Bind(R.id.cb_item_searchjob_select)
        CheckBox cbItemSearchjobSelect;
        @Bind(R.id.tv_item_searchJob_jobName)
        TextView tvItemSearchjobJobname;
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

    static class ViewHolderImage extends MyViewHolder {
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
            ButterKnife.bind(this,itemView);
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
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
            String TalkPath = null;
            DownImg fileDownload = new DownImg();
            TalkPath = fileDownload.DownLoadFile(params[0], "/800HR/Poster");
            //Log.i("PosterActivity", "poster path=" + TalkPath);
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
