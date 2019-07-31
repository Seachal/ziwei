package com.laka.live.shopping.bean;

import java.util.List;

/**
 * Created by zhxu on 2015/12/23.
 * Email:357599859@qq.com
 */
public class ShoppingCommentBean {

    private String id;
    private String content;
    private String createtime;
    private String nickName;
    private String iconUrl;
    private String partnerScore;
    private String myScore;
    private String replyCount;
    private String readCount;
    private List<ShoppingCommentImageBean> images;
    private String postId;
    private String marriage;
    private String age;

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getCreateTime() {
        return createtime;
    }

    public String getNickName() {
        return nickName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getMyScore() {
        return myScore;
    }

    public String getPartnerScore() {
        return partnerScore;
    }

    public String getReplyCount() {
        return replyCount;
    }

    public String getReadCount() {
        return readCount;
    }

    public List<ShoppingCommentImageBean> getImages() {
        return images;
    }

    public String getPostId() {
        return postId;
    }

    public String getMarriage() {
        return marriage;
    }

    public String getAge() {
        return age;
    }
}
