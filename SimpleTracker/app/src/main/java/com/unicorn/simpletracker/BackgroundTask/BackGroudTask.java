package com.unicorn.simpletracker.BackgroundTask;

import android.os.AsyncTask;

public class BackGroudTask extends AsyncTask<Integer, Void, Integer>
{
    BaseTask m_base = null;
    public BackGroudTask(BaseTask base)
    {
        m_base = base;
    }
    @Override
    protected Integer doInBackground(Integer... params) {
        return m_base.doInBackground(params[0]);
    }

    @Override
    protected void onPostExecute(Integer result)
    {
        m_base.onPostExecute(result);
    }
}
