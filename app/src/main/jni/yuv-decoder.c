#include <jni.h>
#include <android/log.h>
#include <math.h>
#include <stdlib.h>

#define TAG    "yuv-decoder" // 这个是自定义的LOG的标识
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,TAG,__VA_ARGS__) // 定义LOGD类型

inline int32_t min(int32_t a, int32_t b) {
    return a < b ? a : b;
}

inline int32_t max(int32_t a, int32_t b) {
    return a > b ? a : b;
}

JNIEXPORT void JNICALL Java_com_laka_live_gpuimage_YUVNativeLibrary_YUVtoRBGA(JNIEnv *env,
                                                                              jobject obj,
                                                                              jbyteArray yuv420sp,
                                                                              jint width,
                                                                              jint height,
                                                                              jintArray rgbOut) {
    int             sz;
    int             i;
    int             j;
    int             Y;
    int             Cr = 0;
    int             Cb = 0;
    int             pixPtr = 0;
    int             jDiv2 = 0;
    int             R = 0;
    int             G = 0;
    int             B = 0;
    int             cOff;
    int w = width;
    int h = height;
    sz = w * h;

    int vStart = sz + (sz >> 2);

    jint *rgbData = (jint *) ((*env)->GetPrimitiveArrayCritical(env, rgbOut, 0));
    jbyte *yuv = (jbyte *) (*env)->GetPrimitiveArrayCritical(env, yuv420sp, 0);

    int index;
    int halfW = w >> 1;

    for (j = 0; j < h; j++) {
        pixPtr = j * w;
        jDiv2 = j >> 1;
        for(i = 0; i < w; i++) {
            Y = yuv[pixPtr];
            if(Y < 0) Y += 255;
            if((i & 0x1) != 1) {
                index = jDiv2 * halfW + (i >> 1);
                cOff = sz + index;
                Cb = yuv[cOff];
                if(Cb < 0) Cb += 127; else Cb -= 128;
                Cr = yuv[vStart + index];
                if(Cr < 0) Cr += 127; else Cr -= 128;
            }


            //ITU-R BT.601 conversion
            //
            //R = 1.164*(Y-16) + 2.018*(Cr-128);
            //G = 1.164*(Y-16) - 0.813*(Cb-128) - 0.391*(Cr-128);
            //B = 1.164*(Y-16) + 1.596*(Cb-128);
            //
            Y = Y + (Y >> 3) + (Y >> 5) + (Y >> 7);
            R = Y + (Cr << 1) + (Cr >> 6);

            if (R < 0) R = 0; else if (R > 255) R = 255;
//            min(255, max(0, R));
            G = Y - Cb + (Cb >> 3) + (Cb >> 4) - (Cr >> 1) + (Cr >> 3);
            if(G < 0) G = 0; else if(G > 255) G = 255;
//            min(255, max(0, G));
            B = Y + Cb + (Cb >> 1) + (Cb >> 4) + (Cb >> 5);
            if(B < 0) B = 0; else if(B > 255) B = 255;
//            min(255, max(0, B));
            rgbData[pixPtr++] = 0xff000000 + (B << 16) + (G << 8) + R;
        }
    }

    (*env)->ReleasePrimitiveArrayCritical(env, rgbOut, rgbData, 0);
    (*env)->ReleasePrimitiveArrayCritical(env, yuv420sp, yuv, 0);
}

