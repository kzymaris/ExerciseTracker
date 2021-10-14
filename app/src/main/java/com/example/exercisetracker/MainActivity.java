package com.example.exercisetracker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.exercisetracker.ui.main.ExerciseFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.exercisetracker.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabs;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private int currTab;
    private int tagCount = 0;
    private ArrayList<Integer> tags = new ArrayList<>();

    public static final String NAME_KEY = "name_key";

    private ArrayList<ArrayList<ExerciseLayout>> entries;
    private ArrayList<Integer> lastId = new ArrayList<>();
    private ActivityResultLauncher<Intent> insertResultLauncher;
    public ActivityResultLauncher<Intent> editResultLauncher;

    private static final int REQUEST_PERMISSION_WRITE = 1001;
    private boolean permissionGranted;
    private static final String FILE_NAME = "exercises.json";
    private ArrayList<Exercises> eList;
    private int dumbAssCounter = 0;
    private boolean dumbAssBoolean = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabs = findViewById(R.id.tabs);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        entries = new ArrayList<>();

        viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(5);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position,
                                       float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                currTab = position;
            }
        });


        sectionsPagerAdapter = new SectionsPagerAdapter(this);

        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setCurrentItem(0);
        new TabLayoutMediator(tabs, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText("Day " + (position + 1));
                    }
                }
        ).attach();

        insertResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent i = result.getData();
                            int tag = tags.get(currTab);
                            ExerciseFragment e = (ExerciseFragment) getSupportFragmentManager().findFragmentByTag("f" + tag);
                            populateExercise(i, true, e.getContainer());
                        }
                    }
                });
        editResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent i = result.getData();
                            int tag = tags.get(currTab);
                            ExerciseFragment e = (ExerciseFragment) getSupportFragmentManager().findFragmentByTag("f" + tag);
                            populateExercise(i, false, e.getContainer());
                        }
                    }
                });


        FileReader fr = null;
        try {
            File f = new File(Environment.getExternalStorageDirectory(), FILE_NAME);
            fr = new FileReader(f);
            eList = new Gson().fromJson(fr, Tabs.class).getList();
            if(eList != null){
                for (Exercises es : eList){
                    addTab();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(fr != null){
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if(entries.size() == 0){
            addTab();
        }


        if(!permissionGranted){
            checkPermissions();
        }

    }

    public void populateTabs() {

        if(dumbAssCounter != eList.size()-1){
            dumbAssCounter++;
        }else if(!dumbAssBoolean){
            dumbAssBoolean = true;
            if(eList != null){
                for (int i=0; i < eList.size(); i++){
                    int tag = tags.get(i);
                    ExerciseFragment ef = (ExerciseFragment) getSupportFragmentManager().findFragmentByTag("f" + tag);
                    for(Exercise e : eList.get(i).getList()){
                        addExercise(ef.getContainer(), e.getName(), e.getWeight(), i);
                    }
                }
            }
        }


    }

    @Override
    protected void onStop() {
        if(entries != null && entries.size() > 0){
            Tabs t = new Tabs();
            t.setList(entries);

            FileOutputStream fos = null;
            File f = new File(Environment.getExternalStorageDirectory(), FILE_NAME);
            try {
                fos = new FileOutputStream(f);
                String s = new Gson().toJson(t);
                fos.write(s.getBytes());
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if(fos != null){
                    try {
                        fos.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        super.onStop();
    }

    private void addExercise(ConstraintLayout container, String name, String weight, int index) {
        ExerciseLayout e = new ExerciseLayout(MainActivity.this, name, weight, editResultLauncher);
        e.setId(1234+entries.get(index).size());
        e.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        e.setPadding(40,20,40,20);
        entries.get(index).add(e);
        container.addView(e);

        ConstraintSet cs = new ConstraintSet();
        cs.clone(container);
        if(entries.get(index).size() == 1){
            cs.connect(e.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        }else{
            cs.connect(e.getId(), ConstraintSet.TOP, lastId.get(index), ConstraintSet.BOTTOM);
        }

        cs.connect(e.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
        cs.connect(e.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
        cs.applyTo(container);
        lastId.set(index, e.getId());
    }

    protected void populateExercise( Intent data, boolean isInsert, ConstraintLayout container) {

        if(isInsert){
            addExercise(container, data.getStringExtra(NAME_KEY), "5", currTab);
        }
        if(!isInsert){
            Iterator<ExerciseLayout> it = entries.get(currTab).iterator();
            while(it.hasNext()){
                ExerciseLayout e = it.next();
                if(data.getStringExtra(ManageExercise.OLD_NAME) != null){
                    if(e.getName().equals(data.getStringExtra(ManageExercise.OLD_NAME))){
                        e.setName(data.getStringExtra(NAME_KEY));
                    }
                }
                if(data.getStringExtra(ManageExercise.TO_DELETE) != null){
                    if(e.getName().equals(data.getStringExtra(ManageExercise.TO_DELETE))){

                        it.remove();
                        container.removeView(e);
                        ArrayList<ExerciseLayout> temp = new ArrayList<>();
                        for(ExerciseLayout el : entries.get(currTab)){
                            temp.add(el);
                            container.removeView(el);
                        }
                        entries.set(currTab, new ArrayList<ExerciseLayout>());
                        for(ExerciseLayout el : temp){
                            addExercise(container, el.getName(),el.getWeight(), currTab);
                        }

                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.addTab){
            addTab();
        }
        if(id == R.id.remove && tabs.getTabCount() > 1){
            sectionsPagerAdapter.removeTabPage(currTab);
            tags.remove(currTab);
            entries.remove(currTab);
            lastId.remove(currTab);
        }
        if(id == R.id.addExercise){
            Intent i = new Intent(MainActivity.this, ManageExercise.class);
            insertResultLauncher.launch(i);
        }

        return super.onOptionsItemSelected(item);
    }

    private void addTab() {
        sectionsPagerAdapter.addTabPage();
        tags.add(tagCount);
        tagCount++;
        entries.add(new ArrayList<ExerciseLayout>());
        lastId.add(0);
    }

    // Checks if external storage is available for read and write
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    // Initiate request for permissions.
    private boolean checkPermissions() {

        if (!isExternalStorageWritable()) {
            Toast.makeText(this, "This app only works on devices with usable external storage",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_WRITE);
            return false;
        } else {
            return true;
        }
    }

    // Handle permissions result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_WRITE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionGranted = true;
                Toast.makeText(this, "External storage permission granted",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "You must grant permission!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}