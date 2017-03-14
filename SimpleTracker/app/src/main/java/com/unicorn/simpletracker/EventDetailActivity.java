package com.unicorn.simpletracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.unicorn.simpletracker.core.Attender;
import com.unicorn.simpletracker.core.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class EventDetailActivity extends AppCompatActivity {

    private ListView simpleList;
    private EditText inputSearch;
    private EventItemDetailAdapter m_evtAdapter;
    private ArrayList<Attender> m_attend;
    private String m_eventName;
    private boolean isOnlyView;

    private void ShowThreeDot()
    {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

//        ShowThreeDot();

        simpleList = (ListView)findViewById(R.id.list_view);

        //test
//        m_attend = new ArrayList<Attender>();
//        m_attend.add(new Attender("Tran Hien Minh Tu", "1234567", "May Tinh"));
//        m_attend.add(new Attender("Vo Tan Dat", "7654321", "Moi Truong"));
        //end test
        m_eventName = this.getIntent().getStringExtra(ListEventsActivity.EVENT_NAME);
        isOnlyView = this.getIntent().getBooleanExtra(FilterEventItemActivity.VIEW_FILTER, true);

        m_attend = Utils.LoadAttendList(m_eventName, getApplicationContext(), true, isOnlyView);
        m_evtAdapter = new EventItemDetailAdapter(m_attend, this);
        simpleList.setAdapter(m_evtAdapter);
        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Attender att = (Attender) m_evtAdapter.getItem(position);
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
    public void onBackPressed() {
        if(isOnlyView)
        {
//            Utils.ExportCSV(m_eventName, m_attend, "export.csv", true);
//            Toast.makeText(EventDetailActivity.this, "Export Done", Toast.LENGTH_SHORT).show();
        }
        else {
            Utils.ExportCSV(m_eventName, m_attend, "last_session.csv", false);
            Toast.makeText(EventDetailActivity.this, "Save Done", Toast.LENGTH_SHORT).show();
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the view_menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.view_menu, menu);

//        if(view_menu.getClass().getSimpleName().equals("MenuBuilder")){
//            try{
//                Method m = view_menu.getClass().getDeclaredMethod(
//                        "setOptionalIconsVisible", Boolean.TYPE);
//                m.setAccessible(true);
//                m.invoke(view_menu, true);
//            }
//            catch(NoSuchMethodException e){
//                Log.e(TAG, "onMenuOpened", e);
//            }
//            catch(Exception e){
//                throw new RuntimeException(e);
//            }
//        }

        if(isOnlyView)
        {
            menu.setGroupVisible(R.id.mark_menu_group, false);
        }
        else {
            menu.setGroupVisible(R.id.view_menu_group, false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_export:
                Utils.ExportCSV(m_eventName, m_attend, "export.csv", true);
                Toast.makeText(EventDetailActivity.this, "Export Done", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_reload:
                ReloadData();
                Toast.makeText(EventDetailActivity.this, "Reload Done", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_clear:
                inputSearch.setText("");
                return true;
            default:
                Toast.makeText(EventDetailActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
        }
    }

    public void ReloadData()
    {
        m_attend = Utils.LoadAttendList(m_eventName, getApplicationContext(), false);
        m_evtAdapter.ReloadData(m_attend);
        m_evtAdapter.notifyDataSetChanged();
    }
}
