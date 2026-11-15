package language_learning_app.facade;

import language_learning_app.builder.LessonDirector;
import language_learning_app.builder.LessonProduct;
import language_learning_app.decorator.HintDecorator;
import language_learning_app.decorator.TranslationDecorator;
import language_learning_app.factory.creators.CreatorAbstract;
import language_learning_app.factory.products.ExerciseInterface;
import language_learning_app.models.User;
import language_learning_app.models.Word;
import language_learning_app.observer.subject.LearningEventPublisher;
import language_learning_app.strategy.ReviewSchedulerContext;
import language_learning_app.strategy.StrategyInterface;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class LearningSessionFacade {
    private final LessonDirector lessonDirector;
    private final CreatorAbstract exerciseCreator;
    private final ReviewSchedulerContext reviewScheduler;
    private final LearningEventPublisher eventPublisher;
    private final User user;
    private int correctAnswerCount;
    private int completedLessonCount;
    private LessonProduct activeLesson;
    private final Set<Word> pendingWords = new HashSet<>();

    public LearningSessionFacade(LessonDirector lessonDirector, CreatorAbstract exerciseCreator,
                                 ReviewSchedulerContext reviewScheduler, LearningEventPublisher eventPublisher,
                                 User user) {
        this.lessonDirector = Objects.requireNonNull(lessonDirector);
        this.exerciseCreator = Objects.requireNonNull(exerciseCreator);
        this.reviewScheduler = Objects.requireNonNull(reviewScheduler);
        this.eventPublisher = Objects.requireNonNull(eventPublisher);
        this.user = Objects.requireNonNull(user);
    }

    public LessonProduct createGreetingsLesson() {
        return lessonDirector.greetings();
    }

    public LessonProduct createGreetingsLesson(String targetLanguage) {
        return lessonDirector.greetings(targetLanguage);
    }

    public LessonProduct createTravelLesson(String targetLanguage) {
        return lessonDirector.travel(targetLanguage);
    }

    public List<ExerciseInterface> createExercises(LessonProduct lesson) {
        if (activeLesson != null) {
            throw new IllegalStateException("Complete the current lesson before starting another.");
        }
        List<ExerciseInterface> exercises = exerciseCreator.createExercises(lesson);
        Set<Word> words = new HashSet<>(lesson.getVocabulary());
        if (words.size() != exercises.size()) {
            throw new IllegalArgumentException("A lesson must not repeat the same Word instance.");
        }
        activeLesson = lesson;
        pendingWords.addAll(words);
        return exercises;
    }

    public ExerciseInterface decorateWithHint(ExerciseInterface exercise) {
        return new HintDecorator(exercise);
    }

    public ExerciseInterface decorateWithTranslation(ExerciseInterface exercise) {
        return new TranslationDecorator(exercise);
    }

    public ExerciseInterface revealTranslation(ExerciseInterface exercise) {
        return decorateWithTranslation(decorateWithHint(exercise));
    }

    public boolean submitAnswer(ExerciseInterface exercise, String answer) {
        Objects.requireNonNull(exercise);
        Word word = exercise.getWord();
        if (activeLesson == null || !pendingWords.contains(word)) {
            throw new IllegalStateException("This word is not awaiting an answer in the current lesson.");
        }
        boolean correct = exercise.checkAnswer(answer);
        boolean addedForReview = reviewScheduler.addWord(word);
        pendingWords.remove(word);
        if (correct) {
            correctAnswerCount++;
            user.addLearnedWord(word);
        }
        eventPublisher.notifyObservers(correct ? "ANSWER_CORRECT" : "ANSWER_INCORRECT", word.getTerm());
        if (addedForReview) {
            notifyReviewScheduled(word);
        }
        return correct;
    }

    public void completeLesson(LessonProduct lesson) {
        Objects.requireNonNull(lesson);
        if (activeLesson != lesson || !pendingWords.isEmpty()) {
            throw new IllegalStateException("Answer every exercise in the active lesson before completing it.");
        }
        activeLesson = null;
        completedLessonCount++;
        eventPublisher.notifyObservers("LESSON_COMPLETED", lesson.getTitle());
    }

    public List<Word> dueWords() {
        return reviewScheduler.dueWords();
    }

    public LocalDate review(Word word, int quality) {
        LocalDate nextReview = reviewScheduler.review(word, quality);
        if (quality >= 3) {
            user.addLearnedWord(word);
        }
        notifyReviewScheduled(word);
        return nextReview;
    }

    public int getCorrectAnswerCount() {
        return correctAnswerCount;
    }

    public int getCompletedLessonCount() {
        return completedLessonCount;
    }

    public int getLearnedWordCount() {
        return user.getLearnedWords().size();
    }

    public void setReviewStrategy(StrategyInterface strategy) {
        reviewScheduler.setStrategy(strategy);
    }

    private void notifyReviewScheduled(Word word) {
        eventPublisher.notifyObservers("REVIEW_SCHEDULED", word.getLanguage() + ": " + word.getTerm());
    }
}
