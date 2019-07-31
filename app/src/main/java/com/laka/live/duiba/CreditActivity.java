package com.laka.live.duiba;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.*;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.ZoomDensity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laka.live.R;
import com.laka.live.ui.activity.BaseActivity;
import com.laka.live.util.Log;

import java.util.List;
import java.util.Stack;

/**
 * Version 1.0.1
 *
 * @author duiba
 * 1、设置toolbar标题title为单行显示。
 * 2、设置tile最大宽度为200dp。
 * 3、修改toolbar高度为dip单位，20dip。
 * 4、修改返回图标为靠左居中，margin-left=10dp。
 * 5、添加dip2px()单位转换方法。
 * <p>
 * Version 1.0.2
 * @author duiba
 * 1、修复未登录用户登录后回到页面，后退到之前的页面时会刷新一次，去掉未登录状态。
 * 2、后退刷新方法修复。
 * 3、将栈内Activity对象改为CreditActivity。
 * <p>
 * Version 1.0.3
 * @author duiba fxt
 * 1、添加分享功能，支持分享的页面的导航栏会显示“分享”，需到兑吧管理后台配置并开启。
 * 2、添加各个功能的注释信息。
 * 3、分享接口和自动登录接口改为AlertDialog的展示形式。
 * <p>
 * Version 1.0.4
 * @author duiba fxt
 * 删除webview配置： settings.setLoadWithOverviewMode(true);
 * 上面配置可能导致页面无法点击，页面适配等问题。
 * <p>
 * Version 1.0.5
 * @author duiba fxt
 * 在onConsume方法中加入刷新积分的js请求。如果页面含有onDBNewOpenBack()方法,则调用该js方法(刷新积分)
 * 根据api版本，4.4之后使用evaluateJavascript方法。
 * <p>
 * Version 1.0.6
 * @author duiba fxt
 * 1.分享功能从js调java代码接口方式改为使用特定uri拦截的方式，消除因页面加载慢导致的分享按钮不出现问题。
 * 2.修复网络慢时，分享按钮可能不出现的bug：改为url拦截方式触发，消除注入js延迟问题。
 * 3.修复分享监听事件空指针的bug：添加creditsListener非空判断。
 * 4.添加处理电话链接，启动本地通话应用。
 * <p>
 * Version 1.0.7
 * @author duiba fxt
 * 提供券码复制功能接口。
 * js触发，券码传值
 * 提供监听返回首页时，积分刷新动作。
 * 删除掉不使用的工具方法
 * <p>
 * Version 1.0.2
 * @author duiba
 * 1、修复未登录用户登录后回到页面，后退到之前的页面时会刷新一次，去掉未登录状态。
 * 2、后退刷新方法修复。
 * 3、将栈内Activity对象改为CreditActivity。
 * <p>
 * Version 1.0.3
 * @author duiba fxt
 * 1、添加分享功能，支持分享的页面的导航栏会显示“分享”，需到兑吧管理后台配置并开启。
 * 2、添加各个功能的注释信息。
 * 3、分享接口和自动登录接口改为AlertDialog的展示形式。
 * <p>
 * Version 1.0.4
 * @author duiba fxt
 * 删除webview配置： settings.setLoadWithOverviewMode(true);
 * 上面配置可能导致页面无法点击，页面适配等问题。
 * <p>
 * Version 1.0.5
 * @author duiba fxt
 * 在onConsume方法中加入刷新积分的js请求。如果页面含有onDBNewOpenBack()方法,则调用该js方法(刷新积分)
 * 根据api版本，4.4之后使用evaluateJavascript方法。
 * <p>
 * Version 1.0.6
 * @author duiba fxt
 * 1.分享功能从js调java代码接口方式改为使用特定uri拦截的方式，消除因页面加载慢导致的分享按钮不出现问题。
 * 2.修复网络慢时，分享按钮可能不出现的bug：改为url拦截方式触发，消除注入js延迟问题。
 * 3.修复分享监听事件空指针的bug：添加creditsListener非空判断。
 * 4.添加处理电话链接，启动本地通话应用。
 * <p>
 * Version 1.0.7
 * @author duiba fxt
 * 提供券码复制功能接口。
 * js触发，券码传值
 * 提供监听返回首页时，积分刷新动作。
 * 删除掉不使用的工具方法
 * <p>
 * Version 1.0.2
 * @author duiba
 * 1、修复未登录用户登录后回到页面，后退到之前的页面时会刷新一次，去掉未登录状态。
 * 2、后退刷新方法修复。
 * 3、将栈内Activity对象改为CreditActivity。
 * <p>
 * Version 1.0.3
 * @author duiba fxt
 * 1、添加分享功能，支持分享的页面的导航栏会显示“分享”，需到兑吧管理后台配置并开启。
 * 2、添加各个功能的注释信息。
 * 3、分享接口和自动登录接口改为AlertDialog的展示形式。
 * <p>
 * Version 1.0.4
 * @author duiba fxt
 * 删除webview配置： settings.setLoadWithOverviewMode(true);
 * 上面配置可能导致页面无法点击，页面适配等问题。
 * <p>
 * Version 1.0.5
 * @author duiba fxt
 * 在onConsume方法中加入刷新积分的js请求。如果页面含有onDBNewOpenBack()方法,则调用该js方法(刷新积分)
 * 根据api版本，4.4之后使用evaluateJavascript方法。
 * <p>
 * Version 1.0.6
 * @author duiba fxt
 * 1.分享功能从js调java代码接口方式改为使用特定uri拦截的方式，消除因页面加载慢导致的分享按钮不出现问题。
 * 2.修复网络慢时，分享按钮可能不出现的bug：改为url拦截方式触发，消除注入js延迟问题。
 * 3.修复分享监听事件空指针的bug：添加creditsListener非空判断。
 * 4.添加处理电话链接，启动本地通话应用。
 * <p>
 * Version 1.0.7
 * @author duiba fxt
 * 提供券码复制功能接口。
 * js触发，券码传值
 * 提供监听返回首页时，积分刷新动作。
 * 删除掉不使用的工具方法
 * <p>
 * Version 1.0.2
 * @author duiba
 * 1、修复未登录用户登录后回到页面，后退到之前的页面时会刷新一次，去掉未登录状态。
 * 2、后退刷新方法修复。
 * 3、将栈内Activity对象改为CreditActivity。
 * <p>
 * Version 1.0.3
 * @author duiba fxt
 * 1、添加分享功能，支持分享的页面的导航栏会显示“分享”，需到兑吧管理后台配置并开启。
 * 2、添加各个功能的注释信息。
 * 3、分享接口和自动登录接口改为AlertDialog的展示形式。
 * <p>
 * Version 1.0.4
 * @author duiba fxt
 * 删除webview配置： settings.setLoadWithOverviewMode(true);
 * 上面配置可能导致页面无法点击，页面适配等问题。
 * <p>
 * Version 1.0.5
 * @author duiba fxt
 * 在onConsume方法中加入刷新积分的js请求。如果页面含有onDBNewOpenBack()方法,则调用该js方法(刷新积分)
 * 根据api版本，4.4之后使用evaluateJavascript方法。
 * <p>
 * Version 1.0.6
 * @author duiba fxt
 * 1.分享功能从js调java代码接口方式改为使用特定uri拦截的方式，消除因页面加载慢导致的分享按钮不出现问题。
 * 2.修复网络慢时，分享按钮可能不出现的bug：改为url拦截方式触发，消除注入js延迟问题。
 * 3.修复分享监听事件空指针的bug：添加creditsListener非空判断。
 * 4.添加处理电话链接，启动本地通话应用。
 * <p>
 * Version 1.0.7
 * @author duiba fxt
 * 提供券码复制功能接口。
 * js触发，券码传值
 * 提供监听返回首页时，积分刷新动作。
 * 删除掉不使用的工具方法
 * <p>
 * Version 1.0.2
 * @author duiba
 * 1、修复未登录用户登录后回到页面，后退到之前的页面时会刷新一次，去掉未登录状态。
 * 2、后退刷新方法修复。
 * 3、将栈内Activity对象改为CreditActivity。
 * <p>
 * Version 1.0.3
 * @author duiba fxt
 * 1、添加分享功能，支持分享的页面的导航栏会显示“分享”，需到兑吧管理后台配置并开启。
 * 2、添加各个功能的注释信息。
 * 3、分享接口和自动登录接口改为AlertDialog的展示形式。
 * <p>
 * Version 1.0.4
 * @author duiba fxt
 * 删除webview配置： settings.setLoadWithOverviewMode(true);
 * 上面配置可能导致页面无法点击，页面适配等问题。
 * <p>
 * Version 1.0.5
 * @author duiba fxt
 * 在onConsume方法中加入刷新积分的js请求。如果页面含有onDBNewOpenBack()方法,则调用该js方法(刷新积分)
 * 根据api版本，4.4之后使用evaluateJavascript方法。
 * <p>
 * Version 1.0.6
 * @author duiba fxt
 * 1.分享功能从js调java代码接口方式改为使用特定uri拦截的方式，消除因页面加载慢导致的分享按钮不出现问题。
 * 2.修复网络慢时，分享按钮可能不出现的bug：改为url拦截方式触发，消除注入js延迟问题。
 * 3.修复分享监听事件空指针的bug：添加creditsListener非空判断。
 * 4.添加处理电话链接，启动本地通话应用。
 * <p>
 * Version 1.0.7
 * @author duiba fxt
 * 提供券码复制功能接口。
 * js触发，券码传值
 * 提供监听返回首页时，积分刷新动作。
 * 删除掉不使用的工具方法
 * <p>
 * Version 1.0.2
 * @author duiba
 * 1、修复未登录用户登录后回到页面，后退到之前的页面时会刷新一次，去掉未登录状态。
 * 2、后退刷新方法修复。
 * 3、将栈内Activity对象改为CreditActivity。
 * <p>
 * Version 1.0.3
 * @author duiba fxt
 * 1、添加分享功能，支持分享的页面的导航栏会显示“分享”，需到兑吧管理后台配置并开启。
 * 2、添加各个功能的注释信息。
 * 3、分享接口和自动登录接口改为AlertDialog的展示形式。
 * <p>
 * Version 1.0.4
 * @author duiba fxt
 * 删除webview配置： settings.setLoadWithOverviewMode(true);
 * 上面配置可能导致页面无法点击，页面适配等问题。
 * <p>
 * Version 1.0.5
 * @author duiba fxt
 * 在onConsume方法中加入刷新积分的js请求。如果页面含有onDBNewOpenBack()方法,则调用该js方法(刷新积分)
 * 根据api版本，4.4之后使用evaluateJavascript方法。
 * <p>
 * Version 1.0.6
 * @author duiba fxt
 * 1.分享功能从js调java代码接口方式改为使用特定uri拦截的方式，消除因页面加载慢导致的分享按钮不出现问题。
 * 2.修复网络慢时，分享按钮可能不出现的bug：改为url拦截方式触发，消除注入js延迟问题。
 * 3.修复分享监听事件空指针的bug：添加creditsListener非空判断。
 * 4.添加处理电话链接，启动本地通话应用。
 * <p>
 * Version 1.0.7
 * @author duiba fxt
 * 提供券码复制功能接口。
 * js触发，券码传值
 * 提供监听返回首页时，积分刷新动作。
 * 删除掉不使用的工具方法
 * <p>
 * Version 1.0.2
 * @author duiba
 * 1、修复未登录用户登录后回到页面，后退到之前的页面时会刷新一次，去掉未登录状态。
 * 2、后退刷新方法修复。
 * 3、将栈内Activity对象改为CreditActivity。
 * <p>
 * Version 1.0.3
 * @author duiba fxt
 * 1、添加分享功能，支持分享的页面的导航栏会显示“分享”，需到兑吧管理后台配置并开启。
 * 2、添加各个功能的注释信息。
 * 3、分享接口和自动登录接口改为AlertDialog的展示形式。
 * <p>
 * Version 1.0.4
 * @author duiba fxt
 * 删除webview配置： settings.setLoadWithOverviewMode(true);
 * 上面配置可能导致页面无法点击，页面适配等问题。
 * <p>
 * Version 1.0.5
 * @author duiba fxt
 * 在onConsume方法中加入刷新积分的js请求。如果页面含有onDBNewOpenBack()方法,则调用该js方法(刷新积分)
 * 根据api版本，4.4之后使用evaluateJavascript方法。
 * <p>
 * Version 1.0.6
 * @author duiba fxt
 * 1.分享功能从js调java代码接口方式改为使用特定uri拦截的方式，消除因页面加载慢导致的分享按钮不出现问题。
 * 2.修复网络慢时，分享按钮可能不出现的bug：改为url拦截方式触发，消除注入js延迟问题。
 * 3.修复分享监听事件空指针的bug：添加creditsListener非空判断。
 * 4.添加处理电话链接，启动本地通话应用。
 * <p>
 * Version 1.0.7
 * @author duiba fxt
 * 提供券码复制功能接口。
 * js触发，券码传值
 * 提供监听返回首页时，积分刷新动作。
 * 删除掉不使用的工具方法
 * <p>
 * Version 1.0.2
 * @author duiba
 * 1、修复未登录用户登录后回到页面，后退到之前的页面时会刷新一次，去掉未登录状态。
 * 2、后退刷新方法修复。
 * 3、将栈内Activity对象改为CreditActivity。
 * <p>
 * Version 1.0.3
 * @author duiba fxt
 * 1、添加分享功能，支持分享的页面的导航栏会显示“分享”，需到兑吧管理后台配置并开启。
 * 2、添加各个功能的注释信息。
 * 3、分享接口和自动登录接口改为AlertDialog的展示形式。
 * <p>
 * Version 1.0.4
 * @author duiba fxt
 * 删除webview配置： settings.setLoadWithOverviewMode(true);
 * 上面配置可能导致页面无法点击，页面适配等问题。
 * <p>
 * Version 1.0.5
 * @author duiba fxt
 * 在onConsume方法中加入刷新积分的js请求。如果页面含有onDBNewOpenBack()方法,则调用该js方法(刷新积分)
 * 根据api版本，4.4之后使用evaluateJavascript方法。
 * <p>
 * Version 1.0.6
 * @author duiba fxt
 * 1.分享功能从js调java代码接口方式改为使用特定uri拦截的方式，消除因页面加载慢导致的分享按钮不出现问题。
 * 2.修复网络慢时，分享按钮可能不出现的bug：改为url拦截方式触发，消除注入js延迟问题。
 * 3.修复分享监听事件空指针的bug：添加creditsListener非空判断。
 * 4.添加处理电话链接，启动本地通话应用。
 * <p>
 * Version 1.0.7
 * @author duiba fxt
 * 提供券码复制功能接口。
 * js触发，券码传值
 * 提供监听返回首页时，积分刷新动作。
 * 删除掉不使用的工具方法
 * <p>
 * Version 1.0.2
 * @author duiba
 * 1、修复未登录用户登录后回到页面，后退到之前的页面时会刷新一次，去掉未登录状态。
 * 2、后退刷新方法修复。
 * 3、将栈内Activity对象改为CreditActivity。
 * <p>
 * Version 1.0.3
 * @author duiba fxt
 * 1、添加分享功能，支持分享的页面的导航栏会显示“分享”，需到兑吧管理后台配置并开启。
 * 2、添加各个功能的注释信息。
 * 3、分享接口和自动登录接口改为AlertDialog的展示形式。
 * <p>
 * Version 1.0.4
 * @author duiba fxt
 * 删除webview配置： settings.setLoadWithOverviewMode(true);
 * 上面配置可能导致页面无法点击，页面适配等问题。
 * <p>
 * Version 1.0.5
 * @author duiba fxt
 * 在onConsume方法中加入刷新积分的js请求。如果页面含有onDBNewOpenBack()方法,则调用该js方法(刷新积分)
 * 根据api版本，4.4之后使用evaluateJavascript方法。
 * <p>
 * Version 1.0.6
 * @author duiba fxt
 * 1.分享功能从js调java代码接口方式改为使用特定uri拦截的方式，消除因页面加载慢导致的分享按钮不出现问题。
 * 2.修复网络慢时，分享按钮可能不出现的bug：改为url拦截方式触发，消除注入js延迟问题。
 * 3.修复分享监听事件空指针的bug：添加creditsListener非空判断。
 * 4.添加处理电话链接，启动本地通话应用。
 * <p>
 * Version 1.0.7
 * @author duiba fxt
 * 提供券码复制功能接口。
 * js触发，券码传值
 * 提供监听返回首页时，积分刷新动作。
 * 删除掉不使用的工具方法
 * <p>
 * Version 1.0.2
 * @author duiba
 * 1、修复未登录用户登录后回到页面，后退到之前的页面时会刷新一次，去掉未登录状态。
 * 2、后退刷新方法修复。
 * 3、将栈内Activity对象改为CreditActivity。
 * <p>
 * Version 1.0.3
 * @author duiba fxt
 * 1、添加分享功能，支持分享的页面的导航栏会显示“分享”，需到兑吧管理后台配置并开启。
 * 2、添加各个功能的注释信息。
 * 3、分享接口和自动登录接口改为AlertDialog的展示形式。
 * <p>
 * Version 1.0.4
 * @author duiba fxt
 * 删除webview配置： settings.setLoadWithOverviewMode(true);
 * 上面配置可能导致页面无法点击，页面适配等问题。
 * <p>
 * Version 1.0.5
 * @author duiba fxt
 * 在onConsume方法中加入刷新积分的js请求。如果页面含有onDBNewOpenBack()方法,则调用该js方法(刷新积分)
 * 根据api版本，4.4之后使用evaluateJavascript方法。
 * <p>
 * Version 1.0.6
 * @author duiba fxt
 * 1.分享功能从js调java代码接口方式改为使用特定uri拦截的方式，消除因页面加载慢导致的分享按钮不出现问题。
 * 2.修复网络慢时，分享按钮可能不出现的bug：改为url拦截方式触发，消除注入js延迟问题。
 * 3.修复分享监听事件空指针的bug：添加creditsListener非空判断。
 * 4.添加处理电话链接，启动本地通话应用。
 * <p>
 * Version 1.0.7
 * @author duiba fxt
 * 提供券码复制功能接口。
 * js触发，券码传值
 * 提供监听返回首页时，积分刷新动作。
 * 删除掉不使用的工具方法
 * <p>
 * Version 1.0.2
 * @author duiba
 * 1、修复未登录用户登录后回到页面，后退到之前的页面时会刷新一次，去掉未登录状态。
 * 2、后退刷新方法修复。
 * 3、将栈内Activity对象改为CreditActivity。
 * <p>
 * Version 1.0.3
 * @author duiba fxt
 * 1、添加分享功能，支持分享的页面的导航栏会显示“分享”，需到兑吧管理后台配置并开启。
 * 2、添加各个功能的注释信息。
 * 3、分享接口和自动登录接口改为AlertDialog的展示形式。
 * <p>
 * Version 1.0.4
 * @author duiba fxt
 * 删除webview配置： settings.setLoadWithOverviewMode(true);
 * 上面配置可能导致页面无法点击，页面适配等问题。
 * <p>
 * Version 1.0.5
 * @author duiba fxt
 * 在onConsume方法中加入刷新积分的js请求。如果页面含有onDBNewOpenBack()方法,则调用该js方法(刷新积分)
 * 根据api版本，4.4之后使用evaluateJavascript方法。
 * <p>
 * Version 1.0.6
 * @author duiba fxt
 * 1.分享功能从js调java代码接口方式改为使用特定uri拦截的方式，消除因页面加载慢导致的分享按钮不出现问题。
 * 2.修复网络慢时，分享按钮可能不出现的bug：改为url拦截方式触发，消除注入js延迟问题。
 * 3.修复分享监听事件空指针的bug：添加creditsListener非空判断。
 * 4.添加处理电话链接，启动本地通话应用。
 * <p>
 * Version 1.0.7
 * @author duiba fxt
 * 提供券码复制功能接口。
 * js触发，券码传值
 * 提供监听返回首页时，积分刷新动作。
 * 删除掉不使用的工具方法
 * <p>
 * Version 1.0.2
 * @author duiba
 * 1、修复未登录用户登录后回到页面，后退到之前的页面时会刷新一次，去掉未登录状态。
 * 2、后退刷新方法修复。
 * 3、将栈内Activity对象改为CreditActivity。
 * <p>
 * Version 1.0.3
 * @author duiba fxt
 * 1、添加分享功能，支持分享的页面的导航栏会显示“分享”，需到兑吧管理后台配置并开启。
 * 2、添加各个功能的注释信息。
 * 3、分享接口和自动登录接口改为AlertDialog的展示形式。
 * <p>
 * Version 1.0.4
 * @author duiba fxt
 * 删除webview配置： settings.setLoadWithOverviewMode(true);
 * 上面配置可能导致页面无法点击，页面适配等问题。
 * <p>
 * Version 1.0.5
 * @author duiba fxt
 * 在onConsume方法中加入刷新积分的js请求。如果页面含有onDBNewOpenBack()方法,则调用该js方法(刷新积分)
 * 根据api版本，4.4之后使用evaluateJavascript方法。
 * <p>
 * Version 1.0.6
 * @author duiba fxt
 * 1.分享功能从js调java代码接口方式改为使用特定uri拦截的方式，消除因页面加载慢导致的分享按钮不出现问题。
 * 2.修复网络慢时，分享按钮可能不出现的bug：改为url拦截方式触发，消除注入js延迟问题。
 * 3.修复分享监听事件空指针的bug：添加creditsListener非空判断。
 * 4.添加处理电话链接，启动本地通话应用。
 * <p>
 * Version 1.0.7
 * @author duiba fxt
 * 提供券码复制功能接口。
 * js触发，券码传值
 * 提供监听返回首页时，积分刷新动作。
 * 删除掉不使用的工具方法
 */
