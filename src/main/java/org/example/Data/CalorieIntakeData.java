package org.example.Data;
import java.time.LocalDate;

public class CalorieIntakeData extends HealthData {
    private String foodItem;
    private int calories;

    public CalorieIntakeData(LocalDate date, String foodItem, int calories) {
        super(date);
        this.foodItem = foodItem;
        this.calories = calories;
    }

    public String getFoodItem() {
        return foodItem;
    }

    public int getCalories() {
        return calories;
    }

    @Override
    public String toString() {
        return String.format("%s,Calorie,%d,%s", getDate(), calories, foodItem);
    }
}
