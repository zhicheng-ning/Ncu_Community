package com.example.learncommunity.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/***
 *@Authot: niko
 *@Date: Created in 21:01 2020/6/18
 *@EMAIL: simaqinsheng@gmail.com
 *@VERSION: 1.0
 */
public class MyListView extends ListView {
    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 生成一个符合MeasureSpec的一个32位的包含测量模式和测量高度的int值。ScrollView中嵌入ListView解决冲突
     *参考 CSDN链接 https://blog.csdn.net/wangbf_java/article/details/60151965
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
