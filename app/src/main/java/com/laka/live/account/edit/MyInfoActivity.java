package com.laka.live.account.edit;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.laka.live.BuildConfig;
import com.laka.live.R;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.account.login.LoginActivity;
import com.laka.live.application.LiveApplication;
import com.laka.live.bean.UserInfo;
import com.laka.live.msg.Msg;
import com.laka.live.msg.QiNiuUploadMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.network.upload.IUploadListener;
import com.laka.live.network.upload.UploadManager;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.activity.WebActivity;
import com.laka.live.ui.widget.MyInfoItemView;
import com.laka.live.ui.widget.Toast;
import com.laka.live.ui.widget.panel.ActionSheetPanel;
import com.laka.live.util.Common;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Util;
import com.laka.live.util.Utils;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

import framework.utils.FileUtil;

public class MyInfoActivity extends BaseActivity implements View.OnClickListener,
        GsonHttpConnection.OnResultListener<QiNiuUploadMsg>, ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = "MyInfoActivity";
    private static final int REQUEST_PERMISSION_CAMERA = 0;
    private static final int REQUEST_PERMISSION_PICTURE = 1;
    private static final int PHOTO_REQUEST_TAKE_PHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int REQUEST_IMAGE = 0;
    private static final int REQUEST_REGION = 1;
    private static final String EXTRA_IS_CROP = "EXTRA_IS_CROP";
    private static final int REQUEST_TAKE_PHOTO = 4;
    private static final int REQUEST_CHOICE_PHOTO = 5;
    private static final int REQUEST_CROP_SMALL_PICTURE = 3;
    public static final String EXTRA_RESULT = "EXTRA_RESULT";
    private static final String PHOTO_CACHE_DIR = "LA_KA/live/photo/";
    private static final int PHOTO_ASPECT = 240;
    private File mImageFile;
    private boolean isCrop = true;
    private SimpleDraweeView mFace;
    private MyInfoItemView mNickItem;
    private MyInfoItemView mSexItem;
    private MyInfoItemView mRegionItem;
    private MyInfoItemView mTagItem;
    private MyInfoItemView mSignItem;
    private MyInfoItemView mAutoItem;
    private String mFilePath;


    public static void startActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, MyInfoActivity.class);
            ActivityCompat.startActivity(activity, intent, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        init();
    }

    private void init() {
        initFace();
        mNickItem = initItem(R.id.nick_name_item);
        mSexItem = initItem(R.id.sex_item);
        mRegionItem = initItem(R.id.region_item);
        mTagItem = initItem(R.id.tag_item);
        mSignItem = initItem(R.id.sign_item);
        mAutoItem = initItem(R.id.auto_info_item);
    }

    private void initFace() {
        View item = findViewById(R.id.face_item);
        item.setOnClickListener(this);
        mFace = (SimpleDraweeView) item.findViewById(R.id.user_face);
    }

    private MyInfoItemView initItem(int itemId) {
        MyInfoItemView item = (MyInfoItemView) findViewById(itemId);
        item.setOnClickListener(this);
        return item;
    }

    private void updateInfo() {
        ImageUtil.loadImage(mFace, AccountInfoManager.getInstance().getCurrentAccountUserAvatar());
        mNickItem.setValueText(AccountInfoManager.getInstance().getCurrentAccountNickName());
        int sex = AccountInfoManager.getInstance().getCurrentAccountSex();
        String sexStr;
        if (sex != UserInfo.GENDER_UNKNOW) {
            if (sex == UserInfo.GENDER_BOY) {
                sexStr = getString(R.string.boy);
            } else {
                sexStr = getString(R.string.girl);
            }
        } else {
            sexStr = "";
        }
        mSexItem.setValueText(sexStr);
        mRegionItem.setValueText(AccountInfoManager.getInstance().getCurrentAccountLocation());
        String sign = AccountInfoManager.getInstance().getCurrentAccountSign();
        if (StringUtils.isEmpty(sign)) {
            sign = ResourceHelper.getString(R.string.default_sign);
        }
        mSignItem.setValueText(sign);
        mAutoItem.setValueText(getString(R.string.apply_now));
    }

    private void handleEditUserHeadIcon() {
        ActionSheetPanel panel = new ActionSheetPanel(this);

        ActionSheetPanel.ActionSheetItem item = new ActionSheetPanel.ActionSheetItem();
        item.id = String.valueOf(PHOTO_REQUEST_GALLERY);
        item.title = ResourceHelper.getString(R.string.choice_from_gallery);
        panel.addSheetItem(item);

        item = new ActionSheetPanel.ActionSheetItem();
        item.id = String.valueOf(PHOTO_REQUEST_TAKE_PHOTO);
        item.title = ResourceHelper.getString(R.string.take_picture);
        panel.addSheetItem(item);

        panel.setSheetItemClickListener(new ActionSheetPanel.OnActionSheetClickListener() {
            @Override
            public void onActionSheetItemClick(String id) {
                if (String.valueOf(PHOTO_REQUEST_GALLERY).equals(id)) {
                    requestPermissionByPic();
                } else if (String.valueOf(PHOTO_REQUEST_TAKE_PHOTO).equals(id)) {
                    requestPermissionByCamera();
                }
            }
        });
        panel.showPanel();
    }

    private void editNickName() {
        EditNickNameActivity.startActivity(this);
    }

    private void editSex() {
        EditSexActivity.startActivity(this);
    }

    private long mLastClickTiem = 0;

    private void editRegion() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastClickTiem > 500) {
            EditRegionActivity.startActivityForResult(this, REQUEST_REGION);
        }
        mLastClickTiem = currentTime;
    }

    private void editSign() {
        EditSignActivity.startActivity(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.face_item:
                handleEditUserHeadIcon();
                break;
            case R.id.nick_name_item:
                editNickName();
                break;
            case R.id.sex_item:
                editSex();
                break;
            case R.id.region_item:
                editRegion();
                break;
            case R.id.tag_item:
                break;
            case R.id.sign_item:
                editSign();
                break;
            case R.id.auto_info_item:
                handleOnApproveUserItemClick();
                break;
        }
    }

    private void handleOnApproveUserItemClick() {
        String userId = AccountInfoManager.getInstance().getCurrentAccountUserIdStr();
        if (StringUtils.isEmpty(userId)) {
            ToastHelper.showToast(ResourceHelper.getString(R.string.login_outdate));
            AccountInfoManager.getInstance().loginOut();
            LiveApplication.getInstance().exitApp(this);
            LoginActivity.startActivity(this);
            return;
        }
        WebActivity.startActivity(this, Common.APPROVE_URL + System.currentTimeMillis(), getString(R.string.auth_info));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CHOICE_PHOTO:
                    if (data != null && data.getData() != null) {
                        Uri selectedImage = data.getData();
                        String picturePath = Util.getRealPathFromURI(this, selectedImage);
                        Log.d(TAG, "Uri:" + selectedImage.toString() + ";picPath:" + picturePath);
                        if (StringUtils.isNotEmpty(picturePath)) {
                            mImageFile = new File(picturePath);
                            ImageUtil.startPhotoZoom(this, data.getData(),  PHOTO_ASPECT, PHOTO_ASPECT,REQUEST_CROP_SMALL_PICTURE);
                            //Util.cropImageUri(this, Uri.fromFile(mImageFile), PHOTO_ASPECT, PHOTO_ASPECT, REQUEST_CROP_SMALL_PICTURE);
//                            onImageResult(mImageFile);
                        } else {
                            ToastHelper.showToast(ResourceHelper.getString(R.string.choice_photo_no_exist));
                        }
                    } else {
                        ToastHelper.showToast(ResourceHelper.getString(R.string.choice_photo_fail));
                    }
                    break;
                case REQUEST_TAKE_PHOTO:
                    Log.d(TAG, "mImageFile :" + mImageFile.toString());
                    if (mImageFile == null || !mImageFile.exists()) {
                        ToastHelper.showToast(ResourceHelper.getString(R.string.take_photo_fail));
                        return;
                    }

                    Util.cropImageUri(this, Uri.fromFile(mImageFile), PHOTO_ASPECT, PHOTO_ASPECT, REQUEST_CROP_SMALL_PICTURE);
//                    if (isCrop) {
//                        handleOnCropPhoto();
//                    } else {
//                        onImageResult(mImageFile);
//                    }
                    break;
                case REQUEST_CROP_SMALL_PICTURE:
                    onImageResult(data, mImageFile);
                    break;
                case REQUEST_REGION:
                    onRegionResult(data);
                    break;
            }
        }
    }

    private void handleOnCropPhoto() {
        File photoCache = new File(getPhotoCacheDir(), "cropped");
        if (photoCache == null) {
            ToastHelper.showToast(R.string.edit_face_fail);
            return;
        }
        Uri outputUri = Uri.fromFile(photoCache);
        new Crop(Uri.fromFile(mImageFile)).output(outputUri).withAspect(PHOTO_ASPECT, PHOTO_ASPECT).start(this);
    }

    private void onImageResult(Intent data, File imageFile) {
        if (imageFile == null || !imageFile.isFile()) {
            showToast(R.string.edit_face_fail);
            return;
        }
        if (TextUtils.isEmpty(imageFile.getAbsolutePath())) {
            showToast(R.string.edit_face_fail);
            return;
        }

        final Bitmap photo = (Bitmap) data.getExtras().get("data");
        // 创建缓存文件
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hhmmss");
        final String filePath = FileUtil.getCachePhotoDir() + "/BK_IMAGE_" + (sdf.format(System.currentTimeMillis())) + ".jpg";
        // 保存图片
        ImageUtil.saveBitmap(photo, filePath);

        editUserFace(filePath);
    }

    // 修改用户头像
    private void editUserFace(final String filePath) {
        showLoadingDialog(false);

        // 执行上传代码
        UploadManager.getInstance().uploadFile(BuildConfig.UPLOAD_BUCKET_IMG,
                UploadManager.TAG_NAME, filePath, new IUploadListener() {
                    @Override
                    public void onSuccess(final String url) {

                        HashMap<String, String> params = new HashMap<>();
                        params.put(Common.AVATAR, url);
                        DataProvider.editUserInfo(this, params, new GsonHttpConnection.OnResultListener<Msg>() {
                            @Override
                            public void onSuccess(Msg msg) {
                                dismissLoadingsDialog();
                                showToast(R.string.edit_face_success, Toast.LENGTH_SHORT);
                                String saveUrl = "http://img.zwlive.lakatv.com/" + url;
                                AccountInfoManager.getInstance().updateCurrentAccountHeadUrl(saveUrl);
                                ImageUtil.loadImage(mFace, AccountInfoManager.getInstance().getCurrentAccountUserAvatar());
                            }

                            @Override
                            public void onFail(int errorCode, String errorMsg, String command) {
                                showToast(R.string.edit_face_fail, Toast.LENGTH_SHORT);
                                dismissLoadingsDialog();
                            }
                        });
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {

                    }

                    @Override
                    public void onProgress(long currentSize, long totalSize) {
                    }
                });


//        DataProvider.getQinNiuUpLoadToken(this, Common.UPLOAD_TYPE_AVATAR, new GsonHttpConnection.OnResultListener<GetQinNiuUpLoadTokenMsg>() {
//            @Override
//            public void onSuccess(GetQinNiuUpLoadTokenMsg msg) {
//                File imageFile = new File(filePath);
//                try {
//                    DataProvider.uploadImageToQiNiu(this, msg.getToken(), msg.getKey(), msg.getUploadToken(),
//                            imageFile, MyInfoActivity.this);
//                } catch (FileNotFoundException e) {
//                    Log.error("test", "ex ", e);
//                    showToast(R.string.edit_face_fail);
//                    dismissLoadingsDialog();
//                }
//            }
//
//            @Override
//            public void onFail(int errorCode, String errorMsg, String command) {
//                dismissLoadingsDialog();
//                showToast(R.string.edit_face_fail, Toast.LENGTH_SHORT);
//            }
//        });
    }

    private void onRegionResult(Intent data) {
        if (data != null) {
            String fatherRegion = data.getStringExtra(EditRegionActivity.EXTRA_FATHER);
            String region = data.getStringExtra(EditRegionActivity.EXTRA_CITY_NAME);
            if (StringUtils.isNotEmpty(region)) {
                region = String.format(Locale.getDefault(), "%s %s", fatherRegion, region);
            }
            AccountInfoManager.getInstance().updateCurrentAccountLocation(region);
            mRegionItem.setValueText(AccountInfoManager.getInstance().getCurrentAccountLocation());

            HashMap<String, String> params = new HashMap<>();
            params.put(Common.REGION, region);
            DataProvider.editUserInfo(this, params, null);
        }
    }

    @Override
    public void onSuccess(QiNiuUploadMsg userMsg) {
        dismissLoadingsDialog();
        showToast(R.string.edit_face_success, Toast.LENGTH_SHORT);
        String url = ImageUtil.LOCAL_IMAGE_URI_PREFIX + mImageFile.getAbsolutePath();
        AccountInfoManager.getInstance().updateCurrentAccountHeadUrl(url);
        ImageUtil.loadImage(mFace, AccountInfoManager.getInstance().getCurrentAccountUserAvatar());
    }

    @Override
    public void onFail(int errorCode, String errorMsg, String command) {
        showToast(R.string.edit_face_fail, Toast.LENGTH_SHORT);
        dismissLoadingsDialog();
    }

    private void requestPermissionByPic() {
        if (Utils.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && Utils.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            choiceImage();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_PICTURE);
        }
    }

    private void requestPermissionByCamera() {
        if (Utils.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                && Utils.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && Utils.checkPermission(this, Manifest.permission.CAMERA)) {
            takePicture();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
        }
    }

    private void takePicture() {
        try {
            mImageFile = Util.createImageFile(this, Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "mImageFile :" + mImageFile.toString());
        Util.takePhoto(this, mImageFile, REQUEST_TAKE_PHOTO);
    }

    private void choiceImage() {
        if (!Util.choicePhoto(this, REQUEST_CHOICE_PHOTO)) {
            showToast(R.string.can_not_find_gallery);
        }
    }

    private File getPhotoCacheDir() {
        File cacheDir = null;
        if (getCacheDir() != null) {
            cacheDir = getCacheDir();
        } else if (getExternalCacheDir() != null) {
            cacheDir = getExternalCacheDir();
        } else {
            cacheDir = new File(PHOTO_CACHE_DIR);
        }
        if (!cacheDir.isDirectory()) {
            cacheDir.mkdir();
        }
        return cacheDir;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_CAMERA:
                for (int i : grantResults) {
                    if (i == PackageManager.PERMISSION_DENIED) {
                        showToast("请打开文件读写权限和拍照权限。");
                        return;
                    }
                }
                takePicture();
                break;
            case REQUEST_PERMISSION_PICTURE:
                for (int i : grantResults) {
                    if (i == PackageManager.PERMISSION_DENIED) {
                        showToast("请打开文件读写权限。");
                        return;
                    }
                }
                choiceImage();
                break;
            default:
                break;
        }
    }
}
