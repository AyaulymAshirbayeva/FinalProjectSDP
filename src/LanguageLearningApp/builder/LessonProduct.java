package LanguageLearningApp.builder;

import LanguageLearningApp.models.Word;
import java.util.List;
import java.util.Objects;

public class LessonProduct {
    private final int id;
    private final String title;
    private final List<Word> vocabulary;

    public LessonProduct(int id, String title, List<Word> vocabulary) {
        if (id <= 0 || title == null || title.isBlank()) {
            throw new IllegalArgumentException("A lesson needs a positive id and a non-blank title.");
        }
        Objects.requireNonNull(vocabulary, "Vocabulary must not be null.");
        if (vocabulary.isEmpty()) {
            throw new IllegalArgumentException("A lesson needs vocabulary.");
        }
        this.id = id;
        this.title = title.strip();
        this.vocabulary = List.copyOf(vocabulary);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<Word> getVocabulary() {
        return vocabulary;
    }
}
