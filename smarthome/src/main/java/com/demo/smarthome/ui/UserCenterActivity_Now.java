package com.demo.smarthome.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.smarthome.base.activity.BaseSmartHomeActivity;
import com.demo.smarthome.base.utils.AppUtils;
import com.demo.smarthome.base.utils.EventBus_Account;
import com.demo.smarthome.base.utils.Httputils;
import com.demo.smarthome.base.utils.MyActivityManager;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.communication.jsonbean.HeadPortrait;
import com.demo.smarthome.communication.jsonbean.ginseng.AccountChangeGinseng;
import com.demo.smarthome.communication.jsonbean.sub.Houses;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.demo.smarthome.LoginActivity;
import com.demo.smarthome.MainActivity;
import com.demo.smarthome.ModifyPasswordActivity;
import com.demo.smarthome.R;
import com.demo.smarthome.base.utils.Constant;
import com.demo.smarthome.communication.httpmanager.RetrofitAPIManager;
import com.demo.smarthome.communication.jsonbean.ResultBean;
import com.demo.smarthome.communication.jsonbean.UserProfileBean;
import com.demo.smarthome.myinterface.HttpInterface;

import java.io.File;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by liukun on 2016/2/22.
 */
public class UserCenterActivity_Now extends BaseSmartHomeActivity implements View.OnClickListener {
    MyActivityManager mam = MyActivityManager.getInstance();
    private Gson gson = new Gson();
    private KProgressHUD kProgressHUD;
    /***
     * 使用照相机拍照获取图片
     */
    public static final int SELECT_PIC_BY_TACK_PHOTO = 0;
    /***
     * 使用相册中的图片
     */
    public static final int SELECT_PIC_BY_PICK_PHOTO = 1;


    public static final int NICKNAME_CODE = 101;
    public static final int PHONE_CODE = 102;

    public static final int NAME_CODE = 103;
    public static final int ID_CODE = 104;
    public static final int PHOTOZOOM = 0; //
    public static final int PHOTOTAKE = 1; //
    public static final int IMAGE_COMPLETE = 2; //
    public static final int CROPREQCODE = 3; //

    /**
     * 获取到的图片路径
     */
    private String picPath;

    /***
     * 从Intent获取图片路径的KEY
     */
    public static final String KEY_PHOTO_PATH = "photo_path";
    private Uri photoUri;

    private PopupWindow popWindow;
    private LayoutInflater layoutInflater;
    private TextView photograph, albums;
    private LinearLayout cancel;

    private SimpleDraweeView head_userimg;//头像
    private TextView name;//名称
    private TextView phone;//电话
    private ImageView img_identity;//身份
    private LinearLayout user_management_btn;//用户管理按钮
    private LinearLayout modify_passwrod_btn;//修改密码按钮
    private LinearLayout cancellation_btn;//注销按钮
    private ImageView edit_name_btn;//修改名称按钮

