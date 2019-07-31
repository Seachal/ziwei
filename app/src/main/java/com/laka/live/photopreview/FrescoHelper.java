package com.laka.live.photopreview;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.laka.live.util.Log;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ToastHelper;

import java.io.File;

/**
 * Created by zwl on 2015/12/9.
 */
public class FrescoHelper {

    public static void initFresco(Context context) {
        ImagePipelineConfig.Builder pipeConfigBuilder = ImagePipelineConfig.newBuilder(context);
        String cachePath = BitmapUtil.getSdcardPath() + "Live/";
        DiskCacheConfig.Builder diskBuilder = DiskCacheConfig.newBuilder(context);
        diskBuilder.setBaseDirectoryPath(new File(cachePath));
        diskBuilder.setBaseDirectoryName("image_cache");
        diskBuilder.setMaxCacheSize(60 * 1024 * 1024L); // 50M
        diskBuilder.setMaxCacheSizeOnLowDiskSpace(20 * 1024 * 1024L);//20M
        diskBuilder.setMaxCacheSizeOnVeryLowDiskSpace(10 * 1024 * 1024L);//10M
        pipeConfigBuilder.setMainDiskCacheConfig(diskBuilder.build());
        Fresco.initialize(context, pipeConfigBuilder.build());
    }

    //return file or null
    public static File getCachedImageOnDisk(String loadUri) {
        File localFile = null;
        if (StringUtils.isEmpty(loadUri)) {
            return localFile;
        }
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(loadUri), null);
        if (ImagePipelineFactory.getInstance().getMainDiskStorageCache().hasKey(cacheKey)) {
            BinaryResource resource = ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource
                    (cacheKey);
            if (resource != null) {
                localFile = ((FileBinaryResource) resource).getFile();
            }
        } else if (ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().hasKey(cacheKey)) {
            BinaryResource resource = ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache()
                    .getResource(cacheKey);
            localFile = ((FileBinaryResource) resource).getFile();
        }
        return localFile;
    }

    public static void loadImage(String imageUrl, FrescoLoadImageBitmapCallback callback) {
        loadImage(imageUrl, callback, false);
    }

    /**
     * 如果useFrescoCacheBitmap时，可以节省内存的使用，但问题：需要处理bitmap.isRecycled的判断。
     *
     * @param imageUrl
     * @param callback
     * @param useFrescoCacheBitmap
     */
    public static void loadImage(String imageUrl, FrescoLoadImageBitmapCallback callback, boolean
            useFrescoCacheBitmap) {
        if (imageUrl == null) {
            callback.onImageLoadFinish(null);
            return;
        }
        File imageFile = getCachedImageOnDisk(imageUrl);
        if (imageFile != null) {
            callback.onImageLoadFinish(BitmapUtil.createBitmapThumbnail(imageFile.getAbsolutePath()));
        } else {
            tryLoadImage(imageUrl, callback, useFrescoCacheBitmap);
        }
    }

    private static void tryLoadImage(final String imageUrl, final FrescoLoadImageBitmapCallback callback,
                                     final boolean useFrescoCacheBitmap) {
        Uri imageUri = Uri.parse(imageUrl);
        DataSubscriber dataSubscriber = new BaseDataSubscriber<CloseableReference<CloseableBitmap>>() {
            @Override
            public void onNewResultImpl(
                    DataSource<CloseableReference<CloseableBitmap>> dataSource) {
                Bitmap bitmap = null;
                if (dataSource == null || !dataSource.isFinished()) {
                    callback.onImageLoadFinish(bitmap);
                    return;
                }
                CloseableReference<CloseableBitmap> imageReference = dataSource.getResult();
                if (imageReference != null && useFrescoCacheBitmap) {
                    final CloseableReference<CloseableBitmap> closeableReference = imageReference.clone();
                    try {
                        CloseableBitmap closeableBitmap = closeableReference.get();
                        bitmap = closeableBitmap.getUnderlyingBitmap();
                    } catch (Exception e) {
                        e.printStackTrace();
                        bitmap = null;
                    } finally {
                        CloseableReference.closeSafely(imageReference);
                        CloseableReference.closeSafely(closeableReference);
                    }
                }
                if (!useFrescoCacheBitmap) {
                    File cacheFile = getCachedImageOnDisk(imageUrl);
                    if (cacheFile != null) {
                        Bitmap tempBitmap = BitmapUtil.createBitmapThumbnail(cacheFile.getAbsolutePath());
                        if (tempBitmap != null) {
                            bitmap = tempBitmap;
                        }
                    }
                }
                callback.onImageLoadFinish(bitmap);
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                Throwable throwable = dataSource.getFailureCause();
                callback.onImageLoadFinish(null);
                throwable.printStackTrace();
            }
        };
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(imageUri).build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, null);
        dataSource.subscribe(dataSubscriber, UiThreadImmediateExecutorService.getInstance());
    }

    public static void loadImage(String imageUrl, FrescoLoadImagePathCallback callback) {
        if (imageUrl == null) {
            callback.onImageLoadFinish(null);
            return;
        }
        File imageFile = getCachedImageOnDisk(imageUrl);
        if (imageFile != null) {
            callback.onImageLoadFinish(imageFile.getAbsolutePath());
        } else {
            tryLoadImage(imageUrl, callback);
        }
    }

    private static void tryLoadImage(final String imageUrl, final FrescoLoadImagePathCallback callback) {
        Uri imageUri = Uri.parse(imageUrl);
        DataSubscriber dataSubscriber = new BaseDataSubscriber<CloseableReference<CloseableBitmap>>() {
            @Override
            public void onNewResultImpl(
                    DataSource<CloseableReference<CloseableBitmap>> dataSource) {
                if (dataSource == null || !dataSource.isFinished()) {
                    callback.onImageLoadFinish(null);
                    return;
                }
                String path = null;
                File cacheFile = getCachedImageOnDisk(imageUrl);
                if (cacheFile != null) {
                    path = cacheFile.getAbsolutePath();
                }
                callback.onImageLoadFinish(path);
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                Throwable throwable = dataSource.getFailureCause();
                callback.onImageLoadFinish(null);
                throwable.printStackTrace();
            }
        };
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(imageUri).build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, null);
        dataSource.subscribe(dataSubscriber, UiThreadImmediateExecutorService.getInstance());
    }


    public interface FrescoLoadImageBitmapCallback {
        void onImageLoadFinish(Bitmap bitmap);
    }

    public interface FrescoLoadImagePathCallback {
        void onImageLoadFinish(String path);
    }

}
