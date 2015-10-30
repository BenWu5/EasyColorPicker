package com.ben.colorpicker.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ben.colorpicker.R;
import com.ben.colorpicker.ui.SelectPhotoActivity;
import com.ben.colorpicker.ui.setting.SettingActivity;

/**
 * Created by Hui on 2015/10/25.
 */
public class PickerFragment extends Fragment implements View.OnClickListener {
    private SelectPhotoActivity mActivity;
    public static PickerFragment newInstance() {
        PickerFragment fragment = new PickerFragment();
        fragment.setArguments(null);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.include_image_bar, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (SelectPhotoActivity) context;
    }

    private void initView(View view) {
        view.findViewById(R.id.take_photo).setOnClickListener(this);
        view.findViewById(R.id.select_photo).setOnClickListener(this);
        view.findViewById(R.id.setting).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_photo:
                mActivity.selectPhoto();
                break;
            case R.id.take_photo:
                mActivity.takePhoto();
                break;
            case R.id.setting:
                Intent intent = new Intent(mActivity, SettingActivity.class);
                startActivity(intent);
                break;

        }
    }
}

