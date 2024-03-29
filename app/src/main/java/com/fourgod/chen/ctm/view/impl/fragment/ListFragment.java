package com.fourgod.chen.ctm.view.impl.fragment;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Visibility;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fourgod.chen.ctm.R;
import com.fourgod.chen.ctm.entity.CategoryListBean;
import com.fourgod.chen.ctm.entity.InfBean;
import com.fourgod.chen.ctm.entity.InfoListBean;
import com.fourgod.chen.ctm.presenter.impl.ListPresenter;
import com.fourgod.chen.ctm.utils.DimenUtils;
import com.fourgod.chen.ctm.view.i.IBaseView;
import com.fourgod.chen.ctm.view.impl.activity.InfoDetailActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tolean on 2018/12/19.
 */

public class ListFragment extends BaseFragment<ListPresenter> implements IBaseView{
    private Banner mBanner;
    private RecyclerView mRecyclerView;
    private View mRoot;
    private List<InfoListBean.DataBean.ListBean> mBeans;
    private List<String> mImgUrls;
    private BaseQuickAdapter mAdapter;
    private SmartRefreshLayout mRefreshLayout;
    private ArrayMap<String, String> mParams;
    private int CurrentPageNum;
    public CategoryListBean.DataBean.ListBean getBean() {
        return mCategory;
    }

    public ListFragment setBean(CategoryListBean.DataBean.ListBean bean) {
        mCategory = bean;
        return this;
    }

    private CategoryListBean.DataBean.ListBean mCategory;
    @Override
    protected ListPresenter getPresenter() {
        return new ListPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        if (mRoot == null) {
            mRoot = inflater.inflate(R.layout.fragment_list, container, false);
        }
        return mRoot;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CurrentPageNum=1;
        mParams = new ArrayMap<>();
        mParams.put("categoryId",String.valueOf(mCategory.getId()));
        mParams.put("pageNum",String.valueOf(CurrentPageNum++));
        presenter.getInfoList(mParams);
    }

    private void initView(){
        mRecyclerView = mRoot.findViewById(R.id.list_RecView);
        mRefreshLayout=mRoot.findViewById(R.id.list_refreshLayout);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                CurrentPageNum = 1;
                mRefreshLayout.setEnableLoadMore(true);
                mParams.put("pageNum",String.valueOf(CurrentPageNum++));
                presenter.getInfoList(mParams);
                refreshlayout.finishRefresh();
            }
        });
        //加载更多
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mParams.put("pageNum",String.valueOf(CurrentPageNum++));
                presenter.getInfoList(mParams);
                mAdapter.notifyDataSetChanged();
                refreshLayout.finishLoadMore();
            }
        });

        mAdapter = new BaseQuickAdapter<InfoListBean.DataBean.ListBean, BaseViewHolder>
                (R.layout.list_item,mBeans) {
            @Override
            protected void convert(BaseViewHolder helper, InfoListBean.DataBean.ListBean item) {
                //TODO 绑定数据
                helper.setText(R.id.item_title, item.getTitle());
                helper.setText(R.id.item_content, item.getContent());
                helper.setText(R.id.item_user_name, item.getUserNickName());
                helper.setText(R.id.item_push_time, item.getCreateTime());
                Glide.with(ListFragment.this.getActivity()).load(item.getUserHeadUrl())
                        .into((ImageView)helper.getView(R.id.item_user_head));
                String[] imgs;
                if (item.getPicture() != null
                        && (imgs = item.getPicture().split("\\|")).length>0) {
                        Glide.with(ListFragment.this.getActivity()).load(imgs[0])
                                .into((ImageView) helper.getView(R.id.item_pic));
                }else{
                    helper.getView(R.id.item_pic).setVisibility(View.GONE);
                }

            }
        };

        mBanner=new Banner(this.getContext());
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
        //mImgUrls=new ArrayList<>();
        //mImgUrls.add("http://lyonz.cn/static/images/albums/fulls/1527556881809.jpg");
        //mBanner.setImages(mImgUrls);
        List<Integer> images=new ArrayList<>();
        images.add(R.mipmap.person_bg);
        images.add(R.mipmap.person_bg);
        mBanner.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,400));
        mBanner.setImages(images);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (getActivity() != null) {
                    Intent intent = new Intent(getActivity(), InfoDetailActivity.class);
                    intent.putExtra("infoDetail",
                            ((InfoListBean.DataBean.ListBean) adapter.getData().get(position)));
                    getActivity().startActivity(intent);
                }
            }
        });
        mAdapter.setHeaderView(mBanner);
        //mRecyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(mRecyclerView);
        mAdapter.setEmptyView(R.layout.empty);
        mBanner.start();
    }

    public void showInfoList(InfoListBean bean) {
        if(mBeans == null){
            mBeans = new ArrayList<>();
        }
        mBeans.clear();
        mBeans.addAll(bean.getData().getList());
        initView();
        mAdapter.notifyDataSetChanged();
    }
    public void addInfmations(InfoListBean bean){
        mBeans.addAll(bean.getData().getList());
        if(bean.getData().getPageNum()==bean.getData().getPages()){
            mRefreshLayout.setEnableLoadMore(false);
        }
    }
}
