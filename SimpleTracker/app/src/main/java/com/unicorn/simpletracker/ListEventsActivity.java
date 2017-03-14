package com.unicorn.simpletracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.unicorn.simpletracker.core.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListEventsActivity extends AppCompatActivity {

    private ListView simpleList;
    public static String EVENT_NAME = "EVENT_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);
        ArrayList<String> eventlist = EventManager.GetInstance().GetEventList();

        simpleList = (ListView)findViewById(R.id.eventListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_listview, R.id.textView, eventlist);
        simpleList.setAdapter(arrayAdapter);

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListEventsActivity.this, FilterEventItemActivity.class);
                intent.putExtra(EVENT_NAME, EventManager.GetInstance().GetEventList().get(position));
                startActivity(intent);
            }
        });
    }
}
