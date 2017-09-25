package com.demo.smarthome;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.demo.smarthome.base.activity.BaseSmartHomeActivity;
import com.demo.smarthome.base.utils.MyActivityManager;

/**
 * Created by liukun on 2016/3/21.
 */
public class MessageCenterDetailActivity extends BaseSmartHomeActivity {
    MyActivityManager mam = MyActivityManager.getInstance();
    private String TAG="MessageCenterDetailActivity";
    private Context mContext;

    private Toolbar mToolbar;
    private TextView mMessageContent;
    private TextView mMessageTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.message_center_detail_activity);
        mam.pushOneActivity(MessageCenterDetailActivity.this);
        mToolbar = (Toolbar)findViewById(R.id.message_center_detail_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mMessageContent = (TextView)findViewById(R.id.message_detail_text);
        mMessageTime = (TextView)findViewById(R.id.message_detail_time);

        Intent intent = getIntent();
        Integer id = intent.getIntExtra("message_id",-1);
        Log.d(TAG,"Message id is "+id);
        if (id>-1)
        {
            ContentResolver contentresolver= mContext.getContentResolver();
            Uri msgUri= Uri.parse("content://DataContentProvider/message/" + id);
            Cursor cursor = contentresolver.query(msgUri, null, null, null, null);
            if(cursor != null && cursor.moveToFirst()) {
                String content = cursor.getString(cursor.getColumnIndex("title")) + "\r\n" +
                        cursor.getString(cursor.getColumnIndex("content")) + "\r\n" +
                        cursor.getString(cursor.getColumnIndex("time"));
                mMessageContent.setText(content);
            }
            cursor.close();
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
    protected void onDestroy() {
        super.onDestroy();
        mam.popOneActivity(MessageCenterDetailActivity.this);
    }
}
