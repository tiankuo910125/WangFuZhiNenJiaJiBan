package com.demo.smarthome;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.smarthome.base.utils.AppUtils;
import com.demo.smarthome.base.utils.MyActivityManager;
import com.demo.smarthome.communication.jsonbean.ginseng.ResetPasswordGinseng;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.demo.smarthome.base.activity.BaseSmartHomeActivity;
import com.demo.smarthome.base.utils.Constant;
import com.demo.smarthome.base.utils.GsonTools;
import com.demo.smarthome.base.utils.Httputils;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.communication.httpmanager.RetrofitAPIManager;
import com.demo.smarthome.communication.jsonbean.ResultBean;
import com.demo.smarthome.myinterface.HttpInterface;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by liukun on 2016/3/20.
 */
public class PasswordRecoveryActivity extends BaseSmartHomeActivity implements View.OnClickListener {
    MyActivityManager mam = MyActivityManager.getInstance();
    private KProgressHUD kProgressHUD;

    private String TAG = "PasswordRecoveryActivity";
    private Context mContext;
    private String mUserName;
    private String mPassWord;

    private EditText mUserNameEditText;
    private EditText mSMSCodeEditText;
    private EditText mPasswordEditText;
    private EditText mPasswordConfirmText;

    private TextView login_btn;
    private TextView mGetSMSCodeBtn;
    private Button mRegisterBtn;

    private boolean bGizwitzRegister = false;

    private TimeCount mTimer;

    private boolean bPwdVisiable = false;

    private static final int REGISTER_TO_SERVER = 100;
    private static final int REGISTER_TO_GIZWITZ = 101;
    private static final int REGISTER_SUCCESS = 102;
    private static final int REGISTER_FAILED = 103;
    private static final int REGISTER_VERTIFY_CODE = 104;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REGISTER_TO_SERVER: {
                    getSubmitData();
//                    Log.d(TAG, "fall in msg REGISTER_TO_SERVER");
//                    //TODO:校验验证码和发送用户名到服务器
//                    Tasks.executeInBackground(mContext, new BackgroundWork<String>() {
//                        @Override
//                        public String doInBackground() throws Exception {
//                            return RegisterUtilsImpl.getInstance().resetPassword(mUserName,mPassWord,mSMSCodeEditText.getText().toString());
//                        }
//                    }, new Completion<String>() {
//                        @Override
//                        public void onSuccess(Context context, String result) {
//                            if (result != null){
//                                ResultBean r = GsonTools.getBean(result, ResultBean.class);
//                                if (r.getCode() == Constant.HttpURL.ErrorCode.OK){
//                                    Toast.makeText(mContext,r.getMessage(),Toast.LENGTH_SHORT).show();
//                                    mHandler.sendEmptyMessage(REGISTER_SUCCESS);
//                                }else {
//                                    Toast.makeText(mContext,r.getMessage(),Toast.LENGTH_SHORT).show();
//                                    mUserNameEditText.setText("");
//                                    mSMSCodeEditText.setText("");
//                                    mPasswordEditText.setText("");
//                                    mPasswordConfirmText.setText("");
//                                }
//                            }else {
//                                mHandler.sendEmptyMessage(REGISTER_FAILED);
//                            }
//                        }
//
//                        @Override
//                        public void onError(Context context, Exception e) {
//
//                        }
//                    });
                    break;
                }

                case REGISTER_SUCCESS: {
                    Log.d(TAG, "fall in msg REGISTER_SUCCESS");
                    onBackPressed();
                    finish();
                    break;
                }