    private ImageView head_back;
    private TextView head_title;
    private ImageView head_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usercenter_activity_new);
        mam.pushOneActivity(UserCenterActivity_Now.this);
        init();
    }


    private void init() {
        kProgressHUD = new KProgressHUD(this);
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        head_userimg = (SimpleDraweeView) this.findViewById(R.id.head_userimg);
        name = (TextView) this.findViewById(R.id.name);
        phone = (TextView) this.findViewById(R.id.phone);
        img_identity = (ImageView) this.findViewById(R.id.img_identity);
        user_management_btn = (LinearLayout) this.findViewById(R.id.user_management_btn);
        modify_passwrod_btn = (LinearLayout) this.findViewById(R.id.modify_passwrod_btn);
        cancellation_btn = (LinearLayout) this.findViewById(R.id.cancellation_btn);
        edit_name_btn = (ImageView) this.findViewById(R.id.edit_name_btn);

        head_back = (ImageView) this.findViewById(R.id.head_back);
        head_title = (TextView) this.findViewById(R.id.head_title);
        head_img = (ImageView) this.findViewById(R.id.head_img);
        head_img.setVisibility(View.GONE);
        head_title.setText("个人中心");

        user_management_btn.setOnClickListener(this);
        modify_passwrod_btn.setOnClickListener(this);
        cancellation_btn.setOnClickListener(this);
        edit_name_btn.setOnClickListener(this);
        head_userimg.setOnClickListener(this);
        head_back.setOnClickListener(this);

        Constant.userProfileBean = gson.fromJson(PreferenceUtil.getString("user_profile"), UserProfileBean.class);
        if (Constant.userProfileBean != null) {
            String imgUrl = Constant.userProfileBean.getProfile().getAvatarPath();
            if (imgUrl != null) {
                imgUrl = imgUrl.replace("https", "http");
                head_userimg.setImageURI(imgUrl);
            }
        }
        String roleStr = PreferenceUtil.getString("rolse");
        if (roleStr != null && roleStr.equals("owner")) {
            img_identity.setImageResource(R.drawable.ic_owner);
        } else {
            img_identity.setImageResource(R.drawable.user);
        }

        String nameStr = Constant.userProfileBean.getProfile().getFirstName();
        if (nameStr == null || nameStr.equals("")) {
            name.setText("火星人");
        } else {
            name.setText(nameStr);
        }

        phone.setText(PreferenceUtil.getString(Constant.UserManager.USERNAME));
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.head_userimg:
                showPopupWindow(head_userimg);
                break;
            case R.id.user_management_btn:
//                intent = new Intent(this, FamilyMemberMgrActivity.class);
                intent = new Intent(this, UserManagementActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                startActivity(intent);
                break;
            case R.id.modify_passwrod_btn:
                intent.setClass(UserCenterActivity_Now.this, ModifyPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.cancellation_btn:
                getSingOut();
                break;
            case R.id.edit_name_btn:
                intent.setClass(UserCenterActivity_Now.this, ModifyActivity.class);
                intent.putExtra("TAG", 3);
                startActivityForResult(intent, 2000);
                break;
            case R.id.head_back:
                this.finish();
                break;
        }
    }

    @SuppressLint("InflateParams")
    @SuppressWarnings("deprecation")
    private void showPopupWindow(View parent) {
        if (popWindow == null) {
            View view = layoutInflater.inflate(R.layout.pop_select_photo, null);
            popWindow = new PopupWindow(view, ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.MATCH_PARENT, true);
            initPop(view);
        }
        popWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    // 实例化pop
    public void initPop(View view) {
        photograph = (TextView) view.findViewById(R.id.photograph);
        albums = (TextView) view.findViewById(R.id.albums);
        cancel = (LinearLayout) view.findViewById(R.id.cancel);
        photograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                popWindow.dismiss();
                takePhoto();
            }
        });
        albums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                popWindow.dismiss();
//                Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                openAlbumIntent
//                        .setDataAndType(
//                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                                "image/*");

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);


//                startActivityForResult(openAlbumIntent,
//                        SELECT_PIC_BY_PICK_PHOTO);

//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
//                startActivityForResult(intent,SELECT_PIC_BY_PICK_PHOTO);

