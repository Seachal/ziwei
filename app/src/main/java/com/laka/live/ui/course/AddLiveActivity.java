package com.laka.live.ui.course;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.laka.live.BuildConfig;
import com.laka.live.R;
import com.laka.live.bean.Course;
import com.laka.live.bean.ImageBean;
import com.laka.live.constants.Constants;
import com.laka.live.dialog.ChooseDialog;
import com.laka.live.help.PostEvent;
import com.laka.live.help.SubcriberTag;
import com.laka.live.network.DataProvider;
import com.laka.live.network.upload.IUploadListener;
import com.laka.live.network.upload.UploadManager;
import com.laka.live.photopreview.PhotoPreviewInfo;
import com.laka.live.photopreview.PhotoPreviewPanel;
import com.laka.live.shopping.bean.newversion.ShoppingGoodsBaseBean;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.ui.adapter.RecommendGoodsAdapter;
import com.laka.live.ui.search.SearchRecommendGoodsActivity;
import com.laka.live.ui.widget.LimitEditText;
import com.laka.live.ui.widget.SpaceItemDecoration;
import com.laka.live.ui.widget.UDImageView;
import com.laka.live.ui.widget.WxCircleLoading;
import com.laka.live.util.Common;
import com.laka.live.util.FastBlur;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.PhotoUtil;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ToastHelper;
import com.laka.live.util.UiPreference;
import com.laka.live.util.Utils;
import com.laka.live.util.ViewUtils;
import com.laka.live.video.model.bean.VideoMaterialBean;
import com.orhanobut.logger.Logger;
import com.sina.weibo.sdk.utils.LogUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import framework.ioc.annotation.InjectExtra;
import framework.ioc.annotation.InjectView;
import framework.utils.GsonTools;

/**
 * Created by Lyf on 2017/3/28.
 * 新增直播课程
 */
public class AddLiveActivity extends BaseActivity implements View.OnClickListener, PhotoUtil.OnDecodeResult, PhotoUtil.OnMaterialStoreResult, TextWatcher {

    @InjectView(id = R.id.title)
    public TextView title;
    @InjectView(id = R.id.save)
    public TextView save;
    @InjectView(id = R.id.blurText, click = "") // click=""代表不需要点击事件
    public TextView blurText;
    @InjectView(id = R.id.airTime)
    public TextView airTime;
    @InjectView(id = R.id.recommendTip)
    public TextView recommendTip;
    @InjectView(id = R.id.airTimeLayout)
    public View airTimeLayout;
    @InjectView(id = R.id.addView)
    public View addView;
    @InjectView(id = R.id.back)
    public ImageView back;
    @InjectView(id = R.id.deleteCourse)
    public TextView deleteCourse;
    @InjectView(id = R.id.rl_course_cover)
    public RelativeLayout mRlCover;
    @InjectView(id = R.id.blurImage)
    public ImageView blurImage;
    @InjectView(id = R.id.blurLayout)
    public ImageView blurLayout;
    @InjectView(id = R.id.deleteCover)
    public ImageView deleteCover;
    @InjectView(id = R.id.coursePrice, click = "")
    public EditText coursePrice;
    @InjectView(id = R.id.courseTitle)
    public LimitEditText courseTitle;
    @InjectView(id = R.id.courseFormula)
    public EditText courseFormula;
    @InjectView(id = R.id.formulaTextCount)
    public TextView formulaTextCount;
    @InjectView(id = R.id.summaryTextCount)
    public TextView summaryTextCount;
    @InjectView(id = R.id.courseSummary)
    public EditText courseSummary;
    @InjectView(id = R.id.summaryPhotoGallery)
    public LinearLayout summaryPhotoGallery;
    @InjectView(id = R.id.formulaPhotoGallery)
    public LinearLayout formulaPhotoGallery;

    @InjectView(id = R.id.circleLoading)
    public WxCircleLoading circleLoading;
    @InjectView(id = R.id.goodsGallery)
    public RecyclerView goodsGallery;

    protected int editPosition = -1; // 编辑的位置,如果是新增的，则值为-1.
    protected Course mCourse; // 课堂的所有数据保存在这个对象里面
    protected PhotoUtil mPhotoUtil;
    protected ChooseDialog saveDialog;
    protected PhotoPreviewPanel mPreviewPanel;
    protected TimePickerView mTimePicker;
    protected RecommendGoodsAdapter recommendGoodsAdapter;

