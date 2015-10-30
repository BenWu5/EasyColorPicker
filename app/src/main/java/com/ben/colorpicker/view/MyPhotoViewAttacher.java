package com.ben.colorpicker.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.ben.colorpicker.R;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Hui on 2015/10/23.
 */
public class MyPhotoViewAttacher extends PhotoViewAttacher {
    public static final String TAG = MyPhotoViewAttacher.class.getSimpleName();
    private boolean isLocked = false;

    private Bitmap mCurrentBitmap;

    protected OnColorSelectedListener mOnColorSelectedListener;

    private int spaceCircle ;

    public MyPhotoViewAttacher(ImageView imageView) {
        super(imageView);
        init(imageView.getContext());
    }

    public MyPhotoViewAttacher(ImageView imageView, boolean zoomable) {
        super(imageView, zoomable);
        init(imageView.getContext());

    }
    public void init(Context content){
        spaceCircle = content.getResources().getDimensionPixelOffset(R.dimen.space_circle);

    }

    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        if (isLocked() && mOnColorSelectedListener != null) {
            int centerX = (int) ev.getX();
            int centerY = (int) ev.getY() - spaceCircle;
            if (centerX < 0) {
                centerX = 0;
            }
            if (centerX > mCurrentBitmap.getWidth()) {
                centerX = mCurrentBitmap.getWidth();
            }
            if (centerY < 0) {
                centerY = 0;
            }
            if (centerY > mCurrentBitmap.getHeight()) {
                centerY = mCurrentBitmap.getHeight()-1;
            }
            int color = mCurrentBitmap.getPixel(centerX, centerY);
            mOnColorSelectedListener.onColorSelected(color, centerX, centerY);
            return true;
        } else {
            return super.onTouch(v, ev);
        }

    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
        if (isLocked) {
            getImageView().destroyDrawingCache();
            mCurrentBitmap = getImageView().getDrawingCache();
        }

    }

    public boolean isLocked() {
        return this.isLocked;
    }

    public void setOnColorSelectedListener(OnColorSelectedListener onColorSelectedListener) {
        mOnColorSelectedListener = onColorSelectedListener;
    }


    public interface OnColorSelectedListener {

        /**
         * Called when a new color has just been selected.
         *
         * @param newColor the new color that has just been selected.
         */
        void onColorSelected(int newColor, int cx, int cy);
    }
}
