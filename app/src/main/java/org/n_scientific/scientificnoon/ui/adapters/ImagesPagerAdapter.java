package org.n_scientific.scientificnoon.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.n_scientific.scientificnoon.ui.fragments.ImageFragment;

import java.util.ArrayList;

/**
 * Created by mohammad on 04/08/17.
 */

public class ImagesPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<String> images;


    public ImagesPagerAdapter(FragmentManager fm, ArrayList<String> images) {
        super(fm);
        this.images = images;
    }

    @Override
    public Fragment getItem(int position) {
        return ImageFragment.newInstance(images.get(position));
    }

    @Override
    public int getCount() {
        return images.size();
    }
}
