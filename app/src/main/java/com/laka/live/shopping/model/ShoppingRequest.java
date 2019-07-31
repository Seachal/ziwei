/*
 * Copyright (C) 2015-2015 SJY.JIANGSU Corporation. All rights reserved
 */

package com.laka.live.shopping.model;


import com.laka.live.shopping.bean.json2bean.JTBShoppingCategories;
import com.laka.live.shopping.bean.json2bean.JTBShoppingEvaluatorList;
import com.laka.live.shopping.bean.json2bean.JTBShoppingNewMain;
import com.laka.live.shopping.bean.json2bean.JTBShoppingTopList;
import com.laka.live.shopping.bean.json2bean.JTBShoppingTopListDetail;
import com.laka.live.shopping.bean.json2bean.newversion.JTBShoppingCartCount;
import com.laka.live.shopping.bean.json2bean.newversion.JTBShoppingCategory;
import com.laka.live.shopping.bean.json2bean.newversion.JTBShoppingGoods;
import com.laka.live.shopping.bean.json2bean.newversion.JTBShoppingGoodsDetail;
import com.laka.live.shopping.bean.json2bean.newversion.JTBShoppingGoodsIncome;
import com.laka.live.shopping.bean.json2bean.newversion.JTBShoppingGoodsIncomeDetail;
import com.laka.live.shopping.bean.json2bean.newversion.JTBShoppingGoodsIncomeDetailInfo;
import com.laka.live.shopping.bean.json2bean.newversion.JTBShoppingHome;
import com.laka.live.shopping.bean.json2bean.newversion.JTBShoppingRecommend;
import com.laka.live.shopping.bean.json2bean.newversion.JTBShoppingTopicGoods;
import com.laka.live.shopping.network.HttpManager;
import com.laka.live.shopping.network.HttpMethod;
import com.laka.live.shopping.network.HttpUrls;
import com.laka.live.shopping.network.IHttpCallBack;
import com.laka.live.shopping.network.IHttpManager;

/**
 * Created by zhxu on 2015/11/9.
 * Email:357599859@qq.com
 */
public class ShoppingRequest {

    public final static String MULTI = "multi";
    public final static String TIME = "time";
    public final static String SALE = "sale";
    public final static String PRICEPLUS = "pricePlus";
    public final static String PRICEMINUS = "priceMinus";

    public final static String ALL = "multi";
    public final static String SATISFIED = "time";
    public final static String GENERAL = "sale";
    public final static String UNSATISFIED = "unsatisfied";

    private final static int mPageSize = 10;

    private static ShoppingRequest mShoppingRequest;

    public ShoppingRequest() {
    }

    public boolean hasMore(int size) {
        return size == mPageSize;
    }

    public static ShoppingRequest getInstance() {
        if (mShoppingRequest == null) {
            mShoppingRequest = new ShoppingRequest();
        }
        return mShoppingRequest;
    }

    /**
     * 获取全部分类数据
     */
    public void getShoppingCategories(IHttpCallBack callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("a", "homeAllCatePage");
        httpManager.request(HttpUrls.SHOPPING_HOME_URL, HttpMethod.GET, JTBShoppingCategories.class, callBack);
    }

    /**
     * 获取商品测评达人
     */
    public void getEvaluatorList(String goodsId, IHttpCallBack callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("a", "evaluatorList");
        httpManager.addParams("goodsId", goodsId);
        httpManager.request(HttpUrls.URL_SHOP, HttpMethod.GET, JTBShoppingEvaluatorList.class, callBack);
    }

    /**
     * 获取首页
     */
    public void getShoppingMain(IHttpCallBack callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("a", "welfarePage");
        httpManager.request(HttpUrls.URL_SHOP, HttpMethod.POST, JTBShoppingNewMain.class, callBack);
    }

    /**
     * 获取商品评论列表
     */
    public void getShoppingReviewList(String goodsId, String endId, String type, IHttpCallBack callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("a", "goodsReviews");
        httpManager.addParams("goodsId", goodsId);
        httpManager.addParams("type", type);
        httpManager.addParams("endId", endId);
        httpManager.addParams("pageSize", "10");
        httpManager.request(HttpUrls.URL_SHOP, HttpMethod.POST, JTBShoppingRecommend.class, callBack);
    }

    /**
     * 获取商品评论列表
     */
    public void getShoppingReviewList(String goodsId, String endId, IHttpCallBack callBack) {
        getShoppingReviewList(goodsId, endId, ALL, callBack);
    }

    /**
     * 发布商品评价
     */
    public void postGoodsReview(String orderGoodsId, String content, String rating, boolean isAnonymous, IHttpCallBack callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("a", "postGoodsReview");
        httpManager.addParams("postGoodsReview", orderGoodsId);
        httpManager.addParams("content", content);
        httpManager.addParams("rating", rating);
        String anonymous = isAnonymous ? "1" : "0";
        httpManager.addParams("isAnony", anonymous);
        httpManager.request(HttpUrls.URL_SHOP, HttpMethod.POST, JTBShoppingRecommend.class, callBack);
    }

