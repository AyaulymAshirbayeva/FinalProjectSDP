package LanguageLearningApp.builder;

import LanguageLearningApp.models.Word;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StandardLessonBuilder implements BuilderInterface {
    private int id;
    private String title;
    private final List<Word> vocabulary = new ArrayList<>();

    @Override
    public BuilderInterface start(int id, String title) {
        this.title = Objects.requireNonNull(title);
        this.id = id;
        vocabulary.clear();
        return this;
    }

    @Override
    public BuilderInterface addWord(Word word) {
        vocabulary.add(Objects.requireNonNull(word));
        return this;
    }

    @Override
    public LessonProduct build() {
        if (title == null || title.isBlank() || vocabulary.isEmpty()) {
            throw new IllegalStateException("A lesson needs a title and vocabulary.");
        }
        return new LessonProduct(id, title, vocabulary);
    }
}
