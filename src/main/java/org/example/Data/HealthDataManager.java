package org.example.Data;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Scanner;


public class HealthDataManager {
    private static final String CALORIE_FILE = "calorie.txt";
    private static final String EXERCISE_FILE = "exercise.txt";
    private static final String SLEEP_FILE = "sleep.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public void addCalorieIntake(Scanner scanner) {
        System.out.print("Enter food item: ");
        String foodItem = scanner.nextLine();

        System.out.print("Enter caloric value: ");
        int calories = scanner.nextInt();
        scanner.nextLine(); // Consume remaining newline character

        System.out.print("Enter date (yyyy-MM-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        HealthData calorieData = new CalorieIntakeData(date, foodItem, calories);
        writeDataToFile(CALORIE_FILE, calorieData);
        System.out.println("Calorie intake added successfully.");
    }

    public void addExerciseActivity(Scanner scanner) {
        System.out.print("Enter exercise type: ");
        String exerciseType = scanner.nextLine();

        System.out.print("Enter duration in minutes: ");
        int durationMinutes = scanner.nextInt();
        scanner.nextLine(); // Consume remaining newline character

        System.out.print("Enter estimated calories burned: ");
        int caloriesBurned = scanner.nextInt();
        scanner.nextLine(); // Consume remaining newline character

        LocalDate date = LocalDate.now();
        HealthData exerciseData = new ExerciseData(date, exerciseType, durationMinutes, caloriesBurned);

        writeDataToFile(EXERCISE_FILE, exerciseData);
        System.out.println("Exercise activity added successfully.");
    }

    public void addSleepRecord(Scanner scanner) {
        System.out.print("Enter sleep date (yyyy-MM-dd): ");
        LocalDate sleepDate = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);

        System.out.print("Enter sleep time (HH:mm): ");
        LocalTime sleepTime = LocalTime.parse(scanner.nextLine(), TIME_FORMATTER);

        System.out.print("Enter wake-up time (HH:mm): ");
        LocalTime wakeUpTime = LocalTime.parse(scanner.nextLine(), TIME_FORMATTER);

