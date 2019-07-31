# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/luwies/Desktop/work/adt-bundle-mac-x86_64-201407022/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontwarn
-dontshrink
-optimizationpasses 2
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keepattributes Exceptions,SourceFile,LineNumberTable
-ignorewarnings


#-printmapping ${c:}/mapping.txt

#-dontwarn android.support.v4.**

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends java.lang.Throwable {*;}
-keep public class * extends java.lang.Exception {*;}
-keep public class * extends android.support.v4.app.Fragment


-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    #public static **[] values();
    #public static ** valueOf(java.lang.String);
   *;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep class * implements java.io.Serializable

-keepattributes *Annotation*,EnclosingMethod,Signature

-keep interface android.content.pm.**{*;}

-keep class android.content.pm.**{*;}
-keep class android.os.Process{*;}

-keep class com.laka.live.bean.**{*;}

-keepclassmembers class * {
    @android.support.v4 *;
}



-keep class com.tencent.mm.sdk.** {
   *;
}

-dontoptimize
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

#==================gson==========================
-dontwarn com.google.**
-keep class com.google.gson.** {*;}
#==================protobuf======================
-dontwarn com.google.**
-keep class com.google.protobuf.** {*;}

#==================QQ Open SDK======================
-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}


#==================Fresco======================
# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**

#-keep class org.simple.** { *; }
#-keep interface org.simple.** { *; }

-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keepattributes *Annotation*
-keep class de.greenrobot.eventbus.** {*;}
-keep class org.greenrobot.eventbus.**{*;}
-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
}


-keep class com.alipay.** {*;}
-keep class com.ta.utdid2.** {*;}
-keep class com.ut.device.** {*;}

-keep class com.tencent.** {*;}
-keep class qalsdk.** {*;}
-keep class tencent.tls** {*;}

-dontwarn com.tencent.**
-dontwarn tencent.**
-dontwarn qalsdk.**

-keep class org.kymjs.kjframe.**{*;}

-keep class com.sina.** {*;}

-keep class com.qq.** {*;}

-keep class de.greenrobot.dao.** {*;}

-keep class laka.live.**{*;}

-keep class com.kyleduo.**{*;}

-keep class com.laka.live.ui.widget.continueButton.**{*;}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keep public class com.laka.live.R$*{
    public static final int *;
}

#KSY CODE
-keep class com.ksy.recordlib.** { *;}

-keep class com.tencent.karaoke.common.media.audiofx.** { *;}

#HotFix

#基线包使用，生成mapping.txt
-printmapping mapping.txt
#生成的mapping.txt在app/buidl/outputs/mapping/release路径下，移动到/app路径下
#修复后的项目使用，保证混淆结果一致
#-applymapping mapping.txt
#HotFix混淆配置
-keep class * extends java.lang.annotation.Annotation
-keepclasseswithmembernames class * {
    native <methods>;
}
-keep class com.alipay.euler.andfix.**{
    *;
}
-keep class com.taobao.hotfix.aidl.**{*;}
-keep class com.ta.utdid2.device.**{*;}
-keep class com.taobao.hotfix.HotFixManager{
    public *;
}

-keep class com.zego.**{*;}

# 饺子视频播放
-keep class cn.jzvd.**{*;}
-dontwarn cn.jzvd.**