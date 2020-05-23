package com.example.importingdata2;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CopyFilesTask extends AsyncTask {

    private ProgressBar pb = null;
    private TextView tv = null;
    private TextView tv2 = null;
    private String toPath = "";
    private String sourceFolder = "";
    private AssetManager am = null;
    private MainActivity ma = null;

    private int actualCount = -1;
    private int total = -1;

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
        copyAssetFolder(am, sourceFolder, toPath);
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

    @Override
    protected void onProgressUpdate(Object[] values) {
        tv2.setText("Copying folder " + actualCount + " of " + total + " to '" + toPath + "'. This may take up to several minutes.");
    }

    private boolean copyAssetFolder(AssetManager assetManager, String fromAssetPath, String toPath) {
        try {
            String[] files = assetManager.list(fromAssetPath);

            if (total < 0) total = files.length;

            new File(toPath).mkdirs();
            boolean res = true;
            for (String file : files){
                String targetFile = file;
                if(file.equalsIgnoreCase("bundle.bundle"))
                    targetFile = ".bundle";
                if(!(new File(toPath + "/" + targetFile).exists())){
                    if (file.contains("."))
                        res &= copyAsset(assetManager,
                                fromAssetPath + "/" + file,
                                toPath + "/" + targetFile);
                    else {
                        res &= copyAssetFolder(assetManager,
                                fromAssetPath + "/" + file,
                                toPath + "/" + targetFile);
                        actualCount++;
                        publishProgress();
                    }
                }
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private int copyAssetFolder(AssetManager assetManager, String fromAssetPath, String toPath, int startFromIndex, int stopIndex) {
        try {
            String[] files = assetManager.list(fromAssetPath);
            new File(toPath).mkdirs();
            int res = Math.min(stopIndex, files.length);
            String file = "";
            for (int i = startFromIndex; i < (Math.min(stopIndex, files.length)); i++){
                file = files[i];
                if (!(new File(toPath + "/" + file).exists())) {
                    if (file.contains("."))
                        copyAsset(assetManager,
                                fromAssetPath + "/" + file,
                                toPath + "/" + file);
                    else
                        copyAssetFolder(assetManager,
                                fromAssetPath + "/" + file,
                                toPath + "/" + file);
                }
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private boolean copyAsset(AssetManager assetManager,
                                     String fromAssetPath, String toPath) {
        boolean returnValue = false;
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(fromAssetPath);
            new File(toPath).createNewFile();
            out = new FileOutputStream(toPath);
            copyFile(in, out);

            in.close();
            in = null;

            out.flush();
            out.close();
            out = null;

            returnValue = true;
        } catch(Exception e) {
            e.printStackTrace();
            returnValue = false;
        } finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

            if(out != null){
                try {
                    out.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

            return returnValue;
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
}
