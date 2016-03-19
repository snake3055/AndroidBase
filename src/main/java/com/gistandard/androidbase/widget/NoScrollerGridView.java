package com.gistandard.androidbase.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by cuishuliang on 2016/2/18.
 */
public class NoScrollerGridView extends GridView {
    public NoScrollerGridView(Context context) {
        super(context);
    }

    public NoScrollerGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollerGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if(ev.getAction() == MotionEvent.ACTION_MOVE){
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }
}
