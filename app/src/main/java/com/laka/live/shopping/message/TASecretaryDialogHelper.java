package com.laka.live.shopping.message;

import android.os.Message;


/**
 * Created by gangqing on 2016/4/29.
 * Email:denggangqing@ta2she.com
 */
public class TASecretaryDialogHelper {
    /**
     * 无参数时传null
     *
     * @param chatParams
     */
//    public void requestChatDetailsId(final ChatParams chatParams) {
//        if (!AccountManager.getInstance().isLogin()) {
//            AccountManager.getInstance().tryOpenLoginWindow();
//            return;
//        }
//        MessageDialogIdRequest request = new MessageDialogIdRequest();
//        request.dialogIdRequest(Constant.USER_ID_SHE_SECRETARY, Constant.USER_TYPE_SECRETARY, new HttpCallbackAdapter() {
//            @Override
//            public <T> void onSuccess(T obj, String result) {
//                if (obj instanceof JTBMsgDialogIdBean) {
//                    handleDialogIdRequestSuccess(chatParams, (JTBMsgDialogIdBean) obj);
//                } else {
//                    ToastHelper.showToast(R.string.network_error_retry);
//                }
//            }
//
//            @Override
//            public void onError(String errorStr, int code) {
//                super.onError(errorStr, code);
//                ToastHelper.showToast(R.string.network_error_retry);
//            }
//        });
//    }
//
//    private void handleDialogIdRequestSuccess(ChatParams chatParams, JTBMsgDialogIdBean bean) {
//        if (bean == null) {
//            return;
//        }
//        if (chatParams == null) {
//            chatParams = new ChatParams();
//        }
//
//        MsgDialogBean dialogs = new MsgDialogBean();
//        dialogs.setDialogId(bean.getData().dialogId);
//        dialogs.setUserId(bean.getData().toUserId);
//        dialogs.setUserRoleId(Constant.TA_SHE_SECRETARY);
//        dialogs.setPullBlackFlag(bean.getData().pullBlackFlag);
//        chatParams.setMessageDialog(dialogs);
//        Message message = Message.obtain();
//        message.obj = chatParams;
//        message.what = MsgDef.MSG_SHOW_MESSAGE_CHAT_WINDOW;
//        MsgDispatcher.getInstance().sendMessage(message);
//    }
}
