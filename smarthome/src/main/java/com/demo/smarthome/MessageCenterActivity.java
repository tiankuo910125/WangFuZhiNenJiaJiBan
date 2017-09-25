package com.demo.smarthome;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.demo.smarthome.base.activity.BaseSmartHomeActivity;
import com.demo.smarthome.base.utils.MyActivityManager;
import com.demo.smarthome.ui.adapter.MessageListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liukun on 2016/3/12.
 */
public class MessageCenterActivity  extends BaseSmartHomeActivity {
    MyActivityManager mam = MyActivityManager.getInstance();
    private String TAG="MessageCenterActivity";
    private Context mContext;

    private Toolbar mToolbar;
    private ListView mMessageList;
    private MessageListAdapter mMessageAdapter;
    private List<String> listItemMsg;
    private List<String> listItemTitle;
    private List<Integer> listItemId;
    private List<String> listItemTime;
    private List<String> listItemType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.message_center_actvity);

        mam.pushOneActivity(MessageCenterActivity.this);
        mToolbar = (Toolbar)findViewById(R.id.message_center_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMessageList = (ListView)findViewById(R.id.message_center_list);
        listItemTitle = new ArrayList<String>();
        listItemMsg = new ArrayList<String>();
        listItemTime = new ArrayList<String>();
        listItemId = new ArrayList<Integer>();
        listItemType = new ArrayList<String>();
         //TODO:从数据库读取历史数据
        ContentResolver contentresolver= mContext.getContentResolver();
        Uri msgUri= Uri.parse("content://DataContentProvider/message");
        Cursor cursor = contentresolver.query(msgUri, new String[]{"id","title","time","content","type"}, null, null, "id desc");
        while (cursor.moveToNext())
        {
            listItemId.add(cursor.getInt(cursor.getColumnIndex("id")));
            listItemTitle.add(cursor.getString(cursor.getColumnIndex("title")));
            listItemMsg.add(cursor.getString(cursor.getColumnIndex("content")));
            listItemTime.add(cursor.getString(cursor.getColumnIndex("time")));
            listItemType.add(cursor.getString(cursor.getColumnIndex("type")));
        }
        cursor.close();

        mMessageAdapter = new MessageListAdapter(this,listItemTitle,listItemMsg,listItemTime,listItemType);
        mMessageList.setAdapter(mMessageAdapter);
        mMessageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext,MessageCenterDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                intent.putExtra("message_id", listItemId.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toobar_clear_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return  super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
        }
        if (id == R.id.action_clear)
        {
            listItemMsg.clear();
            listItemTime.clear();
            mMessageAdapter.notifyDataSetChanged();
            //TODO:数据库清除
            ContentResolver contentresolver= mContext.getContentResolver();
            Uri msgUri= Uri.parse("content://DataContentProvider/message");
            int count = contentresolver.delete(msgUri,null,null);
            Log.d(TAG,"delete database message num is "+count);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mam.popOneActivity(MessageCenterActivity.this);
    }
}
