package com.laka.live.shopping.network;

/**
 * Created by zwl on 2015/11/26.
 * Email: 1501448275@qq.com
 */
public class HttpCode {

    public static final int ERROR_NETWORK = -1000;
    public static final int SUCCESS = 0;//请求成功
    public static final int ERROR_CANCEL = -1001;
    public static final int ERROR_CODE_FAIL_ANALYSIS = -1;//JSON解析失败
    public static final int ERROR_CODE_JSON_IS_EMPTY = -2;//JSON串为空
    public static final int ERROR_CODE_FAILD_OPT = 100;//操作失败
    public static final int ERROR_CODE_ILLEGAL = 101;//非法请求
    public static final int ERROR_CODE_INVALID_VERSON = 103;//无效版本
    public static final int ERROR_CODE_INVALID_TOKEN = 102;//无效token
    public static final int ERROR_CODE_LEVEL_NOT_ENOUGH = 105;//等级不足
    public static final int ERROR_CODE_FROZEN_USER = 106;//用户被冻结
    public static final int ERROR_CODE_BANNED = 107;//用户被禁言
    public static final int ERROR_CODE_DEVICE_FRROZEN = 109;//设备被禁用
    public static final int ERROR_CODE_EMPTY_LIST = -3;//空list

    public static final int ERROR_CODE_BLACKED = 302; //被拉黑
    public static final int ERROR_CODE_MAN_LIMIT = 306; //限制男生
    public static final int ERROR_CODE_NOT_CHAT_SELF = 303; //不能和自己私信
    public static final int ERROR_CODE_HAVE_BLACK = 304; //限制男生
    public static final int ERROR_CODE_CONTENT_LIMIT = 308; //限制内容
    public static final int ERROR_CODE_NOT_CHECK_POST = 309;//未审核帖子限制评论
    public static final int ERROR_CODE_NEED_FOLLOW_EACH = 310;//需要彼此关注才可私信对方
    public static final int ERROR_CODE_LIMIT = 313;//发帖频率限制

    public static final int ERROR_VICODE = 401;//验证码错误
    public static final int ERROR_HAS_REGISTER = 402;//用户已注册
    public static final int ERROR_NOT_REGISTER = 403;//用户未注册
    public static final int ERROR_PASSWORD = 404;//密码错误
    public static final int ERROR_PROHIBIT_LOGIN = 405;//用户禁止登录
    public static final int ERROR_TTP_TOKEN = 406;//第三方登录access_token失效

    public static final int ERROR_CODE_200 = 200;//商品不存在
    public static final int ERROR_CODE_602 = 602;//已经评论过了
    public static final int ERROR_CODE_201 = 201;//商品不足，请重新购买
    public static final int ERROR_CODE_202 = 202;//商品失效,请刷新购物车
    public static final int ERROR_CODE_606 = 606;//结算价格错误
    public static final int ERROR_CODE_104 = 104;//服务器繁忙，请稍后再试
    public static final int ERROR_CODE_608 = 608;//支付类型错误
    public static final int ERROR_CODE_CONTENT_TOO_LONG = 628;//内容过多
    public static final int ERROR_CODE_CONTENT_FREQUENCY = 638;//操作过频繁
    public static final int ERROR_CODE_643 = 638;//TA币不足

}
