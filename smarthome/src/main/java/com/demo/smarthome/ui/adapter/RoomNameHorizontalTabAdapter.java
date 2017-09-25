package com.demo.smarthome.ui.adapter;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.demo.smarthome.R;

import java.util.List;

/**
 * Created by liukun on 2016/3/17.
 */
public class RoomNameHorizontalTabAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mRoomNames;
    private List<Integer> mRoomIndex;
    private LayoutInflater mInflater;
    private int mCheckedItem = 0;

    public RoomNameHorizontalTabAdapter(Context context, List<String> roomnames, List<Integer> roomIndex) {
        super();
        mContext = context;
        mRoomNames = roomnames;
        mRoomIndex = roomIndex;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mRoomNames.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (mRoomIndex == null)
            return position;
        else
            return mRoomIndex.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.room_name_horizontal_tab_adapter, null);
        }
        TextView mRoomname = (TextView)convertView.findViewById(R.id.room_name);

        String str = mRoomNames.get(position);
        String[] sourceStrArray = str.split("--");
        if(sourceStrArray!=null && sourceStrArray.length==2){
            mRoomname.setText(sourceStrArray[1]);
        }else{
            mRoomname.setText("室内");
        }

        if (mCheckedItem == position) {
            mRoomname.setTextSize(16);
            TextPaint tp = mRoomname.getPaint();
            tp.setFakeBoldText(true);
        }else {
            mRoomname.setTextSize(12);
            TextPaint tp = mRoomname.getPaint();
            tp.setFakeBoldText(false);
        }
        return convertView;
    }

    public void setCheckedItem(int position) {
        mCheckedItem = position;
        notifyDataSetChanged();
    }
}
