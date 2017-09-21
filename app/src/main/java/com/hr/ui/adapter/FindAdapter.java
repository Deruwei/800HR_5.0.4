package com.hr.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hr.ui.R;
import com.hr.ui.model.Industry;
import com.hr.ui.utils.OnItemClick;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：Colin
 * 日期：2016/1/6 17:13
 * 邮箱：bestxt@qq.com
 */
public class FindAdapter extends RecyclerView.Adapter<FindAdapter.MyViewHolder> {
    /**
     * UIL配置信息
     */
    private DisplayImageOptions options;
    private Context context;
    private ArrayList<Industry> data;
    private OnItemClick onItemClick;
    // 显示图片信息
    private int pagerType;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public void setData(ArrayList<Industry> data) {
        this.data = data;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public FindAdapter(Context context, int type) {
        this.context = context;
        this.pagerType = type;
        initUIL();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_findactivity_lv, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Industry industry = data.get(position);
        // 获取服务器图片，处理图片，设置图片
        String url = industry.getPic_path();
        if (url == null || url.equals("")) {
            url = industry.getPic_s_path();
        }
        // 请求的url不能为空
        if (url != null && !url.equals("")) {
            try {
                imageLoader.displayImage(url, holder.famousEnterpriseNiv, options);
            } catch (Exception e) {
                // TODO: handle exception
            }
        } else {
        }
        if (pagerType == 1) {
            // 设置公司名称、公司性质、公司规模
            holder.famousEnterpriseTitle.setText(industry.getTitle());
            holder.famousEnterpriseType.setText(industry.getCompany_type());
            holder.famousEnterpriseNumber.setText(industry.getStuff_munber());
        } else {
            holder.famousEnterpriseTitle.setText(industry.getTitle());
            holder.llEnterpriseNumber1.setVisibility(View.GONE);
            holder.llEnterpriseType1.setVisibility(View.GONE);
        }
        if(onItemClick!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.ItemClick(view,position);
                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.famous_enterprise_niv)
        ImageView famousEnterpriseNiv;
        @Bind(R.id.famous_enterprise_title)
        TextView famousEnterpriseTitle;
        @Bind(R.id.famous_enterprise_type)
        TextView famousEnterpriseType;
        @Bind(R.id.ll_enterprise_type1)
        LinearLayout llEnterpriseType1;
        @Bind(R.id.famous_enterprise_number)
        TextView famousEnterpriseNumber;
        @Bind(R.id.ll_enterprise_number1)
        LinearLayout llEnterpriseNumber1;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
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
