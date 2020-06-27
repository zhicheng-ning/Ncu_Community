package com.example.learncommunity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learncommunity.bean.Comment;
import com.example.learncommunity.bean.MyUser;
import com.example.learncommunity.bean.Topic;
import com.example.learncommunity.myview.MyListView;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import de.hdodenhof.circleimageview.CircleImageView;

//帖子详情页
public class PostDetailActivity extends AppCompatActivity {
    private ImageView ivReturn;
    private String topicTitle, topicTextContent, topicAuthor, topicId, topicAuthorSex, topicPostTime, topicPicture;

    private MyUser user;
    private MyListView list;
    private SwipeRefreshLayout swiperLayout;
    List<Comment> mCmtList = new ArrayList<>();
    private TextView tvTopicAuthorName, tvTopicTime, tvTopicTitle, tvTextContent, tvTitle, tvCmtCounts;
    private CircleImageView circleimageview;
    private ImageView ivPostCmt;
    private EditText editContent;
//    系统剪切板
    private ClipboardManager manager;
    private ImageView ivEmptyCmt;
    private ImageView ivTopicPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        //初始化
        Bmob.initialize(this,"82b7b41b0938b3470d9d0f584f20c220");
        user = new MyUser();
        user = BmobUser.getCurrentUser(MyUser.class);

        Bundle bundle = getIntent().getExtras();
        topicPostTime = bundle.getString("topicPostTime");
        topicTitle = bundle.getString("topicTitle");
        topicAuthor = bundle.getString("topicAuthor");
        topicTextContent = bundle.getString("topicTextContent");
        topicId = bundle.getString("topicId");
        topicAuthorSex = bundle.getString("topicAuthorSex");
        topicPicture = bundle.getString("topicPicture");

        editContent = findViewById(R.id.layoutfabuEditText1);//评论框
        tvTitle = findViewById(R.id.tv_title);//标题栏
        tvTopicAuthorName = findViewById(R.id.tv_topicAuthorName);//用户名
        tvTopicTime = findViewById(R.id.layoutfabuTextView2);//时间
        tvTopicTitle = findViewById(R.id.layoutfabuTextView3);//标题
        tvTextContent = findViewById(R.id.layoutfabuTextView4);//内容
        tvCmtCounts = findViewById(R.id.layoutfabuTextView6);//评论数

        circleimageview = findViewById(R.id.layoutfabuCircleImageView1);
        ivReturn = findViewById(R.id.ivReturn);//返回
        ivPostCmt = findViewById(R.id.img_main_small_logo);//发布评论
        ivEmptyCmt = findViewById(R.id.layoutfabuImageView1);//内容为空的图
        ivTopicPic = findViewById(R.id.layoutfabu1image);


