package com.demo.smarthome.qrcode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.smarthome.base.activity.BaseSmartHomeActivity;
import com.demo.smarthome.base.utils.AppUtils;
import com.demo.smarthome.base.utils.Httputils;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.communication.jsonbean.OutModel_QRCode;
import com.demo.smarthome.myinterface.HttpInterface;
import com.facebook.drawee.view.SimpleDraweeView;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.demo.smarthome.HouseManagementActivity;
import com.demo.smarthome.R;
import com.demo.smarthome.base.utils.GsonTools;
import com.demo.smarthome.communication.httpmanager.RetrofitAPIManager;
import com.demo.smarthome.communication.jsonbean.UserProfileBean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by liukun on 2016/3/17.
 */
public class CreateQRActivity extends BaseSmartHomeActivity implements View.OnClickListener {

    private KProgressHUD kProgressHUD;
    private String TAG = "CreateQRActivity";
    private Context mContext;

    private SimpleDraweeView add_user_qrcode;
    private TextView mContent;
    private TextView mHouseName;
    private TextView mHouseAddress;

    private ImageView head_back;
    private TextView head_title;
    private ImageView head_img;
    private TextView head_submit;

    private String uri = "I'm the God, you are be locked,haha";
    private UserProfileBean userProfileBean;
    private int houseID;


//    //监听图片下载过程
//    private ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
//        @Override
//        public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
//            System.out.println("------------------1111111---------------");
//        }
//
//        @Override
//        public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
//            System.out.println("------------------2222222---------------");
//        }
//
//        @Override
//        public void onFailure(String id, Throwable throwable) {
//            System.out.println("------------------3333333---------------");
//            kProgressHUD.dismiss();
//        }
//    };
//
//    private DraweeController draweeController = Fresco.newDraweeControllerBuilder()
//            .setAutoPlayAnimations(true)
//            .setControllerListener(controllerListener)
//            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_qrcode_activity);
        kProgressHUD = new KProgressHUD(this);
        mContext = this;
        mHouseName = (TextView) findViewById(R.id.house_name);
        mHouseAddress = (TextView) findViewById(R.id.house_address);

        head_back = (ImageView) this.findViewById(R.id.head_back);
        head_title = (TextView) this.findViewById(R.id.head_title);
        head_img = (ImageView) this.findViewById(R.id.head_img);
        head_submit = (TextView) this.findViewById(R.id.head_submit);

        head_back.setOnClickListener(this);
        head_title.setText("家庭成员管理");
        head_img.setVisibility(View.GONE);
        head_submit.setVisibility(View.VISIBLE);
        head_submit.setText("刷新");
        head_submit.setOnClickListener(this);


