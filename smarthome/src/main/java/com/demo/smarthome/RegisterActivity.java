package com.demo.smarthome;

import android.content.Context;
import android.content.Intent;
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

import com.demo.smarthome.base.activity.BaseSmartHomeActivity;
import com.demo.smarthome.base.utils.AppUtils;
import com.demo.smarthome.base.utils.Constant;
import com.demo.smarthome.base.utils.GsonTools;
import com.demo.smarthome.base.utils.Httputils;
import com.demo.smarthome.base.utils.MyActivityManager;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.base.utils.StringUtils;
import com.demo.smarthome.communication.httpmanager.RetrofitAPIManager;
import com.demo.smarthome.communication.jsonbean.RegisterResultBean;
import com.demo.smarthome.communication.jsonbean.ResultBean;
import com.demo.smarthome.communication.jsonbean.ginseng.RegisteredGinseng;
import com.demo.smarthome.myinterface.HttpInterface;
import com.demo.smarthome.qrcode.CaptureActivity;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aprivate.oo.gizwitopenapi.GizwitOpenAPI;
import aprivate.oo.gizwitopenapi.response.RegistResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by liukun on 2016/3/7.
 */
public class RegisterActivity extends BaseSmartHomeActivity implements View.OnClickListener {

    MyActivityManager mam = MyActivityManager.getInstance();
    private KProgressHUD kProgressHUD;

