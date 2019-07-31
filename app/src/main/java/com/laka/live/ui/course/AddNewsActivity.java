package com.laka.live.ui.course;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.laka.live.BuildConfig;
import com.laka.live.R;
import com.laka.live.msg.ReleaseNewsMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.network.upload.IUploadListener;
import com.laka.live.network.upload.UploadManager;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.widget.LimitEditText;
import com.laka.live.ui.widget.PopupMenu;
import com.laka.live.ui.widget.WxCircleLoading;
import com.laka.live.util.FastBlur;
import com.laka.live.util.PhotoUtil;
import com.laka.live.util.SystemUtil;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import framework.ioc.annotation.InjectView;

/**
 * Created by Lyf on 2017/6/7.
 * 添加资讯课堂（发布资讯）
 */
public class AddNewsActivity extends BaseActivity implements View.OnClickListener,
        PhotoUtil.OnDecodeResult, GsonHttpConnection.OnResultListener<ReleaseNewsMsg>,PopupMenu.OnPopupListClickListener {

    @InjectView(id = R.id.blurText, click = "") // click=""代表不需要点击事件
    public TextView blurText;
    @InjectView(id = R.id.blurImage)
    public ImageView blurImage;
    @InjectView(id = R.id.back)
    public ImageView back;
    @InjectView(id = R.id.blurLayout)
    public ImageView blurLayout;
    @InjectView(id = R.id.newsTitle)
    public LimitEditText newsTitle;
    @InjectView(id = R.id.newsLink, click = "")
    public TextView newsLink;
    @InjectView(id = R.id.deleteCover)
    public ImageView deleteCover;
    @InjectView(id = R.id.postCourse)
    public TextView postCourseTv;
    @InjectView(id = R.id.circleLoading)
    public WxCircleLoading circleLoading;


    private PhotoUtil mPhotoUtil;
    private PopupMenu mPopupMenu;

    private HashMap<String, String> mParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);
        initView();
    }

    private void initView() {

        mParams = new HashMap<>();
        mPhotoUtil = new PhotoUtil(this);
        initPopupMenu();
        FastBlur.doPartBlur(mContext, blurImage, blurLayout);
        // 获取OSS的KEY
        DataProvider.getStsToken(this);
    }

    private void initPopupMenu() {

        mPopupMenu = new PopupMenu();
        ArrayList<String> popupMenuItemList = new ArrayList<>();
        popupMenuItemList.add("粘贴");
        popupMenuItemList.add("清空");
        mPopupMenu.init(mContext, newsLink, popupMenuItemList,this);
    }

    @Override
    public void onClick(View view) {

        hideKeyboard(mContext);

        switch (view.getId()) {

            case R.id.blurLayout:
                addCourseCover();
                break;
            case R.id.deleteCover:
                setDefaultCover();
                break;
            case R.id.postCourse:
                postCourse();
                break;
//            case R.id.newsLink:
//
//                //
//                break;
            case R.id.back:
                finish();
                break;
        }

    }

    // 发布资讯
    private void postCourse() {

        if (Utils.isEmpty(mParams.get("cover_url"))) {
            ToastHelper.showToast("请上传课程封面");
            return;
        } else if (Utils.isEmpty(newsTitle.getText())) {
            ToastHelper.showToast("请输入资讯标题");
            return;
        } else if (Utils.isEmpty(newsLink.getText().toString())) {
            ToastHelper.showToast("请粘贴微信链接");
            return;
        }

        mParams.put("url", newsLink.getText().toString());
        mParams.put("title", newsTitle.getText());
        DataProvider.postNews(this, mParams, this);
    }

    @Override
    public void onSuccess(ReleaseNewsMsg releaseNewsMsg) {
        PostSuccessActivity.startActivity(this, PostSuccessActivity.POSTNEWSSUCCESSS);
        finish();
    }

    @Override
    public void onFail(int errorCode, String errorMsg, String command) {
        ToastHelper.showToast(errorMsg);
    }

    // 删除课堂封面
    private void setDefaultCover() {

        mParams.put("cover_url", null);
        circleLoading.setVisible(View.GONE);
        blurText.setVisibility(View.VISIBLE);
        blurLayout.setVisibility(View.VISIBLE);
        deleteCover.setVisibility(View.GONE);
        blurImage.setImageResource(R.mipmap.icon_living_bg);
        FastBlur.doPartBlur(mContext, blurImage, blurLayout);
        UploadManager.getInstance().cancelUpload();
    }

    // 添加课堂封面
    private void addCourseCover() {
        mPhotoUtil.takePhoto();
    }

    // 在OnDecodeResult回调方法里处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPhotoUtil.onResult(requestCode, resultCode, data, this);
    }

    // 解析图片的回调
    @Override
    public void onDecodeResult(int requestCode, Bitmap bitmap, String filePath, long size, int duration) {

        switch (requestCode) {
            case PhotoUtil.CUTS_PHOTO:
            case PhotoUtil.TAKE_PHOTO:
            case PhotoUtil.ALBUM_PHOTO:
                handlePhotoFromAlbum(bitmap, filePath);
                break;
        }

    }

    // 处理拿到的图片
    private void handlePhotoFromAlbum(Bitmap bitmap, String filePath) {

        // Log.log("size=" + new File(filePath).length() / 1024 + "kb");
        // 设置图片
        if (bitmap != null)
            blurImage.setImageBitmap(bitmap);
        // 上传图片
        doUploadImage(filePath);
    }

    // 上传图片
    private void doUploadImage(String filePath) {

        blurText.setVisibility(View.GONE);
        blurLayout.setVisibility(View.GONE);
        circleLoading.setVisible(View.VISIBLE);

        // 执行上传代码
        UploadManager.getInstance().uploadFile(BuildConfig.UPLOAD_BUCKET_IMG,
                UploadManager.TAG_NAME, filePath, new IUploadListener() {
                    @Override
                    public void onSuccess(final String url) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("上传完成");
                                mParams.put("cover_url", url);
                                circleLoading.setVisible(View.GONE);
                                deleteCover.setVisibility(View.VISIBLE);
                            }
                        });

                    }

                    @Override
                    public void onFailure(String code, final String errorMsg) {
                        log("onFailure onFailure code=" + code + " msg=" + errorMsg);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast(errorMsg);
                                setDefaultCover();
                            }
                        });
                    }

                    @Override
                    public void onProgress(final long currentSize, final long totalSize) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                circleLoading.setProgress(((float) currentSize / (float) totalSize));
                            }
                        });
                    }
                });

    }

    @Override
    public void onPopupListClick(int mListItemPosition, int mMenuItemPosition) {

        if(mMenuItemPosition == 0) {
            newsLink.setText(SystemUtil.getClipText(this));
        }else{
            newsLink.setText("");
        }

    }
}
