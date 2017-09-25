package com.demo.smarthome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.smarthome.base.utils.MyActivityManager;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.demo.smarthome.base.activity.BaseSmartHomeActivity;
import com.demo.smarthome.base.utils.Constant;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.communication.jsonbean.UserProfileBean;

/**
 * Created by liukun on 2016/2/22.
 */
public class UserCenterActivity extends BaseSmartHomeActivity implements View.OnClickListener{
    MyActivityManager mam = MyActivityManager.getInstance();
    private String TAG = "UserCenterActivity";
    private Gson gson = new Gson();

    private Context mContext;

    private ImageButton mUserManagementBtn;
    private ImageButton mChangePwdBtn;
    private ImageButton mQuitAccountBtn;

    private TextView mUserName;
    private SimpleDraweeView mHeadPic;
    private ImageView mUserIdentify;
    private TextView mUserPhoneNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usercenter_activity);
        mam.pushOneActivity(UserCenterActivity.this);
        mContext = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.user_center_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUserName = (TextView)findViewById(R.id.user_name);
        mUserPhoneNum = (TextView)findViewById(R.id.user_phone_num);
        mUserName.setText(PreferenceUtil.getString(Constant.UserManager.USERNAME));
        mUserPhoneNum.setText(PreferenceUtil.getString(Constant.UserManager.Info.MOBILENUM));

        mUserIdentify = (ImageView)findViewById(R.id.user_identify);

        mUserManagementBtn=(ImageButton)this.findViewById(R.id.user_management_btn);
        mUserManagementBtn.setOnClickListener(this);
        mChangePwdBtn=(ImageButton)this.findViewById(R.id.change_pwd_btn);
        mChangePwdBtn.setOnClickListener(this);
        mQuitAccountBtn=(ImageButton)this.findViewById(R.id.quit_btn);
        mQuitAccountBtn.setOnClickListener(this);

        mHeadPic=(SimpleDraweeView)this.findViewById(R.id.user_center_haed_pic);
        mHeadPic.setOnClickListener(this);
//        mHeadPic.setImageResource(R.drawable.user_black);

        Constant.userProfileBean = gson.fromJson( PreferenceUtil.getString("user_profile"),UserProfileBean.class);
        if(Constant.userProfileBean != null){
            String imgUrl = Constant.userProfileBean.getProfile().getAvatarPath();
            if(imgUrl!=null){
                imgUrl = imgUrl.replace("https","http");
                mHeadPic.setImageURI(imgUrl);
            }
        }
        String roleStr = PreferenceUtil.getString("role");
        if(roleStr!=null && roleStr.equals("owner")){
            mUserIdentify.setImageResource(R.drawable.ic_owner);
        }else{
            mUserIdentify.setImageResource(R.drawable.user);
        }


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
        Intent intent;
        switch (v.getId())
        {
            case R.id.user_center_haed_pic:
                intent = new Intent(this,UserInfoEditActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                startActivity(intent);
                break;

            case R.id.quit_btn:
                Log.d(TAG, getString(R.string.user_center_quit_account_button));
                PreferenceUtil.putBoolean(Constant.UserManager.LOGINSTATE, false);
                intent = new Intent(this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                startActivity(intent);
                finish();
                //TODO:结束所有的界面

                break;

            case R.id.user_management_btn:
                intent = new Intent(this,FamilyMemberMgrActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                startActivity(intent);
                break;

            case R.id.change_pwd_btn:
                intent = new Intent(this,ModifyPasswordActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mam.popOneActivity(UserCenterActivity.this);
    }
}
