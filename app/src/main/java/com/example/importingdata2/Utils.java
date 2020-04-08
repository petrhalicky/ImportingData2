package com.example.importingdata2;

import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Utils {

    public static boolean copyAssetFolder(AssetManager assetManager, String fromAssetPath, String toPath) {
        try {
            String[] files = assetManager.list(fromAssetPath);
            new File(toPath).mkdirs();
            boolean res = true;
            for (String file : files)
                if(!(new File(toPath + "/" + file).exists())){
                    if (file.contains("."))
                        res &= copyAsset(assetManager,
                                fromAssetPath + "/" + file,
                                toPath + "/" + file);
                    else
                        res &= copyAssetFolder(assetManager,
                                fromAssetPath + "/" + file,
                                toPath + "/" + file);
                }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int copyAssetFolder(AssetManager assetManager, String fromAssetPath, String toPath, int startFromIndex, int stopIndex) {
        try {
            String[] files = assetManager.list(fromAssetPath);
            new File(toPath).mkdirs();
            int res = Math.min(stopIndex, files.length);
            String file = "";
            for(int i = startFromIndex; i < (Math.min(stopIndex, files.length)); i++)
                file = files[i];
                if(!(new File(toPath + "/" + file).exists())){
                    if (file.contains("."))
                        copyAsset(assetManager,
                                fromAssetPath + "/" + file,
                                toPath + "/" + file);
                    else
                        copyAssetFolder(assetManager,
                                fromAssetPath + "/" + file,
                                toPath + "/" + file);
                }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static boolean copyAsset(AssetManager assetManager,
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

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
}