/**
 * Version 1.0.2
 * @author duiba
 * 1、修复未登录用户登录后回到页面，后退到之前的页面时会刷新一次，去掉未登录状态。
 * 2、后退刷新方法修复。
 * 3、将栈内Activity对象改为CreditActivity。
 */
/**
 * Version 1.0.3
 * @author duiba fxt
 * 1、添加分享功能，支持分享的页面的导航栏会显示“分享”，需到兑吧管理后台配置并开启。
 * 2、添加各个功能的注释信息。
 * 3、分享接口和自动登录接口改为AlertDialog的展示形式。
 */
/**
 * Version 1.0.4
 * @author duiba fxt
 * 删除webview配置： settings.setLoadWithOverviewMode(true); 
 * 上面配置可能导致页面无法点击，页面适配等问题。
 */
/**
 * Version 1.0.5
 * @author duiba fxt
 * 在onConsume方法中加入刷新积分的js请求。如果页面含有onDBNewOpenBack()方法,则调用该js方法(刷新积分)
 * 根据api版本，4.4之后使用evaluateJavascript方法。
 */
/**
 * Version 1.0.6
 * @author duiba fxt
 * 1.分享功能从js调java代码接口方式改为使用特定uri拦截的方式，消除因页面加载慢导致的分享按钮不出现问题。
 * 2.修复网络慢时，分享按钮可能不出现的bug：改为url拦截方式触发，消除注入js延迟问题。
 * 3.修复分享监听事件空指针的bug：添加creditsListener非空判断。
 * 4.添加处理电话链接，启动本地通话应用。
 */
