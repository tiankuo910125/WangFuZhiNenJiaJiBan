package com.demo.smarthome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.demo.smarthome.base.activity.BaseSmartHomeActivity;
import com.demo.smarthome.base.utils.GsonTools;
import com.demo.smarthome.base.utils.MyActivityManager;
import com.demo.smarthome.base.utils.PreferenceUtil;
import com.demo.smarthome.communication.jsonbean.UserProfileBean;
import com.demo.smarthome.ui.adapter.FamilyMemberAdapter;
import com.demo.smarthome.ui.base.NotScrollListView;

/**
 * Created by liukun on 2016/3/17.
 */
public class FamilyMemberMgrActivity extends BaseSmartHomeActivity implements View.OnClickListener{
    private String TAG="FamilyMemberMgrActivity";
    MyActivityManager mam = MyActivityManager.getInstance();
    private Context mContext;
    private NotScrollListView mMemberList;
    private Toolbar mToolbar;
    private FamilyMemberAdapter mFamilyMemberAdapter;

    private UserProfileBean userProfileBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.family_member_management_activity);
        mContext = this;

        mToolbar = (Toolbar)findViewById(R.id.family_member_management_activity_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mam.pushOneActivity(FamilyMemberMgrActivity.this);
        mMemberList = (NotScrollListView)findViewById(R.id.family_member_list);

        userProfileBean = new UserProfileBean();
        userProfileBean = GsonTools.getBean(PreferenceUtil.getString("user_profile"),UserProfileBean.class);
        if (userProfileBean.getHouses().size()>0) {
            mFamilyMemberAdapter = new FamilyMemberAdapter(mContext, userProfileBean.getHouses());
            mMemberList.setAdapter(mFamilyMemberAdapter);
        }else
        {
            Intent intent = new Intent(mContext, HouseManagementActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mam.popOneActivity(FamilyMemberMgrActivity.this);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