JNIEXPORT void JNICALL Java_com_laka_live_gpuimage_YUVNativeLibrary_YUVtoARBG(JNIEnv *env,
                                                                              jobject obj,
                                                                              jbyteArray yuv420sp,
                                                                              jint width,
                                                                              jint height,
                                                                              jintArray rgbOut) {
    int sz;
    int i;
    int j;
    int Y;
    int Cr = 0;
    int Cb = 0;
    int pixPtr = 0;
    int jDiv2 = 0;
    int R = 0;
    int G = 0;
    int B = 0;
    int cOff;
    int w = width;
    int h = height;
    sz = w * h;

    jint *rgbData = (jint *) ((*env)->GetPrimitiveArrayCritical(env, rgbOut, 0));
    jbyte *yuv = (jbyte *) (*env)->GetPrimitiveArrayCritical(env, yuv420sp, 0);

    for (j = 0; j < h; j++) {
        pixPtr = j * w;
        jDiv2 = j >> 1;
        for (i = 0; i < w; i++) {
            Y = yuv[pixPtr];
            if (Y < 0) Y += 255;
            if ((i & 0x1) != 1) {
                cOff = sz + jDiv2 * w + (i >> 1) * 2;
                Cb = yuv[cOff];
                if (Cb < 0) Cb += 127; else Cb -= 128;
                Cr = yuv[cOff + 1];
                if (Cr < 0) Cr += 127; else Cr -= 128;
            }

            //ITU-R BT.601 conversion
            //
            //R = 1.164*(Y-16) + 2.018*(Cr-128);
            //G = 1.164*(Y-16) - 0.813*(Cb-128) - 0.391*(Cr-128);
            //B = 1.164*(Y-16) + 1.596*(Cb-128);
            //
            Y = Y + (Y >> 3) + (Y >> 5) + (Y >> 7);
            R = Y + (Cr << 1) + (Cr >> 6);
            if (R < 0) R = 0; else if (R > 255) R = 255;
            G = Y - Cb + (Cb >> 3) + (Cb >> 4) - (Cr >> 1) + (Cr >> 3);
            if (G < 0) G = 0; else if (G > 255) G = 255;
            B = Y + Cb + (Cb >> 1) + (Cb >> 4) + (Cb >> 5);
            if (B < 0) B = 0; else if (B > 255) B = 255;
            rgbData[pixPtr++] = 0xff000000 + (B << 16) + (G << 8) + R;
        }
    }

    (*env)->ReleasePrimitiveArrayCritical(env, rgbOut, rgbData, 0);
    (*env)->ReleasePrimitiveArrayCritical(env, yuv420sp, yuv, 0);
}


void RGBtoYUV(int rgb, int isConvertUV, jbyte *y, jbyte *u, jbyte *v) {
    int32_t Y, U, V, R, G, B;

    B = 0x000000FF & rgb;
    G = (0x0000FF00 & rgb) >> 8;
    R = (0x00FF0000 & rgb) >> 16;

    Y = (int32_t) (3 * R / 10 + 587 * G / 1000 + 114 * B / 1000);
    min(255, max(0, Y));
    *y = Y;
    if (isConvertUV) {
        U = (int32_t) ((B - Y) * 113 / 200 + 128);
        V = (int32_t) ((R - Y) * 713 / 1000 + 128);
        U = min(255, max(0, U));
        V = min(255, max(0, V));
        *u = U;
        *v = V;
    }


}

