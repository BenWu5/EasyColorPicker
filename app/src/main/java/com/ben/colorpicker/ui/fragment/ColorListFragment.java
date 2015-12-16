package com.ben.colorpicker.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ben.colorpicker.R;
import com.ben.colorpicker.db.DataStore;
import com.ben.colorpicker.db.Tables;
import com.ben.colorpicker.model.ColorModel;
import com.ben.colorpicker.provider.ColorProvider;
import com.ben.colorpicker.ui.ColorDialog;
import com.ben.colorpicker.ui.adapter.ColorAdapter;
import com.ben.colorpicker.ui.recycler.SectionsDecoration;
import com.ben.colorpicker.utils.ColorUtils;
import com.ben.colorpicker.utils.CopyUtils;
import com.ben.colorpicker.utils.ShareUtils;
import com.ben.colorpicker.view.EmptyView;

/**
 * Created by Hui on 2015/10/25.
 */
public class ColorListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ColorAdapter.MoreOnClickListener, ColorAdapter.CopyOnClickListener {
    ColorAdapter adapter;
    private EmptyView emptyView;

    public static ColorListFragment newInstance() {
        ColorListFragment fragment = new ColorListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        emptyView = (EmptyView) view.findViewById(R.id.empty_view);
        setupRecyclerView(recyclerView);
        adapter = new ColorAdapter(getActivity(), null, this, this);
        adapter.registerAdapterDataObserver(new AdapterObserver());
        recyclerView.setAdapter(adapter);
        getLoaderManager().initLoader(1, null, this);
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return DataStore.query().projection(Tables.Color.PROJECTION).sortOrderDesc(Tables.Color.CREATE_TIME.getName()).query(getActivity(), ColorProvider.uriColor());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.setCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    protected void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.addItemDecoration(new SectionsDecoration(true));
    }

    @Override
    public void copyOnClick(View view, ColorModel colorModel) {
        CopyUtils.copyText("#" + ColorUtils.D2HEX(colorModel.getColor()), getActivity());
    }

    @Override
    public void moreOnClick(View view, final ColorModel colorModel) {
        PopupMenu popup = new PopupMenu(getActivity(), view);
        popup.getMenuInflater()
                .inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        DataStore.delete().model(colorModel).delete(getActivity(), ColorProvider.uriColor());
                        break;
                    case R.id.edit:
                        ColorDialog.newInstance(colorModel).show(getChildFragmentManager(), "");
                        break;
                    case R.id.share:
                        ShareUtils.share("#" + ColorUtils.D2HEX(colorModel.getColor()),
                                TextUtils.isEmpty(colorModel.getNote()) ? null : colorModel.getNote(), getActivity());
                        break;
                }
                return true;
            }
        });
        popup.show();
    }


    private class AdapterObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            emptyView.setEmpty(adapter.getItemCount() == 0);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            emptyView.setEmpty(adapter.getItemCount() == 0);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            emptyView.setEmpty(adapter.getItemCount() == 0);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            emptyView.setEmpty(adapter.getItemCount() == 0);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            emptyView.setEmpty(adapter.getItemCount() == 0);
        }
    }

}