    /**
     * 获取榜单列表
     */
    public void getShoppingTopList(int cateId, int page, IHttpCallBack callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("a", "toplist");
        httpManager.addParams("cateId", cateId + "");
        httpManager.addParams("page", page + "");
        httpManager.addParams("pageSize", mPageSize + "");
        httpManager.request(HttpUrls.URL_SHOP, HttpMethod.GET, JTBShoppingTopList.class, callBack);
    }

    /**
     * 获取榜单详情
     */
    public void getShoppingTopListDetail(int topListId, IHttpCallBack callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("a", "toplistDetail");
        httpManager.addParams("toplistId", topListId + "");
        httpManager.request(HttpUrls.URL_SHOP, HttpMethod.GET, JTBShoppingTopListDetail.class, callBack);
    }

    /**********************2.2.0开始***********************/

    public final static int LIMIT = 20;

    /**
     * 获取新首页数据
     */
    public void getShoppingHome(IHttpCallBack callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.request(HttpUrls.SHOPPING_HOME_URL, HttpMethod.GET,
                JTBShoppingHome.class, callBack);
    }

    /**
     * 获取分类信息
     */
    public void getShoppingCategory(int cateId, IHttpCallBack callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("cateId", String.valueOf(cateId));
        httpManager.request(HttpUrls.SHOPPING_SUBCATEGORIES, HttpMethod.GET,
                JTBShoppingCategory.class, callBack);
    }

    /**
     * 获取商品列表
     */
    public void getShoppingCategoryGoods(int cateId, int sortType, int page, IHttpCallBack callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("cateId", String.valueOf(cateId));
        httpManager.addParams("sortType", String.valueOf(sortType));
        httpManager.addParams("page", String.valueOf(page));
        httpManager.addParams("pageSize", String.valueOf(LIMIT));
        httpManager.request(HttpUrls.SHOPPING_CATEGORY_GOODS, HttpMethod.GET, JTBShoppingGoods.class, callBack);
    }

    /**
     * 获取专题列表
     */
    public void getShoppingTopicGoods(int topicId, int page, IHttpCallBack callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("topicId", String.valueOf(topicId));
        httpManager.addParams("page", String.valueOf(page));
        httpManager.addParams("pageSize", String.valueOf(LIMIT));
        httpManager.request(HttpUrls.SHOPPING_TOPIC_GOODS, HttpMethod.GET, JTBShoppingTopicGoods.class, callBack);
    }

    /**
     * 获取商品详情
     */
    public void getShoppingGoodsDetail(int goodsId, IHttpCallBack callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("goodsId", String.valueOf(goodsId));
        httpManager.request(HttpUrls.SHOPPING_GOODS_DETAIL, HttpMethod.GET, JTBShoppingGoodsDetail.class, callBack);
    }

    /**
     * 获取商品推荐列表
     */
    public void getShoppingRecommend(int goodsId, IHttpCallBack callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("goodsId", String.valueOf(goodsId));
        httpManager.request(HttpUrls.SHOPPING_GOODS_RECOMMEND, HttpMethod.GET, JTBShoppingRecommend.class, callBack);
    }

    /**
     * 获取购物车数量
     */
    public void getShoppingCart(IHttpCallBack callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.request(HttpUrls.SHOPPING_CART_COUNT, HttpMethod.GET, JTBShoppingCartCount.class, callBack);
    }

    /**
     * 获取商品收益列表
     */
    public void getShoppingGoodsIncome(int page, IHttpCallBack callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("page", String.valueOf(page));
        httpManager.addParams("pagesize", String.valueOf(LIMIT));

        httpManager.request(HttpUrls.GOODS_INCOMES, HttpMethod.GET,
                JTBShoppingGoodsIncome.class, callBack);
    }

    /**
     * 获取商品收益明细
     */
    public void getShoppingGoodsIncomeDetail(int goodsId, int incomeType, int page, IHttpCallBack callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("goodsId", String.valueOf(goodsId));
        httpManager.addParams("page", String.valueOf(page));
        httpManager.addParams("pagesize", String.valueOf(LIMIT));
        httpManager.addParams("income_type", String.valueOf(incomeType));

        httpManager.request(HttpUrls.GOODS_INCOMES_DETAILS, HttpMethod.GET,
                JTBShoppingGoodsIncomeDetail.class, callBack);
    }

    /**
     * 获取商品收益明细详情
     */
    public void getShoppingGoodsIncomeDetailInfo(int id, int incomeType, IHttpCallBack callBack) {
        IHttpManager httpManager = HttpManager.getBusinessHttpManger();
        httpManager.addParams("id", String.valueOf(id));
        httpManager.addParams("income_type", String.valueOf(incomeType));

        httpManager.request(HttpUrls.GOODS_INCOMES_DETAILS_INFO, HttpMethod.GET,
                JTBShoppingGoodsIncomeDetailInfo.class, callBack);

    }
}
