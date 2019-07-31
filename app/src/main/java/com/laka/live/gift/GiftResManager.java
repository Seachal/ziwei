package com.laka.live.gift;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;

import com.coolerfall.download.DownloadCallback;
import com.coolerfall.download.DownloadRequest;
import com.coolerfall.download.Priority;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.laka.live.R;
import com.laka.live.application.LiveApplication;
import com.laka.live.bean.GiftInfo;
import com.laka.live.download.DownloadManager;
import com.laka.live.help.NetStateManager;
import com.laka.live.msg.QueryGiftsMsg;
import com.laka.live.network.DataProviderRoom;
import com.laka.live.network.GsonHttpConnection;
import com.laka.live.thread.BackgroundThread;
import com.laka.live.ui.widget.gift.GiftGridView;
import com.laka.live.util.Common;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.Md5Util;
import com.laka.live.util.ResourceHelper;
import com.laka.live.util.UiPreference;
import com.laka.live.util.Utils;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by guan on 16/9/19.
 */
public class GiftResManager {
    private static final String TAG = "GiftResManager";
    private static GiftResManager self;
    private static final int MAX_RETRY_TIME = 5;
    private static final int RETRY_INTERVAL = 2;
    private static final int PROGRESS_INTERVAL = 100;
    public static final String GIFT_PATH = "Laka/Gift";
    private static final String GIFT_DIR = LiveApplication.getInstance().getApplicationContext().getFilesDir()
            + File.separator + GIFT_PATH;

    /**
     * 应用内置大动画资源
     */
    HashSet<String> localBigGiftIds;
    HashMap<String, String> localBigAnimNames;
    /**
     * 应用内置小动画资源
     */
    HashSet<String> localSmaillGiftIds;
    HashMap<String, String> localSmallAnimNames;

    /**
     * 应用内置连击特效动画资源
     */
    HashSet<String> localSpecialGiftIds;
    HashMap<String, String> localSpecialAnimNames;

    /**
     * 应用内置音频资源
     */
    HashSet<String> localSoundGiftIds;
    HashMap<String, String> localSoundResNames;

    /**
     * 应用内置静态图片
     */
    HashSet<String> localImageGiftIds;
    HashMap<String, Integer> localImageRes;


    /**
     * 缓存最热礼物列表
     */
    private ArrayList<GiftInfo> mGiftList;