        HealthData sleepData = new SleepData(sleepDate, sleepTime, wakeUpTime);
        writeDataToFile(SLEEP_FILE, sleepData);
        System.out.println("Sleep record added successfully.");
    }

    public void analyzeDailyCaloricBalance() {
        List<HealthData> calorieDataList = readDataFromFile(CALORIE_FILE);
        List<HealthData> exerciseDataList = readDataFromFile(EXERCISE_FILE);

        Map<LocalDate, Integer> calorieIntakeMap = new HashMap<>();
        Map<LocalDate, Integer> caloriesBurnedMap = new HashMap<>();

        // Calculate calorie intake for each day
        for (HealthData calorieData : calorieDataList) {
            CalorieIntakeData intakeData = (CalorieIntakeData) calorieData;
            LocalDate date = intakeData.getDate();
            int calories = intakeData.getCalories();

            if (calorieIntakeMap.containsKey(date)) {
                calorieIntakeMap.put(date, calorieIntakeMap.get(date) + calories);
            } else {
                calorieIntakeMap.put(date, calories);
            }
        }

        // Calculate calories burned for each day
        for (HealthData exerciseData : exerciseDataList) {
            ExerciseData exercise = (ExerciseData) exerciseData;
            LocalDate date = exercise.getDate();
            int caloriesBurned = exercise.getCaloriesBurned();

            if (caloriesBurnedMap.containsKey(date)) {
                caloriesBurnedMap.put(date, caloriesBurnedMap.get(date) + caloriesBurned);
            } else {
                caloriesBurnedMap.put(date, caloriesBurned);
            }
        }

        // Display daily caloric balance
        System.out.println("------ Daily Caloric Balance ------");
        for (LocalDate date : calorieIntakeMap.keySet()) {
            int calorieIntake = calorieIntakeMap.getOrDefault(date, 0);
            int caloriesBurned = caloriesBurnedMap.getOrDefault(date, 0);
            int caloricBalance = calorieIntake - caloriesBurned;
            System.out.println("Date: " + date + ", Caloric Balance: " + caloricBalance);
        }
        System.out.println("----------------------------------");
    }
    public void analyzeSleepData(Scanner scanner) {
        List<HealthData> sleepDataList = readDataFromFile(SLEEP_FILE);

        if (sleepDataList.isEmpty()) {
            System.out.println("No sleep data available.");
            return;
        }

        System.out.print("Enter the start date (yyyy-MM-dd): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);

        System.out.print("Enter the end date (yyyy-MM-dd): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);

        long totalSleepMinutes = 0;
        int validDataCount = 0;
        Map<LocalDate, Long> sleepMinutesMap = new HashMap<>();

        for (HealthData sleepData : sleepDataList) {
            LocalDate date = sleepData.getDate();
            if (date.isAfter(endDate) || date.isBefore(startDate)) {
                continue; // Skip data outside the given period
            }

            LocalTime sleepTime = ((SleepData) sleepData).getSleepTime();
            LocalTime wakeUpTime = ((SleepData) sleepData).getWakeUpTime();

            if (wakeUpTime.isBefore(sleepTime)) {
                // Adjust wake-up time and date if it's earlier than the sleep time
                wakeUpTime = wakeUpTime.plusHours(24);
                date = date.plusDays(1);
            }

            long sleepMinutes = sleepTime.until(wakeUpTime, ChronoUnit.MINUTES);

            totalSleepMinutes += sleepMinutes;
            validDataCount++;
            sleepMinutesMap.put(date, sleepMinutes);
        }

        if (validDataCount == 0) {
            System.out.println("No sleep data available for the given period.");
            return;
        }

        long averageSleepMinutes = totalSleepMinutes / validDataCount;
        long averageSleepHours = averageSleepMinutes / 60;
        long averageSleepMinutesRemainder = averageSleepMinutes % 60;
        long totalSleepHours = totalSleepMinutes / 60;

        System.out.println("Average Sleep Time: " + averageSleepHours + " hours " + averageSleepMinutesRemainder + " minutes");
        System.out.println("Total Sleep Hours: " + totalSleepHours + " hours");

        for (Map.Entry<LocalDate, Long> entry : sleepMinutesMap.entrySet()) {
            LocalDate date = entry.getKey();
            long sleepMinutes = entry.getValue();

            if (sleepMinutes < averageSleepMinutes) {
                long sleepHours = sleepMinutes / 60;
                long sleepMinutesRemainder = sleepMinutes % 60;

                System.out.println("Date: " + date + ", Sleep Time: " + sleepHours + " hours " + sleepMinutesRemainder + " minutes (Below Average)");
            }
        }
    }

    public void analyzeExerciseLog() {
        List<HealthData> exerciseDataList = readDataFromFile(EXERCISE_FILE);

        for (HealthData exerciseData : exerciseDataList) {
            System.out.println(exerciseData);
        }
    }

public void analyzeHealthSummary() {
    List<HealthData> calorieDataList = readDataFromFile(CALORIE_FILE);
    List<HealthData> exerciseDataList = readDataFromFile(EXERCISE_FILE);
    List<HealthData> sleepDataList = readDataFromFile(SLEEP_FILE);

    // Calculate total calories consumed and burned
    int totalCaloriesConsumed = 0;
    int totalCaloriesBurned = 0;

    for (HealthData calorieData : calorieDataList) {
        totalCaloriesConsumed += ((CalorieIntakeData) calorieData).getCalories();
    }

    for (HealthData exerciseData : exerciseDataList) {
        totalCaloriesBurned += ((ExerciseData) exerciseData).getCaloriesBurned();
    }

    // Calculate total hours of sleep
    long totalSleepMinutes = 0;

    for (HealthData sleepData : sleepDataList) {
        LocalTime sleepTime = ((SleepData) sleepData).getSleepTime();
        LocalTime wakeUpTime = ((SleepData) sleepData).getWakeUpTime();
        long sleepMinutes = sleepTime.until(wakeUpTime, ChronoUnit.MINUTES);

        totalSleepMinutes += sleepMinutes;
    }

    long totalSleepHours = totalSleepMinutes / 60;
    long totalSleepMinutesRemainder = totalSleepMinutes % 60;

    // Calculate average daily caloric balance
    int averageCaloricBalance = (totalCaloriesConsumed - totalCaloriesBurned) / calorieDataList.size();

    // Determine the most common type of exercise
    Map<String, Integer> exerciseTypeCount = new HashMap<>();

    for (HealthData exerciseData : exerciseDataList) {
        String exerciseType = ((ExerciseData) exerciseData).getExerciseType();
        exerciseTypeCount.put(exerciseType, exerciseTypeCount.getOrDefault(exerciseType, 0) + 1);
    }

    String mostCommonExerciseType = null;
    int maxCount = 0;

    for (Map.Entry<String, Integer> entry : exerciseTypeCount.entrySet()) {
        String exerciseType = entry.getKey();
        int count = entry.getValue();

        if (count > maxCount) {
            mostCommonExerciseType = exerciseType;
            maxCount = count;
        }
    }

    // Print the health summary
    System.out.println("------ Health Summary ------");
    System.out.println("Total Calories Consumed: " + totalCaloriesConsumed);
    System.out.println("Total Calories Burned: " + totalCaloriesBurned);
    System.out.println("Total Sleep Time: " + totalSleepHours + " hours " + totalSleepMinutesRemainder + " minutes");
    System.out.println("Average Daily Caloric Balance: " + averageCaloricBalance);
    System.out.println("Most Common Exercise Type: " + mostCommonExerciseType);
    System.out.println("-----------------------------");
}

    private void writeDataToFile(String filename, HealthData data) {
        try {
            FileWriter fileWriter = new FileWriter(filename, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(data.toString());
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }

    private List<HealthData> readDataFromFile(String filename) {
        List<HealthData> dataList = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                HealthData data = parseHealthData(line);
                if (data != null) {
                    dataList.add(data);
                }
            }

            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("Error reading from file.");
        }
        return dataList;
    }

    private HealthData parseHealthData(String line) {
        String[] parts = line.split(",");
        if (parts.length < 3) {
            return null;
        }

        LocalDate date = LocalDate.parse(parts[0], DATE_FORMATTER);
        String type = parts[1];

        switch (type) {
            case "Calorie":
                int calories = Integer.parseInt(parts[2]);
                return new CalorieIntakeData(date, parts[3], calories);
            case "Exercise":
                int duration = Integer.parseInt(parts[2]);
                int caloriesBurned = Integer.parseInt(parts[3]);
                return new ExerciseData(date, parts[4], duration, caloriesBurned);
            case "Sleep":
                LocalTime sleepTime = LocalTime.parse(parts[2], TIME_FORMATTER);
                LocalTime wakeUpTime = LocalTime.parse(parts[3], TIME_FORMATTER);

                // Adjust wake-up time if it's earlier than the sleep time (indicating sleep extends into the next day)
                if (wakeUpTime.isBefore(sleepTime)) {
                    wakeUpTime = wakeUpTime.plusHours(24);
                }

                return new SleepData(date, sleepTime, wakeUpTime);
            default:
                return null;
        }
    }
}
