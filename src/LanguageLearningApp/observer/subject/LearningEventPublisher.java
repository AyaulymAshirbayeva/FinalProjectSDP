package LanguageLearningApp.observer.subject;
import LanguageLearningApp.observer.observer.ObserverInterface;

import java.util.ArrayList;
import java.util.List;

public class LearningEventPublisher implements SubjectInterface {
    private List<ObserverInterface> observers = new ArrayList<>();
    @Override
    public void addObserver(ObserverInterface observer) {
        observers.add(observer);
    }
    @Override
    public void removeObserver(ObserverInterface observer) {
        observers.remove(observer);
    }
    @Override
    public void notifyObservers(String eventType, String word) {
        for (ObserverInterface observer : observers) {
            observer.update(eventType, word);
        }
    }
}
