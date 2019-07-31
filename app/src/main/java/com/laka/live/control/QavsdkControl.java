package com.laka.live.control;

import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.text.TextUtils;

import com.laka.live.application.LiveApplication;
import com.laka.live.util.HardwareUtil;
import com.laka.live.util.Log;
import com.laka.live.util.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ios on 16/7/8.
 */
public class QavsdkControl {
    private static final String TAG = "QavsdkControl";
    //是否开启美颜
    boolean isEnableBeauty = true;
    //是否前置摄像头
    boolean isFrontCamera = true;

    //是否开启麦克风
    boolean isOpenMic = true;

    //获取当前摄像头
    public boolean getIsFrontCamera() {
        return isFrontCamera;
    }

    public void setIsFrontCamera(boolean mFrontCamera) {
        isFrontCamera = mFrontCamera;
    }

    public boolean isOpenMic() {
        return isOpenMic;
    }

    public void setOpenMic(boolean openMic) {
        isOpenMic = openMic;
    }

    //获取是否开启美颜
    public boolean isEnableBeauty() {
        return isEnableBeauty;
    }


    public void setEnableBeauty(boolean isEnableBeauty) {
        this.isEnableBeauty = isEnableBeauty;
    }


    public static Object getField(Object instance, String fieldName) {
        if (instance == null || TextUtils.isEmpty(fieldName)) {
            return null;
        }
        try {
            Class classes = instance.getClass();
            Field txLivePublisherField = classes.getDeclaredField(fieldName);
            txLivePublisherField.setAccessible(true);
            return txLivePublisherField.get(instance);
        } catch (NoSuchFieldException e) {
            Log.e("QavsdkControl", "Exception", e);
        } catch (IllegalAccessException e) {
            Log.e("QavsdkControl", "Exception", e);
        }
        return null;
    }


    private static String getAutoSceneMode(Camera.Parameters parameters) {
        if (parameters != null) {
            List<String> modeList = parameters.getSupportedSceneModes();
            if (isSupported(modeList, Camera.Parameters.SCENE_MODE_AUTO)) {
                return Camera.Parameters.SCENE_MODE_AUTO;
            }
        }
        return null;
    }

    private static String getAutoFocusMode(Camera.Parameters parameters) {
        if (parameters != null) {
            //set continue auto focus, make video focus
            List<String> focusModes = parameters.getSupportedFocusModes();
            if (HardwareUtil.isDevice("GT-I950", "SCH-I959", "MEIZU MX3") && isSupported(focusModes, "continuous-picture")) {
                return "continuous-picture";
            } else if (isSupported(focusModes, "continuous-video")) {
                return "continuous-video";
            } else if (isSupported(focusModes, "auto")) {
                return "auto";
            }
        }
        return null;
    }

    private static boolean isSupported(List<String> list, String key) {
        return list != null && list.contains(key);
    }

    private static void startFaceDetection(Camera camera) {
        Camera.Parameters params = null;
        try {
            params = camera.getParameters();
        } catch (Exception e) {
            return;
        } catch (Error error){
            return;
        }

        if (params != null && params.getMaxNumDetectedFaces() > 0) {
            camera.setFaceDetectionListener(new Camera.FaceDetectionListener() {
                @Override
                public void onFaceDetection(Camera.Face[] faces, Camera camera) {
                    if (camera == null) {
                        return;
                    }
                    Camera.Parameters params;
                    try {
                        params = camera.getParameters();
                    } catch (Exception e) {
                        Log.d(TAG, "camera.getParameters()报错");
                        return;
                    }catch (Error error){
                        return;
                    }

                    if (params == null) {
                        return;
                    }
                    if (faces != null && faces.length > 0) {
                        if (params != null && params.getMaxNumMeteringAreas() > 0) {
                            ArrayList e = new ArrayList();
                            for (Camera.Face face : faces) {
                                if (face != null) {
                                    e.add(new Camera.Area(face.rect, 100));//1000
                                }
                            }
                            params.setMeteringAreas(e);
                        }
                    }
                }
            });
            try {
                camera.startFaceDetection();
            } catch (Exception e) {
                Log.d(TAG, "camera.startFaceDetection()报错");
            }

        }
    }
}
