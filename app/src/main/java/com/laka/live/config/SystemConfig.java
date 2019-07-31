package com.laka.live.config;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.application.LiveApplication;
import com.laka.live.msg.SingleMsg;
import com.laka.live.network.DataProvider;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.util.Common;
import com.laka.live.util.Log;
import com.laka.live.util.UiPreference;
import com.laka.live.util.Utils;

/**
 * Created by luwies on 16/7/4.
 */
public class SystemConfig {

    private final static int CONFIG_SHOW = 1;

    private final static int CONFIG_HIDE = 2;

    private final static int PAY_WEIXIN_DEFAULT = CONFIG_SHOW;

    private final static int PAY_ALIPAY_DEFAULT = CONFIG_SHOW;

    private final static int WITHDRAWAL_WEIXIN_DEFAULT = CONFIG_SHOW;

    private final static int WITHDRAWAL_LAKA_DEFAULT = CONFIG_SHOW;

    private final static int FACE_BEAUTY_DEFAULT = CONFIG_SHOW;

    private final static int SPLASH_SCREEN_DEFAULT = CONFIG_SHOW;

    private final static int SHARE_DEFAULT = CONFIG_SHOW;

    private final static int UPGRADE_DEFAULT = CONFIG_SHOW;

    private final static int LOGIN_WEIXIN_DEFAULT = CONFIG_SHOW;

    private final static int LOGIN_QQ_DEFAULT = CONFIG_SHOW;

    private final static int LOGIN_WEIBO_DEFAULT = CONFIG_SHOW;

    private final static int LOGIN_MOBILE_DEFAULT = CONFIG_SHOW;

    private static SystemConfig ourInstance;

    public static SystemConfig getInstance() {
        if (ourInstance == null) {
            synchronized (SystemConfig.class) {
                if (ourInstance == null) {
                    ourInstance = new SystemConfig();
                }
            }
        }
        return ourInstance;
    }

    private SystemConfig() {
    }

    /**
     * 刷新配置数据
     */
    public void update() {
        DataProvider.getSystemConfig(LiveApplication.getInstance(),
                new GsonHttpConnection.OnResultListener<SingleMsg<Config>>() {
                    @Override
                    public void onSuccess(SingleMsg<Config> configSingleMsg) {
                        if (configSingleMsg != null) {
                            Config config = configSingleMsg.getResult();
                            update(config);
                        }
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg, String command) {
                        //do nothing
                    }
                });
    }

    public String getKefuID(){
      return String.valueOf(UiPreference.getInt(Common.CUSTOMER_SER,72859));
    }

    /**
     * 保存配置信息
     * @param config
     */
    private void update(Config config) {
        if (config != null) {
            checkConfig(config);


            Context context = LiveApplication.getInstance();
            UiPreference.putInt(Common.CONFIG_VERSION, Utils.getVersionCode(context));
            UiPreference.putInt(Common.CUSTOMER_SER, config.getCustomerSer());
            UiPreference.putInt(Common.PAY_WEIXIN, config.getPayWeixin());
            UiPreference.putInt(Common.PAY_ALIPAY, config.getPayAlipay());
            UiPreference.putInt(Common.WITHDRAWAL_WEIXIN, config.getWithdrawalWeixin());
            UiPreference.putInt(Common.WITHDRAWAL_LAKA, config.getWithdrawalLaka());
            UiPreference.putInt(Common.FACE_BEAUTY, config.getFaceBeauty());
            UiPreference.putInt(Common.SPLASH_SCREEN, config.getSplashScreen());
            UiPreference.putInt(Common.SHARE, config.getShare());
            UiPreference.putInt(Common.UPGRADE, config.getUpgrade());

            UiPreference.putInt(Common.LOGIN_WEIXIN, config.getLoginWeixin());
            UiPreference.putInt(Common.LOGIN_QQ, config.getLoginQQ());
            UiPreference.putInt(Common.LOGIN_WEIBO, config.getLoginWeibo());
            UiPreference.putInt(Common.LOGIN_MOBILE, config.getLoginMobile());

            UiPreference.putInt(Common.COIN_DISPLAY_RATE, config.getCoinDisplayRate());
            UiPreference.putInt(Common.COIN_DISPLAY_DECIMAL, config.getCoinDisplayDecimal());
        }
    }

