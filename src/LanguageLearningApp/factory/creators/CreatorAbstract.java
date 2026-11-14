package LanguageLearningApp.factory.creators;

import LanguageLearningApp.builder.LessonProduct;
import LanguageLearningApp.factory.products.ExerciseInterface;
import LanguageLearningApp.models.Word;
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
