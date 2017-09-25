package com.demo.smarthome;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.demo.smarthome.base.activity.BaseSmartHomeActivity;
import com.demo.smarthome.base.utils.MyActivityManager;

/**
 * Created by liukun on 2016/3/17.
 */
public class FeedbackActivity extends BaseSmartHomeActivity {
    MyActivityManager mam = MyActivityManager.getInstance();
    private String TAF="FeedbackActivity";
    private Context mContext;
    private EditText mFeedbackEdit;
    private Button mSubmit;
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_activity);
        mam.pushOneActivity(FeedbackActivity.this);
        mContext = this;
        mFeedbackEdit = (EditText)findViewById(R.id.feedback_edit);
        mSubmit = (Button)findViewById(R.id.feedback_submit_btn);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //TODO:意见反馈上传服务器

            onBackPressed();
            }
        });

        mToolbar = (Toolbar)findViewById(R.id.feedback_activity_toolbar);
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
        mam.popOneActivity(FeedbackActivity.this);
    }
}
