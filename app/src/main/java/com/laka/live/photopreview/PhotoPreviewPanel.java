package com.laka.live.photopreview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.laka.live.R;
import com.laka.live.ui.widget.panel.ActionSheetPanel;
import com.laka.live.ui.widget.panel.BasePanel;
import com.laka.live.util.HardwareUtil;
import com.laka.live.util.Log;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.SystemUtil;
import com.laka.live.util.ThreadManager;
import com.laka.live.util.ToastHelper;

import java.io.File;
import java.util.ArrayList;

public class PhotoPreviewPanel extends BasePanel {
    private static final String SAVE_IMAGE_PATH = "滋味Live/CourseImages/";
    private FixedViewPager mViewPager;
    private TextView mPageIndexView;
    private ImagePagerAdapter mPagerAdapter;

    private ArrayList<String> mImageList = new ArrayList<>();
    private int mPosition = 0;

    public PhotoPreviewPanel(Context context) {
        super(context, false);
        initPanel();
        setCancelTouchOutside(false);
        setDimValue(0.8f);
        setAnimation(R.style.ScaleInOutAnim);
    }

    @Override
    protected View onCreateContentView() {
        View parentView = View.inflate(mContext, R.layout.image_preview_layout, null);
        setupViews(parentView);
        return parentView;
    }

