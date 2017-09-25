package com.demo.smarthome.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.smarthome.base.utils.view_util.CustomListView;
import com.demo.smarthome.communication.jsonbean.LogList;
import com.demo.smarthome.R;

import java.util.List;


/**
 * Created by wangdongyang on 17/2/15.
 */
public class NewJournalAdapter extends RecyclerView.Adapter<NewJournalAdapter.MyViewHolder> {

    private Context context;
    private List<LogList> logs;

    public NewJournalAdapter(Context context,List<LogList> logs) {
        this.context = context;
        this.logs = logs;
    }

    @Override
    public NewJournalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_journal, null);
        MyViewHolder hodler = new MyViewHolder(view);
        return hodler;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        LogList logList = logs.get(position);
        holder.item_journal_day_txt.setText(logList.getDay());
        holder.item_journal_month_txt.setText(logList.getMonth()+"月");
        holder.item_journal_week_txt.setText(logList.getWeek());

        JournalInsideAdapter adapter = new JournalInsideAdapter(context,logList.getLog());
        holder.item_journal_listview.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return logs.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView item_journal_day_txt;
        TextView item_journal_month_txt;
        TextView item_journal_week_txt;
        CustomListView item_journal_listview;


        public MyViewHolder(View view) {
            super(view);
            item_journal_day_txt = (TextView) view.findViewById(R.id.item_journal_day_txt);
            item_journal_month_txt = (TextView) view.findViewById(R.id.item_journal_month_txt);
            item_journal_week_txt = (TextView) view.findViewById(R.id.item_journal_week_txt);
            item_journal_listview = (CustomListView) view.findViewById(R.id.item_journal_listview);
        }
    }
}
