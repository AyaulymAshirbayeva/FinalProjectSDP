package LanguageLearningApp.models;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class User {
    private final String username;
    private final Set<String> activeLanguages = new LinkedHashSet<>();
    private final List<Word> learnedWords = new ArrayList<>();

    public User(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Learner name must not be blank.");
        }
        this.username = username.strip();
    }

    public String getUsername() {
        return username;
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
}
