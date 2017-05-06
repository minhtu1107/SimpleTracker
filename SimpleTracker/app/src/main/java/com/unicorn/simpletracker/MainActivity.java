package com.unicorn.simpletracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.unicorn.simpletracker.core.EventManager;
import com.unicorn.simpletracker.core.GoogleServiceManager;
import com.unicorn.simpletracker.core.Utils;

public class MainActivity extends AppCompatActivity {

    final Context context = this;
    private Button m_NewEvent;
    private Button m_ViewEvent;
    private Button m_Download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.Initialize();
        m_NewEvent =(Button) findViewById(R.id.new_event_button);
        m_NewEvent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Create new event", Toast.LENGTH_SHORT).show();
                //get xml view
                LayoutInflater li = LayoutInflater.from(context);
                View vi = li.inflate(R.layout.input_dialog, null);

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setView(vi);

                final EditText userinput = (EditText) vi.findViewById(R.id.editTextDialogUserInput);
                //build dialog
                alert.setCancelable(false)
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String str = userinput.getText().toString().trim();
                                        if(str.length()>0) {
                                            EventManager.GetInstance().AddEvent(str);
                                        }
                                        updateEvenList();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                // create alert dialog
                AlertDialog alertDialog = alert.create();
                // show it
                alertDialog.show();
            }
        });
        m_ViewEvent = (Button) findViewById(R.id.view_event_button);
        m_ViewEvent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "View all events", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ListEventsActivity.class);
                startActivity(intent);
            }
        });

        //test
//        for(int i = 0; i<10; i++)
//            EventManager.GetInstance().AddEvent("Event "+i);
        //end test
        updateEvenList();


        m_Download = (Button) findViewById(R.id.download_button);
        m_Download.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                ConnnectAndDownload();
            }
        });
        if(!GoogleServiceManager.GetInstance().IsInitial())
        {
            GoogleServiceManager.GetInstance().Initial(getApplicationContext());
        }
    }

    private void ConnnectAndDownload() {
        GoogleServiceManager.GetInstance().ConnnectAndDownload(this);
    }

    public void updateEvenList()
    {
        if(EventManager.GetInstance().GetEventList().size()==0)
            m_ViewEvent.setEnabled(false);
        else
            m_ViewEvent.setEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the view_menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.view_menu.view_menu, view_menu);
        return true;
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
                    String resourceId = driveId.getResourceId();
                    String downloadUrl = "https://drive.google.com/open?id=" + resourceId + "&export=download";
//                    System.out.println("drive.google" + downloadUrl);
//                    open(driveId);
                    GoogleServiceManager.GetInstance().SaveFile(driveId, "root");
                }
                break;
        }
    }

}
