package com.laka.live.analytics;

/**
 * Created by ios on 16/5/21.
 */
public class LiveReport {
    public static final String SEPARATOR = AnalyticsReport.SEPARATOR;//"_"
    public static final String DEFAULT_VIEW_ID = AnalyticsReport.DEFAULT_VIEW_ID;//"NA"
    /**
     * 访问
     */
    public static final String SHOW_EVENT_ID = AnalyticsReport.SHOW_EVENT_ID;//"401"
    /**
     * 点击
     */
    public static final String CLICK_EVENT_ID =AnalyticsReport.CLICK_EVENT_ID;//"403"
    /**
     * 开始直播
     */
    public static final String START_LIVE_EVENT_ID ="505";
    /**
     * 结束直播
     */
    public static final String END_LIVE_EVENT_ID ="506";
    /**
     * 进入直播
     */
    public static final String START_SEE_EVENT_ID ="511";
    /**
     * 退出直播
     */
    public static final String END_SEE_EVENT_ID ="512";
    //父容器ID


    //我的直播开始
    public static final String MY_LIVE_11200 = "11200";
    /**
     * 我的直播-直播准备
     */
    public static final String MY_LIVE_EVENT_11200 = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(MY_LIVE_11200).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播准备-权限页面
     */
    public static final String MY_LIVE_11201 = "11201";
    public static final String MY_LIVE_EVENT_11201 = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(MY_LIVE_11201).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();

    /**
     * 我的直播-开始直播-标题栏输入框
     */
    public static final String MY_LIVE_11202 = "11202";
    public static final String MY_LIVE_EVENT_11202 = new StringBuilder(MY_LIVE_11200).append(SEPARATOR)
            .append(MY_LIVE_11202).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的直播-开始直播-添加封面
     */
    public static final String MY_LIVE_11203 = "11203";
    public static final String MY_LIVE_EVENT_11203 = new StringBuilder(MY_LIVE_11200).append(SEPARATOR)
            .append(MY_LIVE_11203).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 我的直播-开始直播-话题
     */
    public static final String MY_LIVE_11204 = "11204";
    public static final String MY_LIVE_EVENT_11204 = new StringBuilder(MY_LIVE_11200).append(SEPARATOR)
            .append(MY_LIVE_11204).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 我的直播-开始直播-话题页面
     */
    public static final String MY_LIVE_11205 = "11205";
    public static final String MY_LIVE_EVENT_11205 = new StringBuilder(MY_LIVE_11200).append(SEPARATOR)
            .append(MY_LIVE_11205).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();
    /**
     * 我的直播-开始直播-话题-输入框
     */
    public static final String MY_LIVE_11206 = "11206";
    public static final String MY_LIVE_EVENT_11206 = new StringBuilder(MY_LIVE_11205).append(SEPARATOR)
            .append(MY_LIVE_11206).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 我的直播-开始直播-话题-热门话题
     */
    public static final String MY_LIVE_11207 = "11207";
    public static final String MY_LIVE_EVENT_11207 = new StringBuilder(MY_LIVE_11205).append(SEPARATOR)
            .append(MY_LIVE_11207).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 我的直播-开始直播-开始直播按钮
     */
    public static final String MY_LIVE_11208 = "11208";
    public static final String MY_LIVE_EVENT_11208 = new StringBuilder(MY_LIVE_11200).append(SEPARATOR)
            .append(MY_LIVE_11208).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 我的直播-开始直播-关闭
     */
    public static final String MY_LIVE_11209 = "11209";
    public static final String MY_LIVE_EVENT_11209 = new StringBuilder(MY_LIVE_11200).append(SEPARATOR)
            .append(MY_LIVE_11209).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 我的直播-开始直播-美颜
     */
    public static final String MY_LIVE_11210 = "11210";
    public static final String MY_LIVE_EVENT_11210 = new StringBuilder(MY_LIVE_11200).append(SEPARATOR)
            .append(MY_LIVE_11210).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播中
     */
    public static final String MY_LIVE_11212 = "11212";
    public static final String MY_LIVE_EVENT_11212 = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(MY_LIVE_11212).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播中-分享
     */
    public static final String MY_LIVE_11213 = "11213";
    public static final String MY_LIVE_EVENT_11213 = new StringBuilder(MY_LIVE_11212).append(SEPARATOR)
            .append(MY_LIVE_11213).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播中-评论-发送
     */
    public static final String MY_LIVE_11214 = "11214";
    public static final String MY_LIVE_EVENT_11214 = new StringBuilder(MY_LIVE_11212).append(SEPARATOR)
            .append(MY_LIVE_11214).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();


