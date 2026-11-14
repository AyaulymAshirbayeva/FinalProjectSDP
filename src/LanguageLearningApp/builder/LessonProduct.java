package LanguageLearningApp.builder;

import LanguageLearningApp.models.Word;
import java.util.List;

public class LessonProduct {
    private final int id;
    private final String title;
    private final List<Word> vocabulary;

    public LessonProduct(int id, String title, List<Word> vocabulary) {
        this.id = id;
        this.title = title;
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
