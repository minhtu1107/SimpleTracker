package com.unicorn.simpletracker.core;

import java.util.ArrayList;

/**
 * Created by tu.tranhienminh on 3/11/2017.
 */
public class EventManager {

    private static EventManager m_instance;
    private ArrayList<String> m_EventList;

    public EventManager()
    {
        m_EventList = new ArrayList<String>();
    }

    public static EventManager GetInstance()
    {
        if(m_instance == null)
            m_instance = new EventManager();
        return m_instance;
    }

    public ArrayList<String> GetEventList()
    {
        return m_EventList;
    }

    public void AddEvent(String event)
    {
        m_EventList.add(event);
    }
}
