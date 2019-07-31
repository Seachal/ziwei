package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luwies on 16/3/26.
 */
public class SearchGoodsHotKeywordMsg extends Msg {

    @Expose
    @SerializedName(Common.DATA)
    private GoodsHotKey goodsHotKey;

    public List<String> getList() {
        return goodsHotKey.getList();
    }

    public class GoodsHotKey {

        @Expose
        @SerializedName("words")
        private ArrayList<String> words;

        public ArrayList<String> getList() {
            return words;
        }

        public void setList(ArrayList<String> list) {
            this.words = list;
        }
    }


}
