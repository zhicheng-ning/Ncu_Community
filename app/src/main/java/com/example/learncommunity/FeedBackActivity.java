package com.example.learncommunity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learncommunity.bean.Bmobbean;
import com.example.learncommunity.bean.MyUser;
import com.example.learncommunity.myview.MyListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

//反馈
public class FeedBackActivity extends AppCompatActivity {

    private TextInputLayout editlayout1;
    private EditText edittext;
    private MyUser user;
    private ImageView ivReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        ivReturn = findViewById(R.id.iv_return);
        user = new MyUser();
        user = BmobUser.getCurrentUser(MyUser.class);
        //		初始化Bmob
        Bmob.initialize(this, "82b7b41b0938b3470d9d0f584f20c220");
        edittext = (EditText) findViewById(R.id.bmobfankuiEditText1);
        editlayout1 = (TextInputLayout) findViewById(R.id.bmobfankuiTextInputLayout1);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.bmobfankuiFloatingActionButton1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View p1) {

                if (edittext.getText().toString().equals("")) {

                    editlayout1.setError("反馈内容为空");
                } else if (edittext.getText().toString().length() > 500) {

                    editlayout1.setError("反馈内容过长");
                } else {

                    Bmobbean liuyan = new Bmobbean();
                    liuyan.setContent(edittext.getText().toString());
                    liuyan.setAuthor(user);
                    liuyan.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(FeedBackActivity.this);
                            dialog.setTitle("提示").setMessage("反馈成功，处理结果将发送至您的个人邮箱，请注意查收").setCancelable(true).setPositiveButton("确定",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                            edittext.setText("");
                        }
                    });
                }
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
