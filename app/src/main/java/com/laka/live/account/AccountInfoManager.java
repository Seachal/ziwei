package com.laka.live.account;

import com.laka.live.account.login.LoginActivity;
import com.laka.live.bean.UserInfo;
import com.laka.live.manager.RoomManager;
import com.laka.live.util.Common;
import com.laka.live.util.StringUtils;
import com.laka.live.util.UiPreference;
import com.qiyukf.unicorn.api.Unicorn;

/**
 * Created by zwl on 2016/7/18.
 * Email-1501448275@qq.com
 */
public class AccountInfoManager {

    private UserInfo mAccountInfo;
    private static AccountInfoManager sAccountInfoManager;

    private AccountInfoManager() {
        getAccountInfoFromLocalCache();
    }

    public static synchronized AccountInfoManager getInstance() {
        if (sAccountInfoManager == null) {
            sAccountInfoManager = new AccountInfoManager();
        }
        return sAccountInfoManager;
    }

    public UserInfo getAccountInfo() {
        return mAccountInfo;
    }

    public void saveAccountInfo(UserInfo userInfo) {
        mAccountInfo = userInfo;
        UiPreference.saveObject(Common.KEY_MYSELF, userInfo);
    }

    public void clearAccountInfo() {
        if (mAccountInfo == null) {
            return;
        }
        saveAccountInfo(null);
    }

    public boolean checkUserIsLogin() {
        boolean isLogin = false;
        if (mAccountInfo != null && StringUtils.isNotEmpty(mAccountInfo.getToken())) {
            isLogin = true;
        }
        return isLogin;
    }

    public void loginOut() {
        clearAccountInfo();

        //退出socket
        RoomManager.getInstance().stopRoom();
        // 退出账号，要清除Unicorn的账号信息
        Unicorn.logout();
    }

    public String getCurrentAccountUserIdStr() {
        if (mAccountInfo == null) {
            return "";
        }
        return String.valueOf(mAccountInfo.getId());
    }

    public int getCurrentAccountUserId() {
        if (mAccountInfo == null) {
            return 0;
        }
        return mAccountInfo.getId();
    }

    public String getCurrentAccountUserAvatar() {
        if (mAccountInfo == null || StringUtils.isEmpty(mAccountInfo.getAvatar())) {
            return "";
        }
        return mAccountInfo.getAvatar();
    }

    public String getCurrentAccountToken() {
        if (mAccountInfo == null || StringUtils.isEmpty(mAccountInfo.getToken())) {
            return "";
        }
        return mAccountInfo.getToken();
    }

    public String getCurrentAccountNickName() {
        if (mAccountInfo == null || StringUtils.isEmpty(mAccountInfo.getNickName())) {
            return "";
        }
        return mAccountInfo.getNickName();
    }

    public String getCurrentAccountLocation() {
        if (mAccountInfo == null || StringUtils.isEmpty(mAccountInfo.getRegion())) {
            return "";
        }
        return mAccountInfo.getRegion();
    }

    public String getCurrentAccountSign() {
        if (mAccountInfo == null || StringUtils.isEmpty(mAccountInfo.getDescription())) {
            return "";
        }
        return mAccountInfo.getDescription();
    }

    public double getCurrentAccountCoins() {
        if (mAccountInfo == null) {
            return 0;
        }
        return mAccountInfo.getCoins();
    }

    public boolean getCurrentAccountAdminState() {
        return mAccountInfo != null && mAccountInfo.isAdministrator();
    }

    public boolean getCurrentAccountRemindFollowLiveState() {
        return mAccountInfo != null && mAccountInfo.isRemindFollowLive();
    }

    public boolean getCurrentAccountRemindFollowMeState() {
        return mAccountInfo != null && mAccountInfo.isRemindFollowMe();
    }

    public boolean getCurrentAccountRemindInNightState() {
        return mAccountInfo != null && mAccountInfo.isRemindInNight();
    }

    public boolean getCurrentAccountRemindMessageInComeState() {
        return mAccountInfo != null && mAccountInfo.isRemindMessageInCome();
    }

    public boolean getCurrentAccountBindPhoneState() {
        return mAccountInfo != null && mAccountInfo.isBindPhone();
    }

    public boolean getCurrentAccountBindWeChatState() {
        return mAccountInfo != null && mAccountInfo.isBindWeChat();
    }

    public double getCurrentAccountRecvCoins() {
        if (mAccountInfo == null) {
            return 0;
        }
        return mAccountInfo.getRecvCoins();
    }

