package com.demo.smarthome.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.smarthome.R;

import java.util.List;

/**
 * Created by liukun on 2016/3/17.
 */
public class RepeatWeekdaytemAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Boolean> mWeekdaySelectedList;

    private String Weekday[]={"周一","周二","周三","周四","周五","周六","周日"};

    public RepeatWeekdaytemAdapter(Context context, List<Boolean> weekdaySelected) {
        super();
        mContext = context;
        mWeekdaySelectedList = weekdaySelected;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mWeekdaySelectedList.size();
    }

    @Override
    public Object getItem(int position) {
        return mWeekdaySelectedList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.repeat_weekday_item_view, null);
        }

        ((TextView)convertView.findViewById(R.id.weekday)).setText(Weekday[position]);
        ImageView selectedView = (ImageView)convertView.findViewById(R.id.selected_signal);
        if (mWeekdaySelectedList.get(position)){
             selectedView.setBackgroundResource(R.drawable.ok_confirm);
        }else
             selectedView.setBackground(null);

        convertView.findViewById(R.id.repeat_weekday).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeekdaySelectedList.set(position, !mWeekdaySelectedList.get(position));
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
