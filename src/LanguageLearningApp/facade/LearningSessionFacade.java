package LanguageLearningApp.facade;

import LanguageLearningApp.builder.LessonDirector;
import LanguageLearningApp.builder.LessonProduct;
import LanguageLearningApp.decorator.HintDecorator;
import LanguageLearningApp.decorator.TranslationDecorator;
import LanguageLearningApp.factory.creators.CreatorAbstract;
import LanguageLearningApp.factory.products.ExerciseInterface;
import LanguageLearningApp.models.User;
import LanguageLearningApp.models.Word;
import LanguageLearningApp.observer.subject.LearningEventPublisher;
import LanguageLearningApp.strategy.ReviewSchedulerContext;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class LearningSessionFacade {
    private final LessonDirector lessonDirector;
    private final CreatorAbstract exerciseCreator;
    private final ReviewSchedulerContext reviewScheduler;
    private final LearningEventPublisher eventPublisher;
    private final User user;
    private LessonProduct activeLesson;
    private List<ExerciseInterface> activeExercises = List.of();
    private boolean lessonCompleted;
    private final Set<ExerciseInterface> answeredExercises =
            Collections.newSetFromMap(new IdentityHashMap<>());

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
        activeLesson = Objects.requireNonNull(lesson);
        activeExercises = exerciseCreator.createExercises(lesson);
        answeredExercises.clear();
        lessonCompleted = false;
        return activeExercises;
    }

    public ExerciseInterface decorateWithHint(ExerciseInterface exercise) {
        return new HintDecorator(exercise);
    }

    public ExerciseInterface revealTranslation(ExerciseInterface exercise) {
        return new TranslationDecorator(new HintDecorator(exercise));
    }

    public boolean submitAnswer(ExerciseInterface exercise, String answer) {
        Objects.requireNonNull(exercise);
        if (!activeExercises.contains(exercise)) {
            throw new IllegalArgumentException("Exercise does not belong to the active lesson.");
        }
        if (answeredExercises.contains(exercise)) {
            throw new IllegalStateException("This exercise has already been answered.");
        }

        boolean correct = exercise.checkAnswer(answer);
        Word word = exercise.getWord();
        answeredExercises.add(exercise);
        if (correct) {
            user.incrementCorrectAnswers();
            user.addLearnedWord(word);
        }
        eventPublisher.notifyObservers(correct ? "ANSWER_CORRECT" : "ANSWER_INCORRECT", word.getTerm());
        reviewScheduler.addWord(word);
        eventPublisher.notifyObservers("REVIEW_SCHEDULED", word.getTerm());
        return correct;
    }

    public void completeLesson(LessonProduct lesson) {
        Objects.requireNonNull(lesson);
        if (lesson != activeLesson) {
            throw new IllegalArgumentException("Lesson is not the active lesson.");
        }
        if (lessonCompleted) {
            throw new IllegalStateException("This lesson has already been completed.");
        }
        if (answeredExercises.size() != activeExercises.size()) {
            throw new IllegalStateException("Every exercise must be answered before completing the lesson.");
        }
        lessonCompleted = true;
        user.incrementCompletedLessons();
        eventPublisher.notifyObservers("LESSON_COMPLETED", lesson.getTitle());
    }

    public List<Word> dueWords() {
        return reviewScheduler.dueWords();
    }

    public java.time.LocalDate review(Word word, int quality) {
        java.time.LocalDate nextReview = reviewScheduler.review(word, quality);
        if (quality >= 3) {
            user.addLearnedWord(word);
        }
        eventPublisher.notifyObservers("REVIEW_SCHEDULED", word.getTerm());
        return nextReview;
    }

    public int getCorrectAnswerCount() {
        return user.getCorrectAnswersCount();
    }

    public int getCompletedLessonCount() {
        return user.getCompletedLessons();
    }

    public int getLearnedWordCount() {
        return user.getLearnedWords().size();
    }

    public void setReviewStrategy(LanguageLearningApp.strategy.StrategyInterface strategy) {
        reviewScheduler.setStrategy(strategy);
    }
}
