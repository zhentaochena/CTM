package com.fourgod.chen.ctm.model.impl;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import com.fourgod.chen.ctm.entity.HotTagBean;
import com.fourgod.chen.ctm.entity.SearchBean;
import com.fourgod.chen.ctm.network.NetworkInterface;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchModel extends BaseModel {
    public SearchModel(Handler handler) {
        super(handler);
    }

    public void hotList(ArrayMap<String, String> param) {
        Callback callback = new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response)
                    throws IOException {
                if (response.body() != null) {
                    Gson gson = new Gson();
                    HotTagBean bean = gson.fromJson(response.body().string(), HotTagBean.class);
                    bean.setWhat(1);
                    postEvent(bean);
                }
            }
        };
        NetworkInterface.getHotList(param, callback);
    }

    public void doSearch(ArrayMap<String, String> param) {
        Callback callback = new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response)
                    throws IOException {
                if (response.body() != null) {
                    Gson gson = new Gson();
                    SearchBean bean = gson.fromJson(response.body().string(), SearchBean.class);
                    bean.setWhat(2);
                    postEvent(bean);
                }
            }
        };
        NetworkInterface.doSearch(param, callback);
    }
}
