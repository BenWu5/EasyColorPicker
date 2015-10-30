package com.ben.colorpicker.ui.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.ben.colorpicker.R;

/**
 * Created by Hui on 2015/10/25.
 */
public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
    private int[] imageResId = {
            R.drawable.ic_colorize_grey600_24dp,
            R.drawable.ic_list_grey600_24dp,
    };

    private Fragment[] fragments = new Fragment[]{
            PickerFragment.newInstance(), ColorListFragment.newInstance(),
    };
    private Context context;

    public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        if (fragments.length != imageResId.length) {
            throw new IllegalStateException("fragments.length != tabTitles.length");
        }
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = ContextCompat.getDrawable(context, imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}
