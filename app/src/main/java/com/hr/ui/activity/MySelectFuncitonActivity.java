package com.hr.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.adapter.ClassifyMainAdapter;
import com.hr.ui.adapter.ClassifyMoreAdapter;
import com.hr.ui.bean.FunctionBean;
import com.hr.ui.config.Constants;
import com.hr.ui.fragment.PagerPostSearchFragment;
import com.hr.ui.fragment.RecommendJobFragment;
import com.hr.ui.utils.GetJssonList;
import com.hr.ui.utils.netutils.NetService;
import com.hr.ui.view.MyFlowLayout;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    MyFlowLayout functionselectShowinforlayout;
    @Bind(R.id.functionselect_listview)
    ListView functionselectListview;
    @Bind(R.id.functionselect_listview2)
    ListView functionselectListview2;
    private FunctionBean selectFunction=new FunctionBean();
    private List<FunctionBean> functionBeenList = new ArrayList<>();
    private List<FunctionBean> deleteFunction=new ArrayList<>();
    //一级菜单的数据
    private List<FunctionBean> functionBeenList1 = new ArrayList<>();
    //二级菜单的数据
    private List<FunctionBean> functionBeenList2 = new ArrayList<>();
    private List<FunctionBean> functionBeenList3 = new ArrayList<>();
    private List<FunctionBean> selectFunctionBeenList = new ArrayList<>();
    private ClassifyMainAdapter functionFirstAdapter;
    private String selectFirstFunctionId;
    private int num;
    private String fitter;
    private boolean isShow;
    private boolean isRefresh;
    private ClassifyMoreAdapter funcitionSecondAdapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (functionBeenList1 != null && functionBeenList1.size() != 0) {
                        functionFirstAdapter=new ClassifyMainAdapter(MySelectFuncitonActivity.this,functionBeenList1);
                        functionselectListview.setAdapter(functionFirstAdapter);
                    }
                    functionselectListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            selectFunction=functionBeenList1.get(position);
                            isRefresh=true;
                            if (position != 0) {
                                selectFirstFunctionId = functionBeenList1.get(position).getId();
                                //当数量为0时，点击每一项只会改变图片和颜色
                                if (num == 0) {
                                    for (int i = 1; i < functionBeenList1.size(); i++) {
                                        if (i != position) {
                                            functionBeenList1.get(i).setSelect(false);
                                            functionBeenList1.get(i).setShowImage(false);
                                        } else {
                                            functionBeenList1.get(position).setSelect(true);
                                            functionBeenList1.get(position).setShowImage(true);
                                        }
                                    }
                                }

                                //数量大于0的时候，点击每一项的
                                if (num > 0) {
                                    getFunctionFirstListBean(functionBeenList.get(position));
                                    functionBeenList1.get(position).setSelect(true);
                                    functionBeenList1.get(position).setShowImage(true);
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
                    if (selectFunctionBeenList != null && selectFunctionBeenList.size() != 0) {
                        for (int i = 0; i < functionBeenList3.size(); i++) {
                            for (int j = 0; j < selectFunctionBeenList.size(); j++) {
                                if (functionBeenList3.get(i).getId().equals(selectFunctionBeenList.get(j).getId())) {
                                    functionBeenList3.remove(i);
                                    functionBeenList3.add(i, selectFunctionBeenList.get(j));
                                }
                            }
                        }
                    }
                    if (functionBeenList3 != null && functionBeenList3.size() != 0) {
                        funcitionSecondAdapter=new ClassifyMoreAdapter(MySelectFuncitonActivity.this,functionBeenList3);
                        functionselectListview2.setAdapter(funcitionSecondAdapter);
                    }
                    functionselectListview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            if (functionBeenList3.get(position).isSelect() == false) {
                             /*   if(fitter.equals("recommend")) {
                                    RecommendJobFragment.setFunctionSelectMap(selectFunctionBeenList);
                                }else if(fitter.equals("post")){
                                    PagerPostSearchFragment.setFunctionSelectMap(selectFunctionBeenList);

                                }else if(fitter.equals("creatjoborder")){
                                    CreateResumeJobOrderActivity.getInstance().setFunctionSelected(selectFunctionBeenList);
                                }else if(fitter.equals("resumeJobOrder")){
                                    ResumeJobOrderActivity.getInstance().setFunctionSelected(selectFunctionBeenList);
                                }else if(fitter.equals("subscription")){
                                    SubscriptionActivity.subscriptionActivity.setFunctionSelected(selectFunctionBeenList);
                                }*/
                             if(fitter.equals("recommend")||fitter.equals("post")||fitter.equals("subscription")) {
                                 if (num < 3) {

                                     if(position==0){
                                        for(int i=1;i<functionBeenList3.size();i++){
                                            functionBeenList3.get(i).setSelect(false);
                                        }
                                        for(int i=0;i<selectFunctionBeenList.size();i++){
                                            if(selectFunctionBeenList.get(i).getId().substring(0,3).equals(functionBeenList3.get(0).getId().substring(0,3))){
                                                deleteFunction.add(selectFunctionBeenList.get(i));


                                            }
                                        }
                                         selectFunctionBeenList.removeAll(deleteFunction);
                                         for(int i=0;i<deleteFunction.size();i++) {
                                             removeView(deleteFunction.get(i));
                                         }
                                         num=selectFunctionBeenList.size();
                                         num++;
                                         addView(functionBeenList3.get(0));
                                         selectFunctionBeenList.add(functionBeenList3.get(0));
                                         functionBeenList3.get(0).setSelect(true);
                                     }else {
                                            if(functionBeenList3.get(0).isSelect()==true) {
                                                removeView(functionBeenList3.get(0));
                                                selectFunctionBeenList.remove(functionBeenList3.get(0));
                                                functionBeenList3.get(0).setSelect(false);

                                            }
                                            num=selectFunctionBeenList.size();
                                             functionBeenList3 .get(position).setSelect(true);
                                             selectFunctionBeenList.add(functionBeenList3.get(position));
                                             addView(functionBeenList3.get(position));
                                             num++;
                                     }
                                     ivSeeSelectFunction.setImageResource(R.mipmap.shangjiantou);
                                     functionselectShowinforlayout.setVisibility(View.VISIBLE);
                                     isShow = true;
                                     setSelectCount(num);
                                 } else {
                                     boolean f=false;
                                     if(functionBeenList3.get(position).getId().contains("000")) {
                                         for (int i = 0; i < selectFunctionBeenList.size(); i++) {
                                             if (functionBeenList3.get(position).getId().substring(0, 3).equals(selectFunctionBeenList.get(i).getId().substring(0, 3))) {
                                                 f = true;
                                             }
                                         }
                                         if (f == true) {
                                             deleteFunction = new ArrayList<FunctionBean>();
                                             for (int i = 0; i < selectFunctionBeenList.size(); i++) {
                                                 if (selectFunctionBeenList.get(i).getId().substring(0, 3).equals(functionBeenList3.get(position).getId().substring(0, 3))) {
                                                     deleteFunction.add(selectFunctionBeenList.get(i));

                                                 }
                                             }
                                             selectFunctionBeenList.removeAll(deleteFunction);
                                             for (int i = 0; i < deleteFunction.size(); i++) {
                                                 removeView(deleteFunction.get(i));
                                             }
                                             num = selectFunctionBeenList.size();
                                             num++;
                                             addView(functionBeenList3.get(0));
                                             selectFunctionBeenList.add(functionBeenList3.get(0));
                                             functionBeenList3.get(0).setSelect(true);
                                             setSelectCount(num);
                                         }else {
                                             Toast.makeText(MySelectFuncitonActivity.this, "最多只能选择3个职位", Toast.LENGTH_SHORT).show();
                                         }
                                     }else{
                                         for (int i = 0; i < selectFunctionBeenList.size(); i++) {
                                             if (functionBeenList3.get(position).getId().substring(0, 3).equals(selectFunctionBeenList.get(i).getId().substring(0, 3))&&selectFunctionBeenList.get(i).getId().contains("000")) {
                                                 f = true;
                                             }
                                         }
                                         if(f==true){
                                             if(functionBeenList3.get(0).isSelect()==true) {
                                                 removeView(functionBeenList3.get(0));
                                                 selectFunctionBeenList.remove(functionBeenList3.get(0));
                                                 functionBeenList3.get(0).setSelect(false);

                                             }
                                             num=selectFunctionBeenList.size();
                                             functionBeenList3 .get(position).setSelect(true);
                                             selectFunctionBeenList.add(functionBeenList3.get(position));
                                             addView(functionBeenList3.get(position));
                                             num++;
                                             setSelectCount(num);
                                         }
                                         else {
                                             Toast.makeText(MySelectFuncitonActivity.this, "最多只能选择3个职位", Toast.LENGTH_SHORT).show();
                                         }
                                     }
                                 }
                             }else{
                                 if (num < 5) {

                                     if(position==0){
                                         for(int i=1;i<functionBeenList3.size();i++){
                                             functionBeenList3.get(i).setSelect(false);
                                         }
                                         for(int i=0;i<selectFunctionBeenList.size();i++){
                                             if(selectFunctionBeenList.get(i).getId().substring(0,3).equals(functionBeenList3.get(0).getId().substring(0,3))){
                                                 deleteFunction.add(selectFunctionBeenList.get(i));

                                             }
                                         }
                                         selectFunctionBeenList.removeAll(deleteFunction);
                                         for(int i=0;i<deleteFunction.size();i++) {
                                             removeView(deleteFunction.get(i));
                                         }
                                         num=selectFunctionBeenList.size();
                                         num++;
                                         addView(functionBeenList3.get(0));
                                         selectFunctionBeenList.add(functionBeenList3.get(0));
                                         functionBeenList3.get(0).setSelect(true);
                                     }else {
                                         if(functionBeenList3.get(0).isSelect()==true) {
                                             removeView(functionBeenList3.get(0));
                                             selectFunctionBeenList.remove(functionBeenList3.get(0));
                                             functionBeenList3.get(0).setSelect(false);

                                         }
                                         num=selectFunctionBeenList.size();
                                         functionBeenList3 .get(position).setSelect(true);
                                         selectFunctionBeenList.add(functionBeenList3.get(position));
                                         addView(functionBeenList3.get(position));
                                         num++;
                                     }
                                     ivSeeSelectFunction.setImageResource(R.mipmap.shangjiantou);
                                     functionselectShowinforlayout.setVisibility(View.VISIBLE);
                                     isShow = true;
                                     setSelectCount(num);
                                 } else {
                                     boolean f=false;
                                     if(functionBeenList3.get(position).getId().contains("000")) {
                                         for (int i = 0; i < selectFunctionBeenList.size(); i++) {
                                             if (functionBeenList3.get(position).getId().substring(0, 3).equals(selectFunctionBeenList.get(i).getId().substring(0, 3))) {
                                                 f = true;
                                             }
                                         }
                                         if (f == true) {
                                             deleteFunction = new ArrayList<FunctionBean>();
                                             for (int i = 0; i < selectFunctionBeenList.size(); i++) {
                                                 if (selectFunctionBeenList.get(i).getId().substring(0, 3).equals(functionBeenList3.get(position).getId().substring(0, 3))) {
                                                     deleteFunction.add(selectFunctionBeenList.get(i));

                                                 }
                                             }
                                             selectFunctionBeenList.removeAll(deleteFunction);
                                             for (int i = 0; i < deleteFunction.size(); i++) {
                                                 removeView(deleteFunction.get(i));
                                             }
                                             num = selectFunctionBeenList.size();
                                             num++;
                                             addView(functionBeenList3.get(0));
                                             selectFunctionBeenList.add(functionBeenList3.get(0));
                                             functionBeenList3.get(0).setSelect(true);
                                             setSelectCount(num);
                                         }else {
                                             Toast.makeText(MySelectFuncitonActivity.this, "最多只能选择5个职位", Toast.LENGTH_SHORT).show();
                                         }
                                     }else{
                                         for (int i = 0; i < selectFunctionBeenList.size(); i++) {
                                             if (functionBeenList3.get(position).getId().substring(0, 3).equals(selectFunctionBeenList.get(i).getId().substring(0, 3))&&selectFunctionBeenList.get(i).getId().contains("000")) {
                                                 f = true;
                                             }
                                         }
                                         if(f==true){
                                             if(functionBeenList3.get(0).isSelect()==true) {
                                                 removeView(functionBeenList3.get(0));
                                                 selectFunctionBeenList.remove(functionBeenList3.get(0));
                                                 functionBeenList3.get(0).setSelect(false);

                                             }
                                             num=selectFunctionBeenList.size();
                                             functionBeenList3 .get(position).setSelect(true);
                                             selectFunctionBeenList.add(functionBeenList3.get(position));
                                             addView(functionBeenList3.get(position));
                                             num++;
                                             setSelectCount(num);
                                         }
                                         else {
                                             Toast.makeText(MySelectFuncitonActivity.this, "最多只能选择5个职位", Toast.LENGTH_SHORT).show();
                                         }
                                     }
                                 }
                             }

                            } else {
                                if (num > 0) {
                                    num--;
                                    selectFunctionBeenList.remove(functionBeenList3.get(position));
                                    functionBeenList3.get(position).setSelect(false);
                                    removeView(functionBeenList3.get(position));
                                    setSelectCount(num);
                                    if(num==0){
                                        ivSeeSelectFunction.setImageResource(R.mipmap.xiajiantou);
                                        functionselectShowinforlayout.setVisibility(View.GONE);
                                    }
                                }
                            }
                            //*Log.i("functionList的参数",functionBeenList1.toString());*//*
                            funcitionSecondAdapter.notifyDataSetChanged();
                        }
                    });

                    break;
            }
        }
    };
    private void getFunctionFirstListBean(FunctionBean functionBean) {
        for (int i = 1; i < functionBeenList1.size(); i++) {
            if(selectFunction==functionBeenList1.get(i)){
                functionBeenList1.get(i).setSelect(true);
                functionBeenList1.get(i).setShowImage(true);
            }else {
                functionBeenList1.get(i).setSelect(false);
                functionBeenList1.get(i).setShowImage(false);
            }
        }
        for (int i = 1; i < functionBeenList1.size(); i++) {
            for (int j = 0; j < selectFunctionBeenList.size(); j++) {
                    if (functionBeenList1.get(i).getId().substring(0, 3).equals(selectFunctionBeenList.get(j).getId().substring(0, 3))) {

                            functionBeenList1.get(i).setSelect(true);

                    }
            }
        }
    }
    private void getFunctionSecondListBean(){
            for (int i = 0; i < functionBeenList3.size(); i++) {
                functionBeenList3.get(i).setSelect(false);
                for (int j = 0; j < selectFunctionBeenList.size(); j++) {
                    if (functionBeenList3.get(i).equals(selectFunctionBeenList.get(j))) {
                       functionBeenList3.get(i).setSelect(true);
                    }
                }
            }

        /*Log.i("选择3",functionBeenList3.toString());*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_function_bg);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        int flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        getWindow().addFlags(flags);
        ButterKnife.bind(this);
        initView();
        getData();
    }

    private void getData() {
         fitter = getIntent().getStringExtra("filter");
        final String id = getIntent().getStringExtra("id");
        final String value = getIntent().getStringExtra("value");
        selectFunctionBeenList = (List<FunctionBean>) getIntent().getSerializableExtra("selectMap");
        if(selectFunctionBeenList!=null&&selectFunctionBeenList.size()!=0){
            num=selectFunctionBeenList.size();
            ivSeeSelectFunction.setImageResource(R.mipmap.shangjiantou);
            functionselectShowinforlayout.setVisibility(View.VISIBLE);
            isShow=true;
        }
        setSelectCount(num);
        for (int i = 0; i < selectFunctionBeenList.size(); i++) {
            addView(selectFunctionBeenList.get(i));
        }
        SharedPreferences sp = getSharedPreferences(
                Constants.PREFS_NAME, Context.MODE_PRIVATE);
        int industryId = sp.getInt(Constants.INDUSTRY,
                Constants.INDUSTRY_BUILD_ID);
        JSONArray jobJSONArray = null;
        try {
            jobJSONArray = NetService.getJobAsJSONArray(this,
                    "job.json", String.valueOf(industryId));
            functionBeenList = GetJssonList.getFunctionList(jobJSONArray);
            functionBeenList2 = GetJssonList.getFunctionList(jobJSONArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FunctionBean functionBean1 = new FunctionBean();
        functionBean1.setSelect(false);
        functionBean1.setName("全部职能");
        functionBean1.setId("0");
        functionBeenList1.add(functionBean1);
        for (int i = 0; i < functionBeenList.size(); i++) {
            String ids = functionBeenList.get(i).getId();
            String s = ids.substring(ids.length() - 3);
            if (s.equals("000")) {
                functionBeenList1.add(functionBeenList.get(i));
            }
        }
        //Log.i("选择diyici",selectFunctionBeenList.toString());
        for (int i = 1; i < functionBeenList1.size(); i++) {
            for (int j = 0; j < selectFunctionBeenList.size(); j++) {
                if (selectFunctionBeenList.get(j).getId().length() > 2) {

                    if (selectFunctionBeenList.get(j).getId().substring(0, 3).equals(functionBeenList1.get(i).getId().substring(0, 3))) {
                        functionBeenList1.get(i).setSelect(true);
                    }
                }
            }
        }
        Message message = new Message();
        message.what = 1;
        handler.sendMessage(message);

    }

    private void initView() {
      /*  LinearLayoutManager manager1 = new LinearLayoutManager(this);
        LinearLayoutManager manager2 = new LinearLayoutManager(this);
        manager1.setOrientation(LinearLayoutManager.VERTICAL);
        manager2.setOrientation(LinearLayoutManager.VERTICAL);
        functionselectListview.setLayoutManager(manager1);
      *//*  functionselectListview2.setLayoutManager(manager2);*//*
        functionselectListview.addItemDecoration(new SpacesItemDecoration(2));
        functionselectListview.setFocusable(false);*/
       /* functionselectListview2.addItemDecoration(new SpacesItemDecoration(2));
        functionselectListview2.setFocusable(false);*/
    }

    /**
     * 添加已选职能视图
     *
     * @param functionBean
     */
    private void addView(final FunctionBean functionBean) {
        String keyString = functionBean.getId();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 15;
        params.topMargin = 12;
        params.bottomMargin = 12;
        params.rightMargin = 15;
        final TextView tv = (TextView) LayoutInflater.from(this).inflate(
                R.layout.textview_selected, null, false);
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(6, 4, 6, 4);
        if ("0".equalsIgnoreCase(keyString)
                || "000".contains(keyString.subSequence(keyString.length() - 3,
                keyString.length()))) {
            tv.setTextSize(18f);
            // 加粗
            // TextPaint tpaint = tv.getPaint();
            // tpaint.setFakeBoldText(true);
        }
        tv.setText(functionBean.getName());
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFunctionBeenList.remove(functionBean);
                for(int i=0;i<functionBeenList2.size();i++){
                    if(functionBean.getId().equals(functionBeenList2.get(i).getId())){
                        functionBeenList2.get(i).setSelect(false);
                    }
                }
                num--;
                if (num==0){
                    ivSeeSelectFunction.setImageResource(R.mipmap.xiajiantou);
                    functionselectShowinforlayout.setVisibility(View.GONE);
                }
                //Log.i("选择",selectFunctionBeenList.toString());
                functionselectShowinforlayout
                        .removeView(functionselectShowinforlayout
                                .findViewWithTag(functionBean.getId()));
        /*Log.i("选择2",selectFunctionBeenList.toString());*/
                getFunctionSecondListBean();
                getFunctionFirstListBean(functionBean);
                functionFirstAdapter.notifyDataSetChanged();
                if(isRefresh==true) {
                    funcitionSecondAdapter.notifyDataSetChanged();
                }
                setSelectCount(num);
            }
        });
        // Animation animationadd = AnimationUtils.loadAnimation(this,
        // R.anim.slide_in_left);
        // Interpolator interpolator = new CubicInterpolator(Type.IN);
        // animationadd.setInterpolator(interpolator);
        // tv.setAnimation(animationadd);
        tv.setTag(keyString);
        functionselectShowinforlayout.addView(tv);

    }

    /**
     * 设置已选择的数量
     *
     * @param selectedCount
     */
    public void setSelectCount(int selectedCount) {
        if(fitter.equals("recommend")||fitter.equals("post")||fitter.equals("subscription")) {
            funtionselectSelectedinfo.setText(selectedCount + "/3");
        }else{
            funtionselectSelectedinfo.setText(selectedCount + "/5");
        }
    }

    /**
     * 移除指定已选职能视图
     *
     * @param functionBean
     */
    private void removeView(FunctionBean functionBean) {
        functionselectShowinforlayout
                .removeView(functionselectShowinforlayout
                        .findViewWithTag(functionBean.getId()));
        /*Log.i("选择2",selectFunctionBeenList.toString());*/
        getFunctionSecondListBean();
        funcitionSecondAdapter.notifyDataSetChanged();


    }

    @OnClick({R.id.iv_selectfunction_back, R.id.tv_selectfunction_save, R.id.iv_seeSelectFunction})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_selectfunction_back:
                finish();
                break;
            case R.id.tv_selectfunction_save:
                if(fitter.equals("recommend")) {
                    RecommendJobFragment.setFunctionSelectMap(selectFunctionBeenList);
                }else if(fitter.equals("post")){
                    PagerPostSearchFragment.setFunctionSelectMap(selectFunctionBeenList);

                }else if(fitter.equals("creatjoborder")){
                    CreateResumeJobOrderActivity.getInstance().setFunctionSelected(selectFunctionBeenList);
                }else if(fitter.equals("resumeJobOrder")){
                    ResumeJobOrderActivity.getInstance().setFunctionSelected(selectFunctionBeenList);
                }else if(fitter.equals("subscription")){
                    SubscriptionActivity.subscriptionActivity.setFunctionSelected(selectFunctionBeenList);
                }
                if(selectFunctionBeenList.size()==0) {
                    Toast.makeText(this,"请选择职能",Toast.LENGTH_SHORT).show();
                }else{
                    selectFunctionBeenList.clear();
                    finish();
                }
                break;
            case R.id.iv_seeSelectFunction:
                if(num>0) {
                    if (isShow==false) {
                        ivSeeSelectFunction.setImageResource(R.mipmap.shangjiantou);
                        functionselectShowinforlayout.setVisibility(View.VISIBLE);
                    } else {
                        ivSeeSelectFunction.setImageResource(R.mipmap.xiajiantou);
                        functionselectShowinforlayout.setVisibility(View.GONE);
                    }
                    isShow = !isShow;
                }
                break;
        }
    }
}
