package com.unicorn.simpletracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.unicorn.simpletracker.core.Attender;

import java.util.ArrayList;

/**
 * Created by tu.tranhienminh on 3/12/2017.
 */
public class EventItemDetailAdapter extends BaseAdapter implements Filterable {

    private ArrayList<Attender> m_attend;
    private ArrayList<Attender> m_attend_filter;
    private LayoutInflater layoutInflater;
    private Context context;
    private ItemFilter mFilter = new ItemFilter();

    public EventItemDetailAdapter(ArrayList<Attender> m_attend, Context context) {
        this.m_attend = m_attend;
        this.m_attend_filter = m_attend;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return m_attend_filter.size();
    }

    @Override
    public Object getItem(int position) {
        return m_attend_filter.get(position);
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

        Attender att = this.m_attend_filter.get(position);

        if(att.getNumOfField() == 1)
        {
            holder.m_Name.setText("Họ Tên: " + att.getName());

            holder.m_ID.setVisibility(View.GONE);
            holder.m_Course.setVisibility(View.GONE);
        }
        else if(att.getNumOfField() == 2)
        {
            holder.m_Name.setText("Họ Tên: " + att.getName());
            holder.m_ID.setText("ID: " + att.getID());
            holder.m_Course.setVisibility(View.GONE);
        }
        else
        {
            holder.m_Name.setText("Họ Tên: " + att.getName());
            holder.m_ID.setText("ID: " + att.getID());
            holder.m_Course.setText("Ngành: " + att.getCourse());

//            System.out.println("aaa " + att.getName() + " " + att.getID() + " " + att.getCourse());
        }

        holder.m_isAttend.setChecked(att.isAttend());
//        holder.m_isAttend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                att.setAttend(isChecked);
//                System.out.println("CheckBox " + m_attend_filter.get(0).isAttend());
//            }
//        });


        return convertView;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    static class ViewHolder
    {
        TextView m_Name;
        TextView m_ID;
        TextView m_Course;
        CheckBox m_isAttend;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            ArrayList<Attender> originalData = m_attend;

            int count = originalData.size();
            ArrayList<Attender> nlist = new ArrayList<Attender>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = originalData.get(i).getName();
                if (filterableString.toLowerCase().startsWith(filterString)) {
                    nlist.add(originalData.get(i));
                }
                else {
                    filterableString = originalData.get(i).getID();
                    if (filterableString.toLowerCase().startsWith(filterString)) {
                        nlist.add(originalData.get(i));
                    }
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            m_attend_filter = (ArrayList<Attender>) results.values;
            notifyDataSetChanged();
        }

    }
}
