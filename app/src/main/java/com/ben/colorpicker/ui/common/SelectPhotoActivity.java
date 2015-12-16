package com.ben.colorpicker.ui.common;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ben.colorpicker.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

//这个Activity提供选择图片的功能
public abstract class SelectPhotoActivity extends AppCompatActivity {
    public static final String KEY_IMAGE_URI = "key_image_uri";

    private static final int TAKE_PHOTO_REQUEST = 1888;
    private static final int SELECT_PHOTO_REQUEST = 1228;
    private static final String SAVED_IMAGE_DIR_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "colorpicker";
    String capturePath;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            try {
                Uri uri = null;
                if (requestCode == SELECT_PHOTO_REQUEST) {
                    uri = data.getData();
                } else if (requestCode == TAKE_PHOTO_REQUEST) {
                    uri = Uri.fromFile(new File(capturePath));
                }
                setBitmapUri(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected Bitmap getBitmapByUri(Uri uri) throws FileNotFoundException {
        ContentResolver cr = this.getContentResolver();
        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
        return bitmap;
    }

    public abstract void setBitmapUri(Uri uri) throws FileNotFoundException;


    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    public boolean checkPermission() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add(getString(R.string.camera));
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add(getString(R.string.external_storage));

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                String message = getString(R.string.need_access) + " " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return false;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    public void selectPhoto() {
        Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
        intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
        startActivityForResult(intent, SELECT_PHOTO_REQUEST);
    }

    public void takePhoto() {
        if (checkPermission()) {
            takePhotoIntent();
        }

    }

    public void takePhotoIntent() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
            String out_file_path = SAVED_IMAGE_DIR_PATH;
            File dir = new File(out_file_path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            capturePath = SAVED_IMAGE_DIR_PATH + File.separator + "pic" + System.currentTimeMillis() + ".jpg";
            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(capturePath)));
            getImageByCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(getImageByCamera, TAKE_PHOTO_REQUEST);
        } else {
            Toast.makeText(getApplicationContext(), R.string.not_find_sd, Toast.LENGTH_LONG).show();
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message).setPositiveButton(android.R.string.ok, okListener)
                .setNegativeButton(android.R.string.cancel, null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                final List<String> permissionsList = new ArrayList<String>();
                if (!addPermission(permissionsList, Manifest.permission.CAMERA)) ;
                if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) ;
                if (permissionsList.size() == 0) {
                    takePhotoIntent();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