JNIEXPORT void JNICALL Java_com_laka_live_gpuimage_YUVNativeLibrary_ARBGtoYUV(JNIEnv *env,
                                                                              jobject obj,
                                                                              jintArray argb,
                                                                              jint width,
                                                                              jint height,
                                                                              jbyteArray yuvOut) {
    int sz;
    int w = width;
    int h = height;
    sz = w * h;

    jint *rgbData = (jint *) ((*env)->GetPrimitiveArrayCritical(env, argb, 0));
    jbyte *yuv = (jbyte *) (*env)->GetPrimitiveArrayCritical(env, yuvOut, 0);

    int halfW = w >> 1;
    int halfH = h >> 1;
    int vStart = sz + (sz >> 2);
    int i, j;
    for (j = 0; j < halfH; ++j) {
        int index = (j << 1) * w;
        int uvIndex = j * halfW;
        for (i = 0; i < halfW; ++i) {
            int index1 = index + (i << 1);
            int rgb1 = rgbData[index1];

            RGBtoYUV(rgb1, JNI_FALSE, yuv + index1, yuv, yuv);

            int index2 = index1 + 1;
            int rgb2 = rgbData[index2];
            RGBtoYUV(rgb2, JNI_FALSE, yuv + index2, yuv, yuv);

            int index3 = index1 + w;
            int rgb3 = rgbData[index3];
            RGBtoYUV(rgb3, JNI_FALSE, yuv + index3, yuv, yuv);
            int index4 = index3 + 1;
            int rgb4 = rgbData[index4];

            int uv = uvIndex + i;
            RGBtoYUV(rgb4, JNI_TRUE, yuv + index4, yuv + sz + uv, yuv + vStart + uv);


            /*yuv[index1] = yuv1.Y;
            yuv[index2] = yuv2.Y;
            yuv[index3] = yuv3.Y;
            yuv[index4] = yuv4.Y;


            yuv[sz + uv] = yuv4.U;
            yuv[vStart + uv] = yuv4.V;*/
        }
    }

    (*env)->ReleasePrimitiveArrayCritical(env, argb, rgbData, 0);
    (*env)->ReleasePrimitiveArrayCritical(env, yuvOut, yuv, 0);
}

JNIEXPORT void JNICALL Java_com_laka_live_gpuimage_YUVNativeLibrary_greyYUV(JNIEnv *env,
                                                                            jobject obj,
                                                                            jbyteArray yuv420sp,
                                                                            jint width,
                                                                            jint height,
                                                                            jint size) {
    jbyte *yuv = (jbyte *) (*env)->GetPrimitiveArrayCritical(env, yuv420sp, 0);
    int w = width;
    int h = height;
    int start = w * h;
    int sz = size;

    int i = 0;
    for (i = start; i < sz; ++i) {
        yuv[i] = 128;
    }



    (*env)->ReleasePrimitiveArrayCritical(env, yuv420sp, yuv, 0);
}

/**
 * 色值反转
 */
JNIEXPORT void JNICALL Java_com_laka_live_gpuimage_YUVNativeLibrary_negateYUV(JNIEnv *env,
                                                                              jobject obj,
                                                                              jbyteArray yuv420sp,
                                                                              jint size) {
    jbyte *yuv = (jbyte *) (*env)->GetPrimitiveArrayCritical(env, yuv420sp, 0);
    int sz = size;

    int i = 0;
    for (i = 0; i < sz; ++i) {
        yuv[i] = ~yuv[i];
    }

    (*env)->ReleasePrimitiveArrayCritical(env, yuv420sp, yuv, 0);
}

/**
 * 调节亮度
 */
JNIEXPORT void JNICALL Java_com_laka_live_gpuimage_YUVNativeLibrary_brightnessYUV(JNIEnv *env,
                                                                                  jobject obj,
                                                                                  jbyteArray yuv,
                                                                                  jint w,
                                                                                  jint h,
                                                                                  jint light,
                                                                                  jint con) {
    if (light == 0 && con == 0) {
        return;
    }
    unsigned char *yuvData = (unsigned char *) (*env)->GetPrimitiveArrayCritical(env, yuv, 0);
    int width = w;
    int height = h;
    int32_t brightness = light;
    int32_t contrast = con;
    uint size = width * height;

    float c = (100 + contrast) / 100.0f;
    brightness += 128;

    uint8_t cTable[256]; //<nipper>
    int32_t i;
    for (i = 0; i < 256; i++) {
        cTable[i] = (uint8_t) max(0, min(255, (int32_t) ((i - 128) * c + brightness + 0.5f)));
    }
    for (i = 0; i < size; ++i) {
        yuvData[i] = cTable[yuvData[i]];
    }

    (*env)->ReleasePrimitiveArrayCritical(env, yuv, yuvData, 0);
}

/**
 * 调节对比度
 */
