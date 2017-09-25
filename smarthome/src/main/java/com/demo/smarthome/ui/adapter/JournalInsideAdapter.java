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
 * Created by wangdongyang on 17/2/13.
 */
public class JournalInsideAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;

    public JournalInsideAdapter(Context context,List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if(list.size()>=5){
            return 5;
        }else{
            return list.size();
        }
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
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_jurnal_inside,null);
            holder.jurnal_inside_line_top = convertView.findViewById(R.id.jurnal_inside_line_top);
            holder.jurnal_inside_radio = (ImageView) convertView.findViewById(R.id.jurnal_inside_radio);
            holder.jurnal_inside_line_bottom = convertView.findViewById(R.id.jurnal_inside_line_bottom);
            holder.jurnal_inside_time = (TextView) convertView.findViewById(R.id.jurnal_inside_time);
            holder.jurnal_inside_state1 = (TextView) convertView.findViewById(R.id.jurnal_inside_state1);
            holder.jurnal_inside_state2 = (TextView) convertView.findViewById(R.id.jurnal_inside_state2);
            holder.jurnal_inside_state3 = (TextView) convertView.findViewById(R.id.jurnal_inside_state3);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        if(position == 0){
            holder.jurnal_inside_line_top.setVisibility(View.INVISIBLE);
            holder.jurnal_inside_line_bottom.setVisibility(View.VISIBLE);
        }else if(position == 4){
            holder.jurnal_inside_line_top.setVisibility(View.VISIBLE);
            holder.jurnal_inside_line_bottom.setVisibility(View.INVISIBLE);
        }else{
            holder.jurnal_inside_line_top.setVisibility(View.VISIBLE);
            holder.jurnal_inside_line_bottom.setVisibility(View.VISIBLE);
        }

        holder.jurnal_inside_time.setText(list.get(position));

        return convertView;
    }

    class ViewHolder{
        View jurnal_inside_line_top;
        ImageView jurnal_inside_radio;
        View jurnal_inside_line_bottom;
        TextView jurnal_inside_time;
        TextView jurnal_inside_state1;
        TextView jurnal_inside_state2;
        TextView jurnal_inside_state3;
    }

}
