package com.example.exercisetracker;

import java.util.ArrayList;

class Exercises {
    ArrayList<Exercise> list;

    public ArrayList<Exercise> getList() {
        return list;
    }

    public void setList(ArrayList<ExerciseLayout> list) {
        ArrayList<Exercise> eList = new ArrayList<>();
        for ( ExerciseLayout e : list ) {
            eList.add(new Exercise(e.getName(),e.getWeight()));
        }
        this.list = eList;
    }
}