package com.fourgod.chen.ctm.network;

/**
 * Created by laobo on 2018/12/29.
 */

public class NetworkManager {
    private String token;
    private static NetworkManager instance = null;

    public static NetworkManager getInstance(){
        if(instance == null){
            instance = new NetworkManager();
        }
        return instance;
    }

    private NetworkManager(){
        setToken("a7a37c25-f6b8-4ed2-afcf-081ce79f0f1f");
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}