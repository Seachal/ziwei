package com.laka.live.util;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.laka.live.ui.widget.panel.ActionSheetPanel;
import com.laka.live.video.constant.VideoConstant;
import com.laka.live.video.model.bean.VideoMaterialBean;
import com.laka.live.video.ui.activity.VideoMaterialActivity;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.text.SimpleDateFormat;

import framework.utils.FileUtil;
import me.shaohui.advancedluban.Luban;
import me.shaohui.advancedluban.OnCompressListener;

/**
 * Created by Lyf on 2017/5/9.
 */
public class PhotoUtil {

    private Activity mActivity;
    private int requestCode;

    private boolean isCut = true; // 是否要栽剪
    private String cacheImage; // 拍照的图片的缓存地址
    private OnDecodeResult mDecodeResultListener; //解析视频、图片的回调
    public static boolean isCompressing = false; // 当前是否在压缩
    // 从相册拿图片 = 1 ， 从相册拿视频 = 2 ， 录视频 = 3， 裁剪图片 = 4 ， 拍照 = 5 , 素材库 = 6
    public static final int ALBUM_PHOTO = 1, ALBUM_VIDEO = 2, RECORD_VIDEO = 3,
            CUTS_PHOTO = 4, TAKE_PHOTO = 5, MATERIAL_STORE = 6;
    private boolean isUseMaterialStore = false;
    private boolean isUseMediaStore = true;
    private final int EXTRA_DURATION_LIMIT = 60; // 预告视频的时长限制(秒)
    private final int EXTRA_IMAGE_MAX_SIZE = 10; // 上传的图片的原图不能大于当前值,单位是M。
    private final int EXTRA_VIDEO_QUALITY = 0; // 此值在最低质量最小文件尺寸时是0，在最高质量最大文件尺寸时是１.
    // 上传的图片最大宽度
    private static final int POST_IMAGE_MAX_WIDTH = 1280;
    // 上传的图片最大高度
    private static final int POST_IMAGE_MAX_HEIGHT = 1080;
    //上传的图片压缩后的最大尺寸(体积kb为单位)
    private static final int POST_IMAGE_MAX_SIZE = 1024;

    /**
     * description:图片裁剪
     **/
    private static final int DEFAULT_CUT_RATIO = 2;
    private int cutRatio = DEFAULT_CUT_RATIO;

    /**
     * description:素材库回调
     **/
    private OnMaterialStoreResult onMaterialStoreResult;

