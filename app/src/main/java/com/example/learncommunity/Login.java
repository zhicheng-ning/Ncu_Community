package com.example.learncommunity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.learncommunity.bean.MyUser;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/***
 *@Authot: niko
 *@Date: Created in 8:20 2020/6/17
 *@EMAIL: simaqinsheng@gmail.com
 *@VERSION: 1.0
 */
public class Login extends AppCompatActivity {
    private EditText editUserName;
    private EditText editPassword;
    private CheckBox cbRemember;
    private Button btnLogin;
    private Button btnRegist;
    private Button btnFind;

    private ImageView ivLeft;
    private ImageView ivRight;


    private String name;
    private String password;
    //    是否记住密码
    private int remamberFlag = 0;
    private String password1 = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //		初始化Bmob
        Bmob.initialize(this, "82b7b41b0938b3470d9d0f584f20c220");
//        权限
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionList = new ArrayList<>();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (!permissionList.isEmpty()) {
                ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), 1);
            } else {
                init();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        if (grantResult == PackageManager.PERMISSION_DENIED) {
                            String s = permissions[i];
                            Toast.makeText(this, s + "权限被拒绝了", Toast.LENGTH_SHORT).show();
                        } else {
                            init();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }


    private void init() {
//        使用缓存的CurrentUser对象，用户注册成功或是第一次登录成功，都会在本地磁盘中有一个缓存的用户对象
        //避免每次都要登录
//        MyUser user = BmobUser.getCurrentUser(MyUser.class);
//        if (user == null) {
//            setContentView(R.layout.activity_login);
//        } else if (user.getIdentity().equals("用户")) {
//            Intent dj = new Intent();
//            dj.setClass(this, MainActivity.class);
//            startActivity(dj);
////            finish();
//        } else {
//            Intent dj = new Intent();
//            dj.setClass(this, AdminActivity.class);
//            startActivity(dj);
////            finish();
//        }


        ivLeft = findViewById(R.id.iv_left);
        ivRight = findViewById(R.id.iv_right);
//密码
        editPassword = findViewById(R.id.edit_password);
//		账号
        editUserName = findViewById(R.id.edit_userName);

//		登录按钮
        btnLogin = findViewById(R.id.btn_login);
//		忘记密码
        btnFind = findViewById(R.id.btn_findPwd);
//		记住密码
        cbRemember = findViewById(R.id.btn_remember);
//        注册
        btnRegist = findViewById(R.id.btn_regist);
//		记住密码 本地存储
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);

        //如果不为空
        if (sharedPreferences != null) {
            String name1 = sharedPreferences.getString("name", "");
            password1 = sharedPreferences.getString("password", "");
            remamberFlag = sharedPreferences.getInt("remeber_flag", 0);
            editUserName.setText(name1);
        }

        //确定是记住密码
        if (remamberFlag == 1) {
            cbRemember.setChecked(true);
            editPassword.setText(password1);
        }
        //忘记密码
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, FindPwdActivity.class));
            }
        });

        editPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ivLeft.setImageResource(R.drawable.bey);
                    ivRight.setImageResource(R.drawable.bf2);
                }
            }
        });

        editUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ivLeft.setImageResource(R.drawable.bev);
                    ivRight.setImageResource(R.drawable.bf1);
                }
            }
        });

//        登录
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                name = editUserName.getText().toString();
                password = editPassword.getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                //创建Editor对象，写入值
                editor.putString("name", name);
                if (name.equals("")) {
                    Toast.makeText(Login.this, "账号不能为空", Toast.LENGTH_SHORT).show();
                } else if (password.equals("")) {
                    Toast.makeText(Login.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (cbRemember.isChecked()) {
                        remamberFlag = 1;
                        editor.putInt("remeber_flag", remamberFlag);
                        editor.putString("password", password);
                    } else {
                        remamberFlag = 0;
                        editor.putInt("remeber_flag", remamberFlag);
                    }
                    //提交
                    editor.commit();
                    BmobQuery<MyUser> query = new BmobQuery<MyUser>();
                    query.addWhereEqualTo("username", name);
                    query.findObjects(new FindListener<MyUser>() {
                        /**
                         * @param myUserList
                         * @param e
                         */
                        @Override
                        public void done(List<MyUser> myUserList, BmobException e) {
                            if (e == null) {
                                if (myUserList.size()==0){//用户未注册
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(Login.this);
                                    dialog.setTitle("提示").setMessage("用户未注册！").setCancelable(true).setPositiveButton("确定",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            editUserName.setText("");
                                        }
                                    }).show();
                                }else {
                                    for (MyUser myUser:myUserList){
                                        if (myUser.getIdentity().equals("用户")) {
                                            //用户登录
                                            userLogin();
                                        } else if (myUser.getIdentity().equals("管理员")){
//                                        管理员登录
                                            adminLogin();
                                            Toast.makeText(Login.this,"欢迎进入管理员界面",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            }

                        }
                    });

                }
            }
        });
// 注册
        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,RegistActivity.class);
                startActivity(intent);
            }
        });
    }

    private void adminLogin() {

    }

    private void userLogin() {
        MyUser user = new MyUser();
        user.setUsername(name);
        user.setPassword(password);
        user.login(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if (e == null) {
                    Toast.makeText(Login.this,"登陆成功！",Toast.LENGTH_SHORT).show();//弹出成功的提示
                    startActivity(new Intent(Login.this, MainActivity.class));
                    Login.this.finish();
                }else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Login.this);
                    dialog.setTitle("提示").setMessage("用户密码错误！").setCancelable(true).setPositiveButton("确定",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                                editPassword.setText("");
                        }
                    }).show();
                }
            }
        });

    }
}