//                Intent intent = new Intent("android.intent.action.GET_CONTENT");
//                intent.setType("image/*");
//                startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                popWindow.dismiss();

            }
        });
    }

    /**
     * 拍照获取图片
     */
    private void takePhoto() {
        // 执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
            /***
             * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
             * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
             */
            ContentValues values = new ContentValues();
            photoUri = this.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
            /** ----------------- */
            startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
        } else {
            Toast.makeText(this, "内存卡不存在", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 选择图片后，获取图片的路径
     *
     * @param requestCode
     * @param data
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("NewApi")
    private void doPhoto(int requestCode, Intent data) {


        if (requestCode == SELECT_PIC_BY_PICK_PHOTO) // 从相册取图片，有些手机有异常情况，请注意
        {

            if (data != null) {
                photoUri = data.getData();
            }


            if (photoUri == null) {
                Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                return;
            }
        }


        if (requestCode == SELECT_PIC_BY_TACK_PHOTO) // 从相册取图片，有些手机有异常情况，请注意
        {

            if (data != null) {
                photoUri = data.getData();
            }


            if (photoUri == null) {
                Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
                return;
            }
        }


        try {
            if (DocumentsContract.isDocumentUri(UserCenterActivity_Now.this,
                    photoUri)) {

                String wholeID = DocumentsContract.getDocumentId(photoUri);

                String id = wholeID.split(":")[1];

                String[] column = {MediaStore.Images.Media.DATA};

                String sel = MediaStore.Images.Media._ID + "=?";

                Cursor cursor = UserCenterActivity_Now.this.getContentResolver()
                        .query(

                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column,
                                sel,

                                new String[]{id}, null);

                int columnIndex = cursor.getColumnIndex(column[0]);

                if (cursor.moveToFirst()) {

                    picPath = cursor.getString(columnIndex);

                }

                if (Build.VERSION.SDK_INT < 14) {
                    cursor.close();
                }

            } else {

                String[] pojo = {MediaStore.Images.Media.DATA};
                @SuppressWarnings("deprecation")

                // ContentResolver cr = this.getContentResolver();
                        // Cursor  cursor = cr.query(photoUri, pojo, null, null, null);
                        Cursor cursor = managedQuery(photoUri, pojo, null, null, null);
                try {
                    if (cursor != null) {
                        int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                        cursor.moveToFirst();
                        picPath = cursor.getString(columnIndex);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (null != cursor) {
                        if (Build.VERSION.SDK_INT < 14) {
                            cursor.close();
                        }
                    }
                }

            }
        } catch (NoClassDefFoundError e) {

            String[] pojo = {MediaStore.Images.Media.DATA};
            @SuppressWarnings("deprecation")
            Cursor cursor = managedQuery(photoUri, pojo, null, null, null);
            try {
                if (cursor != null) {
                    int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                    cursor.moveToFirst();
                    picPath = cursor.getString(columnIndex);
                }
            } catch (NullPointerException d) {
                Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();

            } catch (Exception i) {
                i.printStackTrace();
            } finally {
                if (null != cursor) {
                    if (Build.VERSION.SDK_INT < 14) {
                        cursor.close();
                    }
                }
            }
        }

        if (picPath != null
                && (picPath.endsWith(".png") || picPath.endsWith(".PNG")
                || picPath.endsWith(".jpg") || picPath.endsWith(".JPG"))) {
            Intent intent3 = new Intent(UserCenterActivity_Now.this,
                    ClipActivity.class);
            intent3.putExtra("path", picPath);
            startActivityForResult(intent3, IMAGE_COMPLETE);
        } else {
            Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
        }
    }


    private void getUpLoadImg(File file) {
        kProgressHUD.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Httputils.HOSTUTIL_HTTPS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPIManager.getOkHttpClient(getApplicationContext()))
                .build();

        HttpInterface httpInterface = retrofit.create(HttpInterface.class);


        // 创建 RequestBody，用于封装 请求RequestBody
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

// MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        Call<ResultBean> call = httpInterface.uploadImage("smart/v1/Public/FileUpload?accessToken=" + PreferenceUtil.getString("accessToken"), body);

        call.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {


                ResultBean resultBean = response.body();
                if (resultBean.getCode() == 200) {
                    HeadPortrait headPortrait = gson.fromJson(gson.toJson(resultBean.getResult()), HeadPortrait.class);
                    getSubmitHeadPortrait(headPortrait.getUrl(), headPortrait.getBase_url());
                } else {
                    kProgressHUD.dismiss();
                    AppUtils.show(UserCenterActivity_Now.this, "失败");
                }

            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                kProgressHUD.dismiss();
                AppUtils.show(UserCenterActivity_Now.this, "失败");
            }
        });
    }


    private void getSubmitHeadPortrait(String imgUrl, String baseUrl) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Httputils.HOSTUTIL_HTTPS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPIManager.getOkHttpClient(getApplicationContext()))
                .build();

        HttpInterface httpInterface = retrofit.create(HttpInterface.class);

        AccountChangeGinseng accountChangeGinseng = new AccountChangeGinseng();
        accountChangeGinseng.setAvatar_path(imgUrl);
        accountChangeGinseng.setName(Constant.userProfileBean.getProfile().getFirstName() + "");
        accountChangeGinseng.setAvatar_base_url(baseUrl);
