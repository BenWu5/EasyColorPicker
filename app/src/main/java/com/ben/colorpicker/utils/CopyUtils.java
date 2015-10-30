package com.ben.colorpicker.utils;

import android.content.Context;
import android.text.ClipboardManager;
import android.widget.Toast;

import com.ben.colorpicker.R;

/**
 * Created by Hui on 2015/10/25.
 */
public class CopyUtils {
    public static void copyText(String text,Context context) {
        ClipboardManager c = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
        c.setText(text);
        Toast.makeText(context, R.string.copied_to_clipboard, Toast.LENGTH_LONG).show();
    }
}