    /**
     * 我的直播-直播中-摄像头切换
     */
    public static final String MY_LIVE_11215 = "11215";
    public static final String MY_LIVE_EVENT_11215 = new StringBuilder(MY_LIVE_11212).append(SEPARATOR)
            .append(MY_LIVE_11215).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播中-麦克风
     */
    public static final String MY_LIVE_11216 = "11216";
    public static final String MY_LIVE_EVENT_11216 = new StringBuilder(MY_LIVE_11212).append(SEPARATOR)
            .append(MY_LIVE_11216).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播中-美颜
     */
    public static final String MY_LIVE_11217 = "11217";
    public static final String MY_LIVE_EVENT_11217 = new StringBuilder(MY_LIVE_11212).append(SEPARATOR)
            .append(MY_LIVE_11217).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播中-闪光
     */
    public static final String MY_LIVE_11219 = "11219";
    public static final String MY_LIVE_EVENT_11219 = new StringBuilder(MY_LIVE_11212).append(SEPARATOR)
            .append(MY_LIVE_11219).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播中-用户头像
     */
    public static final String MY_LIVE_11220 = "11220";
    public static final String MY_LIVE_EVENT_11220 = new StringBuilder(MY_LIVE_11212).append(SEPARATOR)
            .append(MY_LIVE_11220).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 我的直播-直播中-全屏
     */
    public static final String MY_LIVE_11221 = "11221";
    public static final String MY_LIVE_EVENT_11221 = new StringBuilder(MY_LIVE_11212).append(SEPARATOR)
            .append(MY_LIVE_11221).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();
    /**
     * 我的直播-个人信息
     */
    public static final String MY_LIVE_11222 = "11222";
    public static final String MY_LIVE_EVENT_11222 = new StringBuilder(MY_LIVE_11212).append(SEPARATOR)
            .append(MY_LIVE_11222).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();
    /**
     * 我的直播-个人信息-关注
     */
    public static final String MY_LIVE_11223 = "11223";
    public static final String MY_LIVE_EVENT_11223 = new StringBuilder(MY_LIVE_11222).append(SEPARATOR)
            .append(MY_LIVE_11223).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 我的直播-个人信息-私聊
     */
    public static final String MY_LIVE_11224 = "11224";
    public static final String MY_LIVE_EVENT_11224 = new StringBuilder(MY_LIVE_11222).append(SEPARATOR)
            .append(MY_LIVE_11224).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的直播-个人信息-管理/管理员
     */
    public static final String MY_LIVE_11225 = "11225";
    public static final String MY_LIVE_EVENT_11225 = new StringBuilder(MY_LIVE_11222).append(SEPARATOR)
            .append(MY_LIVE_11225).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的直播-个人信息-个人主页
     */
    public static final String MY_LIVE_11226 = "11226";
    public static final String MY_LIVE_EVENT_11226 = new StringBuilder(MY_LIVE_11222).append(SEPARATOR)
            .append(MY_LIVE_11226).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播已结束
     */
    public static final String MY_LIVE_11227 = "11227";
    public static final String MY_LIVE_EVENT_11227 = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(MY_LIVE_11227).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播已结束-分享
     */
    public static final String MY_LIVE_11253 = "11253";
    public static final String MY_LIVE_EVENT_11253 = new StringBuilder(MY_LIVE_11227).append(SEPARATOR)
            .append(MY_LIVE_11253).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播信息
     */
    public static final String MY_LIVE_11228 = "11228";
    public static final String MY_LIVE_EVENT_11228_s = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(MY_LIVE_11228).append(SEPARATOR).append(START_LIVE_EVENT_ID)
            .toString();
    public static final String MY_LIVE_EVENT_11228_e = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(MY_LIVE_11228).append(SEPARATOR).append(END_LIVE_EVENT_ID)
            .toString();
    //我的直播结束

