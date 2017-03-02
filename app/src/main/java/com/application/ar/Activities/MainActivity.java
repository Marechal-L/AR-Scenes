package com.application.ar.Activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.application.ar.myapplication.R;
import com.application.ar.loadObj.Instances;

import org.artoolkit.ar.base.assets.AssetHelper;

/**
 * Created by ludovic on 19/02/17.
 */

public class MainActivity extends Activity implements View.OnClickListener
{
    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage("Chargement des modèles 3D");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                return mProgressDialog;
            default:
                return null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button view = (Button)this.findViewById(R.id.menu_viewing);

        AssetHelper assetHelper = new AssetHelper(getAssets());
        assetHelper.cacheAssetFolder(this.getApplication(), "Data");

        LoadModelsTask loadTask = new LoadModelsTask();
        loadTask.execute(this.getApplicationContext());

        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch(id)
        {
            case R.id.menu_viewing :
                Intent i = new Intent(this, ViewingActivity.class);
                startActivity(i);
                break;
        }
    }

    private class LoadModelsTask extends AsyncTask<Context,Integer,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @TargetApi(Build.VERSION_CODES.CUPCAKE)
        protected String doInBackground(Context... params)
        {
            Instances.context = getApplicationContext();

            int progress;
            while((progress = Instances.loadNext()) != -1)
            {
                publishProgress(progress);
            }

            return "";
        }

        protected void onProgressUpdate(Integer... progress)
        {
            super.onProgressUpdate(progress);
            mProgressDialog.setProgress(progress[0]);
        }

        protected void onPostExecute(String result) {
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
        }
    }
}
