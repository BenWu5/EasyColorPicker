package com.ben.colorpicker.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ben.colorpicker.R;
import com.ben.colorpicker.model.ColorModel;
import com.ben.colorpicker.ui.recycler.SectionsDecoration;
import com.ben.colorpicker.utils.ColorUtils;

import java.util.Calendar;

public class ColorAdapter extends ModelsAdapter<ColorModel> implements SectionsDecoration.Adapter<ColorAdapter.HeaderViewHolder> {
    Context context;
    Calendar calendar = Calendar.getInstance();
    public ColorAdapter(Context context, ModelsAdapter.OnModelClickListener<ColorModel> onModelClickListener, MoreOnClickListener moreOnClickListener, CopyOnClickListener copyOnClickListener) {
        super(onModelClickListener);
        this.context = context;
        this.moreOnClickListener = moreOnClickListener;
        this.copyOnClickListener = copyOnClickListener;

    }

    @Override
    protected ViewHolder createModelViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.li_color_card, parent, false));
        viewHolder.setCopyOnClickListener(copyOnClickListener);
        viewHolder.setMoreOnClickListener(moreOnClickListener);
        return viewHolder;
    }

    @Override
    protected ColorModel modelFromCursor(Cursor cursor) {
        return ColorModel.from(cursor);
    }

    @Override
    public long getHeaderId(int position) {
        final Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        ColorModel colorModel = ColorModel.from(cursor);
        calendar.setTimeInMillis(colorModel.getCreateTime());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0 );
        return calendar.getTimeInMillis();
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.li_color_header, parent, false));
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder viewHolder, int position) {
        final Cursor cursor = getCursor();
        cursor.moveToPosition(position);
        ColorModel colorModel = ColorModel.from(cursor);
        viewHolder.titleTextView.setText(DateUtils.formatDateTime(context, colorModel.getCreateTime(), DateUtils.FORMAT_SHOW_DATE));
    }

    private class ViewHolder extends ModelViewHolder<ColorModel> {
        private final View rootView;
        private final TextView noteTextView;
        private final TextView colorTextView;
        private final ImageView copyImageView;
        private final ImageView moreImageView;
        private MoreOnClickListener moreOnClickListener;
        private CopyOnClickListener copyOnClickListener;
        private Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.root_view);
            moreImageView = (ImageView) itemView.findViewById(R.id.more_iv);
            noteTextView = (TextView) itemView.findViewById(R.id.note);
            colorTextView = (TextView) itemView.findViewById(R.id.color_hex);
            copyImageView = (ImageView) itemView.findViewById(R.id.copy_iv);
        }

        public void setCopyOnClickListener(CopyOnClickListener copyOnClickListener) {
            this.copyOnClickListener = copyOnClickListener;
        }

        public void setMoreOnClickListener(MoreOnClickListener moreOnClickListener) {
            this.moreOnClickListener = moreOnClickListener;
        }

        @Override
        protected void bind(ColorModel model, Cursor cursor, int position) {
            rootView.setBackgroundColor(model.getColor());
            int colorFilter = ColorUtils.getBorW(model.getColor());
            if (model.getNote() != null) {
                noteTextView.setVisibility(View.VISIBLE);
                noteTextView.setText(model.getNote());
                noteTextView.setTextColor(colorFilter);
            } else {
                noteTextView.setVisibility(View.INVISIBLE);
            }
            colorTextView.setText("#" + ColorUtils.D2HEX(model.getColor()));
            colorTextView.setTextColor(colorFilter);
            copyImageView.setColorFilter(colorFilter);
            moreImageView.setColorFilter(colorFilter);
            moreImageView.setOnClickListener(this);
            copyImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            switch (v.getId()) {
                case R.id.more_iv:
                    if (moreOnClickListener != null) {
                        moreOnClickListener.moreOnClick(v, getModel());
                    }
                    break;
                case R.id.copy_iv:
                    if (copyOnClickListener != null) {
                        copyOnClickListener.copyOnClick(v, getModel());
                    }
                    break;
            }


        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
        }
    }

    private MoreOnClickListener moreOnClickListener;

    public interface MoreOnClickListener {
        void moreOnClick(View view, ColorModel colorModel);
    }

    public void setMoreOnClickListener(MoreOnClickListener moreOnClickListener) {
        this.moreOnClickListener = moreOnClickListener;
    }

    private CopyOnClickListener copyOnClickListener;

    public interface CopyOnClickListener {
        void copyOnClick(View view, ColorModel colorModel);
    }

    public void setCopyOnClickListener(CopyOnClickListener copyOnClickListener) {
        this.copyOnClickListener = copyOnClickListener;
    }
}