/**
 * Version 1.0.7
 * @author duiba fxt
 * 提供券码复制功能接口。
 * js触发，券码传值
 * 提供监听返回首页时，积分刷新动作。
 * 删除掉不使用的工具方法
 */

/**
 * Version 1.0.8
 * 修改唤醒登录后历史页面返回刷新的功能，同时修复了一个唤醒登录历史页面不刷新的bug。
 * 改为使用IS_WAKEUP_LOGIN静态变量标记唤醒登录状态，在onResume方法里加入判断，
 * 实现历史页面返回刷新的功能。
 */

public class CreditActivity extends BaseActivity {
    private static String ua;
    private static Stack<CreditActivity> activityStack;
    public static final String VERSION = "1.0.8";
    public static CreditsListener creditsListener;
    public static boolean IS_WAKEUP_LOGIN = false;
    public static String INDEX_URI = "/chome/index";

    public interface CreditsListener {
        /**
         * 当点击分享按钮被点击
         * @param shareUrl 分享的地址
         * @param shareThumbnail 分享的缩略图
         * @param shareTitle 分享的标题
         * @param shareSubtitle 分享的副标题
         */
        public void onShareClick(WebView webView, String shareUrl, String shareThumbnail, String shareTitle, String shareSubtitle);

        /**
         * 当点击登录
         * @param webView 用于登录成功后返回到当前的webview并刷新。
         * @param currentUrl 当前页面的url
         */
        public void onLoginClick(WebView webView, String currentUrl);

