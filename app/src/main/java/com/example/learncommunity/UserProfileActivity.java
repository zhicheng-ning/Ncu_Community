package com.example.learncommunity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learncommunity.bean.MyUser;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class UserProfileActivity extends AppCompatActivity {

    private MyUser user;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private LinearLayout layout;
    private LinearLayout layout1;
    private LinearLayout layout2;
    private LinearLayout layout3;
    private LinearLayout layout4;

    private TextView text4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        //		初始化Bmob
        Bmob.initialize(this, "82b7b41b0938b3470d9d0f584f20c220");

        user = new MyUser();
        user = BmobUser.getCurrentUser(MyUser.class);

        text1 = (TextView) findViewById(R.id.tv_yonghuming);
        text2 = (TextView) findViewById(R.id.layoutpersonTextView1);
        text3 = (TextView) findViewById(R.id.layoutpersonTextView2);
        text4 = (TextView) findViewById(R.id.layoutpersonTextView3);
        layout = (LinearLayout) findViewById(R.id.one);
        layout1 = (LinearLayout) findViewById(R.id.two);
        layout2 = (LinearLayout) findViewById(R.id.three);
        layout3 = (LinearLayout) findViewById(R.id.four);
        layout4 = (LinearLayout) findViewById(R.id.fine);



        getdata();//获取数据

//        账号修改
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                可复用的修改对话框
                ViewGroup layout = (ViewGroup) UserProfileActivity.this.getLayoutInflater().inflate(R.layout.dialog_update, null);
                TextView text = (TextView) layout.findViewById(R.id.dialog2TextView1);
                final EditText edit = (EditText) layout.findViewById(R.id.dialog2EditText1);
                Button button = (Button) layout.findViewById(R.id.dialog1Button1);
                Button button1 = (Button) layout.findViewById(R.id.dialog1Button2);

                AlertDialog.Builder dialog = new AlertDialog.Builder(UserProfileActivity.this, R.style.BottomDialogStyle);
                dialog.setView(layout);

                final AlertDialog dia = dialog.show();
                text.setText("账号修改");
//                确认修改
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String str1 = edit.getText().toString();
                        if (str1.equals("")) {
                            Toast.makeText(UserProfileActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
                        } else if (str1.length() > 12) {
                            Toast.makeText(UserProfileActivity.this, "账号不能过长", Toast.LENGTH_SHORT).show();
                        } else {
                            user = new MyUser();
//                            获得当前缓存用户
                            user = BmobUser.getCurrentUser(MyUser.class);
                            user.setUsername(str1);
//                            更新用户数据
                            user.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(UserProfileActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                        text1.setText(str1);
                                        getdata();
                                        dia.dismiss();
                                    }
                                }
                            });


                        }

                    }
                });

                button1.setOnClickListener(new View.OnClickListener() {

                    @Override

                    public void onClick(View v) {
                        dia.dismiss();

                    }
                });

            }
        });

//        密码修改
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewGroup layout = (ViewGroup) UserProfileActivity.this.getLayoutInflater().inflate(R.layout.dialog_update, null);
                TextView text = (TextView) layout.findViewById(R.id.dialog2TextView1);
                final EditText edit = (EditText) layout.findViewById(R.id.dialog2EditText1);
                Button button = (Button) layout.findViewById(R.id.dialog1Button1);
                Button button1 = (Button) layout.findViewById(R.id.dialog1Button2);

                AlertDialog.Builder dialog = new AlertDialog.Builder(UserProfileActivity.this, R.style.BottomDialogStyle);
                dialog.setView(layout);

                final AlertDialog dia = dialog.show();
                text.setText("密码修改");
//                确认修改
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String str1 = edit.getText().toString();
                        if (str1.equals("")) {
                            Toast.makeText(UserProfileActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                        } else if (str1.length() < 6) {
                            Toast.makeText(UserProfileActivity.this, "密码不能少于6位", Toast.LENGTH_SHORT).show();
                        } else {
                            user = new MyUser();
                            user = BmobUser.getCurrentUser(MyUser.class);
                            user.setPassword(str1);

                            user.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
//                                        修改成功
                                        Toast.makeText(UserProfileActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                        ViewGroup layout = (ViewGroup) UserProfileActivity.this.getLayoutInflater().inflate(R.layout.dailog_exit, null);
                                        Button button = (Button) layout.findViewById(R.id.dialog4Button1);
                                        Button button1 = (Button) layout.findViewById(R.id.dialog4Button2);
                                        TextView text = (TextView) layout.findViewById(R.id.dialog4TextView1);

                                        AlertDialog.Builder dialog = new AlertDialog.Builder(UserProfileActivity.this, R.style.BottomDialogStyle);
//                                        隐藏取消按钮
                                        button1.setVisibility(View.GONE);
                                        text.setText("您的密码已修改，请重新登录账号");
                                        button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                startActivity(new Intent(UserProfileActivity.this, Login.class));//转跳到找回密码界面
                                                finish();
                                                BmobUser.logOut();
                                            }
                                        });
                                        dialog.setView(layout);

                                        final AlertDialog dia = dialog.show();

                                        getdata();
                                        dia.dismiss();
                                    }
                                }
                            });

                        }

                    }
                });