    //观看直播开始
    /**
     * 观看直播-直播中
     */
    public static final String MY_LIVE_11229 = "11229";
    public static final String MY_LIVE_EVENT_11229 = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(MY_LIVE_11229).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();
    /**
     * 观看直播-直播中-主播关注
     */
    public static final String MY_LIVE_11230 = "11230";
    public static final String MY_LIVE_EVENT_11230= new StringBuilder(MY_LIVE_11229).append(SEPARATOR)
            .append(MY_LIVE_11230).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 观看直播-直播中-用户头像
     */
    public static final String MY_LIVE_11231 = "11231";
    public static final String MY_LIVE_EVENT_11231= new StringBuilder(MY_LIVE_11229).append(SEPARATOR)
            .append(MY_LIVE_11231).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 观看直播-直播中-钻石贡献榜
     */
    public static final String MY_LIVE_11232 = "11232";
    public static final String MY_LIVE_EVENT_11232= new StringBuilder(MY_LIVE_11229).append(SEPARATOR)
            .append(MY_LIVE_11232).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 观看直播-直播中-分享
     */
    public static final String MY_LIVE_11233 = "11233";
    public static final String MY_LIVE_EVENT_11233= new StringBuilder(MY_LIVE_11229).append(SEPARATOR)
            .append(MY_LIVE_11233).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 观看直播-直播中-评论（发送）
     */
    public static final String MY_LIVE_11234 = "11234";
    public static final String MY_LIVE_EVENT_11234= new StringBuilder(MY_LIVE_11229).append(SEPARATOR)
            .append(MY_LIVE_11234).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 观看直播-直播中-弹幕（发送）
     */
    public static final String MY_LIVE_11235 = "11235";
    public static final String MY_LIVE_EVENT_11235= new StringBuilder(MY_LIVE_11229).append(SEPARATOR)
            .append(MY_LIVE_11235).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();