        /**
         * 当点复制券码时
         * @param mWebView mWebView对象。
         * @param code 复制的券码
         */
        public void onCopyCode(WebView mWebView, String code);

        /**
         * 通知本地，刷新积分
         * @param mWebView
         * @param credits
         */
        public void onLocalRefresh(WebView mWebView, String credits);
    }

    protected String mUrl;
    protected String shareUrl;            //分享的url
    protected String shareThumbnail;    //分享的缩略图
    protected String shareTitle;        //分享的标题
    protected String shareSubtitle;        //分享的副标题
    protected Boolean ifRefresh = false;
    protected Boolean ifRefreshMain = false;
    protected Boolean delayRefresh = false;
    protected String navColor;
    protected String titleColor;
    protected Long shareColor;

    protected WebView mWebView;
    protected LinearLayout mLinearLayout;
    protected RelativeLayout mNavigationBar;
    protected TextView mTitle;
    protected ImageView mBackView;
    protected TextView mShare;

    private int RequestCode = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 锁定竖屏显示
        mUrl = getIntent().getStringExtra("url");
        if (mUrl == null) {
            throw new RuntimeException("url can't be blank");
        }

        // 管理匿名类栈，用于模拟原生应用的页面跳转。
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.push(this);

        // 配置导航条文本颜色
        titleColor = getIntent().getStringExtra("titleColor");
        String titleColorTemp = "0xff" + titleColor.substring(1, titleColor.length());
        Long titlel = Long.parseLong(titleColorTemp.substring(2), 16);
        // 配置分享文案颜色,同title
        shareColor = titlel;
        // 配置导航栏背景颜色
        navColor = getIntent().getStringExtra("navColor");
        String navColorTemp = "0xff" + navColor.substring(1, navColor.length());
        Long navl = Long.parseLong(navColorTemp.substring(2), 16);
        // 初始化页面
        initView();
        setContentView(mLinearLayout);

