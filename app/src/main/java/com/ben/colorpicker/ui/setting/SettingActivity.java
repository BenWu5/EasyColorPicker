package com.ben.colorpicker.ui.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.ben.colorpicker.BuildConfig;
import com.ben.colorpicker.R;
import com.ben.colorpicker.db.DataStore;
import com.ben.colorpicker.provider.ColorProvider;
import com.ben.colorpicker.ui.ToolbarActivity;
import com.ben.colorpicker.ui.license.LicenseActivity;

public class SettingActivity extends ToolbarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        findViewById(R.id.clear_list).setOnClickListener(this);
        findViewById(R.id.contact_us).setOnClickListener(this);
        findViewById(R.id.git_hub).setOnClickListener(this);
        findViewById(R.id.licenses).setOnClickListener(this);
        TextView version = (TextView) findViewById(R.id.version);
        version.setText(String.valueOf(BuildConfig.VERSION_NAME));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear_list:
                new AlertDialog.Builder(this).setTitle(android.R.string.dialog_alert_title)
                        .setMessage(R.string.clear_list_yes_or_no)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DataStore.delete().clear(SettingActivity.this, ColorProvider.uriColor());
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
                break;
            case R.id.contact_us:
                sendEmail();
                break;
            case R.id.git_hub:
                github();
                break;
            case R.id.licenses:
                licenseActivity();
                break;
        }
    }

    public void sendEmail() {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        String[] recipients = new String[]{"developerbenwu@gmail.com"};
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        emailIntent.setType("text/plain");
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    public void github() {
        Uri uri = Uri.parse("https://github.com/developerbenwu/DrawableText");
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(uri);
        startActivity(intent);
    }
    public void licenseActivity(){
        Intent intent = new Intent(this, LicenseActivity.class);
        startActivity(intent);
    }
}
