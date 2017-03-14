package com.unicorn.simpletracker.core;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tu.tranhienminh on 3/13/2017.
 */
public class Utils {
    private static String ROOT_PATH = "SimpleTracker";
    private static String EVENT_PATH = "Events";
    private static String DEFAULT_ATTEND_LIST = "Default.csv";
    private static boolean m_isInitalize = false;

    public static void Initialize()
    {
        if(m_isInitalize)
            return;
        String root = Environment.getExternalStorageDirectory().toString();
        ROOT_PATH = root + "/" + ROOT_PATH;
        EVENT_PATH = ROOT_PATH + "/" + EVENT_PATH;
        File root_folder = new File(ROOT_PATH);
        File event_folder = new File(EVENT_PATH);
        if(!root_folder.exists())
            root_folder.mkdirs();
        if(!event_folder.exists())
            event_folder.mkdirs();
        m_isInitalize = true;
    }

    public static ArrayList<String> GetEventsFolder()
    {
        ArrayList<String> fd = new ArrayList<String>();
        File[] dirs = new File(EVENT_PATH).listFiles();
        for(File f : dirs)
        {
            if(f.isDirectory())
                fd.add(f.getName());
        }
        return fd;
    }

    public static void CreateEvent(String name)
    {
        File event_folder = new File(EVENT_PATH + "/" + name);
        if(!event_folder.exists())
            event_folder.mkdirs();
    }

    public static ArrayList<Attender> LoadAttendList(String event_name, Context con, boolean onlyView)
    {
        return LoadAttendList(event_name, con, false, onlyView);
    }

    @Nullable
    public static ArrayList<Attender> LoadAttendList(String event_name, Context con, boolean isCheckSave, boolean onlyView)
    {
        List<String[]> csv = null;
        InputStream is = null;
        boolean from_save = false;
        try
        {
            File from_event = new File(EVENT_PATH + "/" + event_name + "/data.csv");
            File from_last_session = new File(EVENT_PATH + "/" + event_name + "/last_session.csv");
            File from_root = new File(ROOT_PATH + "/data.csv");
            //check from export save
            if(isCheckSave && from_last_session.exists())
            {
                is = new FileInputStream(from_last_session);
                from_save = true;
            }
            //check in event
            else if(from_event.exists())
            {
                is = new FileInputStream(from_event);
            }
            //check root
            else if(from_root.exists())
            {
                is = new FileInputStream(from_root);
            }
            //default from assets
            else {
                is = con.getAssets().open("data/Studentdata.csv");
            }
            csv = CSVManager.GetInstance().OpenFile(is);
            is.close();
        }
        catch (Exception ex)
        {
            return null;
        }
        ArrayList<Attender> attList = new ArrayList<Attender>();
        for(int i=0; i<csv.size(); i++)
        {
//            for(int j=0; j<csv.get(i).length; j++)
//            {
//                System.out.println("Attender " + csv.get(i)[j]);
//            }
            Attender att = new Attender(new ArrayList<String>(Arrays.asList(csv.get(i))), from_save);
            if((onlyView && att.isAttend()) || !onlyView)
            {
                attList.add(att);
            }
        }
        return attList;
    }

    public static void ExportCSV(String event_name, ArrayList<Attender> attenders, String exName, boolean isFinal)
    {
        List<String[]> data = new ArrayList<String[]>();
        if(attenders.size()==0)
            return;
        int NumOfField = attenders.get(0).getNumOfField();
        for(int i=0; i<attenders.size(); i++)
        {
            String[] strings;
            if(isFinal)
            {
                strings = new String[NumOfField];
            }
            else
            {
                strings = new String[NumOfField + 1];
            }
            for(int j=0; j<NumOfField; j++)
            {
                strings[j] = attenders.get(i).getByField(j);
            }
            if(!isFinal)
                strings[NumOfField] = attenders.get(i).isAttend()?"1":"0";

            data.add(strings);
        }
        CSVManager.GetInstance().Export(EVENT_PATH + "/" + event_name + "/" + exName,data);
    }
}