        // api11以上的系统隐藏系统默认的ActionBar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        }

        mTitle.setTextColor(titlel.intValue());
        mNavigationBar.setBackgroundColor(navl.intValue());
        mBackView.setPadding(50, 50, 50, 50);
        mBackView.setClickable(true);

        // 添加后退按钮监听事件
        mBackView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                onBackClick();
            }
        });
        // 添加分享按钮的监听事件
        if (mShare != null) {
            mShare.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (creditsListener != null) {
                        creditsListener.onShareClick(mWebView, shareUrl, shareThumbnail, shareTitle, shareSubtitle);
                    }
                }
            });
        }

        //js调java代码接口。
        mWebView.addJavascriptInterface(new Object() {

            //用于跳转用户登录页面事件。
            @JavascriptInterface
            public void login() {
                if (creditsListener != null) {
                    mWebView.post(new Runnable() {
                        @Override
                        public void run() {
                            creditsListener.onLoginClick(mWebView, mWebView.getUrl());
                        }
                    });
                }
            }

            //复制券码
            @JavascriptInterface
            public void copyCode(final String code) {
                if (creditsListener != null) {
                    mWebView.post(new Runnable() {
                        @Override
                        public void run() {
                            creditsListener.onCopyCode(mWebView, code);
                        }
                    });
                }
            }

            //客户端本地触发刷新积分。
            @JavascriptInterface
            public void localRefresh(final String credits) {
                if (creditsListener != null) {
                    mWebView.post(new Runnable() {
                        @Override
                        public void run() {
                            creditsListener.onLocalRefresh(mWebView, credits);
                        }
                    });
                }
            }

        }, "duiba_app");

        if (ua == null) {
            ua = mWebView.getSettings().getUserAgentString() + " Duiba/" + VERSION;
        }
        mWebView.getSettings().setUserAgentString(ua);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                CreditActivity.this.onReceivedTitle(view, title);
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.log("url:" + url);
                return shouldOverrideUrlByDuiba(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                if (activityStack.size() > 1) {
                    setSwipeBackEnable(true);
                    mBackView.setVisibility(View.VISIBLE);
                }

            }

        });


        mWebView.loadUrl(mUrl);
    }

    protected void onBackClick() {
        Intent intent = new Intent();
        setResult(99, intent);
        finishActivity(this);
    }

    // 初始化页面
    protected void initView() {
        mLinearLayout = new LinearLayout(this);
        mLinearLayout.setBackgroundColor(Color.GRAY);
        mLinearLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);

        int height50dp = dip2px(this, 50);
        // 自定义导航栏
        initNavigationBar();

        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, height50dp);

        mLinearLayout.addView(mNavigationBar, mLayoutParams);
        // 初始化WebView配置
        initWebView();

        mLinearLayout.addView(mWebView);


    }

    //自定义导航栏，包含 后退按钮，页面标题，分享按钮（默认隐藏）
    protected void initNavigationBar() {
        int dp200 = dip2px(this, 200);
        int dp50 = dip2px(this, 50);
        int dp20 = dip2px(this, 20);
        int dp10 = dip2px(this, 10);

        mNavigationBar = new RelativeLayout(this);
        mNavigationBar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, dp20));

        mTitle = new TextView(this);
        mTitle.setLines(1);
        mTitle.setTextSize(18);
        mNavigationBar.addView(mTitle);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mTitle.getLayoutParams();
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);

        // 返回键
        mBackView = new ImageView(this);
        mBackView.setBackgroundResource(R.drawable.live_icon_back_h);
        RelativeLayout.LayoutParams mBackLayout = new RelativeLayout
                .LayoutParams(dp20, dp20);
        mBackLayout.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        mBackLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        mBackLayout.setMargins(dp10, 0, 0, 0);

        if (activityStack.size() == 1) {
            ifRefreshMain = true;
            setSwipeBackEnable(false);
            mBackView.setVisibility(View.GONE);
        }

        mNavigationBar.addView(mBackView, mBackLayout);

        //在导航栏的右侧添加分享按钮（无分享信息的页面隐藏）
        mShare = new TextView(this);
        mShare.setLines(1);
        mShare.setTextSize(15.0f);
        mShare.setText("分享");
        mShare.setPadding(0, 0, dp10, 0);
        mShare.setTextColor(shareColor.intValue());
        mNavigationBar.addView(mShare);
        RelativeLayout.LayoutParams shareLp = (RelativeLayout.LayoutParams) mShare.getLayoutParams();
        shareLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        shareLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        //设置为默认不显示
        mShare.setVisibility(View.INVISIBLE);
        mShare.setClickable(false);

    }

    //初始化WebView配置
    protected void initWebView() {
        mWebView = new WebView(this);
        mWebView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        WebSettings settings = mWebView.getSettings();

        // User settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (0 != (getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE)) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }
        settings.setJavaScriptEnabled(true);    //设置webview支持javascript
        settings.setLoadsImagesAutomatically(true);    //支持自动加载图片
        settings.setUseWideViewPort(true);    //设置webview推荐使用的窗口，使html界面自适应屏幕
        settings.setLoadWithOverviewMode(true);
        settings.setSaveFormData(true);    //设置webview保存表单数据
        settings.setSavePassword(true);    //设置webview保存密码
        settings.setDefaultZoom(ZoomDensity.MEDIUM);    //设置中等像素密度，medium=160dpi
        settings.setSupportZoom(true);    //支持缩放

        CookieManager.getInstance().setAcceptCookie(true);

        if (Build.VERSION.SDK_INT > 8) {
            settings.setPluginState(PluginState.ON_DEMAND);
        }

        // Technical settings
        settings.setSupportMultipleWindows(true);
        mWebView.setLongClickable(true);
        mWebView.setScrollbarFadingEnabled(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setDrawingCacheEnabled(true);

        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
    }

    protected void onReceivedTitle(WebView view, String title) {
        mTitle.setText(title);
    }

    /**
     * 拦截url请求，根据url结尾执行相应的动作。 （重要）
     * 用途：模仿原生应用体验，管理页面历史栈。
     * @param view
     * @param url
     * @return
     */
    protected boolean shouldOverrideUrlByDuiba(WebView view, String url) {
        Uri uri = Uri.parse(url);

        if (this.mUrl.equals(url)) {
            view.loadUrl(url);
            return true;
        }

        // 处理电话链接，启动本地通话应用。
        if (url.startsWith("tel:")) {
            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            startActivity(intent);
            return true;
        }

        //判断非http连接时尝试发出系统级intent（比如tbopen等打开第三方应用）
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            final PackageManager packageManager = this.getPackageManager();
            final Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            //Uri.parse("tbopen://m.taobao.com/tbopen/index.html?action=ali.open.nav&module=h5&appkey=23402401&backURL=&TTID=2014_0_23402401@baichuan_h5_0.0.3&bootImage=0&appName=&packageName=&source=bc&tag=&utdid=&scm=&pvid=&uri=https://taoquan.taobao.com/coupon/unify_apply.htm?sellerId=1114408582&activityId=c184815f21714efba5225887e75852cd&h5Url=https%3A%2F%2Ftaoquan.taobao.com%2Fcoupon%2Funify_apply.htm%3FsellerId%3D1114408582%26activityId%3Dc184815f21714efba5225887e75852cd%26params%3D%25257B%252522TTID%252522%25253A%2525222014_0_23402401%252540baichuan_h5_0.0.3%252522%25252C%252522source%252522%25253A%252522bc%252522%25252C%252522ybhpss%252522%25253A%252522dHRpZD0yMDE0XzBfMjM0MDI0MDElNDBiYWljaHVhbl9oNV8wLjAuMw%2525253D%2525253D%252522%25257D&params=%7B%22TTID%22%3A%222014_0_23402401%40baichuan_h5_0.0.3%22%2C%22source%22%3A%22bc%22%2C%22ybhpss%22%3A%22dHRpZD0yMDE0XzBfMjM0MDI0MDElNDBiYWljaHVhbl9oNV8wLjAuMw%253D%253D%22%7D"));
            List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (list.size() > 0) {
                //可以处理此intent的情况
                startActivity(intent);
                return true;
            }

            return false;
        }

        // 截获页面分享请求，分享数据 不需要分享
//        if ("/client/dbshare".equals(uri.getPath())) {
//            String content = uri.getQueryParameter("content");
//            if (creditsListener != null && content != null) {
//                String[] dd = content.split("\\|");
//                if (dd.length == 4) {
//                    setShareInfo(dd[0], dd[1], dd[2], dd[3]);
//                    mShare.setVisibility(View.VISIBLE);
//                    mShare.setClickable(true);
//                }
//                return true;
//            }
//        }

        // 截获页面唤起登录请求。（目前暂时还是用js回调的方式，这里仅作预留。）
        if ("/client/dblogin".equals(uri.getPath())) {
            if (creditsListener != null) {
                mWebView.post(new Runnable() {
                    @Override
                    public void run() {
                        creditsListener.onLoginClick(mWebView, mWebView.getUrl());
                    }
                });
            }
            return true;
        }
        if (url.contains("dbnewopen")) { // 新开页面
            Intent intent = new Intent();
            intent.setClass(CreditActivity.this, CreditActivity.this.getClass());
            intent.putExtra("navColor", navColor);
            intent.putExtra("titleColor", titleColor);
            url = url.replace("dbnewopen", "none");
            intent.putExtra("url", url);
            startActivityForResult(intent, RequestCode);
        } else if (url.contains("dbbackrefresh")) { // 后退并刷新
            url = url.replace("dbbackrefresh", "none");
            Intent intent = new Intent();
            intent.putExtra("url", url);
            intent.putExtra("navColor", navColor);
            intent.putExtra("titleColor", titleColor);
            setResult(RequestCode, intent);
            finishActivity(this);
        } else if (url.contains("dbbackrootrefresh")) { // 回到积分商城首页并刷新
            url = url.replace("dbbackrootrefresh", "none");
            if (activityStack.size() == 1) {
                finishActivity(this);
            } else {
                activityStack.get(0).ifRefresh = true;
                finishUpActivity();
            }
        } else if (url.contains("dbbackroot")) { // 回到积分商城首页
            url = url.replace("dbbackroot", "none");
            if (activityStack.size() == 1) {
                finishActivity(this);
            } else {
                finishUpActivity();
            }
        } else if (url.contains("dbback")) { // 后退
            url = url.replace("dbback", "none");
            finishActivity(this);
        } else {
            if (url.endsWith(".apk") || url.contains(".apk?")) { // 支持应用链接下载
                Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(viewIntent);
                return true;
            }
            view.loadUrl(url);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == 100) {
            if (intent.getStringExtra("url") != null) {
                this.mUrl = intent.getStringExtra("url");
                mWebView.loadUrl(this.mUrl);
                ifRefresh = false;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ifRefreshMain) {
            mWebView.loadUrl(this.mUrl);
        } else if (ifRefresh) {
            this.mUrl = getIntent().getStringExtra("url");
            mWebView.loadUrl(this.mUrl);
            ifRefresh = false;
            //如果首页含有登录的入口，返回时需要同时刷新首页的话，
            // 需要把下面判断语句中的 && this.url.indexOf(INDEX_URI) > 0 去掉。
        } else if (IS_WAKEUP_LOGIN && this.mUrl.indexOf(INDEX_URI) > 0) {
            mWebView.reload();
            IS_WAKEUP_LOGIN = false;
        } else {
            // 返回页面时，如果页面含有onDBNewOpenBack()方法,则调用该js方法。
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mWebView.evaluateJavascript("if(window.onDBNewOpenBack){onDBNewOpenBack()}", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                    }
                });
            } else {
                mWebView.loadUrl("javascript:if(window.onDBNewOpenBack){onDBNewOpenBack()}");
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackClick();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    //--------------------------------------------以下为工具方法----------------------------------------------

    /**
     * 配置分享信息
     */

    protected void setShareInfo(String shareUrl, String shareThumbnail, String shareTitle, String shareSubtitle) {
        this.shareUrl = shareUrl;
        this.shareThumbnail = shareThumbnail;
        this.shareSubtitle = shareSubtitle;
        this.shareTitle = shareTitle;
    }

    /**
     * 结束除了最底部一个以外的所有Activity
     */
    public void finishUpActivity() {
        int size = activityStack.size();
        for (int i = 0; i < size - 1; i++) {
            activityStack.pop().finish();
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }

        if (activityStack.size() == 1) {
            setSwipeBackEnable(false);
            mBackView.setVisibility(View.GONE);
        } else {
            ifRefreshMain = false;
            setSwipeBackEnable(true);
            mBackView.setVisibility(View.VISIBLE);
        }

    }

    /**
     * （已弃用此方法，改为使用静态变量IS_WAKEUP_LOGIN来做通过唤醒登录的判断，从而设置返回后刷新）
     * 设置栈内所有Activity为返回待刷新。
     * 作用：未登录用户唤起登录后，将所有栈内的Activity设置为onResume时刷新页面。
     */
//    public void setAllActivityDelayRefresh(){
//    	int size = activityStack.size();
//        for (int i = 0;i < size; i++) {
//        	if(activityStack.get(i)!=this){
//        		activityStack.get(i).delayRefresh = true;
//        	}
//        }
//    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static void clear() {

        if (activityStack != null)
            activityStack.clear();

    }

    public static void setActivityStack(Stack<CreditActivity> activityStack) {
        CreditActivity.activityStack = activityStack;
    }
}
