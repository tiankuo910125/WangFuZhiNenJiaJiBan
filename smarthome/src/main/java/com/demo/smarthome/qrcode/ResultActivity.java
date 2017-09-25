package com.demo.smarthome.qrcode;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.smarthome.R;
import com.demo.smarthome.base.activity.BaseSmartHomeActivity;
import com.demo.smarthome.base.utils.Constant;
import com.demo.smarthome.base.utils.GsonTools;
import com.demo.smarthome.base.utils.Httputils;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.communication.httpmanager.RetrofitAPIManager;
import com.demo.smarthome.communication.jsonbean.DescriPtion;
import com.demo.smarthome.communication.jsonbean.HouseProfileBean;
import com.demo.smarthome.communication.jsonbean.ResultBean;
import com.demo.smarthome.communication.jsonbean.ginseng.BindCodeGinseng;
import com.demo.smarthome.communication.jsonbean.sub.Houses;
import com.demo.smarthome.myinterface.HttpInterface;
import com.demo.smarthome.tools.GizwitHolders;
import com.demo.smarthome.ui.adapter.HouseManagementAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import aprivate.oo.gizwitopenapi.GizwitOpenAPI;
import aprivate.oo.gizwitopenapi.response.BindingList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ResultActivity extends BaseSmartHomeActivity {

    private String TAG = "ResultActivity";
    private Context mContext;
    private TextView mResultText;
    private ProgressBar mProgressBar;
    private String mCaptureResualt;
    private Gson gson = new Gson();

    private static final int GIZWITZ_USER_LOGIN = 100;
    private static final int SEARCH_ROUTER = 101;
    private static final int BIND_ROUTER = 102;
    private static final int LOGIN_DEVICE = 103;
    private static final int SETUP_PROFILE = 104;
    private static final int FINISH_REGISTER = 106;
    private static final int TIME_OUT = 107;
    private static final int ERROR_STATE = 108;

    private HouseProfileBean houseProfileBean;
    private HouseManagementAdapter mHouseAdapter;

//    private Handler mHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//
////                case NetServerCommunicationImpl.MSG_REGISTER_ROUTER_CALLBACK:
////                    final Bundle data = msg.getData();
////                    System.out.println("=---------------======="+data.getString("result"));
////                    Tasks.executeInBackground(mContext, new BackgroundWork<String>() {
////                        @Override
////                        public String doInBackground() throws Exception {
////                            return HouseUtilsImpl.getInstance().getBindingHouse(data.getString("result"));
////                        }
////                    }, new Completion<String>() {
////
////                        @Override
////                        public void onSuccess(Context context, String result) {
////                            System.out.println("======================="+result);
////
////                            r = GsonTools.getBean(result, ResultBean.class);
////                            if (r.getCode() == Constant.HttpURL.ErrorCode.OK) {
////                                houseProfileBean = new HouseProfileBean();
////                                houseProfileBean = GsonTools.getBean(GsonTools.GsonToString(r.getResult()), HouseProfileBean.class);
////                                mHandler.sendEmptyMessage(GIZWITZ_USER_LOGIN);
////
////                            } else {
////                                mResultText.setText(getString(R.string.capture_result_loading_result_fail));
////                                try {
////                                    Thread.sleep(1000);
////                                } catch (InterruptedException e) {
////                                    e.printStackTrace();
////                                }
////                                finish();
////                            }
////
////                        }
////
////                        @Override
////                        public void onError(Context context, Exception e) {
////                            mResultText.setText(getString(R.string.capture_result_loading_result_fail));
////                            finish();
////                        }
////                    });
////                    break;
//
//                case NetServerCommunicationImpl.MSG_REGISTER_ROUTER_CALLBACK: {
//                    Bundle data = msg.getData();
//                    if (data.getString("result") != null) {
//                        r = GsonTools.getBean(data.getString("result"), ResultBean.class);
//                        if (r.getCode() == Constant.HttpURL.ErrorCode.OK) {
//                            houseProfileBean = new HouseProfileBean();
//                            houseProfileBean = GsonTools.getBean(GsonTools.GsonToString(r.getResult()), HouseProfileBean.class);
//                            mHandler.sendEmptyMessage(GIZWITZ_USER_LOGIN);
//                        } else {
//                            mResultText.setText(getString(R.string.capture_result_loading_result_fail));
//                            try {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            finish();
//                        }
//                    } else {
//                        mResultText.setText(getString(R.string.capture_result_loading_result_fail));
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        finish();
//                    }
//                    break;
//                }
//                case GIZWITZ_USER_LOGIN: {
////                    GizWifiSDK.sharedInstance().userLogin(StringUtils.MD5Encode(PreferenceUtil.getString(Constant.UserManager.USERNAME))
////                            + Constant.GizwitzConfigs.UserNameEnd, Constant.GizwitzConfigs.Password);
//                    break;
//                }
//                case SEARCH_ROUTER: {
////
////                    GizWifiSDK.sharedInstance().getBoundDevices(PreferenceUtil.getString(Constant.GizwitzConfigs.UID),
////                            PreferenceUtil.getString(Constant.GizwitzConfigs.TOKEN), null);
//                    break;
//                }
//                case BIND_ROUTER: {
//                    if (deviceslist.size() > 0) {
//                        boolean bDevice = false;
//                        for (int i = 0; i < deviceslist.size(); i++) {
//                            GizWifiDevice mDevice = deviceslist.get(i);
//                            if (mDevice.getProductKey().equals(houseProfileBean.getGateways().get(0).getExtid())) {
//                                if (!mDevice.isBind()) {
//                                    GizWifiSDK.sharedInstance().bindRemoteDevice(PreferenceUtil.getString(Constant.GizwitzConfigs.UID), PreferenceUtil.getString(Constant.GizwitzConfigs.TOKEN),
//                                            houseProfileBean.getGateways().get(0).getIp(), houseProfileBean.getGateways().get(0).getExtid(), houseProfileBean.getGateways().get(0).getFeature());
//                                } else {
//                                    Message tmp = new Message();
//                                    tmp.what = LOGIN_DEVICE;
//                                    tmp.obj = mDevice;
//                                    mHandler.sendMessage(tmp);
//                                }
//                                bDevice = true;
//                            }
//                        }
//                        if (!bDevice) {
//                            GizWifiSDK.sharedInstance().bindRemoteDevice(PreferenceUtil.getString(Constant.GizwitzConfigs.UID), PreferenceUtil.getString(Constant.GizwitzConfigs.TOKEN),
//                                    houseProfileBean.getGateways().get(0).getIp(), houseProfileBean.getGateways().get(0).getExtid(), houseProfileBean.getGateways().get(0).getFeature());
//                        }
//                    } else {
////                        GizWifiSDK.sharedInstance().bindRemoteDevice(PreferenceUtil.getString(Constant.GizwitzConfigs.UID), PreferenceUtil.getString(Constant.GizwitzConfigs.TOKEN),
////                                houseProfileBean.getGateways().get(0).getIp(), houseProfileBean.getGateways().get(0).getExtid(), houseProfileBean.getGateways().get(0).getFeature());
//                    }
//                    break;
//                }
////                case TIME_OUT: {
////                    mProgressBar.setVisibility(View.GONE);
////                    mResultText.setText(getString(R.string.capture_result_loading_result_fail));
////                    Toast.makeText(mContext, "登录设备超时", Toast.LENGTH_SHORT).show();
////                    finish();
////                    break;
////                }
////                case ERROR_STATE: {
////                    mProgressBar.setVisibility(View.GONE);
////                    mResultText.setText(getString(R.string.capture_result_loading_result_fail));
////                    Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT).show();
////                    finish();
////                    break;
////                }
//                case FINISH_REGISTER: {
//                    mProgressBar.setVisibility(View.GONE);
//                    mResultText.setText(getString(R.string.capture_result_loading_result_ok));
//                    Intent intent = new Intent(mContext, LoginActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
//                    startActivity(intent);
//                    finish();
//                    break;
//                }
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mContext = this;

        Bundle extras = getIntent().getExtras();

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mResultText = (TextView) findViewById(R.id.loading_text);

        if (null != extras) {
            mCaptureResualt = extras.getString("result");
            PreferenceUtil.putString(Constant.RouterManager.ROUTERINFO, mCaptureResualt);
//            try {
//                NetServerCommunicationImpl.getInstance(mContext).registerRouter(mHandler, mCaptureResualt);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
            getBindCodeData(mCaptureResualt);
        }
    }


