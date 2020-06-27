package com.example.learncommunity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.learncommunity.bean.MyUser;
import com.example.learncommunity.fragment.CommunityFragment;
import com.example.learncommunity.fragment.MyInfoFragment;
import com.example.learncommunity.fragment.StudyFragment;

import cn.bmob.v3.Bmob;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

//    三个fragment
    private CommunityFragment cmtyFg;
    private MyInfoFragment infoFg;
    private StudyFragment learnFg;


    private RelativeLayout rlayoutCommunity;
    private RelativeLayout rlayoutStudy;
    private RelativeLayout rlayoutMyInfo;

    private ImageView ivCommunity;
    private ImageView ivStudy;
    private ImageView ivMyInfo;

    private MyUser user;
    private String nowTime;


    //    定义FragmentManager对象管理器
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        initView();
//        默认选中社区模块
        setChoiceItem(1);
    }



    private void setChoiceItem(int index) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // 清空, 重置选项, 隐藏所有Fragment
        clearChioce();
        hideFragments(fragmentTransaction);
        switch (index) {
            case 0:
                ivStudy.setImageResource(R.drawable.tv1);
                if (learnFg == null) {
                    learnFg = new StudyFragment();
                    fragmentTransaction.add(R.id.content, learnFg);
                } else {
                    fragmentTransaction.show(learnFg);
                }
                break;
            case 1:
                ivCommunity.setImageResource(R.drawable.fx);
                if (cmtyFg == null) {
                    cmtyFg = new CommunityFragment();
                    fragmentTransaction.add(R.id.content, cmtyFg);
                } else {
                    fragmentTransaction.show(cmtyFg);
                }
                break;
            case 2:
                ivMyInfo.setImageResource(R.drawable.wd);
                if (infoFg == null) {
                    infoFg = new MyInfoFragment();
                    fragmentTransaction.add(R.id.content, infoFg);
                } else {
                    fragmentTransaction.show(infoFg);
                }
                break;
        }
        fragmentTransaction.commit(); // 提交
    }

    private void clearChioce() {
        ivStudy.setImageResource(R.drawable.tv);
        ivCommunity.setImageResource(R.drawable.fx1);
        ivMyInfo.setImageResource(R.drawable.wd1);
    }

    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (learnFg != null) {
            fragmentTransaction.hide(learnFg);
        }
        if (cmtyFg != null) {
            fragmentTransaction.hide(cmtyFg);
        }
        if (infoFg != null) {
            fragmentTransaction.hide(infoFg);
        }
    }

    private void initView() {
        Bmob.initialize(this, "82b7b41b0938b3470d9d0f584f20c220");
//        初始化底部导航栏的控件
        ivStudy = findViewById(R.id.iv_study);
        ivCommunity = findViewById(R.id.iv_community);
        ivMyInfo = findViewById(R.id.iv_myInfo);
        rlayoutStudy = findViewById(R.id.rlayout_study);
        rlayoutCommunity = findViewById(R.id.rlayout_community);
        rlayoutMyInfo = findViewById(R.id.rlayout_myInfo);
//      监听fragment切换
        rlayoutStudy.setOnClickListener(MainActivity.this);
        rlayoutCommunity.setOnClickListener(MainActivity.this);
        rlayoutMyInfo.setOnClickListener(MainActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlayout_study:
                setChoiceItem(0);
                break;
            case R.id.rlayout_community:
                setChoiceItem(1);
                break;
            case R.id.rlayout_myInfo:
                setChoiceItem(2);
                break;
            default:
                break;
        }
    }


}
