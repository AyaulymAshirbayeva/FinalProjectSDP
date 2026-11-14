import LanguageLearningApp.builder.LessonDirector;
import LanguageLearningApp.builder.LessonProduct;
import LanguageLearningApp.builder.StandardLessonBuilder;
import LanguageLearningApp.facade.LearningSessionFacade;
import LanguageLearningApp.factory.creators.CreatorAbstract;
import LanguageLearningApp.factory.creators.MultipleChoiceCreator;
import LanguageLearningApp.factory.products.ExerciseInterface;
import LanguageLearningApp.models.Word;
import LanguageLearningApp.observer.observer.ProgressTracker;
import LanguageLearningApp.observer.observer.ReminderService;
import LanguageLearningApp.observer.subject.LearningEventPublisher;
import LanguageLearningApp.strategy.ReviewSchedulerContext;
import LanguageLearningApp.strategy.SM2Strategy;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        LessonDirector director = new LessonDirector(new StandardLessonBuilder());
        CreatorAbstract creator = new MultipleChoiceCreator();
        ReviewSchedulerContext scheduler = new ReviewSchedulerContext(new SM2Strategy());
        LearningEventPublisher publisher = new LearningEventPublisher();
        ProgressTracker progress = new ProgressTracker();
        ReminderService reminders = new ReminderService();
        publisher.addObserver(progress);
        publisher.addObserver(reminders);
        LearningSessionFacade session = new LearningSessionFacade(director, creator, scheduler, publisher);

        try (Scanner input = new Scanner(System.in)) {
            System.out.println("=== Language Learning App ===");
            System.out.println("Выберите язык для изучения:");
            System.out.println("1. Русский  2. Deutsch  3. 中文");
            System.out.print("Ваш выбор: ");
            if (!input.hasNextLine()) return;
            String choice = input.nextLine().strip();
            String targetLanguage = switch (choice) {
                case "1" -> "Russian";
                case "2" -> "German";
                case "3" -> "Chinese";
                default -> null;
            };
            if (targetLanguage == null) {
                System.out.println("Неверный выбор. Перезапустите приложение и выберите 1, 2 или 3.");
                return;
            }
            System.out.println("Вы выбрали: " + targetLanguage + ". Перевод вопросов будет на английском.");

            System.out.println("[Builder] Building the Greetings lesson...");
            LessonProduct lesson = session.createGreetingsLesson(targetLanguage);
            System.out.println("Lesson: " + lesson.getTitle() + " (" + lesson.getVocabulary().size() + " words)");

            System.out.println("[Factory Method] Creating multiple-choice exercises...");
            List<ExerciseInterface> exercises = session.createExercises(lesson);
            int correct = 0;
            for (ExerciseInterface exercise : exercises) {
                ExerciseInterface decorated = session.decorateWithHint(exercise);
                System.out.println("\n[Decorator] HintDecorator added to the exercise prompt.");
                System.out.println(decorated.getPrompt());
                List<String> options = decorated.getOptions();
                for (int i = 0; i < options.size(); i++) {
                    System.out.println((i + 1) + ". " + options.get(i));
                }
                System.out.print("Answer (number, or ? to reveal translation): ");
                if (!input.hasNextLine()) return;
                String answer = input.nextLine().strip();
                if (answer.equals("?")) {
                    ExerciseInterface translated = session.decorateWithTranslation(exercise);
                    System.out.println("[Decorator] TranslationDecorator added:");
                    System.out.println(translated.getPrompt());
                    System.out.print("Answer: ");
                    if (!input.hasNextLine()) return;
                    answer = input.nextLine().strip();
                }
                System.out.println("[Facade] Submitting answer; Observer records progress and Strategy queues review.");
                if (session.submitAnswer(exercise, answer)) {
                    correct++;
                    System.out.println("Correct.");
                } else {
                    System.out.println("Not quite. Correct answer: " + exercise.getWord().getTerm());
                }
            }
            session.completeLesson(lesson);
            System.out.println("\nLesson result: " + correct + "/" + exercises.size());

            List<Word> dueWords = session.dueWords();
            System.out.println("[Strategy: SM-2] Words due now: " + dueWords.size());
            for (Word word : dueWords) {
                System.out.println("Review: " + word.getTranslation() + " → " + word.getTerm());
                System.out.print("Recall quality (0-5, Enter=4): ");
                if (!input.hasNextLine()) return;
                String qualityText = input.nextLine().strip();
                int quality = qualityText.isEmpty() ? 4 : parseQuality(qualityText);
                System.out.println("Next review: " + session.review(word, quality));
            }
        }
        progress.displayProgress();
        reminders.displayReminders();
    }

    private static int parseQuality(String value) {
        try {
            int quality = Integer.parseInt(value);
            if (quality >= 0 && quality <= 5) return quality;
        } catch (NumberFormatException ignored) {
        }
        System.out.println("Invalid quality; using 4.");
        return 4;
    }
}
