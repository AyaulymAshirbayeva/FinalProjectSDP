package language_learning_app.factory.creators;

import language_learning_app.builder.LessonProduct;
import language_learning_app.factory.products.ExerciseInterface;
import language_learning_app.models.Word;
import java.util.List;
import java.util.Objects;

public abstract class CreatorAbstract {
    public abstract ExerciseInterface createExercise(Word word, List<Word> vocabulary);

    public final List<ExerciseInterface> createExercises(LessonProduct lesson) {
        Objects.requireNonNull(lesson, "Lesson must not be null.");
        if (lesson.getVocabulary().isEmpty()) {
            throw new IllegalArgumentException("A lesson needs vocabulary to create exercises.");
        }
        return lesson.getVocabulary().stream()
                .map(word -> createExercise(word, lesson.getVocabulary()))
                .toList();
    }
}
