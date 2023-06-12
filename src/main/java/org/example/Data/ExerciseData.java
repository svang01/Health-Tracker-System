package org.example.Data;

import java.time.LocalDate;

public class ExerciseData extends HealthData {
    private String exerciseType;
    private int durationMinutes;
    private int caloriesBurned;

    public ExerciseData(LocalDate date, String exerciseType, int durationMinutes, int caloriesBurned) {
        super(date);
        this.exerciseType = exerciseType;
        this.durationMinutes = durationMinutes;
        this.caloriesBurned = caloriesBurned;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    @Override
    public String toString() {
        return String.format("%s,Exercise,%d,%d,%s", getDate(), durationMinutes, caloriesBurned, exerciseType);
    }
}