    private String TAG = "RegisterActivity";
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
                    getRegisteredData();
//                    Log.d(TAG,"fall in msg REGISTER_TO_SERVER");
//                    //TODO:校验验证码和发送用户名到服务器
//                    Tasks.executeInBackground(mContext, new BackgroundWork<String>() {
//                        @Override
//                        public String doInBackground() throws Exception {
//                            return RegisterUtilsImpl.getInstance().registerUser(mUserName,mPassWord,mSMSCodeEditText.getText().toString());
//                        }
//                    }, new Completion<String>() {
//                        @Override
//                        public void onSuccess(Context context, String result) {
//                            System.out.println("----------------house------------"+result);
//                            if (result != null){
//                                ResultBean r = GsonTools.getBean(result, ResultBean.class);
//                                if (r.getCode() == Constant.HttpURL.ErrorCode.OK){
//                                    RegisterResultBean registerResultBean = GsonTools.getBean(GsonTools.GsonToString(r.getResult()), RegisterResultBean.class);
//                                    PreferenceUtil.putString("accessToken",registerResultBean.getOauth().getAccessToken());
//                                    PreferenceUtil.putString("refreshToken",registerResultBean.getOauth().getRefreshToken());
//                                    PreferenceUtil.putString("tokenType",registerResultBean.getOauth().getTokenType());
//                                    PreferenceUtil.putInt("expiresIn",registerResultBean.getOauth().getExpiresIn());
//                                    Toast.makeText(mContext,r.getMessage(),Toast.LENGTH_SHORT).show();
//
//                                    mHandler.sendEmptyMessage(REGISTER_SUCCESS);
//                                }else {
//                                    Toast.makeText(mContext,r.getMessage(),Toast.LENGTH_SHORT).show();
//                                    mHandler.sendEmptyMessage(REGISTER_FAILED);
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
                case REGISTER_TO_GIZWITZ: {
                    Log.d(TAG, "fall in msg REGISTER_TO_GIZWITZ");
                    //TODO:向机智云进行注册
                    GizwitOpenAPI.getInstance().registServer(StringUtils.MD5Encode(mUserName) + Constant.GizwitzConfigs.UserNameEnd, Constant.GizwitzConfigs.Password, new GizwitOpenAPI.RequestCallback<RegistResponse>() {
                        @Override
                        public void onSuccess(RegistResponse registResponse) {
                            //注册成功，获取到uid和token
                            PreferenceUtil.putString(Constant.GizwitzConfigs.UID, registResponse.getUid());
                            PreferenceUtil.putString(Constant.GizwitzConfigs.TOKEN, registResponse.getToken());
                            bGizwitzRegister = true;
                            Log.e(TAG,"机智云成功openAPI  request Success");
//                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            mHandler.sendEmptyMessage(REGISTER_TO_SERVER);
                        }

                        @Override
                        public void onFailure(String msg) {
                            //注册失败，弹出错误信息
                            bGizwitzRegister = false;
                            Log.e(TAG,"机智云失败openAPI  request failure" + msg.toString());
                            Toast.makeText(RegisterActivity.this, "机智云注册失败", Toast.LENGTH_SHORT).show();
                            mHandler.sendEmptyMessage(REGISTER_FAILED);
                        }
                    });
//                    mCenter.cRegisterDefaultUser(StringUtils.MD5Encode(mUserName) + Constant.GizwitzConfigs.UserNameEnd, Constant.GizwitzConfigs.Password);
                    break;
                }
                case REGISTER_SUCCESS: {
                    Log.d(TAG, "fall in msg REGISTER_SUCCESS");
                    Intent intent = new Intent(mContext, CaptureActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                case REGISTER_FAILED: {
                    Log.d(TAG, "faill in msg REGISTER_FAILED");
                    //注册失败
//                    Toast.makeText(mContext,mContext.getResources().getString(R.string.register_activity_register_error),Toast.LENGTH_SHORT).show();
                    finish();
                    break;
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
        mam.pushOneActivity(RegisterActivity.this);
        kProgressHUD = new KProgressHUD(this);
        mContext = this;
        mUserNameEditText = (EditText) findViewById(R.id.register_activity_username_edit);
        mSMSCodeEditText = (EditText) findViewById(R.id.register_activity_smscode_edit);
        mPasswordEditText = (EditText) findViewById(R.id.register_activity_pwd_edit);
        mPasswordConfirmText = (EditText) findViewById(R.id.register_activity_pwd_confirm);

        mGetSMSCodeBtn = (TextView) findViewById(R.id.register_activity_get_smscode);
        mRegisterBtn = (Button) findViewById(R.id.register_btn);
        login_btn = (TextView) findViewById(R.id.login_btn);

        mGetSMSCodeBtn.setOnClickListener(this);
        mRegisterBtn.setOnClickListener(this);
        login_btn.setOnClickListener(this);

        mTimer = new TimeCount(60000, 1000);//构造CountDownTimer对象

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mam.popOneActivity(RegisterActivity.this);
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
//                        String result = RegisterUtilsImpl.getInstance().getVertifyCode(mUserNameEditText.getText().toString());
//
//
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
            mHandler.sendEmptyMessage(REGISTER_TO_GIZWITZ);
        } else {
            Toast.makeText(this, getString(R.string.register_activity_username_password_error), Toast.LENGTH_SHORT).show();
        }

    }

//    @Override
//    public void didRegisterUser(GizWifiErrorCode result, String uid, String token) {
//        Log.d(TAG, "fall in didRegisterUser and " + result + " " + uid
//                + " " + token);
//        if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
//            //注册成功，获取到uid和token
//            PreferenceUtil.putString(Constant.GizwitzConfigs.UID, uid);
//            PreferenceUtil.putString(Constant.GizwitzConfigs.TOKEN, token);
//            bGizwitzRegister = true;
//            mHandler.sendEmptyMessage(REGISTER_TO_SERVER);
//        } else if (result == GizWifiErrorCode.GIZ_OPENAPI_USERNAME_UNAVALIABLE)//用户已经存在
//        {
//            bGizwitzRegister = true;
//            mHandler.sendEmptyMessage(REGISTER_TO_SERVER);
//        } else {
//            //注册失败，弹出错误信息
//            bGizwitzRegister = false;
//            mHandler.sendEmptyMessage(REGISTER_FAILED);
//        }
//    }

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

        Call<ResultBean> call = httpInterface.getCode(phone);


        call.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {

                ResultBean resultBean = response.body();
                if (resultBean != null && resultBean.getCode() == 200) {

                } else {
                    AppUtils.show(RegisterActivity.this, resultBean.getMessage());
                }

            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                AppUtils.show(RegisterActivity.this, "获取验证码失败");
            }
        });
    }


    /**
     * 注册请求
     */

    private void getRegisteredData() {
        kProgressHUD.show();
        RegisteredGinseng registeredGinseng = new RegisteredGinseng();
        registeredGinseng.setMobile(mUserName);
        registeredGinseng.setPassword(mPassWord);
        registeredGinseng.setVerify_code(mSMSCodeEditText.getText().toString());


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Httputils.HOSTUTIL_HTTPS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPIManager.getOkHttpClient(getApplicationContext()))
                .build();

        HttpInterface httpInterface = retrofit.create(HttpInterface.class);

        Call<ResultBean> call = httpInterface.getRegistered(registeredGinseng);

        call.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                kProgressHUD.dismiss();
                ResultBean r = response.body();
                if (r.getCode() == Constant.HttpURL.ErrorCode.OK) {
                    RegisterResultBean registerResultBean = GsonTools.getBean(GsonTools.GsonToString(r.getResult()), RegisterResultBean.class);
                    PreferenceUtil.putString("accessToken", registerResultBean.getOauth().getAccessToken());
                    PreferenceUtil.putString("refreshToken", registerResultBean.getOauth().getRefreshToken());
                    PreferenceUtil.putString("tokenType", registerResultBean.getOauth().getTokenType());
                    PreferenceUtil.putInt("expiresIn", registerResultBean.getOauth().getExpiresIn());
                    Toast.makeText(mContext, r.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG,"服务器注册成功" + r.getMessage());
                    mHandler.sendEmptyMessage(REGISTER_SUCCESS);
                } else {
                    AppUtils.show(RegisterActivity.this, r.getMessage());
                    Log.e(TAG,"服务器注册验证码不匹配失败" + r.getMessage());
                    mHandler.sendEmptyMessage(REGISTER_FAILED);
                }

            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                kProgressHUD.dismiss();
                Log.e(TAG,"服务器注册失败" + t.getMessage());
                AppUtils.show(RegisterActivity.this, "服务器注册失败");
            }
        });

    }

}
