package com.laka.live.ui.widget.danmu;

import com.laka.live.util.Utils;

/**
 * Created by feiyang on 16/3/2.
 */
public class Danmu {
    public long   id;
    public int    userId;
    public String type;
    public String    avatarUrl;
    public String content;
    public String nickName;
    public int level;
    public boolean isGuest;
    public static final String TYPE_COMMENT="Comment",TYPE_COME ="Come";
    public Danmu(long id, int userId, String type, String avatarUrl, String nickName,String content) {
        this.id = id;
        this.userId = userId;
        this.type = type;

        if(Utils.isEmpty(nickName)){
            nickName="";
        }
//        if(Utils.isEmpty(avatarUrl)){
//            avatarUrl="http://ac-rhj8xovm.clouddn.com/3eb306837d38b35b8770";
//        }
        this.avatarUrl = avatarUrl;
        this.nickName = nickName;
        this.content = content;
    }

    public Danmu() {
    }
}
