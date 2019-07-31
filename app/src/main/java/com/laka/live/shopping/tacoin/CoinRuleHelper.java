package com.laka.live.shopping.tacoin;

import com.google.gson.Gson;
import com.laka.live.shopping.bean.TACoinRuleBean;
import com.laka.live.shopping.bean.TACoinRuleJsonBean;
import com.laka.live.shopping.network.HttpManager;
import com.laka.live.shopping.network.HttpMethod;
import com.laka.live.shopping.network.HttpUrls;
import com.laka.live.shopping.network.IHttpCallBack;
import com.laka.live.shopping.network.IHttpManager;
import com.laka.live.shopping.utils.SettingFlags;
import com.laka.live.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwulin on 2016/3/16.
 * email 1501448275@qq.com
 */
public class CoinRuleHelper {

    public static final String KEY_COIN_RULE = "coin_rule_in_community";

    private TACoinRuleBean mRuleBean;
    private static CoinRuleHelper sCoinRuleHelper;

    private boolean mIsRequesting = false;

    private CoinRuleHelper() {
        String data = SettingFlags.getStringFlag(KEY_COIN_RULE);
        if (StringUtils.isNotEmpty(data)) {
            try {
                TACoinRuleJsonBean jsonBean = new Gson().fromJson(data, TACoinRuleJsonBean.class);
                mRuleBean = jsonBean.getData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static CoinRuleHelper getInstance() {
        if (sCoinRuleHelper == null) {
            sCoinRuleHelper = new CoinRuleHelper();
        }
        return sCoinRuleHelper;
    }

    public void tryRequestCoinRuleList() {
        if (mIsRequesting) {
            return;
        }
        mIsRequesting = true;
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("a", "coinRule");
        httpManager.request(HttpUrls.URL_INDEX, HttpMethod.POST, TACoinRuleJsonBean.class,
                new IHttpCallBack() {
                    @Override
                    public <T> void onSuccess(T obj, String result) {
                        mIsRequesting = false;
                        if (obj != null) {
                            handleOnRequestSuccess((TACoinRuleJsonBean) obj, result);
                        }
                    }

                    @Override
                    public void onError(String errorStr, int code) {
                        mIsRequesting = false;
                    }
                });
    }

    private void handleOnRequestSuccess(TACoinRuleJsonBean ruleJsonBean, String resource) {
        mRuleBean = ruleJsonBean.getData();
        saveCoinRuleInLocal(resource);
    }

    private void saveCoinRuleInLocal(String resource) {
        SettingFlags.setFlag(KEY_COIN_RULE, resource);
    }

    public void clearCoinRuleData() {
        mRuleBean = null;
        SettingFlags.resetFlag(KEY_COIN_RULE);
    }

    public List<String> tryGetCoinRuleList() {
        List<String> coinItems = new ArrayList<>();
        if (mRuleBean != null) {
            coinItems.addAll(mRuleBean.getRewardValues());
        } else {
            tryRequestCoinRuleList();
        }
        return coinItems;
    }

    public boolean hasCoinListInLocal() {
        boolean isHave = false;
        List<String> tempItems = tryGetCoinRuleList();
        if (tempItems.size() > 0) {
            isHave = true;
        }
        return isHave;
    }

    public float tryGetCoinRate() {
        float coinRate = 0.01f;
        if (mRuleBean != null) {
            String coinRateStr = mRuleBean.getCoinRate();
            if (StringUtils.isNotEmpty(coinRateStr)) {
                coinRate = StringUtils.parseFloat(coinRateStr);
                coinRate = 1 / coinRate;
            }
        } else {
            tryRequestCoinRuleList();
        }
        return coinRate;
    }

    public String tryGetGoodsDiscount() {
        String goodsDiscount = "30%";
        if (mRuleBean != null) {
            String goodsDiscountStr = mRuleBean.getGoodsDiscount();
            if (StringUtils.isNotEmpty(goodsDiscountStr)) {
                goodsDiscount = goodsDiscountStr;
            }
        } else {
            tryRequestCoinRuleList();
        }
        return goodsDiscount;
    }

    public int tryGetRewardLevel() {
        int rewardLevel = 1;
        if (mRuleBean != null) {
            String rewardLevelString = mRuleBean.getRewardLevel();
            if (StringUtils.isNotEmpty(rewardLevelString)) {
                rewardLevel = StringUtils.parseInt(rewardLevelString);
            }
        } else {
            tryRequestCoinRuleList();
        }
        return rewardLevel;
    }

    public int tryGetToRewardLevel() {
        int toRewardLevel = 1;
        if (mRuleBean != null) {
            String rewardLevelString = mRuleBean.getToRewardLevel();
            if (StringUtils.isNotEmpty(rewardLevelString)) {
                toRewardLevel = StringUtils.parseInt(rewardLevelString);
            }
        } else {
            tryRequestCoinRuleList();
        }
        return toRewardLevel;
    }

    public int tryGetEvaluatePostGuideCoinNum() {
        int coinNumber = 500;
        if (mRuleBean != null) {
            String evaluatePostGuideCoinNum = mRuleBean.getEvaluPostGuideCoinNum();
            if (StringUtils.isNotEmpty(evaluatePostGuideCoinNum)) {
                coinNumber = StringUtils.parseInt(evaluatePostGuideCoinNum);
            }
        } else {
            tryRequestCoinRuleList();
        }
        return coinNumber;
    }
}