    /**
     * 观看直播-直播中-礼物
     */
    public static final String MY_LIVE_11236 = "11236";
    public static final String MY_LIVE_EVENT_11236= new StringBuilder(MY_LIVE_11229).append(SEPARATOR)
            .append(MY_LIVE_11236).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 观看直播-直播中-点赞
     */
    public static final String MY_LIVE_11237 = "11237";
    public static final String MY_LIVE_EVENT_11237= new StringBuilder(MY_LIVE_11229).append(SEPARATOR)
            .append(MY_LIVE_11237).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 观看直播-个人信息
     */
    public static final String MY_LIVE_11238 = "11238";
    public static final String MY_LIVE_EVENT_11238= new StringBuilder(MY_LIVE_11229).append(SEPARATOR)
            .append(MY_LIVE_11238).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();
    /**
     * 观看直播-个人信息-关注
     */
    public static final String MY_LIVE_11239 = "11239";
    public static final String MY_LIVE_EVENT_11239= new StringBuilder(MY_LIVE_11238).append(SEPARATOR)
            .append(MY_LIVE_11239).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 观看直播-个人信息-私聊
     */
    public static final String MY_LIVE_11240 = "11240";
    public static final String MY_LIVE_EVENT_11240= new StringBuilder(MY_LIVE_11238).append(SEPARATOR)
            .append(MY_LIVE_11240).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 观看直播-个人信息-个人主页
     */
    public static final String MY_LIVE_11241 = "11241";
    public static final String MY_LIVE_EVENT_11241= new StringBuilder(MY_LIVE_11238).append(SEPARATOR)
            .append(MY_LIVE_11241).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 观看直播-个人信息-禁言
     */
    public static final String MY_LIVE_11242 = "11242";
    public static final String MY_LIVE_EVENT_11242= new StringBuilder(MY_LIVE_11238).append(SEPARATOR)
            .append(MY_LIVE_11242).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 观看直播-送礼物
     */
    public static final String MY_LIVE_11243 = "11243";
    public static final String MY_LIVE_EVENT_11243= new StringBuilder(MY_LIVE_11229).append(SEPARATOR)
            .append(MY_LIVE_11243).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();
    /**
     * 观看直播-送礼物-礼品列表
     */
    public static final String MY_LIVE_11244 = "11244";
    public static final String MY_LIVE_EVENT_11244= new StringBuilder(MY_LIVE_11243).append(SEPARATOR)
            .append(MY_LIVE_11244).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 观看直播-送礼物-送礼物按钮
     */
    public static final String MY_LIVE_11245 = "11245";
    public static final String MY_LIVE_EVENT_11245= new StringBuilder(MY_LIVE_11243).append(SEPARATOR)
            .append(MY_LIVE_11245).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 观看直播-送礼物-连送按钮
     */
    public static final String MY_LIVE_11246 = "11246";
    public static final String MY_LIVE_EVENT_11246= new StringBuilder(MY_LIVE_11243).append(SEPARATOR)
            .append(MY_LIVE_11246).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 观看直播-送礼物-去充值
     */
    public static final String MY_LIVE_11247 = "11247";
    public static final String MY_LIVE_EVENT_11247= new StringBuilder(MY_LIVE_11243).append(SEPARATOR)
            .append(MY_LIVE_11247).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 观看直播-送礼物-余额不足
     */
    public static final String MY_LIVE_11248 = "11248";
    public static final String MY_LIVE_EVENT_11248= new StringBuilder(MY_LIVE_11243).append(SEPARATOR)
            .append(MY_LIVE_11248).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();
    /**
     * 观看直播-送礼物-余额不足-去充值
     */
    public static final String MY_LIVE_11249 = "11249";
    public static final String MY_LIVE_EVENT_11249= new StringBuilder(MY_LIVE_11248).append(SEPARATOR)
            .append(MY_LIVE_11249).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 观看直播-直播已结束
     */
    public static final String MY_LIVE_11250 = "11250";
    public static final String MY_LIVE_EVENT_11250= new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(MY_LIVE_11250).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();
    /**
     * 观看直播-直播已结束-按钮
     */
    public static final String MY_LIVE_11251 = "11251";
    public static final String MY_LIVE_EVENT_11251= new StringBuilder(MY_LIVE_11250).append(SEPARATOR)
            .append(MY_LIVE_11251).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 观看直播-直播信息
     */
    public static final String MY_LIVE_11252 = "11252";
    public static final String MY_LIVE_EVENT_11252_s= new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(MY_LIVE_11252).append(SEPARATOR).append(START_SEE_EVENT_ID)
            .toString();
    public static final String MY_LIVE_EVENT_11252_e= new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(MY_LIVE_11252).append(SEPARATOR).append(END_SEE_EVENT_ID)
            .toString();



    /**
     * 我的直播-直播中-伴唱
     */
    public static final String MY_LIVE_11254 = "11254";
    public static final String MY_LIVE_EVENT_11254= new StringBuilder(MY_LIVE_11200).append(SEPARATOR)
            .append(MY_LIVE_11254).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播中-伴唱-音效
     */
    public static final String MY_LIVE_11255 = "11255";
    public static final String MY_LIVE_EVENT_11255= new StringBuilder(MY_LIVE_11200).append(SEPARATOR)
            .append(MY_LIVE_11255).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();


