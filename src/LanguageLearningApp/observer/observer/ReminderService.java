package LanguageLearningApp.observer.observer;
import java.util.ArrayList;
import java.util.List;

public class ReminderService implements ObserverInterface {
    private List<String> reminders = new ArrayList<>();

    @Override
    public void update(String eventType, String word) {
        if (eventType.equals("REVIEW_SCHEDULED")) {
            reminders.add("Review word: " + word);
            System.out.println("Reminder added for: " + word);

        } else if (eventType.equals("LESSON_COMPLETED")) {
            System.out.println("Don't forget to review your lesson!");

        }
    }
    public void displayReminders() {
        System.out.println("\n========== MY REMINDERS ==========");
        if (reminders.isEmpty()) {
            System.out.println("No reminders yet.");
        } else {
            for (String reminder : reminders) {
                System.out.println(reminder);
            }
        }
        System.out.println("==================================");
    }
}