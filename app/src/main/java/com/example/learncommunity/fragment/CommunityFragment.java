package com.example.learncommunity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.learncommunity.PostDetailActivity;
import com.example.learncommunity.PostTopicActivity;
import com.example.learncommunity.R;
import com.example.learncommunity.bean.Comment;
import com.example.learncommunity.bean.MyUser;
import com.example.learncommunity.bean.Topic;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/***
 *@Authot: niko
 *@Date: Created in 20:11 2020/6/17
 *@EMAIL: simaqinsheng@gmail.com
 *@VERSION: 1.0
 */
public class CommunityFragment extends Fragment {
    List<Topic> mPostList = new ArrayList<>();
    private ListView list;
    private SwipeRefreshLayout swiperLayout;
    private ImageView ivEmpty;//无内容的图片
    private MyUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bmob.initialize(getActivity(), "82b7b41b0938b3470d9d0f584f20c220");
        user = new MyUser();
//        获取当前登录用户
        user = BmobUser.getCurrentUser(MyUser.class);
        getTopicData();
//        发表帖子（使用悬浮按钮）
        FloatingActionButton fab = getActivity().findViewById(R.id.fabtn_post);
        list = getActivity().findViewById(R.id.lv_topic);
        ivEmpty = getActivity().findViewById(R.id.iv_emptyGirl);
        swiperLayout = getActivity().findViewById(R.id.swiperlayout_community);
        swiperLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_green_light);
        swiperLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                重新刷新
                getTopicData();
//                延时三秒刷新完成
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swiperLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

//        发帖
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PostTopicActivity.class);
                startActivity(intent);
            }
        });

//        item点击事件，将帖子数据带到评论页
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private Intent intent;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Topic topic = new Topic();
                topic.setObjectId(mPostList.get(position).getObjectId());
                BmobRelation relation = new BmobRelation();
                relation.add(user);
//             简化处理   浏览量+1
                topic.setView(relation);
                topic.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            //浏览量+1
                        }
                    }
                });

                Bundle bundle = new Bundle();
                bundle.putString("topicAuthor", mPostList.get(position).getAuthor().getName());
                bundle.putString("topicTitle", mPostList.get(position).getTitle());
                bundle.putString("topicTextContent", mPostList.get(position).getContent());
                bundle.putString("topicAuthorSex", mPostList.get(position).getAuthor().getSex());
                bundle.putString("topicPostTime", mPostList.get(position).getCreatedAt());
                bundle.putString("topicId", mPostList.get(position).getObjectId());
                if (mPostList.get(position).getPicture() != null) {
//                    帖子有图片
                    bundle.putString("topicPicture",mPostList.get(position).getPicture().getUrl());
                }
