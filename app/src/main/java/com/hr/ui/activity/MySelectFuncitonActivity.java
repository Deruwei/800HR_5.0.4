package com.hr.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hr.ui.R;
import com.hr.ui.adapter.MyFuncitionSecondAdapter;
import com.hr.ui.adapter.MyFunctionFirstAdapter;
import com.hr.ui.bean.FunctionBean;
import com.hr.ui.config.Constants;
import com.hr.ui.utils.GetJssonList;
import com.hr.ui.utils.SpacesItemDecoration;
import com.hr.ui.utils.netutils.NetService;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wdr on 2017/8/10.
 */

public class MySelectFuncitonActivity extends BaseActivity {

    @Bind(R.id.iv_selectfunction_back)
    ImageView ivSelectfunctionBack;
    @Bind(R.id.functionselect_title)
    TextView functionselectTitle;
    @Bind(R.id.tv_selectfunction_save)
    TextView tvSelectfunctionSave;
    @Bind(R.id.relativeLayout3)
    RelativeLayout relativeLayout3;
    @Bind(R.id.funtionselect_selectedinfo)
    TextView funtionselectSelectedinfo;
    @Bind(R.id.iv_seeSelectFunction)
    ImageView ivSeeSelectFunction;
    @Bind(R.id.funtionselect_showmessage)
    LinearLayout funtionselectShowmessage;
    @Bind(R.id.functionselect_showinforlayout)
    LinearLayout functionselectShowinforlayout;
    @Bind(R.id.functionselect_listview)
    RecyclerView functionselectListview;
    @Bind(R.id.functionselect_listview2)
    RecyclerView functionselectListview2;
    private List<FunctionBean> functionBeenList=new ArrayList<>();
    //一级菜单的数据
    private List<FunctionBean> functionBeenList1=new ArrayList<>();
    //二级菜单的数据
    private List<FunctionBean> functionBeenList2=new ArrayList<>();
    private MyFunctionFirstAdapter functionFirstAdapter;
    private MyFuncitionSecondAdapter funcitionSecondAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_function_bg);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        int flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        ButterKnife.bind(this);
        initView();
        getData();
    }

    private void getData() {
        SharedPreferences sp = getSharedPreferences(
                Constants.PREFS_NAME, Context.MODE_PRIVATE);
        int industryId = sp.getInt(Constants.INDUSTRY,
                Constants.INDUSTRY_BUILD_ID);
        JSONArray jobJSONArray = null;
        try {
            jobJSONArray = NetService.getJobAsJSONArray(this,
                    "job.json", String.valueOf(industryId));
            functionBeenList= GetJssonList.getFunctionList(jobJSONArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i=0;i<functionBeenList.size();i++){
            String id=functionBeenList.get(i).getId();
            String s=id.substring(id.length()-3);
            if(s.equals("000")){
                functionBeenList1.add(functionBeenList.get(i));
            }else{
                functionBeenList2.add(functionBeenList.get(i));



            }
        }
        Message message=new Message();
        message.obj=functionBeenList1;

    }

    private void initView() {
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        functionselectListview.setLayoutManager(manager);
        functionselectListview2.setLayoutManager(manager);
        functionselectListview.addItemDecoration(new SpacesItemDecoration(1));
        functionselectListview2.addItemDecoration(new SpacesItemDecoration(1));
    }
}
