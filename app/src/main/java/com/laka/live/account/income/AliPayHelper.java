package com.laka.live.account.income;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.laka.live.help.pay.PayResult;
import com.laka.live.util.ThreadManager;

/**
 * Created by zwl on 2016/7/20.
 * AliPay helper
 */
public class AliPayHelper {
    private static final int SDK_PAY_FLAG = 1;
    public static final String PAY_TYPE_SUCCESS = "9000";
    public static final String PAY_TYPE_CANCEL = "6001";
    public static final String PAY_TYPE_WAIT = "8000";

    private Handler mHandler;

    public AliPayHelper(final AliPayCallback callback) {
        if (callback == null) {
            return;
        }
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SDK_PAY_FLAG: {
                        handleOnPayResult(msg, callback);
                        break;
                    }
                    default:
                        break;
                }
            }
        };
    }

    private void handleOnPayResult(Message msg, AliPayCallback callback) {
        PayResult payResult = new PayResult((String) msg.obj);
        /**
         * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
         * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
         * docType=1) 建议商户依赖异步通知
         */
        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
        String resultStatus = payResult.getResultStatus();
        if (PAY_TYPE_SUCCESS.equals(resultStatus)) {
            callback.onSuccess(resultInfo);
            return;
        }
        if (PAY_TYPE_WAIT.equals(resultStatus)) {
            callback.onWait();
        } else if (PAY_TYPE_CANCEL.equals(resultStatus)) {
            callback.onCancel();
        } else {
            callback.onFailed();
        }
    }

    public void startPayTask(final Activity activity, final String payInfo) {
        ThreadManager.post(ThreadManager.THREAD_WORK, new Runnable() {
            @Override
            public void run() {
                PayTask aliPay = new PayTask(activity);
                String result = aliPay.pay(payInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        });
    }

    public interface AliPayCallback {
        void onSuccess(String payResultInfo);

        void onCancel();

        void onWait();

        void onFailed();
    }
}
