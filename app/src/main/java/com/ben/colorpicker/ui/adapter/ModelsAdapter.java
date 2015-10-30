package com.ben.colorpicker.ui.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.ben.colorpicker.model.Model;


public abstract class ModelsAdapter<M extends Model> extends RecyclerView.Adapter<ModelsAdapter.ModelViewHolder<M>> {
    private final OnModelClickListener<M> onModelClickListener;

    private Cursor cursor;

    public ModelsAdapter(OnModelClickListener<M> onModelClickListener) {
        this.onModelClickListener = onModelClickListener;
    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    @Override
    public ModelViewHolder<M> onCreateViewHolder(ViewGroup parent, int viewType) {
        final ModelViewHolder<M> modelViewHolder = createModelViewHolder(parent, viewType);
        modelViewHolder.setOnModelClickListener(onModelClickListener);
        return modelViewHolder;
    }

    @Override
    public void onBindViewHolder(ModelViewHolder<M> holder, int position) {
        cursor.moveToPosition(position);
        final M model = modelFromCursor(cursor);
        holder.bindViewHolder(model, cursor, position);
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
        if (cursor != null) {
            cursor.moveToFirst();
        }
        notifyDataSetChanged();
    }


    protected abstract ModelViewHolder<M> createModelViewHolder(ViewGroup parent, int viewType);

    protected abstract M modelFromCursor(Cursor cursor);


    public static interface OnModelClickListener<M extends Model> {
        public void onModelClick(View view, M model, Cursor cursor, int position);
    }

    public static abstract class ModelViewHolder<M extends Model> extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OnModelClickListener<M> onModelClickListener;

        private M model;
        private Cursor cursor;
        private int position;

        public ModelViewHolder(View itemView) {
            super(itemView);
        }

        public void bindViewHolder(M model, Cursor cursor, int position) {
            this.model = model;
            this.cursor = cursor;
            this.position = position;
            bind(model, cursor, position);
        }

        public M getModel() {
            return model;
        }

        protected abstract void bind(M model, Cursor cursor, int position);

        private void setOnModelClickListener(OnModelClickListener<M> onModelClickListener) {
            this.onModelClickListener = onModelClickListener;
            if (onModelClickListener != null) {
                itemView.setOnClickListener(this);
            } else {
                itemView.setOnClickListener(null);
            }
        }

        @Override
        public void onClick(View v) {
            cursor.moveToPosition(position);
            if (onModelClickListener != null) {
                onModelClickListener.onModelClick(v, model, cursor, position);
            }
        }
    }
}
