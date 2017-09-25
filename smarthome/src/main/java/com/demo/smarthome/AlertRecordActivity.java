package com.demo.smarthome;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.demo.smarthome.base.activity.BaseSmartHomeActivity;
import com.demo.smarthome.base.utils.MyActivityManager;
import com.demo.smarthome.ui.adapter.OneDayAlertFragmentAdapter;
import com.demo.smarthome.ui.base.NotScrollListView;

import java.util.ArrayList;
import java.util.List;

public class AlertRecordActivity extends BaseSmartHomeActivity {
    MyActivityManager mam = MyActivityManager.getInstance();
    private String TAG = "AlertRecordActivity";
    private Context mContext;

    private Toolbar mToolbar;

    private List<String> listItemMsg;
    private List<String> listItemTitle;
    private List<String> listItemTime;
    private List<String> listItemType;

    private NotScrollListView mEveryDayAlertListView;
    private OneDayAlertFragmentAdapter mOneDayAlertFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_alert_record);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mam.pushOneActivity(AlertRecordActivity.this);

        listItemTitle = new ArrayList<String>();
        listItemMsg = new ArrayList<String>();
        listItemTime = new ArrayList<String>();
        listItemType = new ArrayList<String>();
        //TODO:从数据库读取历史数据
        ContentResolver contentresolver= mContext.getContentResolver();
        Uri msgUri= Uri.parse("content://DataContentProvider/message");
        Cursor cursor = contentresolver.query(msgUri, new String[]{"id","title","time","content","type"}, null, null, "id desc");
        while (cursor.moveToNext())
        {
            listItemTitle.add(cursor.getString(cursor.getColumnIndex("title")));
            listItemMsg.add(cursor.getString(cursor.getColumnIndex("content")));
            listItemTime.add(cursor.getString(cursor.getColumnIndex("time")));
            listItemType.add(cursor.getString(cursor.getColumnIndex("type")));
        }
        cursor.close();

        mEveryDayAlertListView = (NotScrollListView)findViewById(R.id.everyday_alert_list);
        mOneDayAlertFragmentAdapter = new OneDayAlertFragmentAdapter(mContext,listItemTitle,listItemMsg,listItemTime);
        mEveryDayAlertListView.setAdapter(mOneDayAlertFragmentAdapter);

        findViewById(R.id.delete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItemTitle.clear();
                listItemType.clear();
                listItemMsg.clear();
                listItemTime.clear();
                mOneDayAlertFragmentAdapter.notifyDataSetChanged();
                //TODO:数据库清除
                ContentResolver contentresolver= mContext.getContentResolver();
                Uri msgUri= Uri.parse("content://DataContentProvider/message");
                int count = contentresolver.delete(msgUri,null,null);
                Log.d(TAG,"delete database message num is "+count);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mam.popOneActivity(AlertRecordActivity.this);
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

}