        houseID = this.getIntent().getIntExtra("houseID", -1);
        userProfileBean = new UserProfileBean();
        userProfileBean = GsonTools.getBean(PreferenceUtil.getString("user_profile"), UserProfileBean.class);
        if (userProfileBean.getHouses().size() > 0) {
            for (int i = 0; i < userProfileBean.getHouses().size(); i++) {
                if (houseID == userProfileBean.getHouses().get(i).getId()) {
                    mHouseName.setText(userProfileBean.getHouses().get(i).getName());
                    mHouseAddress.setText(userProfileBean.getHouses().get(i).getAddress());
                }
            }
        } else {
            Intent intent = new Intent(mContext, HouseManagementActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
            startActivity(intent);
            finish();
        }

        add_user_qrcode = (SimpleDraweeView) findViewById(R.id.add_user_qrcode);
        mContent = (TextView) findViewById(R.id.qrcode_content);
//        Create2QR();
        getQRCodeData();
//        add_user_qrcode.setController(draweeController);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //生成二维码
//    public void Create2QR(){
//        Tasks.executeInBackground(mContext, new BackgroundWork<String>() {
//            @Override
//            public String doInBackground() throws Exception {
//                //TODO:向服务器获取二维码
//                return RegisterUtilsImpl.getInstance().getAdministratorQRCode(houseID);
//            }
//        }, new Completion<String>() {
//            @Override
//            public void onSuccess(Context context, String result) {
//                if (result != null){
//                    ResultBean r = GsonTools.getBean(result, ResultBean.class);
//                    //TODO: 验证  如果成功继续，如果失败重新登录
//                    if (r.getCode() == Constant.HttpURL.ErrorCode.OK){
//                        String s= GsonTools.GsonToString(r.getResult());
//                        uri = s.substring(8,s.length()-2);
//                        DisplayMetrics dm = new DisplayMetrics();
//                        getWindowManager().getDefaultDisplay().getMetrics(dm);
//                        int mScreenWidth = dm.widthPixels;
//                        Bitmap bitmap;
//                        try {
//                            bitmap = BitmapUtil.createQRCode(uri,mScreenWidth);
//
//                            if(bitmap != null){
//                                mQRcodeImage.setImageBitmap(bitmap);
//                            }
//
//                        } catch (WriterException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                    }else {
//                        Toast.makeText(mContext,"获取二维码失败："+r.getMessage(),Toast.LENGTH_SHORT).show();
//                    }
//                }else{
//                    Toast.makeText(mContext,"服务器无响应",Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onError(Context context, Exception e) {
//
//            }
//        });
//    }

    //更新二维码
//    public void Update2QR(){
//        Tasks.executeInBackground(mContext, new BackgroundWork<String>() {
//            @Override
//            public String doInBackground() throws Exception {
//                //TODO:向服务器获取二维码
//                return RegisterUtilsImpl.getInstance().refreshAdministratorQRCode(houseID);
//            }
//        }, new Completion<String>() {
//            @Override
//            public void onSuccess(Context context, String result) {
//                if (result != null){
//                    ResultBean r = GsonTools.getBean(result, ResultBean.class);
//                    //TODO: 验证  如果成功继续，如果失败重新登录
//                    if (r.getCode() == Constant.HttpURL.ErrorCode.OK){
//                        String s= GsonTools.GsonToString(r.getResult());
//                        uri = s.substring(8,s.length()-2);
//                        DisplayMetrics dm = new DisplayMetrics();
//                        getWindowManager().getDefaultDisplay().getMetrics(dm);
//                        int mScreenWidth = dm.widthPixels;
//                        Bitmap bitmap;
//                        try {
//                            bitmap = BitmapUtil.createQRCode(uri,mScreenWidth);
//
//                            if(bitmap != null){
//                                ad.setImageBitmap(bitmap);
//                            }
//
//                        } catch (WriterException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                    }else {
//                        Toast.makeText(mContext,"获取二维码失败："+r.getMessage(),Toast.LENGTH_SHORT).show();
//                    }
//                }else{
//                    Toast.makeText(mContext,"服务器无响应",Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onError(Context context, Exception e) {
//
//            }
//        });
//    }


    private void getQRCodeData() {

        kProgressHUD.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Httputils.HOSTUTIL_HTTPS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPIManager.getOkHttpClient(getApplicationContext()))
                .build();

        HttpInterface httpInterface = retrofit.create(HttpInterface.class);

        Call<OutModel_QRCode> call = httpInterface.getQRCode("smart/v1/Account/Qrcode/" + houseID + "?accessToken=" + PreferenceUtil.getString("accessToken"));

        call.enqueue(new Callback<OutModel_QRCode>() {
            @Override
            public void onResponse(Call<OutModel_QRCode> call, Response<OutModel_QRCode> response) {


                OutModel_QRCode outModel_qrCode = response.body();
                if (outModel_qrCode.getCode() == 200) {
                    String imgUrl = outModel_qrCode.getResult().getUrl();
                    if (imgUrl != null) {
//                        imgUrl = imgUrl.replace("https", "http");
                        kProgressHUD.dismiss();
                        add_user_qrcode.setImageURI(imgUrl);
                        AppUtils.show(CreateQRActivity.this, outModel_qrCode.getMessage());
                    }
                } else {
                    kProgressHUD.dismiss();
                    AppUtils.show(CreateQRActivity.this, outModel_qrCode.getMessage());
                }
                System.out.println("-----json--------" + response.body());
            }

            @Override
            public void onFailure(Call<OutModel_QRCode> call, Throwable t) {
                kProgressHUD.dismiss();
                AppUtils.show(CreateQRActivity.this, "失败");
            }
        });
    }


    private void upDateQRCodeData() {

        kProgressHUD.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Httputils.HOSTUTIL_HTTPS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPIManager.getOkHttpClient(getApplicationContext()))
                .build();

        HttpInterface httpInterface = retrofit.create(HttpInterface.class);

        Call<OutModel_QRCode> call = httpInterface.upDateQRCode("smart/v1/Account/Qrcode/" + houseID + "?accessToken=" + PreferenceUtil.getString("accessToken"));

        call.enqueue(new Callback<OutModel_QRCode>() {
            @Override
            public void onResponse(Call<OutModel_QRCode> call, Response<OutModel_QRCode> response) {


                OutModel_QRCode outModel_qrCode = response.body();
                if (outModel_qrCode.getCode() == 200) {
                    String imgUrl = outModel_qrCode.getResult().getUrl();
                    if (imgUrl != null) {
//                        imgUrl = imgUrl.replace("https", "http");
                        kProgressHUD.dismiss();
                        AppUtils.show(CreateQRActivity.this, outModel_qrCode.getMessage());
                        add_user_qrcode.setImageURI(imgUrl);
//                        add_user_qrcode.setController(draweeController);
                    }
                } else {
                    kProgressHUD.dismiss();
                    AppUtils.show(CreateQRActivity.this, outModel_qrCode.getMessage());
                }
                System.out.println("-----json--------" + response.body());
            }

            @Override
            public void onFailure(Call<OutModel_QRCode> call, Throwable t) {
                kProgressHUD.dismiss();
                AppUtils.show(CreateQRActivity.this, "失败");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back:
                this.finish();
                break;
            case R.id.head_submit:
                upDateQRCodeData();
                break;
        }
    }
}