        list = findViewById(R.id.comment_list);
        swiperLayout = findViewById(R.id.quanziSwipeRefreshLayout);
        swiperLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_green_light);
        swiperLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //text.setText("正在刷新");
                getdata();

                getcommentdata();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //text.setText("刷新完成");
                        swiperLayout.setRefreshing(false);
                        //Toast.makeText(Quanzipost.this,"刷新完成", Toast.LENGTH_SHORT).show();
                    }
                }, 3000);
            }
        });

        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (list != null && list.getChildCount() > 0) {
                    // 检查listView第一个item是否可见
                    boolean firstItemVisible = list.getFirstVisiblePosition() == 0;
                    // 检查第一个item的顶部是否可见
                    boolean topOfFirstItemVisible = list.getChildAt(0).getTop() == 0;
                    // 启用或者禁用SwipeRefreshLayout刷新标识
                    enable = firstItemVisible && topOfFirstItemVisible;
                } else if (list != null && list.getChildCount() == 0) {
                    // 没有数据的时候允许刷新
                    enable = true;
                }
                // 把标识传给swiperLayoutipeRefreshLayout
                swiperLayout.setEnabled(enable);
            }
        });

        ivPostCmt.setOnClickListener(new View.OnClickListener() {

            private String neirong;


            @Override
            public void onClick(View v) {
                neirong = editContent.getText().toString();
                if (neirong.equals("")) {
                    Toast.makeText(PostDetailActivity.this, "评论内容为空！", Toast.LENGTH_SHORT).show();

                } else if (neirong.length() > 30) {
                    Toast.makeText(PostDetailActivity.this, "评论字数不能超过30字", Toast.LENGTH_SHORT).show();
                } else {

                    Topic post = new Topic();
                    post.setObjectId(topicId);
                    Comment cm = new Comment();
                    cm.setContent(neirong);
                    cm.setZuozhe(user);
                    cm.setPost(post);
                    cm.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                //添加数据成功
                                Toast.makeText(PostDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                                editContent.setText("");
                                hintKeyBoard();
                                getcommentdata();
                            }
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
        getdata();//获取数据
    }

    public void getdata() {

        tvTitle.setText("NCU社区");
        tvTopicAuthorName.setText(topicAuthor);
        tvTopicTime.setText(topicPostTime);
        tvTopicTitle.setText(topicTitle);
        tvTextContent.setText(topicTextContent);
        if ("男".equals(topicAuthorSex)) {
            circleimageview.setImageResource(R.drawable.male);
        } else {
            circleimageview.setImageResource(R.drawable.female);
        }
        if (topicPicture == null) {
            ivTopicPic.setVisibility(View.GONE);
        } else {
            Picasso.with(PostDetailActivity.this)
                    .load(topicPicture)
                    .placeholder(R.drawable.pic1)
                    .error(R.drawable.pic1)
                    .into(ivTopicPic);
        }
    }

    public void hintKeyBoard() {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if (imm.isActive() && getCurrentFocus() != null) {
            //拿到view的token 不为空
            if (getCurrentFocus().getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public void fenxiangbutton(View v) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
//        分享的内容
        shareIntent.putExtra(Intent.EXTRA_TEXT, "标题："+topicTitle + "\n" + "作者：" + topicAuthor + "\n" + "内容："+topicTextContent + "\n\n/********\n*分享内容来源于NCU社区APP\n*" + formatter.format(curDate) + "\n**********/");
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    class ItemListAdapter extends BaseAdapter {
        private Intent intent;
        private Comment fenxiang;

        //适配器
        @Override
        public int getCount() {
            if (mCmtList.size() > 0) {
                return mCmtList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return mCmtList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.item_comment, null);
                viewHolder.time = (TextView) convertView.findViewById(R.id.commentlistTextView2);
                viewHolder.name = (TextView) convertView.findViewById(R.id.commentlistTextView1);
                viewHolder.neirong = (TextView) convertView.findViewById(R.id.commentlistTextView3);
                viewHolder.icon = (CircleImageView) convertView.findViewById(R.id.commentlistCircleImageView1);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            fenxiang = mCmtList.get(position);
            if ("男".equals(fenxiang.getZuozhe().getSex())) {
                viewHolder.icon.setImageResource(R.drawable.male);
            } else {
                viewHolder.icon.setImageResource(R.drawable.female);
            }


            viewHolder.time.setText(fenxiang.getCreatedAt());
            viewHolder.name.setText(fenxiang.getZuozhe().getName());
            viewHolder.neirong.setText(fenxiang.getContent());


            return convertView;
        }

        public class ViewHolder {
            public TextView time, name, neirong;

            public CircleImageView icon;

        }
    }

    public void getcommentdata() {
        Bmob.initialize(this, "82b7b41b0938b3470d9d0f584f20c220");
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        Topic post = new Topic();
        post.setObjectId(topicId);
        //query.order("-createdAt");//依照数据排序时间排序
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(7));//此表示缓存一天
        query.addWhereEqualTo("post", new BmobPointer(post));
        query.include("user,post.author,zuozhe");
        query.setLimit(500);
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
//        查询该帖子下所有评论
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> comments, BmobException e) {
                if (e == null) {
                    if (comments.size() == 0) {
                        ivEmptyCmt.setVisibility(View.VISIBLE);
                    } else {
                        ivEmptyCmt.setVisibility(View.GONE);
                        mCmtList.clear();
                        for (int i = 0; i < comments.size(); i++) {
                            mCmtList.add(comments.get(i));
                        }
                        list.setAdapter(new ItemListAdapter());
//                    加载完成
                        String commentCount = String.valueOf(comments.size());
                        tvCmtCounts.setText(commentCount);
                        swiperLayout.setRefreshing(false);
                    }
                }else {
                    ivEmptyCmt.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getdata();
        getcommentdata();
    }
}
