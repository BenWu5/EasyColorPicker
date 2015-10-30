package com.ben.colorpicker.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;


public abstract class SelectPhotoActivity extends AppCompatActivity {
    public  static final String KEY_IMAGE_URI = "key_image_uri";

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

    abstract void setBitmapUri(Uri uri) throws FileNotFoundException;

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
            Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }
    }
}
