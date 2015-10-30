package com.ben.colorpicker.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.ben.colorpicker.R;
import com.ben.colorpicker.utils.CopyUtils;
import com.ben.colorpicker.view.MyPhotoViewAttacher;
import com.ben.colorpicker.view.PickerView;
import com.ben.colorpicker.view.StaticLayoutView;

import java.io.FileNotFoundException;


public class PickerActivity extends SelectPhotoActivity implements View.OnClickListener, MyPhotoViewAttacher.OnColorSelectedListener {
    ImageView lockButton;
    ImageView mImageView;
    MyPhotoViewAttacher mAttacher;
    CardView mColorView;

    //这里使用自定义控件来显示文字而不是用Textview，主要是因为使用Textview会有些许卡顿,所以用StaticLayoutView
    // StaticLayoutView的用法可以查询
    StaticLayoutView mRGBView;
    StaticLayoutView mHEXView;
    int mHEXViewWidth;              //mHEXViewd的宽
    int mRGBViewWidth;              //mRGBViewd的宽

    String mColorRGB;
    String mColorHEX;
    PickerView mPickView;
    private int mColor = Color.WHITE;  //默认颜色

    private static final int TEXT_SIZETEXT_SIZE = 18 ;  //RGB和HEX默认的文字大小
    private TextPaint textPaint;  //用于绘制RGBview和 HEXview 的文字

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //获取对应控件的宽
                mRGBViewWidth = mRGBView.getWidth();
                mHEXViewWidth = mHEXView.getWidth();
                setColorNotDrawCircle(Color.WHITE);//默认颜色为白色
                getWindow().getDecorView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        setContentView(R.layout.activity_picker);
        initView();

        //初始化TextPaint
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.density = getResources().getDisplayMetrics().density;
        textPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.rgb_and_hex_text_size));
//        textPaint.setTextSize(100);
        textPaint.setColor(Color.BLACK);


        Uri uri = getIntent().getParcelableExtra(KEY_IMAGE_URI);
        if (uri != null) {
            try {
                setBitmapUri(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void initView() {
        mColorView = (CardView) findViewById(R.id.color_view);
        lockButton = (ImageView) findViewById(R.id.lock_button);
        lockButton.setOnClickListener(this);
        mHEXView = (StaticLayoutView) findViewById(R.id.hex_et);
        mRGBView = (StaticLayoutView) findViewById(R.id.rgb_et);
        findViewById(R.id.take_photo).setOnClickListener(this);
        findViewById(R.id.open_photo).setOnClickListener(this);
        mPickView = (PickerView) findViewById(R.id.pick_view);
        findViewById(R.id.rgb_copy).setOnClickListener(this);
        findViewById(R.id.hex_copy).setOnClickListener(this);
        findViewById(R.id.share).setOnClickListener(this);
        findViewById(R.id.add_to_db).setOnClickListener(this);
        mImageView = (ImageView) findViewById(R.id.image);
        mAttacher = new MyPhotoViewAttacher(mImageView);
        mAttacher.setOnColorSelectedListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lock_button:
                setLockImageView(!mAttacher.isLocked());
                break;
            case R.id.color_view:
                break;
            case R.id.rgb_copy:
                copyText(mColorRGB);
                break;
            case R.id.hex_copy:
                copyText(mColorHEX);
                break;
            case R.id.share:
                share();
                break;
            case R.id.open_photo:
                selectPhoto();
                break;
            case R.id.take_photo:
                takePhoto();
                break;
            case R.id.add_to_db:
                ColorDialog.newInstance(mColor).show(getSupportFragmentManager(), "");
                break;
        }
    }

    @Override
    void setBitmapUri(Uri uri) throws FileNotFoundException {
        setLockImageView(false);
        mImageView.setImageBitmap(getBitmapByUri(uri));
        mAttacher.update();
    }

    private void setLockImageView(boolean isLocked) {
        mAttacher.setIsLocked(isLocked);
        if (!mAttacher.isLocked()) {
            lockButton.setImageResource(R.drawable.ic_lock_outline_grey600_24dp);
            mPickView.setVisibility(View.INVISIBLE);
            mPickView.setIsClear(true);
        } else {
            lockButton.setImageResource(R.drawable.ic_lock_open_grey600_24dp);
            mPickView.setVisibility(View.VISIBLE);
        }
    }

    private void copyText(String text) {
        CopyUtils.copyText(text, this);
    }

    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "color");
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.RGB) + mColorRGB
                + "\n"
                + getString(R.string.HEX) + mColorHEX);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, getTitle()));
    }

    public void setColorNotDrawCircle(int color) {
        this.mColor = color;
        mColorView.setCardBackgroundColor(color);

        int R = Color.red(color);
        int G = Color.green(color);
        int B = Color.blue(color);
        String r11 = Integer.toHexString(R);
        String g11 = Integer.toHexString(G);
        String b11 = Integer.toHexString(B);
        mColorHEX = "#" + r11 + g11 + b11;    //十六进制的颜色字符串。
        mColorRGB = R + "|" + G + "|" + B;

        mRGBView.setLayout(getStaticLayout(mColorRGB, mRGBViewWidth));
        mHEXView.setLayout(getStaticLayout(mColorHEX, mHEXViewWidth));
    }

    @Override
    public void onColorSelected(int newColor, int cx, int cy) {
        this.mColor = newColor;
        mColorView.setCardBackgroundColor(newColor);

        int R = Color.red(newColor);
        int G = Color.green(newColor);
        int B = Color.blue(newColor);

        String r11 = Integer.toHexString(R);
        String g11 = Integer.toHexString(G);
        String b11 = Integer.toHexString(B);
        mColorHEX = "#" + r11 + g11 + b11;    //十六进制的颜色字符串。
        mColorRGB = R + "|" + G + "|" + B;

        mRGBView.setLayout(getStaticLayout(mColorRGB, mRGBViewWidth));
        mHEXView.setLayout(getStaticLayout(mColorHEX, mHEXViewWidth));
        mPickView.drawCircle(cx, cy, newColor);
    }

    private StaticLayout getStaticLayout(String string, int viewWidth) {
        return new StaticLayout(string, textPaint, viewWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0f, true);
    }


}
