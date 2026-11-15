package language_learning_app.observer.concrete_observers;
import java.util.LinkedHashSet;
import java.util.Set;

public class ReminderService implements ObserverInterface {
    private final Set<String> reminders = new LinkedHashSet<>();

    @Override
    public void update(String eventType, String word) {
        if (eventType.equals("REVIEW_SCHEDULED")) {
            reminders.add("Review word: " + word);
        }
    }
    public void displayReminders() {
        System.out.println("\n========== SCHEDULED REVIEW WORDS ==========");
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
