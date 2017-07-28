package com.hr.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.model.Invitedinfo;
import com.hr.ui.utils.netutils.AsyncPersonCenterInvited;
import com.hr.ui.utils.netutils.NetUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * 人事经理来信
 */
public class CompanyEmailListActivity extends BaseActivity implements OnClickListener,
        OnItemClickListener {
    ListView listview;
    private MyBaseAdpter myBaseAdpter;
    private String page_nums = "20";// 总记录数
    private ArrayList<Invitedinfo> listInvitedinfos;
    private ImageView iv_companyemail_list_back;
    private Context mContext = CompanyEmailListActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        MobclickAgent.onEvent(this, "cv-interview");
        setContentView(R.layout.activity_company_email_list);
        listview = (ListView) findViewById(R.id.listview_search);
        init();
    }

    private void init() {
        // TODO Auto-generated method stub
        listInvitedinfos = new ArrayList<Invitedinfo>();
        myBaseAdpter = new MyBaseAdpter(mContext, listInvitedinfos);
        listview.setAdapter(myBaseAdpter);
        listview.setOnItemClickListener(this);
        iv_companyemail_list_back = (ImageView) findViewById(R.id.iv_companyemail_list_back);
        iv_companyemail_list_back.setOnClickListener(this);
        new AsyncPersonCenterInvited(mContext, listInvitedinfos, myBaseAdpter, listview).execute("user_stow.invited", "0", "50");
    }

    private class MyBaseAdpter extends BaseAdapter {
        ArrayList<Invitedinfo> listInvitedInfos;
        Context context;

        public MyBaseAdpter(Context context, ArrayList<Invitedinfo> listInvitedinfos) {
            this.context = context;
            this.listInvitedInfos = listInvitedinfos;
        }

        @Override
        public int getCount() {
            return listInvitedInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return listInvitedInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyHolder myHolder = null;
            if (convertView == null) {
                myHolder = new MyHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.wholookme_listview, null);
                convertView.setTag(myHolder);
                myHolder.company_name = (TextView) convertView.findViewById(R.id.company_name);
                myHolder.browsed_time = (TextView) convertView.findViewById(R.id.company_time);
                myHolder.job_place = (TextView) convertView.findViewById(R.id.company_scale);
            } else {
                myHolder = (MyHolder) convertView.getTag();
            }
            if (listInvitedInfos.get(position).getInvited_title().trim().equals("")) {
                myHolder.company_name.setText("（无主题）");
            } else {
                myHolder.company_name.setText(listInvitedInfos.get(position).getInvited_title());
            }
            myHolder.browsed_time.setText("接收时间:" + listInvitedInfos.get(position).getInvited_time());
            myHolder.job_place.setText(listInvitedInfos.get(position).getEnterprise_name());
            return convertView;
        }

        private final class MyHolder {
            TextView company_name, browsed_time, job_place;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_companyemail_list_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (!NetUtils.checkNet(this)) {
            Toast.makeText(this, R.string.no_network, Toast.LENGTH_SHORT).show();
            return;
        }
        // 加载工作详细页
        if (listInvitedinfos.size() != 0) {
            Intent intent = new Intent(mContext, CompanyEmailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("invitedInfo", listInvitedinfos.get(arg2));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
