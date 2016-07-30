package com.serena.www.lazyweekend;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.serena.www.lazyweekend.base.BaseActivity;

import butterknife.BindView;

/**
 * @author Serena
 * @time 2016/7/29  17:40
 * @desc 设置个人资料
 */
public class MyInfoActivity extends BaseActivity {


    @BindView(R.id.action_back)
    TextView mActionBack;
    @BindView(R.id.action_next)
    TextView mActionNext;
    @BindView(R.id.center_title)
    TextView mCenterTitle;
    @BindView(R.id.action_bar)
    RelativeLayout mActionBar;
    @BindView(R.id.avatar)
    ImageView mAvatar;
    @BindView(R.id.nick_name)
    TextView mNickName;
    @BindView(R.id.user_sex)
    TextView mUserSex;
    @BindView(R.id.imageView_male)
    ImageView mImageViewMale;
    @BindView(R.id.text_man)
    TextView mTextMan;
    @BindView(R.id.male_layout)
    LinearLayout mMaleLayout;
    @BindView(R.id.imageView_female)
    ImageView mImageViewFemale;
    @BindView(R.id.text_female)
    TextView mTextFemale;
    @BindView(R.id.female_layout)
    LinearLayout mFemaleLayout;
    @BindView(R.id.user_state)
    TextView mUserState;
    @BindView(R.id.scrollview)
    ScrollView mScrollview;
    @BindView(R.id.container)
    LinearLayout mContainer;

    private String[] mRadioTextViews;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_info;
    }

    @Override
    protected void initView() {
        mRadioTextViews = getResources().getStringArray(R.array.radio_text);
        mActionBack.setBackgroundResource(R.drawable.left);
        mActionNext.setText("下一页");
        //mActionNext.setBackgroundResource(R.drawable.right);
       // mActionBack.setMenuIcon(R.drawable.left);
        //mActionNext.setMenuIcon(R.drawable.right);
        mCenterTitle.setText("设置个人资料");
        //mStatusList.setAdapter(new StatusAdapter(this));
    }
}
