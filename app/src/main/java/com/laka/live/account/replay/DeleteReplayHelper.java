package com.laka.live.account.replay;

import android.app.Activity;
import android.text.TextUtils;

import com.laka.live.R;
import com.laka.live.msg.Msg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.ui.widget.dialog.GenericDialog;
import com.laka.live.ui.widget.dialog.IDialogOnClickListener;
import com.laka.live.ui.widget.dialog.SimpleTextDialog;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.StringUtils;
import com.laka.live.util.ToastHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwl on 2016/7/1.
 * Email-1501448275@qq.com
 */
public class DeleteReplayHelper {

    private static DeleteReplayHelper sInstance;
    private List<VideoParams> mDeleteVideoList = new ArrayList<>();

    private DeleteReplayHelper() {
    }

    public static DeleteReplayHelper getInstance() {
        if (sInstance == null) {
            sInstance = new DeleteReplayHelper();
        }
        return sInstance;
    }

    public List<VideoParams> getDeleteVideoList() {
        return mDeleteVideoList;
    }

    public void addVideoToDeleteList(VideoParams video) {
        if (video == null) {
            return;
        }
        boolean isExist = false;
        for (VideoParams videoParams : mDeleteVideoList) {
            if (videoParams == null) {
                continue;
            }
            if (videoParams.video != null && TextUtils.equals(videoParams.video.getVid(), video.video.getVid())) {
                isExist = true;
                break;
            }
        }
        if (isExist) {
            return;
        }
        mDeleteVideoList.add(video);
    }

    public void removeVideoFromDeleteList(VideoParams video) {
        if (video == null) {
            return;
        }
        VideoParams tempVideo = null;
        for (VideoParams v : mDeleteVideoList) {
            if (v == null) {
                continue;
            }
            if (TextUtils.equals(v.video.getVid(), video.video.getVid())) {
                tempVideo = v;
                break;
            }
        }
        if (tempVideo == null) {
            return;
        }
        mDeleteVideoList.remove(tempVideo);
    }

    public void clearDeleteList() {
        mDeleteVideoList.clear();
    }

    public void showDialogWhenBeforeDeleteRequest(final Activity activity, final DeleteRequestCallback callback) {
        if (mDeleteVideoList.size() == 0) {
            return;
        }
        String tips = ResourceHelper.getString(R.string.replay_sure_delete_start_tip)
                + mDeleteVideoList.size()
                + ResourceHelper.getString(R.string.replay_sure_delete_end_tip);
        final SimpleTextDialog simpleTextDialog = new SimpleTextDialog(activity);
        simpleTextDialog.setTitle(ResourceHelper.getString(R.string.replay_delete_button));
        simpleTextDialog.setText(tips);
        simpleTextDialog.addYesNoButton();
        simpleTextDialog.setOnClickListener(new IDialogOnClickListener() {
            @Override
            public boolean onDialogClick(GenericDialog dialog, int viewId, Object extra) {
                if (viewId == GenericDialog.ID_BUTTON_YES) {
                    startDeleteRequest(activity, callback);
                }
                return false;
            }
        });
        simpleTextDialog.setRecommendButton(GenericDialog.ID_BUTTON_YES);
        simpleTextDialog.setCanceledOnTouchOutside(false);
        simpleTextDialog.show();
    }

    private void startDeleteRequest(final Activity activity, final DeleteRequestCallback callback) {
        String deleteString = "";
        int index = 0;
        for (VideoParams video : mDeleteVideoList) {
            if (index == 0) {
                deleteString += video.video.getTime();
            } else {
                deleteString += "," + video.video.getTime();
            }
            index++;
        }
        if (StringUtils.isEmpty(deleteString)) {
            return;
        }
        DataProvider.removeVideo(activity, deleteString, new GsonHttpConnection.OnResultListener<Msg>() {
            @Override
            public void onSuccess(Msg msg) {
                if (msg.getCode() == Msg.TLV_OK) {
                    callback.deleteSuccess();
                } else {
                    ToastHelper.showToast(ResourceHelper.getString(R.string.replay_delete_failed_tips) + msg.getCode());
                }
            }

            @Override
            public void onFail(int errorCode, String errorMsg, String command) {
                ToastHelper.showToast(ResourceHelper.getString(R.string.replay_delete_failed_tips) + errorCode);
                callback.deleteFailed();
            }
        });
    }

    public interface DeleteRequestCallback {
        void deleteSuccess();

        void deleteFailed();
    }
}
