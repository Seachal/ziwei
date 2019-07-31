//
// Created by luwies on 16/7/26.
//
#include <jni.h>
#include <android/log.h>
#include <stdlib.h>
#include "include/libyuv.h"
#include "com_laka_live_libyuv_LibYuv.h"

#define TAG    "libyuv" // 这个是自定义的LOG的标识
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,TAG,__VA_ARGS__) // 定义LOGD类型

void Java_com_laka_live_libyuv_LibYuv_convertNV21ToARGB(JNIEnv *env, jclass type, jbyteArray yuv_,
                                                        jbyteArray rgb_, jint width, jint height) {
    uint8 *yuv = (uint8 *) env->GetByteArrayElements(yuv_, NULL);
    uint8 *rgb = (uint8 *) env->GetByteArrayElements(rgb_, NULL);

    int size = width * height;
    uint8 *uv = yuv + size;
    int dst_vu_stride = (width + 1) / 2 * 2;
    libyuv::NV21ToARGB(yuv, width, uv, dst_vu_stride, rgb, width * 4, width, height);

    env->ReleaseByteArrayElements(yuv_, (jbyte *) yuv, 0);
    env->ReleaseByteArrayElements(rgb_, (jbyte *) rgb, 0);
}

void Java_com_laka_live_libyuv_LibYuv_convertARGBToNV21(JNIEnv *env, jclass instance,
                                                        jbyteArray yuv_,
                                                        jbyteArray rgb_, jint width, jint height) {
    uint8 *yuv = (uint8 *) env->GetByteArrayElements(yuv_, NULL);
    uint8 *rgb = (uint8 *) env->GetByteArrayElements(rgb_, NULL);

    int dst_y_stride = width;
    uint8 *dst_vu = yuv + width * height;
    int dst_vu_stride = (width + 1) / 2 * 2;

    libyuv::ARGBToNV21(rgb, width * 4, yuv, dst_y_stride, dst_vu, dst_vu_stride, width, height);

    env->ReleaseByteArrayElements(yuv_, (jbyte *) yuv, 0);
    env->ReleaseByteArrayElements(rgb_, (jbyte *) rgb, 0);
}

void Java_com_laka_live_libyuv_LibYuv_ARGBMirror(JNIEnv *env, jclass type, jbyteArray src_,
                                                 jbyteArray dst_, jint width, jint height) {
    uint8 *src = (uint8 *) env->GetByteArrayElements(src_, NULL);
    uint8 *dst = (uint8 *) env->GetByteArrayElements(dst_, NULL);

    int stride = width * 4;
//    libyuv::ARGBMirror(src, stride, dst, stride, width, height);
    libyuv::ARGBScale(src, stride, width, -height, dst, stride, width, height, libyuv::kFilterNone);
    env->ReleaseByteArrayElements(src_, (jbyte *) src, 0);
    env->ReleaseByteArrayElements(dst_, (jbyte *) dst, 0);
}

void Java_com_laka_live_libyuv_LibYuv_ARGBRotate(JNIEnv *env, jclass type, jbyteArray src_,
                                                 jbyteArray dst_, jint width, jint height) {
    uint8 *src = (uint8 *) env->GetByteArrayElements(src_, NULL);
    uint8 *dst = (uint8 *) env->GetByteArrayElements(dst_, NULL);

    int stride = width * 4;
    libyuv::ARGBRotate(src, stride, dst, stride, width, height, libyuv::kRotate180);
    env->ReleaseByteArrayElements(src_, (jbyte *) src, 0);
    env->ReleaseByteArrayElements(dst_, (jbyte *) dst, 0);
}