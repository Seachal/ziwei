package com.laka.live.ui.course;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.laka.live.BuildConfig;
import com.laka.live.R;
import com.laka.live.bean.Course;
import com.laka.live.constants.Constants;
import com.laka.live.help.EventBusManager;
import com.laka.live.help.NetStateManager;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.network.upload.IUploadListener;
import com.laka.live.network.upload.UploadManager;
import com.laka.live.ui.search.SearchRecommendGoodsActivity;
import com.laka.live.ui.widget.WxCircleLoading;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.PhotoUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.UiPreference;
import com.laka.live.util.Utils;
import com.laka.live.util.ViewUtils;
import com.laka.live.video.model.bean.VideoMaterialBean;
import com.laka.live.player.MyExoPlayerPlus;

import org.greenrobot.eventbus.Subscribe;

import cn.jzvd.Jzvd;
import framework.ioc.annotation.InjectView;
import framework.utils.GsonTools;

/**
 * Created by Lyf on 2017/3/28.
 * 新增视频课程
 */
public class AddVideoActivity extends AddLiveActivity implements IUploadListener {

    @InjectView(id = R.id.videoLayout)
    public View videoLayout;
    @InjectView(id = R.id.addVideo)
    public View addVideo;
    @InjectView(id = R.id.priceView)
    public View priceView;
    @InjectView(id = R.id.videoText, click = "")
    public View videoText;
    @InjectView(id = R.id.courseVideo, click = "")
    public ImageView courseVideo;
    @InjectView(id = R.id.deleteVideo)
    public ImageView deleteVideo;
    @InjectView(id = R.id.videoLoading)
    public WxCircleLoading videoLoading;

    private boolean isUpLoading = false; // 当前的上传进度

