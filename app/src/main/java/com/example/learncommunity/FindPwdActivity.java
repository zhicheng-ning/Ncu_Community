package com.example.learncommunity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 找回密码
 */
public class FindPwdActivity extends AppCompatActivity {

    private EditText editFindEmail;
    private Button btnPostEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pwd);
        initView();
    }

    private void initView() {
        editFindEmail = findViewById(R.id.edit_findEmail);
        btnPostEmail = findViewById(R.id.btn_postEmail);

/*
* 用户输入他们的电子邮件，请求重置自己的密码。
Bmob向他们的邮箱发送一封包含特殊的密码重置链接的电子邮件。
用户根据向导点击重置密码连接，打开一个特殊的Bmob页面，根据提示他们可以输入一个新的密码。
用户的密码已被重置为新输入的密码。
* */
        btnPostEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String   email= editFindEmail.getText().toString();
                AlertDialog.Builder dialog = new AlertDialog.Builder(FindPwdActivity.this);
                BmobUser.resetPasswordByEmail(email, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            dialog.setTitle("提示").setMessage("重置邮件已发送至您的邮箱，请注意查收").setCancelable(true).setPositiveButton("确定",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    editFindEmail.setText("");
                                }
                            }).show();
                        } else {
                            dialog.setTitle("提示").setMessage("邮箱未注册！").setCancelable(true).setPositiveButton("确定",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    editFindEmail.setText("");
                                }
                            }).show();
                        }
                    }
                });
            }
        });
    }

    //    返回
    public void onReturnClick(View view) {
        finish();
    }
}
