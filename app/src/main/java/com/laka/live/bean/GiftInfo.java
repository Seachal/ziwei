package com.laka.live.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

import java.io.Serializable;
import java.util.List;

/**
 * Created by luwies on 16/3/7.
 */
public class GiftInfo implements Serializable {

    private static String TAG = "GiftInfo";
    /**
     * 礼物唯一标识
     */
    @SerializedName(Common.ID)
    @Expose
    private int id;
    /**
     * 位置
     */
    @SerializedName(Common.POSITION)
    @Expose
    private int position;

    /**
     * 经验数
     */
    @SerializedName(Common.EXPS)
    @Expose
    private long experience;

    /**
     * 资源包编辑时间
     */
    @SerializedName(Common.EDIT_TIME)
    @Expose
    private String editTime;


    /**
     * 礼物所需金币
     */
    @SerializedName(Common.COINS)
    @Expose
    private double kazuan;
    /**
     * 本地图片资源
     */
//    private int iconRes;
    /**
     * 本地小动画资源
     */
//    private int animRes;
    /**
     * 本地图片资源名(SD卡)
     */
    private String iconFileName;
    /**
     * 本地图片动画数量
     */
    private int  animFileAmount;

    /**
     *  需要播放动画
     */
    private boolean startAnim;

    /**
     * 是否选中
     */
    private boolean isChoose;
    /**
     * 是否可连送
     */
    @SerializedName(Common.REPEAT_GIVE)
    @Expose
    private int isMulti;

    /**
     * 是否全屏动画
     */
    @SerializedName(Common.IS_FULL)
    @Expose
    private int isFull;

    /**
     * 是否有音效
     */
    @SerializedName(Common.IS_SOUND)
    @Expose
    private int isSound;


    /**
     * md5文件校验码
     */
    @SerializedName(Common.CODE)
    @Expose
    private String md5Code;

    /**
     * 静态图片url
     */
    @SerializedName(Common.IMAGE)
    @Expose
    private String image;

    /**
     * 礼物名字
     */
    @SerializedName(Common.NAME)
    @Expose
    private String name;
    /**
     * 礼物描述
     */
    @SerializedName(Common.DESCRIPTION)
    @Expose
    private String description;
    /**
     * 基础素材下载地址
     */
    @SerializedName(Common.URL)
    @Expose
    private String url;
    /**
     * 3种动画个数，key跟上面对应为a,b,c
     */
    @SerializedName(Common.NUM_IMAGE)
    @Expose
    private  ImageNum  imageNum;

    @SerializedName(Common.REPEAT_GIVE_INFO)
    @Expose
    private List<GiftInfo> specialGiveInfos;

    //特效礼物特有字段
    @SerializedName(Common.NUM)
    @Expose
    private int num;//触发特效连送数
    @SerializedName(Common.NOTE)
    @Expose
    private String note;
    @SerializedName(Common.EDITTIME_REPEAT_GIVE)
    @Expose
    private String editTimeRepeat;

    //本地图片
    private int imageRes;
    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public String getEditTimeRepeat() {
        return editTimeRepeat;
    }

    public void setEditTimeRepeat(String editTimeRepeat) {
        this.editTimeRepeat = editTimeRepeat;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<GiftInfo> getSpecialGiveInfos() {
        return specialGiveInfos;
    }

    public void setSpecialGiveInfos(List<GiftInfo> specialGiveInfos) {
        this.specialGiveInfos = specialGiveInfos;
    }



    class ImageNum{
        @SerializedName(Common.A)
        @Expose
        private int aNumber;
        @SerializedName(Common.B)
        @Expose
        private int bNumber;
        @SerializedName(Common.C)
        @Expose
        private int cNumber;
        public int getaNumber() {
            return aNumber;
        }

        public void setaNumber(int aNumber) {
            this.aNumber = aNumber;
        }

        public int getbNumber() {
            return bNumber;
        }

        public void setbNumber(int bNumber) {
            this.bNumber = bNumber;
        }

        public int getcNumber() {
            return cNumber;
        }

        public void setcNumber(int cNumber) {
            this.cNumber = cNumber;
        }
    }

    public int getIsFull() {
        return isFull;
    }

    public void setIsFull(int isFull) {
        this.isFull = isFull;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMd5Code() {
        return md5Code;
    }

    public void setMd5Code(String md5Code) {
        this.md5Code = md5Code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdStr() {
        return String.valueOf(id);
    }

    public boolean isMulti() {
        return isMulti==1;
    }

    public void setMulti(boolean multi) {
        isMulti = multi?1:0;
    }

    public boolean isFull() {
        return isFull==1||isFull==2;
    }

    public boolean isSound() {
        return isFull==1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getExperience() {
        return experience;
    }

    public void setExperience(long experience) {
        this.experience = experience;
    }

//    public int getIconRes() {
//        return iconRes;
//    }
//
//    public void setIconRes(int iconRes) {
//        this.iconRes = iconRes;
//    }


    public String getEditTime() {
        return editTime == null ? "" : editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public double getKazuan() {
        return kazuan;
    }

    public void setKazuan(double kazuan) {
        this.kazuan = kazuan;
    }

//    public int getAnimRes() {
//        return animRes;
//    }
//
//    public void setAnimRes(int animRes) {
//        this.animRes = animRes;
//    }

    public boolean isStartAnim() {
        return startAnim;
    }

    public void setStartAnim(boolean startAnim) {
        this.startAnim = startAnim;
    }

    public int getAnimFileAmount() {
        return animFileAmount;
    }

    public void setAnimFileAmount(int animFileAmount) {
        this.animFileAmount = animFileAmount;
    }

    public String getIconFileName() {
        return iconFileName;
    }

    public void setIconFileName(String iconFileName) {
        this.iconFileName = iconFileName;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
