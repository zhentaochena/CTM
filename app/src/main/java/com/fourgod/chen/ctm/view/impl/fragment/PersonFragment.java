package com.fourgod.chen.ctm.view.impl.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.flyco.tablayout.SlidingTabLayout;
import com.fourgod.chen.ctm.R;
import com.fourgod.chen.ctm.entity.UserInfoBean;
import com.fourgod.chen.ctm.model.impl.MyResAndReqModel;
import com.fourgod.chen.ctm.presenter.impl.PersonPresenter;
import com.fourgod.chen.ctm.utils.DimenUtils;
import com.fourgod.chen.ctm.view.impl.activity.EditUserInfActivity;
import com.fourgod.chen.ctm.view.impl.activity.SetActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by laobo on 2018/12/18.
 */

public class PersonFragment extends BaseFragment<PersonPresenter> {
    private ImageView mSetting;
    private CircleImageView picImg;
    private View mRoot;
    private AppBarLayout appBarLayout;
    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;
    private TabLayoutAdapter adapter;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private RelativeLayout rlContainer;
    private TextView barName;
    private TextView nameTv;
    private View maskView;
    private LinearLayout mEdit;
    private UserInfoBean.DataBean myInfo;
    @Override
    protected PersonPresenter getPresenter() {
        return new PersonPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        if (mRoot == null) {
            mRoot = inflater.inflate(R.layout.fragment_person, container, false);
            initView();
            presenter.loadMyInfo();
        }
        return mRoot;
    }

    private void initView() {
        tabLayout = mRoot.findViewById(R.id.tabs);
        viewPager = mRoot.findViewById(R.id.vp);
        appBarLayout = mRoot.findViewById(R.id.appBar);
        rlContainer = mRoot.findViewById(R.id.rl_container);
        barName = mRoot.findViewById(R.id.tv_name_bar);
        maskView = mRoot.findViewById(R.id.view_mask);
        mEdit = mRoot.findViewById(R.id.tv_edit);
        mSetting = mRoot.findViewById(R.id.img_setting);
        nameTv = mRoot.findViewById(R.id.tv_name);
        picImg = mRoot.findViewById(R.id.img_head);
        BaseFragment resFragment = new MyResAndReqFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type",MyResAndReqModel.TYPE_RESOURCE);
        resFragment.setArguments(bundle);

        BaseFragment reqFragment = new MyResAndReqFragment();
        bundle = new Bundle();
        bundle.putString("type",MyResAndReqModel.TYPE_REQUIREMENT);
        reqFragment.setArguments(bundle);

        BaseFragment chatFragment = new MyResAndReqFragment();
        bundle = new Bundle();
        bundle.putString("type",MyResAndReqModel.TYPE_COLLECTION);
        chatFragment.setArguments(bundle);

        BaseFragment aboutMeFragment = new AboutMeFragment();

        fragments.add(resFragment);
        fragments.add(reqFragment);
        fragments.add(chatFragment);
        fragments.add(aboutMeFragment);
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.person_res));
        titles.add(getString(R.string.person_req));
        titles.add(getString(R.string.person_collect));
        titles.add(getString(R.string.person_about_me));
        adapter = new TabLayoutAdapter(getChildFragmentManager(),fragments,titles);
        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);
        final Context context = getActivity();
        Paint paint = new Paint();
        paint.setTextSize(DimenUtils.sp2px(context,12));
        float maxWidth = 0;
        for(String title : titles){
            float width = paint.measureText(title);
            maxWidth = maxWidth<width?width:maxWidth;
        }
        final float fMaxWidth = maxWidth;
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                float padding = (tabLayout.getWidth()-fMaxWidth*4) / 8;
                if(padding>0) {
                    tabLayout.setTabPadding(DimenUtils.px2dip(context, padding));
                }
            }
        });

        final float initHeight = DimenUtils.dip2px(getActivity(),260);
        final float toolbarHeight = DimenUtils.dip2px(getActivity(),95);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float scale = 1.0f - (-verticalOffset) / (initHeight - toolbarHeight);
                if(scale<0.1){
                    //scale = 0;
                    barName.setVisibility(View.VISIBLE);
                }else{
                    barName.setVisibility(View.GONE);
                }
                maskView.setAlpha(0.5f*(1-scale));
                rlContainer.setAlpha(scale);
            }
        });
        mSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonFragment.this.getContext(), SetActivity.class));
            }
        });
        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PersonFragment.this.getContext(), EditUserInfActivity.class)
                        .putExtra("nickname",myInfo.getNickname())
                        .putExtra("sex",myInfo.getSex())
                        .putExtra("birth",myInfo.getBirthday())
                        .putExtra("hometown",myInfo.getHometown())
                        .putExtra("signal",myInfo.getSignature())
                        .putExtra("headImageUrl",myInfo.getHeadImgUrl());
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==1){
            nameTv.setText(data.getExtras().getString("nickname"));
        }else if(requestCode==1&&resultCode==2){
            nameTv.setText(data.getExtras().getString("nickname"));
            Glide.with(this)
                    .asBitmap() // some .jpeg files are actually gif
                    .load(data.getExtras().getString("headImgUrl"))
                    .apply(new RequestOptions() {{
                        override(Target.SIZE_ORIGINAL);
                    }})
                    .into(picImg);
        }
    }

    public void showMyInfo(UserInfoBean userInfoBean){
        myInfo = userInfoBean.getData();
        barName.setText(myInfo.getNickname());
        nameTv.setText(myInfo.getNickname());
        Glide.with(getActivity()).load(myInfo.getHeadImgUrl())
                .into(picImg);
    }

    class TabLayoutAdapter extends FragmentPagerAdapter{
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
}
