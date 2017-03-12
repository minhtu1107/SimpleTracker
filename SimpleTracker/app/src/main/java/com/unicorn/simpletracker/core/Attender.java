package com.unicorn.simpletracker.core;

/**
 * Created by tu.tranhienminh on 3/12/2017.
 */
public class Attender {
    private String m_ID;

    public String getName() {
        return m_Name;
    }

    public String getCourse() {
        return m_Course;
    }

    public String getID() {
        return m_ID;
    }

    private String m_Name;
    private String m_Course;

    public Attender(String m_Name, String m_ID, String m_Course) {
        this.m_Name = m_Name;
        this.m_ID = m_ID;
        this.m_Course = m_Course;
    }

    public void setAttend(boolean m_isAttend) {
        this.m_isAttend = m_isAttend;
    }

    public boolean isAttend() {
        return m_isAttend;
    }

    private boolean m_isAttend;
}
