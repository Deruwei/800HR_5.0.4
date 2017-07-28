package com.hr.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hr.ui.R;
import com.hr.ui.model.Industry;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;

/**
 * 作者：Colin
 * 日期：2016/1/6 17:13
 * 邮箱：bestxt@qq.com
 */
public class FindAdapter extends BaseAdapter {

    /**
     * UIL配置信息
     */
    private DisplayImageOptions options;
    private Context context;
    private ArrayList<Industry> data;
    // 显示图片信息
    private int pagerType;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private LayoutInflater mInflater;

    public FindAdapter(Context context, ArrayList<Industry> data, int type) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
        this.pagerType = type;
        initUIL();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(
                    R.layout.item_findactivity_lv, null);
            holder.iv = (ImageView) convertView
                    .findViewById(R.id.famous_enterprise_niv);
            holder.title = (TextView) convertView
                    .findViewById(R.id.famous_enterprise_title);
            holder.type = (TextView) convertView
                    .findViewById(R.id.famous_enterprise_type);
            holder.number = (TextView) convertView
                    .findViewById(R.id.famous_enterprise_number);
            holder.ll_enterprise_number1 = (LinearLayout) convertView.findViewById(R.id.ll_enterprise_number1);
            holder.ll_enterprise_type1 = (LinearLayout) convertView.findViewById(R.id.ll_enterprise_type1);
//            setLayoutParams(holder.iv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Industry industry = data.get(position);
        // 获取服务器图片，处理图片，设置图片
        String url = industry.getPic_path();
        if (url == null || url.equals("")) {
            url = industry.getPic_s_path();
        }
        // 请求的url不能为空
        if (url != null && !url.equals("")) {
            try {
                imageLoader.displayImage(url, holder.iv, options);
            } catch (Exception e) {
                // TODO: handle exception
            }
        } else {
        }
        if (pagerType == 1) {
            // 设置公司名称、公司性质、公司规模
            holder.title.setText(industry.getTitle());
            holder.type.setText(industry.getCompany_type());
            holder.number.setText(industry.getStuff_munber());
        } else {
            holder.title.setText(industry.getTitle());
            holder.ll_enterprise_number1.setVisibility(View.GONE);
            holder.ll_enterprise_type1.setVisibility(View.GONE);
        }
//            convertView.setBackgroundResource(R.drawable.list_selector);
        return convertView;
    }

    static class ViewHolder {

        ImageView iv;// 公司广告图片
        TextView title;// 公司名称
        TextView type;// 公司性质
        TextView number;// 公司规模
        LinearLayout ll_enterprise_number1;
        LinearLayout ll_enterprise_type1;

    }

//    /**
//     * 设置控件的宽高
//     *
//     * @param view
//     * @return
//     */
//    private ViewGroup.LayoutParams setLayoutParams(View view) {
//        ViewGroup.LayoutParams params = view.getLayoutParams();
//        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
//        // 控件和屏幕边缘的距离
//        int interval = DensityUtils.dp2px(context, 24);
//        params.width = metrics.widthPixels - interval;
//        params.height = (metrics.widthPixels - interval) * 210 / (964 + interval) + interval;
//        return params;
//    }

    /**
     * 初始化UIL
     */
    private void initUIL() {
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(100))
                .build();
    }
}
