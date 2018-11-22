package com.wangkeke.mvpdemo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * 模拟model层，用于改变view具体状态，类似于数据提供层
 */
public class ModelViewTest extends AppCompatTextView {
    public ModelViewTest(Context context) {
        super(context);
    }

    public ModelViewTest(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ModelViewTest(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 测试自定义方法
     * @param size
     */
    public void setCustomFontSize(float size){
        setTextSize(size);
    }

    public void setCustomFontColor(int color){
        setTextColor(color);
    }
}