    /**
     * 检查数据合法性
     * @param config
     */
    private void checkConfig(Config config) {
        if (config != null) {
            if (checkValue(config.getPayWeixin()) ) {
                config.setPayWeixin(PAY_WEIXIN_DEFAULT);
            }

            if (checkValue(config.getPayAlipay()) ) {
                config.setPayAlipay(PAY_ALIPAY_DEFAULT);
            }

            if (checkValue(config.getWithdrawalWeixin()) ) {
                config.setWithdrawalWeixin(WITHDRAWAL_WEIXIN_DEFAULT);
            }

            if (checkValue(config.getWithdrawalLaka()) ) {
                config.setWithdrawalLaka(WITHDRAWAL_LAKA_DEFAULT);
            }

            if (checkValue(config.getFaceBeauty()) ) {
                config.setFaceBeauty(FACE_BEAUTY_DEFAULT);
            }

            if (checkValue(config.getSplashScreen()) ) {
                config.setSplashScreen(SPLASH_SCREEN_DEFAULT);
            }

            if (checkValue(config.getShare()) ) {
                config.setShare(SHARE_DEFAULT);
            }

            if (checkValue(config.getUpgrade()) ) {
                config.setUpgrade(UPGRADE_DEFAULT);
            }

            if (checkValue(config.getLoginWeixin())) {
                config.setLoginWeixin(LOGIN_WEIXIN_DEFAULT);
            }

            if (checkValue(config.getLoginQQ())) {
                config.setLoginQQ(LOGIN_QQ_DEFAULT);
            }

            if (checkValue(config.getLoginWeibo())) {
                config.setLoginWeibo(LOGIN_WEIBO_DEFAULT);
            }

            if (checkValue(config.getLoginMobile())) {
                config.setLoginMobile(LOGIN_MOBILE_DEFAULT);
            }
        }
    }

    private boolean checkValue(int value) {
        return value < CONFIG_SHOW || value > CONFIG_HIDE;
    }

    private void checkVersion() {
        if (Utils.getVersionCode(LiveApplication.getInstance())
                != UiPreference.getInt(Common.CONFIG_VERSION, 0)) {
            setDefaultValue();
        }
    }

    private void setDefaultValue() {
        //刷新版本号
        UiPreference.putInt(Common.CONFIG_VERSION, Utils.getVersionCode(LiveApplication.getInstance()));
        //使用默认值
        UiPreference.putInt(Common.PAY_WEIXIN, PAY_WEIXIN_DEFAULT);
        UiPreference.putInt(Common.PAY_ALIPAY, PAY_ALIPAY_DEFAULT);
        UiPreference.putInt(Common.WITHDRAWAL_WEIXIN, WITHDRAWAL_WEIXIN_DEFAULT);
        UiPreference.putInt(Common.WITHDRAWAL_LAKA, WITHDRAWAL_LAKA_DEFAULT);
        UiPreference.putInt(Common.FACE_BEAUTY, FACE_BEAUTY_DEFAULT);
        UiPreference.putInt(Common.SPLASH_SCREEN, SPLASH_SCREEN_DEFAULT);
        UiPreference.putInt(Common.SHARE, SHARE_DEFAULT);
        UiPreference.putInt(Common.UPGRADE, UPGRADE_DEFAULT);

        UiPreference.putInt(Common.LOGIN_WEIXIN, LOGIN_WEIXIN_DEFAULT);
        UiPreference.putInt(Common.LOGIN_QQ, LOGIN_QQ_DEFAULT);
        UiPreference.putInt(Common.LOGIN_WEIBO, LOGIN_WEIBO_DEFAULT);
        UiPreference.putInt(Common.LOGIN_MOBILE, LOGIN_MOBILE_DEFAULT);
    }

    public boolean isShowWeixinPay() {
        checkVersion();
        return UiPreference.getInt(Common.PAY_WEIXIN, PAY_WEIXIN_DEFAULT) == CONFIG_SHOW;
    }

    public boolean isShowAliPay() {
        checkVersion();
        return UiPreference.getInt(Common.PAY_ALIPAY, PAY_ALIPAY_DEFAULT) == CONFIG_SHOW;
    }

    public boolean isShowWithdrawalWeixin() {
        checkVersion();
        return UiPreference.getInt(Common.WITHDRAWAL_WEIXIN, WITHDRAWAL_WEIXIN_DEFAULT) == CONFIG_SHOW;
    }

    public boolean isShowWithdrawalLaka() {
        checkVersion();
        return UiPreference.getInt(Common.WITHDRAWAL_LAKA, WITHDRAWAL_LAKA_DEFAULT) == CONFIG_SHOW;
    }

    public boolean isShowFaceBeauty() {
        checkVersion();
        return UiPreference.getInt(Common.FACE_BEAUTY, FACE_BEAUTY_DEFAULT) == CONFIG_SHOW;
    }

    public boolean isShowSplashScreen() {
        checkVersion();
        return UiPreference.getInt(Common.SPLASH_SCREEN, SPLASH_SCREEN_DEFAULT) == CONFIG_SHOW;
    }

    public boolean isShowShare() {
        checkVersion();
        return UiPreference.getInt(Common.SHARE, SHARE_DEFAULT) == CONFIG_SHOW;
    }

    public boolean isShowUpgrade() {
        checkVersion();
        return UiPreference.getInt(Common.UPGRADE, UPGRADE_DEFAULT) == CONFIG_SHOW;
    }

    public boolean isShowLoginWeixin() {
        checkVersion();
        return UiPreference.getInt(Common.LOGIN_WEIXIN, LOGIN_WEIXIN_DEFAULT) == CONFIG_SHOW;
    }

    public boolean isShowLoginQQ() {
        checkVersion();
        return UiPreference.getInt(Common.LOGIN_QQ, LOGIN_QQ_DEFAULT) == CONFIG_SHOW;
    }