    public GiftResManager() {
        mGiftList = (ArrayList<GiftInfo>) UiPreference.getObject(Common.KEY_NEW_GIFT_LIST);
//        if (!Utils.listIsNullOrEmpty(mGiftList)) {
//            Log.d(TAG, " 反序列化礼物列表 =" + mGiftList.size());
//        } else {
//            Log.d(TAG, " 反序列化礼物列表 =null");
//
//        }
        mGiftList = new ArrayList<>();
        GiftInfo gift1 = new GiftInfo();
        gift1.setId(40);
        gift1.setName("奶油");
        gift1.setKazuan(0.1);
        gift1.setImageRes(R.drawable.live_present_cream);
        gift1.setDescription("一个");
        gift1.setMulti(true);
        mGiftList.add(gift1);
        GiftInfo gift2 = new GiftInfo();
        gift2.setId(41);
        gift2.setName("曲奇");
        gift2.setKazuan(0.2);
        gift2.setImageRes(R.drawable.live_present_cookie);
        gift2.setDescription("一个");
        gift2.setMulti(true);
        mGiftList.add(gift2);
        GiftInfo gift3 = new GiftInfo();
        gift3.setId(42);
        gift3.setName("甜甜圈");
        gift3.setKazuan(0.5);
        gift3.setImageRes(R.drawable.live_present_doughnut);
        gift3.setDescription("一个");
        gift3.setMulti(true);
        mGiftList.add(gift3);
        GiftInfo gift4 = new GiftInfo();
        gift4.setId(43);
        gift4.setName("蛋糕杯");
        gift4.setKazuan(1);
        gift4.setImageRes(R.drawable.live_present_cupcake);
        gift4.setDescription("一个");
        gift4.setMulti(true);
        mGiftList.add(gift4);
        GiftInfo gift5 = new GiftInfo();
        gift5.setId(44);
        gift5.setName("冰淇淋");
        gift5.setKazuan(1.8);
        gift5.setImageRes(R.drawable.live_present_icecream);
        gift5.setDescription("一个");
        gift5.setMulti(true);
        mGiftList.add(gift5);
        GiftInfo gift6 = new GiftInfo();
        gift6.setId(45);
        gift6.setName("蛋糕");
        gift6.setKazuan(2.8);
        gift6.setImageRes(R.drawable.live_present_piececake);
        gift6.setDescription("一个");
        gift6.setMulti(true);
        mGiftList.add(gift6);
        GiftInfo gift7 = new GiftInfo();
        gift7.setId(46);
        gift7.setName("三层蛋糕");
        gift7.setKazuan(5);
        gift7.setImageRes(R.drawable.live_present_cake);
        gift7.setDescription("一个");
        gift7.setMulti(false);
        if(LiveApplication.getInstance().isShowGiftAnim){
            gift7.setIsFull(1);
        }
        mGiftList.add(gift7);
        GiftInfo gift8 = new GiftInfo();
        gift8.setId(47);
        gift8.setName("小熊蛋糕");
        gift8.setKazuan(9.9);
        gift8.setImageRes(R.drawable.live_present_bear);
        gift8.setDescription("一个");
        gift8.setMulti(false);
        if(LiveApplication.getInstance().isShowGiftAnim) {
            gift8.setIsFull(1);
        }
        mGiftList.add(gift8);


        localSmaillGiftIds = new HashSet<>();
        localSmaillGiftIds.add("40");
        localSmaillGiftIds.add("41");
        localSmaillGiftIds.add("42");
        localSmaillGiftIds.add("43");
        localSmaillGiftIds.add("44");
        localSmaillGiftIds.add("45");
        //初始化应用内置资源  todo内置资源
        if(LiveApplication.getInstance().isShowGiftAnim) {
            localBigGiftIds = new HashSet<>();
            localBigGiftIds.add("46");
            localBigGiftIds.add("47");
        }else{
            localBigGiftIds = new HashSet<>();
            localSmaillGiftIds.add("46");
            localSmaillGiftIds.add("47");
        }

//        localBigGiftIds.add("39");
//        localBigAnimNames = new HashMap<>();
//        localBigAnimNames.put("28", "firework");
//        localBigAnimNames.put("29", "car");
//        localBigAnimNames.put("39", "god");

//        localSmallAnimNames = new HashMap<>();
//        localSmallAnimNames.put("25", "rose_s");
//        localSmallAnimNames.put("26", "momoda_s");
//        localSmallAnimNames.put("27", "yue_s");
//        localSmallAnimNames.put("33", "gua_s");
//        localSmallAnimNames.put("34", "motou_s");
//        localSmallAnimNames.put("35", "blue_s");
//        localSmallAnimNames.put("36", "ring_s");
//
//        localSpecialGiftIds = new HashSet<>();
//        localSpecialGiftIds.add("25_1");
//        localSpecialGiftIds.add("25_2");
//        localSpecialGiftIds.add("25_3");
//        localSpecialAnimNames = new HashMap<>();
//        localSpecialAnimNames.put("25_1", "xin");
//        localSpecialAnimNames.put("25_2", "huaqiang");
//        localSpecialAnimNames.put("25_3", "dest");
//
//        localSoundGiftIds = new HashSet<>();
//        localSoundGiftIds.add("39");
//        localSoundResNames = new HashMap<>();
//        localSoundResNames.put("39", "gifts_god");


    }

    public static GiftResManager getInstance() {
        if (self == null) {
            synchronized (GiftResManager.class) {
                if (self == null) {
                    self = new GiftResManager();
                }
            }
        }
        return self;
    }

    /**
     * 获取礼物
     */
    public ArrayList<GiftInfo> getGiftList() {

        // 因为这些礼物是静态保存的，所以，每次获取前，都要重置一下选中状态。
        // 不然就会因为遗留上次所选择的状态，导致BUG。
        if(Utils.isNotEmpty(mGiftList)) {
            mGiftList.get(0).setChoose(false);
        }

        return mGiftList;
    }