//        accountChangeGinseng.setEmail("SmartHome@mail.com");
        accountChangeGinseng.setMobile(PreferenceUtil.getString(Constant.UserManager.USERNAME));


        Call<ResultBean> call = httpInterface.getAccountChange("smart/v1/Account/Profile?accessToken=" + PreferenceUtil.getString("accessToken"), accountChangeGinseng);

        call.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                kProgressHUD.dismiss();
                ResultBean resultBean = response.body();
                if (resultBean.getCode() == 200) {

                    UserProfileBean u = gson.fromJson(gson.toJson(resultBean.getResult()), UserProfileBean.class);

                    if (Constant.userProfileBean == null) {
                        Constant.userProfileBean = gson.fromJson(PreferenceUtil.getString("user_profile"), UserProfileBean.class);
                    }
                    Constant.userProfileBean.setInfo(u.getInfo());
                    Constant.userProfileBean.setHouses((ArrayList<Houses>) u.getHouses());
                    Constant.userProfileBean.setProfile(u.getProfile());
                    PreferenceUtil.putString("user_profile", gson.toJson(Constant.userProfileBean));

                    String imgUrl = Constant.userProfileBean.getProfile().getAvatarPath();
                    if (imgUrl != null) {
                        imgUrl = imgUrl.replace("https", "http");
                        head_userimg.setImageURI(imgUrl);
                    }
                    EventBus.getDefault().post(new EventBus_Account(Constant.EVENT_BUS_MODIFY_PERSONAL_INFORMATION));
                } else {
                    AppUtils.show(UserCenterActivity_Now.this, "失败");
                }

            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                kProgressHUD.dismiss();
                AppUtils.show(UserCenterActivity_Now.this, "失败");
            }
        });

    }


    private void getSubmitName(String nameStr) {
        kProgressHUD.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Httputils.HOSTUTIL_HTTPS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPIManager.getOkHttpClient(getApplicationContext()))
                .build();

        HttpInterface httpInterface = retrofit.create(HttpInterface.class);


        if (Constant.userProfileBean == null) {
            Constant.userProfileBean = gson.fromJson(PreferenceUtil.getString("user_profile"), UserProfileBean.class);
        }

        AccountChangeGinseng accountChangeGinseng = new AccountChangeGinseng();
        accountChangeGinseng.setAvatar_path(Constant.userProfileBean.getProfile().getAvatarPath());
        accountChangeGinseng.setName(nameStr);
        accountChangeGinseng.setAvatar_base_url(Constant.userProfileBean.getProfile().getAvatarBaseUrl());
