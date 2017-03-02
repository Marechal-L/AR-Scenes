package com.application.ar.loadObj;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by ludovic on 19/02/17.
 */

public class LoadModelsTask extends AsyncTask<Context,Integer,String> {

    private ProgressDialog progressDialog;



    protected String doInBackground(Context... params)
    {
        publishProgress((int) ((1 / (float) 5) * 100));

        return "";
    }

    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);

    }

    protected void onPostExecute(String result) {

    }
}

