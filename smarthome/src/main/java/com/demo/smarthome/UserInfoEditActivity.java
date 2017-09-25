package com.demo.smarthome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.demo.smarthome.base.activity.BaseSmartHomeActivity;
import com.demo.smarthome.base.utils.MyActivityManager;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.qrcode.CaptureActivity;
import com.demo.smarthome.base.utils.Constant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liukun on 2016/3/14.
 */
public class UserInfoEditActivity extends BaseSmartHomeActivity implements View.OnClickListener {
    private String TAG = "UserInfoEditActivity";
    MyActivityManager mam = MyActivityManager.getInstance();
    private Context mContext;
    private Button mNextBtn;
    private ImageView mHeader;
    private EditText mUsernameEdit;
    private EditText mMobileNumEdit;
    private CheckBox mCheckMale;
    private CheckBox mCheckFemale;
    private EditText mEmailEdit;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.user_info_edit_avtivity);
        mam.pushOneActivity(UserInfoEditActivity.this);
        mNextBtn = (Button) findViewById(R.id.next_btn);
        mHeader = (ImageView) findViewById(R.id.user_info_activity_header_icon);
        mUsernameEdit = (EditText) findViewById(R.id.user_info_edit_activity_username_edit);
        mMobileNumEdit = (EditText) findViewById(R.id.user_info_edit_activity_mobilenum_edit);
        mEmailEdit = (EditText) findViewById(R.id.user_info_edit_activity_email_edit);
        mUsernameEdit.setText(PreferenceUtil.getString(Constant.UserManager.USERNAME));

        mNextBtn.setOnClickListener(this);
        mToolbar = (Toolbar) findViewById(R.id.user_info_activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    protected void onDestroy() {
        super.onDestroy();
        mam.popOneActivity(UserInfoEditActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_btn: {
                gotoNext();
                break;
            }
        }
    }


    private void gotoNext() {
        String username = mUsernameEdit.getText().toString();
        String mobilenum = mMobileNumEdit.getText().toString();
        String email = mEmailEdit.getText().toString();


        if (username.length() == 0) {
            Toast.makeText(this, getString(R.string.user_info_edit_activity_username_error), Toast.LENGTH_SHORT).show();
            return;
        } else if (mobilenum.length() == 0) {
            Toast.makeText(this, getString(R.string.user_info_edit_activity_mobilenum_error), Toast.LENGTH_SHORT).show();
            return;
        } else if (email.length() == 0) {
            Toast.makeText(this, getString(R.string.user_info_edit_activity_email_error), Toast.LENGTH_SHORT).show();
            return;
        }

        Pattern pattern = Pattern.compile("^1[3578][0-9]{9}$");
        Matcher isNum = pattern.matcher(mobilenum);
        if (!isNum.matches()) {
            Toast.makeText(this, getString(R.string.user_info_edit_activity_mobilenum_format_error), Toast.LENGTH_SHORT).show();
            return;
        }

        Pattern p = Pattern.compile("([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})");
        Matcher isEmail = p.matcher(email);
        if (!isEmail.matches()) {
            Toast.makeText(this, getString(R.string.user_info_edit_activity_email_format_error), Toast.LENGTH_SHORT).show();
            return;
        }

        if (username.length() > 0 && mobilenum.length() > 0 && email.length() > 0) {
            PreferenceUtil.putString(Constant.UserManager.USERNAME, username);
            PreferenceUtil.putString(Constant.UserManager.Info.MOBILENUM, mobilenum);
            PreferenceUtil.putString(Constant.UserManager.Info.EMAIL, email);
            //TODO:注册信息上传服务器

            //
            Intent intent = new Intent(this, CaptureActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
            startActivity(intent);
            finish();
        }
    }
}
