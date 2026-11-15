package LanguageLearningApp;

import LanguageLearningApp.builder.LessonDirector;
import LanguageLearningApp.builder.LessonProduct;
import LanguageLearningApp.builder.StandardLessonBuilder;
import LanguageLearningApp.facade.LearningSessionFacade;
import LanguageLearningApp.factory.creators.*;
import LanguageLearningApp.factory.products.ExerciseInterface;
import LanguageLearningApp.models.*;
import LanguageLearningApp.observer.Observer.ProgressTracker;
import LanguageLearningApp.observer.Observer.ReminderService;
import LanguageLearningApp.observer.subject.LearningEventPublisher;
import LanguageLearningApp.strategy.BasicIntervalStrategy;
import LanguageLearningApp.strategy.ReviewSchedulerContext;
import LanguageLearningApp.strategy.SM2Strategy;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in, StandardCharsets.UTF_8)) {
            System.out.println("=== Language Learning App ===");

            String languageChoice = readOption(input,
                    "Choose language to learn: 1. Russian  2. German  3. Chinese", "1", "2", "3");
            if (languageChoice == null) return;
            String targetLanguage;
            if (languageChoice.equals("1")) {
                targetLanguage = "Russian";
            } else if (languageChoice.equals("2")) {
                targetLanguage = "German";
            } else {
                targetLanguage = "Chinese";
            }

            System.out.print("Learner name (Enter for Guest): ");
            if (!input.hasNextLine()) return;
            String username = input.nextLine().strip();
            if (username.isEmpty()) username = "Guest";
            User user = new User(username);
            user.enrollLanguage(targetLanguage);

            String lessonChoice = readOption(input,
                    "Choose lesson: 1. Greetings  2. Travel", "1", "2");
            if (lessonChoice == null) return;

            String exerciseChoice = readOption(input,
                    "Choose exercise: 1. Multiple choice  2. Fill in the blank", "1", "2");
            if (exerciseChoice == null) return;

            String strategyChoice = readOption(input,
                    "Choose review strategy: 1. SM-2  2. Basic intervals", "1", "2");
            if (strategyChoice == null) return;

            LessonDirector director = new LessonDirector(new StandardLessonBuilder());
            CreatorAbstract exerciseCreator;
            if (exerciseChoice.equals("1")) {
                exerciseCreator = new MultipleChoiceCreator();
            } else {
                exerciseCreator = new FillBlankCreator();
            }

            LearningEventPublisher publisher = new LearningEventPublisher();
            ProgressTracker progress = new ProgressTracker();
            ReminderService reminders = new ReminderService();
            publisher.addObserver(progress);
            publisher.addObserver(reminders);

            ReviewSchedulerContext scheduler = new ReviewSchedulerContext(new SM2Strategy());
            LearningSessionFacade session = new LearningSessionFacade(
                    director, exerciseCreator, scheduler, publisher, user);
            if (strategyChoice.equals("2")) {
                session.setReviewStrategy(new BasicIntervalStrategy());
            }

            LessonProduct lesson = lessonChoice.equals("1")
                    ? session.createGreetingsLesson(targetLanguage)
                    : session.createTravelLesson(targetLanguage);
            Course course = new Course(targetLanguage);
            course.addLesson(lesson);

            System.out.println("\nLearner: " + user.getUsername());
            System.out.println("Language: " + targetLanguage);
            System.out.println("Course: " + course.getLanguageName());
            System.out.println("Lesson: " + lesson.getTitle());
            System.out.println("Exercise type: "
                    + (exerciseChoice.equals("1") ? "multiple choice" : "fill in the blank"));
            System.out.println("Review algorithm: "
                    + (strategyChoice.equals("1") ? "SM-2" : "Basic intervals"));

            List<ExerciseInterface> exercises = session.createExercises(lesson);
            for (ExerciseInterface exercise : exercises) {
                ExerciseInterface decorated = session.decorateWithHint(exercise);
                System.out.println("\n" + decorated.getPrompt());
                List<String> options = decorated.getOptions();
                for (int i = 0; i < options.size(); i++) {
                    System.out.println((i + 1) + ". " + options.get(i));
                }
                System.out.print("Your answer (or ? to reveal the English translation): ");
                if (!input.hasNextLine()) return;
                String answer = input.nextLine().strip();
                boolean translationShown = false;
                while (answer.equals("?")) {
                    if (!translationShown) {
                        decorated = session.revealTranslation(exercise);
                        translationShown = true;
                    }
                    System.out.println(decorated.getPrompt());
                    System.out.print("Your answer: ");
                    if (!input.hasNextLine()) return;
                    answer = input.nextLine().strip();
                }

                if (session.submitAnswer(decorated, answer)) {
                    System.out.println("Correct.");
                } else {
                    System.out.println("Not quite. Correct answer: " + exercise.getWord().getTerm());
                }
            }

            session.completeLesson(lesson);
            System.out.println("\nLesson result: " + progress.getCorrectAnswers() + "/" + exercises.size());

            List<Word> dueWords = session.dueWords();
            System.out.println("Words due for review: " + dueWords.size());
            for (Word word : dueWords) {
                System.out.println("Translate: " + word.getTranslation() + " (Enter to reveal the answer)");
                if (!input.hasNextLine()) return;
                input.nextLine();
                System.out.println("Answer: " + word.getTerm());
                Integer quality = readQuality(input);
                if (quality == null) return;
                System.out.println("Next review: " + session.review(word, quality));
            }

            System.out.println("\nCourse vocabulary: " + course.getVocabulary().size() + " words");
            System.out.println("User progress for " + user.getUsername() + ": "
                    + session.getCorrectAnswerCount() + " correct answers, "
                    + session.getCompletedLessonCount() + " completed lesson(s), "
                    + session.getLearnedWordCount() + " learned word(s).");
            progress.displayProgress();
            reminders.displayReminders();
        }
    }

    private static String readOption(Scanner input, String prompt, String... options) {
        while (true) {
            System.out.println(prompt);
            System.out.print("Your choice: ");
            if (!input.hasNextLine()) return null;
            String choice = input.nextLine().strip();
            for (String option : options) {
                if (option.equals(choice)) return choice;
            }
            System.out.println("Invalid choice. Please select one of the listed options.");
        }
    }

    private static Integer readQuality(Scanner input) {
        while (true) {
            System.out.print("Recall quality (0-5, Enter=4): ");
            if (!input.hasNextLine()) return null;
            String value = input.nextLine().strip();
            if (value.isEmpty()) return 4;
            if (value.matches("[0-5]")) return Integer.parseInt(value);
            System.out.println("Invalid quality. Enter a whole number from 0 to 5.");
        }
    }
}
