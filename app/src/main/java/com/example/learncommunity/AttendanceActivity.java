package com.example.learncommunity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.learncommunity.bean.Attendance;
import com.example.learncommunity.bean.MyUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class AttendanceActivity extends AppCompatActivity {


    private Button button;

    List<Attendance> mPostlist = new ArrayList<>();

    private MyUser user;

    private TextView text1;

    private TextView text2;

    private TextView text;

    private String str1;

    private TextView text3;

    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        user = new MyUser();
        user = BmobUser.getCurrentUser(MyUser.class);

        button = (Button) findViewById(R.id.bt_qiandao);
//        当前时间
        text1 = (TextView) findViewById(R.id.fg1TextView1);

//        上次签到时间
        text2 = (TextView) findViewById(R.id.tv_qiandaotime);

        text = (TextView) findViewById(R.id.fg1TextView2);
        text3 = (TextView) findViewById(R.id.layoutqiandaoTextView1);
        list = (ListView) findViewById(R.id.layoutqiandaoListView1);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        Date curDate = new Date(System.currentTimeMillis());
        str1 = formatter.format(curDate);
//        当前时间
        text1.setText(str1);
        huoqu();
        query();
        getdata();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t1 = text1.getText().toString();
                String t2 = text2.getText().toString();
                if (t1.equals(t2)) {
                    Toast.makeText(AttendanceActivity.this, "今天你已经签到了", Toast.LENGTH_SHORT).show();
                } else {

                    Attendance attendance = new Attendance();
                    attendance.setName(user.getUsername());
                    attendance.setState(true);
                    attendance.setUser(user);
                    attendance.setTime(str1);
                    attendance.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Toast.makeText(AttendanceActivity.this, "签到成功 积分+10", Toast.LENGTH_SHORT).show();
                                text2.setText(str1);
                                button.setText("已签到");
                                text.setVisibility(View.VISIBLE);

                                user = new MyUser();
                                user = BmobUser.getCurrentUser(MyUser.class);
                                user.increment("point", 10);
//                                签到数+1
                                user.increment("days", 1);
//                                更新用户信息
                                user.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        query();
                                        getdata();
                                    }
                                });
                            }
                        }
                    });

                }
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
                // 把标识传给swipeRefreshLayout
                //sw.setEnabled(enable);

            }
        });

    }

    class ItemListAdapter extends BaseAdapter {
        private Intent intent, intent2;
        private Attendance fenxiang;

        //适配器
        @Override
        public int getCount() {
            if (mPostlist.size() > 0) {
                return mPostlist.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return mPostlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.item_attendance, null);
                viewHolder.name = (TextView) convertView.findViewById(R.id.layoutkaoqinTextView1);//签到人
                viewHolder.state = (TextView) convertView.findViewById(R.id.layoutkaoqinTextView2);//签到状态
                viewHolder.time = (TextView) convertView.findViewById(R.id.layoutkaoqinTextView3);//签到时间
                //viewHolder.lou = (TextView) convertView.findViewById(R.id.commentTextView3);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            fenxiang = mPostlist.get(position);

            Boolean st = fenxiang.getState();
            //如果为true则认证
            if (st == true) {
                viewHolder.state.setText("已签到");
            } else if (st == false) {
                viewHolder.state.setText("未签到");
            }
            viewHolder.time.setText(fenxiang.getTime());
            viewHolder.name.setText(fenxiang.getUser().getName());
            return convertView;
        }

        public class ViewHolder {
            public TextView time, name, state;

        }


    }

    public void getdata() {

        user = new MyUser();
        user = BmobUser.getCurrentUser(MyUser.class);
        BmobQuery<Attendance> query = new BmobQuery<Attendance>();
        BmobQuery<MyUser> inner = new BmobQuery<MyUser>();
        inner.addWhereEqualTo("objectId", user.getObjectId());
        query.order("-createdAt");//依照数据排序时间排序
        query.include("user");
//        匹配
        query.addWhereMatchesQuery("user", "_User", inner);
        query.findObjects(new FindListener<Attendance>() {
            @Override
            public void done(List<Attendance> attendances, BmobException e) {
                if (e == null) {
                    if (attendances.size() == 0) {
                        Toast.makeText(AttendanceActivity.this, "没有数据，请先签到", Toast.LENGTH_SHORT).show();
                    }else {
                        mPostlist.clear();
                        for (int i = 0; i < attendances.size(); i++) {
                            mPostlist.add(attendances.get(i));
                        }
                        list.setAdapter(new ItemListAdapter());
                    }
                }

            }
        });

    }

    public void huoqu() {


        BmobQuery<Attendance> query = new BmobQuery<Attendance>();
        //query.addWhereEqualTo("name",user.getUsername());
        query.order("-createdAt");//依照数据排序时间排序
        query.include("user");
        query.setLimit(1);
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findObjects(new FindListener<Attendance>() {
            @Override
            public void done(List<Attendance> attendances, BmobException e) {
                if (e == null) {
                    if (attendances.size() == 0) {
                        Toast.makeText(AttendanceActivity.this, "签到有积分噢", Toast.LENGTH_SHORT).show();//弹出成功的提示
                    }
                    else {
                        for (Attendance attendance : attendances) {
                            if (user.getObjectId().equals(attendance.getUser().getObjectId())) {
                                text2.setText(attendance.getTime());
                            }
                            if (text1.getText().toString().equals(text2.getText().toString()) == false) {
                                button.setText("签到");
                                text.setVisibility(View.VISIBLE);
                            } else {
                                button.setText("已签到");
                                text.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

            }
        });

    }

    //        获取用户更新后的数据，显示
    public void query() {
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.addWhereEqualTo("objectId", user.getObjectId());
        query.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> myUsers, BmobException e) {
                if (e == null) {
                    if (myUsers.size() == 0) {
                        Toast.makeText(AttendanceActivity.this, "没有用户信息", Toast.LENGTH_SHORT).show();//弹出成功的提示
                    }else {
                        for (MyUser myUser : myUsers) {
                            //text2.setText(p1.gettime());
                            text3.setText(myUser.getPoint().toString());
                        }
                    }
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        query();
        huoqu();
        getdata();
    }


    public void onReturnClick(View view) {
        finish();
    }
}
