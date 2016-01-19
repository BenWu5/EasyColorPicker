package com.ben.colorpicker.ui.donation;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.ben.colorpicker.R;


/**
 * Created by Hui on 2015/7/12.
 */
public class AlipayDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog materialDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.alipay)
                .setView(R.layout.dialog_alipay)
                .setPositiveButton(android.R.string.cancel, null)
                .setNegativeButton(android.R.string.copy, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= 11) {
                            ClipboardManager myClipboard = (ClipboardManager) getActivity().getSystemService(Activity.CLIPBOARD_SERVICE);
                            ClipData myClip = ClipData.newPlainText("text", "benwu831@126.com");
                            myClipboard.setPrimaryClip(myClip);
                        } else {
                            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                            clipboard.setText("benwu831@126.com");
                        }
                        Toast.makeText(getActivity(), "已复制帐号", Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
        return materialDialog;
    }

}