//                跳转至帖子详情页
                intent = new Intent(getActivity(), PostDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
//        list下拉事件，确保swiper在没有数据/到顶部的item时候再刷新，避免在向上滑动的时候就刷新
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
                // 把标识传给swipeRefreshLayout
                swiperLayout.setEnabled(enable);
            }
        });
    }

    //    获取帖子数据
    private void getTopicData() {
        Bmob.initialize(getActivity(), "82b7b41b0938b3470d9d0f584f20c220");
        BmobQuery<Topic> query = new BmobQuery<>();

        query.order("-createdAt");//依照帖子降序时间排序
//        同时获取帖子的作者信息
        query.include("author");
        query.setLimit(500);
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findObjects(new FindListener<Topic>() {
            @Override
            public void done(List<Topic> topics, BmobException e) {
                if (e == null) {
                    if (topics.size() == 0) {
                        //没有帖子信息
                        ivEmpty.setVisibility(View.VISIBLE);
                    }else {
                        ivEmpty.setVisibility(View.GONE);
                        mPostList.clear();
                        for (int i = 0; i < topics.size(); i++) {
                            mPostList.add(topics.get(i));
                        }
//                列表数据加载完成
                        list.setAdapter(new ItemListAdapter());
                    }
                }else {
                    ivEmpty.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    //    帖子适配器
    class ItemListAdapter extends BaseAdapter {
        //private ViewHolder viewHolder;
        private Topic topic;

        //适配器
        @Override
        public int getCount() {

            if (mPostList.size() > 0) {
                return mPostList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return mPostList.get(position);
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
                convertView = getActivity().getLayoutInflater().inflate(R.layout.item_topic, null);
                viewHolder.img = (ImageView) convertView.findViewById(R.id.imageView);
                viewHolder.viewcount = (TextView) convertView.findViewById(R.id.listfabuTextView6);
                viewHolder.commentcount = (TextView) convertView.findViewById(R.id.listfabuTextView5);


                viewHolder.name = (TextView) convertView.findViewById(R.id.listfabuTextView1);
                viewHolder.topicTitle = (TextView) convertView.findViewById(R.id.listfabuTextView3);
                viewHolder.topicText = (TextView) convertView.findViewById(R.id.listfabuTextView4);
                viewHolder.time = (TextView) convertView.findViewById(R.id.listfabuTextView2);
                viewHolder.icon = (CircleImageView) convertView.findViewById(R.id.listfabuCircleImageView1);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            topic = mPostList.get(position);
            if (topic.getPicture() == null) {
                //帖子没有图片
                viewHolder.img.setVisibility(View.GONE);
            } else {
                //帖子有图片
                viewHolder.img.setVisibility(View.VISIBLE);
                //Picasso使用了流式接口的调用方式 毕加索图片加载框架
                Log.d("图片", "图片："+topic.getPicture().getUrl());
                Log.d("性别", "性别："+topic.getAuthor().getSex());
                Log.d("用户昵称", "昵称："+topic.getAuthor().getName());
                Log.d("用户昵称", "author id："+topic.getAuthor().getObjectId());
                /*android:usesCleartextTraffic="true"  一定注意设置*/
                Picasso.with(getActivity())
                        .load(topic.getPicture().getUrl())
                        .placeholder(R.drawable.pic1)//加载过程中的图片显示
                        .error(R.drawable.pic1)//加载失败中的图片显示
//                        .resize(200,200)
//                        .centerCrop()
                        .into(viewHolder.img);
//                Glide.with(getActivity()).load(topic.getPicture().getUrl()).into(viewHolder.img);

            }

            MyUser m = new MyUser();
            m = topic.getAuthor();
            if ("男".equals(m.getSex())) {
                viewHolder.icon.setImageResource(R.drawable.male);
            } else {
                viewHolder.icon.setImageResource(R.drawable.female);
            }
            viewHolder.time.setText(topic.getCreatedAt());
            //
            viewHolder.name.setText(topic.getAuthor().getName());
            viewHolder.topicTitle.setText(topic.getTitle());
            viewHolder.topicText.setText(topic.getContent());

// 获取该帖子的所有浏览量
            BmobQuery<MyUser> myUserBmobQuery = new BmobQuery<>();
            Topic post = new Topic();
            post.setObjectId(topic.getObjectId());
            myUserBmobQuery.addWhereRelatedTo("view", new BmobPointer(post));
            myUserBmobQuery.findObjects(new FindListener<MyUser>() {
                @Override
                public void done(List<MyUser> myUserList, BmobException e) {
                    if (e == null) {
                        String viewCounts = String.valueOf(myUserList.size());
                        viewHolder.viewcount.setText(viewCounts);
                    }
                }
            });


//            获取该话题下所有的评论
            BmobQuery<Comment> query = new BmobQuery<Comment>();
            Topic post1 = new Topic();
            post1.setObjectId(topic.getObjectId());
            query.addWhereEqualTo("post", new BmobPointer(post1));
//            没user
            query.include("user,post.author,zuozhe");
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
            query.findObjects(new FindListener<Comment>() {
                @Override
                public void done(List<Comment> commentList, BmobException e) {
                    if (e == null) {
                        String commentCounts = String.valueOf(commentList.size());
                        viewHolder.commentcount.setText(commentCounts);
                        swiperLayout.setRefreshing(false);
                    }
                }
            });

            return convertView;
        }

        public class ViewHolder {

            public ImageView img;//帖子图片
            public TextView viewcount;//    浏览量
            public TextView commentcount;//评论量
            public TextView name, topicTitle, topicText, time;//用户名、标题、帖子内容文字、发布时间
            public ImageView link;
            private CircleImageView icon;//circleview圆形头像
        }
    }

}


