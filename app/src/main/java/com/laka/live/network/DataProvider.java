package com.laka.live.network;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.google.gson.reflect.TypeToken;
import com.laka.live.account.AccountInfoManager;
import com.laka.live.application.LiveApplication;
import com.laka.live.bean.OrderPayInfoMsg;
import com.laka.live.bean.UserInfo;
import com.laka.live.bean.WechatPayInfo;
import com.laka.live.config.SystemConfig;
import com.laka.live.msg.BestTopicMsg;
import com.laka.live.msg.CertificateInfoMsg;
import com.laka.live.msg.CommentMsg;
import com.laka.live.msg.ContentMsg;
import com.laka.live.msg.CourseCategoryOneMsg;
import com.laka.live.msg.CourseCategoryTwoMsg;
import com.laka.live.msg.CourseCommentMsg;
import com.laka.live.msg.CourseDetailMsg;
import com.laka.live.msg.CourseIncomeDetailInfoMsg;
import com.laka.live.msg.CourseIncomeDetailMsg;
import com.laka.live.msg.CourseIncomeSumMsg;
import com.laka.live.msg.CourseMsg;
import com.laka.live.msg.CourseReplyMsg;
import com.laka.live.msg.DataMsg;
import com.laka.live.msg.EarningsMsg;
import com.laka.live.msg.FansListMsg;
import com.laka.live.msg.FeatureMsg;
import com.laka.live.msg.FeatureRoomListMsg;
import com.laka.live.msg.FollowCourseMsg;
import com.laka.live.msg.FollowNewsMsg;
import com.laka.live.msg.FollowsListMsg;
import com.laka.live.msg.FollowsMsg;
import com.laka.live.msg.FormulaMsg;
import com.laka.live.msg.FoundMsg;
import com.laka.live.msg.GetQinNiuUpLoadTokenMsg;
import com.laka.live.msg.HomeBannersMsg;
import com.laka.live.msg.HomeCourseMsg;
import com.laka.live.msg.HomeFunctionMsg;
import com.laka.live.msg.HomeHotTopicsMsg;
import com.laka.live.msg.HomeLivingMsg;
import com.laka.live.msg.HomeRecommendMsg;
import com.laka.live.msg.HotTopicMsg;
import com.laka.live.msg.LiveListMsg;
import com.laka.live.msg.Msg;
import com.laka.live.msg.MusicMsg;
import com.laka.live.msg.NewestCourseMsg;
import com.laka.live.msg.OpenLiveMsg;
import com.laka.live.msg.PayCourseMsg;
import com.laka.live.msg.ProductsListMsg;
import com.laka.live.msg.QiNiuUploadMsg;
import com.laka.live.msg.QueryLatestRoomListMsg;
import com.laka.live.msg.QueryPayCourseMsg;
import com.laka.live.msg.QueryRankingListMsg;
import com.laka.live.msg.QueryRoomMsg;
import com.laka.live.msg.QueryTagMsg;
import com.laka.live.msg.QueryUpdateMsg;
import com.laka.live.msg.RecommendGoodsMsg;
import com.laka.live.msg.RecommendListMsg;
import com.laka.live.msg.ReleaseNewsMsg;
import com.laka.live.msg.ReleaseTrailerMsg;
import com.laka.live.msg.RoomListMsg;
import com.laka.live.msg.SearchGoodsHotKeywordMsg;
import com.laka.live.msg.SearchHotKeywordMsg;
import com.laka.live.msg.SearchMsg;
import com.laka.live.msg.SearchRecommendGoodsMsg;
import com.laka.live.msg.SingleMsg;
import com.laka.live.msg.SplashMsg;
import com.laka.live.msg.StartRecordMsg;
import com.laka.live.msg.StsToken;
import com.laka.live.msg.TopicListMsg;
import com.laka.live.msg.TransactionRecordListMsg;
import com.laka.live.msg.UserMsg;
import com.laka.live.msg.VideosListMsg;
import com.laka.live.network.upload.UploadManager;
import com.laka.live.shopping.network.HttpCallbackAdapter;
import com.laka.live.shopping.network.HttpManager;
import com.laka.live.shopping.network.HttpMethod;
import com.laka.live.shopping.network.HttpUrls;
import com.laka.live.shopping.network.IHttpManager;
import com.laka.live.thread.BackgroundThread;
import com.laka.live.util.ChannelUtil;
import com.laka.live.util.Common;
import com.laka.live.util.ImageUtil;
import com.laka.live.util.Log;
import com.laka.live.util.StringUtils;
import com.laka.live.util.Utils;
import com.laka.live.video.constant.VideoApiConstant;
import com.laka.live.video.model.http.bean.MaterialCountResponseBean;
import com.laka.live.video.model.http.bean.VideoCommentListResponseBean;
import com.laka.live.video.model.http.bean.VideoCommentReplyListResponseBean;
import com.laka.live.video.model.http.bean.VideoDetailResponseBean;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by luwies on 16/3/8.
 * 请求网络获取数据类
 */
public class DataProvider {

    private static final String TAG = "DataProvider";

