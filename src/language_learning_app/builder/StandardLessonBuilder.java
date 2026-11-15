package language_learning_app.builder;

import language_learning_app.models.Word;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StandardLessonBuilder implements BuilderInterface {
    private int id;
    private String title;
    private final List<Word> vocabulary = new ArrayList<>();

    @Override
    public BuilderInterface start(int id, String title) {
        if (id <= 0 || title == null || title.isBlank()) {
            throw new IllegalArgumentException("A lesson needs a positive id and a non-blank title.");
        }
        this.title = title.strip();
        this.id = id;
        vocabulary.clear();
        return this;
    }

    @Override
    public BuilderInterface addWord(Word word) {
        if (title == null) {
            throw new IllegalStateException("Start a lesson before adding words.");
        }
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
