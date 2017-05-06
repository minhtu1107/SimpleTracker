package com.unicorn.simpletracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.unicorn.simpletracker.core.GoogleServiceManager;

public class FilterEventItemActivity extends AppCompatActivity {

    public static String VIEW_FILTER = "VIEW_FILTER";
    private Button m_MarkItem;
    private Button m_ViewItem;
    private Button m_Download;
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

        m_Download = (Button) findViewById(R.id.download_item_button);
        m_Download.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                GoogleServiceManager.GetInstance().ConnnectAndDownload(FilterEventItemActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case GoogleServiceManager.RESOLVE_CONNECTION_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
//                    mGoogleApiClient.connect();
                }
                if(resultCode == RESULT_CANCELED)
                {
                }
                break;
            case GoogleServiceManager.REQUEST_CODE_OPENER:
                if (resultCode == RESULT_OK) {
                    DriveId driveId = (DriveId) data.getParcelableExtra(OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
                    GoogleServiceManager.GetInstance().SaveFile(driveId, m_eventName);
                }
                break;
        }
    }
}
