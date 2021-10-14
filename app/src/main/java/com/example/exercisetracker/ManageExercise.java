package com.example.exercisetracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class ManageExercise extends AppCompatActivity {

    public static final String OLD_NAME = "old_name";
    public static final String TO_DELETE = "to_delete";
    public static String oldName;
    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_exercise_layout);

        et = findViewById(R.id.editText);

        Intent i = getIntent();
        String name = i.getStringExtra(MainActivity.NAME_KEY);
        if(name != null){
            et.setText(name);
            oldName = name;
            Button delete = new Button(this);
            delete.setText("Delete");
            delete.setId(1234+456);
            delete.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent();
                    i.putExtra(TO_DELETE, oldName);
                    setResult(RESULT_OK, i);
                    onBackPressed();
                }
            });
            ConstraintLayout c = findViewById(R.id.manage_container);
            c.addView(delete);

            ConstraintSet cs = new ConstraintSet();
            cs.clone(c);

            cs.connect(delete.getId(), ConstraintSet.TOP,R.id.button, ConstraintSet.BOTTOM);
            cs.connect(R.id.button, ConstraintSet.BOTTOM,delete.getId(), ConstraintSet.TOP);
            cs.connect(delete.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
            cs.connect(delete.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
            cs.connect(delete.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
            cs.applyTo(c);
        }
    }

    public void saveExercise(View view) {
        Intent i = new Intent();
        i.putExtra(MainActivity.NAME_KEY, et.getText().toString());
        i.putExtra(OLD_NAME, oldName);
        setResult(RESULT_OK, i);
        onBackPressed();
    }
}
