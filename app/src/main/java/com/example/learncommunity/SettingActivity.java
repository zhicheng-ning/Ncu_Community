package com.example.learncommunity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;

public class SettingActivity extends AppCompatActivity {

    private Button selectBtn3;

    private LinearLayout layout;
    private LinearLayout lrAuthorInfo;

    private ImageView ivReturn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
        selectBtn3 = (Button) findViewById(R.id.btn_selectimage3);
        layout = (LinearLayout) findViewById(R.id.shezhiLinearLayout1);
        lrAuthorInfo = findViewById(R.id.lr_authorInfo);
        ivReturn = findViewById(R.id.iv_return);
//        关于软件信息
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup layout = (ViewGroup) SettingActivity.this.getLayoutInflater().inflate(R.layout.dialog_about, null);

                Button button1 = (Button) layout.findViewById(R.id.dialog7Button1);

                AlertDialog.Builder dialog = new AlertDialog.Builder(SettingActivity.this, R.style.BottomDialogStyle);
                dialog.setView(layout);
                final AlertDialog dia = dialog.show();
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.dismiss();
                    }
                });
            }
        });

//        作者信息
        lrAuthorInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, AboutActivity.class));
            }
        });
//        退出登录按钮
        selectBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                加载自定义的对话框
                ViewGroup layout = (ViewGroup) SettingActivity.this.getLayoutInflater().inflate(R.layout.dailog_exit, null);
//                确认退出
                Button button = (Button) layout.findViewById(R.id.dialog4Button1);
//                取消退出
                Button button1 = (Button) layout.findViewById(R.id.dialog4Button2);
                AlertDialog.Builder dialog = new AlertDialog.Builder(SettingActivity.this, R.style.BottomDialogStyle);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(SettingActivity.this, "已退出登录", Toast.LENGTH_SHORT).show();//弹出成功的提示
//                        用户退出
                        BmobUser.logOut();
                        BmobUser currentUser = BmobUser.getCurrentUser(null);
                        System.exit(0);
                    }
                });
                dialog.setView(layout);
                final AlertDialog dia = dialog.show();
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.dismiss();
                    }
                });
            }
        });

        ivReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