    @Override
    protected FrameLayout.LayoutParams getLayoutParams() {
        int width = FrameLayout.LayoutParams.MATCH_PARENT;
        int height = FrameLayout.LayoutParams.MATCH_PARENT;
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height);
        lp.gravity = Gravity.CENTER;
        return lp;
    }

    private void setupViews(View parentView) {
        mViewPager = (FixedViewPager) parentView.findViewById(R.id.image_preview_viewpager);
        mPageIndexView = (TextView) parentView.findViewById(R.id.image_preview_page_index);
        View backButton = parentView.findViewById(R.id.image_preview_back);
        if (SystemUtil.isTransparentStatusBarEnable()) {
            FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams) backButton.getLayoutParams();
            flp.topMargin += SystemUtil.getStatusBarHeight(mContext);
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidePanel();
            }
        });
        mPagerAdapter = new ImagePagerAdapter();
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                int currentPage = position + 1;
                mPageIndexView.setText(currentPage + "/" + mImageList.size());
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int position) {
            }
        });
    }


    public void setupPreviewPanel(PhotoPreviewInfo info) {
        mImageList.clear();
        mImageList.addAll(info.photoList);
        mPosition = info.position;

        mPagerAdapter.notifyDataSetChanged();
        int currentPage = mPosition + 1;
        if (mImageList.size() <= 1) {
            mPageIndexView.setVisibility(View.INVISIBLE);
        } else {
            mPageIndexView.setVisibility(View.VISIBLE);
        }
        mPageIndexView.setText(currentPage + "/" + mImageList.size());
        mViewPager.setCurrentItem(mPosition);
    }

    // 刷新ImageItemView
    public void notifyImageViews() {

        if(mViewPager == null || mImageList.size() == 0) {
            return;
        }

       for (int count = 0; count < mViewPager.getChildCount() && count < mImageList.size(); ++count) {
           ImageItemView imageItemView = (ImageItemView) mViewPager.getChildAt(count);
           imageItemView.setupPhotoView(mImageList.get(count));
       }

    }

    private class ImagePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mImageList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageItemView imageView = new ImageItemView(mContext);
            String url = null;
            if (position < mImageList.size()) {
                url = mImageList.get(position);
            }
            imageView.setupPhotoView(url);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(imageView, lp);
            return imageView;
        }

    }

    private class ImageItemView extends FrameLayout {
        private PhotoView mPhotoView;
        private TextView mLoadingView;
        private String mImageUrl;

        public ImageItemView(Context context) {
            super(context);
            addPhotoView();
            addLoadingView();
        }

        private void addLoadingView() {
            mLoadingView = new TextView(getContext());
            mLoadingView.setTextColor(getResources().getColor(R.color.white));
            mLoadingView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen
                    .space_18));
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            this.addView(mLoadingView, lp);
        }

        private void addPhotoView() {
            mPhotoView = new PhotoView(getContext());
            mPhotoView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showSavePhotoPanel(mImageUrl);
                    return true;
                }
            });
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            this.addView(mPhotoView, lp);
        }

        public void setupPhotoView(String url) {
            if (StringUtils.isEmpty(url)) {
                return;
            }
            mImageUrl = url;
            if (url.startsWith("http://") || url.startsWith("https://")) {
                mLoadingView.setVisibility(VISIBLE);
                mLoadingView.setText(ResourceHelper.getString(R.string.loading));
                FrescoHelper.loadImage(url, new FrescoHelper.FrescoLoadImageBitmapCallback() {
                    @Override
                    public void onImageLoadFinish(Bitmap bitmap) {
                        if (bitmap != null && !bitmap.isRecycled()) {
                            mLoadingView.setVisibility(View.GONE);
                            mPhotoView.setImageBitmap(bitmap);
                        } else {
                            mLoadingView.setVisibility(View.VISIBLE);
                            mLoadingView.setText(getResources().getString(R.string.loading_failed));
                        }
                    }
                }, true);
            } else {
                String startPath = "file://";
                if (url.startsWith(startPath)) {
                    url = url.substring(startPath.length());
                }
                mLoadingView.setVisibility(View.GONE);
                //chuan
                Bitmap bitmap = BitmapUtil.createBitmapThumbnail(url,
                        HardwareUtil.getDeviceWidth(),
                        HardwareUtil.getDeviceHeight());
                if (bitmap != null && !bitmap.isRecycled()) {
                    mPhotoView.setImageBitmap(bitmap);
                }
            }
        }

        private void showSavePhotoPanel(final String imageUrl) {
            mImageUrl = imageUrl;
            ActionSheetPanel panel = new ActionSheetPanel(getContext());
            ActionSheetPanel.ActionSheetItem item = new ActionSheetPanel.ActionSheetItem();
            item.title = getResources().getString(R.string.photo_save);
            item.id = "save";
            panel.addSheetItem(item);
            panel.setSheetItemClickListener(new ActionSheetPanel.OnActionSheetClickListener() {
                @Override
                public void onActionSheetItemClick(String id) {
                    savePhoto(imageUrl);
                }
            });
            panel.showPanel();
        }

        private void savePhoto(final String imageUrl) {
            if (StringUtils.isEmpty(imageUrl)) {
                ToastHelper.showToast(R.string.save_failed);
                return;
            }
            final File file = FrescoHelper.getCachedImageOnDisk(imageUrl);
            if (file == null) {
                ToastHelper.showToast(R.string.save_failed);
            } else {
                final ValueHolder holder = new ValueHolder();
                holder.bolValue = true;
                String fileName = FileUtil.getFileNameFromPath(imageUrl, true);
                final String finalPath = getSaveImageDir() + fileName;
                ThreadManager.post(ThreadManager.THREAD_WORK, new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FileUtil.copy(file, new File(finalPath));
                        } catch (Exception e) {
                            holder.bolValue = false;
                        }
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        if (holder.bolValue) {
                            notifyGalleryRefresh(mContext, finalPath);
                            ToastHelper.showToast("图片已保存在" + finalPath + "目录下", Toast.LENGTH_LONG);
                        } else {
                            ToastHelper.showToast(R.string.save_failed);
                        }
                    }
                });
            }
        }
    }

    private String getSaveImageDir() {
        return BitmapUtil.getSdcardPath() + SAVE_IMAGE_PATH;
    }

    private void notifyGalleryRefresh(final Context context, final String filePath) {
        if (context == null) {
            return;
        }
        if (StringUtils.isEmpty(filePath)) {
            return;
        }

        ThreadManager.post(ThreadManager.THREAD_UI, new Runnable() {
            @Override
            public void run() {
                Uri uri = Uri.fromFile(new File(filePath));
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                try {
                    context.sendBroadcast(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
