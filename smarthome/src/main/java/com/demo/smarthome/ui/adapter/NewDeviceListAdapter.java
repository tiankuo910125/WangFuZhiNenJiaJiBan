package com.demo.smarthome.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.demo.smarthome.R;
import com.demo.smarthome.base.utils.view_util.CustomListView;
import com.demo.smarthome.communication.jsonbean.LogList;

import java.util.List;

/**
 * Created by wangdongyang on 2017/4/24.
 */
public class NewDeviceListAdapter extends BaseAdapter {

    private Context context;
    private List<LogList> logs;

    public NewDeviceListAdapter(Context context, List<LogList> logs) {
        this.context = context;
        this.logs = logs;
    }

    @Override
    public int getCount() {
        return logs.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if(convertView == null){
            holder  = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_journal,null);
            holder.item_journal_day_txt = (TextView) convertView.findViewById(R.id.item_journal_day_txt);
            holder.item_journal_month_txt = (TextView) convertView.findViewById(R.id.item_journal_month_txt);
            holder.item_journal_week_txt = (TextView) convertView.findViewById(R.id.item_journal_week_txt);
            holder.item_journal_listview = (CustomListView) convertView.findViewById(R.id.item_journal_listview);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        LogList logList = logs.get(position);
        holder.item_journal_day_txt.setText(logList.getDay());
        holder.item_journal_month_txt.setText(logList.getMonth()+"月");
        holder.item_journal_week_txt.setText(logList.getWeek());

        JournalInsideAdapter adapter = new JournalInsideAdapter(context,logList.getLog());
        holder.item_journal_listview.setAdapter(adapter);

        return convertView;
    }

    class ViewHolder{
        TextView item_journal_day_txt;
        TextView item_journal_month_txt;
        TextView item_journal_week_txt;
        CustomListView item_journal_listview;
    }

}