//        accountChangeGinseng.setEmail("1052539046@qq.com");
        accountChangeGinseng.setMobile(PreferenceUtil.getString(Constant.UserManager.USERNAME));


        Call<ResultBean> call = httpInterface.getAccountChange("smart/v1/Account/Profile?accessToken=" + PreferenceUtil.getString("accessToken"), accountChangeGinseng);

        call.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                kProgressHUD.dismiss();
                ResultBean resultBean = response.body();
                if (resultBean.getCode() == 200) {

                    UserProfileBean u = gson.fromJson(gson.toJson(resultBean.getResult()), UserProfileBean.class);
                    if (Constant.userProfileBean == null) {
                        Constant.userProfileBean = gson.fromJson(PreferenceUtil.getString("user_profile"), UserProfileBean.class);
                    }
                    Constant.userProfileBean.setInfo(u.getInfo());
                    Constant.userProfileBean.setHouses((ArrayList<Houses>) u.getHouses());
                    Constant.userProfileBean.setProfile(u.getProfile());

                    PreferenceUtil.putString("user_profile", gson.toJson(Constant.userProfileBean));

                    String nameStr = Constant.userProfileBean.getProfile().getFirstName();
                    if (nameStr == null || nameStr.equals("")) {
                        name.setText("火星人");
                    } else {
                        name.setText(nameStr);
                    }
                    EventBus.getDefault().post(new EventBus_Account(Constant.EVENT_BUS_MODIFY_PERSONAL_REMARKS));
                } else {
                    AppUtils.show(UserCenterActivity_Now.this, "失败");
                }

            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                kProgressHUD.dismiss();
                AppUtils.show(UserCenterActivity_Now.this, "失败");
            }
        });

    }


    private void getSingOut() {
        kProgressHUD.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Httputils.HOSTUTIL_HTTPS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(RetrofitAPIManager.getOkHttpClient(getApplicationContext()))
                .build();

        HttpInterface httpInterface = retrofit.create(HttpInterface.class);
        Call<ResultBean> call = httpInterface.getSignOut("smart/v1/Account/SignOut?accessToken=" + PreferenceUtil.getString("accessToken"));

        call.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(Call<ResultBean> call, Response<ResultBean> response) {
                kProgressHUD.dismiss();
                ResultBean resultBean = response.body();


                if (resultBean.getCode() == 200) {
                    if (MainActivity.mainActivity != null) {
                        MainActivity.mainActivity.finish();
                    }
//                    getSingOutDelete();
                    String name = PreferenceUtil.getString(Constant.UserManager.USERNAME);
                    PreferenceUtil.clear();
                    PreferenceUtil.putString(Constant.UserManager.USERNAME, name);
                    if (MainActivity.mainActivity != null) {
                        MainActivity.mainActivity.finish();
                    }
                    Intent intent = new Intent();
                    intent.setClass(UserCenterActivity_Now.this, LoginActivity.class);
                    intent.putExtra("TYPE", 100);
                    startActivity(intent);
                    finish();

                } else {
                    AppUtils.show(UserCenterActivity_Now.this, "失败");
                }

            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {
                kProgressHUD.dismiss();
                AppUtils.show(UserCenterActivity_Now.this, "失败");
            }
        });

    }

    private void getSingOutDelete() {
//        FileUtil.clearValue(UserCenterActivity.this, Constants.SP_ROLE);
//        FileUtil.clearValue(UserCenterActivity.this, Constants.SP_OUTHOUSE);
//        FileUtil.clearValue(UserCenterActivity.this, Constants.SP_HOUSE);
//        FileUtil.clearValue(UserCenterActivity.this, Constants.SP_HOUSE_ID);
//        FileUtil.clearValue(UserCenterActivity.this, Constants.SP_UID);
//        FileUtil.clearValue(UserCenterActivity.this, Constants.SP_TOKEN);
//        FileUtil.clearValue(UserCenterActivity.this, Constants.SP_WEATHER);
//        FileUtil.clearValue(UserCenterActivity.this, Constants.SP_NUMBER_TEMPERATURE);
//        FileUtil.clearValue(UserCenterActivity.this, Constants.SP_GOTOWORK);
//        FileUtil.clearValue(UserCenterActivity.this, Constants.USERNAME);
//        FileUtil.clearValue(UserCenterActivity.this, Constants.PASSWORD);
//        FileUtil.clearValue(UserCenterActivity.this, Constants.HOUSEJSON);
//        FileUtil.clearValue(UserCenterActivity.this, Constants.JZYSTR);
//        FileUtil.clearValue(UserCenterActivity.this, Constants.ACCESSTOKEN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (resultCode != RESULT_OK || resultCode!=101) {
//            return;
//        }
        Uri uri = null;
        switch (requestCode) {
            case SELECT_PIC_BY_PICK_PHOTO:
                doPhoto(requestCode, data);
                break;
            case SELECT_PIC_BY_TACK_PHOTO:
                doPhoto(requestCode, data);
                break;
            case IMAGE_COMPLETE:
//                final String temppath = data.getStringExtra("path");


//                img_head_portrait.setImageBitmap(getLoacalBitmap(temppath));
//                try {
//                    uploadFile(temppath);
//                } catch (Exception e) {
//                    e.printStackTrace();
//        }
//                FileUtil.saveString(PersonalDataActivity.this,
//                        Constants.LOGIN_HEAD_IMG,
//                        temppath);

//                EventBus.getDefault().post(new MessageEvent(Constants.CHAGEHEADIMG));
                //AppUtil.sendChangeImgIntent();


                if(data==null){

                }else{
                    if(data.getStringExtra("path")==null || data.getStringExtra("path").equals("")){

                    }else{
                        final String temppath = data.getStringExtra("path");
                        File file = new File(temppath);
                        getUpLoadImg(file);
                    }
                }

//                if(data.getStringExtra("path")!=null || !data.getStringExtra("path").equals("")){
//                    final String temppath = data.getStringExtra("path");
//                    File file = new File(temppath);
//                    getUpLoadImg(file);
//                }

                break;
            case 2000:
                if (resultCode == 101) {
                    getSubmitName(data.getStringExtra("NAME"));
                }
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mam.popOneActivity(UserCenterActivity_Now.this);
    }
}
