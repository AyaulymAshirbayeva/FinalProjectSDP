package LanguageLearningApp.observer.observer;
public class ProgressTracker implements ObserverInterface {
    private int correctAnswers;
    private int incorrectAnswers;
    private int completedLessons;
    @Override
    public void update(String eventType, String word) {
        if (eventType.equals("ANSWER_CORRECT")) {
            correctAnswers++;
            System.out.println("Correct answer recorded!");
        } else if (eventType.equals("ANSWER_INCORRECT")) {
            incorrectAnswers++;
            System.out.println("Incorrect answer recorded!");
        } else if (eventType.equals("LESSON_COMPLETED")) {
            completedLessons++;
            System.out.println("Lesson completion recorded!");
        } else if (eventType.equals("REVIEW_SCHEDULED")) {
            System.out.println("Review added to progress!");
        }
    }
    public void displayProgress() {
        System.out.println("\n========== MY PROGRESS ==========");
        System.out.println("Correct answers: " + correctAnswers);
        System.out.println("Incorrect answers: " + incorrectAnswers);
        System.out.println("Completed lessons: " + completedLessons);
        System.out.println("=================================");
    }
}