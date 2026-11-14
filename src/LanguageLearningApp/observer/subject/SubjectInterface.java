package LanguageLearningApp.observer.subject;
import LanguageLearningApp.observer.observer.ObserverInterface;

public interface SubjectInterface {
    void addObserver(ObserverInterface observer);
    void removeObserver(ObserverInterface observer);
    void notifyObservers(String eventType, String word);
}