    /**
     * 我的直播-直播中-伴唱曲库
     */
    public static final String MY_LIVE_11256 = "11256";
    public static final String MY_LIVE_EVENT_11256= new StringBuilder(MY_LIVE_11254).append(SEPARATOR)
            .append(MY_LIVE_11256).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播中-伴唱曲库-搜索
     */
    public static final String MY_LIVE_11257 = "11257";
    public static final String MY_LIVE_EVENT_11257= new StringBuilder(MY_LIVE_11256).append(SEPARATOR)
            .append(MY_LIVE_11257).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播中-伴唱曲库-歌曲
     */
    public static final String MY_LIVE_11258= "11258";
    public static final String MY_LIVE_EVENT_11258= new StringBuilder(MY_LIVE_11256).append(SEPARATOR)
            .append(MY_LIVE_11258).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播中-伴唱搜索
     */
    public static final String MY_LIVE_11259= "11259";
    public static final String MY_LIVE_EVENT_11259= new StringBuilder(MY_LIVE_11254).append(SEPARATOR)
            .append(MY_LIVE_11259).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播中-连麦
     */
    public static final String MY_LIVE_11260= "11260";
    public static final String MY_LIVE_EVENT_11260= new StringBuilder(MY_LIVE_11212).append(SEPARATOR)
            .append(MY_LIVE_11260).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播中-连麦列表
     */
    public static final String MY_LIVE_11261= "11261";
    public static final String MY_LIVE_EVENT_11261= new StringBuilder(MY_LIVE_11260).append(SEPARATOR)
            .append(MY_LIVE_11261).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();


    /**
     * 我的直播-直播中-确认连麦
     */
    public static final String MY_LIVE_11262= "11262";
    public static final String MY_LIVE_EVENT_11262= new StringBuilder(MY_LIVE_11261).append(SEPARATOR)
            .append(MY_LIVE_11262).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播中-拒绝连麦
     */
    public static final String MY_LIVE_11263= "11263";
    public static final String MY_LIVE_EVENT_11263= new StringBuilder(MY_LIVE_11261).append(SEPARATOR)
            .append(MY_LIVE_11263).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播中-清空列表
     */
    public static final String MY_LIVE_11264= "11264";
    public static final String MY_LIVE_EVENT_11264= new StringBuilder(MY_LIVE_11261).append(SEPARATOR)
            .append(MY_LIVE_11264).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播中-在线观众
     */
    public static final String MY_LIVE_11265= "11265";
    public static final String MY_LIVE_EVENT_11265= new StringBuilder(MY_LIVE_11260).append(SEPARATOR)
            .append(MY_LIVE_11265).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播中-邀请连麦
     */
    public static final String MY_LIVE_11266= "11266";
    public static final String MY_LIVE_EVENT_11266= new StringBuilder(MY_LIVE_11265).append(SEPARATOR)
            .append(MY_LIVE_11266).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播中-连麦中
     */
    public static final String MY_LIVE_11267= "11267";
    public static final String MY_LIVE_EVENT_11267= new StringBuilder(MY_LIVE_11212).append(SEPARATOR)
            .append(MY_LIVE_11267).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播中-结束连麦
     */
    public static final String MY_LIVE_11268= "11268";
    public static final String MY_LIVE_EVENT_11268= new StringBuilder(MY_LIVE_11265).append(SEPARATOR)
            .append(MY_LIVE_11268).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播中-连麦
     */
    public static final String MY_LIVE_11269= "11269";
    public static final String MY_LIVE_EVENT_11269= new StringBuilder(MY_LIVE_11229).append(SEPARATOR)
            .append(MY_LIVE_11269).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();


