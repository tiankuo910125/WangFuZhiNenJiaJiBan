package com.demo.smarthome.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.smarthome.ui.base.NotScrollListView;
import com.demo.smarthome.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by liukun on 2016/3/21.
 */
public class OneDayAlertFragmentAdapter extends BaseAdapter {
    private String TAG="OneDayAlertFragmentAdapter";
    private Context mContext;

    private String MONTH[]={"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};
    private String WEEKDAY[]={"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};

    private List<String> listItemMsg;
    private List<String> listItemTitle;
    private List<String> listItemTime;

    private List<String> mDate;
    private List<Integer> mStartList;
    private List<Integer> mEndList;
    private LayoutInflater mInflater;
    public OneDayAlertFragmentAdapter(Context context, List<String> titlelist, List<String> messagelist, List<String> timelist) {
        super();
        mContext = context;
        mInflater = LayoutInflater.from(mContext);

        listItemMsg = messagelist;
        listItemTitle = titlelist;
        listItemTime = timelist;

        mStartList = new ArrayList<>();
        mEndList = new ArrayList<>();

        mDate = new ArrayList<>();
        if (timelist !=null && timelist.size()>0) {
            for (int i = 0; i < timelist.size(); i++) {
                String date = timelist.get(i).substring(0, String.valueOf("yyyy-MM-dd").length());
                if (!mDate.contains(date)) {
                    mDate.add(date);
                    mStartList.add(i);
                    if (i > 0)
                        mEndList.add(i - 1);
                }
            }
            mEndList.add(timelist.size());
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mStartList = new ArrayList<>();
        mEndList = new ArrayList<>();
        mDate = new ArrayList<>();
        if (listItemTime !=null && listItemTime.size()>0) {
            for (int i = 0; i < listItemTime.size(); i++) {
                String date = listItemTime.get(i).substring(0, String.valueOf("yyyy-MM-dd").length());
                if (!mDate.contains(date)) {
                    mDate.add(date);
                    mStartList.add(i);
                    if (i > 0)
                        mEndList.add(i - 1);
                }
            }
            mEndList.add(listItemTime.size());
        }
    }

    @Override
    public int getCount() {
        return mDate.size();
    }

    @Override
    public Object getItem(int position) {
        return mDate.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.everyday_alert_list_adapter,null);
        }

        TextView mDayTextView;
        TextView mMonthTextView;
        TextView mWeekdayTextView;
        NotScrollListView mAlertList;
        AlertItemAdapter mAlertItemAdapter;
        ImageView mDivideLine;

        mDayTextView = (TextView)convertView.findViewById(R.id.day_text);
        mMonthTextView = (TextView)convertView.findViewById(R.id.month_text);
        mWeekdayTextView = (TextView)convertView.findViewById(R.id.weekday_text);
        mAlertList = (NotScrollListView)convertView.findViewById(R.id.alert_list);
        mDivideLine = (ImageView)convertView.findViewById(R.id.vertical_divider);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {

            c.setTime(format.parse(mDate.get(position)));

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int day = c.get(Calendar.DAY_OF_MONTH);
        mDayTextView.setText(String.valueOf(day));
        mMonthTextView.setText(MONTH[c.get(Calendar.MONTH)]);
        mWeekdayTextView.setText(WEEKDAY[c.get(Calendar.DAY_OF_WEEK)-1]);

        mAlertItemAdapter = new AlertItemAdapter(mContext,listItemTitle.subList(mStartList.get(position), mEndList.get(position)),
                listItemMsg.subList(mStartList.get(position), mEndList.get(position)), listItemTime.subList(mStartList.get(position), mEndList.get(position)));
        mAlertList.setAdapter(mAlertItemAdapter);

        mDivideLine.setMinimumHeight(mAlertList.getHeight());

        return convertView;
    }
}
