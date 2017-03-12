package com.unicorn.simpletracker.core;

import java.util.ArrayList;

/**
 * Created by tu.tranhienminh on 3/12/2017.
 */
public class EventData {
    public String getName() {
        return m_Name;
    }

    private String m_Name;
    private ArrayList<Attender> m_AttendList;

    public EventData(String name)
    {
        m_Name = name;
    }
}
