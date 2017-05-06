package com.unicorn.simpletracker.core;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by tu.tranhienminh on 4/30/2017.
 */
public class GoogleServiceManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    static GoogleApiClient mGoogleApiClient = null;
    static GoogleServiceManager m_instance = null;

    public static final int RESOLVE_CONNECTION_REQUEST_CODE = 0;
    public static final int REQUEST_CODE_OPENER = 1;

    public static GoogleServiceManager GetInstance()
    {
        if(m_instance == null)
            m_instance = new GoogleServiceManager();
        return m_instance;
    }
    private Context mContext;
    private Activity mActivity;

    public boolean IsInitial()
    {
        if(mGoogleApiClient == null)
            return false;
        return true;
    }
    public void Initial(Context ct)
    {
        mContext = ct;
        mGoogleApiClient = new GoogleApiClient.Builder(ct)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void ConnnectAndDownload(Activity act) {
        mActivity = act;
        if(!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting())
        {
            mGoogleApiClient.connect();
        }
        else if(mGoogleApiClient.isConnected())
        {
            Download();
        }
    }

    public void Download()
    {
        IntentSender intentSender = Drive.DriveApi
                .newOpenFileActivityBuilder()
                .setMimeType(new String[]{"text/plain", "text/html", "text/csv"})
                .build(mGoogleApiClient);
        try {
            mActivity.startIntentSenderForResult(
                    intentSender, REQUEST_CODE_OPENER, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(mActivity, "connect to drive successful", Toast.LENGTH_SHORT).show();
        Download();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private String mPath = "root";
    public void SaveFile(DriveId driveId, String path) {
        mPath = path;
        DriveFile.DownloadProgressListener listener = new DriveFile.DownloadProgressListener() {
            @Override
            public void onProgress(long bytesDownloaded, long bytesExpected) {
                // Update progress dialog with the latest progress.
                int progress = (int) (bytesDownloaded * 100 / bytesExpected);
            }
        };
        DriveFile driveFile = driveId.asDriveFile();
        driveFile.open(mGoogleApiClient, DriveFile.MODE_READ_ONLY, listener)
                .setResultCallback(driveContentsCallback);
        driveId = null;
    }

    private final ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback =
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(@NonNull DriveApi.DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        return;
                    }

                    // Read from the input stream an print to LOGCAT
                    DriveContents driveContents = result.getDriveContents();
                    boolean ret =  Utils.SaveDriveFile(mPath, driveContents.getInputStream());
                    Toast.makeText(mActivity, ret?"download successful":"download fail", Toast.LENGTH_SHORT).show();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(driveContents.getInputStream()));
//                    StringBuilder builder = new StringBuilder();
//                    String line;
//                    try {
//                        while ((line = reader.readLine()) != null) {
//                            builder.append(line);
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    String contentsAsString = builder.toString();

                    // Close file contents
                    driveContents.discard(mGoogleApiClient);
                }
            };
}
