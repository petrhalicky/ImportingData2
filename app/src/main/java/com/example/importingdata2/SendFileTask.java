package com.example.importingdata2;

import android.os.AsyncTask;
import android.util.Log;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class SendFileTask extends AsyncTask {

    private static final int BUFFER = 80000;

    private String username = "";
    private String password = "";
    private int port = 22;
    private String hostname = "";
    private String path = "";
    private String id = "";

    public SendFileTask(String path, String id){
        this.path = path;
        this.id = id;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        JSch jsch = new JSch();

        try {
            Session session = jsch.getSession(username, hostname, port);
            session.setPassword(password);

            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();

            ChannelSftp sftp = (ChannelSftp) channel;

            File finalFile = new File(path);

            /*
            do{
                if(finalFile.isDirectory()){
                    File[] list = finalFile.listFiles();
                    finalFile = list[0];
                }
            } while(finalFile.isDirectory());
             */

            /*for(File file : finalFile.listFiles()){
                Log.i("JSCH", "File sending " + file.getAbsolutePath());

                sftp.put(new FileInputStream(file), file.getName());
            }*/

            String zipFile = new SimpleDateFormat("yyMMdd").format(Calendar.getInstance().getTime()) + id + ".zip";
            /*String[] files = new String[finalFile.listFiles().length];
            for(File file : finalFile.listFiles()){
                Log.i("JSCH", "File adding " + file.getAbsolutePath());
                files[files.length] = file.getAbsolutePath();
            }
            Log.i("JSCH", "Finishing list of files to zip");*/

            File zipFinish = new File(path + "/" + zipFile);
            //zip(files, zipFinish.getAbsolutePath());
            zip(finalFile.listFiles(), zipFinish.getAbsolutePath());

            Log.i("JSCH", "Files zipped to: " + zipFinish.getAbsolutePath());

            sftp.put(new FileInputStream(zipFinish), zipFinish.getName());

            Log.i("JSCH", "File sending " + zipFinish.getAbsolutePath());

            //Log.i("JSCH", "File sending " + finalFile.getAbsolutePath());

            //sftp.put(new FileInputStream(finalFile), finalFile.getName());
            sftp.exit();
            sftp.disconnect();
            session.disconnect();

            zipFinish.delete();

        } catch (JSchException | SftpException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* usage
    // declare an array for storing the files i.e the path
    // of your source files
    String[] s = new String[2];

    // Type the path of the files in here
    s[0] = inputPath + "/image.jpg";
    s[1] = inputPath + "/textfile.txt"; // /sdcard/ZipDemo/textfile.txt

    // first parameter is d files second parameter is zip file name
    ZipManager zipManager = new ZipManager();

    // calling the zip function
    zipManager.zip(s, inputPath + inputFile);
    */
    private void zip(File[] _files, String zipFileName){
        try {
            BufferedInputStream origin = null;
            ZipOutputStream out = new ZipOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(
                                    zipFileName)));

            byte data[] = new byte[BUFFER];

            for(File file : _files){
                Log.v("Compress", "Adding file: " + file);

                origin = new BufferedInputStream(
                        new FileInputStream(
                                file), BUFFER);

                ZipEntry entry = new ZipEntry(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;

                while((count = origin.read(data, 0, BUFFER)) != -1){
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void zip(String[] _files, String zipFileName){
        try {
            BufferedInputStream origin = null;
            ZipOutputStream out = new ZipOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(
                                    zipFileName)));

            byte data[] = new byte[BUFFER];

            for(String file : _files){
                Log.v("Compress", "Adding file: " + file);

                origin = new BufferedInputStream(
                        new FileInputStream(
                                new File(file)), BUFFER);

                ZipEntry entry = new ZipEntry(file.substring(file.lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;

                while((count = origin.read(data, 0, BUFFER)) != -1){
                    out.write(data, 0, count);
                }
                origin.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /* usage
    ZipManager zipManager = new ZipManager();
    zipManager.unzip(inputPath + inputFile, outputPath);
     */
    private void unzip(String _zipFile, String _targetLocation){
        dirChecker(_targetLocation);

        try {
            ZipInputStream zin = new ZipInputStream(
                    new FileInputStream(
                            new File(_zipFile)));

            ZipEntry entry = null;
            byte data[] = new byte[BUFFER];

            while((entry = zin.getNextEntry()) != null){
                if(entry.isDirectory()) dirChecker(entry.getName());
                else {
                    FileOutputStream fos = new FileOutputStream(
                            new File(_targetLocation + entry.getName()));

                    //for(int c = zin.read(); c != -1; c = zin.read())
                        //fos.write(c);
                    int count;
                    while((count = zin.read(data, 0, BUFFER)) != -1)
                        fos.write(data, 0, BUFFER);

                    zin.closeEntry();
                    fos.close();
                }
            }
            zin.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void dirChecker(String dir){
        File f = new File(dir);
        if(!f.isDirectory()) f.mkdirs();
    }
}
