package com.ben.colorpicker.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ben.colorpicker.R;

/**
 * Created by Hui on 2015/10/23.
 */
public class PickerView extends View {
    private int mColor;
    private int mCenterX;
    private int mCenterY;
    private Paint mPaintR;
    private Paint mPaintr;
    private Paint mCenterPaint;

    private int outAndInCircleWidth;
    private int centerCircleWidth;
    private int outCircleRadius;
    private int inCircleRadius;
    private int centerCircleRadius;

    private boolean isClear = true;
    public PickerView(Context context) {
        super(context);
        init(context);
    }

    public PickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context){
        Resources resources = context.getResources();
        outCircleRadius =  resources.getDimensionPixelOffset(R.dimen.out_circle_radius);
        outAndInCircleWidth =   resources.getDimensionPixelOffset(R.dimen.out_and_in_circle_width);
        inCircleRadius =  outCircleRadius - outAndInCircleWidth;
        centerCircleRadius =  resources.getDimensionPixelOffset(R.dimen.center_circle_radius);
        centerCircleWidth = resources.getDimensionPixelOffset(R.dimen.center_circle_width);

        mPaintR = new Paint();
        mPaintR.setColor(Color.BLACK);
        mPaintR.setAlpha(70);
        mPaintR.setAntiAlias(true);
        mPaintR.setStyle(Paint.Style.STROKE);
        mPaintR.setStrokeWidth(outAndInCircleWidth);

        mPaintr = new Paint();
        mPaintr.setAntiAlias(true);
        mPaintr.setStyle(Paint.Style.STROKE);
        mPaintr.setStrokeWidth(outAndInCircleWidth);

        mCenterPaint= new Paint();
        mCenterPaint.setColor(Color.BLACK);
        mCenterPaint.setAlpha(90);
        mCenterPaint.setAntiAlias(true);
        mCenterPaint.setStyle(Paint.Style.STROKE);
        mCenterPaint.setStrokeWidth(centerCircleWidth);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        invalidate();
        return super.onTouchEvent(event);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!isClear) {
            canvas.drawCircle(mCenterX, mCenterY, outCircleRadius, mPaintR);
            canvas.drawCircle(mCenterX, mCenterY, inCircleRadius, mPaintr);
            canvas.drawCircle(mCenterX, mCenterY, centerCircleRadius, mCenterPaint);
        }
    }
    public void drawCircle(float cx, float cy,int color){
        isClear = false;
        this.mCenterX = (int) cx;
        this.mCenterY = (int) cy;
        this.mColor = color;
        mPaintr.setColor(color);
        invalidate();

    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
    }

    public int getCenterX() {
        return mCenterX;
    }

    public void setCenterX(int centerX) {
        this.mCenterX = centerX;
    }

    public int getCenterY() {
        return mCenterY;
    }

    public void setCenterY(int centerY) {
        this.mCenterY = centerY;
    }

    public boolean isClear() {
        return isClear;
    }

    public void setIsClear(boolean isClear) {
        this.isClear = isClear;
    }
}