    /**
     * 我的直播-直播中-申请连麦
     */
    public static final String MY_LIVE_11270= "11270";
    public static final String MY_LIVE_EVENT_11270= new StringBuilder(MY_LIVE_11269).append(SEPARATOR)
            .append(MY_LIVE_11270).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播中-取消连麦
     */
    public static final String MY_LIVE_11271= "11271";
    public static final String MY_LIVE_EVENT_11271= new StringBuilder(MY_LIVE_11269).append(SEPARATOR)
            .append(MY_LIVE_11271).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播中-拒绝连麦
     */
    public static final String MY_LIVE_11272= "11272";
    public static final String MY_LIVE_EVENT_11272= new StringBuilder(MY_LIVE_11269).append(SEPARATOR)
            .append(MY_LIVE_11272).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播中-同意连麦
     */
    public static final String MY_LIVE_11273= "11273";
    public static final String MY_LIVE_EVENT_11273= new StringBuilder(MY_LIVE_11269).append(SEPARATOR)
            .append(MY_LIVE_11273).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播中-连麦中
     */
    public static final String MY_LIVE_11274= "11274";
    public static final String MY_LIVE_EVENT_11274= new StringBuilder(MY_LIVE_11269).append(SEPARATOR)
            .append(MY_LIVE_11274).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播中-关闭摄像头
     */
    public static final String MY_LIVE_11275= "11275";
    public static final String MY_LIVE_EVENT_11275= new StringBuilder(MY_LIVE_11274).append(SEPARATOR)
            .append(MY_LIVE_11275).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播中-开启摄像头
     */
    public static final String MY_LIVE_11276= "11276";
    public static final String MY_LIVE_EVENT_11276= new StringBuilder(MY_LIVE_11274).append(SEPARATOR)
            .append(MY_LIVE_11276).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();


    /**
     * 我的直播-直播中-结束连麦
     */
    public static final String MY_LIVE_11277= "11277";
    public static final String MY_LIVE_EVENT_11277= new StringBuilder(MY_LIVE_11274).append(SEPARATOR)
            .append(MY_LIVE_11277).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的直播-直播中-课堂黑板
     */
    public static final String MY_LIVE_11279= "11279";
    public static final String MY_LIVE_EVENT_11279= new StringBuilder(MY_LIVE_11229).append(SEPARATOR)
            .append(MY_LIVE_11279).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
//    /**
//     * 观看直播-直播中-关闭
//     */
//    public static final String MY_LIVE_11227 = "11227";
//    public static final String MY_LIVE_EVENT_11227= new StringBuilder(MY_LIVE_11223).append(SEPARATOR)
//            .append(MY_LIVE_11227).append(SEPARATOR).append(CLICK_EVENT_ID)
//            .toString();
//    /**
//     * 观看直播-直播中-分享-微信分享
//     */
//    public static final String MY_LIVE_11232 = "11232";
//    public static final String MY_LIVE_EVENT_11232= new StringBuilder(MY_LIVE_11223).append(SEPARATOR)
//            .append(MY_LIVE_11232).append(SEPARATOR).append(CLICK_EVENT_ID)
//            .toString();
//    /**
//     * 观看直播-直播中-分享-微博分享
//     */
//    public static final String MY_LIVE_11233 = "112333";
//    public static final String MY_LIVE_EVENT_11233= new StringBuilder(MY_LIVE_11223).append(SEPARATOR)
//            .append(MY_LIVE_11233).append(SEPARATOR).append(CLICK_EVENT_ID)
//            .toString();
//    /**
//     * 观看直播-直播中-分享-QQ分享
//     */
//    public static final String MY_LIVE_11234 = "11234";
//    public static final String MY_LIVE_EVENT_11234= new StringBuilder(MY_LIVE_11223).append(SEPARATOR)
//            .append(MY_LIVE_11234).append(SEPARATOR).append(CLICK_EVENT_ID)
//            .toString();
//    /**
//     * 观看直播-直播中-分享-QQ空间分享
//     */
//    public static final String MY_LIVE_11235 = "11235";
//    public static final String MY_LIVE_EVENT_11235= new StringBuilder(MY_LIVE_11223).append(SEPARATOR)
//            .append(MY_LIVE_11235).append(SEPARATOR).append(CLICK_EVENT_ID)
//            .toString();
//    /**
//     * 观看直播-直播中-分享-微信朋友圈分享
//     */
//    public static final String MY_LIVE_11236 = "11236";
//    public static final String MY_LIVE_EVENT_11236= new StringBuilder(MY_LIVE_11223).append(SEPARATOR)
//            .append(MY_LIVE_11236).append(SEPARATOR).append(CLICK_EVENT_ID)
//            .toString();
//    /**
//     * 观看直播-评论-发弹幕-余额不足
//     */
//    public static final String MY_LIVE_11239 = "11239";
//    public static final String MY_LIVE_EVENT_11239= new StringBuilder(MY_LIVE_11223).append(SEPARATOR)
//            .append(MY_LIVE_11239).append(SEPARATOR).append(SHOW_EVENT_ID)
//            .toString();
//    /**
//     * 观看直播-评论-发弹幕-去充值
//     */
//    public static final String MY_LIVE_11240 = "11240";
//    public static final String MY_LIVE_EVENT_11240= new StringBuilder(MY_LIVE_11239).append(SEPARATOR)
//            .append(MY_LIVE_11240).append(SEPARATOR).append(CLICK_EVENT_ID)
//            .toString();

