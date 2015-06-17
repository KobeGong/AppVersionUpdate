package com.kobe.library.network;

/**
 * Created by kobe.gong on 2015/6/17.
 */
public interface HttpCallback {
    void success(String result);
    void failure();
}
