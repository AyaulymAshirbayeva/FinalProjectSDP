package LanguageLearningApp.observer.Observer;
public class ProgressTracker implements ObserverInterface {
    private int correctAnswers;
    private int incorrectAnswers;
    private int completedLessons;
    @Override
    public void update(String eventType, String word) {
        if (eventType.equals("ANSWER_CORRECT")) {
            correctAnswers++;
        } else if (eventType.equals("ANSWER_INCORRECT")) {
            incorrectAnswers++;
        } else if (eventType.equals("LESSON_COMPLETED")) {
            completedLessons++;
        }
    }
    public int getCorrectAnswers() {
        return correctAnswers;
    }
    public int getIncorrectAnswers() {
        return incorrectAnswers;
    }
    public int getCompletedLessons() {
        return completedLessons;
    }
    public void displayProgress() {
        System.out.println("\n========== MY PROGRESS ==========");
        System.out.println("Correct answers: " + correctAnswers);
        System.out.println("Incorrect answers: " + incorrectAnswers);
        System.out.println("Completed lessons: " + completedLessons);
        System.out.println("=================================");
    }
}