                case REGISTER_FAILED: {
                    Log.d(TAG, "fall in msg REGISTER_FAILED");
                    //注册失败
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.register_activity_register_error), Toast.LENGTH_SHORT);
                    finish();
                }
                case REGISTER_VERTIFY_CODE: {
                    Bundle data = msg.getData();
                    String result = data.getString("result");
                    if (result != null) {
                        ResultBean r = GsonTools.getBean(result, ResultBean.class);
                        if (r.getCode() == Constant.HttpURL.ErrorCode.OK) {
                            Toast.makeText(mContext, r.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, r.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mHandler.sendEmptyMessage(REGISTER_FAILED);
                    }
                    break;
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        // 隐藏android系统的状态栏
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.register_activity);
        mam.pushOneActivity(PasswordRecoveryActivity.this);
        kProgressHUD = new KProgressHUD(this);
        mContext = this;
        mUserNameEditText = (EditText) findViewById(R.id.register_activity_username_edit);
        mSMSCodeEditText = (EditText) findViewById(R.id.register_activity_smscode_edit);
        mPasswordEditText = (EditText) findViewById(R.id.register_activity_pwd_edit);
        mPasswordConfirmText = (EditText) findViewById(R.id.register_activity_pwd_confirm);
        login_btn  = (TextView) findViewById(R.id.login_btn);

        mGetSMSCodeBtn = (TextView) findViewById(R.id.register_activity_get_smscode);
        mRegisterBtn = (Button) findViewById(R.id.register_btn);

        mRegisterBtn.setBackgroundResource(R.drawable.submit_bg);
        mGetSMSCodeBtn.setOnClickListener(this);
        mRegisterBtn.setOnClickListener(this);
        login_btn.setOnClickListener(this);

        mTimer = new TimeCount(60000, 1000);//构造CountDownTimer对象

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mam.popOneActivity(PasswordRecoveryActivity.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_activity_get_smscode: {
                //TODO:向服务器发送验证码下发请求
//                new Thread()
//                {
//                    public void run()
//                    {
//                        String result = RegisterUtilsImpl.getInstance().getResetVertifyCode(mUserNameEditText.getText().toString());
//                        Message msg = new Message();
//                        msg.what = REGISTER_VERTIFY_CODE;
//                        Bundle data = new Bundle();
//                        data.putString("result",result);
//                        msg.setData(data);
//                        mHandler.sendMessage(msg);
//                    }
//                }.start();
                getCodeData(mUserNameEditText.getText().toString());
                mTimer.start();
                break;
            }
            case R.id.register_btn: {
                gotoNext();
                break;
            }
            case R.id.login_btn:
                this.finish();
                break;
        }
    }

    private void gotoNext() {
        mUserName = mUserNameEditText.getText().toString();
        mPassWord = mPasswordEditText.getText().toString();
        String passWordConfirm = mPasswordConfirmText.getText().toString();

        if (mUserName.length() > 0 && mPassWord.length() > 0) {
            //用户名匹配手机号格式
            Pattern pattern = Pattern.compile("^1[3578][0-9]{9}$");
            Matcher isNum = pattern.matcher(mUserName);
            if (!isNum.matches()) {
                Toast.makeText(this, getString(R.string.user_info_edit_activity_mobilenum_format_error), Toast.LENGTH_SHORT).show();
                return;
            }
            //密码和密码确认一致
            if (passWordConfirm.length() == 0 || !mPassWord.equals(passWordConfirm)) {
                Toast.makeText(this, getString(R.string.register_activity_password_not_same), Toast.LENGTH_SHORT).show();
                return;
            }

            PreferenceUtil.putString(Constant.UserManager.USERNAME, mUserName);
            PreferenceUtil.putString(Constant.UserManager.PASSWORD, mPassWord);
            //发起注册流程
            mHandler.sendEmptyMessage(REGISTER_TO_SERVER);
        } else {
            Toast.makeText(this, getString(R.string.register_activity_username_password_error), Toast.LENGTH_SHORT).show();
        }
    }


    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            mGetSMSCodeBtn.setText(getString(R.string.register_activity_get_smscode));
            mGetSMSCodeBtn.setClickable(true);
            mGetSMSCodeBtn.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            mGetSMSCodeBtn.setClickable(false);
            mGetSMSCodeBtn.setText(getString(R.string.register_activity_get_smscode) + "(" + (millisUntilFinished / 1000) + ")");
            mGetSMSCodeBtn.setEnabled(false);
        }
    }


    /**
     * 获取验证码请求
     *
     * @param phone
     */
    private void getCodeData(String phone) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Httputils.HOSTUTIL_HTTPS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPIManager.getOkHttpClient(getApplicationContext()))
                .build();

        HttpInterface httpInterface = retrofit.create(HttpInterface.class);

        Call<ResultBean> call = httpInterface.getCodeForgotPassword(phone);


        call.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                ResultBean resultBean = response.body();
                if (resultBean != null && resultBean.getCode() == 200) {

                } else {
                    AppUtils.show(PasswordRecoveryActivity.this, resultBean.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                AppUtils.show(PasswordRecoveryActivity.this, "获取验证码失败");
            }
        });
    }


    private void getSubmitData() {
        kProgressHUD.show();
        ResetPasswordGinseng resetPasswordGinseng = new ResetPasswordGinseng();
        resetPasswordGinseng.setMobile(mUserName);
        resetPasswordGinseng.setVerify_code(mSMSCodeEditText.getText().toString());
        resetPasswordGinseng.setPassword_new(mPassWord);
        resetPasswordGinseng.setPassword_confirm(mPassWord);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Httputils.HOSTUTIL_HTTPS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPIManager.getOkHttpClient(getApplicationContext()))
                .build();

        HttpInterface httpInterface = retrofit.create(HttpInterface.class);

        Call<ResultBean> call = httpInterface.getResetPassword(resetPasswordGinseng);

        call.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                kProgressHUD.dismiss();
                ResultBean r = response.body();
                if (r.getCode() == Constant.HttpURL.ErrorCode.OK) {
                    Toast.makeText(mContext, r.getMessage(), Toast.LENGTH_SHORT).show();
                    mHandler.sendEmptyMessage(REGISTER_SUCCESS);
                } else {
                    AppUtils.show(PasswordRecoveryActivity.this, r.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                kProgressHUD.dismiss();
                AppUtils.show(PasswordRecoveryActivity.this, "修改密码失败");
            }
        });


    }


}
