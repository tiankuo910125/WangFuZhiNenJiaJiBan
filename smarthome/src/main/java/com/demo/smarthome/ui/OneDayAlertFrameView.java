package com.demo.smarthome.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.demo.smarthome.ui.base.NotScrollListView;
import com.demo.smarthome.R;
import com.demo.smarthome.ui.adapter.AlertItemAdapter;
import com.demo.smarthome.ui.base.RoomControlBaseView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by liukun on 2016/3/17.
 */
public class OneDayAlertFrameView extends RoomControlBaseView {
    private Context mContext;
    private LayoutInflater mInflater;
    private NotScrollListView mAlertList;
    private AlertItemAdapter mAlertItemAdapter;

    private TextView mDayTextView;
    private TextView mMonthTextView;
    private TextView mWeekdayTextView;

    private String MONTH[]={"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};
    private String WEEKDAY[]={"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};

    public OneDayAlertFrameView(Context context) {
        super(context);
        init(context);
    }

    public OneDayAlertFrameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public OneDayAlertFrameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mContext = context;
        mInflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.fragment_oneday_alert, this);

        mDayTextView = (TextView)findViewById(R.id.day);
        mMonthTextView = (TextView)findViewById(R.id.month);
        mWeekdayTextView = (TextView)findViewById(R.id.weekday);
        mAlertList = (NotScrollListView)findViewById(R.id.alert_list);

    }

    public void setAlertContent(List<String> titlelist, List<String> messagelist, List<String> timelist){

        mAlertItemAdapter = new AlertItemAdapter(mContext,timelist,titlelist,messagelist);
        mAlertList.setAdapter(mAlertItemAdapter);

    }
    public void setDate(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {

            c.setTime(format.parse(date));

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        mDayTextView.setText(c.get(Calendar.DAY_OF_MONTH));
        mMonthTextView.setText(MONTH[c.get(Calendar.MONTH)-1]);
        mWeekdayTextView.setText(WEEKDAY[c.get(Calendar.DAY_OF_WEEK)-1]);
    }
}
