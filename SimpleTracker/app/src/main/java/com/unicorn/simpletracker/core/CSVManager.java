package com.unicorn.simpletracker.core;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tu.tranhienminh on 3/12/2017.
 */
public class CSVManager {
    private static CSVManager m_instance;

    public static CSVManager GetInstance()
    {
        if(m_instance == null)
            m_instance = new CSVManager();
        return m_instance;
    }
    public List<String[]> OpenFile(InputStream is)
    {
        InputStreamReader reader = new InputStreamReader(is, Charset.forName("UTF-8"));
        try {
            List<String[]> csv = new CSVReader(reader).readAll();
            reader.close();
            return  csv;
        } catch (IOException e) {
//            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> OpenFile(InputStream is, int idx)
    {
        ArrayList<String> arr = null;
        InputStreamReader reader = new InputStreamReader(is, Charset.forName("UTF-8"));
        try {
            List<String[]> csv = new CSVReader(reader).readAll();
            if(csv.size()>0 && csv.get(0).length-1<idx) {
//                System.out.println("invalid idx");
                return arr;
            }
            arr = new ArrayList<String>();
            for(int i=0; i<csv.size(); i++)
            {
                arr.add(csv.get(i)[idx]);
            }
            return  arr;
        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
        }
        return arr;
    }

    public void Export(String path, List<String[]> data)
    {
        File from_root = new File(path);
        if(from_root.exists())
            from_root.delete();
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(path));
            String[] strings;
            for(int i=0; i<data.size(); i++)
            {
                strings = data.get(i);
                writer.writeNext(strings);
            }
            writer.close();
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }
}
