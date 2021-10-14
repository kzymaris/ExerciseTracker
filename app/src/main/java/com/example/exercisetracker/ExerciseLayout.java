package com.example.exercisetracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;


public class ExerciseLayout extends LinearLayout {
    public static final int SIZE = 200;
    TextView name;
    EditText weight;
    Button one;
    Button five;
    Activity c;

    public String getName() {
        return name.getText().toString();
    }


    public String getWeight() {
        return weight.getText().toString();
    }

    public void setName(String n){
        name.setText(n);
    }


    public ExerciseLayout(Context context, String n, String w, ActivityResultLauncher<Intent> l) {
        super(context);
        c = (MainActivity) context;
        final ActivityResultLauncher<Intent> launcher = l;
        name = new TextView(context);
        name.setText(n);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            name.setTextAppearance(R.style.TextAppearance_AppCompat_Large);
        }
        name.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));
        name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(c, ManageExercise.class);
                i.putExtra(MainActivity.NAME_KEY, name.getText());
                launcher.launch(i);
            }
        });
        weight = new EditText(context);
        weight.setText(w);
        weight.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,0));
        one = new Button(context);
        one.setText("+2.5");
        one.setBackgroundResource(R.drawable.round_button);
        one.setLayoutParams(new LayoutParams(SIZE, SIZE, 0));
        one.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                add(2.5);
            }
        });
        five = new Button(context);
        five.setText("+5");
        five.setBackgroundResource(R.drawable.round_button);
        five.setLayoutParams(new LayoutParams(SIZE, SIZE, 0));
        five.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                add(5);
            }
        });
        addView(name);
        addView(weight);
        addView(one);
        addView(five);
    }


    public void add(double num) {
        try{
            weight.setText(Double.toString(Double.valueOf(weight.getText().toString())+num));
        }catch(Exception e){
            weight.setText(Double.toString(num));
        }
    }
}
