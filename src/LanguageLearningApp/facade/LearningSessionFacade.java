package LanguageLearningApp.facade;

import LanguageLearningApp.builder.LessonDirector;
import LanguageLearningApp.builder.LessonProduct;
import LanguageLearningApp.decorator.HintDecorator;
import LanguageLearningApp.decorator.TranslationDecorator;
import LanguageLearningApp.factory.creators.CreatorAbstract;
import LanguageLearningApp.factory.products.ExerciseInterface;
import LanguageLearningApp.models.Word;
import LanguageLearningApp.observer.subject.LearningEventPublisher;
import LanguageLearningApp.strategy.ReviewSchedulerContext;
import java.util.List;
import java.util.Objects;

public class LearningSessionFacade {
    private final LessonDirector lessonDirector;
    private final CreatorAbstract exerciseCreator;
    private final ReviewSchedulerContext reviewScheduler;
    private final LearningEventPublisher eventPublisher;

    public LearningSessionFacade(LessonDirector lessonDirector, CreatorAbstract exerciseCreator,
                                 ReviewSchedulerContext reviewScheduler, LearningEventPublisher eventPublisher) {
        this.lessonDirector = Objects.requireNonNull(lessonDirector);
        this.exerciseCreator = Objects.requireNonNull(exerciseCreator);
        this.reviewScheduler = Objects.requireNonNull(reviewScheduler);
        this.eventPublisher = Objects.requireNonNull(eventPublisher);
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
        return exerciseCreator.createExercises(lesson);
    }

    public ExerciseInterface decorateWithHint(ExerciseInterface exercise) {
        return new HintDecorator(exercise);
    }

    public ExerciseInterface decorateWithTranslation(ExerciseInterface exercise) {
        return new TranslationDecorator(exercise);
    }

    public boolean submitAnswer(ExerciseInterface exercise, String answer) {
        Objects.requireNonNull(exercise);
        boolean correct = exercise.checkAnswer(answer);
        Word word = exercise.getWord();
        eventPublisher.notifyObservers(correct ? "ANSWER_CORRECT" : "ANSWER_INCORRECT", word.getTerm());
        reviewScheduler.addWord(word);
        eventPublisher.notifyObservers("REVIEW_SCHEDULED", word.getTerm());
        return correct;
    }

    public void completeLesson(LessonProduct lesson) {
        Objects.requireNonNull(lesson);
        eventPublisher.notifyObservers("LESSON_COMPLETED", lesson.getTitle());
    }

    public List<Word> dueWords() {
        return reviewScheduler.dueWords();
    }

    public java.time.LocalDate review(Word word, int quality) {
        java.time.LocalDate nextReview = reviewScheduler.review(word, quality);
        eventPublisher.notifyObservers("REVIEW_SCHEDULED", word.getTerm());
        return nextReview;
    }

    public void setReviewStrategy(LanguageLearningApp.strategy.StrategyInterface strategy) {
        reviewScheduler.setStrategy(strategy);
    }
}
