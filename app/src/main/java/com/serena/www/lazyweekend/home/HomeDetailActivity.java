package com.serena.www.lazyweekend.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.serena.www.lazyweekend.R;
import com.serena.www.lazyweekend.base.BaseActivity;
import com.serena.www.lazyweekend.contenst.Constant;
import com.serena.www.lazyweekend.home.adapter.DetailViewPagerAdapter;
import com.serena.www.lazyweekend.home.bean.RDetail;
import com.serena.www.lazyweekend.home.bean.RResult;
import com.serena.www.lazyweekend.mine.activity.LoginActivity;
import com.serena.www.lazyweekend.util.ActivityUtil;
import com.serena.www.lazyweekend.util.ImageUtil;
import com.serena.www.lazyweekend.util.Network;
import com.serena.www.lazyweekend.util.ToastUtils;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Serena
 * @time 2016/8/2  16:02
 * @desc ${TODD}
 */
public class HomeDetailActivity extends BaseActivity {


    @BindView(R.id.home_detail_viewPager)
    ViewPager mHomeDetailViewPager;
    @BindView(R.id.info_tv)
    TextView mInfoTv;
    @BindView(R.id.home_detail_iv)
    ImageView mHomeDetailIv;
    @BindView(R.id.category_tv)
    TextView mCategoryTv;
    @BindView(R.id.price_tv)
    TextView mPriceTv;
    @BindView(R.id.time_tv)
    TextView mTimeTv;
    @BindView(R.id.address_tv)
    TextView mAddressTv;
    @BindView(R.id.info_huodong_ll)
    LinearLayout mInfoHuodongLl;
    @BindView(R.id.tip_title_tv)
    TextView mTipTitleTv;
    @BindView(R.id.tip_Ll)
    LinearLayout mTipLl;
    @BindView(R.id.must_know_tv)
    TextView mMustKnowTv;
    @BindView(R.id.know_Ll)
    LinearLayout mKnowLl;
    @BindView(R.id.how_todo_tv)
    TextView mHowTodoTv;
    @BindView(R.id.todo_Ll)
    LinearLayout mTodoLl;
    @BindView(R.id.go_back_tv)
    TextView mGoBackTv;
    @BindView(R.id.back_Ll)
    LinearLayout mBackLl;
    @BindView(R.id.ticket_data_tv)
    TextView mTicketDataTv;
    @BindView(R.id.ticket_tv)
    TextView mTicketTv;
    @BindView(R.id.ticket_tv1)
    TextView mTicketTv1;
    @BindView(R.id.booking_btn)
    Button mBookingBtn;
    @BindView(R.id.action_left)
    TextView mActionLeft;
    @BindView(R.id.action_right)
    TextView mActionRight;
    @BindView(R.id.action_bar)
    RelativeLayout mActionBar;
    @BindView(R.id.booking_tv)
    TextView mBookingTv;