    private static final int MAX_TEXT_LENGTH = 5000; // 文本的长度
    protected boolean isEdit = false; // 是否是编辑模式
    public final int MAX_PHOTO_SIZE = 8; // 课程简介最多几节课
//    public final int EXTRA_SIZE_LIMIT = 3072; // 预告视频的大小限制(M)
//    public final int EXTRA_DURATION_LIMIT = 60 * 60 * 3; // 预告视频的时长限制(秒)

    @InjectExtra(name = "type", def = "1")
    public Integer courseType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        DataProvider.getStsToken(this);
        initControl();
    }

    // 初始化控件
    protected void initControl() {

        initAirTimePicker();
        saveDialog = new ChooseDialog(this, this);
        saveDialog.setTitle("保存数据")
                .setPrompt("您填写的数据尚未保存，是否保存？")
                .setLeftBtnText("不保存")
                .setRightBtnText("保存")
                .setShowCancelAnim(false);
        mPhotoUtil = new PhotoUtil(this);
        mPhotoUtil.setOnMaterialStoreResult(this);
        coursePrice.addTextChangedListener(this);
        courseFormula.addTextChangedListener(this);
        courseSummary.addTextChangedListener(this);

        if (courseType == Course.LIVE) {
            initData();
        }

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
            String cache = UiPreference.getString("Course" + Course.LIVE, null);
            mCourse = GsonTools.fromJson(cache, Course.class);
            if (mCourse == null) {
                mCourse = new Course();
                mCourse.setType(Course.LIVE);
                mCourse.setItemType(Constants.TYPE_ITEM);
            }
        }

        initView();
    }

    // 初始化时间选择器
    private void initAirTimePicker() {

        //时间选择器
        mTimePicker = new TimePickerView.Builder(mContext,
                new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View view) {
                        // 设置开播时间
                        mCourse.setStart_time(date.getTime() / 1000);
                        airTime.setText(mCourse.getFormatStartTime("yyyy-MM-dd HH:mm"));

                    }
                }).setType(TimePickerView.Type.YEAR_MONTH_DAY_HOUR_MIN)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(20)
                .setSubmitColor(Color.BLACK)
                .setCancelColor(Color.BLACK)
                .setLabel("", "", "", "", "", "") // 隐藏年月日时分的字样
                .build();
    }

    @Override
    public void onClick(View view) {

        if (!(view instanceof EditText || view instanceof LimitEditText)) {
            hideKeyboard(mContext);
        }

        switch (view.getId()) {

            case R.id.priceTv:
                if (mCourse.hasSold()) {
                    ToastHelper.showToast(ResourceHelper.getString(
                            R.string.edit_course_tip, "课程价格"));
                } else {
                    ViewUtils.forcedShowInputMethod(coursePrice);
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
            case R.id.dialog_commit:
//                Logger.e("输出保存的Json：" + GsonTools.toJson(mCourse));
                UiPreference.putString("Course" + Course.LIVE, GsonTools.toJson(mCourse));
                ToastHelper.showToast("已保存课程");
                // 防止窗体泄漏，finish 前关闭窗体
                if (saveDialog != null && saveDialog.isShowing()) {
                    saveDialog.dismiss();
                }
                finish();
                break;
            case R.id.dialog_cancel:
                UiPreference.putString("Course" + Course.LIVE, null);
                // 防止窗体泄漏，finish 前关闭窗体
                if (saveDialog != null && saveDialog.isShowing()) {
                    saveDialog.dismiss();
                }
                finish();
                break;
            case R.id.addView:
            case R.id.addGoods:
                SearchRecommendGoodsActivity.startActivity(this, mCourse.getRecommendGoods());
                break;
            case R.id.back:
                onBackPressed();
                break;
            default:
                break;
        }

    }

    @Override
    public void onEvent(PostEvent event) {
        super.onEvent(event);

        if (event.tag.equals(SubcriberTag.ADD_GOODS)) {
            recommendGoodsAdapter.getmDatas().add((ShoppingGoodsBaseBean) event.event);
            recommendGoodsAdapter.notifyDataSetChanged();
            recommendTip.setText(Html.fromHtml(ResourceHelper.getString(R.string.recommends_tip, recommendGoodsAdapter.getItemCount())));
            if (addView.getVisibility() == View.VISIBLE) {
                addView.setVisibility(View.GONE);
                findById(R.id.goodsLayout).setVisibility(View.VISIBLE);
            }
        } else if (event.tag.equals(SubcriberTag.REMOVE_GOODS)) {
            ShoppingGoodsBaseBean goods = (ShoppingGoodsBaseBean) event.event;
            recommendGoodsAdapter.remove(goods);
            recommendGoodsAdapter.notifyDataSetChanged();
            recommendTip.setText(Html.fromHtml(ResourceHelper.getString(R.string.recommends_tip, recommendGoodsAdapter.getItemCount())));
            if (recommendGoodsAdapter.isEmpty()) {
                addView.setVisibility(View.VISIBLE);
                findById(R.id.goodsLayout).setVisibility(View.GONE);
            }
        }

    }

    // 删除课堂封面
    protected void setDefaultCover() {

        mCourse.setCover_url(null);
        circleLoading.setVisible(View.GONE);
        blurText.setVisibility(View.VISIBLE);
        blurLayout.setVisibility(View.VISIBLE);
        deleteCover.setVisibility(View.GONE);
        blurImage.setImageResource(R.mipmap.icon_living_bg);
        FastBlur.doPartBlur(mContext, blurImage, blurLayout);
        UploadManager.getInstance().cancelUpload();
    }

    // 添加课堂封面
    protected void takePhoto(View view, boolean isCut) {
        mPhotoUtil.setView(view);
        mPhotoUtil.setCut(isCut);

        //新增-课程封面修改为1:1显示
        if (isCut) {
            mPhotoUtil.setCutRatio(1);
        }
        mPhotoUtil.takePhoto();
    }

    // 检查发布条件
    protected boolean checkCourse() {

        // 更新最新填写的数据
        updateCourse();

        if (Utils.isEmpty(mCourse.getCover_url())) {
            ToastHelper.showToast("请添加课程封面");
            return false;
        } else if (Utils.isEmpty(mCourse.getTitle())) {
            ToastHelper.showToast("请添加课程标题");
            return false;
        } else if (mCourse.status < Course.LIVING && airTimeLayout.getVisibility() == View.VISIBLE && mCourse.start_time == 0) {
            ToastHelper.showToast("请选择开播时间");
            return false;
        } else if (mCourse.status < Course.LIVING && mCourse.id == 0 && airTimeLayout.getVisibility() == View.VISIBLE
                && (mCourse.start_time < System.currentTimeMillis() / 1000)) {
            showToast("开播时间不能早于当前时间");
            return false;
        }

        return true;
    }

    // 获取所有已填写的数据
    protected void updateCourse() {

        mCourse.setTitle(courseTitle.getText());
        mCourse.setSummary(courseSummary.getEditableText().toString());
        mCourse.setFormula(courseFormula.getEditableText().toString());
        mCourse.setPrice(coursePrice.getText().toString().trim());
    }

    // 添加课程
    protected void addCourse() {

        try {
            if (checkCourse()) {
                Intent intent = new Intent();
                intent.putExtra("data", mCourse);
                intent.putExtra("editPosition", editPosition);
                int requestCode = isEdit ? Constants.REQUEST_EDIT_COURSE : Constants.REQUEST_ADD_COURSE;
                setResult(requestCode, intent);
                // 已保存的，就清空缓存
                UiPreference.putString("Course" + Course.LIVE, null);
                finish();
            }
        } catch (Exception e) {
            Log.log("addCourse=" + e.toString());
        }

    }


    /**
     * 添加图片
     *
     * @param gallery 课程简介列表或课程配方列表
     * @param bean    图片对应的Bean
     */
    public void addNewImage(final LinearLayout gallery, ImageBean bean) {

        if (gallery.getChildCount() < MAX_PHOTO_SIZE) {

            final UDImageView imageView = new UDImageView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) mContext.getResources().getDimension(R.dimen.space_75),
                    (int) mContext.getResources().getDimension(R.dimen.space_80));
            int rightMargin = (int) mContext.getResources().getDimension(R.dimen.space_8);
            params.setMargins(0, 0, rightMargin, 0);
            imageView.setLayoutParams(params);
            imageView.setClickable(true);
            imageView.setAnimationEnable(true);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getTag() == null) {
                        v.setId(gallery.getId());
                        takePhoto(v, false);
                    } else {

                        ArrayList<String> imageUrls = getImageUrls(gallery.getId());

                        int position = 0;
                        ImageBean tag = (ImageBean) imageView.getTag();

                        for (String url : imageUrls) {
                            if (url.equals(tag.getUrl())) {
                                break;
                            } else {
                                ++position;
                            }
                        }

                        if (imageUrls.size() > position) {
                            PhotoPreviewInfo info = new PhotoPreviewInfo();
                            info.photoList = imageUrls;
                            info.position = position;
                            mPreviewPanel = new PhotoPreviewPanel(mContext);
                            mPreviewPanel.setupPreviewPanel(info);
                            mPreviewPanel.showPanel();
                        }
                    }
                }
            });
            imageView.setOnDeleteListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (imageView.isUploading()) {
                        ToastHelper.showToast("上传已取消");
                        UploadManager.getInstance().cancelUpload();
                    } else {
                        // 删除ImageBean
                        int position = 0;
                        ImageBean tag = (ImageBean) imageView.getTag();

                        List<ImageBean> imageBeanList;

                        if (gallery.getId() == R.id.summaryPhotoGallery) {
                            imageBeanList = mCourse.getSummaryImages();
                        } else {
                            imageBeanList = mCourse.getFormulaImages();
                        }

                        for (ImageBean bean : imageBeanList) {
                            if (bean.getUrl().equals(tag.getUrl())) {
                                break;
                            } else {
                                ++position;
                            }
                        }
                        if (imageBeanList.size() > position) {
                            imageBeanList.remove(position);
                        }
                    }

                    imageView.setDefault();

                    if (gallery.getChildCount() > 1) {
                        gallery.removeView(imageView);
                    }

                    // 最后一张如果不是添加的占位图，就需要再添加一张新的出来
                    if (gallery.getChildAt(gallery.getChildCount() - 1)
                            .getTag() != null) {

                        if (gallery.getId() == R.id.summaryPhotoGallery) {
                            addNewSummaryImage();
                        } else if (gallery.getId() == R.id.formulaPhotoGallery) {
                            addNewNewFormulaImage();
                        }

                    }

                }
            });

            // 恢复数据
            if (bean != null) {
                imageView.recovery(bean);
            }

            final HorizontalScrollView horizontalScrollView = (HorizontalScrollView) gallery.getParent();
            gallery.addView(imageView);
            //添加完图片就滚到右边
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    horizontalScrollView.fullScroll(View.FOCUS_RIGHT);
                }
            });

        }
    }

    // 恢复数据
    protected void initView() {

        //2018.08.07---修改封面为 1:1显示
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRlCover.getLayoutParams();
        params.height = Utils.getScreenWidth(this);
        mRlCover.setLayoutParams(params);

        // 恢复课程封面
        if (Utils.isNotEmpty(mCourse.getCover_url())) {
            blurText.setVisibility(View.GONE);
            blurLayout.setVisibility(View.GONE);
            circleLoading.setVisible(View.GONE);
            deleteCover.setVisibility(View.VISIBLE);

            if (TextUtils.isEmpty(mCourse.getLocalCoverUrl())) {
                String coverUrl = mCourse.getCover_url();
                if (!coverUrl.startsWith("http")) {

                }
                ImageUtil.displayImage(blurImage, coverUrl, R.drawable.blank_icon_banner);
            } else {
                blurImage.setImageURI(Uri.parse(mCourse.getLocalCoverUrl()));
            }
        } else {
            FastBlur.doPartBlur(mContext, blurImage, blurLayout);
        }

        // 恢复课程配方
        if (Utils.isNotEmpty(mCourse.getFormulaImages())) {
            for (ImageBean bean : mCourse.getFormulaImages()) {
                addNewFormulaImage(bean);
            }
        }

        // 恢复课程简介的图片
        if (Utils.isNotEmpty(mCourse.getSummaryImages())) {
            for (ImageBean bean : mCourse.getSummaryImages()) {
                addNewSummaryImage(bean);
            }
        }

        // 新增一个占位
        addNewSummaryImage();
        addNewNewFormulaImage();

        // 恢复价格
        coursePrice.setText(mCourse.getPrice() == 0 ? "" : String.valueOf(mCourse.getPrice()));

        // 已购买的不能修改价格
        if (mCourse.hasSold()) {
            coursePrice.setInputType(InputType.TYPE_NULL);
            coursePrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastHelper.showToast(ResourceHelper.getString(
                            R.string.edit_course_tip, "课程价格"));
                }
            });
        }

        // 恢复时间
        if (mCourse.start_time != 0) {
            airTime.setText(mCourse.getFormatStartTime("yyyy-MM-dd HH:mm"));
        }

        // 恢复课程标题
        if (Utils.isNotEmpty(mCourse.getTitle())) {
            courseTitle.setText(mCourse.getTitle());
        }

        // 恢复课程简介
        if (Utils.isNotEmpty(mCourse.getSummary())) {
            courseSummary.setText(mCourse.getSummary());
        }

        // 恢复课程配方
        if (Utils.isNotEmpty(mCourse.getFormula())) {
            courseFormula.setText(mCourse.getFormula());
        }

        // 恢复推荐商品
        recommendGoodsAdapter = new RecommendGoodsAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        goodsGallery.setLayoutManager(linearLayoutManager);
        goodsGallery.addItemDecoration(new SpaceItemDecoration(ResourceHelper.getDimen(R.dimen.space_6), recommendGoodsAdapter));

        recommendGoodsAdapter.setmDatas(mCourse.getRecommendGoods());
        goodsGallery.setAdapter(recommendGoodsAdapter);
        recommendTip.setText(Html.fromHtml(ResourceHelper.getString(R.string.recommends_tip, recommendGoodsAdapter.getItemCount())));

        if (recommendGoodsAdapter.isEmpty()) {
            addView.setVisibility(View.VISIBLE);
            findById(R.id.goodsLayout).setVisibility(View.GONE);
        } else {
            addView.setVisibility(View.GONE);
            findById(R.id.goodsLayout).setVisibility(View.VISIBLE);
        }

    }

    // 在OnDecodeResult回调方法里处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Logger.i("AddLiveActivity回调onActivityResult：" +
