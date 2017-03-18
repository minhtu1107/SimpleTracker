package com.unicorn.simpletracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.unicorn.simpletracker.core.EventManager;
import com.unicorn.simpletracker.core.Utils;

import java.util.ArrayList;

public class ListEventsActivity extends AppCompatActivity {

    final Context context = this;
    private ListView simpleList;
    public static String EVENT_NAME = "EVENT_NAME";
    private CheckBox m_Delete;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);
        ArrayList<String> eventlist = EventManager.GetInstance().GetEventList();

        m_Delete = (CheckBox)findViewById(R.id.delete_mode);
        simpleList = (ListView)findViewById(R.id.eventListView);
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_listview, R.id.textView, eventlist);
        simpleList.setAdapter(arrayAdapter);

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String eventName = EventManager.GetInstance().GetEventList().get(position);
                if(m_Delete.isChecked())
                {
                    LayoutInflater li = LayoutInflater.from(context);
                    View vi = li.inflate(R.layout.input_dialog, null);

                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setView(vi);

                    EditText userinput = (EditText) vi.findViewById(R.id.editTextDialogUserInput);
                    userinput.setVisibility(View.GONE);
                    TextView title = (TextView) vi.findViewById(R.id.textView1);
                    title.setText("Do you want to delete \"" + eventName + "\"?");

                    //build dialog
                    alert.setCancelable(false)
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Utils.DeleteEvent(eventName);
                                            arrayAdapter.remove(eventName);
                                            arrayAdapter.notifyDataSetChanged();
                                        }
                                    })
                            .setNegativeButton("No",
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
                else {
                    Intent intent = new Intent(ListEventsActivity.this, FilterEventItemActivity.class);
                    intent.putExtra(EVENT_NAME, eventName);
                    startActivity(intent);
                }
            }
        });
    }
}
