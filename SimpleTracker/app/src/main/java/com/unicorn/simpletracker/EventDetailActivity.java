package com.unicorn.simpletracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.unicorn.simpletracker.core.Attender;
import com.unicorn.simpletracker.core.Utils;

import java.util.ArrayList;

public class EventDetailActivity extends AppCompatActivity {

    private ListView simpleList;
    private EditText inputSearch;
    private EventItemDetailAdapter m_evtAdapter;
    private ArrayList<Attender> m_attend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        simpleList = (ListView)findViewById(R.id.list_view);

        //test
//        m_attend = new ArrayList<Attender>();
//        m_attend.add(new Attender("Tran Hien Minh Tu", "1234567", "May Tinh"));
//        m_attend.add(new Attender("Vo Tan Dat", "7654321", "Moi Truong"));
        //end test

        m_attend = Utils.LoadAttendList("A", getApplicationContext());
        m_evtAdapter = new EventItemDetailAdapter(m_attend, this);
        simpleList.setAdapter(m_evtAdapter);
        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Attender att = m_attend.get(position);
                CheckBox c = (CheckBox) view.findViewById(R.id.checkBox);
                c.setChecked(!att.isAttend());
                att.setAttend(!att.isAttend());
//                System.out.println("CheckBox    ");
            }
        });
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                EventDetailActivity.this.m_evtAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                Utils.ExportCSV(m_attend);
                Toast.makeText(EventDetailActivity.this, "Export Done", Toast.LENGTH_SHORT).show();
                return true;
            default:
                Toast.makeText(EventDetailActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
        }
    }
}
