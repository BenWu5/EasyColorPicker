package com.ben.colorpicker.view;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Hui on 2015/10/30.
 */
public class StaticLayoutView extends View {

    private Layout layout = null;

    private int width;
    private int height;

    public StaticLayoutView(Context context) {
        super(context);
    }

    public StaticLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StaticLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
        if (this.layout.getWidth() != 0 || this.layout.getHeight() != 0) {
            width = this.layout.getWidth();
            height = this.layout.getHeight();
            requestLayout();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        if (layout != null) {
            layout.draw(canvas, null, null, 0);
        }
        canvas.restore();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (layout != null) {
            setMeasuredDimension(layout.getWidth(), layout.getHeight());
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }
}