    private RDetail mDetail;
    InnerHandler handler;
    private DetailViewPagerAdapter mDetailViewPagerAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        initData();
        handler = new InnerHandler(this);
    }

    @Override
    protected void initView() {
        ShareSDK.initSDK(HomeDetailActivity.this);
        mActionBar.setBackgroundResource(R.color.touming);
        mActionLeft.setBackgroundResource(R.drawable.left);
        mActionRight.setBackgroundResource(R.drawable.ic_nav_share_white);
        mActionLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mActionRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });

        mBookingTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.startActivity(HomeDetailActivity.this, LoginActivity.class, false, 0);
            }
        });
    }

    public void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("测试");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("消息来自懒人周末");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("消息来自懒人周末");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }

    private void initList() {
        mDetailViewPagerAdapter = new DetailViewPagerAdapter();
        mDetailViewPagerAdapter.setDetail(mDetail);
        mHomeDetailViewPager.setAdapter(mDetailViewPagerAdapter);

        int middle = mDetail.getImages().size() / 2;
        mHomeDetailViewPager.setCurrentItem(middle - middle % mDetail.getImages().size());
        //增加滚动监听
        mHomeDetailViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mInfoTv.setText(mDetail.getTitle());
        ImageUtil.loadImg(mHomeDetailIv, mDetail.getCategory().icon_view);
        mCategoryTv.setText(mDetail.getCategory().cn_name);
        mPriceTv.setText("¥ " + mDetail.getPrice_info());
        mTimeTv.setText(mDetail.getTime().info);
        mAddressTv.setText(mDetail.getAddress());
        for (int i = 0; i < mDetail.getDescription().size(); i++) {
            if ("image".equals(mDetail.getDescription().get(i).getType())) {
                int displayWidth = getWindowManager().getDefaultDisplay().getWidth();
                Integer height = mDetail.getDescription().get(i).getSize().get(0);
                Integer width = mDetail.getDescription().get(i).getSize().get(1);
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, displayWidth * width / height));
                ImageUtil.loadImg(imageView, mDetail.getDescription().get(i).getContent());
                /*SimpleDraweeView draweeView = new SimpleDraweeView(this);
                GenericDraweeHierarchyBuilder builder =
                        new GenericDraweeHierarchyBuilder(getResources());
                GenericDraweeHierarchy hierarchy = builder
                        .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                        .build();
                draweeView.setHierarchy(hierarchy);
                draweeView.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
                draweeView.setAspectRatio(height * 1.0f / width);
                draweeView.setImageURI(mDetail.getDescription().get(i).getContent());
                draweeView.setPadding(0, 10, 0, 10);*/
                mInfoHuodongLl.addView(imageView);
            } else if ("text".equals(mDetail.getDescription().get(i).getType())) {
                TextView mInfoTv = new TextView(this);
                mInfoTv.setGravity(Gravity.CENTER);
                mInfoTv.setTextSize(16);
                mInfoTv.setPadding(0, 60, 0, 40);
                mInfoHuodongLl.addView(mInfoTv);
                mInfoTv.setText(mDetail.getDescription().get(i).getContent());
            }
        }

        for (int j = 0; j < mDetail.getLrzm_tips().size(); j++) {
            if ("text".equals(mDetail.getLrzm_tips().get(j).getType())) {
                TextView mTipTv = new TextView(this);
                mTipLl.addView(mTipTv);
                mTipTv.setText((j + 1) + ":" + mDetail.getLrzm_tips().get(j).getContent());
                mTipTv.setTextSize(16);
                mTipTv.setSingleLine(false);
            } else {
                ImageView mTipIv = new ImageView(this);
                mTipLl.addView(mTipIv);
                ImageUtil.loadImg(mTipIv, mDetail.getLrzm_tips().get(j).getContent());
            }
        }

        for (int k = 0; k < mDetail.getBooking_policy().size(); k++) {
            if ("text".equals(mDetail.getBooking_policy().get(k).getType())) {
                TextView mKnowTv = new TextView(this);
                mKnowLl.addView(mKnowTv);
                mKnowTv.setText((k + 1) + ":" + mDetail.getBooking_policy().get(k).getContent());
                mKnowTv.setTextSize(16);
                mKnowTv.setSingleLine(false);
            } else {
                ImageView mKnowIv = new ImageView(this);
                mKnowLl.addView(mKnowIv);
                ImageUtil.loadImg(mKnowIv, mDetail.getBooking_policy().get(k).getContent());
            }
        }

        for (int a = 0; a < mDetail.getTicket_usage().size(); a++) {
            if ("text".equals(mDetail.getTicket_usage().get(a).getType())) {
                TextView mTodoTv = new TextView(this);
                mTodoLl.addView(mTodoTv);
                mTodoTv.setText((a + 1) + ":" + mDetail.getTicket_usage().get(a).getContent());
                mTodoTv.setTextSize(16);
                mTodoTv.setSingleLine(false);
            } else {
                ImageView mTodoIv = new ImageView(this);
                mTodoLl.addView(mTodoIv);
                ImageUtil.loadImg(mTodoIv, mDetail.getTicket_usage().get(a).getContent());
            }
        }


        for (int b = 0; b < mDetail.getTicket_rule().size(); b++) {
            if ("text".equals(mDetail.getTicket_rule().get(b).getType())) {
                TextView mBackTv = new TextView(this);
                mBackLl.addView(mBackTv);
                mBackTv.setText((b + 1) + ":" + mDetail.getTicket_rule().get(b).getContent());
                mBackTv.setTextSize(16);
                mBackTv.setSingleLine(false);
            } else {
                ImageView mBackIv = new ImageView(this);
                mBackLl.addView(mBackIv);
                ImageUtil.loadImg(mBackIv, mDetail.getTicket_rule().get(b).getContent());
            }
        }

       /* mTicketTv.setVisibility(View.VISIBLE);
        mTicketTv.setText(mDetail.getRepresent_data().get(0).title);
        mTicketTv1.setVisibility(View.VISIBLE);
        mTicketTv1.setText("¥ " + mDetail.getRepresent_data().get(0).getPrice_info());*/
    }


    private void initData() {
        long leoId = getIntent().getLongExtra("leo_id", 0);
        Network.getInstance().mService.getRBookingList(leoId, Constant.SESSION_ID, Constant.V).enqueue(new Callback<RResult<RDetail>>() {
            @Override
            public void onResponse(Call<RResult<RDetail>> call, Response<RResult<RDetail>> response) {
                RResult<RDetail> rResult = response.body();
                if (rResult.getStatus() == 200) {
                    mDetail = rResult.getResult();
                    Log.d("HomeDetailActivity", "onResponse: " + mDetail);
                } else {
                    ToastUtils.show(HomeDetailActivity.this, "网络异常");
                }
                handler.sendEmptyMessage(0);
            }

            @Override
            public void onFailure(Call<RResult<RDetail>> call, Throwable t) {
                Log.d("HomeDetailActivity", "onFailure: ");
                t.printStackTrace();
            }
        });
    }


    static class InnerHandler extends Handler {

        WeakReference<HomeDetailActivity> mHomeDetailActivity;

        public InnerHandler(HomeDetailActivity hf) {
            this.mHomeDetailActivity = new WeakReference<>(hf);
        }

        @Override
        public void handleMessage(Message msg) {
            HomeDetailActivity homeDetailActivity = mHomeDetailActivity.get();
            if (homeDetailActivity == null) {
                return;
            }
            homeDetailActivity.initList();

        }
    }


}
