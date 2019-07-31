package com.laka.live.shopping.order.model;

public class BaseModel {
    public String mName;
    public int mId;

    public BaseModel() {
        super();
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }
}
