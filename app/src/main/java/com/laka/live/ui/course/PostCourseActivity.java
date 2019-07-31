package com.laka.live.ui.course;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.laka.live.BuildConfig;
import com.laka.live.R;
import com.laka.live.analytics.AnalyticsReport;
import com.laka.live.bean.Course;
import com.laka.live.bean.CourseDetail;
import com.laka.live.bean.Topic;
import com.laka.live.constants.Constants;
import com.laka.live.dialog.ChooseDialog;
import com.laka.live.dialog.ConfirmDialog;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.msg.CourseDetailMsg;
import com.laka.live.msg.DataMsg;
import com.laka.live.msg.ReleaseTrailerMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.network.upload.IUploadListener;
import com.laka.live.network.upload.UploadManager;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsBaseBean;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.adapter.PostCourseAdapter;
import com.laka.live.ui.widget.DividerItemDecoration;
import com.laka.live.ui.widget.SelectorButton;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.NumberUtils;
import com.laka.live.util.PhotoUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.UiPreference;
import com.laka.live.util.Utils;
import com.laka.live.util.ViewUtils;
import com.laka.live.video.model.bean.VideoMaterialBean;
import com.laka.live.player.MyExoPlayerPlus;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.Jzvd;
import framework.ioc.annotation.InjectExtra;
import framework.ioc.annotation.InjectView;
import framework.utils.GsonTools;
import framework.utils.PermissionUtils;


/**
 * Created by Lyf on 2017/5/27.
 * 发布课程页
 */
