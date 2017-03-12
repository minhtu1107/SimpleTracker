package com.unicorn.simpletracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.unicorn.simpletracker.core.Attender;
import com.unicorn.simpletracker.core.EventManager;

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
        m_attend = new ArrayList<Attender>();
        m_attend.add(new Attender("Tran Hien Minh Tu", "1234567", "May Tinh"));
        m_attend.add(new Attender("Vo Tan Dat", "7654321", "Moi Truong"));
        //end test

        m_evtAdapter = new EventItemDetailAdapter(m_attend, this);
        simpleList.setAdapter(m_evtAdapter);

        inputSearch = (EditText) findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
//                EventDetailActivity.this.arrayAdapter.getFilter().filter(cs);
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
}
