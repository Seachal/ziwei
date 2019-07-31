package com.laka.live.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.laka.live.application.LiveApplication;
import com.laka.live.ui.widget.danmu.IBitmapListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.orhanobut.logger.Logger;
import com.qiyukf.unicorn.api.ImageLoaderListener;
import com.qiyukf.unicorn.api.UnicornImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by luwies on 16/3/4.
 */
public class ImageUtil implements UnicornImageLoader {
    private static final String TAG = "ImageUtilRoom";

    public static final int FADE_IN_DURATIONMILLIS = 250;

    public static final String LOCAL_IMAGE_URI_PREFIX = "file://";

    public static final String RES_URI_PREFIX = "res:///";

    public static final String ASSET_URI_PREFIX = "asset:///";

    public static Uri MI_CROP_PHOTO_PATH;

    //设置加载网络图片的相关参数
    public static DisplayImageOptions options = new DisplayImageOptions.Builder()
            .bitmapConfig(Bitmap.Config.RGB_565)
            .displayer(new SimpleBitmapDisplayer())
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();

    /**
     * 七牛服务器域名
     */
    public static final String QINIU_CDN_HOST_NAME = "7xrkjc.com2.z0.glb.qiniucdn.com";

    public static LruCache<Integer, Drawable> drawableCache;

    // text to entry
    static {
        drawableCache = new LruCache<Integer, Drawable>(100) {
            @Override
            protected void entryRemoved(boolean evicted, Integer key, Drawable oldValue, Drawable newValue) {
                if (oldValue != newValue) {
                }
//                    oldValue.recycle();
            }
        };
    }


    /**
     * 展示图片
     *
     * @param uri       图片的地址
     * @param imageView 图片控件
     * @param resId     自定义的图片加载参数
     */
    public static void displayImage(ImageView imageView, String uri,
                                    int resId) {
        ImageLoader.getInstance().displayImage(uri, imageView, getOptions(resId));
    }

    public static void displayImage(ImageView imageView, String uri) {
        ImageLoader.getInstance().displayImage(uri, imageView);
    }

    public static void displayCacheImage(ImageView imageView, String uri) {
        ImageLoader.getInstance().displayImage(uri, imageView, options);
    }

    public static void loadImage(String uri, ImageLoadingListener listener) {
        ImageSize imageSize = new ImageSize(100, 100);
        ImageLoader.getInstance().loadImage(uri, imageSize, listener);
    }

    /**
     * @param resId 默认加载图
     * @return 返回默认加载图及其它相关设置
     */
    public static DisplayImageOptions getOptions(int resId) {

        return new DisplayImageOptions.Builder()
                .showImageOnFail(resId)
                .showImageOnLoading(resId)
                .showImageForEmptyUri(resId)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

//    public static final Drawable getDrawable(Context context, int key) {
//        Drawable cache = drawableCache.get(key);
//        if (cache == null) {
//            cache = ContextCompat.getDrawable(context, key);
//            drawableCache.put(key, cache);
//        }
//        return cache;
//    }

    //预加载表情图片
//    public static void initGiftDrawables(final Context context) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                int[] giftRes = GiftGridView.giftIconRes;
//                for (int i = 0; i < giftRes.length; i++) {
//                    int key = giftRes[i];
//                    getDrawable(context, key);
//                }
//            }
//        }).start();
//
//    }

    @Override
    public Bitmap loadImageSync(String uri, int width, int height) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        // check cache
        boolean cached = true;
        ImageDownloader.Scheme scheme = ImageDownloader.Scheme.ofUri(uri);
        if (scheme == ImageDownloader.Scheme.HTTP
                || scheme == ImageDownloader.Scheme.HTTPS
                || scheme == ImageDownloader.Scheme.UNKNOWN) {
            // non local resource
            cached = MemoryCacheUtils.findCachedBitmapsForImageUri(uri, ImageLoader.getInstance().getMemoryCache()).size() > 0
                    || DiskCacheUtils.findInCache(uri, ImageLoader.getInstance().getDiskCache()) != null;
        }

        if (cached) {
            ImageSize imageSize = (width > 0 && height > 0) ? new ImageSize(width, height) : null;
            Bitmap bitmap = ImageLoader.getInstance().loadImageSync(uri, imageSize, options);
            if (bitmap == null) {
                Log.e(TAG, "load cached image failed, uri =" + uri);
            }
            return bitmap;
        }

        return null;
    }

