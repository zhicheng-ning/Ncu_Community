package com.example.learncommunity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learncommunity.bean.MyUser;
import com.example.learncommunity.bean.Topic;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

//发帖页面
public class PostTopicActivity extends AppCompatActivity {

    private TextView tvCancle;
    private TextView tvPostTopic;
    private EditText editPostTitle;
    private EditText editTextContent;
    private ImageView ivShowPic;
    private ImageView ivChosePic;

    private MyUser user;
    public static final int CHOOSE_PHOTO = 1;

    private String postTitle;
    private String postTextContetn;
    private String imagePath = null;//图片路径


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_topic);
        user = new MyUser();
//        当前用户
        user = BmobUser.getCurrentUser(MyUser.class);

        tvCancle = findViewById(R.id.tv_cancle);
        tvPostTopic = findViewById(R.id.tv_postTopic);
        editPostTitle = findViewById(R.id.edit_postTitle);
        editTextContent = findViewById(R.id.edit_postTextContent);
        ivShowPic = findViewById(R.id.iv_showChosePic);
        ivChosePic = findViewById(R.id.iv_chosePic);

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        选择图片
        ivChosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                系统内容提供器
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent, CHOOSE_PHOTO);
            }
        });

//        发布帖子
        tvPostTopic.setOnClickListener(new View.OnClickListener() {
            private String icon_path;
            @Override
            public void onClick(View v) {
                postTitle = editPostTitle.getText().toString();
                postTextContetn = editTextContent.getText().toString();
                if (postTitle.equals("") || postTextContetn.equals("")) {
                    Toast.makeText(PostTopicActivity.this, "帖子标题和内容不能为空", Toast.LENGTH_SHORT).show();
                } else if (postTitle.length() > 50) {
                    Toast.makeText(PostTopicActivity.this, "帖子标题过长", Toast.LENGTH_SHORT).show();
                } else if (postTextContetn.length() > 500) {
                    Toast.makeText(PostTopicActivity.this, "帖子长度过长", Toast.LENGTH_SHORT).show();
                } else {
                    icon_path = imagePath;
                    if (icon_path == null) {
//                        帖子没有发布图片
                        postTopicNoPic();
                    } else {
//                        帖子有图片
                        postTopicWithPic(icon_path);
                    }
                }


            }
        });




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleIMageOnKitKat(data);
                    } else {
                        handleIMageBeforKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void handleIMageOnKitKat(Intent data) {
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的URI，则使用document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果不是document类型的URI，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        }
        displayImage(imagePath);
    }

    //    api16以前的处理
    private void handleIMageBeforKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    //    显示图片
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            ivShowPic.setImageBitmap(bitmap);
        } else {
            Toast.makeText(PostTopicActivity.this, "未得到图片", Toast.LENGTH_SHORT).show();
        }
    }

    //    获得图片路径
    private String getImagePath(Uri uri, String selection) {
        String path = null;
//        系统内容提供器
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
//    发布帖子带图片

    private void postTopicWithPic(String icon_path) {

//        上传图片到数据库
        BmobFile bmobFile = new BmobFile(new File(icon_path));
        bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
//                    图片上传成功
                    Topic topic = new Topic();
                    topic.setAuthor(user);
                    topic.setTitle(postTitle);
                    topic.setContent(postTextContetn);
                    topic.setPicture(bmobFile);
                    topic.save(new SaveListener<String>() {
                        @Override
                        public void done(String objectId, BmobException e) {
                            Toast.makeText(PostTopicActivity.this, "帖子发布成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        });

    }

//    发布帖子不带图片
    private void postTopicNoPic() {
        Topic topic = new Topic();
        topic.setAuthor(user);
        topic.setTitle(postTitle);
        topic.setContent(postTextContetn);
        topic.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                Toast.makeText(PostTopicActivity.this, "帖子发布成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