    //观看直播结束

    //私信开始

    /**
     * 私信-信息列表页面
     */
    public static final String MY_LIVE_15000 = "15000";
    public static final String MY_LIVE_EVENT_15000 = new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(MY_LIVE_15000).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();
    /**
     * 私信-信息列表-忽略未读
     */
    public static final String MY_LIVE_15001 = "15001";
    public static final String MY_LIVE_EVENT_15001 = new StringBuilder(MY_LIVE_15000).append(SEPARATOR)
            .append(MY_LIVE_15001).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 私信-信息列表-未关注人信息
     */
    public static final String MY_LIVE_15002 = "15002";
    public static final String MY_LIVE_EVENT_15002 = new StringBuilder(MY_LIVE_15000).append(SEPARATOR)
            .append(MY_LIVE_15002).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 私信-信息列表-列表
     */
    public static final String MY_LIVE_15003 = "15003";
    public static final String MY_LIVE_EVENT_15003 = new StringBuilder(MY_LIVE_15000).append(SEPARATOR)
            .append(MY_LIVE_15003).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 私信-未关注人信息列表页面
     */
    public static final String MY_LIVE_15004 = "15004";
    public static final String MY_LIVE_EVENT_15004= new StringBuilder(MY_LIVE_15000).append(SEPARATOR)
            .append(MY_LIVE_15004).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();


    /**
     * 私信-未关注人信息列表-忽略未读
     */
    public static final String MY_LIVE_15005 = "15005";
    public static final String MY_LIVE_EVENT_15005= new StringBuilder(MY_LIVE_15004).append(SEPARATOR)
            .append(MY_LIVE_15005).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 私信-聊天
     */
    public static final String MY_LIVE_15006 = "15006";
    public static final String MY_LIVE_EVENT_15006= new StringBuilder(DEFAULT_VIEW_ID).append(SEPARATOR)
            .append(MY_LIVE_15006).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();
    /**
     * 私信-聊天-输入框
     */
    public static final String MY_LIVE_15007 = "15007";
    public static final String MY_LIVE_EVENT_15007= new StringBuilder(MY_LIVE_15006).append(SEPARATOR)
            .append(MY_LIVE_15007).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 私信-聊天-礼物按钮
     */
    public static final String MY_LIVE_15008 = "15008";
    public static final String MY_LIVE_EVENT_15008= new StringBuilder(MY_LIVE_15006).append(SEPARATOR)
            .append(MY_LIVE_15008).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 私信-聊天-礼物列表
     */
    public static final String MY_LIVE_15009 = "15009";
    public static final String MY_LIVE_EVENT_15009 = new StringBuilder(MY_LIVE_15006).append(SEPARATOR)
            .append(MY_LIVE_15009).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 私信-聊天-礼物列表-去充值
     */
    public static final String MY_LIVE_15010 = "15010";
    public static final String MY_LIVE_EVENT_15010= new StringBuilder(MY_LIVE_15006).append(SEPARATOR)
            .append(MY_LIVE_15010).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    /**
     * 私信-聊天-礼物列表-送礼物
     */
    public static final String MY_LIVE_15011 = "15011";
    public static final String MY_LIVE_EVENT_15011= new StringBuilder(MY_LIVE_15006).append(SEPARATOR)
            .append(MY_LIVE_15011).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
    //私信结束
    public static final String MY_LIVE_15400 = "15400";
    /**
     * 我的-已订阅-直播
     */
    public static final String MY_LIVE_15451 = "15451";
    public static final String MY_LIVE_EVENT_15451= new StringBuilder(MY_LIVE_15400).append(SEPARATOR)
            .append(MY_LIVE_15451).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();