    public boolean isShowLoginWeibo() {
        checkVersion();
        return UiPreference.getInt(Common.LOGIN_WEIBO, LOGIN_WEIBO_DEFAULT) == CONFIG_SHOW;
    }

    public boolean isShowLoginMobile() {
        checkVersion();
        return UiPreference.getInt(Common.LOGIN_MOBILE, LOGIN_MOBILE_DEFAULT) == CONFIG_SHOW;
    }

    public static class Config {
        @Expose
        @SerializedName(Common.CUSTOMER_SER)
        private int customerSer;

        @Expose
        @SerializedName(Common.PAY_WEIXIN)
        private int payWeixin = PAY_WEIXIN_DEFAULT;

        @Expose
        @SerializedName(Common.PAY_ALIPAY)
        private int payAlipay = PAY_ALIPAY_DEFAULT;

        @Expose
        @SerializedName(Common.WITHDRAWAL_WEIXIN)
        private int withdrawalWeixin = WITHDRAWAL_WEIXIN_DEFAULT;

        @Expose
        @SerializedName(Common.WITHDRAWAL_LAKA)
        private int withdrawalLaka = WITHDRAWAL_LAKA_DEFAULT;

        @Expose
        @SerializedName(Common.FACE_BEAUTY)
        private int faceBeauty = FACE_BEAUTY_DEFAULT;

        @Expose
        @SerializedName(Common.SPLASH_SCREEN)
        private int splashScreen = SPLASH_SCREEN_DEFAULT;

        @Expose
        @SerializedName(Common.SHARE)
        private int share = SHARE_DEFAULT;

        @Expose
        @SerializedName(Common.UPGRADE)
        private int upgrade = UPGRADE_DEFAULT;

        @Expose
        @SerializedName(Common.LOGIN_WEIXIN)
        private int loginWeixin;

        @Expose
        @SerializedName(Common.LOGIN_QQ)
        private int loginQQ;

        @Expose
        @SerializedName(Common.LOGIN_WEIBO)
        private int loginWeibo;

        @Expose
        @SerializedName(Common.LOGIN_MOBILE)
        private int loginMobile;

        @Expose
        @SerializedName(Common.COIN_DISPLAY_RATE)
        private int coinDisplayRate;

        @Expose
        @SerializedName(Common.COIN_DISPLAY_DECIMAL)
        private int coinDisplayDecimal;

        public int getCoinDisplayRate() {
            return coinDisplayRate;
        }

        public void setCoinDisplayRate(int coinDisplayRate) {
            this.coinDisplayRate = coinDisplayRate;
        }

        public int getCoinDisplayDecimal() {
            return coinDisplayDecimal;
        }

        public void setCoinDisplayDecimal(int coinDisplayDecimal) {
            this.coinDisplayDecimal = coinDisplayDecimal;
        }

        public int getCustomerSer() {
            return customerSer;
        }

        public void setCustomerSer(int customerSer) {
            this.customerSer = customerSer;
        }

        public int getPayWeixin() {
            return payWeixin;
        }

        public void setPayWeixin(int payWeixin) {
            this.payWeixin = payWeixin;
        }

        public int getPayAlipay() {
            return payAlipay;
        }

        public void setPayAlipay(int payAlipay) {
            this.payAlipay = payAlipay;
        }

        public int getWithdrawalWeixin() {
            return withdrawalWeixin;
        }

        public void setWithdrawalWeixin(int withdrawalWeixin) {
            this.withdrawalWeixin = withdrawalWeixin;
        }

        public int getWithdrawalLaka() {
            return withdrawalLaka;
        }

        public void setWithdrawalLaka(int withdrawalLaka) {
            this.withdrawalLaka = withdrawalLaka;
        }

        public int getFaceBeauty() {
            return faceBeauty;
        }

        public void setFaceBeauty(int faceBeauty) {
            this.faceBeauty = faceBeauty;
        }

        public int getSplashScreen() {
            return splashScreen;
        }

        public void setSplashScreen(int splashScreen) {
            this.splashScreen = splashScreen;
        }

        public int getShare() {
            return share;
        }

        public void setShare(int share) {
            this.share = share;
        }

        public int getUpgrade() {
            return upgrade;
        }

        public void setUpgrade(int upgrade) {
            this.upgrade = upgrade;
        }

        public int getLoginWeixin() {
            return loginWeixin;
        }

        public void setLoginWeixin(int loginWeixin) {
            this.loginWeixin = loginWeixin;
        }

        public int getLoginQQ() {
            return loginQQ;
        }

        public void setLoginQQ(int loginQQ) {
            this.loginQQ = loginQQ;
        }

        public int getLoginWeibo() {
            return loginWeibo;
        }

        public void setLoginWeibo(int loginWeibo) {
            this.loginWeibo = loginWeibo;
        }

        public int getLoginMobile() {
            return loginMobile;
        }

        public void setLoginMobile(int loginMobile) {
            this.loginMobile = loginMobile;
        }
    }
}