    public void updateCurrentAccountInfo(UserInfo userInfo) {
        saveAccountInfo(userInfo);
    }

    public void updateCurrentAccountSex(int sex) {
        if (mAccountInfo == null) {
            return;
        }
        mAccountInfo.setGender(sex);
        saveAccountInfo(mAccountInfo);
    }

    public void updateCurrentAccountSign(String sign) {
        if (mAccountInfo == null || StringUtils.isEmpty(sign)) {
            return;
        }
        mAccountInfo.setDescription(sign);
        saveAccountInfo(mAccountInfo);
    }

    public void updateCurrentAccountRemindInNightState(boolean state) {
        if (mAccountInfo == null) {
            return;
        }
        mAccountInfo.setRemindInNight(state);
        saveAccountInfo(mAccountInfo);
    }

    public void updateCurrentAccountRemindFollowLiveState(boolean state) {
        if (mAccountInfo == null) {
            return;
        }
        mAccountInfo.setRemindFollowLive(state);
        saveAccountInfo(mAccountInfo);
    }

    public void updateCurrentAccountRemindFollowMeState(boolean state) {
        if (mAccountInfo == null) {
            return;
        }
        mAccountInfo.setRemindFollowMe(state);
        saveAccountInfo(mAccountInfo);
    }

    public void updateCurrentAccountRemindMessageInComeState(boolean state) {
        if (mAccountInfo == null) {
            return;
        }
        mAccountInfo.setRemindMessageInCome(state);
        saveAccountInfo(mAccountInfo);
    }

    public void updateCurrentAccountCoins(double coins) {
        if (mAccountInfo == null) {
            return;
        }
        mAccountInfo.setCoins(coins);
        saveAccountInfo(mAccountInfo);
    }

    public void updateCurrentAccountHeadUrl(String url) {
        if (mAccountInfo == null) {
            return;
        }
        mAccountInfo.setAvatar(url);
        saveAccountInfo(mAccountInfo);
    }

    public void updateCurrentAccountLocation(String location) {
        if (mAccountInfo == null) {
            return;
        }
        mAccountInfo.setRegion(location);
        saveAccountInfo(mAccountInfo);
    }

    public void updateCurrentAccountAdminState(boolean state) {
        if (mAccountInfo == null) {
            return;
        }
        mAccountInfo.setSadmin(state);
        saveAccountInfo(mAccountInfo);
    }

    public void updateCurrentAccountNickName(String nickName) {
        if (mAccountInfo == null) {
            return;
        }
        mAccountInfo.setNickName(nickName);
        saveAccountInfo(mAccountInfo);
    }


    public void updateCurrentAccountBindPhoneState(boolean state) {
        if (mAccountInfo == null) {
            return;
        }
        mAccountInfo.setBindPhone(state);
        saveAccountInfo(mAccountInfo);
    }

    public void updateCurrentAccountBindWeChatState(boolean state) {
        if (mAccountInfo == null) {
            return;
        }
        mAccountInfo.setBindWeChat(state);
        saveAccountInfo(mAccountInfo);
    }

    public boolean checkCurrentAccountIsBindPhone() {
        return mAccountInfo != null && mAccountInfo.isBindPhone();
    }

    public int getCurrentAccountSex() {
        if (mAccountInfo == null) {
            return 0;
        }
        return mAccountInfo.getGender();
    }

    public boolean getLiveOnlyWifiState(){
        return mAccountInfo != null && mAccountInfo.isLiveOnlyWifi();
    }

    public void updateLiveOnlyWifiState(boolean liveOnlyWifi){
        if (mAccountInfo == null){
            return;
        }
        mAccountInfo.setLiveOnlyWifi(liveOnlyWifi);
        saveAccountInfo(mAccountInfo);
    }

    private void getAccountInfoFromLocalCache() {
        Object object = UiPreference.getObject(Common.KEY_MYSELF);
        if (object instanceof UserInfo) {
            mAccountInfo = (UserInfo) object;
        }
    }

    public void tryOpenLoginWindow() {
        //// TODO: 2017/7/13 去登陆
//        LoginActivity.startActivity(, LoginActivity.TYPE_FROM_NEED_LOGIN);
    }

    public String getPhoneNumber() {
        // TODO: 2017/7/13 手机号
        return "";
    }

    public boolean isLogin() {
        return checkUserIsLogin();
    }

    public String getToken() {
        if (mAccountInfo == null) {
            return "";
        }
        return mAccountInfo.getToken();
    }
}