JNIEXPORT void JNICALL Java_com_laka_live_gpuimage_YUVNativeLibrary_contrastYUV(JNIEnv *env,
                                                                                jobject obj,
                                                                                jbyteArray yuv,
                                                                                jint w,
                                                                                jint h,
                                                                                jfloat jPercent) {
    if (jPercent == 0) {
        return;
    }
    unsigned char *yuvData = (unsigned char *) (*env)->GetPrimitiveArrayCritical(env, yuv, 0);
    uint width = w;
    uint height = h;
    float percent = jPercent;
    uint i = 0;
    int size = width * height;

    int average;
    int sum = 0;
    for (; i < size; i++) {
        sum += yuvData[i];
    }
    average = sum / size;
    int tmp;
    for (i = 0; i < size; i++) {
        tmp = yuvData[i];
        tmp = average + (tmp - average) * (1 + percent);
        if (tmp > UINT8_MAX) {
            tmp = UINT8_MAX;
        } else if (tmp < 0) {
            tmp = 0;
        }
        yuvData[i] = tmp;
    }

    (*env)->ReleasePrimitiveArrayCritical(env, yuv, yuvData, 0);
}

/**
 * 调节饱和度
 */
JNIEXPORT void JNICALL Java_com_laka_live_gpuimage_YUVNativeLibrary_saturationYUV(JNIEnv *env,
                                                                                  jobject obj,
                                                                                  jbyteArray yuv,
                                                                                  jint w,
                                                                                  jint h,
                                                                                  jint jSaturation) {
    /*if (jSaturation == 0) {
        return;
    }*/
    unsigned char *yuvData = (unsigned char *) (*env)->GetPrimitiveArrayCritical(env, yuv, 0);
    int width = w;
    int height = h;
    int saturation = jSaturation;
    int ySize = width * height;

    int size = ySize * 3 / 2;

    //from CxImage CxImage::Saturate
    uint8_t cTable[256];
    int32_t i;
    for (i = 0; i < 256; i++) {
        cTable[i] = (uint8_t) max(0, min(255, (int32_t) ((i - 128) * (100 + saturation) / 100 +
                                                         128)));
    }

    i = ySize;
    for (; i < size; ++i) {
        yuvData[i] = cTable[yuvData[i]];
    }
    (*env)->ReleasePrimitiveArrayCritical(env, yuv, yuvData, 0);
}

/**
 * 420SP图像合并
 */
JNIEXPORT void JNICALL Java_com_laka_live_gpuimage_YUVNativeLibrary_combineYUV420SP(JNIEnv *env,
                                                                                    jobject obj,
                                                                                    jbyteArray yuv420sp,
                                                                                    jbyteArray bgYuv420sp,
                                                                                    jint width,
                                                                                    jint height,
                                                                                    jint bgWidth,
                                                                                    jint bgHieght,
                                                                                    jint size,
                                                                                    jint bgSize,
                                                                                    jint left,
                                                                                    jint top) {
    jbyte *yuv = (jbyte *) (*env)->GetPrimitiveArrayCritical(env, yuv420sp, 0);
    jbyte *bgYuv = (jbyte *) (*env)->GetPrimitiveArrayCritical(env, bgYuv420sp, 0);
    int w = width;
    int h = height;
    int bgW = bgWidth;
    int bgH = bgHieght;
    int sz = size;
    int bgSz = bgSize;
    int l = left;
    int t = top;

    LOGD("w = %d", w);
    LOGD("h = %d", h);
    LOGD("l = %d", l);
    LOGD("t = %d", t);

    LOGD("bgW = %d", bgW);
    LOGD("bgH = %d", bgH);

    //Y的个数
    int ySize = w * h;
    int bgYSize = bgW * bgH;

    if (l < 0 || l >= bgW) {
        return;
    }

    if (t < 0 || t >= bgH) {
        return;
    }

    int i, j;
    int right = l + w;
    if (right > bgW) {
        right = bgW;
    }
    int bottom = t + h;
    if (bottom > bgH) {
        bottom = bgH;
    }


    LOGD("right = %d", right);
    LOGD("bottom = %d", bottom);


    int conbineWidth = right - l;
    int conbineHeight = bottom - t;

    int lines = conbineHeight / 2;

    int bgStart = l + t * bgWidth;
//    bgStart = 0;
    LOGD("bgStart : %d", bgStart);
    int start = 0;
    int uvStart = ySize;
    int bgUVStart = bgYSize + l + t / 2 * bgW;
    for (i = 0; i < lines; ++i) {
        memcpy(bgYuv + bgStart, yuv + start, conbineWidth);
        bgStart += bgW;
        start += w;
        memcpy(bgYuv + bgStart, yuv + start, conbineWidth);
        bgStart += bgW;
        start += w;

        memcpy(bgYuv + bgUVStart, yuv + uvStart, conbineWidth);
        bgUVStart += bgW;
        uvStart += w;


    }


    (*env)->ReleasePrimitiveArrayCritical(env, yuv420sp, yuv, 0);
    (*env)->ReleasePrimitiveArrayCritical(env, bgYuv420sp, bgYuv, 0);
}

