package com.hr.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import com.hr.ui.utils.OnItemClick;
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
    private List<FunctionBean> functionBeenList3=new ArrayList<>();
    private List<FunctionBean> selectFunctionBeenList=new ArrayList<>();
    private MyFunctionFirstAdapter functionFirstAdapter;
    private String selectFirstFunctionId;
    private int num;
    private MyFuncitionSecondAdapter funcitionSecondAdapter;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    if(functionBeenList1!=null&&functionBeenList1.size()!=0) {
                        functionFirstAdapter.setFunctionBeen(functionBeenList1);
                        functionselectListview.setAdapter(functionFirstAdapter);
                    }
                    functionFirstAdapter.setOnItemClick(new OnItemClick() {
                        @Override
                        public void ItemClick(View view, int position) {
                            if (position != 0) {
                                selectFirstFunctionId = functionBeenList1.get(position).getId();
                                if(num<3) {
                                    if (functionBeenList1.get(position).isSelect() == false) {
                                        functionBeenList1.get(position).setSelect(true);
                                    }
                                    for(int i=1;i<functionBeenList1.size();i++) {
                                            for(int j=0;j<selectFunctionBeenList.size();j++) {
                                                if (functionBeenList1.get(i).isSelect()==true||functionBeenList1.get(i).getId().substring(0, 3).equals(selectFunctionBeenList.get(j).getId().substring(0, 3))){
                                                    functionBeenList1.get(i).setSelect(true);
                                                }else{
                                                    functionBeenList1.get(i).setSelect(false);
                                                }
                                            }
                                    }

                                }
                                functionFirstAdapter.notifyDataSetChanged();
                                functionBeenList3 = new ArrayList<FunctionBean>();
                                for (int i = 0; i < functionBeenList2.size(); i++) {
                                    if (selectFirstFunctionId.substring(0, 3).equals(functionBeenList2.get(i).getId().substring(0, 3))) {
                                        functionBeenList3.add(functionBeenList2.get(i));
                                    }
                                }
                                Message message = Message.obtain();
                                message.what = 2;
                                handler.sendMessage(message);

                            }
                        }
                    });
                    break;
                case 2:
                    if(functionBeenList3!=null&&functionBeenList3.size()!=0) {
                        Log.i("职能第二个",functionBeenList3.toString());
                        funcitionSecondAdapter.setFunctionBean(functionBeenList3);
                        functionselectListview2.setAdapter(funcitionSecondAdapter);
                    }
                    funcitionSecondAdapter.setOnItemClick(new OnItemClick() {
                        @Override
                        public void ItemClick(View view, int position) {
                            if(functionBeenList3.get(position).isSelect()==false) {
                                if(num<3) {
                                    functionBeenList3.get(position).setSelect(true);
                                    selectFunctionBeenList.add(functionBeenList3.get(position));
                                    num++;
                                }

                            }else{
                                if(num>0) {
                                    num--;
                                    selectFunctionBeenList.remove(functionBeenList3.get(position));
                                    functionBeenList3.get(position).setSelect(false);
                                }
                            }
                            funcitionSecondAdapter.notifyDataSetChanged();
                        }
                    });
                    break;
            }
        }
    };
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
        functionFirstAdapter = new MyFunctionFirstAdapter(this);
        funcitionSecondAdapter=new MyFuncitionSecondAdapter(this);
        SharedPreferences sp = getSharedPreferences(
                Constants.PREFS_NAME, Context.MODE_PRIVATE);
        int industryId = sp.getInt(Constants.INDUSTRY,
                Constants.INDUSTRY_BUILD_ID);
        JSONArray jobJSONArray = null;
        try {
            jobJSONArray = NetService.getJobAsJSONArray(this,
                    "job.json", String.valueOf(industryId));
            functionBeenList= GetJssonList.getFunctionList(jobJSONArray);
            functionBeenList2=GetJssonList.getFunctionList(jobJSONArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FunctionBean functionBean1=new FunctionBean();
        functionBean1.setSelect(false);
        functionBean1.setName("全部职能");
        functionBean1.setId("0");
        functionBeenList1.add(functionBean1);
        for(int i=0;i<functionBeenList.size();i++){
            String id=functionBeenList.get(i).getId();
            String s=id.substring(id.length()-3);
            if(s.equals("000")){
                functionBeenList1.add(functionBeenList.get(i));
            }
        }
        Message message=new Message();
        message.what=1;
        handler.sendMessage(message);

    }

    private void initView() {
        LinearLayoutManager manager1=new LinearLayoutManager(this);
        LinearLayoutManager manager2=new LinearLayoutManager(this);
        manager1.setOrientation(LinearLayoutManager.VERTICAL);
        manager2.setOrientation(LinearLayoutManager.VERTICAL);
        functionselectListview.setLayoutManager(manager1);
        functionselectListview2.setLayoutManager(manager2);
        functionselectListview.addItemDecoration(new SpacesItemDecoration(2));
        functionselectListview2.addItemDecoration(new SpacesItemDecoration(2));
    }
}