    /**
     * 获取手机验证码
     *
     * @param phone    手机号码
     * @param listener 回调结果
     */
    public static void getPhoneVerifyCode(Context tag,
                                          String phone, GsonHttpConnection.OnResultListener<Msg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.PHONE, phone);
        GsonHttpConnection.getInstance().post(tag, Common.GET_PHONE_VERIFY_CODE_URL,
                params, Msg.class, listener);
    }

    /**
     * @param tag
     * @param phone    手机号码
     * @param pcv      收到的验证码
     * @param listener 回调
     */
    public static void loginWithPhone(Object tag, Context context, String phone, String pcv,
                                      GsonHttpConnection.OnResultListener<UserMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.PHONE, phone);
        params.put(Common.PVC, pcv);
        addSystemInfo(params);
        addPushId(params, context);
        GsonHttpConnection.getInstance().post(tag, Common.LOGIN_WITH_PHONE_URL,
                params, UserMsg.class, listener);
    }

    /**
     * @param tag
     * @param code     微信返回的code
     * @param listener 回调
     */
    public static void loginWithWechat(Object tag, Context context, String code,
                                       GsonHttpConnection.OnResultListener<UserMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.CODE, code);
        addSystemInfo(params);
        addPushId(params, context);
        GsonHttpConnection.getInstance().post(tag, Common.LOGIN_WITH_WECHAT_URL,
                params, UserMsg.class, listener);
    }

    private static void addSystemInfo(HashMap<String, String> params) {
        if (params != null) {
            params.put(Common.CHANNEL, ChannelUtil.getChannel(LiveApplication.getInstance()));
            params.put(Common.PLATFORM, Common.ANDROID);
            params.put(Common.OS_VERSION, android.os.Build.MODEL + "," + android.os.Build.VERSION.RELEASE);
        }
    }

    public static void loginWithSinaWeibo(Object tag, Context context, String accessToken, String uid,
                                          GsonHttpConnection.OnResultListener<UserMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.ACCESS_TOKEN, accessToken);
        params.put(Common.UID, uid);
        addSystemInfo(params);
        addPushId(params, context);
        GsonHttpConnection.getInstance().post(tag, Common.LOGIN_WITH_SINA_WEIBO_URL,
                params, UserMsg.class, listener);
    }

    public static void loginWithQQ(Object tag, Context context, String accessToken, String openId,
                                   GsonHttpConnection.OnResultListener<UserMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.ACCESS_TOKEN, accessToken);
        params.put(Common.OPENID, openId);
        addSystemInfo(params);
        addPushId(params, context);
        GsonHttpConnection.getInstance().post(tag, Common.LOGIN_WITH_QQ_URL,
                params, UserMsg.class, listener);
    }

    private static void addPushId(HashMap<String, String> params, Context context) {
        if (params != null) {
            params.put(Common.JPUSH_ID, JPushInterface.getRegistrationID(context));
        }
    }

    public static void getUserInfo(Object tag, GsonHttpConnection.OnResultListener<UserMsg> listener) {
        getUserInfo(tag, "", "", false, false, true, listener);
    }

    public static void getUserInfo(Object tag, String userId, boolean isQueryFollow,
                                   GsonHttpConnection.OnResultListener<UserMsg> listener) {
        getUserInfo(tag, userId, "", isQueryFollow, false, false, listener);
    }

    public static void getUserInfo(Object tag, String userId, String roomId, boolean isQueryFollow,
                                   GsonHttpConnection.OnResultListener<UserMsg> listener) {
        getUserInfo(tag, userId, roomId, isQueryFollow, false, false, listener);
    }

    public static void getUserInfo(Object tag, String userId, boolean isQueryFollow, boolean isQueryBlock, boolean isQueryWithdrawal,
                                   GsonHttpConnection.OnResultListener<UserMsg> listener) {
        getUserInfo(tag, userId, "", isQueryFollow, isQueryBlock, isQueryWithdrawal, listener);
    }

    public static void getUserInfo(Object tag, String userId, String roomId, boolean isQueryFollow, boolean isQueryBlock, boolean isQueryWithdrawal,
                                   GsonHttpConnection.OnResultListener<UserMsg> listener) {
        Log.d(TAG, " getUserInfo userId=" + userId);
        HashMap<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(userId)) {
            params.put(Common.USER_ID, userId);
        }
        params.put(Common.TOKEN, AccountInfoManager.getInstance().getCurrentAccountToken());
        StringBuilder fields = new StringBuilder(Common.SEND_RANKING_TOP3);
        if (isQueryFollow) {
            fields.append(Common.FIELDS_SEPARATOR).append(Common.FOLLOW);
        }
        if (isQueryBlock) {
            fields.append(Common.FIELDS_SEPARATOR).append(Common.BLOCK);
        }

        if (isQueryWithdrawal) {
            fields.append(Common.FIELDS_SEPARATOR).append(Common.CASH_WITHDRAWAL);
        }

        if (!StringUtils.isEmpty(roomId)) {
            params.put(Common.ROOM, roomId);
            fields.append(Common.FIELDS_SEPARATOR).append(Common.USER_TAG);
            fields.append(Common.FIELDS_SEPARATOR).append(Common.FORBIDDEN_STATE);
        }
        params.put(Common.FIELDS, fields.toString());
        GsonHttpConnection.getInstance().post(tag, Common.GET_USERINFO_URL,
                params, UserMsg.class, listener);
    }

    /**
     * 修改用户信息
     *
     * @param tag
     * @param params
     * @param listener
     */
    public static void editUserInfo(Object tag, HashMap<String, String> params,
                                    GsonHttpConnection.OnResultListener<Msg> listener) {

        if (params == null) {
            params = new HashMap<>();
        }
        addToken(params);
        GsonHttpConnection.getInstance().post(tag, Common.EDIT_USERINFO_URL,
                params, Msg.class, listener);
    }


    /**
     * 获取直播详情
     */
    public static void getCourseDetail(Object tag, String course_id,
                                       GsonHttpConnection.OnResultListener<CourseDetailMsg> listener) {

        HashMap<String, String> params = new HashMap<>();
        params.put("course_id", course_id);
        addToken(params);
        GsonHttpConnection.getInstance().post(tag, Common.COURSE_DETAIL,
                params, CourseDetailMsg.class, listener);

    }

    /**
     * 统计观看数
     */
    public static void postSeePlayCount(Object tag, String course_id, GsonHttpConnection.OnResultListener<FormulaMsg> listener) {

        HashMap<String, String> params = new HashMap<>();
        params.put("course_id", course_id);
        addToken(params);
        GsonHttpConnection.getInstance().post(tag, Common.WATCH_COUNT,
                params, FormulaMsg.class, listener);

    }

    /**
     * 获取直播详情
     * 0=新浪微博，1=微信朋友圈，2=QQ，3=QQ空间，4=微信，404=其他
     */
    public static void postShareCount(Object tag, int type) {

        HashMap<String, String> params = new HashMap<>();
        params.put("type", String.valueOf(type));
        addToken(params);
        GsonHttpConnection.getInstance().post(tag, Common.SHARE_COUNT,
                params, Msg.class, null);

    }


    /**
     * 获取用户认证信息
     */
    public static void getApprove(Object tag, GsonHttpConnection.OnResultListener<CertificateInfoMsg> listener) {

        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        GsonHttpConnection.getInstance().post(tag, Common.USER_APPROVE,
                params, CertificateInfoMsg.class, listener);

    }

    // 请求服务器获取oss上传临时token，app使用token上传视频等文件到阿里云 oss 服务器上
    public static void getStsToken(Object tag, GsonHttpConnection.OnResultListener<StsToken> listener) {

        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        GsonHttpConnection.getInstance().post(tag, Common.GET_STS_TOKEN,
                params, StsToken.class, listener);
    }

    // 请求服务器获取oss上传临时token，app使用token上传视频等文件到阿里云 oss 服务器上
    public static void getStsToken(Object tag) {

        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        GsonHttpConnection.getInstance().post(tag, Common.GET_STS_TOKEN,
                params, StsToken.class, new GsonHttpConnection.OnResultListener<StsToken>() {

                    @Override
                    public void onSuccess(StsToken msg) {
                        UploadManager.accessKeyId = msg.id;
                        UploadManager.accessKeySecret = msg.secret;
                        UploadManager.accessKeyToken = msg.token;
                        UploadManager.resetKey();
                    }

                    @Override
                    public void onFail(int errorCode, String errorMsg, String command) {

                    }

                });
    }


    /**
     * 获取配方做法
     */
    public static void getFormula(Object tag, String course_id, GsonHttpConnection.OnResultListener<FormulaMsg> listener) {

        HashMap<String, String> params = new HashMap<>();
        params.put("course_id", course_id);
        addToken(params);
        GsonHttpConnection.getInstance().post(tag, Common.GET_FORMULA,
                params, FormulaMsg.class, listener);
    }

    /**
     * 购买课程
     */
    public static void payCourse(Object tag, HashMap<String, String> params,
                                 GsonHttpConnection.OnResultListener<PayCourseMsg> listener) {

        addToken(params);
        GsonHttpConnection.getInstance().post(tag, Common.PAY_COURSE,
                params, PayCourseMsg.class, listener);

    }

    /**
     * 获取支付清单
     */
    public static void getPayDetail(Object tag, HashMap<String, String> params,
                                    GsonHttpConnection.OnResultListener<QueryPayCourseMsg> listener) {

        addToken(params);
        GsonHttpConnection.getInstance().post(tag, Common.PAY_DETAIL,
                params, QueryPayCourseMsg.class, listener);

    }

    /**
     * 发布预告
     */
    public static void postPreLive(Object tag, HashMap<String, String> params,
                                   GsonHttpConnection.OnResultListener<ReleaseTrailerMsg> listener) {
        if (params == null) {
            params = new HashMap<>();
        }
        addToken(params);
        GsonHttpConnection.getInstance().post(tag, Common.RELEASE_TRAILER,
                params, ReleaseTrailerMsg.class, listener);

    }

    /**
     * 发布资讯
     */
    public static void postNews(Object tag, HashMap<String, String> params,
                                GsonHttpConnection.OnResultListener<ReleaseNewsMsg> listener) {
        if (params == null) {
            params = new HashMap<>();
        }
        addToken(params);
        GsonHttpConnection.getInstance().post(tag, Common.RELEASE_NEWS,
                params, ReleaseNewsMsg.class, listener);

    }

    /**
     * 更新预告
     */
    public static void updatePreLive(Object tag, HashMap<String, String> params,
                                     GsonHttpConnection.OnResultListener<ReleaseTrailerMsg> listener) {
        if (params == null) {
            params = new HashMap<>();
        }
        addToken(params);
        GsonHttpConnection.getInstance().post(tag, Common.UPDATE_TRAILER,
                params, ReleaseTrailerMsg.class, listener);

    }

    /**
     * 获取预告视频
     */
    public static void getNoticeInfo(Object tag,
                                     GsonHttpConnection.OnResultListener<DataMsg> listener) {

        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        GsonHttpConnection.getInstance().post(tag, Common.GET_NOTICE_INFO,
                params, DataMsg.class, listener);

    }

    /**
     * 获取预告视频
     */
    public static void getTrailerDetail(Object tag, String trailer_id,
                                        GsonHttpConnection.OnResultListener<CourseDetailMsg> listener) {

        HashMap<String, String> params = new HashMap<>();
        params.put("trailer_id", trailer_id);
        addToken(params);
        GsonHttpConnection.getInstance().post(tag, Common.TRAILER_DETAIL,
                params, CourseDetailMsg.class, listener);

    }

    /**
     * 我的直播
     * user_id	int
     * 用户id
     * type	int
     * 课程类型，1=直播，2=视频
     * status	int
     * 1=已发布，2：已购买
     */
    public static String queryMyLive(Object tag, String user_id, String type, String page, String status, GsonHttpConnection.OnResultListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("type", type);
        params.put("status", status);
        params.put("user_id", user_id);
        params.put(Common.PAGE, page);
        if (AccountInfoManager.getInstance().getAccountInfo() != null)
            params.put(Common.TOKEN, AccountInfoManager.getInstance().getAccountInfo().getToken());
        return GsonHttpConnection.getInstance().post(tag, Common.QUERY_MY_LIVE, params, LiveListMsg.class, listener);
    }

    public static String queryMyNews(Object tag, String viewed_id, String page, GsonHttpConnection.OnResultListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("viewed_id", viewed_id);
        params.put(Common.PAGE, page);
        if (AccountInfoManager.getInstance().getAccountInfo() != null)
            params.put(Common.TOKEN, AccountInfoManager.getInstance().getAccountInfo().getToken());
        return GsonHttpConnection.getInstance().post(tag, Common.MY_NEWS, params, LiveListMsg.class, listener);
    }

    /**
     * 删除课堂
     */
    public static void deleteCourses(Object tag, HashMap<String, String> params,
                                     GsonHttpConnection.OnResultListener<Msg> listener) {
        if (params == null) {
            params = new HashMap<>();
        }
        addToken(params);
        GsonHttpConnection.getInstance().post(tag, Common.DELETE_TRAILER,
                params, Msg.class, listener);

    }

    /**
     * 删除资讯
     */
    public static void deleteNews(Object tag, HashMap<String, String> params,
                                  GsonHttpConnection.OnResultListener<Msg> listener) {
        if (params == null) {
            params = new HashMap<>();
        }
        addToken(params);
        GsonHttpConnection.getInstance().post(tag, Common.DELETE_NEWS,
                params, Msg.class, listener);

    }

    /**
     * @param tag
     * @param token
     * @param key
     * @param uploadToken
     * @param image
     * @param listener
     * @throws FileNotFoundException
     */
    public static void uploadImageToQiNiu(final Object tag,
                                          final String token,
                                          final String key, final String uploadToken, final File image,
                                          final GsonHttpConnection.OnResultListener<QiNiuUploadMsg> listener)
            throws FileNotFoundException {

        BackgroundThread.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, " uploadImageToQiNiu uploadToken=" + uploadToken);
                    byte cmp[] = ImageUtil.compressUploadBitmap(image);
                    PartSource partSource;
                    if (cmp == null) {
                        partSource = new FilePartSource(image);
                    } else {
                        InputStream inputStream = new ByteArrayInputStream(cmp);
                        partSource = new InputStreamPartSource(Common.FILE, inputStream);
                    }

                    StringPart tokenPart = new StringPart(Common.TOKEN, token);
                    StringPart keyPart = new StringPart(Common.KEY, key);
                    FilePart filePart = new FilePart(Common.FILE, partSource, FilePart.IMAGE_CONTENT_TYPE, FilePart.DEFAULT_CHARSET);
                    Part[] parts;
                    if (!Utils.isEmpty(uploadToken)) {
                        StringPart uploadTokenPart = new StringPart(Common.X_UPLOAD_TOKEN, uploadToken);
                        parts = new Part[]{tokenPart, keyPart, uploadTokenPart, filePart};
                    } else {
                        parts = new Part[]{tokenPart, keyPart, filePart};
                    }


                    MultipartEntity entity = new MultipartEntity(parts);

                    MultipartRequest request = new MultipartRequest(Common.QIN_NIU_UPLOAD_URL, listener,
                            entity, GsonHttpConnection.getInstance().getGson(), QiNiuUploadMsg.class);
                    request.setTag(tag);
                    LiveApplication.mQueue.add(request);
                } catch (FileNotFoundException e) {
                    if (listener != null) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onFail(Msg.FILE_NOT_FOUND_ERROR, "file not found", "");
                            }
                        });

                    }
                }

            }
        });

    }


    public static void getQinNiuUpLoadToken(Object tag, String type, GsonHttpConnection.OnResultListener<GetQinNiuUpLoadTokenMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        if (!Utils.isEmpty(type)) {
            params.put(Common.TYPE, type);
        }
        GsonHttpConnection.getInstance().post(tag, Common.GET_QINIU_UPLOAD_TOKEN_URL,
                params, GetQinNiuUpLoadTokenMsg.class, listener);
    }

    /**
     * 登出
     */
    public static void logout(GsonHttpConnection.OnResultListener listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        GsonHttpConnection.getInstance().post(null, Common.LOGOUT_URL, params, Msg.class, listener);
    }

    public static String queryFan(Object tag, String userId, int page, GsonHttpConnection.OnResultListener<FansListMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.TOKEN, AccountInfoManager.getInstance().getCurrentAccountToken());
        params.put(Common.USER_ID, userId);
        params.put(Common.PAGE, String.valueOf(page));

        return GsonHttpConnection.getInstance().post(tag, Common.QUERY_FAN_URL, params, FansListMsg.class, listener);
    }

    /**
     * 获取拉流地址
     */
    public static String openLive(Object tag, String courseId, GsonHttpConnection.OnResultListener<OpenLiveMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("course_id", courseId);
        addToken(params);
        return GsonHttpConnection.getInstance().post(tag, Common.OPEN_LIVE_URL, params, OpenLiveMsg.class, listener);
    }

    /**
     * 获取测试拉流地址
     */
    public static String openTestLive(Object tag, int prepare, GsonHttpConnection.OnResultListener<OpenLiveMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put("prepare", String.valueOf(prepare));//是否准备中，1是，0否，默认否。
        return GsonHttpConnection.getInstance().post(tag, Common.OPEN_TEST_LIVE_URL, params, OpenLiveMsg.class, listener);
    }

    /**
     * 开启录像
     */
    public static String startRecord(Object tag, GsonHttpConnection.OnResultListener<StartRecordMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
       /* UserInfo myUserInfo = AccountInfoManager.getInstance().getAccountInfo();
        String token = myUserInfo.getToken();
        params.put(Common.TOKEN, token);
        params.put(Common.USER_ID, myUserInfo.getIdStr());*/
        addToken(params);
        return GsonHttpConnection.getInstance().post(tag, Common.START_RECORD_URL, params, StartRecordMsg.class, listener);
    }

    /**
     * 房间信息
     */
    public static String queryRoom(Object tag, String userId, GsonHttpConnection.OnResultListener<QueryRoomMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.TOKEN, AccountInfoManager.getInstance().getCurrentAccountToken());
        params.put(Common.USER_ID, userId);
        return GsonHttpConnection.getInstance().post(tag, Common.QUERY_ROOM_URL, params, QueryRoomMsg.class, listener);
    }

    public static String querySubscribe(Object tag, String userId, int page, String course_id,
                                        GsonHttpConnection.OnResultListener<FollowCourseMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        UserInfo myUserInfo = AccountInfoManager.getInstance().getAccountInfo();
        String token = myUserInfo.getToken();
        params.put(Common.TOKEN, token);
        params.put("course_id", course_id);
        params.put(Common.USER_ID, userId);
        params.put(Common.PAGE, String.valueOf(page));

        return GsonHttpConnection.getInstance().post(tag, Common.QUERY_SUBSCRIBE_URL, params, FollowCourseMsg.class, listener);
    }

    public static String queryFollow(Object tag, String userId, int page, GsonHttpConnection.OnResultListener<FollowsListMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        UserInfo myUserInfo = AccountInfoManager.getInstance().getAccountInfo();
        String token = myUserInfo.getToken();
        params.put(Common.TOKEN, token);
        params.put(Common.USER_ID, userId);
        params.put(Common.PAGE, String.valueOf(page));

        return GsonHttpConnection.getInstance().post(tag, Common.QUERY_FOLLOW_URL, params, FollowsListMsg.class, listener);
    }

    public static String search(Object tag, String keyword, int page, GsonHttpConnection.OnResultListener listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put(Common.KEYWORD, keyword);
        params.put(Common.PAGE, String.valueOf(page));
        return GsonHttpConnection.getInstance().get(tag, Common.SEARCH_URL, params, SearchMsg.class, listener);
    }

    // 默认按综合排序
    public static String searchGoods(Object tag, String keyword, int page, GsonHttpConnection.OnResultListener listener) {
        return searchGoods(tag, keyword, 1, page, listener);
    }


    /**
     * @param tag
     * @param keyword
     * @param sortType 排序类型，1=综合，2=销量，3=最新，4=价格降序，5=价格升序
     * @param page
     * @param listener
     * @return
     */
    public static String searchGoods(Object tag, String keyword, int sortType, int page, GsonHttpConnection.OnResultListener listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);

        params.put(Common.USERID, AccountInfoManager.getInstance().getAccountInfo().getIdStr());
        params.put(Common.SORTTYPE, String.valueOf(sortType));
        params.put(Common.KEYWORDS, URLEncoder.encode(keyword));
        params.put(Common.PAGE, String.valueOf(page));
        return GsonHttpConnection.getInstance().get(tag, Common.SEARCH_GOODS_URL, params, SearchRecommendGoodsMsg.class, listener);
    }


    public static String searchGoods(Object tag, String keyword, int page, Type type, GsonHttpConnection.OnResultListener listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put(Common.SORTTYPE, String.valueOf(1));
        params.put(Common.KEYWORDS, URLEncoder.encode(keyword));
        params.put(Common.PAGE, String.valueOf(page));
        return GsonHttpConnection.getInstance().get(tag, Common.SEARCH_GOODS_URL, params, type, listener);
    }

    public static void getSearchHotKeyword(Object tag, GsonHttpConnection.OnResultListener listener) {

        LiveApplication.mQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                if (TextUtils.equals(request.getUrl(), Common.SEARCH_HOT_KEYWORD_URL)) {
                    return true;
                }
                return false;
            }
        });
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        GsonHttpConnection.getInstance().get(tag, Common.SEARCH_HOT_KEYWORD_URL, params,
                SearchHotKeywordMsg.class, listener);
    }

    public static void getSearchGoodsHotKeyword(final GsonHttpConnection.OnResultListener listener) {

        IHttpManager httpManager = HttpManager.getBusinessHttpManger();

        httpManager.request(HttpUrls.SEARCH_GOODS_HOT_KEYWORD_URL, HttpMethod.GET, SearchGoodsHotKeywordMsg.class, new HttpCallbackAdapter() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                listener.onSuccess(obj);
            }

            @Override
            public void onError(String errorStr, int code) {
                super.onError(errorStr, code);
            }
        });

    }

    // 获取推荐商品
    public static void getRecommendGoods(String courseId, final GsonHttpConnection.OnResultListener<RecommendGoodsMsg> listener) {

        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("courseId", courseId);
        httpManager.request(HttpUrls.GET_RECOMMENDS_GOODS_URL, HttpMethod.GET, RecommendGoodsMsg.class, new HttpCallbackAdapter() {
            @Override
            public <T> void onSuccess(T obj, String result) {
                if (obj == null) {
                    listener.onFail(0, "", "");
                    return;
                }
                listener.onSuccess((RecommendGoodsMsg) obj);
            }

            @Override
            public void onError(String errorStr, int code) {
                listener.onFail(code, errorStr, "");
            }
        });

    }


    public static void queryTag(Object tag, GsonHttpConnection.OnResultListener listener) {
        LiveApplication.mQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                if (TextUtils.equals(request.getUrl(), Common.QUERY_TAG_URL)) {
                    return true;
                }
                return false;
            }
        });
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        GsonHttpConnection.getInstance().get(tag, Common.QUERY_TAG_URL, params, QueryTagMsg.class, listener);
    }

    public static String queryVideo(Object tag, String userId, int page, int limit, GsonHttpConnection.OnResultListener<VideosListMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.LIMIT, String.valueOf(limit));
        params.put(Common.TOKEN, AccountInfoManager.getInstance().getCurrentAccountToken());
        params.put(Common.USER_ID, userId);
        params.put(Common.PAGE, String.valueOf(page));
        if (!TextUtils.equals(userId, AccountInfoManager.getInstance().getCurrentAccountUserIdStr())) {
            params.put(Common.FIELDS, Common.USER);
        }
        return GsonHttpConnection.getInstance().post(tag, Common.QUERY_VIDEO_URL, params, VideosListMsg.class, listener);
    }

    public static void queryEarnings(Object tag, GsonHttpConnection.OnResultListener listener) {

        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        GsonHttpConnection.getInstance().post(tag, Common.QUERY_EARNINGS_URL, params, EarningsMsg.class, listener);
    }

    public static void addToken(HashMap params) {
        addToken(params, true);
    }

    public static void addToken(HashMap params, boolean withId) {
        if (params != null) {
            UserInfo my = AccountInfoManager.getInstance().getAccountInfo();
            if (my != null) {
                params.put(Common.TOKEN, my.getToken());
                if (withId) {
                    params.put(Common.USER_ID, my.getIdStr());
                }
            }
        }
    }

    public static void queryProduct(Object tag, GsonHttpConnection.OnResultListener listener) {

        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put(Common.PLATFORM, Common.ANDROID);
        params.put(Common.FIELDS, Common.BALANCE);
        GsonHttpConnection.getInstance().post(tag, Common.QUERY_PRODUCT_URL, params, ProductsListMsg.class, listener);
    }

    public static String queryHotRoom(Object tag, boolean isRequestBanner, GsonHttpConnection.OnResultListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.SLIDES, String.valueOf(isRequestBanner ? 1 : 0));
        addToken(params);
        return GsonHttpConnection.getInstance().post(tag, Common.QUERY_ROOM_HOT_URL, params, RoomListMsg.class, listener);
    }

    public static String queryFollowVideo(Object tag, int page, int limit, GsonHttpConnection.OnResultListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.PAGE, String.valueOf(page));
        params.put(Common.LIMIT, String.valueOf(limit));
        addToken(params);
        return GsonHttpConnection.getInstance().post(tag, Common.GET_FOLLOWS, params, FollowsMsg.class, listener);
    }

    public static String queryFollowCourse(Object tag, int list_type, String user_id, int page, int limit, GsonHttpConnection.OnResultListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.PAGE, String.valueOf(page));
        params.put(Common.LIMIT, String.valueOf(limit));
        params.put("user_id", String.valueOf(user_id));
        params.put("list_type", String.valueOf(list_type));
        addToken(params);
        return GsonHttpConnection.getInstance().post(tag, Common.GET_FOLLOWS_COURSES, params, CourseMsg.class, listener);
    }

    /**
     * 获取关注Tab课程
     */
    public static String getFollows(Object tag, GsonHttpConnection.OnResultListener<FollowsMsg> listener) {

        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        return GsonHttpConnection.getInstance().post(tag, Common.GET_FOLLOWS,
                params, FollowsMsg.class, listener);

    }


    public static String queryLatestRoom(Object tag, int page, int limit, GsonHttpConnection.OnResultListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.PAGE, String.valueOf(page));
        params.put(Common.LIMIT, String.valueOf(limit));
        addToken(params);
        return GsonHttpConnection.getInstance().post(tag, Common.QUERY_ROOM_LATEST_URL, params, QueryLatestRoomListMsg.class, listener);
    }


    /**
     * 贡献榜
     */
    public static String querySendRanking(Object tag, String userId, GsonHttpConnection.OnResultListener listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put(Common.USER_ID, userId);
        return GsonHttpConnection.getInstance().post(tag, Common.QUERY_SEND_RANKING_URL, params, QueryRankingListMsg.class, listener);
    }

    /**
     * 排行榜
     */
    public static String queryRankingList(Object tag, int type, int list_type, GsonHttpConnection.OnResultListener<QueryRankingListMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put(Common.TYPE, String.valueOf(type));
        params.put(Common.TOP_LIST_TYPE, String.valueOf(list_type));
        return GsonHttpConnection.getInstance().post(tag, Common.QUERY_RANKING_LIST_URL, params, QueryRankingListMsg.class, listener);
    }

    public static void follow(Object tag, int userId, GsonHttpConnection.OnResultListener<Msg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put(Common.FOLLOW_ID, String.valueOf(userId));
        GsonHttpConnection.getInstance().post(tag, Common.FOLLOW_URL, params, Msg.class, listener);
    }

    public static void batchFollow(Object tag, List<Integer> userIds, GsonHttpConnection.OnResultListener<Msg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < userIds.size(); i++) {
            stringBuilder.append(userIds.get(i));
            if (i < userIds.size() - 1) {
                stringBuilder.append(",");
            }
        }
        params.put(Common.UIDS, stringBuilder.toString());
        GsonHttpConnection.getInstance().post(tag, Common.FOLLOW_BATCH_URL, params, Msg.class, listener);
    }

    public static void unFollow(Object tag, int userId, GsonHttpConnection.OnResultListener<Msg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put(Common.UNFOLLOW_ID, String.valueOf(userId));
        GsonHttpConnection.getInstance().post(tag, Common.FOLLOW_URL, params, Msg.class, listener);
    }

    /**
     * 获取全部话题
     */
    public static String queryTopicList(Object tag, int page, int limit, GsonHttpConnection.OnResultListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.PAGE, String.valueOf(page));
        params.put(Common.LIMIT, String.valueOf(limit));
        addToken(params);
        return GsonHttpConnection.getInstance().post(tag, Common.QUERY_TOPIC_LIST_URL, params, TopicListMsg.class, listener);
    }

    /**
     * 获取直播话题推荐
     */
    public static String queryHotTopic(Object tag, GsonHttpConnection.OnResultListener listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        return GsonHttpConnection.getInstance().post(tag, Common.QUERY_HOT_TOPIC_URL, params, HotTopicMsg.class, listener);
    }

    /**
     * 拉黑
     *
     * @param tag
     * @param userId
     * @param listener
     */
    public static void shield(Object tag, String userId, GsonHttpConnection.OnResultListener<Msg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put(Common.BLOCK_ID, userId);
        GsonHttpConnection.getInstance().post(tag, Common.BLOCK_URL, params, Msg.class, listener);
    }

    /**
     * 取消拉黑
     *
     * @param tag
     * @param userId
     * @param listener
     */
    public static void unShield(Object tag, String userId, GsonHttpConnection.OnResultListener<Msg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put(Common.UNBLOCK_ID, userId);
        GsonHttpConnection.getInstance().post(tag, Common.BLOCK_URL, params, Msg.class, listener);
    }

    /**
     * 举报
     *
     * @param tag
     * @param userId
     * @param listener
     */
    public static void report(Object tag, String userId, String courseId, String action, GsonHttpConnection.OnResultListener<Msg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put(Common.ACTION, action);
        params.put(Common.USER_ID, userId);
        params.put(Common.COURSE_ID, courseId);
        GsonHttpConnection.getInstance().post(tag, Common.REPORT_URL, params, Msg.class, listener);
    }

    public static String queryTransactionRecordListMsg(Object tag, int page, GsonHttpConnection.OnResultListener<Msg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put(Common.PAGE, String.valueOf(page));
        return GsonHttpConnection.getInstance().post(tag, Common.QUERY_TRANSACTION_LOG_URL, params,
                TransactionRecordListMsg.class, listener);

    }

    public static String queryCourseIncomeSum(Object tag, GsonHttpConnection.OnResultListener<CourseIncomeSumMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);

        return GsonHttpConnection.getInstance().get(tag, Common.COURSE_INCOMESUM_URL, params,
                CourseIncomeSumMsg.class, listener);
    }

    public static String queryCourseIncomesListMsg(Object tag, int page, GsonHttpConnection.OnResultListener<CourseMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put(Common.PAGE, String.valueOf(page));
        return GsonHttpConnection.getInstance().get(tag, Common.COURSE_INCOMES_URL, params,
                CourseMsg.class, listener);
    }

    /**
     * 获取课程收益明细
     */
    public static String queryCourseIncomeDetails(Object tag, String courseId, int incomeType, int page,
                                                  GsonHttpConnection.OnResultListener<CourseIncomeDetailMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put(Common.PAGE, String.valueOf(page));
        params.put(Common.LIMIT, String.valueOf(LIMIT));
        params.put(Common.COURSE_ID, courseId);
        params.put(Common.INCOME_TYPE, String.valueOf(incomeType));

        return GsonHttpConnection.getInstance().get(tag, Common.COURSE_INCOME_DETAILS, params,
                CourseIncomeDetailMsg.class, listener);
    }

    /**
     * 获取课程收益明细详情
     */
    public static String queryCourseIncomeDetailInfo(Object tag, int id, int incomeType,
                                                     GsonHttpConnection.OnResultListener<CourseIncomeDetailInfoMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put(Common.LIMIT, String.valueOf(LIMIT));
        params.put(Common.ID, String.valueOf(id));
        params.put(Common.INCOME_TYPE, String.valueOf(incomeType));

        return GsonHttpConnection.getInstance().get(tag, Common.COURSE_INCOME_DETAIL_INFO, params,
                CourseIncomeDetailInfoMsg.class, listener);
    }

    public static void updateCheck(Context context, int updateFlag, GsonHttpConnection.OnResultListener<QueryUpdateMsg> listener) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            HashMap<String, String> params = new HashMap<>();
            params.put(Common.PLATFORM, Common.PLATFORM_ANDROID);
            params.put(Common.VERSION, String.valueOf(info.versionCode));
            params.put(Common.CHANNEL, ChannelUtil.getChannel(context));
            params.put(Common.FLAG, String.valueOf(updateFlag));
            Log.d(TAG, "updateCheck PLATFORM=" + Common.PLATFORM_ANDROID + " VERSION=" + String.valueOf(info.versionCode)
                    + " CHANNEL=" + ChannelUtil.getChannel(context) + " FLAG=" + String.valueOf(updateFlag));