/**
 * 420P图像合并
 */
JNIEXPORT void JNICALL Java_com_laka_live_gpuimage_YUVNativeLibrary_combineYUV420P(JNIEnv *env,
                                                                                   jobject obj,
                                                                                   jbyteArray yuv420p,
                                                                                   jbyteArray bgYuv420p,
                                                                                   jint width,
                                                                                   jint height,
                                                                                   jint bgWidth,
                                                                                   jint bgHieght,
                                                                                   jint size,
                                                                                   jint bgSize,
                                                                                   jint left,
                                                                                   jint top) {
    jbyte *yuv = (jbyte *) (*env)->GetPrimitiveArrayCritical(env, yuv420p, 0);
    jbyte *bgYuv = (jbyte *) (*env)->GetPrimitiveArrayCritical(env, bgYuv420p, 0);
    int w = width;
    int h = height;
    int bgW = bgWidth;
    int bgH = bgHieght;
    int sz = size;
    int bgSz = bgSize;
    int l = left;
    int t = top;

    LOGD("w = %d", w);
    LOGD("h = %d", h);
    LOGD("l = %d", l);
    LOGD("t = %d", t);

    LOGD("bgW = %d", bgW);
    LOGD("bgH = %d", bgH);

    //Y的个数
    int ySize = w * h;
    int bgYSize = bgW * bgH;

    if (l < 0 || l >= bgW) {
        return;
    }

    if (t < 0 || t >= bgH) {
        return;
    }

    int i, j;
    int right = l + w;
    if (right > bgW) {
        right = bgW;
    }
    int bottom = t + h;
    if (bottom > bgH) {
        bottom = bgH;
    }


    LOGD("right = %d", right);
    LOGD("bottom = %d", bottom);


    int conbineWidth = right - l;
    int conbineHeight = bottom - t;

    int lines = conbineHeight / 2;

    int bgYStart = l + t * bgWidth;
//    bgYStart = 0;
    LOGD("bgYStart : %d", bgYStart);
    int yStart = 0;
    int uStart = ySize;
    int vSatrt = ySize + ySize / 4;
    int bgUStart = bgYSize + (l * 2 + t * bgW) / 4;
    int bgVSatrt = bgYSize + bgYSize / 4 + (l * 2 + t * bgW) / 4;

    LOGD("uStart : %d", uStart);
    LOGD("vSatrt : %d", vSatrt);
    LOGD("bgUStart : %d", bgUStart);
    LOGD("bgVSatrt : %d", bgVSatrt);
    for (i = 0; i < lines; ++i) {
        memcpy(bgYuv + bgYStart, yuv + yStart, conbineWidth);
        bgYStart += bgW;
        yStart += w;
        memcpy(bgYuv + bgYStart, yuv + yStart, conbineWidth);
        bgYStart += bgW;
        yStart += w;

        //u
        memcpy(bgYuv + bgUStart, yuv + uStart, conbineWidth / 2);
        bgUStart += bgW / 2;
        uStart += w / 2;
        //v
        memcpy(bgYuv + bgVSatrt, yuv + vSatrt, conbineWidth / 2);
        bgVSatrt += bgW / 2;
        vSatrt += w / 2;

    }


    (*env)->ReleasePrimitiveArrayCritical(env, yuv420p, yuv, 0);
    (*env)->ReleasePrimitiveArrayCritical(env, bgYuv420p, bgYuv, 0);
}