    public PhotoUtil(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void onResult(int requestCode, int resultCode, Intent data,
                         OnDecodeResult mDecodeResultListener) {

//        Logger.i("PhotoUtil onResult" +
//                "\nrequestCode：" + requestCode +
//                "\nresultCode：" + resultCode +
//                "\nData：" + data);

        if (data == null && requestCode != TAKE_PHOTO) {
            return;
        }

        this.requestCode = requestCode;
        this.mDecodeResultListener = mDecodeResultListener;

        switch (requestCode) {

            case ALBUM_PHOTO:
                // 裁剪图片
                if (isCut) {
//                    Logger.e("裁剪PIC：" + data.getData());
                    ImageUtil.startPhotoZoom(mActivity, data.getData(), CUTS_PHOTO, cutRatio);
                } else {
                    this.requestCode = ALBUM_PHOTO;
                    if (data.getScheme().equals("content")) {
                        compressBitmap(getRealPathFromURI(data.getData()));
                    } else {
                        handlePhotoFromAlbum(data);
                    }
                }
                break;
            case ALBUM_VIDEO:
                // 从相册拿到视频
                handleVideoFromAlbum(data);
                break;
            case RECORD_VIDEO:
                // 拿到录制的新视频
                handleVideoFromCamera(data);
                break;
            case CUTS_PHOTO:
                // 处理裁剪后的图片
//                Logger.e("图片裁剪完毕");
                handlePhotoFromAlbum(data);
//                if(data.getScheme() == null) {
//                    Log.log("1");
//                    handlePhotoFromAlbum(data);
//                } else if (data.getScheme().equals("content")) {
//                    Log.log("2");
//                    compressBitmap(getRealPathFromURI(data.getData()));
//                } else if (data.getScheme().equals("file")) {
//                    Log.log("3");
//                    compressBitmap(cacheImage);
//                } else {
//                    Log.log("4");
//
//                }
                break;
            case TAKE_PHOTO:
                // 拍照
                if (isCut) {
                    if (new File(cacheImage).length() == 0)
                        return;
                    ImageUtil.startPhotoZoom(mActivity, Uri.fromFile(new File(cacheImage)), CUTS_PHOTO, cutRatio);
                } else {
                    this.requestCode = TAKE_PHOTO;
                    handlePhotoFromCamera();
                }
                break;
            case MATERIAL_STORE:
                //从素材库选择
                VideoMaterialBean materialBean = (VideoMaterialBean) data.getSerializableExtra(VideoConstant.MATERIAL_EXTRA_ITEM);
                handleVideoFromMaterialStore(materialBean);
                break;
            default:
                break;
        }

    }

    public String getRealPathFromURI(Uri contentUri) {

        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = mActivity.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null)
            return null;
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    // 拍照
    public void takePhotoFromCamera() {

        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hhmmss");
            cacheImage = FileUtil.getCachePhotoDir() + "/BeiKe" + (sdf.format(System.currentTimeMillis())) + ".jpg";
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(cacheImage)));
            mActivity.startActivityForResult(intent, TAKE_PHOTO);
        } catch (Exception e) {
            toast("抱歉，无法打开摄像头，请选择其他方式获取图片");
        }

    }

    // 录视频
    public void takeVideoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, EXTRA_VIDEO_QUALITY);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, EXTRA_DURATION_LIMIT);
        mActivity.startActivityForResult(intent, RECORD_VIDEO);
    }

    // 从相册选择视频
    public void takeVideoFromAlbum() {

        if (Build.VERSION.SDK_INT < 19) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("video/*");// image/*
            mActivity.startActivityForResult(photoPickerIntent, ALBUM_VIDEO);
        } else {
            Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
            photoPickerIntent.setType("*/*");
            mActivity.startActivityForResult(photoPickerIntent, ALBUM_VIDEO);
        }

    }

    public void takeVideoFromMaterialStore() {
        Intent intent = new Intent();
        intent.setClass(mActivity, VideoMaterialActivity.class);
        intent.putExtra(VideoConstant.MATERIAL_UI_TYPE, VideoConstant.MATERIAL_TYPE_CHOOSE);
        mActivity.startActivityForResult(intent, MATERIAL_STORE);
    }

    // 从相册选择图片
    public void takePhotoFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        mActivity.startActivityForResult(intent, ALBUM_PHOTO);
    }

    // 从相册选择图片
    public void takePhotoFromAlbum(boolean isCut) {
        this.isCut = isCut;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        mActivity.startActivityForResult(intent, ALBUM_PHOTO);
    }

    // 处理从相册拿到的视频
    public void handleVideoFromAlbum(Intent data) {

        try {

            Uri uri = data.getData();
            String filePath = GetPathFromUri4kitkat.getPath(mActivity, uri);
            if (filePath == null) {
                toast("请选择正确的视频文件");
                return;
            } else if (!filePath.endsWith(".mp4")) {
                toast("请选择正确的视频文件(仅限mp4文件)");
                return;
            }
            // 解码视频
            decodeVideoCover(new File(filePath), filePath);
        } catch (SecurityException e) {
            toast("获取视频失败，请先授权读写SD卡权限");
        } catch (Exception e) {
            Log.log("handleVideoFromAlbum:" + e.toString());
            toast("获取视频失败");
        }

    }

    // 处理拍照后拿到的图片
    public void handlePhotoFromCamera() {

        // 这个项目上传图片时自带压缩功能。。不用压了
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 压缩图片
                    compressBitmap(cacheImage);
                } catch (Exception e) {
                    toast("获取图片失败");
                }
            }
        }).start();

    }

    // 处理录制的视频
    private void handleVideoFromCamera(Intent data) {

        Uri uri = data.getData();
        Cursor cursor = mActivity.getContentResolver().query(uri, null, null, null, null);

        if (cursor != null && cursor.moveToNext()) {

            final String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
            cursor.close();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        decodeVideoCover(new File(filePath), filePath);
                    } catch (Exception e) {
                        toast("解码视频封面失败");
                    }
                }
            }).start();

        }


    }

    // 处理从相册拿到的图片(一般不执行这里，主要用来兼容个别机型)
    private void handlePhotoFromAlbum(final Intent data) {

        Bitmap bitmap = null;
        String filePath = "";
        Uri tempPath = null;
        try {

            if (data.getExtras() != null) {
                bitmap = (Bitmap) data.getExtras().get("data");
            }

            if (bitmap == null && data.getData() != null) {
                bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), data.getData());
            }

            /**
             * description:部分机型可能因为系统限制。intent的data不能过大，这里直接取URI自己转
             * 例如小米巨坑
             **/
            if (bitmap == null && data.getParcelableExtra(MediaStore.EXTRA_OUTPUT) != null) {
                tempPath = data.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
                bitmap = BitmapFactory.decodeStream(mActivity.getContentResolver().openInputStream(tempPath));
            }

            /**
             * description:部分手机连Crop的事件都没有走回调，这里面需要ImageUtil存一个静态的变量保存
             * 还是小米巨坑
             **/
            if (bitmap == null) {
                tempPath = ImageUtil.MI_CROP_PHOTO_PATH;
                bitmap = BitmapFactory.decodeStream(mActivity.getContentResolver().openInputStream(tempPath));
            }

