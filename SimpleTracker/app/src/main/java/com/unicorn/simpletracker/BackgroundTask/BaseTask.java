package com.unicorn.simpletracker.BackgroundTask;

import android.os.AsyncTask;

/**
 * Created by tu.tranhienminh on 3/17/2017.
 */
public interface BaseTask {

    public int doInBackground(int param);
    public void onPostExecute(int result);
}