//定义转换数组
static double highlights_add[256], highlights_sub[256];
static double midtones_add[256], midtones_sub[256];
static double shadows_add[256], shadows_sub[256];
static int isInitBalanceColorTable = 0;
//初始化转换数组

void init_balance_color_table() {
    int i;
    for (i = 0; i < 256; i++) {
        highlights_add[i] = shadows_sub[255 - i] = (1.075 - 1 / ((double) i / 16.0 + 1));
        midtones_add[i] = midtones_sub[i] =
                0.667 * (1 - (((double) i - 127.0) / 127.0) * (((double) i - 127.0) / 127.0));
        shadows_add[i] = highlights_sub[i] =
                0.667 * (1 - (((double) i - 127.0) / 127.0) * (((double) i - 127.0) / 127.0));
    }
}


//实现相关函数
int FMax(const int X, const int Y) {
    return (X < Y ? Y : X);
}

int FMin(const int X, const int Y) {
    return (Y < X ? Y : X);
}

void BalanceColor(jbyte *bitmap, int width, int height ,int value) {
    int red, green, blue;
    unsigned char r_lookup[256], g_lookup[256], b_lookup[256];

    if (isInitBalanceColorTable == 0) {
        init_balance_color_table();
        isInitBalanceColorTable = 1;
    }

    int i = 0;
    for (i = 0; i < 256; i++) {
        red = i;
        green = i;
        blue = i;

        red += (int) (0.0 * shadows_sub[red] +
                      value * midtones_add[red]
                      + 0.0 * highlights_sub[red]);
        red = FMax(0, FMin(0xFF, red));

        green += (int) (0.0 * shadows_sub[green] +
                        value * midtones_add[green]
                        + 0.0 * highlights_sub[green]);
        green = FMax(0, FMin(0xFF, green));

        blue += (int) (0.0 * shadows_sub[blue] +
                       value * midtones_add[blue]
                       + 0.0 * highlights_sub[blue]);
        blue = FMax(0, FMin(0xFF, blue));

        r_lookup[i] = (unsigned char)
                red;
        g_lookup[i] = (unsigned char)
                green;
        b_lookup[i] = (unsigned char)
                blue;
    }

    int row, col;
    int index;
    for (row = 0; row < height; row++) {
        for (col = 0; col < width; col += 3) {
            index = row * width;
            bitmap[index] = b_lookup[bitmap[index]];
            bitmap[index + 1] = b_lookup[bitmap[index + 1]];
            bitmap[index + 2] = b_lookup[bitmap[index + 2]];
        }
    }
}

JNIEXPORT void JNICALL Java_com_laka_live_gpuimage_YUVNativeLibrary_skinWhitening(JNIEnv *env,
                                                                                  jclass obj,
                                                                                  jbyteArray src,
                                                                                  jint w,
                                                                                  jint h,
                                                                                  jint level) {
    if (level <= 0) {
        return;
    }
    uint width = w;
    uint height = h;
    uint8_t *source = (uint8_t *) (*env)->GetPrimitiveArrayCritical(env, src, 0);

    BalanceColor(source, width, height, level);

    (*env)->ReleasePrimitiveArrayCritical(env, src, source, 0);
}


