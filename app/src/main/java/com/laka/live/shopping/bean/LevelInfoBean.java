package com.laka.live.shopping.bean;

/**
 * Created by gangqing on 2016/3/15.
 * Email:denggangqing@ta2she.com
 */
public class LevelInfoBean {

    private String addCoinCount;
    private String totalCoinCount;
    private int curLevel;
    private String totalPointCount;
    private String addPointCount;
    private boolean isLevelUp;
    private int continueCount;
    private int maxRewardTimes;

    public boolean isLevelUp() {
        return isLevelUp;
    }

    public void setIsLevelUp(boolean isLevelUp) {
        this.isLevelUp = isLevelUp;
    }

    public void setAddCoinCount(String addCoinCount) {
        this.addCoinCount = addCoinCount;
    }

    public void setTotalCoinCount(String totalCoinCount) {
        this.totalCoinCount = totalCoinCount;
    }

    public void setCurLevel(int curLevel) {
        this.curLevel = curLevel;
    }

    public void setTotalPointCount(String totalPointCount) {
        this.totalPointCount = totalPointCount;
    }

    public void setAddPointCount(String addPointCount) {
        this.addPointCount = addPointCount;
    }

    public void setContinueCount(int continueCount) {
        this.continueCount = continueCount;
    }

    public void setMaxRewardTimes(int maxRewardTimes) {
        this.maxRewardTimes = maxRewardTimes;
    }

    public String getAddCoinCount() {
        return addCoinCount;
    }

    public String getTotalCoinCount() {
        return totalCoinCount;
    }

    public int getCurLevel() {
        return curLevel;
    }

    public String getTotalPointCount() {
        return totalPointCount;
    }

    public String getAddPointCount() {
        return addPointCount;
    }

    public int getContinueCount() {
        return continueCount;
    }

    public int getMaxRewardTimes() {
        return maxRewardTimes;
    }
}
