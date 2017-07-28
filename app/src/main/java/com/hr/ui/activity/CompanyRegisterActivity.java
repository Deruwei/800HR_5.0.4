package com.hr.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hr.ui.R;
import com.hr.ui.config.Constants;
import com.hr.ui.utils.datautils.SharedPreferencesUtils;
import com.hr.ui.utils.netutils.AsyncComRegister;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 企业注册页面
 */
public class CompanyRegisterActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_comregister_back)
    ImageView ivComregisterBack;
    @Bind(R.id.et_comregister_comname)
    EditText etComregisterComname;
    @Bind(R.id.et_comregister_comphone)
    EditText etComregisterComphone;
    @Bind(R.id.et_comregister_comcontacts)
    EditText etComregisterComcontacts;
    @Bind(R.id.et_comregister_comnemail)
    EditText etComregisterComnemail;
    @Bind(R.id.et_comregister_username)
    EditText etComregisterUsername;
    @Bind(R.id.et_comregister_password)
    EditText etComregisterPassword;
    @Bind(R.id.et_comregister_password2)
    EditText etComregisterPassword2;
    @Bind(R.id.rl_comregistr_save)
    RelativeLayout rlComregistrSave;
    @Bind(R.id.iv_registcom_check)
    ImageView ivRegistcomCheck;
    @Bind(R.id.tv_registcom_agreement)
    TextView tvRegistcomAgreement;
    @Bind(R.id.tv_company_register_phone)
    TextView tvCompanyRegisterPhone;

    private String emailString, usernameString, password0String, password1String, companyNameString, comPhoneString, comContactsString;
    private int industryID;
    private boolean isCheck = true;
    public static CompanyRegisterActivity companyRegisterActivity = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_regist);
        ButterKnife.bind(this);
        companyRegisterActivity = CompanyRegisterActivity.this;
        initData();
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                finish();
            }
        }
    };

    private void initData() {
        SharedPreferencesUtils sharedPreferencedUtils = new SharedPreferencesUtils(this);
        industryID = sharedPreferencedUtils.getIntValue(Constants.INDUSTRY, 0);
        switch (industryID) {
            case 11:   //建筑
                tvCompanyRegisterPhone.setText("010-82197168");
                break;
            case 12:   //金融
                tvCompanyRegisterPhone.setText("010-82197466");
                break;
            case 14:   //医药
                tvCompanyRegisterPhone.setText("010-82197575");
                break;
            case 26:   //服装
                tvCompanyRegisterPhone.setText("010-82197265");
                break;
            case 29:   //化工
                tvCompanyRegisterPhone.setText("010-82197555");
                break;
            case 22:   //制造业
                tvCompanyRegisterPhone.setText("010-82197466");
                break;
            default:
                tvCompanyRegisterPhone.setText("010-62123388");
                break;
        }
    }

    @OnClick({R.id.iv_comregister_back, R.id.rl_comregistr_save, R.id.iv_registcom_check, R.id.tv_registcom_agreement})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_comregister_back:
                finish();
                break;
            case R.id.rl_comregistr_save:
                saveData();
                break;
            case R.id.iv_registcom_check:
                if (isCheck) {
                    ivRegistcomCheck.setImageResource(R.mipmap.hui);
                    isCheck = false;
                } else {
                    ivRegistcomCheck.setImageResource(R.mipmap.lv);
                    isCheck = true;
                }
                break;
            case R.id.tv_registcom_agreement:
                Intent intentUserAgreement = new Intent(this, UserAgreementActivity.class);
                intentUserAgreement.putExtra("userOrCom", "2");
                startActivity(intentUserAgreement);
                break;
        }
    }

    private void saveData() {
        emailString = etComregisterComnemail.getText().toString().trim();
        usernameString = etComregisterUsername.getText().toString().trim();
        password0String = etComregisterPassword.getText().toString().trim();
        password1String = etComregisterPassword2.getText().toString().trim();
        companyNameString = etComregisterComname.getText().toString().trim();
        comPhoneString = etComregisterComphone.getText().toString().trim();
        comContactsString = etComregisterComcontacts.getText().toString().trim();
        if (!checkData()) {
            return;
        }
        AsyncComRegister asyncComRegister = new AsyncComRegister(CompanyRegisterActivity.this, handler);
        asyncComRegister.execute(emailString, usernameString, password0String, industryID + "", password1String, companyNameString, comPhoneString, comContactsString);
    }

    /**
     * 数据验证
     */
    private boolean checkData() {
        // ---验证 邮箱
        String check = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(emailString);
        //验证固话
        String check2 = "^\\d{3,4}-\\d{7,8}$";
        Pattern regex2 = Pattern.compile(check2);
        Matcher matcher2 = regex2.matcher(comPhoneString);
        boolean isMatched = matcher.matches();
        boolean isMatched2 = matcher2.matches();
        if (companyNameString.length() == 0) {
            Toast.makeText(this, getString(R.string.comname_null),
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (comPhoneString.length() == 0) {
            Toast.makeText(this, getString(R.string.comphone_null),
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (!isMatched2) {
            Toast.makeText(this, getString(R.string.comphone_mistake),
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (comContactsString.length() == 0) {
            Toast.makeText(this, getString(R.string.comcontacts_null), Toast.LENGTH_LONG).show();
            return false;
        }
        if (emailString.length() == 0) {
            Toast.makeText(this, "请输入电子邮箱",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (!isMatched) {
            Toast.makeText(this, "电子邮箱地址格式错误",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        // ---验证用户名
        if (usernameString.length() == 0) {
            Toast.makeText(this, getString(R.string.username_null),
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (usernameString.length() < 4 || usernameString.length() > 25) {
            Toast.makeText(this, getString(R.string.username_length_error), Toast.LENGTH_LONG).show();
            return false;
        }

        Pattern pattern1 = Pattern.compile("^[_.@a-zA-Z0-9]+$");
        Matcher matcher1 = pattern1.matcher(usernameString);
        if (!matcher1.find()) {
            Toast.makeText(this, getString(R.string.username_error),
                    Toast.LENGTH_LONG).show();
            return false;
        }

        // ---验证密码
        if (password0String.length() == 0) {
            Toast.makeText(this, getString(R.string.password_null), Toast.LENGTH_LONG).show();
            return false;
        }
        if (password0String.length() < 6) {
            Toast.makeText(this, getString(R.string.password_length_error),
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (!password0String.equals(password1String)) {
            Toast.makeText(this,
                    getString(R.string.password_pswconfirm_not_equal),
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (password0String.equals(usernameString)) {
            Toast.makeText(this, getString(R.string.password_username_equal),
                    Toast.LENGTH_LONG).show();
            return false;
        }
        Pattern patternPwd = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,25}$");
        Matcher matcherPwd = patternPwd.matcher(password1String);
        if (!matcherPwd.find()) {
            Toast.makeText(this, getString(R.string.password_error), Toast.LENGTH_LONG).show();
            return false;
        }
        if (!isCheck) {
            Toast.makeText(this, "您未同意注册协议", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}
