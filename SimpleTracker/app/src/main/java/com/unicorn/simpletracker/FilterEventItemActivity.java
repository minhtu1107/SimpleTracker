package com.unicorn.simpletracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FilterEventItemActivity extends AppCompatActivity {

    public static String VIEW_FILTER = "VIEW_FILTER";
    private Button m_MarkItem;
    private Button m_ViewItem;
    private String m_eventName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_event_item);

        m_eventName = this.getIntent().getStringExtra(ListEventsActivity.EVENT_NAME);
        m_MarkItem =(Button) findViewById(R.id.mark_item_button);
        m_MarkItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterEventItemActivity.this, EventDetailActivity.class);
                intent.putExtra(ListEventsActivity.EVENT_NAME, m_eventName);
                intent.putExtra(VIEW_FILTER, false);
                startActivity(intent);
            }
        });

        m_ViewItem =(Button) findViewById(R.id.view_item_button);
        m_ViewItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterEventItemActivity.this, EventDetailActivity.class);
                intent.putExtra(ListEventsActivity.EVENT_NAME, m_eventName);
                intent.putExtra(VIEW_FILTER, true);
                startActivity(intent);
            }
        });
    }
}
