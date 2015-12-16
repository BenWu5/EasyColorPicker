package com.ben.colorpicker.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;


public class EmptyView extends TextView {
    private boolean isEmpty = false;

    public EmptyView(Context context) {
        super(context);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
        update();
    }

    private void update() {
        if (isEmpty) {
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
        }
    }

}
