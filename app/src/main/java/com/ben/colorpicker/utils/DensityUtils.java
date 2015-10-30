package com.ben.colorpicker.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by Hui on 2015/10/30.
 */
public class DensityUtils {
    private DensityUtils  () {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    /**
     * sp转px
     *
     * @param context
     * @param val
     * @return
     */
    public static int sp2px(Context context, float spVal)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }






}
