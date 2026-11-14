package LanguageLearningApp.facade;

import LanguageLearningApp.builder.LessonDirector;
import LanguageLearningApp.factory.creators.CreatorAbstract;
import LanguageLearningApp.strategy.ReviewSchedulerContext;
import LanguageLearningApp.observer.subject.LearningEventPublisher;

public class LearningSessionFacade {
    private LessonDirector lessonDirector;
    private CreatorAbstract exerciseCreator;
    private ReviewSchedulerContext reviewScheduler;
    private LearningEventPublisher eventPublisher;

    public LearningSessionFacade(LessonDirector lessonDirector, CreatorAbstract exerciseCreator,
                                 ReviewSchedulerContext reviewScheduler, LearningEventPublisher eventPublisher) {
        this.lessonDirector = lessonDirector;
        this.exerciseCreator = exerciseCreator;
        this.reviewScheduler = reviewScheduler;
        this.eventPublisher = eventPublisher;
    }
}