//                取消修改
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.dismiss();

                    }
                });


            }
        });

//        性别修改
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewGroup layout = (ViewGroup) UserProfileActivity.this.getLayoutInflater().inflate(R.layout.dialog_sex_update, null);
                Button button = (Button) layout.findViewById(R.id.dialog3Button1);
                Button button1 = (Button) layout.findViewById(R.id.dialog3Button2);
                final RadioButton rbtnMale = (RadioButton) layout.findViewById(R.id.dialog3RadioButton1);
                RadioButton rbtnFemale = (RadioButton) layout.findViewById(R.id.dialog3RadioButton2);
                AlertDialog.Builder dialog = new AlertDialog.Builder(UserProfileActivity.this, R.style.BottomDialogStyle);
                dialog.setView(layout);

                final AlertDialog dia = dialog.show();
//                确认修改性别
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (rbtnMale.isChecked()) {
                            user = new MyUser();
                            user = BmobUser.getCurrentUser(MyUser.class);
                            user.setSex("男");
                            user.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    Toast.makeText(UserProfileActivity.this, "修改性别成功", Toast.LENGTH_SHORT).show();
                                    getdata();
                                    dia.dismiss();
                                }
                            });
                        } else {
                            user = new MyUser();
                            user = BmobUser.getCurrentUser(MyUser.class);
                            user.setSex("女");
                            user.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    Toast.makeText(UserProfileActivity.this, "修改性别成功", Toast.LENGTH_SHORT).show();
                                    getdata();
                                    dia.dismiss();
                                }
                            });
                        }

                    }
                });
//                取消
                button1.setOnClickListener(new View.OnClickListener() {

                    @Override

                    public void onClick(View v) {
                        dia.dismiss();

                    }
                });


            }
        });

//        修改昵称
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup layout = (ViewGroup) UserProfileActivity.this.getLayoutInflater().inflate(R.layout.dialog_update, null);
                TextView text = (TextView) layout.findViewById(R.id.dialog2TextView1);
                final EditText edit = (EditText) layout.findViewById(R.id.dialog2EditText1);
                Button button = (Button) layout.findViewById(R.id.dialog1Button1);
                Button button1 = (Button) layout.findViewById(R.id.dialog1Button2);

                AlertDialog.Builder dialog = new AlertDialog.Builder(UserProfileActivity.this, R.style.BottomDialogStyle);
                dialog.setView(layout);

                final AlertDialog dia = dialog.show();

                text.setText("昵称修改");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String str1 = edit.getText().toString();
                        user = new MyUser();
                        user = BmobUser.getCurrentUser( MyUser.class);
                        user.setName(str1);
                        user.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                Toast.makeText(UserProfileActivity.this, "昵称修改成功", Toast.LENGTH_SHORT).show();
                                text3.setText(str1);
                                getdata();
                                dia.dismiss();
                            }
                        });

                    }
                });

//取消修改昵称
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dia.dismiss();

                    }
                });
            }
        });

//        邮箱修改
        layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup layout = (ViewGroup) UserProfileActivity.this.getLayoutInflater().inflate(R.layout.dialog_update, null);
                TextView text = (TextView) layout.findViewById(R.id.dialog2TextView1);
                final EditText edit = (EditText) layout.findViewById(R.id.dialog2EditText1);
                Button button = (Button) layout.findViewById(R.id.dialog1Button1);
                Button button1 = (Button) layout.findViewById(R.id.dialog1Button2);

                AlertDialog.Builder dialog = new AlertDialog.Builder(UserProfileActivity.this, R.style.BottomDialogStyle);
                dialog.setView(layout);

                final AlertDialog dia = dialog.show();
                text.setText("邮箱修改");

//                确认修改
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String str1 = edit.getText().toString();
                        user = new MyUser();
                        user = BmobUser.getCurrentUser(MyUser.class);
                        user.setEmail(str1);
                        user.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                Toast.makeText(UserProfileActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                text4.setText(str1);
                                getdata();
                                dia.dismiss();
                            }
                        });
                    }
                });

                button1.setOnClickListener(new View.OnClickListener() {

                    @Override

                    public void onClick(View v) {
                        dia.dismiss();
                    }
                });

            }
        });

    }


    //    获得用户数据，更新页面
    public void getdata() {
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.addWhereEqualTo("objectId", user.getObjectId());
        query.order("-createdAt");//依照数据排序时间排序
        query.setLimit(1);
        //query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> myUsers, BmobException e) {
                if (e == null) {
                    if (myUsers.size() == 0) {
                        Toast.makeText(UserProfileActivity.this, "没有该用户数据", Toast.LENGTH_SHORT).show();//弹出成功的提示
                    }
                    for (MyUser myUser : myUsers) {
                        text1.setText(myUser.getUsername());
                        text2.setText(myUser.getName());
                        text3.setText(myUser.getSex());
                        text4.setText(myUser.getEmail());
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getdata();
    }

    public void onReturnClick(View view) {
        finish();
    }

}
