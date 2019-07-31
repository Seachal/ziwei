package com.laka.live.photopreview;

import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by zwl on 16/8/3.
 */

public class FileUtil {
    public static String getFileNameFromPath(String filePath) {
        return getFileNameFromPath(filePath, false);
    }

    public static String getFileNameFromPath(String filePath, boolean includeExtension) {
        if (filePath == null) {
            return null;
        }
        int splitIndex = filePath.lastIndexOf(File.separator);
        if (splitIndex >= 0 && splitIndex < filePath.length()) {
            String fileName = filePath.substring(splitIndex + 1);
            if (includeExtension) {
                return fileName;
            } else {
                int dotIndex = fileName.lastIndexOf('.');
                if (dotIndex <= 0 || dotIndex == fileName.length() - 1) {
                    return null;
                }
                return fileName.substring(0, dotIndex);
            }
        }
        return filePath;
    }

    public static void copy(File source, File target) throws FileNotFoundException,IOException {
        copy(source, target, new byte[64 * 1024]);
    }

    public static void copy(File source, File target, byte[] buffer) throws FileNotFoundException, IOException {
        InputStream in = new FileInputStream(source);
        File parent = target.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        if (target.isDirectory()) {
            target = new File(target, source.getName());
        }
        OutputStream out = new FileOutputStream(target);
        int read;
        try {
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            in.close();
            out.close();
        }
    }
}
