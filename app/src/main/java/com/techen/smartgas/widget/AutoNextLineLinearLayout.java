package com.techen.smartgas.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.techen.smartgas.R;

import java.util.Hashtable;

public class AutoNextLineLinearLayout extends ViewGroup {

    /**
     * 子view左右间距
     */
    private int mHorizontalSpacing;
    /**
     * 子view上下行距离
     */
    private int mVerticalSpacing;

    private Context context;
    private int mLineCount;
    private int mChildCount;
    private int mWidth;

    public AutoNextLineLinearLayout(Context context) {
        this(context, null);
        this.context = context;
    }

    public AutoNextLineLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoNextLineLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

}
