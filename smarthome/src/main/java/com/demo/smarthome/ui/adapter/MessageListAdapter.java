package com.demo.smarthome.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.demo.smarthome.R;

import java.util.List;

/**
 * Created by liukun on 2016/3/21.
 */
public class MessageListAdapter extends BaseAdapter {
    private String TAG="MessageListAdapter";
    private Context mContext;
    private List<String> mTitleList;
    private List<String> mMessageList;
    private List<String> mTimeList;
    private List<String> mMessageType;
    private LayoutInflater mInflater;
    public MessageListAdapter(Context context,List<String> titlelist,List<String> messagelist,List<String> timelist,List<String> type) {
        super();
        mContext = context;
        mMessageList = messagelist;
        mTimeList = timelist;
        mTitleList = titlelist;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMessageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.alert_list_item_adapter,null);
        }
        ((TextView)convertView.findViewById(R.id.alert_item_title)).setText(mTitleList.get(position));
        ((TextView)convertView.findViewById(R.id.alert_item_text)).setText(mMessageList.get(position));
        ((TextView)convertView.findViewById(R.id.alert_item_time)).setText(mTimeList.get(position));
        return convertView;
    }
}
