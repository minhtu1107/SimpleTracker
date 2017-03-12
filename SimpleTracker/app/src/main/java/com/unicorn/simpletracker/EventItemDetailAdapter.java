package com.unicorn.simpletracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.unicorn.simpletracker.core.Attender;

import java.util.ArrayList;

/**
 * Created by tu.tranhienminh on 3/12/2017.
 */
public class EventItemDetailAdapter extends BaseAdapter {

    private ArrayList<Attender> m_attend;
    private LayoutInflater layoutInflater;
    private Context context;

    public EventItemDetailAdapter(ArrayList<Attender> m_attend, Context context) {
        this.m_attend = m_attend;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return m_attend.size();
    }

    @Override
    public Object getItem(int position) {
        return m_attend.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.item_event_detail, null);
            holder = new ViewHolder();
            holder.m_Name = (TextView) convertView.findViewById(R.id.name);
            holder.m_ID = (TextView) convertView.findViewById(R.id.id);
            holder.m_Course = (TextView) convertView.findViewById(R.id.course);
            holder.m_isAttend = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        Attender att = this.m_attend.get(position);
        holder.m_Name.setText(att.getName());
        holder.m_ID.setText(att.getID());
        holder.m_Course.setText(att.getCourse());
        holder.m_isAttend.setChecked(att.isAttend());

        return convertView;
    }

    static class ViewHolder
    {
        TextView m_Name;
        TextView m_ID;
        TextView m_Course;
        CheckBox m_isAttend;
    }
}