    @Override
    public void loadImage(String uri, int width, int height, final ImageLoaderListener listener) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageSize imageSize = (width > 0 && height > 0) ? new ImageSize(width, height) : null;

        ImageLoader.getInstance().loadImage(uri, imageSize, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                if (listener != null) {
                    listener.onLoadComplete(loadedImage);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                super.onLoadingFailed(imageUri, view, failReason);
                if (listener != null) {
                    listener.onLoadFailed(failReason.getCause());
                }
            }
        });
    }


    /**
     * 清除指定url的图片缓存
     *
     * @param imageUri
     */
    public static void clearCache(String imageUri) {
        if (TextUtils.isEmpty(imageUri) == false) {
            Fresco.getImagePipeline().evictFromCache(Uri.parse(imageUri));
        }
    }

    public static void loadImage(SimpleDraweeView imageView, String url) {
        loadImage(imageView, url, null);
    }

    public static void loadImage(String url, ImageView imageView, ImageLoadingListener listener) {
        ImageLoader.getInstance().displayImage(url, imageView, listener);
    }

    public static void loadImage(SimpleDraweeView imageView, String url, ControllerListener listener) {
        loadImage(ImageType.TYPE_NORMAL, imageView, url, listener);
    }

    public static void loadImage(ImageType type, SimpleDraweeView imageView, String url) {
        loadImage(type, imageView, url, null);
    }

    public static void loadResImage(int resId, SimpleDraweeView imageView) {
        loadResImage(resId, imageView, null);
    }

    public static void loadResImage(int resId, SimpleDraweeView imageView, ControllerListener listener) {
        loadImage(ImageType.TYPE_NORMAL, imageView, RES_URI_PREFIX + resId, listener);
    }

    public static void loadAssetsImage(String path, SimpleDraweeView imageView) {
        loadAssetsImage(path, imageView, null);
    }

    public static void loadAssetsImage(String path, SimpleDraweeView imageView, ControllerListener listener) {
        loadImage(ImageType.TYPE_NORMAL, imageView, ASSET_URI_PREFIX + path, listener);
    }

    public static void loadLocalImage(String uri, SimpleDraweeView imageView) {
        loadLocalImage(uri, imageView, null);
    }

    public static void loadLocalImage(String uri, SimpleDraweeView imageView, ControllerListener listener) {
        loadImage(ImageType.TYPE_NORMAL, imageView, LOCAL_IMAGE_URI_PREFIX + uri, listener);
    }

    public static void loadImage(ImageType type, SimpleDraweeView imageView, String url, ControllerListener listener) {

        //type 用于扩展
        loadImage(imageView, url, listener, false, null);
    }


    public static void loadImage(final SimpleDraweeView imageView, String url,
                                 ControllerListener listener, boolean tapToRetryEnabled, BasePostprocessor processor) {

        ImageViewAware imageViewAware = new ImageViewAware(imageView, false);
        int width = imageViewAware.getWidth();
        int height = imageViewAware.getHeight();
//        Log.d(TAG,"loadImage width="+width+" height="+height);
        if (width <= 0) {
            width = UIUtil.getScreenWidth(imageView.getContext()) / 2;
        }
        if (height <= 0) {
            height = UIUtil.getScreenHeight(imageView.getContext()) / 2;
        }

        if (TextUtils.isEmpty(url)) {
            url = "";
        }
        Uri uri = Uri.parse(url);
        String scheme = uri.getScheme();
        if (TextUtils.isEmpty(scheme) == false && (scheme.equalsIgnoreCase("http") ||
                scheme.equalsIgnoreCase("https"))) {
//            Log.debug(TAG, "load image host " + uri.getHost());
            if (TextUtils.equals(QINIU_CDN_HOST_NAME, uri.getHost())) {
                uri = getQiniuUri(url, width, height);
            }
        }

//        Log.debug(TAG, "load image url = " + uri.toString());

        ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(uri)
                .setAutoRotateEnabled(true)
                .setPostprocessor(processor)
                .setLocalThumbnailPreviewsEnabled(true)
                .setResizeOptions(new ResizeOptions(width, height));


        GenericDraweeHierarchy hierarchy = imageView.getHierarchy();
        hierarchy.setFadeDuration(FADE_IN_DURATIONMILLIS);
        AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(imageView.getController())
                .setControllerListener(listener)
                .setImageRequest(imageRequestBuilder.build())
                .setAutoPlayAnimations(true)
                .setTapToRetryEnabled(tapToRetryEnabled)
                .build();
        imageView.setController(controller);
    }

    public static void getBitmapByUrl(String url, SimpleDraweeView imageView, final IBitmapListener listener) {
        ImageViewAware imageViewAware = new ImageViewAware(imageView, false);
        int width = imageViewAware.getWidth();
        int height = imageViewAware.getHeight();
        if (width <= 0) {
            width = UIUtil.getScreenWidth(imageView.getContext()) / 2;
        }
        if (height <= 0) {
            height = UIUtil.getScreenHeight(imageView.getContext()) / 2;
        }
        getBitmapByUrl(url, width, height, listener);
    }

    public static void getBitmapByUrl(String url, final int width, final int height, final IBitmapListener listener) {
        if (Utils.isEmpty(url)) {
            return;
        }
        Uri uri = Uri.parse(url);
        ImageRequest imageRequest;
        if (width != 0 && height != 0) {
            uri = getQiniuUri(url, width, height);
            imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(uri)
                    .setProgressiveRenderingEnabled(true)
                    .setResizeOptions(new ResizeOptions(width, height))
                    .build();
        } else {
            imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(uri)
                    .setProgressiveRenderingEnabled(true)
                    .build();
        }


        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(imageRequest, LiveApplication.getInstance());

        dataSource.subscribe(new BaseDataSubscriber<CloseableReference<CloseableImage>>() {
            @Override
            public void onNewResultImpl(
                    DataSource<CloseableReference<CloseableImage>> dataSource) {

                if (!dataSource.isFinished()) {
                    Log.d(TAG, "Not yet finished - this is just another progressive scan.");
                }

                CloseableReference<CloseableImage> imageReference = dataSource.getResult();
                if (imageReference != null) {
                    try {
//                CloseableImage image = imageReference.get();
                        CloseableBitmap image = (CloseableBitmap) imageReference.get();
                        Bitmap loadedImage = image.getUnderlyingBitmap();
                        if (loadedImage != null) {
                            listener.onSuccess(loadedImage);
                            Log.d(TAG, "getBitmapByUrl onSuccess");
                        } else {
                            listener.onFail(0);
                            Log.d(TAG, "getBitmapByUrl onFail");
                        }
                        // do something with the image
                    } finally {
                        imageReference.close();
                    }
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                Throwable throwable = dataSource.getFailureCause();
                Log.d(TAG, "getBitmapByUrl onFailureImpl");
                listener.onFail(0);
                // handle failure
            }
        }, CallerThreadExecutor.getInstance());
//        dataSource.subscribe(new BaseBitmapDataSubscriber() {
//                                 @Override
//                                 public void onNewResultImpl(@Nullable Bitmap bitmap) {
//                                     // You can use the bitmap in only limited ways
//                                     // No need to do any cleanup.
////                                     Log.d(TAG, "getBitmapByUrl onNewResultImpl width=" + bitmap.getWidth() + " height=" + bitmap.getHeight()
////                                             + " 指定width=" + width + " height=" + height);
//
//                                     //chuan
//                                     listener.onSuccess(bitmap.copy(Bitmap.Config.ARGB_8888 , true));
//                                 }
//
//                                 @Override
//                                 public void onFailureImpl(DataSource dataSource) {
//                                     // No cleanup required here.
////                                     Log.d(TAG, "getBitmapByUrl onFailureImpl");
//                                     listener.onFail(0);
//                                 }
//                             },
//                CallerThreadExecutor.getInstance());
    }

    private static Uri getQiniuUri(String url, int width, int height) {
        StringBuilder sb = new StringBuilder(url);
        sb.append("?imageView2/0/w/");
        sb.append(width);
        sb.append("/h/");
        sb.append(height);
        return Uri.parse(sb.toString());
    }

    public static byte[] getBitmapByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
        }


        return out.toByteArray();
    }

    public static byte[] compressUploadBitmap(String file) {
        if (TextUtils.isEmpty(file)) {
            return null;
        }
        return compressUploadBitmap(new File(file));
    }


    public static byte[] compressUploadBitmap(File file) {

        String filePath = file.getPath();
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        if (bmp == null) {
            return null;
        }

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        if (actualHeight <= 0 || actualWidth <= 0) {
            return null;
        }

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 1920.0f;
        float maxWidth = 1080.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            Log.error(TAG, "OutOfMemoryError : ", exception);
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            Log.error(TAG, "OutOfMemoryError : ", exception);
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.debug("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.debug("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.debug("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.debug("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            Log.error(TAG, "IOException : ", e);
        }


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] b = baos.toByteArray();


        if (bmp.isRecycled() == false) {
            bmp.recycle();
        }

        if (scaledBitmap.isRecycled() == false) {
            scaledBitmap.recycle();
        }

        return b;

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


    public enum ImageType {
        TYPE_NORMAL
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    public static Bitmap getBitmapByDrawableId(Context mContext, int drawableId, int bitmapWidth, int bitmapHeight) {
//        Bitmap mDefauleBitmap = null;
//        Drawable drawable = ContextCompat.getDrawable(mContext, drawableId);
//        if(drawable!=null){
//            drawable.setBounds(0, 0, width, height);
//            mDefauleBitmap = ((BitmapDrawable)drawable).getBitmap();
//        }
        Bitmap mDefauleBitmap = null;
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), drawableId);
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Log.d(TAG, "width = " + width);
            Log.d(TAG, "height = " + height);
            Matrix matrix = new Matrix();
            matrix.postScale(((float) bitmapWidth) / width, ((float) bitmapHeight) / height);
            mDefauleBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            Log.d(TAG, "mDefauleBitmap getWidth = " + mDefauleBitmap.getWidth());
            Log.d(TAG, "mDefauleBitmap getHeight = " + mDefauleBitmap.getHeight());
        }

        return mDefauleBitmap;
    }


    public static Bitmap getViewBitmap(Activity activity, View view) {
        if (view == null)
            return null;
        Rect r = new Rect();
        view.getGlobalVisibleRect(r);
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int w = display.getWidth();
        int h = display.getHeight();
        Bitmap Bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        View decorview = activity.getWindow().getDecorView();
        decorview.setDrawingCacheEnabled(true);
        Bmp = decorview.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(Bmp, r.left, r.top, r.right - r.left, r.bottom - r.top);
        decorview.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public static Bitmap getCircularBitmap(Bitmap bitmapimg) {
        Bitmap output = Bitmap.createBitmap(bitmapimg.getWidth(),
                bitmapimg.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmapimg.getWidth(),
                bitmapimg.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmapimg.getWidth() / 2,
                bitmapimg.getHeight() / 2, bitmapimg.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmapimg, rect, rect, paint);
        return output;
    }

    public static Bitmap zoomImage(Bitmap bgimage, int newWidth, int newHeight) {

        // 获取这个图片的宽和高

        int width = bgimage.getWidth();
        int height = bgimage.getHeight();

        // 创建操作图片用的matrix对象

        Matrix matrix = new Matrix();

        // 计算缩放率，新尺寸除原始尺寸

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // 缩放图片动作

        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, width, height,
                matrix, true);
        return bitmap;

    }

    /**
     * @param b Bitmap <a
     *          href="\"http://www.eoeandroid.com/home.php?mod=space&uid=7300\""
     *          target="\"_blank\"">@return</a> 图片存储的位置
     */
    public static String saveImg(Bitmap b, String name) throws Exception {
        String path = Environment.getExternalStorageDirectory().getPath()
                + File.separator + "laka/headImg/";
        File mediaFile = new File(path + File.separator + name + ".jpg");
        if (mediaFile.exists()) {
            mediaFile.delete();

        }
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        mediaFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(mediaFile);
        b.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        fos.close();
        b.recycle();
        b = null;
        System.gc();
        return mediaFile.getPath();
    }

    public static boolean saveBitmap(Bitmap bm, String path, int quality) {

        FileOutputStream fos = null;
        try {

            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, quality, fos);
            fos.flush();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean saveBitmap(Bitmap bm, String path) {

        FileOutputStream fos = null;
        try {

            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static Bitmap DecodeUriAsBitmap(Context context, Uri uri) {
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(context.getContentResolver()
                    .openInputStream(uri), null, opts);
            opts.inSampleSize = CalculateOriginal(opts, 1024, 1024);
            opts.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(context.getContentResolver()
                    .openInputStream(uri), null, opts);

        } catch (Throwable e) {
            return null;
        }
        return bitmap;
    }

    public static int CalculateOriginal(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSampleSize = 1;
        if (width > reqWidth || height > reqHeight) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最大的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会小于等于目标的宽和高。
            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;

            /**
             * 以下的处理逻辑用于一些特殊比例的图片，比如全景图片的宽度将远大于其高度
             * 那么由以上算法得出的压缩率将不满足需求，其占用的内存仍然非常大
             */
            // 计算原图的总像素值
            final float totalPixels = width * height;
            // 可允许的最大像素值——要求图片大小的两倍
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;
            // 如果压缩后的总像素扔大于最大像素值，增加压缩率
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    public static void startPhotoZoom(Activity context, Uri uri, int outputX, int outputY, int openCutReqCode) {

        int outPutX = SystemUtil.getScreenWidth(context);
        outPutX = px2dip(context, outPutX);
        int outPutY = outPutX / 2;

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        // 保持比例
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true); // no face detection

        context.startActivityForResult(intent, openCutReqCode);
    }

    /**
     * 图片裁剪
     * 默认裁剪宽高比例 2:1
     *
     * @param context
     * @param uri
     * @param openCutReqCode
     */
    public static void startPhotoZoom(Activity context, Uri uri, int openCutReqCode) {
        startPhotoZoom(context, uri, openCutReqCode, 2);
    }

    /**
     * 图片裁剪，带比例
     *
     * @param context
     * @param uri
     * @param openCutReqCode
     * @param ratio
     */
    public static void startPhotoZoom(Activity context, Uri uri, int openCutReqCode, int ratio) {

        /**
         * description:裁剪TempFile
         **/
        Uri uriTempFile;
        File outputImage = new File(Environment.getExternalStorageDirectory(), "crop.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        uriTempFile = Uri.fromFile(outputImage);

        int outPutX = SystemUtil.getScreenWidth(context);
        outPutX = px2dip(context, outPutX);
        int outPutY = outPutX / ratio;

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", ratio);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", outPutX);
        intent.putExtra("outputY", outPutY);
        // 保持比例
        intent.putExtra("scale", true);
        /**
         * description:小米手机会报错，因为会限制ReturnData的大小
         **/
        if (SystemUtil.isMIPhone()) {
            MI_CROP_PHOTO_PATH = uriTempFile;
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriTempFile);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        } else {
            intent.putExtra("return-data", true);
        }

        Logger.e("重新跳转：" + intent + "\nCode：" + openCutReqCode + "\n重定向Uri：" + uriTempFile);
        context.startActivityForResult(intent, openCutReqCode);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}

