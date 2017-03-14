package com.unicorn.simpletracker.core;

import java.util.ArrayList;

/**
 * Created by tu.tranhienminh on 3/12/2017.
 */
public class Attender {

    public void setAttend(boolean m_isAttend) {
        this.m_isAttend = m_isAttend;
    }

    public boolean isAttend() {
        return m_isAttend;
    }

    private boolean m_isAttend;

    public static final int ATTENDER_ID = 0;
    public static final int ATTENDER_NAME = ATTENDER_ID + 1;
    public static final int ATTENDER_COURSE = ATTENDER_NAME + 1;

    private ArrayList<String> m_data;
    public Attender(ArrayList<String> data)
    {
        this.m_data = data;
        this.m_isAttend = false;
//        for(int i=0; i<m_data.size(); i++)
//            System.out.println("Attender " + i + m_data.get(i));
    }

    public Attender(ArrayList<String> data, boolean isFromSave)
    {
        this.m_data = data;
        this.m_isAttend = false;
//        for(int i=0; i<m_data.size(); i++)
//            System.out.println("Attender " + i + m_data.get(i));
        if(isFromSave)
        {
            String str = data.get(data.size()-1);
            if(str.contains("1"))
            {
                this.m_isAttend = true;
            }
            this.m_data.remove(data.size()-1);
        }
    }

    public String getName()
    {
        String ret = null;
        if(m_data.size()>ATTENDER_NAME)
            ret = m_data.get(ATTENDER_NAME);
        return ret;
    }

    public String getID()
    {
        String ret = null;
        if(m_data.size()>ATTENDER_ID)
            ret = m_data.get(ATTENDER_ID);
        return ret;
    }

    public String getCourse()
    {
        String ret = null;
        if(m_data.size()>ATTENDER_COURSE)
            ret = m_data.get(ATTENDER_COURSE);
        return ret;
    }

    public int getNumOfField()
    {
        return m_data.size();
    }
    public String getByField(int idx)
    {
        return m_data.get(idx);
    }
}
