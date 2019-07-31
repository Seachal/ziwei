/**
 * Created by linhz on 2016/06/04.
 */
package com.laka.live.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.Date;

public class IOUtil {


    /**
     * @param aInput
     * @param aReadLength
     * @param aBufferLength
     * @return
     * @throws IOException
     */
    public static byte[] readBytes(InputStream aInput, int aReadLength,
                                   int aBufferLength) throws IOException {
        if (aInput == null || aReadLength <= 0)
            return null;

        byte[] sData = new byte[aReadLength];

        // aBufferLength = Math.max(aBufferLength,2048);
        if (aBufferLength <= 0) {
            aBufferLength = 2048;
        }
        int sLength = 0;
        for (int i = 0; i < aReadLength; ) {
            if (aReadLength - i < aBufferLength)
                sLength = aInput.read(sData, i, aReadLength - i);
            else
                sLength = aInput.read(sData, i, aBufferLength);

            if (sLength == -1)
                break;
            i += sLength;
        }

        return sData;
    }

    public static byte[] readByteArrayFromInputStream(InputStream aInputStream) {
        byte[] res = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            int len = 0;
            int size = 1024 * 4;
            byte[] buf = new byte[size];
            while ((len = aInputStream.read(buf, 0, size)) != -1)
                bos.write(buf, 0, len);
            res = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            safeClose(bos);
        }

        return res;
    }

    public static void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取文件的创建时间
     *
     * @param fullFileName
     * @return
     */
    private static Date getFileCreateTime(String fullFileName) {
        Path path = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            path = Paths.get(fullFileName);
            BasicFileAttributeView basicview = Files.getFileAttributeView(path,
                    BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
            BasicFileAttributes attr;
            try {
                attr = basicview.readAttributes();
                Date createDate = new Date(attr.creationTime().toMillis());
                return createDate;
            } catch (Exception e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.set(1970, 0, 1, 0, 0, 0);
            return cal.getTime();
        }
        return null;
    }

    //删除文件夹
    //param folderPath 文件夹完整绝对路径
    public static void delFolder(String folderPath) {
        try {
            delAllFiles(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除指定文件夹下的所有文件
     *
     * @param path
     */
    public static boolean delAllFiles(String path) {
        boolean result = false;
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            return result;
        }
        if (!dirFile.isDirectory()) {
            return result;
        }

        String[] tempFiles = dirFile.list();
        File temp = null;
        for (int i = 0; i < tempFiles.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempFiles[i]);
            } else {
                temp = new File(path + File.separator + tempFiles[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFiles(path + "/" + tempFiles[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempFiles[i]);//再删除空文件夹
                result = true;
            }
        }
        return result;
    }

}
