package com.example.importingdata2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;

import android.provider.Settings;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final String folder = "Custom";
    private static final int MY_PERMISSION_REQUSTE_STORAGE = 1;
    private static final int MY_PERMISSION_REQUSTE_INTERNET = 2;
    private static final int MY_PERMISSION_REQUSTE_PHONE = 3;
    private static final String TAG = "Chilkat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUSTE_STORAGE);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_REQUSTE_STORAGE);
            }
        }

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.INTERNET)){
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, MY_PERMISSION_REQUSTE_INTERNET);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, MY_PERMISSION_REQUSTE_INTERNET);
            }
        }

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.MODIFY_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.MODIFY_PHONE_STATE)){
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.MODIFY_PHONE_STATE}, MY_PERMISSION_REQUSTE_PHONE);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.MODIFY_PHONE_STATE}, MY_PERMISSION_REQUSTE_PHONE);
            }
        }
        */

        checkPermissionAndRunRequest(Manifest.permission.WRITE_EXTERNAL_STORAGE, MY_PERMISSION_REQUSTE_STORAGE);
        checkPermissionAndRunRequest(Manifest.permission.INTERNET, MY_PERMISSION_REQUSTE_INTERNET);
        checkPermissionAndRunRequest(Manifest.permission.MODIFY_PHONE_STATE, MY_PERMISSION_REQUSTE_PHONE);

        final ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar2);
        final TextView tv = (TextView) findViewById(R.id.textView3);
        final String toPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cz.data.app";

        CopyFilesTask cp = new CopyFilesTask(this, folder, toPath);
        cp.execute();

        SendFileTask sf = new SendFileTask(Environment.getExternalStorageDirectory().getAbsolutePath() + "/log.from.app", Settings.Secure.ANDROID_ID);
        sf.execute();
    }

    private void checkPermissionAndRunRequest(String permission, int request_code){
        if(ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)){
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, request_code);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, request_code);
            }
        }
    }

    public View getComponentByID(int id){
        return findViewById(id);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSION_REQUSTE_STORAGE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        //all right, do nothing
                    } else {
                        Toast.makeText(this, "No storage permission granted!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            case MY_PERMISSION_REQUSTE_INTERNET:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED){
                        //all right, do nothing
                    } else {
                        Toast.makeText(this, "No internet permission granted!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            case MY_PERMISSION_REQUSTE_PHONE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
                        //all right, do nothing
                    } else {
                        Toast.makeText(this, "No internet permission granted!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}

