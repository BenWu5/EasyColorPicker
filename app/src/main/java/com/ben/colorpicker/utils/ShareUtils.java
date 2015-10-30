package com.ben.colorpicker.utils;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Hui on 2015/10/25.
 */
public class ShareUtils {
    public final static void share(String string,String title, Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, string);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, title));
    }


}
