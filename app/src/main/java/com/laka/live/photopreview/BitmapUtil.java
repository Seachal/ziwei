package com.laka.live.photopreview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.laka.live.application.LiveApplication;
import com.laka.live.util.HardwareUtil;
import com.laka.live.util.IOUtil;
import com.laka.live.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zwl on 16/8/3.
 *
 */

public class BitmapUtil {
    public static final int LIMIT_MAX_WIDTH = 720;
    public static final int LIMIT_MAX_HEIGHT = 1080;

    public static Bitmap createBitmapThumbnail(String filePath) {
        int width = Math.min(720, HardwareUtil.getDeviceWidth());
        int height = Math.min(1080, HardwareUtil.getDeviceHeight());
        return createBitmapThumbnail(filePath, width, height);
    }

    public static Bitmap createBitmapThumbnail(String filePath, int width, int height) {
        return createBitmapThumbnail(filePath, width, height, false);
    }

    public static Bitmap createBitmapThumbnail(String filePath, int width, int height, boolean scale) {
        return createBitmapThumbnail(filePath, width, height, false, scale);
    }

    public static Bitmap createBitmapThumbnail(String filePath, int width, int height,
                                               boolean limitSize, boolean scale) {
        if (filePath == null) {
            return null;
        }
        Bitmap bitmap = null;
        InputStream is = null;
        boolean isAsset = isAssetsFile(filePath);
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            if (!isAsset) {
                is = new FileInputStream(filePath);
            } else {
                is = getInputStream(LiveApplication.getInstance().getApplicationContext(), filePath);
            }

            BitmapFactory.decodeStream(is, null, options);

            IOUtil.safeClose(is);
            if (!isAsset) {
                is = new FileInputStream(filePath);
            } else {
                is = getInputStream(LiveApplication.getInstance().getApplicationContext(), filePath);
            }
            if (limitSize) {
                int[] size = correctLimitSize(width, height);
                width = size[0];
                height = size[1];
            }
            options.inSampleSize = computeSampleSize(options, -1, width * height);
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(is, null, options);

            if (bitmap != null && scale) {
                Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
                //chuan
                if (bitmap != newBitmap){
                    bitmap.recycle();
                    bitmap = newBitmap;
                }
            }

        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            IOUtil.safeClose(is);
        }
        return bitmap;
    }

    public static int[] correctLimitSize(int desWidth, int desHeight) {
        int[] result = new int[2];
        result[0] = desWidth;
        result[1] = desHeight;
        float radio = 1.0f * desHeight / desWidth;
        if (radio >= 1) {
            if (desWidth > LIMIT_MAX_WIDTH) {
                result[0] = LIMIT_MAX_WIDTH;
                result[1] = (int) (radio * result[0]);
            }
        } else {
            if (desHeight > LIMIT_MAX_WIDTH) {
                result[1] = LIMIT_MAX_WIDTH;
                result[0] = (int) (result[1] / radio);
            }
        }
        return result;
    }

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    public static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength),
                Math.floor(h / minSideLength)
        );
        if (upperBound < lowerBound) {
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }

    }

    public static InputStream getInputStream(Context context, String name) {
        try {
            return context.getAssets().open(name);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isAssetsFile(String path) {
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        if (path.startsWith("file://") || path.startsWith("content://")) {
            return false;
        }
        if (path.startsWith("/sdcard/")) {
            return false;
        } else {
            String sdPath = getSdcardPath();
            String sdSecondPath = getSdSecondPath();
            if (path.startsWith(sdPath) || path.startsWith(sdSecondPath)) {
                return false;
            }
            if (path.startsWith(LiveApplication.getInstance().getApplicationInfo().dataDir)) {
                return false;
            }
        }

        return true;
    }

    public static String getSdcardPath() {
        String sdcardPath = "";
        File sdcardDir = Environment.getExternalStorageDirectory();
        if (sdcardDir != null) {
            sdcardPath = sdcardDir.getAbsolutePath();
            if (sdcardPath != null && !sdcardPath.endsWith(File.separator)) {
                sdcardPath += File.separator;
            }
        }
        return sdcardPath;
    }

    public static String getSdSecondPath() {
        String sdcardPath = "";
        String sdFirstPath = "";
        String sdSecondPath = "";
        File sdcardDir = Environment.getExternalStorageDirectory();
        if (sdcardDir != null) {
            sdcardPath = sdcardDir.getAbsolutePath();
            if (sdcardPath != null && !sdcardPath.endsWith(File.separator)) {
                sdcardPath += File.separator;
                if (sdcardPath.trim().length() < 2) {
                    return "";
                }
                int index = sdcardPath.substring(1).indexOf(File.separator);
                if (index > -1 && index < sdcardPath.length()) {
                    sdFirstPath = sdcardPath.substring(0, index + 2);
                    sdSecondPath = sdcardPath.substring(index + 1);
                }

            }
        }
        return sdSecondPath;
    }
}