    public static void startActivityForResult(Activity mContext, int requestCode) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", Course.VIDEO);
        Utils.startActivityForResult(mContext, AddVideoActivity.class, bundle, requestCode);
    }

    public static void startActivityForResult(Activity mContext, Bundle bundle, int requestCode) {
        bundle.putInt("type", Course.VIDEO);
        Utils.startActivityForResult(mContext, AddVideoActivity.class, bundle, requestCode);
    }

    @Override
    protected void initControl() {
        super.initControl();

        NetStateManager.getInstance().start();
        videoLayout.setVisibility(View.VISIBLE); // 显示添加课堂视频的部分
        airTimeLayout.setVisibility(View.GONE); // 隐藏开播时间
        // 动态调整课程价格的高度
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) priceView.getLayoutParams();
        params.height = ResourceHelper.getDimen(R.dimen.space_70);
        priceView.setLayoutParams(params);

        initData();
    }

    // 初始化数据
    private void initData() {

        if (getIntent().getSerializableExtra("data") != null) {

            isEdit = true;
            editPosition = getIntent().getIntExtra("editPosition", -1);
            mCourse = (Course) getIntent().getSerializableExtra("data");
            if (mCourse.id == 0) {
                deleteCourse.setVisibility(View.VISIBLE);  // 未发布的课程才能在编辑时删除
            }
        } else {
            String cache = UiPreference.getString("Course" + Course.VIDEO, null);
            mCourse = GsonTools.fromJson(cache, Course.class);
            if (mCourse == null) {
                mCourse = new Course();
                mCourse.setType(Course.VIDEO);
                mCourse.setItemType(Constants.TYPE_ITEM);
            }
            if (Utils.isNotEmpty(mCourse.getObjectKey())) {
                UploadManager.getInstance()
                        .resumedUploadFile(BuildConfig.UPLOAD_BUCKET_VIDEO, UploadManager.TAG_NAME, mCourse.getObjectKey(), mCourse.getLocalVideoUrl(), this);
            }
//            Logger.e("读取课程内容：" + cache);
        }

        initView();
    }

    @Override
    protected boolean checkCourse() {
        return super.checkCourse();
    }

    @Override
    protected void initView() {
        super.initView();

        // 恢复课程视频
        if (Utils.isNotEmpty(mCourse.getVideo_url())) {
            videoText.setVisibility(View.GONE);
            videoLoading.setVisible(View.GONE);
            deleteVideo.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(mCourse.getLocalVideoUrl())) {
                ImageUtil.displayImage(courseVideo, mCourse.getSnapshot_url(), R.drawable.transparent);
            } else {
                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(mCourse.getLocalVideoUrl(),
                        MediaStore.Video.Thumbnails.MICRO_KIND);
                courseVideo.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    protected void addCourse() {
        if (Utils.isEmpty(mCourse.getVideo_url())) {
            ToastHelper.showToast("请添加课程视频");
        } else if (checkCourse()) {
            Intent intent = new Intent();
            // 确保返回时有item类型
            intent.putExtra("data", mCourse);
            intent.putExtra("editPosition", editPosition);
            setResult(isEdit ? Constants.REQUEST_EDIT_COURSE : Constants.REQUEST_ADD_COURSE, intent);
            finish();
            // 已保存的，就清空缓存
            UiPreference.putString("Course" + Course.VIDEO, null);
        }
    }

    @Override
    public void onClick(View view) {

        hideKeyboard(mContext);

        switch (view.getId()) {

            case R.id.priceTv:
                if (mCourse.hasSold()) {
                    ToastHelper.showToast(ResourceHelper.getString(
                            R.string.edit_course_tip, "课程价格"));
                } else {
                    ViewUtils.forcedShowInputMethod(coursePrice);
                }
                break;
            case R.id.addVideo:
                if (Utils.isEmpty(mCourse.getVideo_url())) {
                    // mPhotoUtil.takeVideo();
                    // 新需求改动-需要直接跳转到素材库
                    mPhotoUtil.takeVideoFromMaterialStore();
                } else {
                    //TODO MyVideoPlayer 播放课程视频
                    MyExoPlayerPlus.playerVideo(this, mCourse.getVideo_url(), "课程视频");
                }
                break;
            case R.id.save:
                addCourse();
                break;
            case R.id.airTime:
                mTimePicker.show();
                break;
            case R.id.deleteCover:
                setDefaultCover();
                break;
            case R.id.deleteVideo:
                setDefaultVideo();
                break;
            case R.id.blurLayout:
                takePhoto(view, true);
                break;
            case R.id.formulaImage:
                takePhoto(view, false);
                break;
            case R.id.deleteCourse:
                Intent intent = new Intent();
                // 删除课程
                mCourse = null;
                intent.putExtra("data", mCourse);
                intent.putExtra("editPosition", editPosition);
                setResult(Constants.REQUEST_ADD_COURSE, intent);
                finish();
                break;
            case R.id.addView:
            case R.id.addGoods:
                SearchRecommendGoodsActivity.startActivity(this, mCourse.getRecommendGoods());
                break;
            case R.id.dialog_commit:
                // Logger.e("保存课程视频：" + GsonTools.toJson(mCourse));
                UiPreference.putString("Course" + Course.VIDEO, GsonTools.toJson(mCourse));
                ToastHelper.showToast("已保存课程");
                // 防止窗体泄漏，finish 前关闭窗体
                if (saveDialog != null && saveDialog.isShowing()) {
                    saveDialog.dismiss();
                }
                finish();
                break;
            case R.id.dialog_cancel:
                UiPreference.putString("Course" + Course.VIDEO, null);
                UploadManager.getInstance().cancelResumableTask();
                // 防止窗体泄漏，finish 前关闭窗体
                if (saveDialog != null && saveDialog.isShowing()) {
                    saveDialog.dismiss();
                }
                finish();
                break;
            case R.id.back:
                onBackPressed();
                break;
            default:
                break;
        }

    }


    // 删除课堂视频
    private void setDefaultVideo() {

        mCourse.setDuration("0");
        mCourse.setObjectKey(null);
        mCourse.setVideo_url(null);
        mCourse.setLocalVideoUrl(null);
        mCourse.setSnapshot_url(null);
        videoLoading.setVisible(View.GONE);
        videoText.setVisibility(View.VISIBLE);
        deleteVideo.setVisibility(View.GONE);
        courseVideo.setImageResource(R.drawable.transparent);
        // 取消上传
        UploadManager.getInstance().cancelResumableTask();
    }

    // 在OnDecodeResult回调方法里处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Logger.i("AddVideoActivity回调onActivityResult：" +
//                "\nrequestCode：" + requestCode +
//                "\nresultCode：" + resultCode +
//                "\n输出data：" + data);
        mPhotoUtil.onResult(requestCode, resultCode, data, this);
    }

    // 解析视频的回调
    @Override
    public void onDecodeResult(int requestCode, Bitmap bitmap, String filePath, long size, int duration) {
        super.onDecodeResult(requestCode, bitmap, filePath, size, duration);
//        Logger.e("收到图片回调？：" + bitmap + "\npath：" + filePath);

        switch (requestCode) {
            case PhotoUtil.ALBUM_VIDEO: // 从相册拿到视频
            case PhotoUtil.RECORD_VIDEO: // 拿到录制的新视频
                handleVideoFromAlbum(bitmap, filePath, size, duration);
                break;
            default:
                break;
        }
    }

    /**
     * 从素材库选择回调
     *
     * @param materialBean
     */
    @Override
    public void onChooseMaterialResult(VideoMaterialBean materialBean) {
        super.onChooseMaterialResult(materialBean);
        ImageUtil.displayImage(courseVideo, materialBean.getVideoCover());
        videoText.setVisibility(View.GONE);
        videoLoading.setVisible(View.GONE);
        deleteVideo.setVisibility(View.VISIBLE);
    }

    // 从相册拿到视频
    private void handleVideoFromAlbum(final Bitmap bitmap, final String filePath, final long size, final int duration) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

//                if (duration > EXTRA_DURATION_LIMIT) {
//                    showToast(R.string.video_course_over_time);
//                    return;
//                } else if (size > EXTRA_SIZE_LIMIT) {
//                    showToast(R.string.video_course_over_size);
//                    return;
//                }

                // 设置预告视频封面
                mCourse.setLocalVideoUrl(filePath);
                mCourse.setDuration(String.valueOf(duration));
                if (bitmap != null) {
                    courseVideo.setImageBitmap(bitmap);
                }
                videoText.setVisibility(View.GONE);
                videoLoading.setVisible(View.VISIBLE);
                deleteVideo.setVisibility(View.VISIBLE);
                doUploadVideo(filePath, null);
            }
        });

    }


    // 上传视频
    private void doUploadVideo(final String filePath, String objectKey) {
        isUpLoading = true;
        // 执行上传代码
        objectKey = UploadManager.getInstance().resumedUploadFile(BuildConfig.UPLOAD_BUCKET_VIDEO, UploadManager.TAG_NAME, objectKey, filePath, this);
        // 保存当前的ObjectKey
        saveObjectKey(objectKey);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        updateCourse();
        UiPreference.putString("Course" + Course.VIDEO, GsonTools.toJson(mCourse));
    }

    @Override
    public void onBackPressed() {

        if (Jzvd.backPress()) {
            return;
        } else if (!isEdit && (showSaveDialog() || Utils.isNotEmpty(mCourse.getLocalVideoUrl()))) {
            //TODO 窗体泄漏
            saveDialog.show();
            return;
        } else {
            //更新缓存
            UiPreference.putString("Course" + Course.VIDEO, GsonTools.toJson(mCourse));
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isEdit) {
            UploadManager.getInstance().cancelResumableTask();
        }
    }

    @Subscribe(sticky = true)
    @Override
    public void onEvent(final PostEvent event) {
        super.onEvent(event);

        if (!mContext.isDestroyed()) {
            if (event.tag.equals(SubcriberTag.UPLOAD_PROGRSS)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 设置预告视频封面
                        if (videoLoading.getVisibility() == View.GONE && Utils.isNotEmpty(mCourse.getLocalVideoUrl())) {
                            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(mCourse.getLocalVideoUrl(),
                                    MediaStore.Video.Thumbnails.MICRO_KIND);
                            courseVideo.setImageBitmap(bitmap);
                            videoText.setVisibility(View.GONE);
                            videoLoading.setVisible(View.VISIBLE);
                            deleteVideo.setVisibility(View.VISIBLE);
                        } else if (videoLoading != null) {
                            float progress = (float) event.event;
                            videoLoading.setProgress(progress);
                        }
                    }
                });


            } else if (event.tag.equals(SubcriberTag.UPLOAD_FAILURE)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast((String) event.event);
                        setDefaultVideo();
                    }
                });

            } else if (event.tag.equals(SubcriberTag.UPLOAD_SUCCESS)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("上传完成");
                        videoLoading.setVisible(View.GONE);
                        videoLoading.setProgress(0); // 重置进度
                    }
                });

            }

        }

        if (event.tag.equals(SubcriberTag.NON_NETWORK_TO_HAS_NET)) {
            // 从没网到有网
            if (Utils.isNotEmpty(mCourse.getObjectKey())) {
                UploadManager.getInstance()
                        .resumedUploadFile(BuildConfig.UPLOAD_BUCKET_VIDEO, UploadManager.TAG_NAME, mCourse.getObjectKey(), mCourse.getLocalVideoUrl(), this);
            }

        }

    }

    @Override
    public void onProgress(long currentSize, long totalSize) {
        Log.log("debug=" + ((float) currentSize / (float) totalSize));
        EventBusManager.postSticky(((float) currentSize / (float) totalSize), SubcriberTag.UPLOAD_PROGRSS);
    }

    @Override
    public void onSuccess(String url) {
        isUpLoading = false;
        // 如果是上传成功，就删除ObjectKey
        saveObjectKey(null);
        mCourse.setVideo_url(url);
        UiPreference.putString("Course" + Course.VIDEO, GsonTools.toJson(mCourse));
        EventBusManager.postSticky(url, SubcriberTag.UPLOAD_SUCCESS);
    }

    @Override
    public void onFailure(String code, final String errorMsg) {

        isUpLoading = false;

        if (code.equals("-1")) {
            // 取消上传，也不用保存
            saveObjectKey(null);
            // 如果是已取消上传，就不用保存
            EventBusManager.postSticky(errorMsg, SubcriberTag.UPLOAD_FAILURE);
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showToast(errorMsg);
                }
            });
        }

        log("onFailure onFailure code=" + code + " msg=" + errorMsg + ",");
    }

    // 保存objectKey
    private void saveObjectKey(String objectKey) {
        mCourse.setObjectKey(objectKey);
        UiPreference.putString("Course" + Course.VIDEO, GsonTools.toJson(mCourse));
    }

}