package com.example.exercisetracker.ui.main;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.exercisetracker.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStateAdapter {

    private long curId = 0;

    private final List<ExerciseFragment> fragments = new ArrayList<>();
    private final List<Long> ids = new ArrayList<>();
    private FragmentActivity fa;

    public SectionsPagerAdapter(FragmentActivity fa) {
        super(fa);
        this.fa = fa;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return ExerciseFragment.newInstance(ids.get(position), fa);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }

    @Override
    public long getItemId(int position){
        return ids.get(position);
    }

    @Override
    public boolean containsItem (long itemId){
        return ids.contains(itemId);
    }

    public void removeTabPage(int position) {
        fragments.remove(position);
        ids.remove(position);
        notifyDataSetChanged();
    }

    public void addTabPage() {
        fragments.add(ExerciseFragment.newInstance(curId, fa));
        ids.add(curId);
        curId++;
        notifyDataSetChanged();
    }

}