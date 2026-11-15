package language_learning_app.observer.subject;
import language_learning_app.observer.concrete_observers.ObserverInterface;

public interface SubjectInterface {
    void addObserver(ObserverInterface observer);
    void removeObserver(ObserverInterface observer);
    void notifyObservers(String eventType, String word);
}
