package com.example.importingdata2;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CopyFilesTask extends AsyncTask {

    ProgressBar pb = null;
    TextView tv = null;
    TextView tv2 = null;
    String toPath = "";
    String sourceFolder = "";
    AssetManager am = null;
    MainActivity ma = null;

    public CopyFilesTask(MainActivity ma, String sourceFolder, String toPath){
        this.ma = ma;
        this.sourceFolder = sourceFolder;
        this.am = ma.getAssets();
        this.pb = (ProgressBar) ma.getComponentByID(R.id.progressBar2);
        this.tv = (TextView) ma.getComponentByID(R.id.textView3);
        this.tv2 = (TextView) ma.getComponentByID(R.id.textView2);
        this.toPath = toPath;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        Utils.copyAssetFolder(am, sourceFolder, toPath);
        return null;
    }

    @Override
    protected void onPreExecute() {
        pb.setVisibility(View.VISIBLE);
        tv.setVisibility(View.INVISIBLE);
        tv2.setVisibility(View.VISIBLE);
        tv2.setText("Copying files to '" + toPath + "'. This may take up to several minutes.");
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object o) {
        pb.setVisibility(View.GONE);
        tv.setText("Files succesfully copied to '" + toPath + "'");
        tv.setVisibility(View.VISIBLE);
        tv2.setVisibility(View.INVISIBLE);
        super.onPostExecute(o);
    }
}
