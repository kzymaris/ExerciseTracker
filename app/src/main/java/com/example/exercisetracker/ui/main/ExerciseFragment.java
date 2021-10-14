package com.example.exercisetracker.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.exercisetracker.MainActivity;
import com.example.exercisetracker.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ExerciseFragment extends Fragment {

    public static final String ARG_SECTION_NUMBER = "section_number";
    View layout;
    ConstraintLayout container;
    private static FragmentActivity frag;


    public static ExerciseFragment newInstance(long id, FragmentActivity fa) {
        frag = fa;
        ExerciseFragment fragment = new ExerciseFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ARG_SECTION_NUMBER, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_main, container, false);
        this.container = layout.findViewById(R.id.container);
        ((MainActivity) frag).populateTabs();
        return layout;
    }

    public ConstraintLayout getContainer(){
        return container;
    }
}