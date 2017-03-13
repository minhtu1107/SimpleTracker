package com.unicorn.simpletracker.core;

import android.content.Context;
import android.os.Environment;

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

    public static ArrayList<Attender> LoadAttendList(String event_name, Context con)
    {
        List<String[]> csv = null;
        InputStream is = null;
        try
        {
            File from_event = new File(EVENT_PATH + "/" + event_name + "/data.csv");
            File from_root = new File(ROOT_PATH + "/data.csv");
            //check in event
            if(from_event.exists())
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
            Attender att = new Attender(Arrays.asList(csv.get(i)));
            attList.add(att);
        }
        return attList;
    }

    public static void ExportCSV(ArrayList<Attender> attenders)
    {
        List<String[]> data = new ArrayList<String[]>();
        if(attenders.size()==0)
            return;
        int NumOfField = attenders.get(0).getNumOfField();
        for(int i=0; i<attenders.size(); i++)
        {
            String[] strings = new String[NumOfField];
            for(int j=0; j<NumOfField; j++)
            {
                strings[j] = attenders.get(i).getByField(j);
            }
            data.add(strings);
        }
        CSVManager.GetInstance().Export(ROOT_PATH + "/export.csv",data);
    }
}
