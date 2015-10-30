package com.ben.colorpicker.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ben.colorpicker.R;
import com.ben.colorpicker.db.DataStore;
import com.ben.colorpicker.model.ColorModel;
import com.ben.colorpicker.provider.ColorProvider;
import com.ben.colorpicker.utils.ColorUtils;

import java.util.Calendar;
import java.util.UUID;

/**
 * Created by Hui on 2015/10/25.
 */
public class ColorDialog extends DialogFragment implements View.OnClickListener {
    private static final String KEY_COLOR = "KEY_COLOR";
    private static final String KEY_COLOR_MODEL = "KEY_COLOR_MODEL";

    private ColorModel colorModel;


    private CardView colorView;
    private EditText noteEt;
    private EditText dateEt;
    private EditText timeEt;

    private String colorId;
    private int color;
    private String note;
    private Calendar calendar;

    public static ColorDialog newInstance(int color) {
        Bundle args = new Bundle();
        args.putInt(KEY_COLOR, color);
        ColorDialog fragment = new ColorDialog();
        fragment.setArguments(args);
        return fragment;
    }
    public static ColorDialog newInstance(ColorModel colorModel) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_COLOR_MODEL, colorModel);
        ColorDialog fragment = new ColorDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        colorModel = getArguments().getParcelable(KEY_COLOR_MODEL);
        calendar = Calendar.getInstance();
        if(colorModel==null){
            colorModel = new ColorModel();
            color = getArguments().getInt(KEY_COLOR);
            colorModel.setColor(color);
        }else {
            colorId = colorModel.getId();
            color = colorModel.getColor();
            note= colorModel.getNote();
            calendar.setTimeInMillis(colorModel.getCreateTime());
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_color, null);
        noteEt = (EditText) view.findViewById(R.id.note_et);
        dateEt = (EditText) view.findViewById(R.id.date_et);
        timeEt = (EditText) view.findViewById(R.id.time_et);
        noteEt.setText(colorModel.getNote());
        noteEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                note = noteEt.getText().toString();
            }
        });
        EditText hexEt = (EditText) view.findViewById(R.id.hex_et);
        colorView = (CardView) view.findViewById(R.id.color_view);
        colorView.setCardBackgroundColor(color);
        hexEt.setText("#" + ColorUtils.D2HEX(color));
        builder.setView(view)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                ColorModel colorModel = new ColorModel();
                                colorModel.setId(colorId);
                                colorModel.setColor(color);
                                colorModel.setNote(note);
                                colorModel.setCreateTime(calendar.getTimeInMillis());
                                boolean isNew = TextUtils.isEmpty(colorModel.getId());
                                if (isNew) {
                                    colorModel.setId(UUID.randomUUID().toString());
                                    DataStore.insert().model(colorModel).into(getActivity(), ColorProvider.uriColor());
                                    Toast.makeText(getActivity(),R.string.added_to_list,Toast.LENGTH_LONG).show();
                                } else {
                                    DataStore.update().model(colorModel).update(getActivity(), ColorProvider.uriColor());
                                }

                            }
                        }).setNegativeButton(android.R.string.cancel, null);
        dateEt.setText(DateUtils.formatDateTime(getActivity(), calendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE));
        timeEt.setText(DateUtils.formatDateTime(getActivity(), calendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME));
        timeEt.setOnClickListener(this);
        dateEt.setOnClickListener(this);
        return builder.create();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_et:
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        dateEt.setText(DateUtils.formatDateTime(getActivity(), calendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE));

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.time_et:
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);
                        timeEt.setText(DateUtils.formatDateTime(getActivity(), calendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME));
                    }
                },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();;
                break;
        }
    }
}
