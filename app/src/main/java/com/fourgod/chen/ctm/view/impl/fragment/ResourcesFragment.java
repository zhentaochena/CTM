package com.fourgod.chen.ctm.view.impl.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.flyco.tablayout.SlidingTabLayout;
import com.fourgod.chen.ctm.R;
import com.fourgod.chen.ctm.presenter.impl.BasePresenter;
import com.fourgod.chen.ctm.presenter.impl.ResourcesPresenter;
import com.fourgod.chen.ctm.view.i.IBaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tolean on 2018/12/18.
 */

public class ResourcesFragment extends BaseFragment<ResourcesPresenter> implements IBaseView{
    private View mRoot;
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    private List<String> mTitles;
    private List<Fragment> mFragments;
    @Override
    protected ResourcesPresenter getPresenter() {
        return new ResourcesPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        if (mRoot == null) {
            mRoot = inflater.inflate(R.layout.fragment_resources, container, false);
            initView();
        }
        return mRoot;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private void initView(){
        mSlidingTabLayout = mRoot.findViewById(R.id.resources_tab);
        mViewPager = mRoot.findViewById(R.id.resources_vp);
        mViewPager.setOffscreenPageLimit(2);
        String[] Titles={"电子产品","书籍","生活用品","专业人才","廉价劳动力","二手电脑"};
        mFragments=new ArrayList<>();
        mTitles=new ArrayList<>();
        for(int i=0;i<6;i++){
            mFragments.add(new ListFragment());
            mTitles.add(Titles[i]);
        }
        TabLayoutAdapter adapter =new TabLayoutAdapter(getChildFragmentManager(),mFragments,mTitles);
        mViewPager.setAdapter(adapter);
        mSlidingTabLayout.setViewPager(mViewPager);
        //mSlidingTabLayout.setViewPager(mViewPager,mTitles,this.getActivity(),mFragments);
    }
}

class TabLayoutAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private List<String> titles;

    public TabLayoutAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}