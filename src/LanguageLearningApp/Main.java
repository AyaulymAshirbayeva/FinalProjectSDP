package LanguageLearningApp;

import LanguageLearningApp.observer.observer.ProgressTracker;
import LanguageLearningApp.observer.observer.ReminderService;
import LanguageLearningApp.observer.subject.LearningEventPublisher;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        LearningEventPublisher publisher = new LearningEventPublisher();

        ProgressTracker progressTracker = new ProgressTracker();
        ReminderService reminderService = new ReminderService();

        publisher.addObserver(progressTracker);
        publisher.addObserver(reminderService);

        System.out.println("========== LANGUAGE LEARNING APP ==========");
        System.out.println("Choose a language to learn:");
        System.out.println("1. Chinese");
        System.out.println("2. Russian");
        System.out.println("3. German");

        System.out.print("Your choice: ");
        int choice = scanner.nextInt();

        String selectedLanguage = "";

        if (choice == 1) {
            selectedLanguage = "Chinese";
        } else if (choice == 2) {
            selectedLanguage = "Russian";
        } else if (choice == 3) {
            selectedLanguage = "German";
        } else {
            System.out.println("Invalid choice!");
            scanner.close();
            return;
        }

        System.out.println("\nYou selected: " + selectedLanguage);
        System.out.println("Your learning session has started!");

        // Observer demonstration
        publisher.notifyObservers("ANSWER_CORRECT", selectedLanguage);
        publisher.notifyObservers("ANSWER_INCORRECT", selectedLanguage);
        publisher.notifyObservers("LESSON_COMPLETED", selectedLanguage);
        publisher.notifyObservers("REVIEW_SCHEDULED", selectedLanguage);

        progressTracker.displayProgress();
        reminderService.displayReminders();

        scanner.close();
    }
}