//                "\nrequestCode：" + requestCode +
//                "\nresultCode：" + resultCode +
//                "\n输出data：" + data);
        mPhotoUtil.onResult(requestCode, resultCode, data, this);
    }

    // 解析图片的回调
    @Override
    public void onDecodeResult(int requestCode, @Nullable Bitmap bitmap, String filePath, long size, int duration) {
//        Logger.e("收到图片回调？：" + bitmap + "\npath：" + filePath);
        switch (requestCode) {
            case PhotoUtil.CUTS_PHOTO:
                //Log.log("size="+ (new File(filePath).length() / 1024));
                handlePhotoFromAlbum(bitmap, filePath);
                break;
            case PhotoUtil.ALBUM_PHOTO:
            case PhotoUtil.TAKE_PHOTO:
//                Log.log("size=" + (new File(filePath).length() / 1024) + "kb");
//                Log.log("filePath=" + filePath);
                handlePhotoFromAlbum(mPhotoUtil.getView(), bitmap, filePath);
                break;
            default:
                break;
        }

    }

    // 处理拿到的图片
    protected void handlePhotoFromAlbum(Bitmap bitmap, String filePath) {
//        Logger.i("处理本地照片：" + bitmap + "\npath：" + filePath);
        // 保存图片的本地地址
        mCourse.setLocalCoverUrl(filePath);
        // 设置图片
        if (bitmap != null) {
            blurImage.setImageBitmap(bitmap);
        }
        // 上传图片
        doUploadCoverImage(filePath);
    }

    public void handlePhotoFromAlbum(View view, Bitmap bitmap, String filePath) {

        UDImageView udImageView = (UDImageView) view;
        udImageView.setImageBitmap(bitmap);
        doUploadCourseImage(udImageView, filePath, bitmap.getWidth(), bitmap.getHeight());

    }

    // 上传课程封面图片
    private void doUploadCoverImage(String filePath) {

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
                                mCourse.setCover_url(url);
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

    // 上传课程简介和配方的图片
    private void doUploadCourseImage(final UDImageView udImageView, String filePath, final int width, final int height) {
        // 开始下载
        udImageView.setLoadingStatus(true);
        // 执行上传代码
        UploadManager.getInstance().uploadFile(BuildConfig.UPLOAD_BUCKET_IMG,
                UploadManager.TAG_NAME, filePath, new IUploadListener() {
                    @Override
                    public void onSuccess(final String url) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastHelper.showToast("上传完成");

                                udImageView.setLoadingStatus(false);
                                ImageBean bean = new ImageBean(url, width, height);
                                udImageView.setTag(bean);
                                if (udImageView.getId() == summaryPhotoGallery.getId()) {
                                    mCourse.addSummaryImage(bean);
                                    addNewSummaryImage();
                                } else {
                                    mCourse.addFormulaImage(bean);
                                    addNewNewFormulaImage();
                                }

                            }
                        });

                    }

                    @Override
                    public void onFailure(String code, final String errorMsg) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast(errorMsg);
                                udImageView.setDefault();
                            }
                        });
                    }

                    @Override
                    public void onProgress(final long currentSize, final long totalSize) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                udImageView.setProgress(((float) currentSize / (float) totalSize));
                            }
                        });
                    }
                });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        updateCourse();
        UiPreference.putString("Course" + Course.LIVE, GsonTools.toJson(mCourse));
    }

    @Override
    public void onBackPressed() {

        if (!isEdit && showSaveDialog()) {
            saveDialog.show();
            return;
        }

        super.onBackPressed();
    }

    // 显示保存提示框
    protected boolean showSaveDialog() {

        updateCourse();

        if (Utils.isNotEmpty(mCourse.getCover_url())
                || Utils.isNotEmpty(mCourse.getVideo_url())
                || Utils.isNotEmpty(mCourse.getTitle())
                || mCourse.getPrice() != 0
                || (airTimeLayout.getVisibility() == View.VISIBLE && mCourse.start_time > 0)
                || Utils.isNotEmpty(mCourse.getSummary())
                || Utils.isNotEmpty(mCourse.getSummaryImages())
                || Utils.isNotEmpty(mCourse.getFormulaImages())
                || Utils.isNotEmpty(mCourse.getFormula())
                || Utils.isNotEmpty(mCourse.getRecommendGoods())) {
            return true;
        } else {
            UiPreference.putString("Course" + Course.LIVE, null);
            return false;
        }

    }

    private String before;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        before = s.toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if (s == coursePrice.getEditableText()) {
            if (StringUtils.limitPoint(coursePrice, before, 9999.9)) {
                //ToastHelper.showToast("味豆数必须小于10000");
            }
        } else if (s == courseFormula.getEditableText()) {
            int length = s.toString().length();
            formulaTextCount.setText(length + "/" + MAX_TEXT_LENGTH);
            ViewUtils.setPartTextColor(formulaTextCount, R.color.color848484, 0, String.valueOf(length).length());
        } else if (s == courseSummary.getEditableText()) {
            int length = s.toString().length();
            summaryTextCount.setText(length + "/" + MAX_TEXT_LENGTH);
            ViewUtils.setPartTextColor(summaryTextCount, R.color.color848484, 0, String.valueOf(length).length());
        }
    }

    private ArrayList<String> getImageUrls(int galleryId) {

        List<ImageBean> imageBeanList;

        if (galleryId == R.id.summaryPhotoGallery) {
            imageBeanList = mCourse.getSummaryImages();
        } else {
            imageBeanList = mCourse.getFormulaImages();
        }

        ArrayList<String> urls = new ArrayList<>();

        for (int count = 0; count < imageBeanList.size(); ++count) {
            ImageBean bean = imageBeanList.get(count);
            if (!bean.getUrl().startsWith("http")) {
                bean.setUrl("http://img.zwlive.lakatv.com/" + bean.getUrl());
            }
            urls.add(bean.getUrl());
        }

        Log.log(GsonTools.toJson(urls));

        return urls;
    }


    public void addNewSummaryImage() {
        addNewSummaryImage(null);
    }

    public void addNewNewFormulaImage() {
        addNewFormulaImage(null);
    }

    // 添加新的课程简介图片
    public void addNewSummaryImage(ImageBean bean) {
        addNewImage(summaryPhotoGallery, bean);
    }

    // 添加新的课程配方图片
    public void addNewFormulaImage(ImageBean bean) {
        addNewImage(formulaPhotoGallery, bean);
    }

    @Override
    public void onChooseMaterialResult(VideoMaterialBean materialBean) {
        // 素材库回调
//        Logger.e("输出回调Bean：" + materialBean);
        mCourse.setDuration(materialBean.getVideoDuration());
        mCourse.setSnapshot_url(materialBean.getVideoCover());
        mCourse.setVideo_url(materialBean.getVideoUrl());
//        mCourse.setLocalCoverUrl("");
//        mCourse.setLocalVideoUrl("");
    }
}