//            Logger.e("输出TempPath：" + tempPath);

            if (bitmap == null) {
                return;
            }

            // 限制原图大小
            if (isExceedSize(filePath)) {
                return;
            }

            // 创建缓存文件
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hhmmss");
            filePath = FileUtil.getCompressPhotoDir() + "/TTClub_IMAGE_" + (sdf.format(System.currentTimeMillis())) + ".jpg";
            ImageUtil.saveBitmap(bitmap, filePath);
            // 压缩图片
            compressBitmap(filePath);

        } catch (Exception e) {
//            Logger.e("Error----handlePhotoFromAlbum：" + e.toString());
            Log.log("handlePhotoFromAlbum:" + e.toString());
        }

    }

    /**
     * 从素材库选择，保存当前数据发布视频的时候回调
     */
    private void handleVideoFromMaterialStore(VideoMaterialBean materialBean) {
        if (onMaterialStoreResult != null) {
            onMaterialStoreResult.onChooseMaterialResult(materialBean);
        }
    }

    // 拍照或从相册拿到图片后，进行压缩(一般是拿完就开始压，个别机型可能拿不到，需要执行handlePhotoFromAlbum)
    private void compressBitmap(String filePath) {
//        Logger.e("压缩图片：" + filePath);
        if (isExceedSize(filePath)) {
            return;
        }

        Luban.compress(mActivity, new File(filePath))
                .setMaxSize(POST_IMAGE_MAX_SIZE)
//                .setMaxWidth(POST_IMAGE_MAX_WIDTH)
//                .setMaxHeight(POST_IMAGE_MAX_HEIGHT)
                .putGear(Luban.CUSTOM_GEAR)
                .launch(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        isCompressing = true;
                    }

                    @Override
                    public void onSuccess(File file) {

                        isCompressing = false;
                        if (file != null) {
                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            mDecodeResultListener.onDecodeResult(requestCode, bitmap, file.getAbsolutePath(), file.length() / 1024 / 1024, 0);
//                            Logger.e("回调：" + bitmap);
                        } else {
                            mDecodeResultListener.onDecodeResult(requestCode, null, "", 0, 0);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        isCompressing = false;
                    }
                });
    }

    // 上传的原图的大小是否超过限制
    private boolean isExceedSize(String filePath) {

        Log.log("isExceedSize=" + (new File(filePath).length() / 1024 / 1024));

        // 限制原图大小
        if (new File(filePath).length() / 1024 / 1024 >= EXTRA_IMAGE_MAX_SIZE) {
            ToastHelper.showToast("请上传小于" + EXTRA_IMAGE_MAX_SIZE + "M的图片");
            return true;
        } else {
            return false;
        }

    }

    // 解码视频的封面
    private void decodeVideoCover(final File file, final String filePath) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                long size = 1; // 默认1M
                int duration = 1; // 默认1秒
                Bitmap bitmap = null; // 可以没有图片。。。
                try {
                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();//实例化MediaMetadataRetriever对象
                    mmr.setDataSource(filePath);
                    bitmap = mmr.getFrameAtTime();//获得视频第一帧的Bitmap对象
                } catch (Exception e) {
                    Log.log("decodeVideoCover=" + e.toString());
                }
                try {
                    size = file.length() / 1024 / 1024; // 获取视频大小
                } catch (Exception e) {
                    Log.log("decodeVideoCover=" + e.toString());
                }
                try {
                    duration = Integer.parseInt(getDuration(filePath)); // 获取视频时长
                } catch (Exception e) {
                    Log.log("decodeVideoCover=" + e.toString());
                }
                mDecodeResultListener.onDecodeResult(requestCode, bitmap, filePath, size, duration);
            }
        }).start();
    }

    // 获取视频秒长
    public static String getDuration(String filePath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();//实例化MediaMetadataRetriever对象
        mmr.setDataSource(filePath);
        long duration = Long.parseLong(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));//时长(毫秒)
        return String.valueOf(duration / 1000);
    }

    // 获取图片
    public void takePhoto() {

        ActionSheetPanel panel = new ActionSheetPanel(mActivity);
        ActionSheetPanel.ActionSheetItem item = new ActionSheetPanel.ActionSheetItem();
        item.title = "拍照";
        item.id = "takePhotoFromCamera";
        panel.addSheetItem(item);

        item = new ActionSheetPanel.ActionSheetItem();
        item.title = "从相册选择";
        item.id = "takePhotoFromAlbum";
        panel.addSheetItem(item);

        panel.setSheetItemClickListener(new ActionSheetPanel.OnActionSheetClickListener() {
            @Override
            public void onActionSheetItemClick(String id) {
                if (id.equals("takePhotoFromCamera")) {
                    takePhotoFromCamera();
                } else {
                    takePhotoFromAlbum();
                }
            }
        });
        panel.showPanel();
    }


    // 获取视频
    public void takeVideo() {

        ActionSheetPanel panel = new ActionSheetPanel(mActivity);
        ActionSheetPanel.ActionSheetItem item = null;

        if (isUseMaterialStore) {
            item = new ActionSheetPanel.ActionSheetItem();
            item.title = "从素材库里面选择";
            item.id = "takeVideoFromMaterial";
            panel.addSheetItem(item);
        }

        if (isUseMediaStore) {
            item = new ActionSheetPanel.ActionSheetItem();
            item.title = "录视频";
            item.id = "takeVideoFromCamera";
            panel.addSheetItem(item);

            item = new ActionSheetPanel.ActionSheetItem();
            item.title = "从相册选择";
            item.id = "takeVideoFromAlbum";
            panel.addSheetItem(item);
        }

        panel.setSheetItemClickListener(new ActionSheetPanel.OnActionSheetClickListener() {
            @Override
            public void onActionSheetItemClick(String id) {
                if (id.equals("takeVideoFromCamera")) {
                    takeVideoFromCamera();
                } else if (id.equals("takeVideoFromAlbum")) {
                    takeVideoFromAlbum();
                } else if (id.equals("takeVideoFromMaterial")) {
                    takeVideoFromMaterialStore();
                }
            }
        });
        panel.showPanel();
    }


    private void toast(final String toast) {

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mActivity, toast, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setOnMaterialStoreResult(OnMaterialStoreResult onMaterialStoreResult) {
        this.onMaterialStoreResult = onMaterialStoreResult;
    }

    public interface OnDecodeResult {
        void onDecodeResult(int requestCode, @Nullable Bitmap bitmap, String filePath, long size, int duration);
    }

    public interface OnMaterialStoreResult {

        /**
         * 素材库选择回调
         *
         * @param materialBean
         */
        void onChooseMaterialResult(VideoMaterialBean materialBean);
    }

    private View photoView = null;

    public void setView(View photoView) {
        this.photoView = photoView;
    }

    public View getView() {
        return photoView == null ? new View(mActivity) : photoView;
    }

    public boolean isCut() {
        return isCut;
    }

    public void setCut(boolean cut) {
        isCut = cut;
    }

    public void setCutRatio(int cutRatio) {
        this.cutRatio = cutRatio;
    }

    public void setUseMaterialStore(boolean useMaterialStore) {
        isUseMaterialStore = useMaterialStore;
    }

    public void setUseMediaStore(boolean useMediaStore) {
        isUseMediaStore = useMediaStore;
    }
}
