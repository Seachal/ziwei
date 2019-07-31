package com.laka.live.util;

import android.os.Bundle;

import com.baidu.location.BDLocation;

/**
 * Created by ios on 16/7/8.
 */
public class LakaUtil {

    /**
     * 获取百度定位返回的地址（直接返回城市名称如：广州）
     *
     * @param poi
     * @return
     */
//    public static String getShortPlace(BDLocation poi) {
//        if (poi == null) {
//            return "";
//        }
//        String city = "";
//        city = poi.getCity();
//        if (!Utils.isEmpty(city)) {
//            if (city.contains("省")) {
//                city = city.replace("省", "");
//            }
//            if (city.contains("市")) {
//                city = city.replace("市", "");
//            }
//        }
//        return city;
//    }

    /**
     * 获取百度定位返回的地址（直接返回城市名称如：广东省广州市）
     *
     * @param poi
     * @return
     */
    public static String getFullPlace(BDLocation poi) {
        if (poi == null) {
            return "";
        }
        String province = poi.getProvince();
        if (!Utils.isEmpty(province) && province.contains("省")) {
            province = province.replace("省", " ");
        }else{
            province="";
        }
        String city = poi.getCity();
        if(Utils.isEmpty(city)){
            city="";
        }
        return province+ city;
    }

    //公用打印辅助函数
//    public static String getNetStatusString(Bundle status) {
//        String str = String.format("%-14s %-14s %-14s\n%-14s %-14s %-14s\n%-14s %-14s %-14s\n%-14s",
//                "CPU:"+status.getString(TXLiveConstants.NET_STATUS_CPU_USAGE),
//                "RES:"+status.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH)+"*"+status.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT),
//                "SPD:"+status.getInt(TXLiveConstants.NET_STATUS_NET_SPEED)+"Kbps",
//                "JIT:"+status.getInt(TXLiveConstants.NET_STATUS_NET_JITTER),
//                "CAS:"+status.getInt(TXLiveConstants.NET_STATUS_CACHE_SIZE),
//                "FPS:"+status.getInt(TXLiveConstants.NET_STATUS_VIDEO_FPS),
//                "ARA:"+status.getInt(TXLiveConstants.NET_STATUS_AUDIO_BITRATE)+"Kbps",
//                "QUE:"+status.getInt(TXLiveConstants.NET_STATUS_CODEC_CACHE)+"|"+status.getInt(TXLiveConstants.NET_STATUS_CACHE_SIZE),
//                "DRP:"+status.getInt(TXLiveConstants.NET_STATUS_CODEC_DROP_CNT)+"|"+status.getInt(TXLiveConstants.NET_STATUS_DROP_SIZE),
//                "VRA:"+status.getInt(TXLiveConstants.NET_STATUS_VIDEO_BITRATE)+"Kbps",
//                "SVR:"+status.getString(TXLiveConstants.NET_STATUS_SERVER_IP));
//        return str;
//    }
}