//            addToken(params);
            GsonHttpConnection.getInstance().post(null, Common.QUERY_UPDATE_URL, params, QueryUpdateMsg.class,
                    listener);
        } catch (PackageManager.NameNotFoundException e) {

        }
    }

    public static void feedback(Object tag, String feedback, String contact,
                                GsonHttpConnection.OnResultListener<Msg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put(Common.VALUE, feedback);
        if (TextUtils.isEmpty(contact) == false) {
            params.put(Common.PHONE, contact);
        }
        GsonHttpConnection.getInstance().post(tag, Common.FEEDBACK_URL, params, Msg.class, listener);
    }

    public static void bindWechat(Object tag, String code, GsonHttpConnection.OnResultListener<Msg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put(Common.CODE, code);
        GsonHttpConnection.getInstance().post(tag, Common.BIND_WECHAT_URL, params, Msg.class, listener);
    }

    public static void bindPhone(Object tag, String phone, String pvc,
                                 GsonHttpConnection.OnResultListener<Msg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put(Common.PHONE, phone);
        params.put(Common.PVC, pvc);
        GsonHttpConnection.getInstance().post(tag, Common.BIND_PHONE_URL, params, Msg.class, listener);
    }

    // 获取商城订单的微信支付参数
    public static void getOrderWechatPayInfo(String orderId,
                                             final GsonHttpConnection.OnResultListener<OrderPayInfoMsg> listener) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams(Common.ORDER_ID, orderId);
        httpManager.request(HttpUrls.QUERY_ORDER_WECHAT_PAY_URL, HttpMethod.POST, OrderPayInfoMsg.class, new HttpCallbackAdapter() {
            @Override
            public <T> void onSuccess(T obj, String result) {

                listener.onSuccess((OrderPayInfoMsg) obj);
            }

            @Override
            public void onError(String errorStr, int code) {
                super.onError(errorStr, code);
                listener.onFail(code, errorStr, "order/pay/wxpayParams");
            }
        });

    }

    // 获取商城订单的支付宝支付参数
    public static void getOrderAliPayInfo(String orderId,
                                          final GsonHttpConnection.OnResultListener<OrderPayInfoMsg> listener) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams(Common.ORDER_ID, orderId);
        httpManager.request(HttpUrls.QUERY_ORDER_ALI_PAY_URL, HttpMethod.POST, OrderPayInfoMsg.class, new HttpCallbackAdapter() {
            @Override
            public <T> void onSuccess(T obj, String result) {

                listener.onSuccess((OrderPayInfoMsg) obj);
            }

            @Override
            public void onError(String errorStr, int code) {
                super.onError(errorStr, code);
                listener.onFail(code, errorStr, "order/pay/wxpayParams");
            }
        });

    }

    public static void getWechatPayInfo(Object tag, String productId,
                                        GsonHttpConnection.OnResultListener<SingleMsg<WechatPayInfo>> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put(Common.PRODUCT_ID, productId);
        GsonHttpConnection.getInstance().post(tag, Common.QUERY_WECHAT_PAY_URL, params,
                new TypeToken<SingleMsg<WechatPayInfo>>() {
                }.getType(), listener);
    }

    public static void postJPushId(String id) {
        if (!TextUtils.isEmpty(id))
            setJPushId(id, null);
    }

    public static void setJPushId(String id, GsonHttpConnection.OnResultListener listener) {

        Log.d(TAG, " setJPushId id=" + id);
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put(Common.JPUSH_ID, id);
        GsonHttpConnection.getInstance().post(null, Common.SET_JPUSH_ID_URL, params, Msg.class, listener);
    }

    public static void cashWithdrawal(Object tag, int cash, GsonHttpConnection.OnResultListener<Msg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put(Common.CASH, String.valueOf(cash));
        GsonHttpConnection.getInstance().post(tag, Common.CASH_WITHDRAWAL_URL, params, Msg.class, listener);
    }

    public static void exchange(Object tag, long coins, GsonHttpConnection.OnResultListener<Msg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put(Common.COINS, String.valueOf(coins));
        GsonHttpConnection.getInstance().post(tag, Common.EXCHANGE_URL, params, Msg.class, listener);
    }

    public static void removeVideo(Object tag, String time, GsonHttpConnection.OnResultListener<Msg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put(Common.TIME, time);
        GsonHttpConnection.getInstance().post(tag, Common.REMOVE_VIDEO_URL, params, Msg.class, listener);
    }

    public static String queryAllFeature(Object tag, int page, int limit,
                                         GsonHttpConnection.OnResultListener<FeatureMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put(Common.PAGE, String.valueOf(page));
        params.put(Common.LIMIT, String.valueOf(limit));
        return GsonHttpConnection.getInstance().post(tag, Common.QUERY_CHAR_FIELD_ALL_URL, params, FeatureMsg.class, listener);
    }

    public static String queryFeature(Object tag, int charid, int page, int limit,
                                      GsonHttpConnection.OnResultListener<FeatureRoomListMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put(Common.CHARID, String.valueOf(charid));
        params.put(Common.PAGE, String.valueOf(page));
        params.put(Common.LIMIT, String.valueOf(limit));
        return GsonHttpConnection.getInstance().post(tag, Common.QUERY_CHAR_FIELD_URL, params,
                FeatureRoomListMsg.class, listener);
    }

    public static void getSystemConfig(Context context,
                                       GsonHttpConnection.OnResultListener<SingleMsg<SystemConfig.Config>> listener) {
        if (context == null) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.PLATFORM, Common.ANDROID);
        params.put(Common.VERSION, String.valueOf(Utils.getVersionCode(context)));
        GsonHttpConnection.getInstance().get(null, Common.GET_SYSTEM_CONFIG_URL, params,
                new TypeToken<SingleMsg<SystemConfig.Config>>() {
                }.getType(), listener);
    }

    public static String getTopicRoomList(Object tag, String topicId, int page, int limit,
                                          GsonHttpConnection.OnResultListener<FeatureRoomListMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put(Common.TOPIC_ID, topicId);
        params.put(Common.PAGE, String.valueOf(page));
        params.put(Common.LIMIT, String.valueOf(limit));
        return GsonHttpConnection.getInstance().get(tag, Common.GET_TOPIC_ROOM_LIST, params,
                FeatureRoomListMsg.class, listener);
    }


    public static String queryFind(Object tag, GsonHttpConnection.OnResultListener<RoomListMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        return GsonHttpConnection.getInstance().post(tag, Common.QUERY_FIND, params,
                RoomListMsg.class, listener);
    }

    public static String searchSongs(Object tag, int page, int limit, String keyword,
                                     GsonHttpConnection.OnResultListener<MusicMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put(Common.PAGE, String.valueOf(page));
        params.put(Common.LIMIT, String.valueOf(limit));
        params.put(Common.KEYWORD, keyword);
        return GsonHttpConnection.getInstance().post(tag, Common.SEARCH_SONGS_URL, params,
                MusicMsg.class, listener);
    }

    public static String getSongHotWord(Object tag, int limit,
                                        GsonHttpConnection.OnResultListener<SearchHotKeywordMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put(Common.LIMIT, String.valueOf(limit));
        return GsonHttpConnection.getInstance().post(tag, Common.GET_HOT_SONGS_URL, params,
                SearchHotKeywordMsg.class, listener);
    }

    public static String recommendList(Object tag, GsonHttpConnection.OnResultListener<RecommendListMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        UserInfo myUserInfo = AccountInfoManager.getInstance().getAccountInfo();
        params.put(Common.TOKEN, myUserInfo.getToken());
        params.put(Common.USER_ID, myUserInfo.getId() + "");
        return GsonHttpConnection.getInstance().post(tag, Common.RECOMMEND_LIST_URL, params, RecommendListMsg.class, listener);
    }


    /**
     * 查询资讯列表
     */
    public static String queryNewsList(Object tag, int type, int page, GsonHttpConnection.OnResultListener<HomeRecommendMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.TYPE, String.valueOf(type));
        params.put(Common.PAGE, String.valueOf(page));
        addToken(params);
        return GsonHttpConnection.getInstance().post(tag, Common.QUERY_NEWS_LIST, params, HomeRecommendMsg.class, listener);
    }

    /**
     * 查询课堂首页推荐
     */
    public static String getCourseLiveList(Object tag, int page, GsonHttpConnection.OnResultListener<HomeRecommendMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.PAGE, String.valueOf(page));
        addToken(params);
        return GsonHttpConnection.getInstance().get(tag, Common.GET_COURSE_LIVE, params, HomeRecommendMsg.class, listener);
    }

    /**
     * 查询课堂首页推荐
     */
    public static String getCourseVideoList(Object tag, int page, GsonHttpConnection.OnResultListener<HomeRecommendMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.PAGE, String.valueOf(page));
        addToken(params);
        return GsonHttpConnection.getInstance().get(tag, Common.GET_COURSE_VIDEO, params, HomeRecommendMsg.class, listener);
    }

    /**
     * 查询课程分类
     */
    public static String getCourseCategory(Object tag, int listType, int category_id, int page, int limit,
                                           GsonHttpConnection.OnResultListener<HomeRecommendMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.LIST_TYPE, String.valueOf(listType));
        params.put(Common.CATEGORY_ID, String.valueOf(category_id));
        params.put(Common.PAGE, String.valueOf(page));
        params.put(Common.LIMIT, String.valueOf(limit));
        addToken(params);
        return GsonHttpConnection.getInstance().get(tag, Common.QUERY_COURSE_CATEGORY, params, HomeRecommendMsg.class, listener);
    }

    /**
     * 查询首页推荐
     */
    public static String queryHomeRecommend(Object tag, int type, GsonHttpConnection.OnResultListener<HomeRecommendMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        //params.put(Common.LIST_TYPE, String.valueOf(type));
        addToken(params);
        return GsonHttpConnection.getInstance().get(tag, Common.COURSE_RECOMMEND, params, HomeRecommendMsg.class, listener);
    }

    /**
     * 查询首页发现
     */
    public static String queryHomeFind(Object tag, int limit, int start, GsonHttpConnection.OnResultListener listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.LIMIT, String.valueOf(limit));
        params.put(Common.START, String.valueOf(start));
        addToken(params);
        return GsonHttpConnection.getInstance().post(tag, Common.QUERY_FIND, params, HomeCourseMsg.class, listener);
    }

    /**
     * 查询首页发现--临时请求，后面要删除
     */
    public static String queryFind(Object tag, boolean isRequestTopics, boolean isRequestTopicsLive,
                                   GsonHttpConnection.OnResultListener<FoundMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        params.put(Common.TOPICS, String.valueOf(isRequestTopics ? 1 : 0));
        params.put(Common.TOPICS_LIVE, String.valueOf(isRequestTopicsLive ? 1 : 0));
        return GsonHttpConnection.getInstance().post(tag, Common.QUERY_FIND, params,
                FoundMsg.class, listener);
    }

    /**
     * 查询首页Banner
     */
    public static String queryHomeBanners(Object tag, GsonHttpConnection.OnResultListener<HomeBannersMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        return GsonHttpConnection.getInstance().get(tag, Common.QUERY_HOME_BANNERS_URL, params, HomeBannersMsg.class, listener);
    }

    /**
     * 查询首页功能
     */
    public static String queryHomeFunction(Object tag, GsonHttpConnection.OnResultListener<HomeFunctionMsg> listener) {
        return GsonHttpConnection.getInstance().get(tag, Common.QUERY_HOME_FUNCTION_URL, null, HomeFunctionMsg.class, listener);
    }

    /**
     * 查询首页话题
     */
    public static String queryHomeTopics(Object tag, GsonHttpConnection.OnResultListener listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params);
        return GsonHttpConnection.getInstance().post(tag, Common.QUERY_HOME_TOPICS_URL, params, TopicListMsg.class, listener);
    }

    /**
     * 查询首页正在直播
     */
    public static String queryHomeLiving(Object tag, GsonHttpConnection.OnResultListener<HomeLivingMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params, true);
        return GsonHttpConnection.getInstance().get(tag, Common.QUERY_HOME_LIVE_URL, params,
                HomeLivingMsg.class, listener);
    }

    /**
     * 首页热门话题
     */
    public static String queryHomeHotTopics(Object tag, int page, int limit, GsonHttpConnection.OnResultListener<HomeHotTopicsMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.PAGE, String.valueOf(page));
        params.put(Common.LIMIT, String.valueOf(limit));
        addToken(params);
        return GsonHttpConnection.getInstance().post(tag, Common.QUERY_HOT_TOPICS, params,
                HomeHotTopicsMsg.class, listener);
    }

    /**
     * 获取用户一个最新的课程
     */
    public static String getNewestCourse(Object tag, String viewedId, GsonHttpConnection.OnResultListener<NewestCourseMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("viewed_id", viewedId);
        addToken(params);
        return GsonHttpConnection.getInstance().post(tag, Common.NEWEST_COURSE_URL, params, NewestCourseMsg.class, listener);
    }

    /**
     * 关注动态
     */
    public static String queryFollowNews(Object tag, int page, int limit, GsonHttpConnection.OnResultListener<FollowNewsMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.PAGE, String.valueOf(page));
        params.put(Common.LIMIT, String.valueOf(limit));
        addToken(params);

        return GsonHttpConnection.getInstance().post(tag, Common.FOLLOW_NEWS_URL, params, FollowNewsMsg.class, listener);
    }

    public final static int LIMIT = 20;

    /**
     * 获取课程评论列表
     */
    public static String queryCourseComment(Object tag, String courseId, int page,
                                            GsonHttpConnection.OnResultListener<CourseCommentMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.PAGE, String.valueOf(page));
        params.put(Common.LIMIT, String.valueOf(LIMIT));
        params.put(Common.COURSE_ID, courseId);

        return GsonHttpConnection.getInstance().get(tag,
                Common.COURSE_COMMENTS_URL, params, CourseCommentMsg.class, listener);
    }

    /**
     * 获取课程回复列表
     */
    public static String queryCourseReplies(Object tag, int commentId, int page,
                                            GsonHttpConnection.OnResultListener<CourseReplyMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.PAGE, String.valueOf(page));
        params.put(Common.LIMIT, String.valueOf(LIMIT));
        params.put(Common.COMMENT_ID, String.valueOf(commentId));

        return GsonHttpConnection.getInstance().get(tag,
                Common.COURSE_REPLIES_URL, params, CourseReplyMsg.class, listener);
    }

    /**
     * 发布课程评论
     */
    public static String commentCourse(Object tag, String courseId, int commentId, String content,
                                       GsonHttpConnection.OnResultListener<CommentMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.COMMENT_ID, String.valueOf(commentId));
        params.put(Common.COURSE_ID, courseId);
        params.put(Common.CONTENT, content);

        addToken(params, true);

        return GsonHttpConnection.getInstance().post(tag,
                Common.COURSE_COMMENT_URL, params, CommentMsg.class, listener);
    }

    /**
     * 点赞评论
     */
    public static String praiseComment(Object tag, int commentId, int flag,
                                       GsonHttpConnection.OnResultListener<Msg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.COMMENT_ID, String.valueOf(commentId));
        params.put(Common.FLAG, String.valueOf(flag));

        return GsonHttpConnection.getInstance().get(tag,
                Common.COURSE_PRAISE_URL, params, Msg.class, listener);
    }

    /**
     * 闪屏
     */
    public static String querySplash(Object tag, GsonHttpConnection.OnResultListener<SplashMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addSystemInfo(params);

        return GsonHttpConnection.getInstance().get(tag,
                Common.SPLASH_URL, params, SplashMsg.class, listener);
    }

    /**
     * 一元课程
     */
    public static String queryCheapCourse(Object tag, int page,
                                          GsonHttpConnection.OnResultListener<CourseMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.PAGE, String.valueOf(page));
        params.put(Common.LIMIT, String.valueOf(LIMIT));

        addToken(params, true);
        return GsonHttpConnection.getInstance().get(tag,
                Common.CHEAP_COURSE_URL, params, CourseMsg.class, listener);
    }

    /**
     * 最新课程
     */
    public static String queryNewCourse(Object tag, int page,
                                        GsonHttpConnection.OnResultListener<CourseMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.PAGE, String.valueOf(page));
        params.put(Common.LIMIT, String.valueOf(LIMIT));

        addToken(params, true);
        return GsonHttpConnection.getInstance().get(tag,
                Common.NEW_COURSE_URL, params, CourseMsg.class, listener);
    }

    /**
     * 优质课程
     */
    public static String queryBestCourse(Object tag, int page,
                                         GsonHttpConnection.OnResultListener<CourseMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.PAGE, String.valueOf(page));
        params.put(Common.LIMIT, String.valueOf(LIMIT));

        addToken(params, true);
        return GsonHttpConnection.getInstance().get(tag,
                Common.BEST_COURSE_URL, params, CourseMsg.class, listener);
    }

    /**
     * 免费课程
     */
    public static String queryFreeCourse(Object tag, int page,
                                         GsonHttpConnection.OnResultListener<CourseMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.PAGE, String.valueOf(page));
        params.put(Common.LIMIT, String.valueOf(LIMIT));

        addToken(params, true);
        return GsonHttpConnection.getInstance().get(tag,
                Common.FREE_COURSE_URL, params, CourseMsg.class, listener);
    }

    /**
     * 限时优惠课程
     */
    public static String queryLimitCourse(Object tag, int page,
                                          GsonHttpConnection.OnResultListener<CourseMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.PAGE, String.valueOf(page));
        params.put(Common.LIMIT, String.valueOf(LIMIT));

        addToken(params, true);
        return GsonHttpConnection.getInstance().get(tag,
                Common.LIMIT_COURSE_URL, params, CourseMsg.class, listener);
    }

    /**
     * 热门课程
     */
    public static String queryHotCourse(Object tag, int page,
                                        GsonHttpConnection.OnResultListener<CourseMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.PAGE, String.valueOf(page));
        params.put(Common.LIMIT, String.valueOf(LIMIT));

        addToken(params, true);
        return GsonHttpConnection.getInstance().get(tag,
                Common.HOTTEST_COURSE_URL, params, CourseMsg.class, listener);
    }

    /**
     * 获取课程内容一级分类
     */
    public static String getCourseCategoryOne(Object tag, GsonHttpConnection.OnResultListener<CourseCategoryOneMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params, true);
        return GsonHttpConnection.getInstance().get(tag,
                Common.COURSE_CATEGORY_ONE_URL, params, CourseCategoryOneMsg.class, listener);
    }

    /**
     * 获取课程内容二级分类
     */
    public static String getCourseCategoryTwo(Object tag, int cateId, GsonHttpConnection.OnResultListener<CourseCategoryTwoMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("category_id", String.valueOf(cateId));
        addToken(params, true);
        return GsonHttpConnection.getInstance().get(tag,
                Common.COURSE_CATEGORY_TWO_URL, params, CourseCategoryTwoMsg.class, listener);
    }

    /**
     * 获取课程内容
     */
    public static String getCourseContent(Object tag, GsonHttpConnection.OnResultListener<ContentMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params, true);
        return GsonHttpConnection.getInstance().get(tag,
                Common.COURSE_CONTENT_COLUMNS_URL, params, ContentMsg.class, listener);
    }

    /**
     * 获取优选专题详细
     */
    public static String getCourseTopicsDetail(Object tag, int topicId, int page, GsonHttpConnection.OnResultListener<BestTopicMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("topicId", String.valueOf(topicId));
        params.put("page", String.valueOf(page));
        addToken(params, true);
        return GsonHttpConnection.getInstance().get(tag,
                Common.COURSE_COURSE_TOPICS_DETAIL_URL, params, BestTopicMsg.class, listener);
    }

    /**
     * 获取素材库个数
     *
     * @param tag
     * @param listener
     * @return
     */
    @Deprecated
    public static String getMaterialCount(Object tag, GsonHttpConnection.OnResultListener<MaterialCountResponseBean> listener) {
        HashMap<String, String> params = new HashMap<>();
        addToken(params, true);
        return GsonHttpConnection.getInstance().post(tag,
                VideoApiConstant.GET_MATERIAL_COUNT_API, params, MaterialCountResponseBean.class, listener);
    }


    /**
     * 获取小视频评论列表
     */
    public static String getVideoComments(Object tag, String videoId, int page,
                                          GsonHttpConnection.OnResultListener<VideoCommentListResponseBean> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.PAGE, String.valueOf(page));
        params.put(Common.LIMIT, String.valueOf(LIMIT));
        params.put(Common.MINI_VIDEO_ID, videoId);

        return GsonHttpConnection.getInstance().get(tag,
                VideoApiConstant.GET_VIDEO_COMMENT_LIST_API, params, VideoCommentListResponseBean.class, listener);
    }

    /**
     * 获取小视频回复列表
     */
    public static String queryVideoReplies(Object tag, int commentId, int page,
                                           GsonHttpConnection.OnResultListener<VideoCommentReplyListResponseBean> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.PAGE, String.valueOf(page));
        params.put(Common.LIMIT, String.valueOf(LIMIT));
        params.put(Common.COMMENT_ID, String.valueOf(commentId));

        return GsonHttpConnection.getInstance().get(tag,
                VideoApiConstant.GET_SUB_COMMENT_API, params, VideoCommentReplyListResponseBean.class, listener);
    }

    /**
     * 发布小视频评论
     */
    public static String commentVideo(Object tag, String videoId, int commentId, String content,
                                      GsonHttpConnection.OnResultListener<CommentMsg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.COMMENT_ID, String.valueOf(commentId));
        params.put(Common.MINI_VIDEO_ID, videoId);
        params.put(Common.CONTENT, content);

        addToken(params, true);

        return GsonHttpConnection.getInstance().post(tag,
                VideoApiConstant.POST_COMMENT_API, params, CommentMsg.class, listener);
    }

    /**
     * 点赞小视频评论
     */
    public static String praiseVideoComment(Object tag, int miniVideoId,
                                            GsonHttpConnection.OnResultListener<Msg> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.MINI_VIDEO_ID, String.valueOf(miniVideoId));

        return GsonHttpConnection.getInstance().get(tag,
                VideoApiConstant.LIKE_VIDEO_API, params, Msg.class, listener);
    }


    /**
     * 获取小视频详情
     */
    public static String getVideoDetailInfo(Object tag, String videoId,
                                            GsonHttpConnection.OnResultListener<VideoDetailResponseBean> listener) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Common.MINI_VIDEO_ID, videoId);

        //这里的URL需要更换一下
        return GsonHttpConnection.getInstance().get(tag,
                VideoApiConstant.GET_VIDEO_INFO_API, params, VideoDetailResponseBean.class, listener);
    }
}