//    @Override
//    protected void didUpdateProduct(GizWifiErrorCode result, String productKey, String productUI) {
//        Log.d(TAG, "fall in didUpdateProduct, and error is " + result);
//        if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
//            //更新配置文件成功，返回设备列表
//            //loadProfileFile();
//            mProgressBar.setVisibility(View.GONE);
//            Message msg = new Message();
//            msg.what = FINISH_REGISTER;
//            mHandler.sendMessage(msg);
//        }
//    }
//
//    @Override
//    protected void didDiscovered(GizWifiErrorCode result, List<GizWifiDevice> deviceList) {
//        Log.d(TAG, "fall in didDiscovered, and error is " + result + "; deviceList size is " + deviceList.size());
//        if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
//            //更新配置文件成功，返回设备列表
//            deviceslist = deviceList;
//            mHandler.sendEmptyMessage(BIND_ROUTER);
//        } else {
//            mHandler.sendEmptyMessage(ERROR_STATE);
//        }
//    }
//
//    @Override
//    public void didBindDevice(GizWifiErrorCode result, String did) {
//        Log.d(TAG, "fall in didBindDevice, and error is " + result);
//        if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
//            Message tmp = new Message();
//            tmp.what = FINISH_REGISTER;
//            mHandler.sendMessage(tmp);
//        } else {
//            mHandler.sendEmptyMessage(ERROR_STATE);
//        }
//    }
//
//    @Override
//    protected void didUserLogin(GizWifiErrorCode result, String uid, String token) {
//        super.didUserLogin(result, uid, token);
//        Log.d(TAG, "fall in didUserLogin and " + result + " " + uid
//                + " " + token);
//        if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
//            //登陆成功，获取到uid和token
//            PreferenceUtil.putString(Constant.GizwitzConfigs.UID, uid);
//            PreferenceUtil.putString(Constant.GizwitzConfigs.TOKEN, token);
//            mHandler.sendEmptyMessage(SEARCH_ROUTER);
//        } else {
//            //登陆失败，弹出错误信息
//            mHandler.sendEmptyMessage(ERROR_STATE);
//        }
//    }
//
//
//    @Override
//    protected void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
//        if (isFinishing()) return;
//        mHandler.removeMessages(TIME_OUT);
//        if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS && isSubscribed) {
//            mXpgWifiDevice = device;
//            mHandler.sendEmptyMessage(SETUP_PROFILE);
//        } else {
//            mHandler.sendEmptyMessage(ERROR_STATE);
//        }
//
//    }


    /**
     * 绑定房屋请求
     *
     * @param code
     */
    private void getBindCodeData(final String code) {
        final BindCodeGinseng bindCodeGinseng = new BindCodeGinseng();
        bindCodeGinseng.setCode(code);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Httputils.HOSTUTIL_HTTPS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPIManager.getOkHttpClient(this))
                .build();

        HttpInterface httpInterface = retrofit.create(HttpInterface.class);

        Call<ResultBean> call = httpInterface.getBindCode("smart/v1/Account/Scan?accessToken=" + PreferenceUtil.getString("accessToken"), bindCodeGinseng);

        call.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {

                final ResultBean resultBean = response.body();
//               resultBean.getResult();
                Log.e(TAG,"resultBean.getResult()" + resultBean.getResult());

                if (resultBean != null) {

                    if (resultBean.getCode() == 200) {
                        //机智云和服务器都绑定成功，不再重新登录，直接刷新列表
                        //获取productSecret
                        final Houses bean = GsonTools.getBean(GsonTools.GsonToString(resultBean.getResult()), Houses.class);

                        Log.e(TAG,"bean=====" +  bean.toString());
                        Log.e(TAG,"string=====" +  bean.getGateways().get(0).getDescription());

                        Log.e(TAG,"getGateways=====" +  bean.getGateways());


                        DescriPtion descriPtion = gson.fromJson( bean.getGateways().get(0).getDescription(), DescriPtion.class);

                        String productSecret = descriPtion.getProductSecret();
                        String productKey = descriPtion.getProductKey();
                        String mac = descriPtion.getMac();

                        Log.e(TAG,"productSecret" + productSecret);
//
                        //向机智云绑定

                        GizwitOpenAPI.getInstance().bindServer(productKey, mac, "wfkj", productSecret, new GizwitOpenAPI.RequestCallback<BindingList.DevicesBean>() {
                            @Override
                            public void onSuccess(BindingList.DevicesBean bindResponse) {
                                //刷新机智云列表，添加

                                Log.e(TAG,"getBindingList==" + GizwitHolders.getInstance().getBindingList());

                                if(GizwitHolders.getInstance().getBindingList() == null) {

                                    GizwitHolders.getInstance().setBindingList(new BindingList());

                                }
                                List<BindingList.DevicesBean> devices = GizwitHolders.getInstance().getBindingList().getDevices();

                                if(devices == null) {
                                    devices = new ArrayList<BindingList.DevicesBean>();
                                    GizwitHolders.getInstance().getBindingList().setDevices(devices);
                                }
                                devices.add(bindResponse);


                                Log.e(TAG,"bindResponse=====" + bindResponse.toString());
                                Toast.makeText(ResultActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
                                //要处理刷新列表！！！(服务器)
                                List<Houses> houses = GizwitHolders.getInstance().getUserProfileBean().getHouses();
//                                Log.e(TAG,"houses===获取的==" + houses.toString());
                                if(houses == null) {
                                    //创建一个列表
                                    houses = new ArrayList<>();
                                    GizwitHolders.getInstance().getUserProfileBean().setHouses(houses);
                                }
                                houses.add(bean);
                                Log.e(TAG,"houses===获取的==" + houses.toString());
//                                Log.e(TAG,"houses===添加的==" + houses.add(bean));
                                setResult(2, getIntent());
                                finish();
                            }

                            @Override
                            public void onFailure(String msg) {
                                Toast.makeText(ResultActivity.this, "绑定失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        mResultText.setText(getString(R.string.capture_result_loading_result_fail));
                        Log.e(TAG,"服务器绑定失败！！！" + resultBean.getCode().toString());
                        Log.e(TAG,"服务器绑定失败！！！" + resultBean.getMessage().toString());
                        Log.e(TAG,"服务器绑定失败！！！" + resultBean.getResult());
                        Log.e(TAG,"服务器绑定失败！！！" + resultBean.getRequest());
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        finish();
                    }
                } else {
                    Log.e(TAG,"服务器的返回值为空");
//                    mHandler.sendEmptyMessage(5003);
                }
            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
//                mHandler.sendEmptyMessage(5003);
                Log.e(TAG,"绑定服务器的联网请求失败" + t.getMessage());
            }
        });
    }


}
