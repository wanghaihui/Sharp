package com.conquer.sharp.util.common;

import android.content.Context;
import android.os.Environment;

import com.conquer.sharp.api.SharpUIKit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;

public class FileUtils {
    private static final String ROOT_DIRECTORY = "Sharp";

    public static String getRootPath() {
        if (validSDCard()) {
            return Environment.getExternalStorageDirectory().getPath() + File.separator + ROOT_DIRECTORY;
        } else {
            return SharpUIKit.getContext().getCacheDir().getPath() + File.separator + ROOT_DIRECTORY;
        }
    }

    private static boolean validSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 保存对象到文件--内部存储
     */
    public static Object saveObjectToFile(Context context, Object object, String fileName) {
        // save object to file
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(object);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 判断文件是否存在
     */
    private boolean isFileExist(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private boolean isFileExist(String parent, String fileName) {
        try {
            File file = new File(parent, fileName);
            if (file.exists()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static void copyAssetsToSDCard(Context context, String srcPath, String dstPath) {
        try {
            String[] fileList = context.getAssets().list(srcPath);
            if (fileList.length > 0) {
                File file = new File(dstPath);
                if (!file.exists()) {
                    if(file.mkdir()) {
                        traversalFiles(context, srcPath, dstPath, fileList);
                    }
                } else {
                    traversalFiles(context, srcPath, dstPath, fileList);
                }
            } else {
                File outFile = new File(dstPath);
                InputStream is = context.getAssets().open(srcPath);
                FileOutputStream fos = new FileOutputStream(outFile);
                byte[] buffer = new byte[1024];
                int byteCount;
                while ((byteCount = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, byteCount);
                }
                fos.flush();
                is.close();
                fos.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void traversalFiles(Context context, String srcPath, String dstPath, String[] fileList) {
        for (String fileName : fileList) {
            if (srcPath.equals("")) {
                // assets directory
                copyAssetsToSDCard(context, fileName, dstPath + File.separator + fileName);
            } else {
                copyAssetsToSDCard(context, srcPath + File.separator + fileName,
                        dstPath + File.separator + fileName);
            }
        }
    }
}
