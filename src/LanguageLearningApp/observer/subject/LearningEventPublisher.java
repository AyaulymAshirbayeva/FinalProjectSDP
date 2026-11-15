package LanguageLearningApp.observer.subject;
import LanguageLearningApp.observer.observer.ObserverInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LearningEventPublisher implements SubjectInterface {
    private final List<ObserverInterface> observers = new ArrayList<>();
    @Override
    public void addObserver(ObserverInterface observer) {
        Objects.requireNonNull(observer, "Observer must not be null.");
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }
    @Override
    public void removeObserver(ObserverInterface observer) {
        observers.remove(observer);
    }
    @Override
    public void notifyObservers(String eventType, String word) {
        Objects.requireNonNull(eventType, "Event type must not be null.");
        for (ObserverInterface observer : List.copyOf(observers)) {
            observer.update(eventType, word);
        }
    }
}