    /**
     * 判断本地礼物版本是否需要更新
     */
    public void checkGiftUpdate() {
        Log.d(TAG, "开始同步和下载礼物");

        //取消动态礼物
        if(true){
            return;
        }

        //动态礼物接口
        DataProviderRoom.queryGifts(LiveApplication.getInstance(), null, new GsonHttpConnection.OnResultListener<QueryGiftsMsg>() {
                    @Override
                    public void onSuccess(QueryGiftsMsg result) {
                        if (!result.isSuccessFul()) {
                            return;
                        }
                        ArrayList<GiftInfo> giftInfoList = (ArrayList<GiftInfo>) result.getList();

                        UiPreference.saveObject(Common.KEY_DYNC_GIFT_LIST, giftInfoList); //保存本地

                        if (Utils.listIsNullOrEmpty(giftInfoList)) {
                            Log.d(TAG, "queryGifts onFail");
                            return;
                        }

                        Log.d(TAG, "礼物列表=" + giftInfoList.size());
                        for (GiftInfo item : giftInfoList) {

                            if (Utils.listIsNullOrEmpty(mGiftList)) {
                                downloadGift(item, false, "");
                                continue;
                            }
                            checkGiftNeedDown(item);
                        }
                        mGiftList = giftInfoList;
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg, String command) {
                        Log.d(TAG, "queryGifts onFail1 errorCode=" + errorCode + " errorMsg=" + errorMsg);
                    }
                }

        );

    }

    private void checkGiftNeedDown(GiftInfo item) {
        for (GiftInfo oldItem :
                mGiftList) {
            if (!oldItem.getIdStr().equals(item.getIdStr())) {
                continue;
            }

            List<GiftInfo> specialGiveInfos = oldItem.getSpecialGiveInfos();
            if (!Utils.listIsNullOrEmpty(specialGiveInfos)) {
                Log.d(TAG, "有连送特效 id=" + oldItem.getId() + " 特效数=" + specialGiveInfos.size());

                for (GiftInfo gift :
                        specialGiveInfos) {
                    downloadGift(gift, false, oldItem.getIdStr());
                }
            }

            if (!oldItem.getEditTime().equals(item.getEditTime())) {
                Log.d(TAG, "资源包有更新强制下载 id=" + item.getEditTime());
                downloadGift(item, true, "");
            } else {
                downloadGift(item, false, "");
            }
            break;
        }
    }

    public void reDownloadGift(String giftID) {
        clearGiftRes(giftID);
        downloadGift(giftID);
    }

    private void clearGiftRes(String giftID) {
        try {
            //清除缓存
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            GiftInfo gift = getGift(giftID);
            imagePipeline.evictFromCache(Uri.parse(ImageUtil.LOCAL_IMAGE_URI_PREFIX + getSmallAnimPath(giftID)));
            if (gift.isFull()) {
                imagePipeline.evictFromCache(Uri.parse(ImageUtil.LOCAL_IMAGE_URI_PREFIX + getBigAnimPath(giftID)));
            }
            //清除sd卡文件
            boolean result = Utils.deleteFolder(GIFT_DIR + File.separator + giftID);
            Log.d(TAG, " clearGiftRes删除 result=" + result + " giftID=" + giftID);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            Log.d(TAG, "clearGiftRes 报错:"
                    + sw.toString());
        }
    }

    public void downloadGift(String giftID) {
        if (!Utils.listIsNullOrEmpty(mGiftList)) {
            for (GiftInfo item : mGiftList) {
                if (item.getIdStr().equals(giftID)) {
                    downloadGift(item, false, "");
                    break;
                }
            }
        }
    }

    public void downloadGift(String giftID, int count) {
        GiftInfo gift = getGift(giftID);
        if (gift == null) {
            return;
        }
        List<GiftInfo> specialInfos = gift.getSpecialGiveInfos();
        if (!Utils.listIsNullOrEmpty(specialInfos)) {
            for (GiftInfo item :
                    specialInfos) {
                if (item.getNum() == count) {
                    downloadGift(item, false, giftID);
                }
            }
        }
    }

    //删除文件夹和文件夹里面的文件
//    public static void deleteDir(String path) {
//        File dir = new File(path);
//        if (dir == null || !dir.exists() || !dir.isDirectory())
//            return;
//
//        for (File file : dir.listFiles()) {
//            if (file.isFile())
//                file.delete(); // 删除所有文件
//            else if (file.isDirectory())
//                deleteDir(path); // 递规的方式删除文件夹
//        }
//        dir.delete();// 删除目录本身
//    }

