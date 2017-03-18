package com.unicorn.simpletracker;

import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.unicorn.simpletracker.BackgroundTask.BackGroudTask;
import com.unicorn.simpletracker.BackgroundTask.BaseTask;
import com.unicorn.simpletracker.core.Attender;
import com.unicorn.simpletracker.core.Mail;
import com.unicorn.simpletracker.core.Utils;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventDetailActivity extends AppCompatActivity implements BaseTask{

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
            SuperOnBack();
        }
        else {
            alertDialog.show();
            new BackGroudTask(this).execute(SAVE_TASK);
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
//            case R.id.action_export:
//                Utils.ExportCSV(m_eventName, m_attend, "export.csv", true);
//                Toast.makeText(EventDetailActivity.this, "Export Done", Toast.LENGTH_SHORT).show();
//                alertDialog.show();
//                new SaveTask().execute(new String[]{"export.csv", "Export Done"});
//                return true;
            case R.id.action_reload:
                alertDialog.show();
                new BackGroudTask(this).execute(RELOAD_TASK);
                return true;
            case R.id.action_clear:
                inputSearch.setText("");
                return true;
            case R.id.action_mail:
                MailDialog();
                return true;
            case R.id.action_add:
                AddDialog();
                return true;
            default:
                Toast.makeText(EventDetailActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
        }
    }

    public void AddDialog()
    {
        LayoutInflater li = LayoutInflater.from(this);
        View vi = li.inflate(R.layout.input_dialog, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(vi);

        TextView tex = (TextView) vi.findViewById(R.id.textView1);
        tex.setText("ID");
        final EditText userID = (EditText) vi.findViewById(R.id.editTextDialogUserInput);

        TextView tex1 = (TextView) vi.findViewById(R.id.textView2);
        tex1.setText("Name");
        tex1.setVisibility(View.VISIBLE);
        final EditText userName = (EditText) vi.findViewById(R.id.field_name);
        userName.setVisibility(View.VISIBLE);

        final CheckBox userAttend = (CheckBox) vi.findViewById(R.id.field_attend);
        userAttend.setVisibility(View.VISIBLE);

        //build dialog
        alert.setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String ID = userID.getText().toString().trim();
                                String Name = userName.getText().toString().trim();
                                if(ID.length()>0 && Name.length()>0) {
                                    ArrayList<String> data = new ArrayList<String>();
                                    data.add(ID);
                                    data.add(Name);
                                    Attender att = new Attender(data);
                                    att.setAttend(userAttend.isChecked());
                                    m_attend.add(att);

                                    dialog.cancel();
                                    alertDialog.show();
                                    new BackGroudTask(EventDetailActivity.this).execute(ADD_TASK);
                                }
                                else
                                    Toast.makeText(EventDetailActivity.this, "Invalid attendance", Toast.LENGTH_SHORT).show();
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

    private String m_mailAddress = null;
    public void MailDialog()
    {
        LayoutInflater li = LayoutInflater.from(this);
        View vi = li.inflate(R.layout.input_dialog, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(vi);

        TextView tex = (TextView) vi.findViewById(R.id.textView1);
        tex.setText("Enter mail address");
        final EditText userinput = (EditText) vi.findViewById(R.id.editTextDialogUserInput);
        //build dialog
        alert.setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                m_mailAddress = userinput.getText().toString().trim();
                                if(m_mailAddress.length()>0) {
                                    dialog.cancel();
                                    alertDialog.show();
                                    new BackGroudTask(EventDetailActivity.this).execute(MAIL_TASK);
                                }
                                else
                                    Toast.makeText(EventDetailActivity.this, "No mail address", Toast.LENGTH_SHORT).show();
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

    public void SendMail()
    {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        String filename="export.csv";
        File Root= Environment.getExternalStorageDirectory();
        String filelocation=Root.getAbsolutePath() + Utils.EXPORT_PATH + m_eventName + "/" + filename;

        //Explicitly only use Gmail to send
//        emailIntent.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"minhtu107@hotmail.com"}); // recipients
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Email subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message text");
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+filelocation));
        startActivity(emailIntent);
    }

    public int JavaMail()
    {
        String filename=m_eventName;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy_HH-mm");
        String currentDateandTime = sdf.format(new Date());

        filename += "_" + currentDateandTime + ".csv";
        File Root= Environment.getExternalStorageDirectory();
        String filelocation=Root.getAbsolutePath() + Utils.EXPORT_PATH + m_eventName + "/" + filename;
//        System.out.println("Mail    " + filename);

        if(m_attend.size() == 0)
            return MAIL_RESULT_EMPTY_LIST;
        Utils.ExportCSV(m_eventName, m_attend, filename, true);

//        Mail m = new Mail("attendance102@gmail.com", "Attendance@");
        Mail m = new Mail("attendance102@gmail.com", "Attendance@");

        String[] toArr = {m_mailAddress};
        m.setTo(toArr);
        m.setFrom("SimpleTracker");
        m.setSubject("Attendance list: " + filename);
        m.setBody("Attendance list: " + filename);

        try {
            m.addAttachment(filelocation);

            if(m.send()) {
//                Toast.makeText(EventDetailActivity.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                return MAIL_RESULT_SUCCESS;
            } else {
//                Toast.makeText(EventDetailActivity.this, "Email was not sent.", Toast.LENGTH_LONG).show();
                return MAIL_RESULT_NOT_SUCCESS;
            }
        } catch(Exception e) {
//            Toast.makeText(EventDetailActivity.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
//            Log.e("MailApp", "Could not send email", e);
            return MAIL_RESULT_ERROR;
        }
    }

    public void ReloadData()
    {
        m_attend = Utils.LoadAttendList(m_eventName, getApplicationContext(), false);
        m_evtAdapter.ReloadData(m_attend);
    }

    private static final int RELOAD_TASK = 0;
    private static final int SAVE_TASK = RELOAD_TASK + 1;
    private static final int ADD_TASK = SAVE_TASK + 1;
    private static final int MAIL_TASK = ADD_TASK + 1;

    private static final int MAIL_RESULT_EMPTY_LIST = MAIL_TASK + 1;
    private static final int MAIL_RESULT_SUCCESS = MAIL_RESULT_EMPTY_LIST + 1;
    private static final int MAIL_RESULT_NOT_SUCCESS = MAIL_RESULT_SUCCESS + 1;
    private static final int MAIL_RESULT_ERROR = MAIL_RESULT_NOT_SUCCESS + 1;
    @Override
    public int doInBackground(int param) {
        switch (param)
        {
            case RELOAD_TASK:
                ReloadData();
                return param;
            case ADD_TASK:
            case SAVE_TASK:
                Utils.ExportCSV(m_eventName, m_attend, "last_session.csv", false);
                return param;
            case MAIL_TASK:
                return JavaMail();
            default:
                break;
        }
        return -1;
    }

    @Override
    public void onPostExecute(int result) {
        alertDialog.dismiss();
        switch (result)
        {
            case RELOAD_TASK:
                m_evtAdapter.notifyDataSetChanged();
                Toast.makeText(EventDetailActivity.this, "Reload Done", Toast.LENGTH_SHORT).show();
                break;
            case ADD_TASK:
                Toast.makeText(EventDetailActivity.this, "Add Done", Toast.LENGTH_SHORT).show();
                break;
            case SAVE_TASK:
                SuperOnBack();
                Toast.makeText(EventDetailActivity.this, "Save Done", Toast.LENGTH_SHORT).show();
                break;
            case MAIL_RESULT_EMPTY_LIST:
                Toast.makeText(EventDetailActivity.this, "No Attendance", Toast.LENGTH_SHORT).show();
                break;
            case MAIL_RESULT_SUCCESS:
                Toast.makeText(EventDetailActivity.this, "Email was sent successfully", Toast.LENGTH_SHORT).show();
                break;
            case MAIL_RESULT_NOT_SUCCESS:
                Toast.makeText(EventDetailActivity.this, "Email was not sent", Toast.LENGTH_SHORT).show();
                break;
            case MAIL_RESULT_ERROR:
                Toast.makeText(EventDetailActivity.this, "There was a problem sending the email", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
