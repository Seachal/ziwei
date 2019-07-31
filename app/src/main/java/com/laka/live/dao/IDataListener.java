package com.laka.live.dao;

public interface IDataListener {
     void onSuccess(Object data, int code, String msg);
     void onFailure(int code, String msg);
}
