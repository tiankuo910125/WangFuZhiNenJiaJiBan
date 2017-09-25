package com.demo.smarthome;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.smarthome.base.activity.BaseSmartHomeActivity;
import com.demo.smarthome.base.utils.AppUtils;
import com.demo.smarthome.base.utils.Constant;
import com.demo.smarthome.base.utils.Httputils;
import com.demo.smarthome.base.utils.MyActivityManager;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.communication.httpmanager.RetrofitAPIManager;
import com.demo.smarthome.communication.jsonbean.ResultBean;
import com.demo.smarthome.communication.jsonbean.ginseng.ForgotPasswordGinseng;
import com.demo.smarthome.myinterface.HttpInterface;
import com.kaopiz.kprogresshud.KProgressHUD;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wangdongyang on 17/2/8.
 */
public class ModifyPasswordActivity extends BaseSmartHomeActivity implements OnClickListener {
    MyActivityManager mam = MyActivityManager.getInstance();
    private Toolbar mToolbar;
    private KProgressHUD kProgressHUD;

    private ImageView head_back;
    private TextView head_title;
    private ImageView head_img;

    private String passwordOldStr;
    private String passwordStr;
    private String passwordConfirmStr;

    private EditText edit_password_old;
    private EditText edit_password;
    private EditText edit_password_confirm;
    private TextView password_submit_btn;

//    private ProgressDialog proDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        mam.pushOneActivity(ModifyPasswordActivity.this);
        kProgressHUD = new KProgressHUD(this);
        init();
    }


    private void init() {

//        mToolbar = (Toolbar)findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        head_back = (ImageView) this.findViewById(R.id.head_back);
        head_title = (TextView) this.findViewById(R.id.head_title);
        head_img = (ImageView) this.findViewById(R.id.head_img);

        edit_password_old = (EditText) this.findViewById(R.id.edit_password_old);
        edit_password = (EditText) this.findViewById(R.id.edit_password);
        edit_password_confirm = (EditText) this.findViewById(R.id.edit_password_confirm);
        password_submit_btn = (TextView) this.findViewById(R.id.password_submit_btn);

        password_submit_btn.setOnClickListener(this);
        head_back.setOnClickListener(this);
        head_title.setText("修改密码");
        head_img.setVisibility(View.GONE);

    }


    private void getSubmit() {
        passwordOldStr = edit_password_old.getText().toString().trim();
        passwordStr = edit_password.getText().toString().trim();
        passwordConfirmStr = edit_password_confirm.getText().toString().trim();

        if (passwordOldStr == null || passwordOldStr.equals("") || passwordStr == null || passwordStr.equals("") || passwordConfirmStr == null || passwordConfirmStr.equals("")) {
            Toast.makeText(ModifyPasswordActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
        } else {
            if (passwordStr.equals(passwordConfirmStr)) {
                getSubmitData();
            } else {
                Toast.makeText(ModifyPasswordActivity.this, "新密码输入不一致", Toast.LENGTH_SHORT).show();
//                AppUtils.show(ModifyPasswordActivity.this, "新密码输入不一致");
            }
        }
    }


    private void getSubmitData() {

        kProgressHUD.show();

        ForgotPasswordGinseng forgotPasswordGinseng = new ForgotPasswordGinseng();
        forgotPasswordGinseng.setPassword(passwordOldStr);
        forgotPasswordGinseng.setPassword_new(passwordStr);
        forgotPasswordGinseng.setPassword_confirm(passwordConfirmStr);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Httputils.HOSTUTIL_HTTPS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPIManager.getOkHttpClient(getApplicationContext()))
                .build();

        HttpInterface httpInterface = retrofit.create(HttpInterface.class);

        Call<ResultBean> call = httpInterface.getForgotPassword("smart/v1/Account/Password?accessToken="+ PreferenceUtil.getString("accessToken"),forgotPasswordGinseng);

        call.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                kProgressHUD.dismiss();

                ResultBean resultBean = response.body();
                if(resultBean.getCode() == 200){
                    AppUtils.show(ModifyPasswordActivity.this,"修改密码成功");
                    PreferenceUtil.putString(Constant.UserManager.PASSWORD,passwordStr);
                    finish();
                }else{
                    AppUtils.show(ModifyPasswordActivity.this,"修改密码失败");
                }

            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                kProgressHUD.dismiss();
                AppUtils.show(ModifyPasswordActivity.this,"修改密码失败");
            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.password_submit_btn:
                getSubmit();
                break;
            case R.id.head_back:
                this.finish();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mam.popOneActivity(ModifyPasswordActivity.this);
    }
}
