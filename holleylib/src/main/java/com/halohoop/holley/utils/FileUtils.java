package com.halohoop.holley.utils;

import android.text.TextUtils;

import java.io.File;

/**
 * Created by Pooholah on 2017/5/14.
 */

public class FileUtils {
    /**
     * 还是得到完整路径
     * @param filePath
     * @return
     */
    public static String getFolderName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }

    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);//还是得到完整路径
        if (TextUtils.isEmpty(folderName)) {
            return false;
        }

        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory())
                ? true
                : folder.mkdirs();
    }
    public static boolean makeDirs(File file) {
        return makeDirs(file.getAbsolutePath());
    }
}
