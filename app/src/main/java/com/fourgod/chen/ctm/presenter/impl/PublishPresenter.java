package com.fourgod.chen.ctm.presenter.impl;

import android.os.Handler;
import android.os.Message;

import com.fourgod.chen.ctm.model.impl.PublishModel;
import com.fourgod.chen.ctm.view.impl.activity.PublishActivity;

public class PublishPresenter extends BasePresenter<PublishActivity, PublishModel> {

    public PublishPresenter(PublishActivity view) {
        super(view);
    }

    @Override
    protected PublishModel getModel(Handler handler) {
        return new PublishModel(handler);
    }

    @Override
    protected void eventReceive(Message msg) {

    }
}