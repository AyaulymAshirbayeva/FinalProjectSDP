package LanguageLearningApp.models;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class User {
    private final String username;
    private final String password;
    private final Set<String> activeLanguages = new LinkedHashSet<>();
    private final List<Word> learnedWords = new ArrayList<>();
    private int completedLessons = 0;
    private int correctAnswersCount = 0;

    public User(String username) {
        this.username = Objects.requireNonNull(username);
        this.password = null;
    }

    public User(String username, String password) {
        this.username = Objects.requireNonNull(username);
        this.password = Objects.requireNonNull(password);
    }

    public String getUsername() {
        return username;
    }
    public boolean checkPassword(String inputPassword) {
        return password != null && password.equals(inputPassword);
    }
    public void enrollLanguage(String language) {
        activeLanguages.add(Objects.requireNonNull(language));
    }
    public Set<String> getActiveLanguages() {
        return Set.copyOf(activeLanguages);
    }
    public void addLearnedWord(Word word) {
        Objects.requireNonNull(word);
        if (!learnedWords.contains(word)) {
            learnedWords.add(word);
        }
    }

    public List<Word> getLearnedWords() {
        return List.copyOf(learnedWords);
    }
    public int getCompletedLessons() {
        return completedLessons;
    }
    public void incrementCompletedLessons() {
        this.completedLessons++;
    }
    public int getCorrectAnswersCount() {
        return correctAnswersCount;
    }
    public void incrementCorrectAnswers() {
        this.correctAnswersCount++;
    }
}
