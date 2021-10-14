package com.example.exercisetracker;

import java.util.ArrayList;

public class Tabs {
    ArrayList<Exercises> tabs;

    public ArrayList<Exercises> getList() {
        return tabs;
    }

    public void setList(ArrayList<ArrayList<ExerciseLayout>> e) {
        ArrayList<Exercises> tabs = new ArrayList<>();
        for(ArrayList<ExerciseLayout> layouts : e){
            Exercises ex = new Exercises();
            ex.setList(layouts);
            tabs.add(ex);
        }
        this.tabs = tabs;
    }
}