    /**
     * 下载礼物版本
     */
    public void downloadGift(final GiftInfo item, final boolean isForceDown, final String parentId) {
        //3G不下载
        if (NetStateManager.getInstance().getIs3G(LiveApplication.getInstance().getApplicationContext())) {
            Log.d(TAG, "当前是3g不下载礼物资源");
            return;
        }

        if (isForceDown) {
            clearGiftRes(item.getIdStr());
            Log.d(TAG, "时间显示资源更新,强制下载");
        } else {
            if (Utils.isEmpty(parentId)) {
                //判断是否需要下载
                if (item.isFull()) {
                    if (!Utils.isEmpty(getBigAnimPath(item.getIdStr()))) {  //id对应的全屏礼物存在不下载
                        Log.d(TAG, " id=" + item.getIdStr() + " name=" + item.getName() + " 对应的全屏或半屏礼物存在不下载");
                        return;
                    }
                } else {
                    if (!Utils.isEmpty(getSmallAnimPath(item.getIdStr()))) {  //id对应的小动态全屏礼物存在不下载
                        Log.d(TAG, " id=" + item.getIdStr() + " name=" + item.getName() + " 对应的小动态礼物存在不下载");
                        return;
                    }
                }
            } else {
                //判断特效动画是否存在
                if (!Utils.isEmpty(getSpecialAnimPath(item.getNum(), parentId))) {
                    Log.d(TAG, " id=" + item.getIdStr() + " name=" + item.getNote() + " 对应的特效动画礼物存在不下载");
                    return;
                }
            }
        }

        if (Utils.isEmpty(item.getUrl())) {
            Log.d(TAG, "下载地址不存在");
            return;
        }

        if (DownloadManager.getInstance().isDownloading(item.getUrl())) {
            Log.d(TAG, " 下载中不重复添加任务 item=" + item.getName());
            return;
        } else {
            Log.d(TAG, " 开始下载礼物" + item.getName() + item.getNote() + " url=" + item.getUrl());
        }

        DownloadRequest request = new DownloadRequest.Builder()
                .url(item.getUrl())
                .retryTime(MAX_RETRY_TIME)
                .retryInterval(RETRY_INTERVAL, TimeUnit.SECONDS)
                .progressInterval(PROGRESS_INTERVAL, TimeUnit.MILLISECONDS)
                .priority(Priority.HIGH)
                .allowedNetworkTypes(DownloadRequest.NETWORK_WIFI | DownloadRequest.NETWORK_MOBILE)
                .destinationDirectory(GIFT_DIR)
                .downloadCallback(new DownloadCallback() {
                    @Override
                    public void onFailure(int downloadId, int statusCode, String errMsg) {
                        super.onFailure(downloadId, statusCode, errMsg);
                        Log.d(TAG, " onFailure errMsg=" + errMsg);
                    }

                    @Override
                    public void onProgress(int downloadId, long bytesWritten, long totalBytes) {
                        super.onProgress(downloadId, bytesWritten, totalBytes);
                        Log.d(TAG, " onProgress downloadId=" + downloadId + " percent=" + (float) bytesWritten / (float) totalBytes);
                    }

                    @Override
                    public void onRetry(int downloadId) {
                        super.onRetry(downloadId);
//                        Log.d(TAG, " onRetry downloadId="+downloadId);
                    }

                    @Override
                    public void onStart(int downloadId, long totalBytes) {
                        super.onStart(downloadId, totalBytes);
                        Log.d(TAG, " onStart downloadId=" + downloadId);
                    }

                    @Override
                    public void onSuccess(final int downloadId, final String filePath) {
                        super.onSuccess(downloadId, filePath);
                        Log.d(TAG, " onSuccess downloadId=" + downloadId + " filePath=" + filePath);

                        //解压校验
                        BackgroundThread.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "开始解压");
                                try {
                                    File musicDir;
                                    if (!Utils.isEmpty(parentId)) {
                                        musicDir = new File(GIFT_DIR + File.separator + parentId + "_" + item.getId());//用id命名
                                    } else {
                                        musicDir = new File(GIFT_DIR + File.separator + item.getId());//用id命名
                                    }

                                    musicDir.delete();
                                    if (musicDir.exists() == false) {
                                        musicDir.mkdirs();
                                    }

                                    //校验md5
                                    String curMd5 = Md5Util.md5sum(filePath);
                                    String needMd5 = item.getMd5Code();
                                    if (curMd5.equalsIgnoreCase(needMd5)) {
                                        Log.d(TAG, "md5校验成功 ");
                                    } else {
                                        Log.d(TAG, "md5校验失败 重新下载 needMd5=" + needMd5 + " curMdt=" + curMd5);
                                        return;
                                    }
                                    Utils.unZipFolder(filePath, musicDir.getAbsolutePath());
                                    Log.d(TAG, "解压成功 id=" + item.getId() + " parentId=" + parentId + " path=" + musicDir.getAbsolutePath());
                                } catch (Exception e) {
                                    StringWriter sw = new StringWriter();
                                    e.printStackTrace(new PrintWriter(sw, true));
                                    Log.d(TAG, "解压礼物失败2报错:"
                                            + sw.toString());
                                } finally {
                                    File zip = new File(filePath);
                                    zip.delete();
                                }
                            }
                        });
                    }
                })
                .build();
        int downloadId = DownloadManager.getInstance().add(request);
        if (downloadId != -1) {
//            Log.d(TAG, " 礼物已经在下载队列里");
        } else {
//            Log.d(TAG, " 礼物未加入下载队列");
        }
    }


    public String getSmallAnimPath(String giftId) {
        if (localSmaillGiftIds.contains(giftId)) {
            String path = "giftAnims/"+giftId+"/data.json";
//            String path = "anim"+giftId+".json";
            Log.d(TAG, "内置小动画 path=" + path);
            return path;
        } else {
            return "";
        }
//        if (localSmaillGiftIds.contains(giftId)) {
//            String path = "giftAnims/anim_" + localSmallAnimNames.get(giftId) + ".webp";
//            Log.d(TAG, "内置小动画 path=" + path);
//            return path;
//        } else {
//            String path = GIFT_DIR + File.separator + giftId + File.separator + "b1.webp";
//            File file = new File(path);
//            if (file.exists()) {
//                Log.d(TAG, " small动画文件存在 giftId=" + giftId + " path=" + path);
//                return path;
//            } else {
//                Log.d(TAG, " small动画文件不存在 giftId=" + giftId + " path=" + path);
//                return "";
//            }
//        }
    }


    public String getBigAnimPath(String giftId) {
        if (localBigGiftIds.contains(giftId)) {
            String path = "giftAnims/"+giftId+"/data.json";
            Log.d(TAG, "内置大动画 path=" + path);
            return path;
        } else {
            return "";
        }
//        if (localBigGiftIds.contains(giftId)) {
//            String path = "giftAnims/anim_" + localBigAnimNames.get(giftId) + ".webp";
//            Log.d(TAG, "内置大动画 path=" + path);
//            return path;
//        } else {
//            String path = GIFT_DIR + File.separator + giftId + File.separator + "c1" + ".webp";
//            File file = new File(path);
//            if (file.exists()) {
//                Log.d(TAG, "sd卡大动画文件存在 giftId=" + giftId + " path=" + path);
//                return path;
//            } else {
//                Log.d(TAG, "sd卡大动画文件不存在 giftId=" + giftId + " path=" + path);
//                return "";
//            }
//        }
    }

    public String getSpecialAnimPath(int num, String parentId) {
        int id = 0;
        GiftInfo gift = getGift(parentId);
        List<GiftInfo> gifts = gift.getSpecialGiveInfos();
        for (GiftInfo item :
                gifts) {
            if (item.getNum() == num) {
                id = item.getId();
                break;
            }
        }
        if (localSpecialGiftIds.contains(parentId + "_" + id)) {
            String path = "specialAnims/" + localSpecialAnimNames.get(parentId + "_" + id) + ".webp";
            Log.d(TAG, "内置连送特效动画 path=" + path);
            return path;
        } else {
            String path = GIFT_DIR + File.separator + parentId + "_" + id + File.separator + "c1" + ".webp";
            File file = new File(path);
            if (file.exists()) {
                Log.d(TAG, "sd卡特效动画文件存在 giftId=" + parentId + " id" + id + " path=" + path);
                return path;
            } else {
                Log.d(TAG, "sd卡特效动画文件不存在 giftId=" + parentId + " id" + id + " path=" + path);
                return "";
            }
        }
    }

    public String getSoundPath(String giftId) {
        if (localSoundGiftIds.contains(giftId)) {
            String path = "sound/" + localSoundResNames.get(giftId) + ".mp3";
            Log.d(TAG, "内置音频 path=" + path);
            return path;
        } else {
            String path = GIFT_DIR + File.separator + giftId + File.separator + "sound.mp3";
            File file = new File(path);
            if (file.exists()) {
                Log.d(TAG, "sd卡音频文件存在 giftId=" + giftId + " path=" + path);
                return path;
            } else {
                Log.d(TAG, "sd卡音频文件不存在 giftId=" + giftId + " path=" + path);
                return "";
            }
        }
    }

    public boolean checkIsResReady(GiftInfo gift) {
        Log.d(TAG, " checkIsResReady isFull=" + gift.isFull());
        if (gift.isFull()) {
            if (Utils.isEmpty(getBigAnimPath(gift.getIdStr()))) {
                Log.d(TAG, "本地不存在,需要下载");
                downloadGift(gift.getIdStr());
                return false;
            }
        } else {
            Log.d(TAG, "不是全屏礼物,不需要下载");
        }
        return true;
    }


    public void stopDownloadGift() {
        Log.d(TAG, " 停止下载礼物");
        DownloadManager.getInstance().cancelAll();
    }


    public static String getReceiveGiftContent(String giftID) {
        return ResourceHelper.getString(R.string.send) + getUnitByGiftId(giftID + "") + getNameByGiftId(giftID + "");
    }

    public static String getUnitByGiftId(String giftID) {
        if (Utils.isEmpty(giftID)) {
            return "";
        }
        if(Utils.isEmpty(GiftResManager.getInstance().getGiftList())){
            return "";
        }
        for (GiftInfo gift :
                GiftResManager.getInstance().getGiftList()) {
            if (gift.getIdStr().equals(giftID)) {
                return gift.getDescription();
            }
        }
        return ResourceHelper.getString(R.string.one_ge);
    }

    public static String getNameByGiftId(String giftID) {
        if (Utils.isEmpty(giftID)) {
            return "";
        }
        if(Utils.isEmpty(GiftResManager.getInstance().getGiftList())){
            return "";
        }

        for (GiftInfo gift :
                GiftResManager.getInstance().getGiftList()) {
            if (gift.getIdStr().equals(giftID)) {
                return gift.getName();
            }
        }
        return ResourceHelper.getString(R.string.mystery_gift);
    }


    public static int getChatResByGiftId(String giftID) {
        if (Utils.isEmpty(giftID)) {
            return 0;
        }
        return 0;
    }

    public static String getImageResByGiftId(String giftID) {
        if (Utils.isEmpty(giftID)) {
            return "";
        }

        if(Utils.isEmpty(GiftResManager.getInstance().getGiftList())){
            return "";
        }

        for (GiftInfo gift :
                GiftResManager.getInstance().getGiftList()) {
            if (gift.getIdStr().equals(giftID)) {
                return gift.getImage();
            }
        }

        return "";
    }

    public static boolean isMultiGift(String giftId) {
        ArrayList<GiftInfo> gifts = GiftResManager.getInstance().getGiftList();
        for (GiftInfo gift :
                gifts) {
            if (gift.getIdStr().equals(giftId)) {
                return gift.isMulti();
            }
        }
        return false;
    }

    public GiftInfo getGift(String giftID) {
        if (!Utils.listIsNullOrEmpty(mGiftList)) {
            for (GiftInfo gift :
                    mGiftList) {
                if (giftID.equals(gift.getIdStr())) {
                    return gift;
                }
            }
        }
        return null;
    }


    public boolean isFullAnimGift(String giftID) {
        GiftInfo gift = getGift(giftID);
        if (gift != null && (gift.isFull())) {//|| !Utils.listIsNullOrEmpty(gift.getSpecialGiveInfos())
            return true;
        }
        return false;
    }

    public boolean isSpecialAnimGift(String giftID) {
        GiftInfo gift = getGift(giftID);
        if (gift != null && !Utils.listIsNullOrEmpty(gift.getSpecialGiveInfos())) {
            return true;
        }
        return false;
    }

    public boolean isSpecailAnimAmount(String giftID, int count) {
        GiftInfo gift = getGift(giftID);
        List<GiftInfo> gifts = gift.getSpecialGiveInfos();
        if (!Utils.listIsNullOrEmpty(gifts)) {
            for (GiftInfo item :
                    gifts) {
                if (item.getNum() == count) {
                    return true;
                }
            }
        }

        return false;
    }

    public String getSpecialSendGiftStr(String giftID, int count) {
        if (Utils.isEmpty(giftID)) {
            return "";
        }

        if (isSpecialAnimGift(giftID)) {
            GiftInfo gift = getGift(giftID);
            List<GiftInfo> gifts = gift.getSpecialGiveInfos();
            if (!Utils.listIsNullOrEmpty(gifts)) {
                for (GiftInfo item :
                        gifts) {
                    if (item.getNum() == count) {
                        return "送出了" + item.getNote();
                    }
                }
            }
        }
        return "送出了" + GiftResManager.getInstance().getUnitByGiftId(giftID) + GiftResManager.getInstance().getNameByGiftId(giftID);
    }


}
