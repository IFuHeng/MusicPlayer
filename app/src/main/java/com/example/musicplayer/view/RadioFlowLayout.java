package com.example.musicplayer.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

public class RadioFlowLayout extends RadioGroup {
    private MeasureInfo measureInfo;

    public RadioFlowLayout(Context context) {
        super(context);
    }

    public RadioFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int totalHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        measureInfo = getMeasureInfo(totalWidth - getPaddingLeft() - getPaddingRight(), modeWidth
                , totalHeight - getPaddingTop() - getPaddingBottom(), modeHeight);

        setMeasuredDimension(resolveSize(measureInfo.width + getPaddingLeft() + getPaddingRight(), widthMeasureSpec)
                , resolveSize(measureInfo.height + getPaddingTop() + getPaddingBottom(), heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        final int edgeLeft = l + getPaddingLeft();
        final int edgeRight = r - getPaddingRight();

        int offX = edgeLeft;
        int offY = t + getPaddingTop();
        int curMaxHeight = 0;


        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            if (child.getLayoutParams() instanceof MarginLayoutParams) {
                MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
                childWidth += params.leftMargin + params.rightMargin;
                childHeight += params.topMargin + params.bottomMargin;
            }
            curMaxHeight = Math.max(curMaxHeight, childHeight);
            if (offX + childWidth > edgeRight) {
                offX = edgeLeft;
                offY += curMaxHeight;
                curMaxHeight = childHeight;
            }
            child.layout(offX, offY, offX + childWidth, offY + childHeight);
        }

        super.onLayout(changed, l, t, r, b);
    }

    private MeasureInfo getMeasureInfo(int totalWidth, int modeWidth, int totalHeight, int modeHeight) {
        final int count = getChildCount();
        MeasureInfo result = new MeasureInfo();
        int offX = 0;
        int offY = 0;
        int curRowMaxHeight = 0;
        int maxWidth = 0;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(totalWidth, modeWidth == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : modeWidth);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(totalHeight, modeHeight == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : modeHeight);


            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            ViewGroup.LayoutParams childParams = child.getLayoutParams();
            if (childParams instanceof MarginLayoutParams) {
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) childParams;
                childWidth += marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
                childHeight += marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
            }

            if (offX + childWidth > totalWidth) {// 如果当前行宽度+当前child宽度 > 总宽度 , 则换行
                result.addRowBounds(new Rect(0, offY, offX, curRowMaxHeight));
                offX = 0;
                offY += curRowMaxHeight;
                curRowMaxHeight = childHeight;
            } else {
                curRowMaxHeight = Math.max(curRowMaxHeight, childHeight);
            }
        }

        result.width = maxWidth;
        result.height = offY + curRowMaxHeight;
        return result;
    }

    /**
     * 分析测量宽度高度的信息，包含每行的宽高信息
     */
    class MeasureInfo {
        int width;
        int height;
        List<Rect> arrRowsBound = new ArrayList<>();

        public void addRowBounds(Rect rect) {
            if (rect != null && rect.width() > 0 && rect.height() > 0) {
                arrRowsBound.add(rect);
            }
        }
    }
}