    /**
     * 我的-课堂列表
     */
    public static final String MY_LIVE_15452 = "15452";
    public static final String MY_LIVE_EVENT_15452= new StringBuilder(MY_LIVE_15451).append(SEPARATOR)
            .append(MY_LIVE_15452).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的-已订阅-视频
     */
    public static final String MY_LIVE_15453 = "15453";
    public static final String MY_LIVE_EVENT_15453= new StringBuilder(MY_LIVE_15400).append(SEPARATOR)
            .append(MY_LIVE_15453).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();

    /**
     * 我的-已订阅-课堂列表
     */
    public static final String MY_LIVE_15454 = "15454";
    public static final String MY_LIVE_EVENT_15454= new StringBuilder(MY_LIVE_15453).append(SEPARATOR)
            .append(MY_LIVE_15454).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的-已发布-直播
     */
    public static final String MY_LIVE_15455 = "15455";
    public static final String MY_LIVE_EVENT_15455= new StringBuilder(MY_LIVE_15400).append(SEPARATOR)
            .append(MY_LIVE_15455).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();

    /**
     * 我的-已发布-直播-编辑
     */
    public static final String MY_LIVE_15456 = "15456";
    public static final String MY_LIVE_EVENT_15456= new StringBuilder(MY_LIVE_15455).append(SEPARATOR)
            .append(MY_LIVE_15456).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的-已发布-直播-取消课堂
     */
    public static final String MY_LIVE_15457 = "15457";
    public static final String MY_LIVE_EVENT_15457= new StringBuilder(MY_LIVE_15455).append(SEPARATOR)
            .append(MY_LIVE_15457).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的-已发布-直播-课堂列表
     */
    public static final String MY_LIVE_15458 = "15458";
    public static final String MY_LIVE_EVENT_15458= new StringBuilder(MY_LIVE_15455).append(SEPARATOR)
            .append(MY_LIVE_15458).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的-已发布-视频
     */
    public static final String MY_LIVE_15459 = "15459";
    public static final String MY_LIVE_EVENT_15459= new StringBuilder(MY_LIVE_15400).append(SEPARATOR)
            .append(MY_LIVE_15459).append(SEPARATOR).append(SHOW_EVENT_ID)
            .toString();

    /**
     * 我的-已发布-视频-课堂列表
     */
    public static final String MY_LIVE_15460 = "15460";
    public static final String MY_LIVE_EVENT_15460= new StringBuilder(MY_LIVE_15459).append(SEPARATOR)
            .append(MY_LIVE_15460).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的-最新直播课程-课程封面
     */
    public static final String MY_LIVE_15461 = "15461";
    public static final String MY_LIVE_EVENT_15461= new StringBuilder(MY_LIVE_15400).append(SEPARATOR)
            .append(MY_LIVE_15461).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的-最新直播课程-课程标题
     */
    public static final String MY_LIVE_15462 = "15462";
    public static final String MY_LIVE_EVENT_15462= new StringBuilder(MY_LIVE_15400).append(SEPARATOR)
            .append(MY_LIVE_15462).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();

    /**
     * 我的-最新直播课程-观看按钮
     */
    public static final String MY_LIVE_15463 = "15463";
    public static final String MY_LIVE_EVENT_15463= new StringBuilder(MY_LIVE_15400).append(SEPARATOR)
            .append(MY_LIVE_15463).append(SEPARATOR).append(CLICK_EVENT_ID)
            .toString();
}
