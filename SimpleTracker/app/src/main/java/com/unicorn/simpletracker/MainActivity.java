package com.unicorn.simpletracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.unicorn.simpletracker.core.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final Context context = this;
    private Button m_NewEvent;
    private Button m_ViewEvent;

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
}
