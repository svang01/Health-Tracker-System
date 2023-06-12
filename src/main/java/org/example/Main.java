package org.example;

import org.example.Data.HealthDataManager;
import org.example.User.User;
import org.example.User.UserManager;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserManager userManager = new UserManager();
        HealthDataManager healthDataManager = new HealthDataManager();
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            // Create new user with the provided username and password
            User newUser = userManager.createUser(username, password);
            System.out.println("User created: " + newUser.getUsername());

            // Log in with the existing username and password
            System.out.print("Enter username to log in: ");
            String loginUsername = scanner.nextLine();

            System.out.print("Enter password to log in: ");
            String loginPassword = scanner.nextLine();

            User loggedInUser = userManager.logIn(loginUsername, loginPassword);
            if (loggedInUser != null) {
                System.out.println("Logged in as: " + loggedInUser.getUsername());

                while (true) {
                    System.out.println("Select an option:");
                    System.out.println("1. Add Calorie Intake");
                    System.out.println("2. Add Exercise Activity");
                    System.out.println("3. Add Sleep Record");
                    System.out.println("4. Analyze Daily Caloric Balance");
                    System.out.println("5. Analyze Sleep Data");
                    System.out.println("6. Analyze Exercise Log");
                    System.out.println("7. Analyze Health Summary");
                    System.out.println("8. Exit");

                    int option = scanner.nextInt();
                    scanner.nextLine(); // Consume remaining newline character

                    switch (option) {
                        case 1:
                            healthDataManager.addCalorieIntake(scanner);
                            break;
                        case 2:
                            healthDataManager.addExerciseActivity(scanner);
                            break;
                        case 3:
                            healthDataManager.addSleepRecord(scanner);
                            break;
                        case 4:
                            healthDataManager.analyzeDailyCaloricBalance();
                            break;
                        case 5:
                            healthDataManager.analyzeSleepData(scanner);
                            break;
                        case 6:
                            healthDataManager.analyzeExerciseLog();
                            break;
                        case 7:
                            healthDataManager.analyzeHealthSummary();
                            break;
                        case 8:
                            System.out.println("Exiting the application...");
                            return;
                        default:
                            System.out.println("Invalid option");
                            break;
                    }
                }
            } else {
                System.out.println("Invalid username or password");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}