public class PostCourseActivity extends BaseActivity implements PhotoUtil.OnDecodeResult, PhotoUtil.OnMaterialStoreResult,
        GsonHttpConnection.OnResultListener<ReleaseTrailerMsg>, View.OnClickListener {


    @InjectView(id = R.id.recyclerView)
    public RecyclerView mRecyclerView;
    @InjectView(id = R.id.commitCheck)
    public SelectorButton mCommitCheck;
    @InjectView(id = R.id.originalPrice)
    public TextView mOriginalPrice;
    @InjectView(id = R.id.coursePrice)
    public TextView mCoursePrice;
    @InjectView(id = R.id.back)
    public ImageView mBack;
    @InjectView(id = R.id.save)
    public TextView mSave;
    @InjectView(id = R.id.title)
    public TextView mTitle;

    private List<Course> mData = new ArrayList<>();
    private PhotoUtil mPhotoUtil;
    private ChooseDialog mSaveDialog;
    private PostCourseAdapter mAdapter;
    private OptionsPickerView<String> mDiscountPicker; // 折扣选择器

    public final int EXTRA_SIZE_LIMIT = 10; // 预告视频的大小限制(M)
    public final int EXTRA_DURATION_LIMIT = 60; // 预告视频的时长限制(秒)
    public final int MAX_TOPICS = 3; // 话题的最大个数

    @InjectExtra(name = "code", def = "1")
    public Integer courseType;
    // 预告ID（用于编辑课堂）
    @InjectExtra(name = "trailer_id")
    public String trailer_id;

    private String noticeInfo = ""; // 确认发布的提示文本
    // 是否有权限
    private boolean hasPermission = false;

    /**
     * description:是否为素材库选取，素材库选取的素材不拼接videoUrl上传
     **/
    private boolean isMaterialVideo = false;

    public static void startActivity(Context context, int course_type, String trailer_id) {
        Intent intent = new Intent(context, PostCourseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("code", course_type);
        bundle.putString("trailer_id", trailer_id);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initView();
        if (!hasPermission) {
            checkPermissions();
        }
    }

    private void initView() {

        // 获取OSSKEY
        DataProvider.getStsToken(this);

        if (courseType == Course.LIVE) {
            mTitle.setText("发布直播");
            mCommitCheck.setNewText("发布直播");
        } else {
            mTitle.setText("发布视频");
            mCommitCheck.setNewText("发布视频");
        }
        initRecyclerView();
        initDiscountPicker();
        mPhotoUtil = new PhotoUtil(this);
        mPhotoUtil.setUseMaterialStore(true);
        mPhotoUtil.setOnMaterialStoreResult(this);
        mSaveDialog = new ChooseDialog(this, this);
        mSaveDialog.setTitle("保存数据")
                .setPrompt("您填写的数据尚未保存，是否保存？")
                .setLeftBtnText("不保存")
                .setRightBtnText("保存")
                .setShowCancelAnim(false);


        // 初始化数据
        initData();
        // 获取提示
        getNoticeInfo();
    }


    private void initData() {

        if (Utils.isNotEmpty(trailer_id)) {
            getData();
            mSave.setVisibility(View.GONE);
            mCommitCheck.setNewText("保存课堂");
        } else {
            String dataCache = UiPreference.getString("Courses" + courseType, null);
            if (dataCache == null) {
                Course course = new Course();
                course.setTopics(new ArrayList<Topic>());
                course.setItemType(Constants.TYPE_HEAD);
                mData.add(course);
                course = new Course();
                course.setItemType(Constants.TYPE_ADD);
                mData.add(course);
            } else {
                mData.addAll(GsonTools.getJsonList(dataCache, Course.class));
            }

//            Logger.e("读取本地缓存：" + dataCache);
            mAdapter.notifyDataSetChanged();
            refreshPriceView();
        }
    }

    private void initRecyclerView() {

        mAdapter = new PostCourseAdapter(this, courseType, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 避免图片闪烁
        ((DefaultItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mAdapter.setmDatas(mData);

    }

    // 初始化折扣选择器
    private void initDiscountPicker() {

        // 初始化折扣选择器
        OptionsPickerView.Builder builder = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int position, int options2, int options3, View v) {
                mData.get(mData.size() - 1).setDiscount((float) (10 - 0.5 * (position)));
                mAdapter.notifyItemChanged(mData.size() - 1);
                refreshPriceView();
            }
        });
        builder.setDividerColor(Color.DKGRAY)
                .setSubmitColor(Color.BLACK)
                .setCancelColor(Color.BLACK)
                .setContentTextSize(20);

        mDiscountPicker = new OptionsPickerView<>(builder);

        // 初始化折扣列表
        List<String> discountData = new ArrayList<>();

        for (int position = 0; position <= 20; ++position) {

            double discount = (10 - 0.5 * (position));

            if (position == 0) {
                discountData.add("无折扣");
            } else if (position == 20) {
                discountData.add("免费");
            } else {
                discountData.add(discount + "折");
            }

        }
        // 给折扣列表添加数据
        mDiscountPicker.setPicker(discountData);
    }

    // 加载数据
    private void getData() {

        showNewDialog("正在加载数据...");
        DataProvider.getTrailerDetail(this, trailer_id, new GsonHttpConnection.OnResultListener<CourseDetailMsg>() {
            @Override
            public void onSuccess(CourseDetailMsg courseDetailMsg) {

                CourseDetail courseDetail = courseDetailMsg.data;
                Course header = new Course();
                header.setCourseTrailer(courseDetail.course_trailer);
                header.setItemType(Constants.TYPE_HEAD);
                mData.add(header);

                for (Course course : courseDetailMsg.data.courses) {
                    course.setItemType(Constants.TYPE_ITEM);
                    mData.add(course);
                }
                Course footer = new Course();
                footer.setItemType(Constants.TYPE_EDIT);
                footer.setDiscountType(Integer.parseInt(courseDetail.course_trailer.discount_type));
                footer.setDiscount(courseDetail.course_trailer.discount);
                mData.add(footer);
                if (courseDetail.course_trailer.isOpenLimitDiscount()) {
                    mAdapter.setAutoScroll(false);
                    footer.setDiscount_time(courseDetail.course_trailer.getDiscountDeadline());
                }
                mAdapter.notifyDataSetChanged();
                refreshPriceView();

                dismissDialog();
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                showToast(errorMsg);
                dismissDialog();
            }
        });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.commitCheck:
                // 检查发布条件
                if (mAdapter.getCourseCount() == 0) {
                    ToastHelper.showToast("请添加课程");
                    return;
                }
                ConfirmDialog confirmDialog = new ConfirmDialog(this, this);
                confirmDialog.setContent(noticeInfo);
                confirmDialog.show();
                break;
            case R.id.commit:
                // 确认发布
                commit();
                break;
            case R.id.back:
                onBackPressed();
                break;
            case R.id.save:
                UiPreference.putString("Courses" + courseType, GsonTools.toJson(mData));
                ToastHelper.showToast("已保存草稿");
                break;
            case R.id.dialog_commit:
//                Logger.e("发布课程，保存草稿：" + GsonTools.toJson(mData));
                UiPreference.putString("Courses" + courseType, GsonTools.toJson(mData));
                ToastHelper.showToast("已保存草稿");
                // 防止窗体泄漏，finish 前关闭窗体
                if (mSaveDialog != null && mSaveDialog.isShowing()) {
                    mSaveDialog.dismiss();
                }
                finish();
                break;
            case R.id.dialog_cancel:
                UiPreference.putString("Courses" + courseType, null);
                // 防止窗体泄漏，finish 前关闭窗体
                if (mSaveDialog != null && mSaveDialog.isShowing()) {
                    mSaveDialog.dismiss();
                }
                finish();
                break;
            default:
                break;
        }

    }

    @Override
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostEvent event) {

        Course course = mData.get(0);

        switch (event.getTag()) {

            case SubcriberTag.ADD_TOPIC:
                // 获取话题
                List<String> selected = (List<String>) event.event;
                // 清空之前的话题
                course.getTopics().clear();
                // 加载新的话题
                for (String topic : selected) {
                    course.getTopics().add(new Topic(topic));
                }
                // 刷新话题
                mAdapter.notifyItemChanged(0);
                break;
            case SubcriberTag.CLICK_TRAILER:
                // 点击预告
                if ((int) event.event == Constants.ADD_TRAILER) {
                    mPhotoUtil.takeVideo();
                } else if ((int) event.event == Constants.PLAY_TRAILER) {

                    //TODO MyVideoPlayer 播放预告视频
                    MyExoPlayerPlus.playerVideo(this, course.getVideo_url(),"预告视频");

                }
                break;
            case SubcriberTag.CLICK_DISCOUNT:
                // 点击优惠
                if (mAdapter.hasNotSold()) {
                    mDiscountPicker.show();
                } else {
                    ToastHelper.showToast(ResourceHelper.getString(
                            R.string.edit_course_tip, "优惠折扣"));
                }
                break;
            default:
                break;

        }

    }

    // 设置原总价和优惠价
    private void refreshPriceView() {

        // 原总价
        float totalPrice = 0;

        // 获取总价
        for (Course course : mData) {
            totalPrice += course.getPrice();
        }

        // 获取折扣
        float discount = mData.get(mData.size() - 1).discount;

        if (discount == 10) {
            mOriginalPrice.setVisibility(View.GONE);
            mCoursePrice.setText("总计 " + NumberUtils.splitDoubleStr(totalPrice) + " 味豆");
        } else {
            mOriginalPrice.setVisibility(View.VISIBLE);
            // 设置原总价
            mOriginalPrice.setText(getString(R.string.total_price, NumberUtils.splitDoubleStr(totalPrice)));
            // 计算优惠价
            float discountPrice = 0.0f;
            // 计算规则,先将带小数的价格乘10，再去乘折扣，计算完再加在一起，作为最后的优惠价
            for (int i = 1; i < mData.size() - 1; ++i) {
                discountPrice += Float.valueOf(NumberUtils.splitAndUIntStr((mData.get(i).getPrice() * 10 * (discount / 10.0))));
            }
            // 这里第一步是，向上取整。第二步，转成Float是为了将3显示成3.0。因为有可能是3.0被向上取整成3。
            discountPrice = Float.valueOf(NumberUtils.splitAndUpDoubleStr(discountPrice / 10.0));
            // 设置优惠价
            mCoursePrice.setText(getString(R.string.discount_price, String.valueOf(discountPrice)));
            // 设置删除线
            ViewUtils.setPartTextDeleteLine(mOriginalPrice, 0, mOriginalPrice.length());
        }

        ViewUtils.setPartTextColor(mCoursePrice, R.color.colorFF950B, 3, mCoursePrice.length());

    }

    // 在OnDecodeResult回调方法里处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Constants.REQUEST_ADD_COURSE == requestCode) {

            if (data != null) {
                Course course = (Course) data.getSerializableExtra("data");
                mData.add(mAdapter.getItemCount() - 1, course);
                refreshPriceView();
                mAdapter.notifyDataSetChanged();
            }

        } else if (Constants.REQUEST_EDIT_COURSE == requestCode) {

            if (data == null) {
                return;
            }
            int editPosition = data.getIntExtra("editPosition", -1);
            Course course = (Course) data.getSerializableExtra("data");

            // 如果编辑返回来的course为空,则代表是删除该课程
            if (course == null) {
                mData.remove(editPosition);
            } else {
                mData.set(editPosition, course);
            }
            refreshPriceView();
            mAdapter.notifyDataSetChanged();
        } else {
            mPhotoUtil.onResult(requestCode, resultCode, data, this);
        }

    }

    // 解析视频的回调
    @Override
    public void onDecodeResult(int requestCode, Bitmap bitmap, String filePath, long size, int duration) {

        switch (requestCode) {
            case PhotoUtil.ALBUM_VIDEO:
            case PhotoUtil.RECORD_VIDEO:
                handleVideoFromAlbum(bitmap, filePath, size, duration);
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
        ImageUtil.loadImage(materialBean.getVideoCover(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                final ImageView blurImage = mRecyclerView.findViewHolderForLayoutPosition(0).
                        itemView.findViewById(R.id.blurImage);
                blurImage.setImageBitmap(bitmap);
                mAdapter.notifyItemChanged(0);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        mData.get(0).setDuration(materialBean.getVideoDuration());
        mData.get(0).setCover_url(materialBean.getVideoCover());
        mData.get(0).setVideo_url(materialBean.getVideoUrl());
        mData.get(0).setLocalVideoUrl("");
        mData.get(0).setLocalCoverUrl("");
        mAdapter.notifyItemChanged(0);
    }

    // 从相册拿到视频
    private void handleVideoFromAlbum(final Bitmap bitmap, final String filePath, long size, int duration) {

//        if (duration > EXTRA_DURATION_LIMIT) {
//            showToast(R.string.video_over_time);
//            return;
//        } else if (size > EXTRA_SIZE_LIMIT) {
//            showToast(R.string.video_over_size);
//            return;
//        }

        final ImageView blurImage = (ImageView) mRecyclerView.findViewHolderForLayoutPosition(0).itemView.findViewById(R.id.blurImage);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 设置预告视频封面
                if (bitmap != null)
                    blurImage.setImageBitmap(bitmap);
                mAdapter.notifyItemChanged(0);
            }
        });

        doUploadVideo(filePath, blurImage);
    }

    // 上传视频
    private void doUploadVideo(final String filePath, final ImageView blurImage) {

        // 执行上传代码
        UploadManager.getInstance().uploadFile(BuildConfig.UPLOAD_BUCKET_VIDEO,
                UploadManager.TAG_NAME, filePath, new IUploadListener() {
                    @Override
                    public void onSuccess(final String url) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mData.get(0).setVideo_url(url);
                                mData.get(0).setLocalVideoUrl(filePath);
                                mAdapter.notifyItemChanged(0);
                                showToast("上传完成");
                            }
                        });
                    }

                    @Override
                    public void onFailure(String code, final String errorMsg) {
                        log("onFailure onFailure code=" + code + " msg=" + errorMsg);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mData.get(0).setProgress(0);
                                mData.get(0).setVideo_url(null);
                                mData.get(0).setLocalVideoUrl(null);
                                blurImage.setTag(R.id.is_do_blur, null);
                                blurImage.setImageResource(R.mipmap.icon_living_bg);
                                mAdapter.notifyItemChanged(0);
                                showToast(errorMsg);
                            }
                        });
                    }

                    @Override
                    public void onProgress(long currentSize, long totalSize) {
                        float progress = ((float) currentSize / (float) totalSize);
                        log("uploadFile onProgress progress=" + progress);
                        mData.get(0).setProgress(progress);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyItemChanged(0);
                            }
                        });
                    }
                });

    }

    @Override
    public void onBackPressed() {

        if (Jzvd.backPress()) {
            return;
        } else if (Utils.isEmpty(trailer_id) && showSaveDialog()) {
            //TODO 窗体泄漏
            mSaveDialog.show();
            return;
        }

        super.onBackPressed();
    }

    // 显示保存提示框
    private boolean showSaveDialog() {
        if (Utils.isNotEmpty(trailer_id)) {
            return false;
        }
        if (Utils.isNotEmpty(mData.get(0).getVideo_url()) || mAdapter.getCourseCount() > 0
                || Utils.isNotEmpty(mData.get(0).getTopics()) || mData.get(mData.size() - 1).isDiscount()) {
            return true;
        } else {
            // 避免进来时有缓存，然后，把缓存都清空了，再按返回，然后缓存数据并没有清除的现象。
            UiPreference.putString("Courses" + courseType, null);
            return false;
        }

    }

    // 确认发布课堂
    private void commit() {

        HashMap<String, String> params = new HashMap<>();

        // 两接口通用
        String video_url = StringUtils.deleteAll(mData.get(0).getVideo_url(), "http://video.zwlive.lakatv.com/");

        //这里需要判断，假若是素材库传入的数据，不需要截取
        if (!Utils.isEmpty(mData)) {
            if (Utils.isHttpUrl(mData.get(0).getVideo_url())) {
                video_url = mData.get(0).getVideo_url();
            }
        }

        if (Utils.isNotEmpty(mData.get(0).getTopics())) {

            List<String> topics = new ArrayList<>();
            for (Topic topic : mData.get(0).getTopics()) {
                topics.add(topic.getName());
            }
            params.put("topics", GsonTools.toJson(topics).replace("#", "")); // 话题
        }

        params.put("video_url", video_url); // 视频地址
        params.put("course_type", String.valueOf(courseType)); // 课程类型
        params.put("discount", String.valueOf(mData.get(mData.size() - 1).getDiscount())); // 折扣

        if (mData.get(mData.size() - 1).isOpenLimitDiscount()) {
            params.put("discount_type", "2");// 1=普通折扣，2=限时折扣
            params.put("discount_deadline", String.valueOf(mData.get(mData.size() - 1).getDiscount_time()));
        } else {
            params.put("discount_type", "1");// 1=普通折扣，2=限时折扣
        }

        for (int index = 1; index < mData.size(); ++index) {
            if (Utils.isNotEmpty(mData.get(index).getRecommendGoods())) {
                mData.get(index).rec_goods_ids = GsonTools.toJson(getRecommendIds(mData.get(index).getRecommendGoods()));
                mData.get(index).setRecommendGoods(null);
            }
        }

        if (TextUtils.isEmpty(trailer_id)) {
            params.put("courses", GsonTools.toJson(mData.subList(1, mData.size() - 1))); // 所有课程
            showNewDialog("正在发布...");
            DataProvider.postPreLive(this, params, this);
        } else {
            params.put("trailer_id", trailer_id); // 需要更新的预告id
            params.put("update_courses", GsonTools.toJson(mData.subList(1, mData.size() - 1))); // 原有的课时
            params.put("add_courses", GsonTools.toJson(new ArrayList<Course>())); // 新增的课时
            showNewDialog("正在保存...");
            DataProvider.updatePreLive(this, params, this);
        }

    }

    @Override
    public void onSuccess(ReleaseTrailerMsg data) {

        dismissDialog();

        // 发布成功，要清空缓存
        UiPreference.putString("Courses" + courseType, null);

        int type;
        String trailerId;

        Course course = mData.get(1);
        // 根据不同的状态显示不同的提示语
        if (TextUtils.isEmpty(trailer_id)) {
            type = PostSuccessActivity.POSTLIVESUCCESS;
            course.id = Long.parseLong(data.getCourseId());
            trailerId = data.getTrailerId();
        } else {
            type = PostSuccessActivity.UPDATELIVESUCCESS;
            trailerId = trailer_id;
        }
        AnalyticsReport.onEvent(this, AnalyticsReport.POST_SUCCESS_VIEW, getParams());
        PostSuccessActivity.startActivity(this, type, trailerId, course);
        finish();
    }

    @Override
    public void onFail(int errorCode, String errorMsg, String command) {
        dismissDialog();
        ToastHelper.showToast(errorMsg);
    }

    // 拼接推荐商品的id
    private ArrayList<Integer> getRecommendIds(ArrayList<ShoppingGoodsBaseBean> recommendGoods) {

        ArrayList<Integer> rec_goods_ids = new ArrayList<>();
        for (ShoppingGoodsBaseBean goods : recommendGoods) {
            rec_goods_ids.add(goods.getGoodsId());
        }
        return rec_goods_ids;
    }


    private HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("Class_type", courseType == Course.LIVE ? "22" : "23");
        return params;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (Utils.isEmpty(trailer_id) && (Utils.isNotEmpty(mData.get(0).getVideo_url())
                || mAdapter.getCourseCount() > 0 || Utils.isNotEmpty(mData.get(0).getTopics()))) {
            UiPreference.putString("Courses" + courseType, GsonTools.toJson(mData));
        }
    }

    // 检查权限
    private void checkPermissions() {

        if (PermissionUtils.hasNotGrant(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                || PermissionUtils.hasNotGrant(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                || PermissionUtils.hasNotGrant(this, Manifest.permission.CAMERA)) {
            PermissionUtils.checkPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
        } else {
            hasPermission = true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 没加的权限，在这个工具里面都会提示。
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // 加载数据
    private void getNoticeInfo() {

        DataProvider.getNoticeInfo(this, new GsonHttpConnection.OnResultListener<DataMsg>() {
            @Override
            public void onSuccess(DataMsg dataMsg) {
                noticeInfo = dataMsg.noticeInfo;
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
            }

        });

    }

}
