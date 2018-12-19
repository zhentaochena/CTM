package com.fourgod.chen.ctm.view.impl.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourgod.chen.ctm.R;
import com.fourgod.chen.ctm.adapter.MyResAndReqRlvAdapter;
import com.fourgod.chen.ctm.entity.MyInfBean;
import com.fourgod.chen.ctm.model.impl.MyResAndReqModel;
import com.fourgod.chen.ctm.presenter.impl.MyResAndReqPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by laobo on 2018/12/19.
 */

public class MyResAndReqFragment extends BaseFragment<MyResAndReqPresenter> {
    private static final String TAG = "Lao";
    private View mRoot;
    private RecyclerView recyclerView;
    private MyResAndReqRlvAdapter rlvAdapter;
    private List<MyInfBean> myInfList = new ArrayList<>();

    @Override
    protected MyResAndReqPresenter getPresenter() {
        return new MyResAndReqPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        if (mRoot == null) {
            mRoot = inflater.inflate(R.layout.fragment_my_res_and_req, container, false);
            initView();
        }
        return mRoot;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置fragment类型，需求或资源
        Bundle bundle = getArguments();
        if(bundle != null){
            presenter.setType(bundle.getInt("type", MyResAndReqModel.TYPE_RESOURCE));
        }else{
            presenter.setType(MyResAndReqModel.TYPE_RESOURCE);
        }
    }

    private void initView() {
        recyclerView = mRoot.findViewById(R.id.rlv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        presenter.loadData();
    }

    public void initData(List<MyInfBean> data){
        Log.d(TAG, "initData: " + data.size());
        myInfList.clear();
        myInfList.addAll(data);
        if(rlvAdapter == null){
            rlvAdapter = new MyResAndReqRlvAdapter(R.layout.item_my_res_and_req,myInfList);
        }
        rlvAdapter.notifyDataSetChanged();
    }

    public void addData(List<MyInfBean> data){
        myInfList.addAll(data);
        rlvAdapter.notifyDataSetChanged();
    }
}