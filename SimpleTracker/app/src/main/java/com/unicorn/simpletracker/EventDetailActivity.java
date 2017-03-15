package com.unicorn.simpletracker;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.unicorn.simpletracker.core.Attender;
import com.unicorn.simpletracker.core.Utils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class EventDetailActivity extends AppCompatActivity {

    private ListView simpleList;
    private EditText inputSearch;
    private RadioButton m_byID;
    private RadioButton m_byName;
    private EventItemDetailAdapter m_evtAdapter;
    private ArrayList<Attender> m_attend;
    private String m_eventName;
    private boolean isOnlyView;

    private AlertDialog alertDialog;

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
        if(!isOnlyView) {
            simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Attender att = (Attender) m_evtAdapter.getItem(position);
                    CheckBox c = (CheckBox) view.findViewById(R.id.checkBox);
                    c.setChecked(!att.isAttend());
                    att.setAttend(!att.isAttend());
//                System.out.println("CheckBox    ");
                }
            });
        }
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        inputSearch.setInputType(InputType.TYPE_CLASS_NUMBER);
        m_byID = (RadioButton)findViewById(R.id.by_id);
        m_byName = (RadioButton)findViewById(R.id.by_name);

        m_byID.setChecked(true);
        m_byName.setChecked(false);

        m_byID.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                m_byID.setChecked(true);
                m_byName.setChecked(false);
                inputSearch.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        });

        m_byName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                m_byID.setChecked(false);
                m_byName.setChecked(true);
                inputSearch.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        });

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

        CreateDialog();
    }

    public void CreateDialog()
    {
        LayoutInflater li = LayoutInflater.from(this);
        View vi = li.inflate(R.layout.progress_dialog, null);

        ProgressBar pBar2 = (ProgressBar)vi.findViewById(R.id.progressBar);
        pBar2.setIndeterminate(true);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(vi);
        alert.setCancelable(false);
        // create alert dialog
        alertDialog = alert.create();
        // show it
//        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if(isOnlyView)
        {
//            Utils.ExportCSV(m_eventName, m_attend, "export.csv", true);
//            Toast.makeText(EventDetailActivity.this, "Export Done", Toast.LENGTH_SHORT).show();
            SuperOnBack();
        }
        else {
            alertDialog.show();
            new SaveTask().execute(new String[]{"last_session.csv", "Save Done"});
//            Utils.ExportCSV(m_eventName, m_attend, "last_session.csv", false);
//            alertDialog.dismiss();
//            Toast.makeText(EventDetailActivity.this, "Save Done", Toast.LENGTH_SHORT).show();
        }
    }

    public void SuperOnBack()
    {
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
//                Utils.ExportCSV(m_eventName, m_attend, "export.csv", true);
//                Toast.makeText(EventDetailActivity.this, "Export Done", Toast.LENGTH_SHORT).show();
                alertDialog.show();
                new SaveTask().execute(new String[]{"export.csv", "Export Done"});
                return true;
            case R.id.action_reload:
//                ReloadData();
//                Toast.makeText(EventDetailActivity.this, "Reload Done", Toast.LENGTH_SHORT).show();
                alertDialog.show();
                new SaveTask().execute(new String[]{"Reload Done"});
                return true;
            case R.id.action_clear:
                inputSearch.setText("");
                return true;
            case R.id.action_mail:
                SendMail();
                return true;
            default:
                Toast.makeText(EventDetailActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
        }
    }

//    public void MailDialog()
//    {
//        LayoutInflater li = LayoutInflater.from(this);
//        View vi = li.inflate(R.layout.input_dialog, null);
//
//        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//        alert.setView(vi);
//
//        final EditText userinput = (EditText) vi.findViewById(R.id.editTextDialogUserInput);
//        //build dialog
//        alert.setCancelable(false)
//                .setPositiveButton("Ok",
//                        new DialogInterface.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                .setNegativeButton("Cancel",
//                        new DialogInterface.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//        // create alert dialog
//        AlertDialog alertDialog = alert.create();
//        // show it
//        alertDialog.show();
//    }

    public void SendMail()
    {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        String filename="export.csv";
        File Root= Environment.getExternalStorageDirectory();
        String filelocation=Root.getAbsolutePath() + Utils.EXPORT_PATH + m_eventName + "/" + filename;
//        System.out.println("Mail    " + filelocation);
//        Uri path = Uri.fromFile(filelocation);

        //Explicitly only use Gmail to send
//        emailIntent.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"minhtu107@hotmail.com"}); // recipients
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Email subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message text");
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+filelocation));
        startActivity(emailIntent);
    }

    public void ReloadData()
    {
        m_attend = Utils.LoadAttendList(m_eventName, getApplicationContext(), false);
        m_evtAdapter.ReloadData(m_attend);
    }

    private class SaveTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params) {
//            "last_session.csv"
            if(params.length==1)
            {
                ReloadData();
                return params[0];
            }
            else {
                Utils.ExportCSV(m_eventName, m_attend, params[0], false);
                return params[1];
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
//            "Save Done"
            alertDialog.dismiss();
            Toast.makeText(EventDetailActivity.this, result, Toast.LENGTH_SHORT).show();
            if(result.contains("Save"))
                SuperOnBack();
            if(result.contains("Reload"))
                m_evtAdapter.notifyDataSetChanged();
        }
    }
}
